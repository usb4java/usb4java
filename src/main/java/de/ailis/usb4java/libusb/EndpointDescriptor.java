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
     * If libusbx encounters unknown endpoint descriptors, it will store them
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
        return String.format("Endpoint Descriptor:%n"
            + "  bLength           %5d%n"
            + "  bDecsriptorType   %5d%n"
            + "  bEndpointAddress   0x%02x%n"
            + "  bmAttributes       0x%02x%n"
            + "  wMaxPacketSize    %5d%n"
            + "  bInterval         %5d%n"
            + "  extralen     %10d%n"
            + "  extra:%n"
            + "%s",
            bLength() & 0xff, 
            bDescriptorType() & 0xff, 
            bEndpointAddress() & 0xff, 
            bmAttributes() & 0xff,
            wMaxPacketSize() & 0xffff, 
            bInterval() & 0xff, 
            extraLength(),
            DescriptorUtils.dump(extra()).replaceAll("(?m)^", "    "));
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final EndpointDescriptor other = (EndpointDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(bEndpointAddress(), other.bEndpointAddress())
            .append(bmAttributes(), other.bmAttributes())
            .append(bInterval(), other.bInterval())
            .append(bSynchAddress(), other.bSynchAddress())
            .append(wMaxPacketSize(), other.wMaxPacketSize())
            .append(extraLength(), other.extraLength())
            .append(extra(), other.extra())
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(bEndpointAddress())
            .append(bmAttributes())
            .append(wMaxPacketSize())
            .append(bInterval())
            .append(bRefresh())
            .append(bSynchAddress())
            .append(extra())
            .append(extraLength())
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
