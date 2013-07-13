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
        return this.interfaceDescriptorPointer;
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
     * If libusb encounters unknown interface descriptors, it will store them
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
        return dump(null);
    }

    /**
     * Returns a dump of this descriptor.
     * 
     * @param handle
     *            The USB device handle for resolving string descriptors. If
     *            null then no strings are resolved.
     * @return The descriptor dump.
     */
    public String dump(final DeviceHandle handle)
    {
        final StringBuilder builder = new StringBuilder();
        final int iInterface = iInterface();
        String sInterface = LibUsb.getStringDescriptor(handle, iInterface);
        if (sInterface == null) sInterface = "";
        builder.append(String.format("%s"
            + "  extralen %17d%n"
            + "  extra:%n"
            + "%s%n",
            DescriptorUtils.dump(this),
            extraLength(),
            DescriptorUtils.dump(extra()).replaceAll("(?m)^", "    ")));
        if (extraLength() != 0) return builder.toString();
        for (final EndpointDescriptor edesc: endpoint())
        {
            builder.append(edesc.dump().replaceAll("(?m)^", "  "));
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final InterfaceDescriptor other = (InterfaceDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(bAlternateSetting(), other.bAlternateSetting())
            .append(bInterfaceClass(), other.bInterfaceClass())
            .append(bInterfaceNumber(), other.bInterfaceNumber())
            .append(bInterfaceProtocol(), other.bInterfaceProtocol())
            .append(bInterfaceSubClass(), other.bInterfaceSubClass())
            .append(bNumEndpoints(), other.bNumEndpoints())
            .append(iInterface(), other.iInterface())
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
            .append(bInterfaceNumber())
            .append(bAlternateSetting())
            .append(bNumEndpoints())
            .append(bInterfaceClass())
            .append(bInterfaceSubClass())
            .append(bInterfaceProtocol())
            .append(iInterface())
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
