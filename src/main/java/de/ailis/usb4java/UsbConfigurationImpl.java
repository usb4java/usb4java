/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;


/**
 * UsbConfiguration implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbConfigurationImpl implements UsbConfiguration
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The USB configuration descriptor.
     */

    public UsbConfigurationImpl(final UsbConfigurationDescriptor descriptor)
    {
        // TODO Implement me!
    }


    /**
     * @see javax.usb.UsbConfiguration#isActive()
     */

    @Override
    public boolean isActive()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbInterfaces()
     */

    @Override
    public List<UsbInterface> getUsbInterfaces()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbInterface(byte)
     */

    @Override
    public UsbInterface getUsbInterface(final byte number)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#containsUsbInterface(byte)
     */

    @Override
    public boolean containsUsbInterface(final byte number)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * Adds a USB interface.
     *
     * @param iface
     *            The interface to add.
     */

    void addUsbInterface(final UsbInterface iface)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbDevice()
     */

    @Override
    public UsbDevice getUsbDevice()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbConfigurationDescriptor()
     */

    @Override
    public UsbConfigurationDescriptor getUsbConfigurationDescriptor()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbConfiguration#getConfigurationString()
     */

    @Override
    public String getConfigurationString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }
}
