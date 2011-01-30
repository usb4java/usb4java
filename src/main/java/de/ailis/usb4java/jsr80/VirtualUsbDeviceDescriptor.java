/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbConst;
import javax.usb.UsbDescriptor;
import javax.usb.UsbDeviceDescriptor;


/**
 * Virtual USB device descriptor used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class VirtualUsbDeviceDescriptor implements UsbDeviceDescriptor
{
    /**
     * @see UsbDescriptor#bLength()
     */

    @Override
    public byte bLength()
    {
        return UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE;
    }


    /**
     * @see UsbDescriptor#bDescriptorType()
     */

    @Override
    public byte bDescriptorType()
    {
        return UsbConst.DESCRIPTOR_TYPE_DEVICE;
    }


    /**
     * @see UsbDeviceDescriptor#bcdUSB()
     */

    @Override
    public short bcdUSB()
    {
        return 0x101;
    }


    /**
     * @see UsbDeviceDescriptor#bDeviceClass()
     */

    @Override
    public byte bDeviceClass()
    {
        return UsbConst.HUB_CLASSCODE;
    }


    /**
     * @see UsbDeviceDescriptor#bDeviceSubClass()
     */

    @Override
    public byte bDeviceSubClass()
    {
        return 0;
    }


    /**
     * @see UsbDeviceDescriptor#bDeviceProtocol()
     */

    @Override
    public byte bDeviceProtocol()
    {
        return 0;
    }


    /**
     * @see UsbDeviceDescriptor#bMaxPacketSize0()
     */

    @Override
    public byte bMaxPacketSize0()
    {
        return 8;
    }


    /**
     * @see UsbDeviceDescriptor#idVendor()
     */

    @Override
    public short idVendor()
    {
        return (short) 0xffff;
    }


    /**
     * @see UsbDeviceDescriptor#idProduct()
     */

    @Override
    public short idProduct()
    {
        return (short) 0xffff;
    }


    /**
     * @see UsbDeviceDescriptor#bcdDevice()
     */

    @Override
    public short bcdDevice()
    {
        return 0;
    }


    /**
     * @see UsbDeviceDescriptor#iManufacturer()
     */

    @Override
    public byte iManufacturer()
    {
        return 1;
    }


    /**
     * @see UsbDeviceDescriptor#iProduct()
     */

    @Override
    public byte iProduct()
    {
        return 2;
    }


    /**
     * @see UsbDeviceDescriptor#iSerialNumber()
     */

    @Override
    public byte iSerialNumber()
    {
        return 3;
    }


    /**
     * @see UsbDeviceDescriptor#bNumConfigurations()
     */

    @Override
    public byte bNumConfigurations()
    {
        return 1;
    }
}
