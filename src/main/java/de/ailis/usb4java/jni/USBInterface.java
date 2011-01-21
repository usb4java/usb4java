/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB interface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USBInterface
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USBInterface(final long pointer)
    {
        this.pointer = pointer;
    }

    public native USBInterfaceDescriptor[] altsetting();

    public native int num_altsetting();
}
