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
 * A structure representing the USB 2.0 Extension descriptor. This descriptor is
 * documented in section 9.6.2.1 of the USB 3.0 specification.
 * 
 * All multiple-byte fields are represented in host-endian format.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb20ExtensionDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long usb20ExtensionDescriptorPointer;

    /**
     * Constructs a new BOS descriptor which can be passed to the
     * {@link LibUsb#getUsb20ExtensionDescriptor(Context, 
     * BOSDevCapabilityDescriptor, Usb20ExtensionDescriptor)} method.
     */
    public Usb20ExtensionDescriptor()
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
        return this.usb20ExtensionDescriptorPointer;
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
     * Returns the device capability type.
     * 
     * @return The device capability type.
     */
    public native byte bDevCapabilityType();

    /**
     * Returns the bitmap of supported device level features. 
     * 
     * @return The supported device level features.
     */
    public native int bmAttributes();

    /**
     * Returns a dump of this descriptor.
     * 
     * @return The descriptor dump.
     */
    public String dump()
    {
        return String.format("USB 2.0 Extension Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bDevCapabilityType %7d%n"
            + "  bmAttributes %13s%n",
            bLength() & 0xff,
            bDescriptorType() & 0xff,
            bDevCapabilityType() & 0xff,
            String.format("0x%08x", bmAttributes() & 0xffffffff));
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final Usb20ExtensionDescriptor other =
            (Usb20ExtensionDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(bDevCapabilityType(), other.bDevCapabilityType())
            .append(bmAttributes(), other.bmAttributes()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(bDevCapabilityType())
            .append(bmAttributes())
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
