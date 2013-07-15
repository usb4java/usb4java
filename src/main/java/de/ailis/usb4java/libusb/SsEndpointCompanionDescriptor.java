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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A structure representing the superspeed endpoint companion descriptor.
 *
 * This descriptor is documented in section 9.6.7 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SsEndpointCompanionDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long ssEndpointCompanionDescriptorPointer;

    /**
     * Constructs a new descriptor which can be passed to the
     * {@link LibUsb#getSsEndpointCompanionDescriptor(Context, EndpointDescriptor, SsEndpointCompanionDescriptor)}
     * method.
     */
    public SsEndpointCompanionDescriptor()
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
        return this.ssEndpointCompanionDescriptorPointer;
    }

    /**
     * Returns the size of this descriptor (in bytes).
     *
     * @return The descriptor size in bytes;
     */
    public native byte bLength();

    /**
     * Returns the descriptor type.
     *
     * @return The descriptor type.
     */
    public native byte bDescriptorType();

    /**
     * Returns the maximum number of packets the endpoint can send or receive as
     * part of a burst.
     *
     * @return The maximum number of packets as part of a burst.
     */
    public native byte bMaxBurst();

    /**
     * Returns the attributes. In bulk endpoint: bits 4:0 represents the
     * maximum number of streams the EP supports. In isochronous endpoint:
     * bits 1:0 represents the Mult - a zero based value that determines the
     * maximum number of packets within a service interval.
     *
     * @return The attributes.
     */
    public native byte bmAttributes();

    /**
     * Returns the total number of bytes this endpoint will transfer every
     * service interval. Valid only for periodic endpoints.
     *
     * @return The total number of bytes per service interval.
     */
    public native short wBytesPerInterval();

    /**
     * Returns a dump of this descriptor.
     *
     * @return The descriptor dump.
     */
    public String dump()
    {
        return String.format("Device Descriptor:%n" + "  bLength %18d%n"
            + "  bDescriptorType %10d%n" + "  bMaxBurst %16s%n"
            + "  bmAttributes %13d%n" + "  wBytesPerInterval %8d%n",
            this.bLength() & 0xff, this.bDescriptorType() & 0xff,
            this.bMaxBurst() & 0xff, this.bmAttributes() & 0xff,
            this.wBytesPerInterval() & 0xffff);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        final SsEndpointCompanionDescriptor other = (SsEndpointCompanionDescriptor) obj;
        return new EqualsBuilder()
            .append(this.bDescriptorType(), other.bDescriptorType())
            .append(this.bLength(), other.bLength())
            .append(this.bMaxBurst(), other.bMaxBurst())
            .append(this.bmAttributes(), other.bmAttributes())
            .append(this.wBytesPerInterval(), other.wBytesPerInterval())
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.bLength())
            .append(this.bDescriptorType()).append(this.bMaxBurst())
            .append(this.bmAttributes()).append(this.wBytesPerInterval())
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return this.dump();
    }
}
