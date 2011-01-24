/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;


/**
 * USB interface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Interface
{
    /** The low-level interface structure. */
    private final ByteBuffer iface;


    /**
     * Constructor.
     *
     * @param iface
     *            The low-level interface structure.
     */

    private USB_Interface(final ByteBuffer iface)
    {
        this.iface = iface;
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
        return "USB Interface " + this.iface;
    }
}
