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

import java.nio.ByteBuffer;

import javax.usb.UsbEndpointDescriptor;

/**
 * A structure representing the standard USB endpoint descriptor.
 * 
 * This descriptor is documented in section 9.6.6 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class EndpointDescriptor implements UsbEndpointDescriptor
{
    /** The native pointer to the descriptor structure. */
    long pointer;

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
            bLength(), bDescriptorType(), bEndpointAddress(), bmAttributes(),
            wMaxPacketSize(), bInterval(), extraLength(),
            DumpUtils.toHexDump(extra()).replaceAll("(?m)^", "    "));
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        final EndpointDescriptor other = (EndpointDescriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && bEndpointAddress() == other.bEndpointAddress()
            && bmAttributes() == other.bmAttributes()
            && bInterval() == other.bInterval()
            && bSynchAddress() == other.bSynchAddress()
            && wMaxPacketSize() == other.wMaxPacketSize()
            && extraLength() == other.extraLength()
            && extra().equals(other.extra());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bLength();
        result = 37 * result + bDescriptorType();
        result = 37 * result + bEndpointAddress();
        result = 37 * result + bmAttributes();
        result = 37 * result + wMaxPacketSize();
        result = 37 * result + bInterval();
        result = 37 * result + bRefresh();
        result = 37 * result + bSynchAddress();
        result = 37 * result + extra().hashCode();
        result = 37 * result + extraLength();
        return result;
    }
    
    @Override
    public String toString()
    {
        return dump();
    }
}
