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

final class UsbInterfaceDescriptorImpl extends
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
    public byte bInterfaceNumber()
    {
        return (byte) (this.descriptor.bInterfaceNumber() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bAlternateSetting()
     */

    @Override
    public byte bAlternateSetting()
    {
        return (byte) (this.descriptor.bAlternateSetting() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bNumEndpoints()
     */

    @Override
    public byte bNumEndpoints()
    {
        return (byte) (this.descriptor.bNumEndpoints() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceClass()
     */

    @Override
    public byte bInterfaceClass()
    {
        return (byte) (this.descriptor.bInterfaceClass() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceSubClass()
     */

    @Override
    public byte bInterfaceSubClass()
    {
        return (byte) (this.descriptor.bInterfaceSubClass() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceProtocol()
     */

    @Override
    public byte bInterfaceProtocol()
    {
        return (byte) (this.descriptor.bInterfaceProtocol() & 0xff);
    }


    /**
     * @see javax.usb.UsbInterfaceDescriptor#iInterface()
     */

    @Override
    public byte iInterface()
    {
        return (byte) (this.descriptor.iInterface() & 0xff);
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
        final UsbInterfaceDescriptorImpl other = (UsbInterfaceDescriptorImpl) o;
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
