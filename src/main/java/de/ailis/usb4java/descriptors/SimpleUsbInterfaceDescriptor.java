/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbInterfaceDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Simple USB interface descriptor.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbInterfaceDescriptor extends SimpleUsbDescriptor
    implements UsbInterfaceDescriptor
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The interface number. */
    private final byte bInterfaceNumber;

    /** The alternate setting number. */
    private final byte bAlternateSetting;

    /** The number of endpoints. */
    private final byte bNumEndpoints;

    /** The interface class. */
    private final byte bInterfaceClass;

    /** The interface sub class. */
    private final byte bInterfaceSubClass;

    /** The interface protocol. */
    private final byte bInterfaceProtocol;

    /** The interface string descriptor index. */
    private final byte iInterface;

    /**
     * Constructor.
     * 
     * @param bLength
     *            The descriptor length.
     * @param bDescriptorType
     *            The descriptor type.
     * @param bInterfaceNumber
     *            The interface number.
     * @param bAlternateSetting
     *            The alternate setting number.
     * @param bNumEndpoints
     *            The number of endpoints.
     * @param bInterfaceClass
     *            The interface class.
     * @param bInterfaceSubClass
     *            The interface sub class.
     * @param bInterfaceProtocol
     *            The interface protocol.
     * @param iInterface
     *            The interface string descriptor index.
     */
    public SimpleUsbInterfaceDescriptor(final byte bLength,
        final byte bDescriptorType, final byte bInterfaceNumber,
        final byte bAlternateSetting, final byte bNumEndpoints,
        final byte bInterfaceClass, final byte bInterfaceSubClass,
        final byte bInterfaceProtocol, final byte iInterface)
    {
        super(bLength, bDescriptorType);
        this.bInterfaceNumber = bInterfaceNumber;
        this.bAlternateSetting = bAlternateSetting;
        this.bNumEndpoints = bNumEndpoints;
        this.bInterfaceClass = bInterfaceClass;
        this.bInterfaceSubClass = bInterfaceSubClass;
        this.bInterfaceProtocol = bInterfaceProtocol;
        this.iInterface = iInterface;
    }

    /**
     * Copy constructor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbInterfaceDescriptor(final UsbInterfaceDescriptor descriptor)
    {
        this(descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.bInterfaceNumber(),
            descriptor.bAlternateSetting(),
            descriptor.bNumEndpoints(),
            descriptor.bInterfaceClass(),
            descriptor.bInterfaceSubClass(),
            descriptor.bInterfaceProtocol(),
            descriptor.iInterface());
    }

    @Override
    public byte bInterfaceNumber()
    {
        return this.bInterfaceNumber;
    }

    @Override
    public byte bAlternateSetting()
    {
        return this.bAlternateSetting;
    }

    @Override
    public byte bNumEndpoints()
    {
        return this.bNumEndpoints;
    }

    @Override
    public byte bInterfaceClass()
    {
        return this.bInterfaceClass;
    }

    @Override
    public byte bInterfaceSubClass()
    {
        return this.bInterfaceSubClass;
    }

    @Override
    public byte bInterfaceProtocol()
    {
        return this.bInterfaceProtocol;
    }

    @Override
    public byte iInterface()
    {
        return this.iInterface;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bDescriptorType())
            .append(bLength())
            .append(this.bAlternateSetting)
            .append(this.bInterfaceClass)
            .append(this.bInterfaceNumber)
            .append(this.bInterfaceProtocol)
            .append(this.bInterfaceSubClass)
            .append(this.bNumEndpoints)
            .append(this.iInterface)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SimpleUsbInterfaceDescriptor other =
            (SimpleUsbInterfaceDescriptor) obj;
        return new EqualsBuilder()
            .append(bLength(), other.bLength())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(this.bAlternateSetting, other.bAlternateSetting)
            .append(this.bInterfaceClass, other.bInterfaceClass)
            .append(this.bInterfaceNumber, other.bInterfaceNumber)
            .append(this.bInterfaceProtocol, other.bInterfaceProtocol)
            .append(this.bInterfaceSubClass, other.bInterfaceSubClass)
            .append(this.bNumEndpoints, other.bNumEndpoints)
            .append(this.iInterface, other.iInterface)
            .isEquals();
    }

    @Override
    public String toString()
    {
        return String.format("Interface Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bInterfaceNumber %9d%n"
            + "  bAlternateSetting %8d%n"
            + "  bNumEndpoints %12d%n"
            + "  bInterfaceClass %10d%n"
            + "  bInterfaceSubClass %7d%n"
            + "  bInterfaceProtocol %7d%n"
            + "  iInterface %15d%n",
            bLength(),
            bDescriptorType(),
            this.bInterfaceNumber & 0xff,
            this.bAlternateSetting & 0xff,
            this.bNumEndpoints & 0xff,
            this.bInterfaceClass & 0xff,
            this.bInterfaceSubClass & 0xff,
            this.bInterfaceProtocol & 0xff,
            this.iInterface & 0xff);
    }
}
