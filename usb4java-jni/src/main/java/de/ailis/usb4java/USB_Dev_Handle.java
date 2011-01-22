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

public class USB_Dev_Handle
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Dev_Handle(final long pointer)
    {
        this.pointer = pointer;
    }
}
