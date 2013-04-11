/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbEndpointDescriptor;

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
     * Copy constructor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbEndpointDescriptor(final UsbEndpointDescriptor descriptor)
    {
        this(descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.bEndpointAddress(),
            descriptor.bmAttributes(),
            descriptor.wMaxPacketSize(),
            descriptor.bInterval());
    }

    /**
     * @see javax.usb.UsbEndpointDescriptor#bEndpointAddress()
     */
    @Override
    public byte bEndpointAddress()
    {
        return this.bEndpointAddress;
    }

    /**
     * @see javax.usb.UsbEndpointDescriptor#bmAttributes()
     */
    @Override
    public byte bmAttributes()
    {
        return this.bmAttributes;
    }

    /**
     * @see javax.usb.UsbEndpointDescriptor#wMaxPacketSize()
     */
    @Override
    public short wMaxPacketSize()
    {
        return this.wMaxPacketSize;
    }

    /**
     * @see javax.usb.UsbEndpointDescriptor#bInterval()
     */
    @Override
    public byte bInterval()
    {
        return this.bInterval;
    }
}
