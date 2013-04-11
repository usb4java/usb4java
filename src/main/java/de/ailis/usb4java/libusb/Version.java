/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
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
public final class Version implements Comparable<Version>
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

    @Override
    public String toString()
    {
        return major() + "." + minor() + "." + micro() + rc();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + major();
        result = prime * result + minor();
        result = prime * result + micro();
        result =
            prime * result + ((rc() == null) ? 0 : rc().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        final Version other = (Version) o;
        if (rc() == null && other.rc() != null) return false;
        else if (!rc().equals(other.rc())) return false;
        return major() == other.major()
            && minor() == other.minor()
            && micro() == other.micro()
            && rc() == other.rc();
    }

    @Override
    public int compareTo(Version o)
    {
        if (major() > o.major()) return 1;
        if (major() < o.major()) return -1;
        if (minor() > o.minor()) return 1;
        if (minor() < o.minor()) return -1;
        if (micro() > o.micro()) return 1;
        if (micro() < o.micro()) return -1;
        if (rc() != null && o.rc() == null) return 1;
        if (rc() == null && o.rc() != null) return -1;
        return rc().compareTo(o.rc());
    }
}
