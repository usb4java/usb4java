/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;


/**
 * All standard descriptors have the two fields  bLength and bDescriptorType
 * in common. So this base class implements them.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public abstract class USB_Descriptor_Header
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Descriptor_Header(final long pointer)
    {
        this.pointer = pointer;
    }


    /**
     * Returns the size of the descriptor in bytes.
     *
     * @return The size of the descriptor in bytes (unsigned byte).
     */

    public native short bLength();


    /**
     * Returns the interface descriptor type.
     *
     * @return The interface descriptor type (unsigned byte).
     */

    public native short bDescriptorType();
}
