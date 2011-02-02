/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbClaimException;
import javax.usb.UsbConfiguration;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;

import de.ailis.usb4java.USBLock;
import de.ailis.usb4java.USB_Interface_Descriptor;


/**
 * usb4java implementation of UsbInterface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbInterfaceImpl implements UsbInterface
{
    /** The USB configuration. */
    private final UsbConfigurationImpl configuration;

    /** The interface descriptor. */
    private final UsbInterfaceDescriptor descriptor;


    /**
     * Constructor.
     *
     * @param configuration
     *            The USB configuration.
     * @param lowLevelDescriptor
     *            The low-level USB interface descriptor.
     */

    public UsbInterfaceImpl(final UsbConfigurationImpl configuration,
        final USB_Interface_Descriptor lowLevelDescriptor)
    {
        this.configuration = configuration;
        this.descriptor = new UsbInterfaceDescriptorImpl(lowLevelDescriptor);
    }


    /**
     * Checks if the configuration is active. If not then an
     * UsbNotActiveException is thrown.
     */

    private void checkConfigurationActive()
    {
        if (!this.configuration.isActive())
            throw new UsbNotActiveException("Configuration is not active");
    }


    /**
     * Checks if setting is active. Throws an UsbNotActiveException if not.
     */

    private void checkSettingActive()
    {
        checkConfigurationActive();
        if (!isActive())
            throw new UsbNotActiveException("Setting is not active");
    }


    /**
     * @see UsbInterface#claim()
     */

    @Override
    public void claim() throws UsbException
    {
        claim(null);
    }


    /**
     * @see UsbInterface#claim(UsbInterfacePolicy)
     *
     *      TODO Policy is ignored
     */

    @Override
    public void claim(final UsbInterfacePolicy policy)
        throws UsbClaimException, UsbException
    {
        final AbstractDevice device = (AbstractDevice) this.configuration
                .getUsbDevice();
        USBLock.acquire();
        try
        {
            device.setActiveUsbConfigurationNumber(this.configuration
                    .getUsbConfigurationDescriptor().bConfigurationValue());
            device.claimInterface(this.descriptor.bInterfaceNumber());
            this.configuration.setUsbInterface(this.descriptor.bInterfaceNumber(), this);
        }
        finally
        {
            USBLock.release();
        }
    }


    /**
     * @see UsbInterface#release()
     */

    @Override
    public void release() throws UsbClaimException, UsbException
    {
        ((AbstractDevice) this.configuration.getUsbDevice())
                .releaseInterface(this.descriptor.bInterfaceNumber());
    }


    /**
     * @see UsbInterface#isClaimed()
     */

    @Override
    public boolean isClaimed()
    {
        return ((AbstractDevice) this.configuration.getUsbDevice())
                .isInterfaceClaimed(this.descriptor.bInterfaceNumber());
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
        return ((this.configuration)
                .getNumSettings(this.descriptor.bInterfaceNumber()));
    }


    /**
     * @see UsbInterface#getActiveSettingNumber()
     */

    @Override
    public byte getActiveSettingNumber()
    {
        checkConfigurationActive();
        checkSettingActive();
        return this.configuration
                .getUsbInterface(this.descriptor.bInterfaceNumber())
                .getUsbInterfaceDescriptor().bAlternateSetting();
    }


    /**
     * @see UsbInterface#getActiveSetting()
     */

    @Override
    public UsbInterface getActiveSetting()
    {
        checkConfigurationActive();
        checkSettingActive();
        return this.configuration.getUsbInterface(this.descriptor
                .bInterfaceNumber());
    }


    /**
     * @see UsbInterface#getSetting(byte)
     */

    @Override
    public UsbInterface getSetting(final byte number)
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
    public List<UsbInterface> getSettings()
    {
        return Collections.unmodifiableList(new ArrayList<UsbInterface>(
            (this.configuration).getSettings(
                this.descriptor.bInterfaceNumber()).values()));
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
        return this.descriptor;
    }


    /**
     * @see UsbInterface#getInterfaceString()
     */

    @Override
    public String getInterfaceString() throws UsbException,
        UnsupportedEncodingException
    {
        final byte iInterface = this.descriptor.iInterface();
        if (iInterface == 0) return null;
        return this.configuration.getUsbDevice().getString(iInterface);
    }
}
