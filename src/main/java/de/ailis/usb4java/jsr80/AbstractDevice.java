/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import static de.ailis.usb4java.USB.USB_DT_STRING;
import static de.ailis.usb4java.USB.usb_close;
import static de.ailis.usb4java.USB.usb_get_descriptor;
import static de.ailis.usb4java.USB.usb_get_string;
import static de.ailis.usb4java.USB.usb_open;
import static de.ailis.usb4java.USB.usb_strerror;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private final byte activeConfigurationNumber = 0;

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
            configurations.add(new UsbConfigurationImpl(this, config));
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
        // TODO
        throw new UnsupportedOperationException();
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
