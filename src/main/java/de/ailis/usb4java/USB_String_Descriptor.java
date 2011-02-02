/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.Arrays;


/**
 * A string descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_String_Descriptor extends USB_Descriptor_Header
{
    /**
     * Constructor.
     *
     * @param data
     *            The descriptor data.
     */

    public USB_String_Descriptor(final ByteBuffer data)
    {
        super(data);
    }


    /**
     * Returns the string data in UTF-16 encoding.
     *
     * @return The string data.
     */

    public char[] wData()
    {
        this.data.position(2);
        final CharBuffer chars = this.data.order(ByteOrder.LITTLE_ENDIAN)
                .asCharBuffer();
        final char[] output = new char[(bLength() - 2) / 2];
        chars.get(output);
        return output;
    }


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return new String(wData());
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
        final USB_String_Descriptor other = (USB_String_Descriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && Arrays.equals(wData(), other.wData());
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
        result = 37 * result + Arrays.hashCode(wData());
        return result;
    }
}
