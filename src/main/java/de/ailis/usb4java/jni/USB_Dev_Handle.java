/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import java.nio.ByteBuffer;


/**
 * USB Device handle.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Dev_Handle
{
    /** The low-level USB device handle structure. */
    private final ByteBuffer handle;

    /**
     * Constructor.
     *
     * @param handle
     *            The low-level USB device handle structure.
     */
    private USB_Dev_Handle(final ByteBuffer handle)
    {
        this.handle = handle;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "USB_Dev_Handle " + this.handle;
    }
}
