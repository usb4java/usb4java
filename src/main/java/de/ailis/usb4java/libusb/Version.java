/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusb <http://www.libusb.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2011 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2012 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2012 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2012 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package de.ailis.usb4java.libusb;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Structure providing the version of the libusb runtime.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Version implements Comparable<Version>
{
    /** The native pointer to the version structure. */
    private long versionPointer;

    /**
     * Package-private constructor to prevent manual instantiation. An instance
     * is only returned by the JNI method {@link LibUsb#getVersion()}.
     */
    Version()
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
        return versionPointer;
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
     * Returns the library nano version.
     * 
     * @return The library nano version.
     */
    public native int nano();

    /**
     * Returns the release candidate suffix string, e.g. "-rc4".
     * 
     * @return The release candidate suffix string.
     */
    public native String rc();

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(major())
        .append(minor())
        .append(micro())
        .append(nano())
        .append(rc())
        .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final Version other = (Version) obj;

        return new EqualsBuilder()
            .append(major(), other.major())
            .append(minor(), other.minor())
            .append(micro(), other.micro())
            .append(nano(), other.nano())
            .append(rc(), other.rc())
            .isEquals();
    }

    @Override
    public int compareTo(final Version other)
    {
        if (this == other)
        {
            return 0;
        }
        if (other == null)
        {
            return 1;
        }

        return new CompareToBuilder()
            .append(major(), other.major())
            .append(minor(), other.minor())
            .append(micro(), other.micro())
            .append(nano(), other.nano())
            .append(rc(), other.rc())
            .toComparison();
    }

    @Override
    public String toString()
    {
        return major() + "." + minor() + "." + micro() + "." + nano() + rc();
    }
}
