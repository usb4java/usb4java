/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import java.io.Serializable;

import javax.usb.UsbDescriptor;

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

    /**
     * @see UsbDescriptor#bLength()
     */
    @Override
    public final byte bLength()
    {
        return this.bLength;
    }

    /**
     * @see UsbDescriptor#bDescriptorType()
     */
    @Override
    public final byte bDescriptorType()
    {
        return this.bDescriptorType;
    }
}
