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
 * Structure providing the version of the libusbx runtime.
 */
public final class Version
{
    /** The native pointer to the version structure. */
    long pointer;

    /**
     * Returns the library major version.
     * 
     * @return The library major version.
     */
    public native int major();
    
    /**
     * Returns the library minor version.
     * 
     * @return The library minor version.
     */
    public native int minor();
    
    /**
     * Returns the library micro version.
     * 
     * @return The library micro version.
     */
    public native int micro();
    
    /**
     * Returns the release candidate suffix string, e.g. "-rc4".
     * 
     * @return The release candidate suffix string.
     */
    public native String rc();   

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return major() + "." + minor() + "." + micro() + rc();
    }
}
