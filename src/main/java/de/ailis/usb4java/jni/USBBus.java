/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB Bus.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USBBus
{
    /** Pointer to low-level C structure. */
    final long pointer;

    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level C structure pointer.
     */

    USBBus(final long pointer)
    {
        this.pointer = pointer;
    }


    /**
     * Returns the dirname.
     *
     * @return The dirname.
     */

    public native String dirname();


    /**
     * Returns the next USB bus or null if none.
     *
     * @return The next USB bus or null if none.
     */

    public native USBBus next();


    /**
     * Returns the previous USB bus or null if none.
     *
     * @return The previous USB bus or null if none.
     */

    public native USBBus prev();


    /**
     * Returns the location.
     *
     * @return The location.
     */

    public native long location();


    /**
     * Returns the USB devices.
     *
     * @return The USB devices or null if none.
     */

    public native USBDevice devices();


    /**
     * Returns the USB root device.
     *
     * @return The USB root device or null if none.
     */

    public native USBDevice root_dev();


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return dirname();
    }
}
