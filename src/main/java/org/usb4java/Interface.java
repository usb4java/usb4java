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
import org.usb4java.jna.NativeInterface;
import org.usb4java.jna.NativeInterfaceDescriptor;

/**
 * A collection of alternate settings for a particular USB interface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Interface {
    /** The native interface structure. */
    private final NativeInterface nativeInterface;

    Interface(final NativeInterface nativeInterface) {
        this.nativeInterface = nativeInterface;
    }

    /**
     * Returns the array with interface descriptors. The length of this array is determined by the
     * {@link #numAltsetting()} field.
     *
     * @return The array with interface descriptors.
     */
    public InterfaceDescriptor[] altsetting() {
        final int numAltsetting = numAltsetting();
        final InterfaceDescriptor[] descriptors = new InterfaceDescriptor[numAltsetting];
        if (numAltsetting > 0) {
            final NativeInterfaceDescriptor[] nativeDescriptors =
                (NativeInterfaceDescriptor[]) this.nativeInterface.altsetting.toArray(numAltsetting);
            for (int i = 0; i != numAltsetting; ++i) {
                descriptors[i] = new InterfaceDescriptor(nativeDescriptors[i]);
            }
        }
        return descriptors;
    }

    /**
     * Returns the number of alternate settings that belong to this interface.
     *
     * @return The number of alternate settings.
     */
    public int numAltsetting() {
        return this.nativeInterface.num_altsetting;
    }

    /**
     * Returns a dump of this interface.
     *
     * @return The interface dump.
     */
    public String dump() {
        final StringBuilder builder = new StringBuilder();

        builder.append(String.format("Interface:%n" + "  numAltsetting %10d", this.numAltsetting()));

        for (final InterfaceDescriptor intDesc: this.altsetting()) {
            builder.append(String.format("%n") + intDesc.dump());
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.altsetting()).append(this.numAltsetting()).toHashCode();
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

        final Interface other = (Interface) obj;

        return new EqualsBuilder().append(this.altsetting(), other.altsetting())
            .append(this.numAltsetting(), other.numAltsetting()).isEquals();
    }

    @Override
    public String toString() {
        return this.dump();
    }
}
