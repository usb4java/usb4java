/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbEndpoint;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;

import de.ailis.usb4java.USB_Interface;


/**
 * usb4java implementation of UsbInterface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbInterfaceImpl implements UsbInterface
{
    /** The USB configuration. */
    private final UsbConfiguration configuration;

    /** The low-level USB interface. */
    private final USB_Interface iface;


    /**
     * Constructor.
     *
     * @param configuration
     *            The USB configuration.
     * @param iface
     *            The low-level USB-interface.
     */

    public UsbInterfaceImpl(final UsbConfiguration configuration,
        final USB_Interface iface)
    {
        this.configuration = configuration;
        this.iface = iface;
    }


    /**
     * @see UsbInterface#claim()
     */

    @Override
    public void claim()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#claim(UsbInterfacePolicy)
     */

    @Override
    public void claim(final UsbInterfacePolicy policy)
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#release()
     */

    @Override
    public void release()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#isClaimed()
     */

    @Override
    public boolean isClaimed()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#isActive()
     */

    @Override
    public boolean isActive()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#getNumSettings()
     */

    @Override
    public int getNumSettings()
    {
        return this.iface.num_altsetting();
    }


    /**
     * @see UsbInterface#getActiveSettingNumber()
     */

    @Override
    public byte getActiveSettingNumber()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#getActiveSetting()
     */

    @Override
    public UsbInterface getActiveSetting()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#getSetting(byte)
     */

    @Override
    public UsbInterface getSetting(final byte number)
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#containsSetting(byte)
     */

    @Override
    public boolean containsSetting(final byte number)
    {
        return number >= 0 && number < this.iface.num_altsetting();
    }


    /**
     * @see UsbInterface#getSettings()
     */

    @Override
    public List<UsbInterface> getSettings()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#getUsbEndpoints()
     */

    @Override
    public List<UsbEndpoint> getUsbEndpoints()
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#getUsbEndpoint(byte)
     */

    @Override
    public UsbEndpoint getUsbEndpoint(final byte address)
    {
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#containsUsbEndpoint(byte)
     */

    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        // TODO
        throw new UnsupportedOperationException();
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
        // TODO
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbInterface#getInterfaceString()
     */

    @Override
    public String getInterfaceString()
    {
        // TODO
        throw new UnsupportedOperationException();
    }
}
