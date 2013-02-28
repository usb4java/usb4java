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


/**
 * A collection of alternate settings for a particular USB interface.
 */
public final class Interface
{
    /** The native pointer to the descriptor structure. */
    long pointer;

    /**
     * Returns the array with interface descriptors. The length of this array is
     * determined by the {@link #numAltSetting()} field.
     * 
     * @return The array with interfaces.
     */
    public native InterfaceDescriptor[] iface();

    /**
     * Returns the number of alternate settings that belong to this interface.
     * 
     * @return The number of alternate settings.
     */
    public native int numAltSetting();
}
