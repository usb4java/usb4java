/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbInterfaceDescriptor;


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
     * @see UsbInterfaceDescriptor#bInterfaceNumber()
     */

    @Override
    public byte bInterfaceNumber()
    {
        return this.bInterfaceNumber;
    }


    /**
     * @see UsbInterfaceDescriptor#bAlternateSetting()
     */

    @Override
    public byte bAlternateSetting()
    {
        return this.bAlternateSetting;
    }


    /**
     * @see UsbInterfaceDescriptor#bNumEndpoints()
     */

    @Override
    public byte bNumEndpoints()
    {
        return this.bNumEndpoints;
    }


    /**
     * @see UsbInterfaceDescriptor#bInterfaceClass()
     */

    @Override
    public byte bInterfaceClass()
    {
        return this.bInterfaceClass;
    }


    /**
     * @see UsbInterfaceDescriptor#bInterfaceSubClass()
     */

    @Override
    public byte bInterfaceSubClass()
    {
        return this.bInterfaceSubClass;
    }


    /**
     * @see UsbInterfaceDescriptor#bInterfaceProtocol()
     */

    @Override
    public byte bInterfaceProtocol()
    {
        return this.bInterfaceProtocol;
    }


    /**
     * @see UsbInterfaceDescriptor#iInterface()
     */

    @Override
    public byte iInterface()
    {
        return this.iInterface;
    }
}
