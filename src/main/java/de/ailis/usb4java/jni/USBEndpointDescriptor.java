/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * USB endport descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USBEndpointDescriptor
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USBEndpointDescriptor(final long pointer)
    {
        this.pointer = pointer;
    }

    public native byte bLength();

    public native byte bDescriptorType();

    public native byte bEndpointAddress();

    public native byte bmAttributes();

    public native short wMaxPacketSize();

    public native byte bInterval();

    public native byte bRefresh();

    public native byte bSynchAddress();

    public native byte[] extra();

    public native int extralen();
}
