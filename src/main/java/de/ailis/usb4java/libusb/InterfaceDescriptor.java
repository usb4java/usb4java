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

import javax.usb.UsbInterfaceDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ailis.usb4java.utils.DescriptorUtils;

/**
 * A structure representing the standard USB interface descriptor.
 * 
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class InterfaceDescriptor implements UsbInterfaceDescriptor
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
        return interfaceDescriptorPointer;
    }

    @Override
    public native byte bLength();

    @Override
    public native byte bDescriptorType();

    @Override
    public native byte bInterfaceNumber();

    @Override
    public native byte bAlternateSetting();

    @Override
    public native byte bNumEndpoints();

    @Override
    public native byte bInterfaceClass();

    @Override
    public native byte bInterfaceSubClass();

    @Override
    public native byte bInterfaceProtocol();

    @Override
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
     * If libusbx encounters unknown interface descriptors, it will store them
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

        builder.append(String.format("%s%n" + "  extralen %17d%n"
            + "  extra:%n" + "%s", DescriptorUtils.dump(this), extraLength(),
            DescriptorUtils.dump(extra()).replaceAll("(?m)^", "    ")));

        for (final EndpointDescriptor epDesc : endpoint())
        {
            builder.append("%n" + epDesc.dump());
        }

        return builder.toString();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(bInterfaceNumber())
            .append(bAlternateSetting())
            .append(bNumEndpoints())
            .append(bInterfaceClass())
            .append(bInterfaceSubClass())
            .append(bInterfaceProtocol())
            .append(iInterface())
            .append(endpoint())
            .append(extra())
            .append(extraLength())
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
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final InterfaceDescriptor other = (InterfaceDescriptor) obj;

        return new EqualsBuilder()
            .append(bLength(), other.bLength())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bInterfaceNumber(), other.bInterfaceNumber())
            .append(bAlternateSetting(), other.bAlternateSetting())
            .append(bNumEndpoints(), other.bNumEndpoints())
            .append(bInterfaceClass(), other.bInterfaceClass())
            .append(bInterfaceSubClass(), other.bInterfaceSubClass())
            .append(bInterfaceProtocol(), other.bInterfaceProtocol())
            .append(iInterface(), other.iInterface())
            .append(endpoint(), other.endpoint())
            .append(extra(), other.extra())
            .append(extraLength(), other.extraLength())
            .isEquals();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
