/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbDescriptor;

import de.ailis.usb4java.USB_Descriptor_Header;


/**
 * usb4java implementation of JSR-80 UsbDescriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @param <T>
 *            The descriptor type.
 */

public abstract class UsbDescriptorImpl<T extends USB_Descriptor_Header>
        implements UsbDescriptor
{
    /** The low level USB descriptor header. */
    protected final T descriptor;


    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB configuration descriptor.
     */

    public UsbDescriptorImpl(final T descriptor)
    {
        this.descriptor = descriptor;
    }


    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */

    @Override
    public final byte bLength()
    {
        return (byte) (this.descriptor.bLength() & 0xff);
    }


    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */

    @Override
    public final byte bDescriptorType()
    {
        return (byte) (this.descriptor.bDescriptorType() & 0xff);
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
        final UsbDescriptorImpl<?> other = (UsbDescriptorImpl<?>) o;
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
