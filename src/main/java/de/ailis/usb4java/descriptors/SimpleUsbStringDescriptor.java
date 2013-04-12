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
 * Simple string descriptor.
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
    public SimpleUsbStringDescriptor(final UsbStringDescriptor descriptor)
    {
        super(descriptor.bLength(), descriptor.bDescriptorType());
        this.bString = descriptor.bString().clone();
    }

    @Override
    public byte[] bString()
    {
        return this.bString.clone();
    }

    @Override
    public String getString() throws UnsupportedEncodingException
    {
        return new String(this.bString, "UTF-16LE");
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        final SimpleUsbStringDescriptor other = (SimpleUsbStringDescriptor) obj;
        return Arrays.equals(this.bString, other.bString);
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bLength();
        result = 37 * result + bDescriptorType();
        result = 37 * result + Arrays.hashCode(this.bString);
        return result;
    }
    
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
}
