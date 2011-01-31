/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbStringDescriptor;

import de.ailis.usb4java.USB_String_Descriptor;


/**
 * usb4java implementation of JSR-80 UsbStringDescriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbStringDescriptorImpl extends
        UsbDescriptorImpl<USB_String_Descriptor> implements UsbStringDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB string descriptor.
     */

    public UsbStringDescriptorImpl(final USB_String_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see javax.usb.UsbStringDescriptor#bString()
     */

    @Override
    public final byte[] bString()
    {
        return this.descriptor.toString().getBytes();
    }


    /**
     * @see javax.usb.UsbStringDescriptor#getString()
     */

    @Override
    public final String getString()
    {
        return this.descriptor.toString();
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
        final UsbStringDescriptorImpl other = (UsbStringDescriptorImpl) o;
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
