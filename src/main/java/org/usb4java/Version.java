/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.usb4java.jna.NativeVersion;

/**
 * Structure providing the version of the libusb runtime.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Version {
    /** The native version structure. */
    private final NativeVersion version;

    /**
     * Creates a new version container from the specified native version structure.
     * 
     * @param version
     *            The native version structure.
     */
    public Version(final NativeVersion version) {
        this.version = version;
    }

    /**
     * Returns the library major version.
     *
     * @return The library major version.
     */
    public int major() {
        return this.version.major & 0xff;
    }

    /**
     * Returns the library minor version.
     *
     * @return The library minor version.
     */
    public int minor() {
        return this.version.minor & 0xff;
    }

    /**
     * Returns the library micro version.
     *
     * @return The library micro version.
     */
    public int micro() {
        return this.version.micro & 0xffff;
    }

    /**
     * Returns the library nano version.
     *
     * @return The library nano version.
     */
    public int nano() {
        return this.version.nano & 0xffff;
    }

    /**
     * Returns the release candidate suffix string, e.g. "-rc4".
     *
     * @return The release candidate suffix string.
     */
    public String rc() {
        return this.version.rc;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.major()).append(this.minor()).append(this.micro()).append(this.nano())
            .append(this.rc()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final Version other = (Version) obj;

        return new EqualsBuilder().append(this.major(), other.major()).append(this.minor(), other.minor())
            .append(this.micro(), other.micro()).append(this.nano(), other.nano()).append(this.rc(), other.rc())
            .isEquals();
    }

    @Override
    public String toString() {
        return this.major() + "." + this.minor() + "." + this.micro() + "." + this.nano() + this.rc();
    }
}
