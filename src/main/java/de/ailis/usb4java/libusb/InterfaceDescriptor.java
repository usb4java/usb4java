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

/**
 * A structure representing the standard USB interface descriptor.
 * 
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class InterfaceDescriptor implements UsbInterfaceDescriptor
{
    /** The native pointer to the descriptor structure. */
    long pointer;

    /**
     * @see javax.usb.UsbDescriptor#bLength()
     */
    @Override
    public native byte bLength();

    /**
     * @see javax.usb.UsbDescriptor#bDescriptorType()
     */
    @Override
    public native byte bDescriptorType();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceNumber()
     */
    @Override
    public native byte bInterfaceNumber();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bAlternateSetting()
     */
    @Override
    public native byte bAlternateSetting();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bNumEndpoints()
     */
    @Override
    public native byte bNumEndpoints();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceClass()
     */
    @Override
    public native byte bInterfaceClass();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceSubClass()
     */
    @Override
    public native byte bInterfaceSubClass();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#bInterfaceProtocol()
     */
    @Override
    public native byte bInterfaceProtocol();

    /**
     * @see javax.usb.UsbInterfaceDescriptor#iInterface()
     */
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
        for (final EndpointDescriptor edesc : endpoint())
        {
            builder.append(edesc.dump().replaceAll("(?m)^", "  "));
        }
        return builder.toString();
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
        final InterfaceDescriptor other = (InterfaceDescriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && bAlternateSetting() == other.bAlternateSetting()
            && bInterfaceClass() == other.bInterfaceClass()
            && bInterfaceNumber() == other.bInterfaceNumber()
            && bInterfaceProtocol() == other.bInterfaceProtocol()
            && bInterfaceSubClass() == other.bInterfaceSubClass()
            && bNumEndpoints() == other.bNumEndpoints()
            && iInterface() == other.iInterface()
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
        result = 37 * result + bInterfaceNumber();
        result = 37 * result + bAlternateSetting();
        result = 37 * result + bNumEndpoints();
        result = 37 * result + bInterfaceClass();
        result = 37 * result + bInterfaceSubClass();
        result = 37 * result + bInterfaceProtocol();
        result = 37 * result + iInterface();
        result = 37 * result + extra().hashCode();
        result = 37 * result + extraLength();
        return result;
    }
}
