/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import static de.ailis.usb4java.jni.USB.USB_DT_STRING;
import static de.ailis.usb4java.jni.USB.libusb_has_detach_kernel_driver_np;
import static de.ailis.usb4java.jni.USB.usb_claim_interface;
import static de.ailis.usb4java.jni.USB.usb_close;
import static de.ailis.usb4java.jni.USB.usb_detach_kernel_driver_np;
import static de.ailis.usb4java.jni.USB.usb_get_descriptor;
import static de.ailis.usb4java.jni.USB.usb_get_string;
import static de.ailis.usb4java.jni.USB.usb_open;
import static de.ailis.usb4java.jni.USB.usb_release_interface;
import static de.ailis.usb4java.jni.USB.usb_set_configuration;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import de.ailis.usb4java.descriptors.LibUsbDeviceDescriptor;
import de.ailis.usb4java.descriptors.LibUsbStringDescriptor;
import de.ailis.usb4java.exceptions.LibUsbException;
import de.ailis.usb4java.jni.USB_Config_Descriptor;
import de.ailis.usb4java.jni.USB_Dev_Handle;
import de.ailis.usb4java.jni.USB_Device;
import de.ailis.usb4java.jni.USB_String_Descriptor;
import de.ailis.usb4java.support.ControlIrpQueue;
import de.ailis.usb4java.support.UsbDeviceListenerList;
import de.ailis.usb4java.support.UsbLock;


/**
 * usb4java implementation of JSR-80 UsbDevice.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public abstract class LibUsbDevice implements UsbDevice
{
    /** The low-level USB device. */
    protected final USB_Device device;

    /** The device descriptor. */
    private final UsbDeviceDescriptor descriptor;

    /** The USB configurations. */
    private final List<LibUsbConfiguration> configurations;

    /** The USB device listener list. */
    private final UsbDeviceListenerList listeners = new UsbDeviceListenerList();

    /** The device handle. Null if device is not open. */
    private USB_Dev_Handle handle;

    /** The number of the currently active configuration. */
    private byte activeConfigurationNumber = 0;

    /** The number of the currently claimed interface. */
    private Byte claimedInterfaceNumber = null;

    /** The port this device is connected to. */
    private UsbPort port;

    /** The IRP queue. */
    private final ControlIrpQueue queue = new ControlIrpQueue(this);


    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */

    public LibUsbDevice(final USB_Device device)
    {
        this.device = device;
        this.descriptor = new LibUsbDeviceDescriptor(device.descriptor());

        final USB_Config_Descriptor[] configs = device.config();
        final List<LibUsbConfiguration> configurations =
                new ArrayList<LibUsbConfiguration>(configs.length);
        for (final USB_Config_Descriptor config : configs)
        {
            if (config == null) continue;
            final LibUsbConfiguration configuration = new LibUsbConfiguration(
                this, config);
            configurations.add(configuration);

            // TODO No idea how to find out the active configuration via
            // libusb. So for now we use the first configuration.
            if (this.activeConfigurationNumber == 0)
            {
                this.activeConfigurationNumber = (byte) config
                        .bConfigurationValue();
            }
        }
        this.configurations = Collections.unmodifiableList(configurations);
    }


    /**
     * Ensures the device is connected.
     *
     * @throws UsbDisconnectedException
     *             When device is disconnected.
     */

    final void checkConnected() throws UsbDisconnectedException
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

    public final USB_Dev_Handle open() throws UsbException
    {
        if (this.handle == null)
        {
            UsbLock.acquire();
            try
            {
                this.handle = usb_open(this.device);
                if (this.handle == null)
                {
                    throw new LibUsbException("Can't open device "
                        + this.device);
                }
            }
            finally
            {
                UsbLock.release();
            }
        }
        return this.handle;
    }


    /**
     * Closes the device. If device is not open then nothing is done.
     *
     * @throws UsbException
     *             When device could not be closed.
     */

    public final void close() throws UsbException
    {
        if (this.handle != null)
        {
            UsbLock.acquire();
            try
            {
                final int result = usb_close(this.handle);
                if (result < 0)
                    throw new LibUsbException("Can't close device "
                        + this.device, result);
            }
            finally
            {
                UsbLock.release();
            }
        }
    }


    /**
     * @see UsbDevice#getParentUsbPort()
     */

    @Override
    public final UsbPort getParentUsbPort() throws UsbDisconnectedException
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
            final LibUsbHub hub = (LibUsbHub) this;
            for (final LibUsbDevice device : hub.getAttachedUsbDevices())
                hub.disconnectUsbDevice(device);
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
            throw new RuntimeException(e.toString(), e);
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
    public final String getManufacturerString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        checkConnected();
        final byte index = this.descriptor.iManufacturer();
        if (index == 0) return null;
        return getString(index);
    }


    /**
     * @see UsbDevice#getSerialNumberString()
     */

    @Override
    public final String getSerialNumberString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        checkConnected();
        final byte index = this.descriptor.iSerialNumber();
        if (index == 0) return null;
        return getString(index);
    }


    /**
     * @see UsbDevice#getProductString()
     */

    @Override
    public final String getProductString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        checkConnected();
        final byte index = this.descriptor.iProduct();
        if (index == 0) return null;
        return getString(index);
    }


    /**
     * @see UsbDevice#getSpeed()
     */

    @Override
    public final Object getSpeed()
    {
        return UsbConst.DEVICE_SPEED_UNKNOWN;
    }


    /**
     * @see UsbDevice#getUsbConfigurations()
     */

    @Override
    public final List<LibUsbConfiguration> getUsbConfigurations()
    {
        return this.configurations;
    }


    /**
     * @see UsbDevice#getUsbConfiguration(byte)
     */

    @Override
    public final LibUsbConfiguration getUsbConfiguration(final byte number)
    {
        for (final LibUsbConfiguration configuration : this.configurations)
        {
            if (configuration.getUsbConfigurationDescriptor()
                    .bConfigurationValue() == number)
            {
                return configuration;
            }
        }
        return null;
    }


    /**
     * @see UsbDevice#containsUsbConfiguration(byte)
     */

    @Override
    public final boolean containsUsbConfiguration(final byte number)
    {
        return getUsbConfiguration(number) != null;
    }


    /**
     * @see UsbDevice#getActiveUsbConfigurationNumber()
     */

    @Override
    public final byte getActiveUsbConfigurationNumber()
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

            UsbLock.acquire();
            try
            {
                final int result = usb_set_configuration(open(), number & 0xff);
                if (result < 0)
                    throw new LibUsbException("Unable to set configuration",
                        result);
                this.activeConfigurationNumber = number;
            }
            finally
            {
                UsbLock.release();
            }
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
        throws UsbException, UsbClaimException
    {
        if (this.claimedInterfaceNumber != null)
            throw new UsbClaimException("A interface is already claimed");

        UsbLock.acquire();
        try
        {
            // Detach existing driver from the device if requested and
            // libusb supports it.
            if (force && libusb_has_detach_kernel_driver_np())
            {
                usb_detach_kernel_driver_np(open(), number);
            }

            final int result = usb_claim_interface(open(), number & 0xff);
            if (result < 0)
                throw new LibUsbException("Unable to claim interface",
                    result);
            this.claimedInterfaceNumber = number;
        }
        finally
        {
            UsbLock.release();
        }
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

    final void releaseInterface(final byte number) throws UsbException,
        UsbClaimException
    {
        if (this.claimedInterfaceNumber == null)
            throw new UsbClaimException("No interface is claimed");
        if (!Byte.valueOf(number).equals(this.claimedInterfaceNumber))
            throw new UsbClaimException("Interface not claimed");

        UsbLock.acquire();
        try
        {
            final int result = usb_release_interface(open(), number & 0xff);
            if (result < 0) throw new LibUsbException(
                "Unable to release interface", result);
            this.claimedInterfaceNumber = null;
        }
        finally
        {
            UsbLock.release();
        }
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
    public final LibUsbConfiguration getActiveUsbConfiguration()
    {
        return getUsbConfiguration(this.activeConfigurationNumber);
    }


    /**
     * @see UsbDevice#isConfigured()
     */

    @Override
    public final boolean isConfigured()
    {
        return this.activeConfigurationNumber != 0;
    }


    /**
     * @see UsbDevice#getUsbDeviceDescriptor()
     */

    @Override
    public final UsbDeviceDescriptor getUsbDeviceDescriptor()
    {
        return this.descriptor;
    }


    /**
     * @see UsbDevice#getUsbStringDescriptor(byte)
     */

    @Override
    public final UsbStringDescriptor getUsbStringDescriptor(final byte index)
        throws UsbException, UsbDisconnectedException
    {
        checkConnected();
        UsbLock.acquire();
        try
        {
            final short[] languages = getLanguages();
            final USB_Dev_Handle handle = open();
            final short langid = languages.length == 0 ? 0 : languages[0];
            final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
            final int len = usb_get_string(handle, index, langid, buffer);
            if (len < 0)
                throw new LibUsbException("Unable to get string descriptor "
                    + index + " from device " + this.device, len);
            return new LibUsbStringDescriptor(
                new USB_String_Descriptor(buffer));
        }
        finally
        {
            UsbLock.release();
        }
    }


    /**
     * @see UsbDevice#getString(byte)
     */

    @Override
    public final String getString(final byte index) throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
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
        UsbLock.acquire();
        try
        {
            final USB_Dev_Handle handle = open();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
            final int len = usb_get_descriptor(handle, USB_DT_STRING, 0,
                buffer);
            if (len < 0)
                throw new LibUsbException(
                    "Unable to get string descriptor languages", len);
            if (len < 2)
                throw new UsbException("Received illegal descriptor length: "
                    + len);
            final short[] languages = new short[(len - 2) / 2];
            if (languages.length == 0) return languages;
            buffer.position(2);
            buffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                    .get(languages);
            return languages;
        }
        finally
        {
            UsbLock.release();
        }
    }


    /**
     * @see UsbDevice#syncSubmit(UsbControlIrp)
     */

    @Override
    public final void syncSubmit(final UsbControlIrp irp) throws UsbException,
        IllegalArgumentException, UsbDisconnectedException
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        checkConnected();
        this.queue.add(irp);
        irp.waitUntilComplete();
        if (irp.isUsbException()) throw irp.getUsbException();
    }


    /**
     * @see UsbDevice#asyncSubmit(UsbControlIrp)
     */

    @Override
    public final void asyncSubmit(final UsbControlIrp irp) throws UsbException,
        IllegalArgumentException, UsbDisconnectedException
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        checkConnected();
        this.queue.add(irp);
    }


    /**
     * @see UsbDevice#syncSubmit(List)
     */

    @Override
    public final void syncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException, IllegalArgumentException, UsbDisconnectedException
    {
        if (list == null)
            throw new IllegalArgumentException("list must not be null");
        checkConnected();
        for (final Object item : list)
        {
            if (!(item instanceof UsbControlIrp))
                throw new IllegalArgumentException(
                    "List contains non-UsbControlIrp objects");
            syncSubmit((UsbControlIrp) item);
        }
    }


    /**
     * @see UsbDevice#asyncSubmit(List)
     */

    @Override
    public final void
        asyncSubmit(@SuppressWarnings("rawtypes") final List list)
            throws UsbException, IllegalArgumentException,
            UsbDisconnectedException
    {
        if (list == null)
            throw new IllegalArgumentException("list must not be null");
        checkConnected();
        for (final Object item : list)
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
    public final UsbControlIrp createUsbControlIrp(final byte bmRequestType,
        final byte bRequest, final short wValue, final short wIndex)
    {
        return new DefaultUsbControlIrp(bmRequestType, bRequest, wValue,
            wIndex);
    }


    /**
     * @see UsbDevice#addUsbDeviceListener(UsbDeviceListener)
     */

    @Override
    public final void addUsbDeviceListener(final UsbDeviceListener listener)
    {
        this.listeners.add(listener);
    }


    /**
     * @see UsbDevice#removeUsbDeviceListener(UsbDeviceListener)
     */

    @Override
    public final void removeUsbDeviceListener(final UsbDeviceListener listener)
    {
        this.listeners.remove(listener);
    }


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public final String toString()
    {
        return this.device.toString();
    }
}
