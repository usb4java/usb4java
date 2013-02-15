/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

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
public final class VirtualRootHubInterface implements UsbInterface
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
    public VirtualRootHubInterface(final UsbConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * @see UsbInterface#claim()
     */
    @Override
    public void claim() throws UsbException
    {
        throw new UsbException("Virtual interfaces can't be claimed");
    }

    /**
     * @see UsbInterface#claim(UsbInterfacePolicy)
     */
    @Override
    public void claim(final UsbInterfacePolicy policy) throws UsbException
    {
        throw new UsbException("Virtual interfaces can't be claimed");
    }

    /**
     * @see UsbInterface#release()
     */
    @Override
    public void release() throws UsbException
    {
        throw new UsbException("Virtual interfaces can't be released");
    }

    /**
     * @see UsbInterface#isClaimed()
     */
    @Override
    public boolean isClaimed()
    {
        return true;
    }

    /**
     * @see UsbInterface#isActive()
     */
    @Override
    public boolean isActive()
    {
        return true;
    }

    /**
     * @see UsbInterface#getNumSettings()
     */
    @Override
    public int getNumSettings()
    {
        return 0;
    }

    /**
     * @see UsbInterface#getActiveSettingNumber()
     */
    @Override
    public byte getActiveSettingNumber()
    {
        return 0;
    }

    /**
     * @see UsbInterface#getActiveSetting()
     */
    @Override
    public UsbInterface getActiveSetting()
    {
        return this;
    }

    /**
     * @see UsbInterface#getSetting(byte)
     */
    @Override
    public UsbInterface getSetting(final byte number)
    {
        return this;
    }

    /**
     * @see UsbInterface#containsSetting(byte)
     */
    @Override
    public boolean containsSetting(final byte number)
    {
        return false;
    }

    /**
     * @see UsbInterface#getSettings()
     */
    @Override
    public List<UsbInterface> getSettings()
    {
        return this.settings;
    }

    /**
     * @see UsbInterface#getUsbEndpoints()
     */
    @Override
    public List<UsbEndpoint> getUsbEndpoints()
    {
        return this.endpoints;
    }

    /**
     * @see UsbInterface#getUsbEndpoint(byte)
     */
    @Override
    public UsbEndpoint getUsbEndpoint(final byte address)
    {
        return null;
    }

    /**
     * @see UsbInterface#containsUsbEndpoint(byte)
     */
    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        return false;
    }

    /**
     * @see UsbInterface#getUsbConfiguration()
     */
    @Override
    public UsbConfiguration getUsbConfiguration()
    {
        return this.configuration;
    }

    /**
     * @see UsbInterface#getUsbInterfaceDescriptor()
     */
    @Override
    public UsbInterfaceDescriptor getUsbInterfaceDescriptor()
    {
        return this.descriptor;
    }

    /**
     * @see UsbInterface#getInterfaceString()
     */
    @Override
    public String getInterfaceString()
    {
        return null;
    }
}
