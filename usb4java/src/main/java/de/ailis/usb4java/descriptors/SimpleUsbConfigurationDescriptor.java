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
     * @see UsbConfigurationDescriptor#wTotalLength()
     */

    @Override
    public short wTotalLength()
    {
        return this.wTotalLength;
    }


    /**
     * @see UsbConfigurationDescriptor#bNumInterfaces()
     */

    @Override
    public byte bNumInterfaces()
    {
        return this.bNumInterfaces;
    }


    /**
     * @see UsbConfigurationDescriptor#bConfigurationValue()
     */

    @Override
    public byte bConfigurationValue()
    {
        return this.bConfigurationValue;
    }


    /**
     * @see UsbConfigurationDescriptor#iConfiguration()
     */

    @Override
    public byte iConfiguration()
    {
        return this.iConfiguration;
    }


    /**
     * @see UsbConfigurationDescriptor#bmAttributes()
     */

    @Override
    public byte bmAttributes()
    {
        return this.bmAttributes;
    }


    /**
     * @see UsbConfigurationDescriptor#bMaxPower()
     */

    @Override
    public byte bMaxPower()
    {
        return this.bMaxPower;
    }
}
