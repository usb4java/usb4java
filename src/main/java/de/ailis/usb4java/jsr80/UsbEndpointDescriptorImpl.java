/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbEndpointDescriptor;

import de.ailis.usb4java.USB_Endpoint_Descriptor;


/**
 * usb4java implementation of JSR-80 UsbEndpointDescriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbEndpointDescriptorImpl extends
        UsbDescriptorImpl<USB_Endpoint_Descriptor> implements
        UsbEndpointDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB endpoint descriptor.
     */

    public UsbEndpointDescriptorImpl(final USB_Endpoint_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see javax.usb.UsbEndpointDescriptor#bEndpointAddress()
     */

    @Override
    public byte bEndpointAddress()
    {
        return (byte) (this.descriptor.bEndpointAddress() & 0xff);
    }


    /**
     * @see javax.usb.UsbEndpointDescriptor#bmAttributes()
     */

    @Override
    public byte bmAttributes()
    {
        return (byte) (this.descriptor.bmAttributes() & 0xff);
    }


    /**
     * @see javax.usb.UsbEndpointDescriptor#wMaxPacketSize()
     */

    @Override
    public short wMaxPacketSize()
    {
        return (short) (this.descriptor.wMaxPacketSize() & 0xffff);
    }


    /**
     * @see javax.usb.UsbEndpointDescriptor#bInterval()
     */

    @Override
    public byte bInterval()
    {
        return (byte) (this.descriptor.bInterval() & 0xff);
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
        final UsbEndpointDescriptorImpl other = (UsbEndpointDescriptorImpl) o;
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
