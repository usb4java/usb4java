/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import javax.usb.UsbInterfaceDescriptor;

import de.ailis.usb4java.jni.USB_Interface_Descriptor;


/**
 * UsbInterfaceDescriptor implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbInterfaceDescriptorImpl implements UsbInterfaceDescriptor
{
    /** The low-level USB interface descriptor. */
    private final USB_Interface_Descriptor descriptor;


    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB interface descriptor.
     */

    UsbInterfaceDescriptorImpl(final USB_Interface_Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }


    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */

    @Override
    public byte bLength()
    {
        return this.descriptor.bLength();
    }


    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */

    @Override
    public byte bDescriptorType()
    {
        return this.descriptor.bDescriptorType();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceNumber()
     */

    @Override
    public byte bInterfaceNumber()
    {
        return this.descriptor.bInterfaceNumber();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bAlternateSetting()
     */

    @Override
    public byte bAlternateSetting()
    {
        return this.descriptor.bAlternateSetting();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bNumEndpoints()
     */

    @Override
    public byte bNumEndpoints()
    {
        return this.descriptor.bNumEndpoints();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceClass()
     */

    @Override
    public byte bInterfaceClass()
    {
        return this.descriptor.bInterfaceClass();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceSubClass()
     */

    @Override
    public byte bInterfaceSubClass()
    {
        return this.descriptor.bInterfaceSubClass();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceProtocol()
     */

    @Override
    public byte bInterfaceProtocol()
    {
        return this.descriptor.bInterfaceProtocol();
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#iInterface()
     */

    @Override
    public byte iInterface()
    {
        return this.descriptor.iInterface();
    }
}
