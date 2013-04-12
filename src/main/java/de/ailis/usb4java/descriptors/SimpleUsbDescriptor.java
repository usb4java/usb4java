/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import java.io.Serializable;

import javax.usb.UsbDescriptor;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Base class for all simple USB descriptors.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public abstract class SimpleUsbDescriptor implements UsbDescriptor,
    Serializable
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The descriptor length. */
    private final byte bLength;

    /** The descriptor type. */
    private final byte bDescriptorType;

    /**
     * Constructor.
     * 
     * @param bLength
     *            The descriptor length.
     * @param bDescriptorType
     *            The descriptor type.
     */
    public SimpleUsbDescriptor(final byte bLength, final byte bDescriptorType)
    {
        this.bLength = bLength;
        this.bDescriptorType = bDescriptorType;
    }

    @Override
    public final byte bLength()
    {
        return this.bLength;
    }

    @Override
    public final byte bDescriptorType()
    {
        return this.bDescriptorType;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .appendSuper(super.hashCode())
            .append(this.bDescriptorType)
            .append(this.bLength)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SimpleUsbDescriptor other = (SimpleUsbDescriptor) obj;
        if (this.bDescriptorType != other.bDescriptorType) return false;
        if (this.bLength != other.bLength) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("  bLength %18d%n  bDescriptorType %10d%n",
            this.bLength, this.bDescriptorType);
    }
}
