/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbConfigurationDescriptor;

import de.ailis.usb4java.USB_Config_Descriptor;


/**
 * usb4java implementation of JSR-80 UsbConfigurationDescriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbConfigurationDescriptorImpl extends
        UsbDescriptorImpl<USB_Config_Descriptor> implements
        UsbConfigurationDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB configuration descriptor.
     */

    public UsbConfigurationDescriptorImpl(final USB_Config_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#wTotalLength()
     */

    @Override
    public final short wTotalLength()
    {
        return (short) (this.descriptor.wTotalLength() & 0xffff);
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bNumInterfaces()
     */

    @Override
    public final byte bNumInterfaces()
    {
        return (byte) (this.descriptor.bNumInterfaces() & 0xff);
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bConfigurationValue()
     */

    @Override
    public final byte bConfigurationValue()
    {
        return (byte) (this.descriptor.bConfigurationValue() & 0xff);
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#iConfiguration()
     */

    @Override
    public final byte iConfiguration()
    {
        return (byte) (this.descriptor.iConfiguration() & 0xff);
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bmAttributes()
     */

    @Override
    public final byte bmAttributes()
    {
        return (byte) (this.descriptor.bmAttributes() & 0xff);
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bMaxPower()
     */

    @Override
    public final byte bMaxPower()
    {
        return (byte) (this.descriptor.MaxPower() & 0xff);
    }
}
