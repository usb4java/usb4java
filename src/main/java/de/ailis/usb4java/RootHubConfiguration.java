/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbInterface;

import de.ailis.usb4java.descriptors.SimpleUsbConfigurationDescriptor;

/**
 * Virtual USB configuration used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
final class RootHubConfiguration implements UsbConfiguration
{
    /** The virtual interfaces. */
    private final List<UsbInterface> interfaces =
        new ArrayList<UsbInterface>();

    /** The device this configuration belongs to. */
    private final UsbDevice device;

    /** The USB configuration descriptor. */
    private final UsbConfigurationDescriptor descriptor =
        new SimpleUsbConfigurationDescriptor(
             UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION,
             UsbConst.DESCRIPTOR_TYPE_CONFIGURATION,
             (byte) (UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION
                + UsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE),
             (byte) 1,
             (byte) 1,
             (byte) 0,
             (byte) 0x80,
             (byte) 0);

    /**
     * Constructor.
     *
     * @param device
     *            The device this configuration belongs to.
     */
    RootHubConfiguration(final UsbDevice device)
    {
        this.device = device;
        this.interfaces.add(new RootHubInterface(this));
    }

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public List<UsbInterface> getUsbInterfaces()
    {
        return this.interfaces;
    }

    @Override
    public UsbInterface getUsbInterface(final byte number)
    {
        if (number != 0) return null;
        return this.interfaces.get(0);
    }

    @Override
    public boolean containsUsbInterface(final byte number)
    {
        return number == 0;
    }

    @Override
    public UsbDevice getUsbDevice()
    {
        return this.device;
    }

    @Override
    public UsbConfigurationDescriptor getUsbConfigurationDescriptor()
    {
        return this.descriptor;
    }

    @Override
    public String getConfigurationString()
    {
        return null;
    }
}
