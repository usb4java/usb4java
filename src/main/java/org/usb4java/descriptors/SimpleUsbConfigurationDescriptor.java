/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.descriptors;

import javax.usb.UsbConfigurationDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.libusb4java.ConfigDescriptor;

/**
 * Simple USB configuration descriptor.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbConfigurationDescriptor extends SimpleUsbDescriptor
    implements UsbConfigurationDescriptor
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The total length. */
    private final short wTotalLength;

    /** The number of interfaces. */
    private final byte bNumInterfaces;

    /** The configuration value. */
    private final byte bConfigurationValue;

    /** The configuration string descriptor index. */
    private final byte iConfiguration;

    /** The attributes. */
    private final byte bmAttributes;

    /** The maximum power. */
    private final byte bMaxPower;

    /**
     * Constructor.
     * 
     * @param bLength
     *            The descriptor length.
     * @param bDescriptorType
     *            The descriptor type.
     * @param wTotalLength
     *            The total length.
     * @param bNumInterfaces
     *            The number of interfaces.
     * @param bConfigurationValue
     *            The configuration value.
     * @param iConfiguration
     *            The configuration string descriptor index.
     * @param bmAttributes
     *            The attributes.
     * @param bMaxPower
     *            The maximum power.
     */
    public SimpleUsbConfigurationDescriptor(final byte bLength,
        final byte bDescriptorType, final short wTotalLength,
        final byte bNumInterfaces, final byte bConfigurationValue,
        final byte iConfiguration, final byte bmAttributes,
        final byte bMaxPower)
    {
        super(bLength, bDescriptorType);
        this.wTotalLength = wTotalLength;
        this.bNumInterfaces = bNumInterfaces;
        this.bConfigurationValue = bConfigurationValue;
        this.iConfiguration = iConfiguration;
        this.bmAttributes = bmAttributes;
        this.bMaxPower = bMaxPower;
    }

    /**
     * Construct from a libusb4java configuration descriptor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbConfigurationDescriptor(ConfigDescriptor descriptor)
    {
        this(descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.wTotalLength(),
            descriptor.bNumInterfaces(),
            descriptor.bConfigurationValue(),
            descriptor.iConfiguration(),
            descriptor.bmAttributes(),
            descriptor.bMaxPower());
    }

    @Override
    public short wTotalLength()
    {
        return this.wTotalLength;
    }

    @Override
    public byte bNumInterfaces()
    {
        return this.bNumInterfaces;
    }

    @Override
    public byte bConfigurationValue()
    {
        return this.bConfigurationValue;
    }

    @Override
    public byte iConfiguration()
    {
        return this.iConfiguration;
    }

    @Override
    public byte bmAttributes()
    {
        return this.bmAttributes;
    }

    @Override
    public byte bMaxPower()
    {
        return this.bMaxPower;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bDescriptorType())
            .append(bLength())
            .append(this.bConfigurationValue)
            .append(this.bMaxPower)
            .append(this.bNumInterfaces)
            .append(this.bmAttributes)
            .append(this.iConfiguration)
            .append(this.wTotalLength)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SimpleUsbConfigurationDescriptor other =
            (SimpleUsbConfigurationDescriptor) obj;
        return new EqualsBuilder()
            .append(bLength(), other.bLength())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(this.bConfigurationValue, other.bConfigurationValue)
            .append(this.bMaxPower, other.bMaxPower)
            .append(this.bNumInterfaces, other.bNumInterfaces)
            .append(this.bmAttributes, other.bmAttributes)
            .append(this.iConfiguration, other.iConfiguration)
            .append(this.wTotalLength, other.wTotalLength)
            .isEquals();
    }

    @Override
    public String toString()
    {
        return dump(this);
    }

    /**
     * Dumps the specified USB configuration descriptor into a string and
     * returns it.
     *
     * @param descriptor
     *            The USB configuration descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbConfigurationDescriptor descriptor)
    {
        return String.format(
            "Configuration Descriptor:%n" +
            "  bLength %18d%n" +
            "  bDescriptorType %10d%n" +
            "  wTotalLength %13d%n" +
            "  bNumInterfaces %11d%n" +
            "  bConfigurationValue %6d%n" +
            "  iConfiguration %11d%n" +
            "  bmAttributes %13s%n" +
            "    %s%n" +
            "%s" +
            "  bMaxPower %16smA%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.wTotalLength() & 0xffff,
            descriptor.bNumInterfaces() & 0xff,
            descriptor.bConfigurationValue() & 0xff,
            descriptor.iConfiguration() & 0xff,
            String.format("0x%02x", descriptor.bmAttributes() & 0xff),
            ((descriptor.bmAttributes() & 64) == 0) ? ("(Bus Powered)")
                : ("Self Powered"),
            ((descriptor.bmAttributes() & 32) == 0) ? ("")
                : String.format("    Remote Wakeup%n"),
            (descriptor.bMaxPower() & 0xff) * 2);
    }
}
