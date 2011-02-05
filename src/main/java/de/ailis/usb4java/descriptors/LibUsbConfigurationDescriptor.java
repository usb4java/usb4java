/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbConfigurationDescriptor;

import de.ailis.usb4java.jni.USB_Config_Descriptor;


/**
 * USB configuration descriptor wrapping a libusb configuration descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class LibUsbConfigurationDescriptor extends
    LibUsbDescriptor<USB_Config_Descriptor> implements
    UsbConfigurationDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB configuration descriptor.
     */

    public LibUsbConfigurationDescriptor(final USB_Config_Descriptor
        descriptor)
    {
        super(descriptor);
    }


    /**
     * @see UsbConfigurationDescriptor#wTotalLength()
     */

    @Override
    public short wTotalLength()
    {
        return (short) (this.descriptor.wTotalLength() & 0xffff);
    }


    /**
     * @see UsbConfigurationDescriptor#bNumInterfaces()
     */

    @Override
    public byte bNumInterfaces()
    {
        return (byte) (this.descriptor.bNumInterfaces() & 0xff);
    }


    /**
     * @see UsbConfigurationDescriptor#bConfigurationValue()
     */

    @Override
    public byte bConfigurationValue()
    {
        return (byte) (this.descriptor.bConfigurationValue() & 0xff);
    }


    /**
     * @see UsbConfigurationDescriptor#iConfiguration()
     */

    @Override
    public byte iConfiguration()
    {
        return (byte) (this.descriptor.iConfiguration() & 0xff);
    }


    /**
     * @see UsbConfigurationDescriptor#bmAttributes()
     */

    @Override
    public byte bmAttributes()
    {
        return (byte) (this.descriptor.bmAttributes() & 0xff);
    }


    /**
     * @see UsbConfigurationDescriptor#bMaxPower()
     */

    @Override
    public byte bMaxPower()
    {
        return (byte) (this.descriptor.bMaxPower() & 0xff);
    }


    /**
     * @see Object#equals(Object)
     */

    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (this == o) return true;
        if (o.getClass() != getClass()) return false;
        final LibUsbConfigurationDescriptor other =
            (LibUsbConfigurationDescriptor) o;
        return this.descriptor.equals(other.descriptor);
    }


    /**
     * @see Object#hashCode()
     */

    @Override
    public int hashCode()
    {
        return this.descriptor.hashCode();
    }
}
