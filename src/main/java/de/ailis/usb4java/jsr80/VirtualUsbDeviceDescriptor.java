/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbConst;
import javax.usb.UsbDeviceDescriptor;


/**
 * Virtual USB device descriptor used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class VirtualUsbDeviceDescriptor implements UsbDeviceDescriptor
{
    /** The USB specification release number of the virtual usb device. */
    private static final int BCD_USB = 0x101;

    /** The maximum packet size of this virtual device. */
    private static final byte MAX_PACKET_SIZE_0 = 8;

    /** The manufacturer string descriptor index. */
    private static final byte MANUFACTURER_INDEX = 1;

    /** The product string descriptor index. */
    private static final byte PRODUCT_INDEX = 2;

    /** The serial number string descriptor index. */
    private static final byte SERIAL_NUMBER_INDEX = 3;


    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */

    @Override
    public byte bLength()
    {
        return UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE;
    }


    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
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
        return BCD_USB;
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
        return MAX_PACKET_SIZE_0;
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
        return MANUFACTURER_INDEX;
    }


    /**
     * @see UsbDeviceDescriptor#iProduct()
     */

    @Override
    public byte iProduct()
    {
        return PRODUCT_INDEX;
    }


    /**
     * @see UsbDeviceDescriptor#iSerialNumber()
     */

    @Override
    public byte iSerialNumber()
    {
        return SERIAL_NUMBER_INDEX;
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
