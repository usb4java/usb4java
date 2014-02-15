/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;

import de.ailis.usb4java.descriptors.SimpleUsbInterfaceDescriptor;

/**
 * The virtual USB interfaces used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
final class RootHubInterface implements UsbInterface
{
    /** The list of endpoints. */
    private final List<UsbEndpoint> endpoints = new ArrayList<UsbEndpoint>(0);

    /** The list of alternate settings. */
    private final List<UsbInterface> settings = new ArrayList<UsbInterface>(0);

    /** The USB configuration. */
    private final UsbConfiguration configuration;

    /** The interface descriptor. */
    private final UsbInterfaceDescriptor descriptor =
        new SimpleUsbInterfaceDescriptor(
            UsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE,
            UsbConst.DESCRIPTOR_TYPE_INTERFACE,
            (byte) 0,
            (byte) 0,
            (byte) 0,
            UsbConst.HUB_CLASSCODE,
            (byte) 0,
            (byte) 0,
            (byte) 0);

    /**
     * Constructor.
     *
     * @param configuration
     *            The USB configuration.
     */
    RootHubInterface(final UsbConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void claim() throws UsbException
    {
        throw new UsbException("Virtual interfaces can't be claimed");
    }

    @Override
    public void claim(final UsbInterfacePolicy policy) throws UsbException
    {
        throw new UsbException("Virtual interfaces can't be claimed");
    }

    @Override
    public void release() throws UsbException
    {
        throw new UsbException("Virtual interfaces can't be released");
    }

    @Override
    public boolean isClaimed()
    {
        return true;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public int getNumSettings()
    {
        return 0;
    }

    @Override
    public byte getActiveSettingNumber()
    {
        return 0;
    }

    @Override
    public UsbInterface getActiveSetting()
    {
        return this;
    }

    @Override
    public UsbInterface getSetting(final byte number)
    {
        return this;
    }

    @Override
    public boolean containsSetting(final byte number)
    {
        return false;
    }

    @Override
    public List<UsbInterface> getSettings()
    {
        return this.settings;
    }

    @Override
    public List<UsbEndpoint> getUsbEndpoints()
    {
        return this.endpoints;
    }

    @Override
    public UsbEndpoint getUsbEndpoint(final byte address)
    {
        return null;
    }

    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        return false;
    }

    @Override
    public UsbConfiguration getUsbConfiguration()
    {
        return this.configuration;
    }

    @Override
    public UsbInterfaceDescriptor getUsbInterfaceDescriptor()
    {
        return this.descriptor;
    }

    @Override
    public String getInterfaceString()
    {
        return null;
    }
}
