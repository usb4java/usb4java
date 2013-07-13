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
 * A structure representing the Binary Device Object Store (BOS) descriptor.
 * 
 * This descriptor is documented in section 9.6.2 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class BOSDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long bosDescriptorPointer;

    /**
     * Constructs a new BOS descriptor which can be passed to the
     * {@link LibUsb#getBOSDescriptor(DeviceHandle, BOSDescriptor)}
     * method.
     */
    public BOSDescriptor()
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
    public native BOSDevCapabilityDescriptor[] devCapability();

    /**
     * Returns a dump of this descriptor.
     * 
     * @return The descriptor dump.
     */
    public String dump()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("BOS Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  wTotalLength %13s%n"
            + "  bNumDeviceCaps %11s%n",
            bLength() & 0xff,
            bDescriptorType() & 0xff,
            wTotalLength() & 0xffff,
            bNumDeviceCaps() & 0xff));
        for (final BOSDevCapabilityDescriptor descriptor: devCapability())
        {
            builder.append(descriptor.dump().replaceAll("(?m)^", "  "));
        }
        return builder.toString();            
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final BOSDescriptor other =
            (BOSDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(wTotalLength(), other.wTotalLength())
            .append(bNumDeviceCaps(), other.bNumDeviceCaps())
            .append(devCapability(), other.devCapability()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(wTotalLength())
            .append(bNumDeviceCaps())
            .append(devCapability())
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
