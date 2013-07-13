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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ailis.usb4java.utils.DescriptorUtils;

/**
 * A generic representation of a BOS Device Capability descriptor.
 * 
 * It is advised to check bDevCapabilityType and call the matching
 * get*Descriptor method to get a structure fully matching the type.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class BOSDevCapabilityDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long bosDevCapabilityDescriptorPointer;

    /**
     * Package-private constructor to prevent manual instantiation. BOS device
     * capability descriptors are always created by JNI.
     */
    BOSDevCapabilityDescriptor()
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
        return this.bosDevCapabilityDescriptorPointer;
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
     * Returns the device capability data (bLength - 3 bytes).
     * 
     * @return The device capability data.
     */
    public native ByteBuffer devCapabilityData();

    /**
     * Returns a dump of this descriptor.
     * 
     * @return The descriptor dump.
     */
    public String dump()
    {
        return String.format("BOS Device Capability Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bDevCapabilityType %7s%n"
            + "  devCapabilityData:%n%s%n",
            bLength() & 0xff,
            bDescriptorType() & 0xff,
            bDevCapabilityType() & 0xff,
            DescriptorUtils.dump(devCapabilityData())
                .replaceAll("(?m)^", "    "));
        
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final BOSDevCapabilityDescriptor other =
            (BOSDevCapabilityDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(bDevCapabilityType(), other.bDevCapabilityType())
            .append(devCapabilityData().array(),
                other.devCapabilityData().array()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(bDevCapabilityType())
            .append(devCapabilityData())
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
