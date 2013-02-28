/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */

package de.ailis.usb4java.libusb;

import java.nio.ByteBuffer;

import javax.usb.UsbEndpointDescriptor;

/**
 * A structure representing the standard USB endpoint descriptor.
 * 
 * This descriptor is documented in section 9.6.6 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class EndpointDescriptor implements UsbEndpointDescriptor
{
    /** The native pointer to the descriptor structure. */
    long pointer;

    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */
    @Override
    public native byte bLength();

    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */
    @Override
    public native byte bDescriptorType();

    /**
     * @see javax.usb.UsbEndpointDescriptor#bEndpointAddress()
     */
    @Override
    public native byte bEndpointAddress();

    /**
     * @see javax.usb.UsbEndpointDescriptor#bmAttributes()
     */
    @Override
    public native byte bmAttributes();

    /**
     * @see javax.usb.UsbEndpointDescriptor#wMaxPacketSize()
     */
    @Override
    public native short wMaxPacketSize();

    /**
     * @see javax.usb.UsbEndpointDescriptor#bInterval()
     */
    @Override
    public native byte bInterval();

    /**
     * For audio devices only: the rate at which synchronization feedback is
     * provided.
     * 
     * @return The synchronization feedback rate.
     */
    public native byte bRefresh();

    /**
     * For audio devices only: the address of the synch endpoint.
     * 
     * @return The synch endpoint address.
     */
    public native byte bSynchAddress();

    /**
     * Extra descriptors.
     * 
     * If libusbx encounters unknown endpoint descriptors, it will store them
     * here, should you wish to parse them.
     * 
     * @return The extra descriptors.
     */
    public native ByteBuffer extra();

    /**
     * Length of the extra descriptors, in bytes.
     * 
     * @return The extra descriptors length.
     */
    public native int extra_length();

}
