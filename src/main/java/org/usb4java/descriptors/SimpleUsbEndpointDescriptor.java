/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.descriptors;

import javax.usb.UsbEndpointDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.libusb4java.EndpointDescriptor;
import org.libusb4java.utils.DescriptorUtils;

/**
 * Simple USB endpoint descriptor.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbEndpointDescriptor extends SimpleUsbDescriptor
    implements UsbEndpointDescriptor
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The poll interval. */
    private final byte bInterval;

    /** The maximum packet size. */
    private final short wMaxPacketSize;

    /** The endpoint attributes. */
    private final byte bmAttributes;

    /** The endpoint address. */
    private final byte bEndpointAddress;

    /**
     * Constructor.
     * 
     * @param bLength
     *            The descriptor length.
     * @param bDescriptorType
     *            The descriptor type.
     * @param bEndpointAddress
     *            The address of the endpoint.
     * @param bmAttributes
     *            The endpoint attributes.
     * @param wMaxPacketSize
     *            The maximum packet size.
     * @param bInterval
     *            The poll interval.
     */
    public SimpleUsbEndpointDescriptor(final byte bLength,
        final byte bDescriptorType, final byte bEndpointAddress,
        final byte bmAttributes, final short wMaxPacketSize,
        final byte bInterval)
    {
        super(bLength, bDescriptorType);
        this.bEndpointAddress = bEndpointAddress;
        this.wMaxPacketSize = wMaxPacketSize;
        this.bmAttributes = bmAttributes;
        this.bInterval = bInterval;
    }

    /**
     * Construct from a libusb4java endpoint descriptor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbEndpointDescriptor(EndpointDescriptor descriptor)
    {
        this(descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.bEndpointAddress(),
            descriptor.bmAttributes(),
            descriptor.wMaxPacketSize(),
            descriptor.bInterval());
    }

    @Override
    public byte bEndpointAddress()
    {
        return this.bEndpointAddress;
    }

    @Override
    public byte bmAttributes()
    {
        return this.bmAttributes;
    }

    @Override
    public short wMaxPacketSize()
    {
        return this.wMaxPacketSize;
    }

    @Override
    public byte bInterval()
    {
        return this.bInterval;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bDescriptorType())
            .append(bLength())
            .append(this.bEndpointAddress)
            .append(this.bInterval)
            .append(this.bmAttributes)
            .append(this.wMaxPacketSize)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SimpleUsbEndpointDescriptor other =
            (SimpleUsbEndpointDescriptor) obj;
        return new EqualsBuilder()
            .append(bLength(), other.bLength())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(this.bEndpointAddress, other.bEndpointAddress)
            .append(this.bInterval, other.bInterval)
            .append(this.bmAttributes, other.bmAttributes)
            .append(this.wMaxPacketSize, other.wMaxPacketSize)
            .isEquals();
    }

    @Override
    public String toString()
    {
        return dump(this);
    }

    /**
     * Dumps the specified USB endpoint descriptor into a string and returns it.
     *
     * @param descriptor
     *            The USB endpoint descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbEndpointDescriptor descriptor)
    {
        return String.format(
            "Endpoint Descriptor:%n" +
            "  bLength %18d%n" +
            "  bDescriptorType %10d%n" +
            "  bEndpointAddress %9s  EP %d %s%n" +
            "  bmAttributes %13d%n" +
            "    Transfer Type             %s%n" +
            "    Synch Type                %s%n" +
            "    Usage Type                %s%n" +
            "  wMaxPacketSize %11d%n" +
            "  bInterval %16d%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            String.format("0x%02x", descriptor.bEndpointAddress() & 0xff),
            descriptor.bEndpointAddress() & 0x0f,
            DescriptorUtils.getDirectionName(descriptor.bEndpointAddress()),
            descriptor.bmAttributes() & 0xff,
            DescriptorUtils.getTransferTypeName(descriptor.bmAttributes()),
            DescriptorUtils.getSynchTypeName(descriptor.bmAttributes()),
            DescriptorUtils.getUsageTypeName(descriptor.bmAttributes()),
            descriptor.wMaxPacketSize() & 0xffff,
            descriptor.bInterval() & 0xff);
    }
}
