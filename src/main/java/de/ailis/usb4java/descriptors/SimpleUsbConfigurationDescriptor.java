/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbConfigurationDescriptor;

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
     * Copy constructor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbConfigurationDescriptor(
        final UsbConfigurationDescriptor descriptor)
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
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.bConfigurationValue;
        result = prime * result + this.bMaxPower;
        result = prime * result + this.bNumInterfaces;
        result = prime * result + this.bmAttributes;
        result = prime * result + this.iConfiguration;
        result = prime * result + this.wTotalLength;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        SimpleUsbConfigurationDescriptor other =
            (SimpleUsbConfigurationDescriptor) obj;
        if (this.bConfigurationValue != other.bConfigurationValue)
            return false;
        if (this.bMaxPower != other.bMaxPower) return false;
        if (this.bNumInterfaces != other.bNumInterfaces) return false;
        if (this.bmAttributes != other.bmAttributes) return false;
        if (this.iConfiguration != other.iConfiguration) return false;
        if (this.wTotalLength != other.wTotalLength) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("Configuration Descriptor:\n%s"
            + "  wTotalLength %13d\n"
            + "  bNumInterfaces %11d\n"
            + "  bConfigurationValue %6d\n"
            + "  iConfiguration %11d\n"
            + "  bmAttributes %13s\n"
            + "  bMaxPower %16smA\n",
            super.toString(),
            this.wTotalLength & 0xffff,
            this.bNumInterfaces & 0xff,
            this.bConfigurationValue & 0xff,
            this.iConfiguration & 0xff,
            String.format("0x%02x", this.bmAttributes & 0xff),
            (this.bMaxPower & 0xff) * 2);
    }
}
