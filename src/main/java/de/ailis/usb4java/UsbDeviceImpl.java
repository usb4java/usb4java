/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbPort;
import javax.usb.UsbStringDescriptor;
import javax.usb.event.UsbDeviceListener;

import de.ailis.usb4java.jni.USB_Device;


/**
 * UsbDevice implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbDeviceImpl implements UsbDevice
{
    /** The USB configurations. */
    private final List<UsbConfiguration> configurations = new ArrayList<UsbConfiguration>();

    /** The currently active USB configuration. */
    private byte activeConfigurationNumber = 0;


    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */

    public UsbDeviceImpl(final USB_Device device)
    {
        // TODO Implement me!
    }


    /**
     * @see javax.usb.UsbDevice#getParentUsbPort()
     */

    @Override
    public UsbPort getParentUsbPort() throws UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#isUsbHub()
     */

    @Override
    public boolean isUsbHub()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getManufacturerString()
     */

    @Override
    public String getManufacturerString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getSerialNumberString()
     */

    @Override
    public String getSerialNumberString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getProductString()
     */

    @Override
    public String getProductString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getSpeed()
     */

    @Override
    public Object getSpeed()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getUsbConfigurations()
     */

    @Override
    public List<UsbConfiguration> getUsbConfigurations()
    {
        return Collections.unmodifiableList(this.configurations);
    }


    /**
     * @see javax.usb.UsbDevice#getUsbConfiguration(byte)
     */

    @Override
    public UsbConfiguration getUsbConfiguration(final byte number)
    {
        for (final UsbConfiguration configuration : this.configurations)
            if (configuration.getUsbConfigurationDescriptor()
                    .bConfigurationValue() == number)
                return configuration;
        return null;
    }


    /**
     * @see javax.usb.UsbDevice#containsUsbConfiguration(byte)
     */

    @Override
    public boolean containsUsbConfiguration(final byte number)
    {
        for (final UsbConfiguration configuration : this.configurations)
            if (configuration.getUsbConfigurationDescriptor()
                    .bConfigurationValue() == number)
                return true;
        return false;
    }


    /**
     * Adds a USB configuration.
     *
     * @param configuration
     *            The USB configuration to add.
     */

    void addUsbConfiguration(final UsbConfiguration configuration)
    {
        if (this.configurations.contains(configuration))
            throw new IllegalStateException("configuration already added");
        this.configurations.add(configuration);

        // Use the first added configuration is the active one.
        // FIXME This may be wrong. Find out how this really works.
        if (this.activeConfigurationNumber == 0)
            this.activeConfigurationNumber = configuration
                    .getUsbConfigurationDescriptor().bConfigurationValue();
    }


    /**
     * @see javax.usb.UsbDevice#getActiveUsbConfigurationNumber()
     */

    @Override
    public byte getActiveUsbConfigurationNumber()
    {
        return this.activeConfigurationNumber;
    }


    /**
     * @see javax.usb.UsbDevice#getActiveUsbConfiguration()
     */

    @Override
    public UsbConfiguration getActiveUsbConfiguration()
    {
        return getUsbConfiguration(this.activeConfigurationNumber);
    }


    /**
     * @see javax.usb.UsbDevice#isConfigured()
     */

    @Override
    public boolean isConfigured()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getUsbDeviceDescriptor()
     */

    @Override
    public UsbDeviceDescriptor getUsbDeviceDescriptor()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getUsbStringDescriptor(byte)
     */

    @Override
    public UsbStringDescriptor getUsbStringDescriptor(final byte index)
        throws UsbException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#getString(byte)
     */

    @Override
    public String getString(final byte index) throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#syncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public void syncSubmit(final UsbControlIrp irp) throws UsbException,
        IllegalArgumentException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#asyncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public void asyncSubmit(final UsbControlIrp irp) throws UsbException,
        IllegalArgumentException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#syncSubmit(java.util.List)
     */

    @Override
    public void syncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException, IllegalArgumentException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#asyncSubmit(java.util.List)
     */

    @Override
    public void asyncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException, IllegalArgumentException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#createUsbControlIrp(byte, byte, short, short)
     */

    @Override
    public UsbControlIrp createUsbControlIrp(final byte bmRequestType,
        final byte bRequest,
        final short wValue, final short wIndex)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#addUsbDeviceListener(javax.usb.event.UsbDeviceListener)
     */

    @Override
    public void addUsbDeviceListener(final UsbDeviceListener listener)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbDevice#removeUsbDeviceListener(javax.usb.event.UsbDeviceListener)
     */

    @Override
    public void removeUsbDeviceListener(final UsbDeviceListener listener)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }
}
