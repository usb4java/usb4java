/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
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

    /** The string data in UTF-16LE encoding. */
    private final byte[] bString;

    /**
     * Constructs a new string descriptor by reading the descriptor data
     * from the specified byte buffer.
     * 
     * @param data
     *            The descriptor data as a byte buffer.
     */
    public SimpleUsbStringDescriptor(final ByteBuffer data)
    {
        super(data.get(0), data.get(1));

        data.position(2);
        this.bString = new byte[bLength() - 2];
        data.get(this.bString);
    }

    /**
     * Constructs a new string descriptor with the specified data.
     * 
     * @param bLength
     *            The descriptor length.
     * @param bDescriptorType
     *            The descriptor type.
     * @param string
     *            The string.
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    public SimpleUsbStringDescriptor(final byte bLength,
        final byte bDescriptorType, final String string)
        throws UnsupportedEncodingException
    {
        super(bLength, bDescriptorType);
        this.bString = string.getBytes("UTF-16LE");
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
        if (obj == null || getClass() != obj.getClass()) return false;
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
