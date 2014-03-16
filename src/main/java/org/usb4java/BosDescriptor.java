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

/**
 * A structure representing the Binary Device Object Store (BOS) descriptor.
 *
 * This descriptor is documented in section 9.6.2 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class BosDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long bosDescriptorPointer;

    /**
     * Constructs a new BOS descriptor which can be passed to the
     * {@link LibUsb#getBosDescriptor(DeviceHandle, BosDescriptor)} method.
     */
    public BosDescriptor()
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
        return this.bosDescriptorPointer;
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
     * Returns the length of this descriptor and all of its sub descriptors.
     *
     * @return The total descriptor length.
     */
    public native short wTotalLength();

    /**
     * Returns the number of separate device capability descriptors in the BOS.
     *
     * @return The number of device capability descriptors.
     */
    public native byte bNumDeviceCaps();

    /**
     * Returns the array with the device capability descriptors.
     *
     * @return The array with device capability descriptors.
     */
    public native BosDevCapabilityDescriptor[] devCapability();

    /**
     * Returns a dump of this descriptor.
     *
     * @return The descriptor dump.
     */
    public String dump()
    {
        final StringBuilder builder = new StringBuilder();

        builder.append(String.format(
            "BOS Descriptor:%n" +
            "  bLength %18d%n" +
            "  bDescriptorType %10d%n" +
            "  wTotalLength %13s%n" +
            "  bNumDeviceCaps %11s%n",
            this.bLength() & 0xFF,
            this.bDescriptorType() & 0xFF,
            this.wTotalLength() & 0xFFFF,
            this.bNumDeviceCaps() & 0xFF));

        for (final BosDevCapabilityDescriptor descriptor : this.devCapability())
        {
            builder.append(descriptor.dump().replaceAll("(?m)^", "  "));
        }

        return builder.toString();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(this.bLength())
            .append(this.bDescriptorType())
            .append(this.wTotalLength())
            .append(this.bNumDeviceCaps())
            .append(this.devCapability())
            .toHashCode();
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

        final BosDescriptor other = (BosDescriptor) obj;

        return new EqualsBuilder()
            .append(this.bLength(), other.bLength())
            .append(this.bDescriptorType(), other.bDescriptorType())
            .append(this.wTotalLength(), other.wTotalLength())
            .append(this.bNumDeviceCaps(), other.bNumDeviceCaps())
            .append(this.devCapability(), other.devCapability())
            .isEquals();
    }

    @Override
    public String toString()
    {
        return this.dump();
    }
}
