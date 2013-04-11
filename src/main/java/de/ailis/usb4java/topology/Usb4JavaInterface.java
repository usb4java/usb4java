/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.usb.UsbClaimException;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;

import de.ailis.usb4java.descriptors.SimpleUsbInterfaceDescriptor;
import de.ailis.usb4java.libusb.EndpointDescriptor;
import de.ailis.usb4java.libusb.InterfaceDescriptor;

/**
 * usb4java implementation of UsbInterface.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaInterface implements UsbInterface
{
    /** The configuration this interface belongs to. */
    private final Usb4JavaConfiguration configuration;

    /** The interface descriptor. */
    private final UsbInterfaceDescriptor descriptor;

    /** The endpoints of this interface. */
    private final Map<Byte, Usb4JavaEndpoint> endpoints =
        new HashMap<Byte, Usb4JavaEndpoint>();

    /**
     * Constructor.
     * 
     * @param configuration
     *            The USB configuration this interface belongs to.
     * @param descriptor
     *            The libusb interface descriptor.
     */
    Usb4JavaInterface(final Usb4JavaConfiguration configuration,
        final InterfaceDescriptor descriptor)
    {
        this.configuration = configuration;
        this.descriptor = new SimpleUsbInterfaceDescriptor(descriptor);
        for (EndpointDescriptor endpointDescriptor: descriptor.endpoint())
        {
            final Usb4JavaEndpoint endpoint =
                new Usb4JavaEndpoint(this, endpointDescriptor);
            this.endpoints.put(endpointDescriptor.bEndpointAddress(), endpoint);
        }
    }


    /**
     * Ensures this setting and configuration is active.
     *
     * @throws UsbNotActiveException
     *             When the setting or the configuration is not active.
     */
    private void checkActive() throws UsbNotActiveException
    {
        if (!this.configuration.isActive())
            throw new UsbNotActiveException("Configuration is not active");
        if (!isActive())
            throw new UsbNotActiveException("Setting is not active");
    }

    /**
     * Ensures that the device is connected.
     *
     * @throws UsbDisconnectedException
     *             When device has been disconnected.
     */
    private void checkConnected() throws UsbDisconnectedException
    {
        this.configuration.getUsbDevice().checkConnected();
    }

    /**
     * @see UsbInterface#claim()
     */
    @Override
    public void claim() throws UsbException, UsbClaimException,
        UsbNotActiveException, UsbDisconnectedException
    {
        claim(null);
    }

    /**
     * @see UsbInterface#claim(UsbInterfacePolicy)
     */
    @Override
    public void claim(final UsbInterfacePolicy policy) throws UsbException,
        UsbClaimException, UsbNotActiveException, UsbDisconnectedException
    {
        checkActive();
        checkConnected();
        final Usb4JavaDevice device = this.configuration.getUsbDevice();
        device.claimInterface(this.descriptor.bInterfaceNumber(),
            policy != null && policy.forceClaim(this));
        this.configuration.setUsbInterface(
            this.descriptor.bInterfaceNumber(), this);
    }

    /**
     * @see UsbInterface#release()
     */
    @Override
    public void release() throws UsbClaimException, UsbException,
        UsbNotActiveException, UsbDisconnectedException
    {
        checkActive();
        checkConnected();
        this.configuration.getUsbDevice().releaseInterface(
            this.descriptor.bInterfaceNumber());
    }

    /**
     * @see UsbInterface#isClaimed()
     */
    @Override
    public boolean isClaimed()
    {
        return this.configuration.getUsbDevice().isInterfaceClaimed(
            this.descriptor.bInterfaceNumber());
    }

    /**
     * @see UsbInterface#isActive()
     */
    @Override
    public boolean isActive()
    {
        return this.configuration.getUsbInterface(this.descriptor
            .bInterfaceNumber()) == this;
    }

    /**
     * @see UsbInterface#getNumSettings()
     */
    @Override
    public int getNumSettings()
    {
        return this.configuration.getNumSettings(this.descriptor
            .bInterfaceNumber());
    }

    /**
     * @see UsbInterface#getActiveSettingNumber()
     */
    @Override
    public byte getActiveSettingNumber() throws UsbNotActiveException
    {
        checkActive();
        return this.configuration
            .getUsbInterface(this.descriptor.bInterfaceNumber())
            .getUsbInterfaceDescriptor().bAlternateSetting();
    }

    /**
     * @see UsbInterface#getActiveSetting()
     */
    @Override
    public Usb4JavaInterface getActiveSetting() throws UsbNotActiveException
    {
        checkActive();
        return this.configuration.getUsbInterface(this.descriptor
                .bInterfaceNumber());
    }

    /**
     * @see UsbInterface#getSetting(byte)
     */
    @Override
    public Usb4JavaInterface getSetting(final byte number)
    {
        return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).get(number & 0xff);
    }

    /**
     * @see UsbInterface#containsSetting(byte)
     */
    @Override
    public boolean containsSetting(final byte number)
    {
        return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).containsKey(number & 0xff);
    }

    /**
     * @see UsbInterface#getSettings()
     */
    @Override
    public List<Usb4JavaInterface> getSettings()
    {
        return Collections.unmodifiableList(new ArrayList<Usb4JavaInterface>(
            this.configuration.getSettings(
                this.descriptor.bInterfaceNumber()).values()));
    }

    /**
     * @see UsbInterface#getUsbEndpoints()
     */
    @Override
    public List<Usb4JavaEndpoint> getUsbEndpoints()
    {
        return Collections.unmodifiableList(new ArrayList<Usb4JavaEndpoint>(
            this.endpoints.values()));
    }

    /**
     * @see UsbInterface#getUsbEndpoint(byte)
     */
    @Override
    public Usb4JavaEndpoint getUsbEndpoint(final byte address)
    {
        return this.endpoints.get(address);
    }

    /**
     * @see UsbInterface#containsUsbEndpoint(byte)
     */
    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        return this.endpoints.containsKey(address);
    }

    /**
     * @see UsbInterface#getUsbConfiguration()
     */
    @Override
    public Usb4JavaConfiguration getUsbConfiguration()
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
    public String getInterfaceString() throws UsbException,
        UnsupportedEncodingException, UsbDisconnectedException
    {
        checkConnected();
        final byte iInterface = this.descriptor.iInterface();
        if (iInterface == 0) return null;
        return this.configuration.getUsbDevice().getString(iInterface);
    }
        
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return String.format("USB interface %02x", 
            this.descriptor.bInterfaceNumber());
    }    
}
