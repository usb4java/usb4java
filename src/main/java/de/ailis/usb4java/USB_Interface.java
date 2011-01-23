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

public final class USB_Interface
{
    /** The low-level pointer to the C structure. */
    private final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    private USB_Interface(final long pointer)
    {
        this.pointer = pointer;
    }


    /**
     * Returns the array with all available interface descriptors.
     *
     * @return The array with the interface descriptors.
     */

    public native USB_Interface_Descriptor[] altsetting();


    /**
     * Returns the number of available interface descriptors.
     *
     * @return The number of available interface descriptors.
     */

    public native int num_altsetting();




    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return String.format("USB Interface 0x%08x", this.pointer);
    }
}
