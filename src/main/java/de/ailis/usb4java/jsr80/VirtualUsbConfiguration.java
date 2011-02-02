/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDevice;
import javax.usb.UsbInterface;


/**
 * Virtual USB configuration used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class VirtualUsbConfiguration implements UsbConfiguration
{
    /** The virtual interfaces. */
    private final List<UsbInterface> interfaces =
        new ArrayList<UsbInterface>();

    /** The device this configuration belongs to. */
    private final UsbDevice device;

    /** The USB configuration descriptor. */
    private final UsbConfigurationDescriptor descriptor =
        new VirtualUsbConfigurationDescriptor();


    /**
     * Constructor.
     *
     * @param device
     *            The device this configuration belongs to.
     */

    VirtualUsbConfiguration(final UsbDevice device)
    {
        this.device = device;
        this.interfaces.add(new VirtualUsbInterface(this));
    }


    /**
     * @see UsbConfiguration#isActive()
     */

    @Override
    public boolean isActive()
    {
        return true;
    }


    /**
     * @see UsbConfiguration#getUsbInterfaces()
     */

    @Override
    public List<UsbInterface> getUsbInterfaces()
    {
        return this.interfaces;
    }


    /**
     * @see UsbConfiguration#getUsbInterface(byte)
     */

    @Override
    public UsbInterface getUsbInterface(final byte number)
    {
        if (number != 0) return null;
        return this.interfaces.get(0);
    }


    /**
     * @see UsbConfiguration#containsUsbInterface(byte)
     */

    @Override
    public boolean containsUsbInterface(final byte number)
    {
        return number == 0;
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
    public String getConfigurationString()
    {
        return null;
    }
}
