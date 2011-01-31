/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;


/**
 * All standard descriptors have the two fields bLength and bDescriptorType in
 * common. So this base class implements them.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public abstract class USB_Descriptor_Header
{
    /** The descriptor data. */
    protected final ByteBuffer data;


    /**
     * Constructor.
     *
     * @param data
     *            The descriptor data.
     */

    public USB_Descriptor_Header(final ByteBuffer data)
    {
        this.data = data;
    }


    /**
     * Returns the size of the descriptor in bytes.
     *
     * @return The size of the descriptor in bytes (unsigned byte).
     */

    public final native int bLength();


    /**
     * Returns the interface descriptor type.
     *
     * @return The interface descriptor type (unsigned byte).
     */

    public final native int bDescriptorType();


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        final USB_Device_Descriptor other = (USB_Device_Descriptor) o;
        return bDescriptorType() == other.bDescriptorType() &&
            bLength() == other.bLength();
    }


    /**
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bDescriptorType();
        result = 37 * result + bLength();
        return result;
    }
}
