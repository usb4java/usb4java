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
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;

import de.ailis.usb4java.descriptors.LibUsbEndpointDescriptor;
import de.ailis.usb4java.descriptors.LibUsbInterfaceDescriptor;
import de.ailis.usb4java.jni.USB_Endpoint_Descriptor;
import de.ailis.usb4java.jni.USB_Interface_Descriptor;
import de.ailis.usb4java.support.UsbLock;


/**
 * usb4java implementation of UsbInterface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class LibUsbInterface implements UsbInterface
{
    /** The USB configuration. */
    private final LibUsbConfiguration configuration;

    /** The interface descriptor. */
    private final UsbInterfaceDescriptor descriptor;

    /** The endpoint address to endpoints mapping. */
    private final Map<Byte, LibUsbEndpoint> endpointMap =
            new HashMap<Byte, LibUsbEndpoint>();

    /** The endpoints. */
    private final List<LibUsbEndpoint> endpoints;


    /**
     * Constructor.
     *
     * @param configuration
     *            The USB configuration.
     * @param lowLevelDescriptor
     *            The low-level USB interface descriptor.
     * @param device
     *            The USB device.
     */

    public LibUsbInterface(final LibUsbConfiguration configuration,
        final USB_Interface_Descriptor lowLevelDescriptor,
        final LibUsbDevice device)
    {
        this.configuration = configuration;
        this.descriptor = new LibUsbInterfaceDescriptor(lowLevelDescriptor);

        final List<LibUsbEndpoint> endpoints = new ArrayList<LibUsbEndpoint>();
        for (final USB_Endpoint_Descriptor desc : lowLevelDescriptor
                .endpoint())
        {
            final UsbEndpointDescriptor descriptor =
                    new LibUsbEndpointDescriptor(desc);
            final LibUsbEndpoint endpoint =
                    new LibUsbEndpoint(this, descriptor, device);
            this.endpointMap.put(descriptor.bEndpointAddress(), endpoint);
            endpoints.add(endpoint);
        }
        this.endpoints = Collections.unmodifiableList(endpoints);
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
        final LibUsbDevice device = this.configuration.getUsbDevice();
        UsbLock.acquire();
        try
        {
            /*
             * device.setActiveUsbConfigurationNumber(this.configuration
             * .getUsbConfigurationDescriptor().bConfigurationValue());
             */
            device.claimInterface(this.descriptor.bInterfaceNumber(),
                policy != null && policy.forceClaim(this));
            this.configuration.setUsbInterface(
                this.descriptor.bInterfaceNumber(), this);
        }
        finally
        {
            UsbLock.release();
        }
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
    public LibUsbInterface getActiveSetting() throws UsbNotActiveException
    {
        checkActive();
        return this.configuration.getUsbInterface(this.descriptor
                .bInterfaceNumber());
    }


    /**
     * @see UsbInterface#getSetting(byte)
     */

    @Override
    public LibUsbInterface getSetting(final byte number)
    {
        return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).get(number);
    }


    /**
     * @see UsbInterface#containsSetting(byte)
     */

    @Override
    public boolean containsSetting(final byte number)
    {
        return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).containsKey(number);
    }


    /**
     * @see UsbInterface#getSettings()
     */

    @Override
    public List<LibUsbInterface> getSettings()
    {
        return Collections.unmodifiableList(new ArrayList<LibUsbInterface>(
            this.configuration.getSettings(
                this.descriptor.bInterfaceNumber()).values()));
    }


    /**
     * @see UsbInterface#getUsbEndpoints()
     */

    @Override
    public List<LibUsbEndpoint> getUsbEndpoints()
    {
        return this.endpoints;
    }


    /**
     * @see UsbInterface#getUsbEndpoint(byte)
     */

    @Override
    public LibUsbEndpoint getUsbEndpoint(final byte address)
    {
        return this.endpointMap.get(address);
    }


    /**
     * @see UsbInterface#containsUsbEndpoint(byte)
     */

    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        return this.endpointMap.containsKey(address);
    }


    /**
     * @see UsbInterface#getUsbConfiguration()
     */

    @Override
    public LibUsbConfiguration getUsbConfiguration()
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
}
