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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Structure providing the version of the libusbx runtime.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Version implements Comparable<Version>
{
    /** The native pointer to the version structure. */
    private long pointer;
    
    /**
     * Private constructor to prevent manual instantiation. An instance
     * is only returned by the JNI method {@link LibUSB#getVersion()}.
     */
    private Version()
    {
        // Empty
    }

    /**
     * Returns the native pointer.
     * 
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.pointer;
    }

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

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.pointer).toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final Version other = (Version) obj;
        return new EqualsBuilder()
            .append(major(), other.major())
            .append(minor(), other.minor())
            .append(micro(), other.micro())
            .append(rc(), other.rc())
            .isEquals();
    }

    @Override
    public int compareTo(final Version other)
    {
        return new CompareToBuilder()
            .append(major(), other.major())
            .append(minor(), other.minor())
            .append(micro(), other.micro())
            .append(rc(), other.rc())
            .toComparison();
    }
}
