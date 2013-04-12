/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.usb.UsbStringDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Simple string descriptor.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbStringDescriptor extends SimpleUsbDescriptor
    implements UsbStringDescriptor
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
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SimpleUsbStringDescriptor other = (SimpleUsbStringDescriptor) obj;
        return new EqualsBuilder()
            .append(bLength(), other.bLength())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(this.bString, other.bString)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bDescriptorType())
            .append(bLength())
            .append(this.bString)
            .toHashCode();
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
