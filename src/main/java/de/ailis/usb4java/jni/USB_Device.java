/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB Device.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB_Device
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Device(final long pointer)
    {
        this.pointer = pointer;
    }


    /**
     * Returns the filename.
     *
     * @return The filename.
     */

    public native String filename();


    /**
     * Returns the next USB device or null if none.
     *
     * @return The next USB device or null if none.
     */

    public native USB_Device next();


    /**
     * Returns the child devices.
     *
     * @return The child devices.
     */

    public native USB_Device[] children();


    /**
     * Returns the previous USB device or null if none.
     *
     * @return The previous USB device or null if none.
     */

    public native USB_Device prev();


    /**
     * Returns the device number.
     *
     * @return The device number.
     */

    public native short devnum();


    /**
     * Returns the number of child devices.
     *
     * @return The number of child devices.
     */

    public native short num_children();


    /**
     * Returns the USB bus.
     *
     * @return The USB bus.
     */

    public native USB_Bus bus();


    /**
     * Returns the USB device descriptor.
     *
     * @return The USB device descriptor.
     */

    public native USB_Device_Descriptor descriptor();


    /**
     * Returns the USB config descriptor.
     *
     * @return The USB config descriptor.
     */

    public native USB_Config_Descriptor[] config();


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return filename();
    }
}
