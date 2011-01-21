/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import javax.usb.UsbConfigurationDescriptor;


/**
 * UsbConfigurationDescriptor implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbConfigurationDescriptorImpl implements
        UsbConfigurationDescriptor
{
    /** The low-level USB config desciptor. */
    private final USB_Config_Descriptor configDescriptor;


    /**
     * Constructor.
     *
     * @param configDescriptor
     *            The low-level config descriptor.
     */

    UsbConfigurationDescriptorImpl(final USB_Config_Descriptor configDescriptor)
    {
        this.configDescriptor = configDescriptor;
    }


    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */

    @Override
    public byte bLength()
    {
        return this.configDescriptor.bLength();
    }


    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */

    @Override
    public byte bDescriptorType()
    {
        return this.configDescriptor.bDescriptorType();
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#wTotalLength()
     */

    @Override
    public short wTotalLength()
    {
        return this.configDescriptor.wTotalLength();
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bNumInterfaces()
     */

    @Override
    public byte bNumInterfaces()
    {
        return this.configDescriptor.bNumInterfaces();
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bConfigurationValue()
     */

    @Override
    public byte bConfigurationValue()
    {
        return this.configDescriptor.bConfigurationValue();
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#iConfiguration()
     */

    @Override
    public byte iConfiguration()
    {
        return this.configDescriptor.iConfiguration();
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bmAttributes()
     */

    @Override
    public byte bmAttributes()
    {
        return this.configDescriptor.bmAttributes();
    }


    /**
     * @see javax.usb.UsbConfigurationDescriptor#bMaxPower()
     */

    @Override
    public byte bMaxPower()
    {
        return this.configDescriptor.MaxPower();
    }
}
