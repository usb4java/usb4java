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
public final class SSEndpointCompanionDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long ssEndpointCompanionDescriptor;

    /**
     * Constructs a new descriptor which can be passed to the
     * {@link LibUsb#getSSEndpointCompanionDescriptor(Context, EndpointDescriptor, SSEndpointCompanionDescriptor)}
     * method.
     */
    public SSEndpointCompanionDescriptor()
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
        return this.ssEndpointCompanionDescriptor;
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
     * Returns the attributes. In bulk endpoint: bits 4:0 represents the maximum
     * number of streams the EP supports. In isochronous endpoint: bits 1:0
     * represents the Mult - a zero based value that determines the maximum
     * number of packets within a service interval
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
        return String.format("Device Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bMaxBurst %19s%n"
            + "  bmAttributes %13d %s%n"
            + "  wBytesPerInterval %10d",
            bLength() & 0xff,
            bDescriptorType() & 0xff,
            bMaxBurst() & 0xff,
            bmAttributes() & 0xff,
            wBytesPerInterval() & 0xffff);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final SSEndpointCompanionDescriptor other =
            (SSEndpointCompanionDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(bMaxBurst(), other.bMaxBurst())
            .append(bmAttributes(), other.bmAttributes())
            .append(wBytesPerInterval(), other.wBytesPerInterval()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(bMaxBurst())
            .append(bmAttributes())
            .append(wBytesPerInterval())
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
