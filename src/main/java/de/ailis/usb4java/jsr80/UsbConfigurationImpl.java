/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;

import de.ailis.usb4java.USB_Config_Descriptor;


/**
 * usb4java implementation of JSR80 UsbConfiguration.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbConfigurationImpl implements UsbConfiguration
{
    /** The low-level USB configuration descriptor. */
    private final USB_Config_Descriptor lowLevelDescriptor;

    /** The JSR 80 USB configuration descriptor. */
    private final UsbConfigurationDescriptor descriptor;

    /** The USB device. */
    private final UsbDevice device;


    /**
     * Constructor.
     *
     * @param device
     *            The USB device.
     * @param lowLevelDescriptor
     *            The low-level USB configuration descriptor.
     */

    public UsbConfigurationImpl(final UsbDevice device,
        final USB_Config_Descriptor lowLevelDescriptor)
    {
        this.device = device;
        this.lowLevelDescriptor = lowLevelDescriptor;
        this.descriptor = new UsbConfigurationDescriptorImpl(lowLevelDescriptor);
    }


    /**
     * @see javax.usb.UsbConfiguration#isActive()
     */

    @Override
    public boolean isActive()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbInterfaces()
     */

    @Override
    public List<UsbInterface> getUsbInterfaces()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbInterface(byte)
     */

    @Override
    public UsbInterface getUsbInterface(final byte number)
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#containsUsbInterface(byte)
     */

    @Override
    public boolean containsUsbInterface(final byte number)
    {
        return number >= 0 && number < this.lowLevelDescriptor.bNumInterfaces();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbDevice()
     */

    @Override
    public UsbDevice getUsbDevice()
    {
        return this.device;
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbConfigurationDescriptor()
     */

    @Override
    public UsbConfigurationDescriptor getUsbConfigurationDescriptor()
    {
        return this.descriptor;
    }


    /**
     * @see javax.usb.UsbConfiguration#getConfigurationString()
     */

    @Override
    public String getConfigurationString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO
        throw new UnsupportedOperationException();
    }
}
