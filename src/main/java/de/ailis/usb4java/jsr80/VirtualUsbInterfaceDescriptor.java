/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbConst;
import javax.usb.UsbInterfaceDescriptor;


/**
 * Virtual USB interface descriptor used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class VirtualUsbInterfaceDescriptor implements UsbInterfaceDescriptor
{
    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */

    @Override
    public byte bLength()
    {
        return UsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE;
    }


    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */

    @Override
    public byte bDescriptorType()
    {
        return UsbConst.DESCRIPTOR_TYPE_INTERFACE;
    }


    /**
     * @see UsbInterfaceDescriptor#bInterfaceNumber()
     */

    @Override
    public byte bInterfaceNumber()
    {
        return 0;
    }


    /**
     * @see UsbInterfaceDescriptor#bAlternateSetting()
     */

    @Override
    public byte bAlternateSetting()
    {
        return 0;
    }


    /**
     * @see UsbInterfaceDescriptor#bNumEndpoints()
     */

    @Override
    public byte bNumEndpoints()
    {
        return 0;
    }


    /**
     * @see UsbInterfaceDescriptor#bInterfaceClass()
     */

    @Override
    public byte bInterfaceClass()
    {
        return UsbConst.HUB_CLASSCODE;
    }


    /**
     * @see UsbInterfaceDescriptor#bInterfaceSubClass()
     */

    @Override
    public byte bInterfaceSubClass()
    {
        return 0;
    }

    /**
     * @see UsbInterfaceDescriptor#bInterfaceProtocol()
     */

    @Override
    public byte bInterfaceProtocol()
    {
        return 0;
    }


    /**
     * @see UsbInterfaceDescriptor#iInterface()
     */

    @Override
    public byte iInterface()
    {
        return 0;
    }
}
