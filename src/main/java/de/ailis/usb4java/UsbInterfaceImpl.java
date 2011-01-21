/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.usb.UsbClaimException;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;


/**
 * UsbInterface implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbInterfaceImpl implements UsbInterface
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            . The USB interface descriptor.
     */

    public UsbInterfaceImpl(final UsbInterfaceDescriptor descriptor)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#claim()
     */

    @Override
    public void claim() throws UsbClaimException, UsbException,
        UsbNotActiveException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#claim(javax.usb.UsbInterfacePolicy)
     */

    @Override
    public void claim(final UsbInterfacePolicy policy) throws UsbClaimException,
        UsbException, UsbNotActiveException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#release()
     */

    @Override
    public void release() throws UsbClaimException, UsbException,
        UsbNotActiveException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#isClaimed()
     */

    @Override
    public boolean isClaimed()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#isActive()
     */

    @Override
    public boolean isActive()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getNumSettings()
     */

    @Override
    public int getNumSettings()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getActiveSettingNumber()
     */

    @Override
    public byte getActiveSettingNumber() throws UsbNotActiveException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getActiveSetting()
     */

    @Override
    public UsbInterface getActiveSetting() throws UsbNotActiveException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getSetting(byte)
     */

    @Override
    public UsbInterface getSetting(final byte number)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#containsSetting(byte)
     */

    @Override
    public boolean containsSetting(final byte number)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getSettings()
     */

    @Override
    public List<UsbInterface> getSettings()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getUsbEndpoints()
     */

    @Override
    public List<UsbEndpoint> getUsbEndpoints()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getUsbEndpoint(byte)
     */

    @Override
    public UsbEndpoint getUsbEndpoint(final byte address)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#containsUsbEndpoint(byte)
     */

    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getUsbConfiguration()
     */

    @Override
    public UsbConfiguration getUsbConfiguration()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getUsbInterfaceDescriptor()
     */

    @Override
    public UsbInterfaceDescriptor getUsbInterfaceDescriptor()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbInterface#getInterfaceString()
     */

    @Override
    public String getInterfaceString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }
}
