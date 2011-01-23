/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;


/**
 * USB Device handle.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Dev_Handle
{
    /** The low-level pointer to the C structure. */
    private final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    private USB_Dev_Handle(final long pointer)
    {
        this.pointer = pointer;
    }


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return String.format("USB_Dev_Handle 0x%08x", this.pointer);
    }
}
