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

import java.nio.ByteBuffer;

import javax.usb.UsbEndpointDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ailis.usb4java.utils.DescriptorUtils;

/**
 * A structure representing the standard USB endpoint descriptor.
 *
 * This descriptor is documented in section 9.6.6 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class EndpointDescriptor implements UsbEndpointDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long endpointDescriptorPointer;

    /**
     * Package-private constructor to prevent manual instantiation. Endpoint
     * descriptors are always created by JNI.
     */
    EndpointDescriptor()
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
        return this.endpointDescriptorPointer;
    }

    @Override
    public native byte bLength();

    @Override
    public native byte bDescriptorType();

    @Override
    public native byte bEndpointAddress();

    @Override
    public native byte bmAttributes();

    @Override
    public native short wMaxPacketSize();

    @Override
    public native byte bInterval();

    /**
     * For audio devices only: the rate at which synchronization feedback is
     * provided.
     *
     * @return The synchronization feedback rate.
     */
    public native byte bRefresh();

    /**
     * For audio devices only: the address of the synch endpoint.
     *
     * @return The synch endpoint address.
     */
    public native byte bSynchAddress();

    /**
     * Extra descriptors.
     *
     * If libusb encounters unknown endpoint descriptors, it will store them
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
        return String.format(
            "%s%n" + "  extralen %17d%n" + "  extra:%n" + "%s", DescriptorUtils
                .dump(this), this.extraLength(),
            DescriptorUtils.dump(this.extra()).replaceAll("(?m)^", "    "));
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.bLength())
            .append(this.bDescriptorType()).append(this.bEndpointAddress())
            .append(this.bmAttributes()).append(this.wMaxPacketSize())
            .append(this.bInterval()).append(this.bRefresh())
            .append(this.bSynchAddress()).append(this.extra())
            .append(this.extraLength()).toHashCode();
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

        final EndpointDescriptor other = (EndpointDescriptor) obj;

        return new EqualsBuilder().append(this.bLength(), other.bLength())
            .append(this.bDescriptorType(), other.bDescriptorType())
            .append(this.bEndpointAddress(), other.bEndpointAddress())
            .append(this.bmAttributes(), other.bmAttributes())
            .append(this.wMaxPacketSize(), other.wMaxPacketSize())
            .append(this.bInterval(), other.bInterval())
            .append(this.bRefresh(), other.bRefresh())
            .append(this.bSynchAddress(), other.bSynchAddress())
            .append(this.extra(), other.extra())
            .append(this.extraLength(), other.extraLength()).isEquals();
    }

    @Override
    public String toString()
    {
        return this.dump();
    }
}
