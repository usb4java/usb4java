/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbInterfaceDescriptor;

import de.ailis.usb4java.USB_Interface_Descriptor;


/**
 * usb4java implementation of JSR-80 UsbInterfaceDescriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbInterfaceDescriptorImpl extends
        UsbDescriptorImpl<USB_Interface_Descriptor> implements
        UsbInterfaceDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB interface descriptor.
     */

    public UsbInterfaceDescriptorImpl(final USB_Interface_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceNumber()
     */

    @Override
    public final byte bInterfaceNumber()
    {
        return (byte) (this.descriptor.bInterfaceNumber() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bAlternateSetting()
     */

    @Override
    public final byte bAlternateSetting()
    {
        return (byte) (this.descriptor.bAlternateSetting() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bNumEndpoints()
     */

    @Override
    public final byte bNumEndpoints()
    {
        return (byte) (this.descriptor.bNumEndpoints() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceClass()
     */

    @Override
    public final byte bInterfaceClass()
    {
        return (byte) (this.descriptor.bInterfaceClass() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceSubClass()
     */

    @Override
    public final byte bInterfaceSubClass()
    {
        return (byte) (this.descriptor.bInterfaceSubClass() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceProtocol()
     */

    @Override
    public final byte bInterfaceProtocol()
    {
        return (byte) (this.descriptor.bInterfaceProtocol() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#iInterface()
     */

    @Override
    public final byte iInterface()
    {
        return (byte) (this.descriptor.iInterface() & 0xff);
    }
}
