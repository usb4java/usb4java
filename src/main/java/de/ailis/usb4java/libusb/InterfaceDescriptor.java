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

import javax.usb.UsbInterfaceDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ailis.usb4java.utils.DumpUtils;

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
    private long pointer;

    /**
     * Returns the native pointer.
     * 
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.pointer;
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
        String sInterface = LibUSB.getStringDescriptor(handle, iInterface);
        if (sInterface == null) sInterface = "";
        builder.append(String.format("Interface Descriptor:%n"
            + "  bLength             %5d%n"
            + "  bDescriptorType     %5d%n"
            + "  bInterfaceNumber    %5d%n"
            + "  bAlternateSetting   %5d%n"
            + "  bNumEndpoints       %5d%n"
            + "  bInterfaceClass     %5d %s%n"
            + "  bInterfaceSubClass  %5d%n"
            + "  bInterfaceProtocol  %5d%n"
            + "  iInterface          %5d %s%n"
            + "  extralen       %10d%n"
            + "  extra:%n"
            + "%s",
            bLength(), bDescriptorType(), bInterfaceNumber(),
            bAlternateSetting(), bNumEndpoints(), bInterfaceClass(),
            DumpUtils.getUSBClassName(bInterfaceClass()), bInterfaceSubClass(),
            bInterfaceProtocol(), iInterface(), sInterface, extraLength(),
            DumpUtils.toHexDump(extra()).replaceAll("(?m)^", "    ")));
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
