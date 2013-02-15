/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbEndpointDescriptor;

import de.ailis.usb4java.jni.USB_Endpoint_Descriptor;

/**
 * USB endpoint descriptor wrapping a libusb endpoint descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaEndpointDescriptor extends
    Usb4JavaDescriptor<USB_Endpoint_Descriptor> implements UsbEndpointDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB endpoint descriptor.
     */
    public Usb4JavaEndpointDescriptor(final USB_Endpoint_Descriptor descriptor)
    {
        super(descriptor);
    }

    /**
     * @see UsbEndpointDescriptor#bEndpointAddress()
     */
    @Override
    public byte bEndpointAddress()
    {
        return (byte) (this.descriptor.bEndpointAddress() & 0xff);
    }

    /**
     * @see UsbEndpointDescriptor#bmAttributes()
     */
    @Override
    public byte bmAttributes()
    {
        return (byte) (this.descriptor.bmAttributes() & 0xff);
    }

    /**
     * @see UsbEndpointDescriptor#wMaxPacketSize()
     */
    @Override
    public short wMaxPacketSize()
    {
        return (short) (this.descriptor.wMaxPacketSize() & 0xffff);
    }

    /**
     * @see UsbEndpointDescriptor#bInterval()
     */
    @Override
    public byte bInterval()
    {
        return (byte) (this.descriptor.bInterval() & 0xff);
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
        final Usb4JavaEndpointDescriptor other = (Usb4JavaEndpointDescriptor) o;
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
