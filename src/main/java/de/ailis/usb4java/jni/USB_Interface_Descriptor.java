/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB interface descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB_Interface_Descriptor
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Interface_Descriptor(final long pointer)
    {
        this.pointer = pointer;
    }

    public native byte bLength();

    public native byte bDescriptorType();

    public native byte bInterfaceNumber();

    public native byte bAlternateSetting();

    public native byte bNumEndpoints();

    public native byte bInterfaceClass();

    public native byte bInterfaceSubClass();

    public native byte bInterfaceProtocol();

    public native byte iInterface();

    public native USB_Endpoint_Descriptor[] endpoint();

    public native byte[] extra();

    public native int extralen();
}
