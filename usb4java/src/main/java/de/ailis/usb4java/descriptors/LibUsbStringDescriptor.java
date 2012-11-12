/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbStringDescriptor;

import de.ailis.usb4java.jni.USB_String_Descriptor;


/**
 * USB string descriptor wrapping a libusb string descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class LibUsbStringDescriptor extends
        LibUsbDescriptor<USB_String_Descriptor> implements UsbStringDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB string descriptor.
     */

    public LibUsbStringDescriptor(final USB_String_Descriptor descriptor)
    {
        super(descriptor);
    }


    /**
     * @see UsbStringDescriptor#bString()
     */

    @Override
    public byte[] bString()
    {
        return this.descriptor.toString().getBytes();
    }


    /**
     * @see UsbStringDescriptor#getString()
     */

    @Override
    public String getString()
    {
        return this.descriptor.toString();
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
        final LibUsbStringDescriptor other = (LibUsbStringDescriptor) o;
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
