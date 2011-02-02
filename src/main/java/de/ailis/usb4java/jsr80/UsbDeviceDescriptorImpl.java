/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbDeviceDescriptor;

import de.ailis.usb4java.USB_Device_Descriptor;


/**
 * usb4java implementation of JSR-80 UsbDeviceDescriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbDeviceDescriptorImpl extends
        UsbDescriptorImpl<USB_Device_Descriptor> implements UsbDeviceDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB device descriptor.
     */

    public UsbDeviceDescriptorImpl(final USB_Device_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bcdUSB()
     */

    @Override
    public short bcdUSB()
    {
        return (short) (this.descriptor.bcdUSB() & 0xffff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bDeviceClass()
     */

    @Override
    public byte bDeviceClass()
    {
        return (byte) (this.descriptor.bDeviceClass() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bDeviceSubClass()
     */

    @Override
    public byte bDeviceSubClass()
    {
        return (byte) (this.descriptor.bDeviceSubClass() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bDeviceProtocol()
     */

    @Override
    public byte bDeviceProtocol()
    {
        return (byte) (this.descriptor.bDeviceProtocol() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bMaxPacketSize0()
     */

    @Override
    public byte bMaxPacketSize0()
    {
        return (byte) (this.descriptor.bMaxPacketSize0() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#idVendor()
     */

    @Override
    public short idVendor()
    {
        return (short) (this.descriptor.idVendor() & 0xffff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#idProduct()
     */

    @Override
    public short idProduct()
    {
        return (short) (this.descriptor.idProduct() & 0xffff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bcdDevice()
     */

    @Override
    public short bcdDevice()
    {
        return (short) (this.descriptor.bcdDevice() & 0xffff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#iManufacturer()
     */

    @Override
    public byte iManufacturer()
    {
        return (byte) (this.descriptor.iManufacturer() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#iProduct()
     */

    @Override
    public byte iProduct()
    {
        return (byte) (this.descriptor.iProduct() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#iSerialNumber()
     */

    @Override
    public byte iSerialNumber()
    {
        return (byte) (this.descriptor.iSerialNumber() & 0xff);
    }


    /**
     * @see javax.usb.UsbDeviceDescriptor#bNumConfigurations()
     */

    @Override
    public byte bNumConfigurations()
    {
        return (byte) (this.descriptor.bNumConfigurations() & 0xff);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (this == o) return true;
        if (o.getClass() != getClass()) return false;
        final UsbDeviceDescriptorImpl other = (UsbDeviceDescriptorImpl) o;
        return this.descriptor.equals(other.descriptor);
    }


    /**
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode()
    {
        return this.descriptor.hashCode();
    }
}
