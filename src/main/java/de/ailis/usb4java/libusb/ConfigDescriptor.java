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

import javax.usb.UsbConfigurationDescriptor;

/**
 * A structure representing the standard USB configuration descriptor.
 * 
 * This descriptor is documented in section 9.6.3 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 */
public final class ConfigDescriptor implements UsbConfigurationDescriptor
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
     * @see javax.usb.UsbConfigurationDescriptor#wTotalLength()
     */
    @Override
    public native short wTotalLength();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bNumInterfaces()
     */
    @Override
    public native byte bNumInterfaces();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bConfigurationValue()
     */
    @Override
    public native byte bConfigurationValue();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#iConfiguration()
     */
    @Override
    public native byte iConfiguration();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bmAttributes()
     */
    @Override
    public native byte bmAttributes();

    /**
     * @see javax.usb.UsbConfigurationDescriptor#bMaxPower()
     */
    @Override
    public native byte bMaxPower();
 
    /**
     * Returns the array with interfaces supported by this configuration.
     *
     * @return The array with interfaces.
     */
    public native Interface[] iface();
    
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
        builder
            .append(String.format("Configuration Descriptor:%n"
                + "  bLength               %5d%n"
                + "  bDescriptorType       %5d%n"
                + "  wTotalLength          %5d%n"
                + "  bNumInterfaces        %5d%n"
                + "  bConfigurationValue   %5d%n"
                + "  iConfiguration        %5d%n"
                + "  bmAttributes           %#04x%n"
                + "  MaxPower              %5d mA%n"
                + "  extralen         %10d%n"
                + "  extra:%n"
                + "%s",
                bLength(), bDescriptorType(), wTotalLength(), bNumInterfaces(),
                bConfigurationValue(), iConfiguration(), bmAttributes(),
                bMaxPower() * 2, extraLength(), DumpUtils.toHexDump(extra())
                    .replaceAll("(?m)^", "    ")));
        for (final Interface descriptor : iface())
        {
            builder.append(descriptor.dump(handle)
                    .replaceAll("(?m)^", "  "));
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
        final ConfigDescriptor other = (ConfigDescriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && bConfigurationValue() == other.bConfigurationValue()
            && bmAttributes() == other.bmAttributes()
            && bNumInterfaces() == other.bNumInterfaces()
            && iConfiguration() == other.iConfiguration()
            && bMaxPower() == other.bMaxPower()
            && wTotalLength() == other.wTotalLength();
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
        result = 37 * result + wTotalLength();
        result = 37 * result + bNumInterfaces();
        result = 37 * result + bConfigurationValue();
        result = 37 * result + iConfiguration();
        result = 37 * result + bmAttributes();
        result = 37 * result + bMaxPower();
        result = 37 * result + extra().hashCode();
        result = 37 * result + extraLength();
        return result;
    }
}
