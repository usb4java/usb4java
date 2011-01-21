/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB config descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB_Config_Descriptor
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Config_Descriptor(final long pointer)
    {
        this.pointer = pointer;
    }

    public native byte bLength();

    public native byte bDescriptorType();

    public native short wTotalLength();

    public native byte bNumInterfaces();

    public native byte bConfigurationValue();

    public native byte iConfiguration();

    public native byte bmAttributes();

    public native byte MaxPower();

    public native short extralen();

    public native byte[] extra();

    public native USB_Interface[] interface_();
}
