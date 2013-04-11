/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.usb.UsbStringDescriptor;

/**
 * A string descriptor.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbStringDescriptor extends SimpleUsbDescriptor implements
    UsbStringDescriptor
{
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The string data in UTF-16 format. */
    private final byte[] bString;

    /**
     * Constructor.
     * 
     * @param data
     *            The descriptor data as a byte buffer.
     */
    public SimpleUsbStringDescriptor(final ByteBuffer data)
    {
        super(data.get(0), data.get(1));

        data.position(2);
        this.bString = new byte[(bLength() - 2)];
        data.get(this.bString);
    }

    /**
     * Copy constructor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbStringDescriptor(UsbStringDescriptor descriptor)
    {
        super(descriptor.bLength(), descriptor.bDescriptorType());
        this.bString = descriptor.bString().clone();
    }

    /**
     * @see javax.usb.UsbStringDescriptor#bString()
     */
    @Override
    public byte[] bString()
    {
        return this.bString.clone();
    }

    /**
     * @see javax.usb.UsbStringDescriptor#getString()
     */
    @Override
    public String getString() throws UnsupportedEncodingException
    {
        return new String(this.bString, "UTF-16LE");
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        try
        {
            return getString();
        }
        catch (UnsupportedEncodingException e)
        {
            return super.toString();
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        final SimpleUsbStringDescriptor other = (SimpleUsbStringDescriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && Arrays.equals(this.bString, other.bString);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bLength();
        result = 37 * result + bDescriptorType();
        result = 37 * result + Arrays.hashCode(this.bString);
        return result;
    }
}
