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

package org.libusb4java;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.libusb4java.utils.DescriptorUtils;

/**
 * A structure representing the standard USB interface descriptor.
 *
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class InterfaceDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long interfaceDescriptorPointer;

    /**
     * Package-private constructor to prevent manual instantiation. Interface
     * descriptors are always created by JNI.
     */
    InterfaceDescriptor()
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
        return this.interfaceDescriptorPointer;
    }

    public native byte bLength();

    public native byte bDescriptorType();

    public native byte bInterfaceNumber();

    public native byte bAlternateSetting();

    public native byte bNumEndpoints();

    public native byte bInterfaceClass();

    public native byte bInterfaceSubClass();

    public native byte bInterfaceProtocol();

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
     * If libusb encounters unknown interface descriptors, it will store them
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
    public native int extraLength();

    /**
     * Returns a dump of this descriptor.
     *
     * @return The descriptor dump.
     */
    public String dump()
    {
        final StringBuilder builder = new StringBuilder();

        builder.append(String.format(
            "%s%n" +
            "  extralen %17d%n" +
            "  extra:%n" +
            "%s",
            DescriptorUtils.dump(this),
            this.extraLength(),
            DescriptorUtils.dump(this.extra()).replaceAll("(?m)^", "    ")));

        for (final EndpointDescriptor epDesc : this.endpoint())
        {
            builder.append("%n" + epDesc.dump());
        }

        return builder.toString();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(this.bLength())
            .append(this.bDescriptorType())
            .append(this.bInterfaceNumber())
            .append(this.bAlternateSetting())
            .append(this.bNumEndpoints())
            .append(this.bInterfaceClass())
            .append(this.bInterfaceSubClass())
            .append(this.bInterfaceProtocol())
            .append(this.iInterface())
            .append(this.endpoint())
            .append(this.extra())
            .append(this.extraLength())
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
        if (this.getClass() != obj.getClass())
        {
            return false;
        }

        final InterfaceDescriptor other = (InterfaceDescriptor) obj;

        return new EqualsBuilder()
            .append(this.bLength(), other.bLength())
            .append(this.bDescriptorType(), other.bDescriptorType())
            .append(this.bInterfaceNumber(), other.bInterfaceNumber())
            .append(this.bAlternateSetting(), other.bAlternateSetting())
            .append(this.bNumEndpoints(), other.bNumEndpoints())
            .append(this.bInterfaceClass(), other.bInterfaceClass())
            .append(this.bInterfaceSubClass(), other.bInterfaceSubClass())
            .append(this.bInterfaceProtocol(), other.bInterfaceProtocol())
            .append(this.iInterface(), other.iInterface())
            .append(this.endpoint(), other.endpoint())
            .append(this.extra(), other.extra())
            .append(this.extraLength(), other.extraLength())
            .isEquals();
    }

    @Override
    public String toString()
    {
        return this.dump();
    }
}
