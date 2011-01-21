/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB Device handle.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USBDevHandle
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USBDevHandle(final long pointer)
    {
        this.pointer = pointer;
    }
}
