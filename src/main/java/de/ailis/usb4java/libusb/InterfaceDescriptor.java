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

import javax.usb.UsbInterfaceDescriptor;

/**
 * A structure representing the standard USB interface descriptor.
 * 
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class InterfaceDescriptor implements UsbInterfaceDescriptor
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
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceNumber()
     */
    @Override
    public native byte bInterfaceNumber();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bAlternateSetting()
     */
    @Override
    public native byte bAlternateSetting();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bNumEndpoints()
     */
    @Override
    public native byte bNumEndpoints();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceClass()
     */
    @Override
    public native byte bInterfaceClass();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceSubClass()
     */
    @Override
    public native byte bInterfaceSubClass();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceProtocol()
     */
    @Override
    public native byte bInterfaceProtocol();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#iInterface()
     */
    @Override
    public native byte iInterface();

    /**
     * Returns the array with endpoints.
     *
     * @return The array with endpoints.
     */
    public native EndpointDescriptor[] endpoint();
    
    /**
     * Extra descriptors.
     * 
     * If libusbx encounters unknown interface descriptors, it will store them
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
