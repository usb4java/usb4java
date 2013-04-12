/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.usb.UsbClaimException;
import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbPort;
import javax.usb.UsbStringDescriptor;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;
import javax.usb.util.DefaultUsbControlIrp;

import de.ailis.usb4java.Services;
import de.ailis.usb4java.descriptors.SimpleUsbStringDescriptor;
import de.ailis.usb4java.exceptions.Usb4JavaException;
import de.ailis.usb4java.exceptions.Usb4JavaRuntimeException;
import de.ailis.usb4java.libusb.ConfigDescriptor;
import de.ailis.usb4java.libusb.Device;
import de.ailis.usb4java.libusb.DeviceHandle;
import de.ailis.usb4java.libusb.LibUSB;
import de.ailis.usb4java.support.ControlIrpQueue;
import de.ailis.usb4java.support.UsbDeviceListenerList;

/**
 * A Usb device.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class Usb4JavaDevice implements UsbDevice
{
    /** The USB device manager. */
    private final UsbDeviceManager manager;

    /** The device id. */
    private final DeviceId id;

    /** The parent id. Null if no parent exists. */
    private final DeviceId parentId;

    /** The device speed. */
    private final int speed;

    /** The device configurations. */
    private List<Usb4JavaConfiguration> configurations;

    /** Mapping from configuration value to configuration. */
    private Map<Byte, Usb4JavaConfiguration> configMapping =
        new HashMap<Byte, Usb4JavaConfiguration>();

    /** The USB device listener list. */
    private final UsbDeviceListenerList listeners = new UsbDeviceListenerList();

    /** The device handle. Null if not open. */
    private DeviceHandle handle;

    /** The number of the currently active configuration. */
    private byte activeConfigurationNumber = 0;

    /** The number of the currently claimed interface. */
    private Byte claimedInterfaceNumber = null;

    /** The port this device is connected to. */
    private UsbPort port;

    /** The IRP queue. */
    private final ControlIrpQueue queue = new ControlIrpQueue(this,
        this.listeners);

    /** If kernel driver was detached when interface was claimed. */
    private boolean detachedKernelDriver;

    /**
     * Constructs a new device.
     * 
     * @param manager
     *            The USB device manager which is responsible for this device.
     * @param id
     *            The device id. Must not be null.
     * @param parentId
     *            The parent device id. May be null if this device has no parent
     *            (Because it is a root device).
     * @param speed
     *            The device speed.
     * @param device
     *            The libusb device. This reference is only valid during the
     *            constructor execution, so don't store it in a property or
     *            something like that.
     * @throws Usb4JavaException
     *             When device configuration could not be read.
     */
    Usb4JavaDevice(final UsbDeviceManager manager, final DeviceId id,
        final DeviceId parentId, final int speed, final Device device)
        throws Usb4JavaException
    {
        if (manager == null)
            throw new IllegalArgumentException("manager must be set");
        if (id == null) throw new IllegalArgumentException("id must be set");
        this.manager = manager;
        this.id = id;
        this.parentId = parentId;
        this.speed = speed;

        // Read device configurations
        final int numConfigurations =
            id.getDeviceDescriptor().bNumConfigurations() & 0xff;
        final List<Usb4JavaConfiguration> configurations =
            new ArrayList<Usb4JavaConfiguration>(numConfigurations);
        for (int i = 0; i < numConfigurations; i += 1)
        {
            ConfigDescriptor configDescriptor = new ConfigDescriptor();
            int result =
                LibUSB.getConfigDescriptor(device, i, configDescriptor);
            if (result < 0)
                throw new Usb4JavaException("Unable to get configuation " + i
                    + " for device " + id);
            try
            {
                Usb4JavaConfiguration config =
                    new Usb4JavaConfiguration(this,
                        configDescriptor);
                configurations.add(config);
                this.configMapping.put(configDescriptor.bConfigurationValue(),
                    config);
            }
            finally
            {
                LibUSB.freeConfigDescriptor(configDescriptor);
            }
        }
        this.configurations = Collections.unmodifiableList(configurations);

        // Determine the active configuration number
        final ConfigDescriptor configDescriptor = new ConfigDescriptor();
        final int result =
            LibUSB.getActiveConfigDescriptor(device, configDescriptor);
        if (result < 0)
            throw new Usb4JavaException(
                "Unable to read active config descriptor from device " + id,
                result);
        this.activeConfigurationNumber = configDescriptor.bConfigurationValue();
        LibUSB.freeConfigDescriptor(configDescriptor);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Usb4JavaDevice other = (Usb4JavaDevice) obj;
        return this.id.equals(other.id);
    }

    /**
     * Returns the device id.
     * 
     * @return The device id.
     */
    public DeviceId getId()
    {
        return this.id;
    }

    /**
     * Returns the parent device id.
     * 
     * @return The parent device id or null of there is no parent.
     */
    public DeviceId getParentId()
    {
        return this.parentId;
    }

    /**
     * Ensures the device is connected.
     * 
     * @throws UsbDisconnectedException
     *             When device is disconnected.
     */
    final void checkConnected()
    {
        if (this.port == null) throw new UsbDisconnectedException();
    }

    /**
     * Opens the USB device and returns the USB device handle. If device was
     * already open then the old handle is returned.
     * 
     * @return The USB device handle.
     * @throws UsbException
     *             When USB device could not be opened.
     */
    public final DeviceHandle open() throws UsbException
    {
        if (this.handle == null)
        {
            final Device device = this.manager.getLibUsbDevice(this.id);
            try
            {
                DeviceHandle handle = new DeviceHandle();
                int result = LibUSB.open(device, handle);
                if (result < 0)
                {
                    throw new Usb4JavaException("Can't open device "
                        + this.id, result);
                }
                this.handle = handle;
            }
            finally
            {
                this.manager.releaseDevice(device);
            }
        }
        return this.handle;
    }

    /**
     * Closes the device. If device is not open then nothing is done.
     */
    public final void close()
    {
        if (this.handle != null)
        {
            LibUSB.close(this.handle);
            this.handle = null;
        }
    }

    /**
     * @see UsbDevice#getParentUsbPort()
     */
    @Override
    public UsbPort getParentUsbPort()
    {
        checkConnected();
        return this.port;
    }

    /**
     * Sets the parent USB port. If port is unset then a usbDeviceDetached event
     * is send.
     * 
     * @param port
     *            The port to set. Null to unset.
     */
    final void setParentUsbPort(final UsbPort port)
    {
        if (this.port == null && port == null)
            throw new IllegalStateException("Device already detached");
        if (this.port != null && port != null)
            throw new IllegalStateException("Device already attached");

        // Disconnect client devices
        if (port == null && isUsbHub())
        {
            final Usb4JavaHub hub = (Usb4JavaHub) this;
            for (final Usb4JavaDevice device: hub.getAttachedUsbDevices())
            {
                hub.disconnectUsbDevice(device);
            }
        }

        this.port = port;

        final Services services;
        try
        {
            services = (Services) UsbHostManager.getUsbServices();
        }
        catch (final UsbException e)
        {
            // Can't happen. When we got here then USB services are already
            // loaded
            throw new Usb4JavaRuntimeException(e.toString(), e);
        }

        if (port == null)
        {
            this.listeners.usbDeviceDetached(new UsbDeviceEvent(this));
            services.usbDeviceDetached(this);
        }
        else
        {
            services.usbDeviceAttached(this);
        }
    }

    /**
     * @see UsbDevice#getManufacturerString()
     */
    @Override
    public String getManufacturerString() throws UsbException,
        UnsupportedEncodingException
    {
        checkConnected();
        final byte index = getUsbDeviceDescriptor().iManufacturer();
        if (index == 0) return null;
        return getString(index);
    }

    /**
     * @see UsbDevice#getSerialNumberString()
     */
    @Override
    public String getSerialNumberString() throws UsbException,
        UnsupportedEncodingException
    {
        checkConnected();
        final byte index = getUsbDeviceDescriptor().iSerialNumber();
        if (index == 0) return null;
        return getString(index);
    }

    /**
     * @see UsbDevice#getProductString()
     */
    @Override
    public String getProductString() throws UsbException,
        UnsupportedEncodingException
    {
        checkConnected();
        final byte index = getUsbDeviceDescriptor().iProduct();
        if (index == 0) return null;
        return getString(index);
    }

    /**
     * @see UsbDevice#getSpeed()
     */
    @Override
    public Object getSpeed()
    {
        switch (this.speed)
        {
            case LibUSB.SPEED_FULL:
                return UsbConst.DEVICE_SPEED_FULL;
            case LibUSB.SPEED_LOW:
                return UsbConst.DEVICE_SPEED_LOW;
            default:
                return UsbConst.DEVICE_SPEED_UNKNOWN;
        }
    }

    /**
     * @see UsbDevice#getUsbConfigurations()
     */
    @Override
    public List<Usb4JavaConfiguration> getUsbConfigurations()
    {
        return this.configurations;
    }

    /**
     * @see UsbDevice#getUsbConfiguration(byte)
     */
    @Override
    public Usb4JavaConfiguration getUsbConfiguration(byte number)
    {
        return this.configMapping.get(number);
    }

    /**
     * @see UsbDevice#containsUsbConfiguration(byte)
     */
    @Override
    public boolean containsUsbConfiguration(byte number)
    {
        return this.configMapping.containsKey(number);
    }

    /**
     * @see UsbDevice#getActiveUsbConfigurationNumber()
     */
    @Override
    public byte getActiveUsbConfigurationNumber()
    {
        return this.activeConfigurationNumber;
    }

    /**
     * Sets the active USB configuration.
     * 
     * @param number
     *            The number of the USB configuration to activate.
     * @throws UsbException
     *             When configuration could not be activated.
     */
    final void setActiveUsbConfigurationNumber(final byte number)
        throws UsbException
    {
        if (number != this.activeConfigurationNumber)
        {
            if (this.claimedInterfaceNumber != null)
                throw new UsbException("Can't change configuration while an "
                    + "interface is still claimed");

            final int result = LibUSB.setConfiguration(open(), number & 0xff);
            if (result < 0)
                throw new Usb4JavaException("Unable to set configuration",
                    result);
            this.activeConfigurationNumber = number;
        }
    }

    /**
     * Claims the specified interface.
     * 
     * @param number
     *            The number of the interface to claim.
     * @param force
     *            If claim should be forces if possible.
     * @throws UsbException
     *             When interface could not be claimed.
     * @throws UsbClaimException
     *             When an interface is already claimed.
     */
    final void claimInterface(final byte number, final boolean force)
        throws UsbException
    {
        if (this.claimedInterfaceNumber != null)
            throw new UsbClaimException("An interface is already claimed");

        final DeviceHandle handle = open();

        // Detach existing driver from the device if requested and
        // libusb supports it.
        if (force)
        {
            int result = LibUSB.kernelDriverActive(handle, number);
            if (result == LibUSB.ERROR_NO_DEVICE)
                throw new UsbDisconnectedException();
            if (result == 1)
            {
                result = LibUSB.detachKernelDriver(handle, number);
                if (result < 0)
                    throw new Usb4JavaException(
                        "Unable to detach kernel driver", result);
                this.detachedKernelDriver = true;
            }
        }

        final int result = LibUSB.claimInterface(handle, number & 0xff);
        if (result < 0)
            throw new Usb4JavaException("Unable to claim interface",
                result);
        this.claimedInterfaceNumber = number;
    }

    /**
     * Releases a claimed interface.
     * 
     * @param number
     *            The number of the interface to release.
     * @throws UsbClaimException
     *             When the interface is not claimed.
     * @throws UsbException
     *             When interface could not be claimed.
     */
    final void releaseInterface(final byte number) throws UsbException
    {
        if (this.claimedInterfaceNumber == null)
            throw new UsbClaimException("No interface is claimed");
        if (!Byte.valueOf(number).equals(this.claimedInterfaceNumber))
            throw new UsbClaimException("Interface not claimed");

        final DeviceHandle handle = open();
        int result = LibUSB.releaseInterface(handle, number & 0xff);
        if (result < 0) throw new Usb4JavaException(
            "Unable to release interface", result);

        if (this.detachedKernelDriver)
        {
            result = LibUSB.attachKernelDriver(handle, number & 0xff);
            if (result < 0) throw new Usb4JavaException(
                "Uanble to re-attach kernel driver", result);
        }

        this.claimedInterfaceNumber = null;
    }

    /**
     * Checks if the specified interface is claimed.
     * 
     * @param number
     *            The number of the interface to check.
     * @return True if interface is claimed, false if not.
     */
    final boolean isInterfaceClaimed(final byte number)
    {
        return Byte.valueOf(number).equals(this.claimedInterfaceNumber);
    }

    /**
     * @see UsbDevice#getActiveUsbConfiguration()
     */
    @Override
    public Usb4JavaConfiguration getActiveUsbConfiguration()
    {
        return getUsbConfiguration(getActiveUsbConfigurationNumber());
    }

    /**
     * @see UsbDevice#isConfigured()
     */
    @Override
    public boolean isConfigured()
    {
        return getActiveUsbConfigurationNumber() != 0;
    }

    /**
     * @see UsbDevice#getUsbDeviceDescriptor()
     */
    @Override
    public UsbDeviceDescriptor getUsbDeviceDescriptor()
    {
        return this.id.getDeviceDescriptor();
    }

    /**
     * @see UsbDevice#getUsbStringDescriptor(byte)
     */
    @Override
    public UsbStringDescriptor getUsbStringDescriptor(byte index)
        throws UsbException
    {
        checkConnected();
        final short[] languages = getLanguages();
        final DeviceHandle handle = open();
        final short langId = languages.length == 0 ? 0 : languages[0];
        final ByteBuffer data = ByteBuffer.allocateDirect(256);
        final int result =
            LibUSB.getStringDescriptor(handle, index, langId, data);
        if (result < 0)
            throw new Usb4JavaException("Unable to get string descriptor "
                + index + " from device " + this, result);
        return new SimpleUsbStringDescriptor(data);
    }

    /**
     * @see UsbDevice#getString(byte)
     */
    @Override
    public String getString(byte index) throws UsbException,
        UnsupportedEncodingException
    {
        return getUsbStringDescriptor(index).getString();
    }

    /**
     * Returns the languages the specified device supports.
     * 
     * @return Array with supported language codes. Never null. May be empty.
     * @throws UsbException
     *             When string descriptor languages could not be read.
     */
    private short[] getLanguages() throws UsbException
    {
        final DeviceHandle handle = open();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
        final int result = LibUSB.getDescriptor(handle, LibUSB.DT_STRING, 0,
            buffer);
        if (result < 0)
            throw new Usb4JavaException(
                "Unable to get string descriptor languages", result);
        if (result < 2)
            throw new UsbException("Received illegal descriptor length: "
                + result);
        final short[] languages = new short[(result - 2) / 2];
        if (languages.length == 0) return languages;
        buffer.position(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(languages);
        return languages;
    }

    /**
     * @see UsbDevice#syncSubmit(javax.usb.UsbControlIrp)
     */
    @Override
    public void syncSubmit(UsbControlIrp irp) throws UsbException
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        checkConnected();
        this.queue.add(irp);
        irp.waitUntilComplete();
        if (irp.isUsbException()) throw irp.getUsbException();
    }

    /**
     * @see UsbDevice#asyncSubmit(javax.usb.UsbControlIrp)
     */
    @Override
    public void asyncSubmit(UsbControlIrp irp)
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        checkConnected();
        this.queue.add(irp);
    }

    /**
     * @see UsbDevice#syncSubmit(java.util.List)
     */
    @Override
    public void syncSubmit(List list) throws UsbException
    {
        if (list == null)
            throw new IllegalArgumentException("list must not be null");
        checkConnected();
        for (final Object item: list)
        {
            if (!(item instanceof UsbControlIrp))
                throw new IllegalArgumentException(
                    "List contains non-UsbControlIrp objects");
            syncSubmit((UsbControlIrp) item);
        }
    }

    /**
     * @see UsbDevice#asyncSubmit(java.util.List)
     */
    @Override
    public void asyncSubmit(List list)
    {
        if (list == null)
            throw new IllegalArgumentException("list must not be null");
        checkConnected();
        for (final Object item: list)
        {
            if (!(item instanceof UsbControlIrp))
                throw new IllegalArgumentException(
                    "List contains non-UsbControlIrp objects");
            asyncSubmit((UsbControlIrp) item);
        }
    }

    /**
     * @see UsbDevice#createUsbControlIrp(byte, byte, short, short)
     */
    @Override
    public UsbControlIrp createUsbControlIrp(byte bmRequestType, byte bRequest,
        short wValue, short wIndex)
    {
        return new DefaultUsbControlIrp(bmRequestType, bRequest, wValue,
            wIndex);
    }

    /**
     * @see UsbDevice#addUsbDeviceListener(UsbDeviceListener)
     */
    @Override
    public void addUsbDeviceListener(UsbDeviceListener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * @see UsbDevice#removeUsbDeviceListener(UsbDeviceListener)
     */
    @Override
    public void removeUsbDeviceListener(UsbDeviceListener listener)
    {
        this.listeners.remove(listener);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString()
    {
        return this.id.toString();
    }

    /**
     * @see UsbDevice#isUsbHub()
     */
    @Override
    public boolean isUsbHub()
    {
        return false;
    }
}
