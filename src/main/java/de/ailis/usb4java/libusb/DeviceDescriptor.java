/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */

package de.ailis.usb4java.libusb;

import java.nio.ByteBuffer;

import javax.usb.UsbDeviceDescriptor;

/**
 * A structure representing the standard USB device descriptor.
 * 
 * This descriptor is documented in section 9.6.1 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class DeviceDescriptor implements UsbDeviceDescriptor
{
    /** The native data of the descriptor structure. */
    ByteBuffer data;
    
    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */
    @Override
    public native byte bLength();

    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */
    @Override
    public native byte bDescriptorType();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bcdUSB()
     */
    @Override
    public native short bcdUSB();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bDeviceClass()
     */
    @Override
    public native byte bDeviceClass();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bDeviceSubClass()
     */
    @Override
    public native byte bDeviceSubClass();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bDeviceProtocol()
     */
    @Override
    public native byte bDeviceProtocol();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bMaxPacketSize0()
     */
    @Override
    public native byte bMaxPacketSize0();

    /**
     * @see javax.usb.UsbDeviceDescriptor#idVendor()
     */
    @Override
    public native short idVendor();

    /**
     * @see javax.usb.UsbDeviceDescriptor#idProduct()
     */
    @Override
    public native short idProduct();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bcdDevice()
     */
    @Override
    public native short bcdDevice();

    /**
     * @see javax.usb.UsbDeviceDescriptor#iManufacturer()
     */
    @Override
    public native byte iManufacturer();

    /**
     * @see javax.usb.UsbDeviceDescriptor#iProduct()
     */
    @Override
    public native byte iProduct();

    /**
     * @see javax.usb.UsbDeviceDescriptor#iSerialNumber()
     */
    @Override
    public native byte iSerialNumber();

    /**
     * @see javax.usb.UsbDeviceDescriptor#bNumConfigurations()
     */
    @Override
    public native byte bNumConfigurations();

    /**
     * Returns a dump of this descriptor.
     * 
     * @param handle
     *            The USB device handle for resolving string descriptors. If
     *            null then no strings are resolved.
     * @return The descriptor dump.
     */
    public String dump(final DeviceHandle handle)
    {
        final int iManufacturer = iManufacturer();
        String sManufacturer =
            LibUSB.getStringDescriptor(handle, iManufacturer);
        if (sManufacturer == null) sManufacturer = "";
        final int iProduct = iProduct();
        String sProduct = LibUSB.getStringDescriptor(handle, iProduct);
        if (sProduct == null) sProduct = "";
        final int iSerialNumber = iSerialNumber();
        String sSerialNumber =
            LibUSB.getStringDescriptor(handle, iSerialNumber);
        if (sSerialNumber == null) sSerialNumber = "";
        return String.format("Device Descriptor:%n"
            + "  bLength               %5d%n"
            + "  bDescriptorType       %5d%n"
            + "  bcdUSB                %5s%n"
            + "  bDeviceClass          %5d %s%n"
            + "  bDeviceSubClass       %5d%n"
            + "  bDeviceProtocol       %5d%n"
            + "  bMaxPacketSize0       %5d%n"
            + "  idVendor             %#06x%n"
            + "  idProduct            %#06x%n"
            + "  bcdDevice             %5s%n"
            + "  iManufacturer         %5d %s%n"
            + "  iProduct              %5d %s%n"
            + "  iSerialNumber         %5d %s%n"
            + "  bNumConfigurations    %5d%n",
            bLength(), bDescriptorType(), DumpUtils.decodeBCD(bcdUSB()),
            bDeviceClass(), DumpUtils.getUSBClassName(bDeviceClass()),
            bDeviceSubClass(), bDeviceProtocol(),
            bMaxPacketSize0(), idVendor(), idProduct(),
            DumpUtils.decodeBCD(bcdDevice()), iManufacturer, sManufacturer,
            iProduct, sProduct, iSerialNumber, sSerialNumber,
            bNumConfigurations());
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        final DeviceDescriptor other = (DeviceDescriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && idProduct() == other.idProduct()
            && idVendor() == other.idVendor()
            && bcdDevice() == other.bcdDevice()
            && bcdUSB() == other.bcdUSB()
            && bDescriptorType() == other.bDescriptorType()
            && bDeviceClass() == other.bDeviceClass()
            && bDeviceProtocol() == other.bDeviceProtocol()
            && bDeviceSubClass() == other.bDeviceSubClass()
            && bLength() == other.bLength()
            && bMaxPacketSize0() == other.bMaxPacketSize0()
            && bNumConfigurations() == other.bNumConfigurations()
            && iManufacturer() == other.iManufacturer()
            && iProduct() == other.iProduct()
            && iSerialNumber() == other.iSerialNumber();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bLength();
        result = 37 * result + bDescriptorType();
        result = 37 * result + bcdUSB();
        result = 37 * result + bDeviceClass();
        result = 37 * result + bDeviceSubClass();
        result = 37 * result + bDeviceProtocol();
        result = 37 * result + bMaxPacketSize0();
        result = 37 * result + idVendor();
        result = 37 * result + idProduct();
        result = 37 * result + bcdDevice();
        result = 37 * result + iManufacturer();
        result = 37 * result + iProduct();
        result = 37 * result + iSerialNumber();
        result = 37 * result + bNumConfigurations();
        return result;
    }
}
