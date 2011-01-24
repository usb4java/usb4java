/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;


/**
 * The USB Bus.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Bus
{
    /** Low-level bus structure. */
    private final ByteBuffer bus;


    /**
     * Constructor.
     *
     * @param bus
     *            The low-level bus structure.
     */

    private USB_Bus(final ByteBuffer bus)
    {
        this.bus = bus;
    }


    /**
     * Returns the directory name of the USB bus.
     *
     * @return The directory name. Never null.
     */

    public native String dirname();


    /**
     * Returns the next USB bus or null if none.
     *
     * @return The next USB bus or null if none.
     */

    public native USB_Bus next();


    /**
     * Returns the previous USB bus or null if none.
     *
     * @return The previous USB bus or null if none.
     */

    public native USB_Bus prev();


    /**
     * Returns the location. The original datatype for this information is
     * an unsigned 32 bit integer. To avoid problems with values larger than
     * 0x7fffffff the wrapper returns a long integer instead.
     *
     * @return The location (32 bit unsigned integer).
     */

    public native long location();


    /**
     * Returns the USB devices. Actually this returns the first USB device and
     * you can use the {@link USB_Device#next()} and {@link USB_Device#prev()}
     * methods to navigate to the other devices. When no USB device was found
     * then null is returned.
     *
     * @return The first USB device or null if none.
     */

    public native USB_Device devices();


    /**
     * Returns the USB root device.
     *
     * @return The USB root device or null if none.
     */

    public native USB_Device root_dev();


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return dirname();
    }
}
