/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import static de.ailis.usb4java.USB.USB_DT_STRING;
import static de.ailis.usb4java.USB.usb_claim_interface;
import static de.ailis.usb4java.USB.usb_close;
import static de.ailis.usb4java.USB.usb_control_msg;
import static de.ailis.usb4java.USB.usb_get_descriptor;
import static de.ailis.usb4java.USB.usb_get_string;
import static de.ailis.usb4java.USB.usb_open;
import static de.ailis.usb4java.USB.usb_release_interface;
import static de.ailis.usb4java.USB.usb_set_configuration;
import static de.ailis.usb4java.USB.usb_strerror;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbClaimException;
import javax.usb.UsbConfiguration;
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

import de.ailis.usb4java.USBLock;
import de.ailis.usb4java.USB_Config_Descriptor;
import de.ailis.usb4java.USB_Dev_Handle;
import de.ailis.usb4java.USB_Device;
import de.ailis.usb4java.USB_String_Descriptor;


/**
 * usb4java implemention of JSR-80 UsbDevice.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

abstract class AbstractDevice implements UsbDevice
{
    /** The low-level USB device. */
    protected final USB_Device device;

    /** The device descriptor. */
    private final UsbDeviceDescriptorImpl descriptor;

    /** The USB configurations. */
    private final List<UsbConfiguration> configurations;

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


    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */

    public AbstractDevice(final USB_Device device)
    {
        this.device = device;
        this.descriptor = new UsbDeviceDescriptorImpl(device.descriptor());

        final USB_Config_Descriptor[] configs = device.config();
        final List<UsbConfiguration> configurations =
                new ArrayList<UsbConfiguration>(configs.length);
        for (final USB_Config_Descriptor config : configs)
        {
            final UsbConfiguration configuration = new UsbConfigurationImpl(
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
     * Opens the USB device and returns the USB device handle. If device was
     * already open then the old handle is returned.
     *
     * @return The USB device handle.
     * @throws UsbException
     *             When USB device could not be opened.
     */

    final USB_Dev_Handle open() throws UsbException
    {
        if (this.handle == null)
        {
            USBLock.acquire();
            try
            {
                this.handle = usb_open(this.device);
                if (this.handle == null)
                {
                    throw new UsbException("Can't open device "
                        + this.device + ": " + usb_strerror());
                }
            }
            finally
            {
                USBLock.release();
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

    final void close() throws UsbException
    {
        if (this.handle != null)
        {
            USBLock.acquire();
            try
            {
                if (usb_close(this.handle) < 0)
                {
                    throw new UsbException("Can't close device "
                        + this.device + ": " + usb_strerror());
                }
            }
            finally
            {
                USBLock.release();
            }
        }
    }


    /**
     * @see UsbDevice#getParentUsbPort()
     */

    @Override
    public final UsbPort getParentUsbPort()
    {
        if (this.port == null) throw new UsbDisconnectedException();
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
            final UsbPorts hub = (UsbPorts) this;
            for (final UsbDevice device : hub.getAttachedUsbDevices())
                hub.disconnectUsbDevice(device);
        }

        this.port = port;

        final UsbServicesImpl services;
        try
        {
            services = (UsbServicesImpl) UsbHostManager.getUsbServices();
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
        UnsupportedEncodingException
    {
        final byte index = this.descriptor.iManufacturer();
        if (index == 0) return null;
        return getString(index);
    }


    /**
     * @see UsbDevice#getSerialNumberString()
     */

    @Override
    public final String getSerialNumberString() throws UsbException,
        UnsupportedEncodingException
    {
        final byte index = this.descriptor.iSerialNumber();
        if (index == 0) return null;
        return getString(index);
    }


    /**
     * @see UsbDevice#getProductString()
     */

    @Override
    public final String getProductString() throws UsbException,
        UnsupportedEncodingException
    {
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
    public final List<UsbConfiguration> getUsbConfigurations()
    {
        return this.configurations;
    }


    /**
     * @see UsbDevice#getUsbConfiguration(byte)
     */

    @Override
    public final UsbConfiguration getUsbConfiguration(final byte number)
    {
        for (final UsbConfiguration configuration : this.configurations)
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

            USBLock.acquire();
            try
            {
                final int result = usb_set_configuration(open(), number & 0xff);
                if (result < 0) throw new UsbException(usb_strerror());
                this.activeConfigurationNumber = number;
            }
            finally
            {
                USBLock.release();
            }
        }
    }


    /**
     * Claims the specified interface.
     *
     * @param number
     *            The number of the interface to claim.
     * @throws UsbException
     *             When interface could not be claimed.
     * @throws UsbClaimException
     *             When an interface is already claimed.
     */

    final void claimInterface(final byte number) throws UsbClaimException,
        UsbException
    {
        if (this.claimedInterfaceNumber != null)
            throw new UsbClaimException("A interface is already claimed");

        USBLock.acquire();
        try
        {
            final int result = usb_claim_interface(open(), number & 0xff);
            if (result < 0) throw new UsbException(usb_strerror());
            this.claimedInterfaceNumber = number;
        }
        finally
        {
            USBLock.release();
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

    final void releaseInterface(final byte number) throws UsbClaimException,
        UsbException
    {
        if (this.claimedInterfaceNumber == null)
            throw new UsbClaimException("No interface is claimed");
        if (!Byte.valueOf(number).equals(this.claimedInterfaceNumber))
            throw new UsbClaimException("Interface not claimed");

        USBLock.acquire();
        try
        {
            final int result = usb_release_interface(open(), number & 0xff);
            if (result < 0) throw new UsbException(usb_strerror());
            this.claimedInterfaceNumber = null;
        }
        finally
        {
            USBLock.release();
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
    public UsbConfiguration getActiveUsbConfiguration()
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
        throws UsbException
    {
        USBLock.acquire();
        try
        {
            final short[] languages = getLanguages();
            final USB_Dev_Handle handle = open();
            final short langid = languages.length == 0 ? 0 : languages[0];
            final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
            final int len = usb_get_string(handle, index, langid, buffer);
            if (len < 0)
                throw new UsbException("Unable to get string descriptor "
                    + index
                    + " from device " + this.device + ": " + len);
            return new UsbStringDescriptorImpl(
                new USB_String_Descriptor(buffer));
        }
        finally
        {
            USBLock.release();
        }
    }


    /**
     * @see UsbDevice#getString(byte)
     */

    @Override
    public final String getString(final byte index) throws UsbException,
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
        USBLock.acquire();
        try
        {
            final USB_Dev_Handle handle = open();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
            final int len = usb_get_descriptor(handle, USB_DT_STRING, 0,
                buffer);
            if (len < 0)
                throw new UsbException(
                    "Unable to get string descriptor languages: "
                        + usb_strerror());
            if (len < 2)
                throw new UsbException("Illegal descriptor length: "
                    + usb_strerror());
            final short[] languages = new short[(len - 2) / 2];
            if (languages.length == 0) return languages;
            buffer.position(2);
            buffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                    .get(languages);
            return languages;
        }
        finally
        {
            USBLock.release();
        }
    }


    /**
     * @see UsbDevice#syncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public final void syncSubmit(final UsbControlIrp irp) throws UsbException
    {
        USBLock.acquire();
        try
        {
            final ByteBuffer buffer = ByteBuffer
                    .allocateDirect(irp.getLength());
            final USB_Dev_Handle handle = open();
            final int len = usb_control_msg(handle, irp.bmRequestType(),
                irp.bRequest(),
                irp.wValue(), irp.wIndex(), buffer, 250);
            if (len < 0) throw new UsbException(usb_strerror());
            buffer.rewind();
            buffer.get(irp.getData(), 0, len);
        }
        finally
        {
            USBLock.release();
        }
    }


    /**
     * @see UsbDevice#asyncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public final void asyncSubmit(final UsbControlIrp irp) throws UsbException
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbDevice#syncSubmit(java.util.List)
     */

    @Override
    public final void syncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException
    {
        for (final Object item : list)
        {
            syncSubmit((UsbControlIrp) item);
        }
    }


    /**
     * @see UsbDevice#asyncSubmit(java.util.List)
     */

    @Override
    public final void asyncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException
    {
        for (final Object item : list)
        {
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
}
