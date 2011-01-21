/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;


/**
 * USB interface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB_Interface
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Interface(final long pointer)
    {
        this.pointer = pointer;
    }

    public native USB_Interface_Descriptor[] altsetting();

    public native int num_altsetting();
}
