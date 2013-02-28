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

import javax.usb.UsbConfigurationDescriptor;

/**
 * A structure representing the standard USB configuration descriptor.
 * 
 * This descriptor is documented in section 9.6.3 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class ConfigDescriptor implements UsbConfigurationDescriptor
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
     * @see javax.usb.UsbConfigurationDescriptor#wTotalLength()
     */
    @Override
    public native short wTotalLength();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bNumInterfaces()
     */
    @Override
    public native byte bNumInterfaces();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bConfigurationValue()
     */
    @Override
    public native byte bConfigurationValue();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#iConfiguration()
     */
    @Override
    public native byte iConfiguration();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bmAttributes()
     */
    @Override
    public native byte bmAttributes();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bMaxPower()
     */
    @Override
    public native byte bMaxPower();
 
    /**
     * Returns the array with interfaces supported by this configuration.
     *
     * @return The array with interfaces.
     */
    public native InterfaceDescriptor[] iface();
    
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
