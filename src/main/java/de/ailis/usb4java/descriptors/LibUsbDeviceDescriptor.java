/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbDeviceDescriptor;

import de.ailis.usb4java.jni.USB_Device_Descriptor;


/**
 * USB device descriptor wrapping a libusb device descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class LibUsbDeviceDescriptor extends
        LibUsbDescriptor<USB_Device_Descriptor> implements UsbDeviceDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB device descriptor.
     */

    public LibUsbDeviceDescriptor(final USB_Device_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see UsbDeviceDescriptor#bcdUSB()
     */

    @Override
    public short bcdUSB()
    {
        return (short) (this.descriptor.bcdUSB() & 0xffff);
    }


    /**
     * @see UsbDeviceDescriptor#bDeviceClass()
     */

    @Override
    public byte bDeviceClass()
    {
        return (byte) (this.descriptor.bDeviceClass() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#bDeviceSubClass()
     */

    @Override
    public byte bDeviceSubClass()
    {
        return (byte) (this.descriptor.bDeviceSubClass() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#bDeviceProtocol()
     */

    @Override
    public byte bDeviceProtocol()
    {
        return (byte) (this.descriptor.bDeviceProtocol() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#bMaxPacketSize0()
     */

    @Override
    public byte bMaxPacketSize0()
    {
        return (byte) (this.descriptor.bMaxPacketSize0() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#idVendor()
     */

    @Override
    public short idVendor()
    {
        return (short) (this.descriptor.idVendor() & 0xffff);
    }


    /**
     * @see UsbDeviceDescriptor#idProduct()
     */

    @Override
    public short idProduct()
    {
        return (short) (this.descriptor.idProduct() & 0xffff);
    }


    /**
     * @see UsbDeviceDescriptor#bcdDevice()
     */

    @Override
    public short bcdDevice()
    {
        return (short) (this.descriptor.bcdDevice() & 0xffff);
    }


    /**
     * @see UsbDeviceDescriptor#iManufacturer()
     */

    @Override
    public byte iManufacturer()
    {
        return (byte) (this.descriptor.iManufacturer() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#iProduct()
     */

    @Override
    public byte iProduct()
    {
        return (byte) (this.descriptor.iProduct() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#iSerialNumber()
     */

    @Override
    public byte iSerialNumber()
    {
        return (byte) (this.descriptor.iSerialNumber() & 0xff);
    }


    /**
     * @see UsbDeviceDescriptor#bNumConfigurations()
     */

    @Override
    public byte bNumConfigurations()
    {
        return (byte) (this.descriptor.bNumConfigurations() & 0xff);
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
        final LibUsbDeviceDescriptor other = (LibUsbDeviceDescriptor) o;
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
