/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbDescriptor;

import de.ailis.usb4java.jni.USB_Descriptor_Header;

/**
 * Abstract USB descriptor wrapping a libusb descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @param <T>
 *            The descriptor type.
 */
public abstract class Usb4JavaDescriptor<T extends USB_Descriptor_Header>
        implements UsbDescriptor
{
    /** The low level USB descriptor header. */
    protected final T descriptor;

    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB configuration descriptor.
     */
    public Usb4JavaDescriptor(final T descriptor)
    {
        this.descriptor = descriptor;
    }

    /**
     * @see UsbDescriptor#bLength()
     */
    @Override
    public final byte bLength()
    {
        return (byte) (this.descriptor.bLength() & 0xff);
    }

    /**
     * @see UsbDescriptor#bDescriptorType()
     */
    @Override
    public final byte bDescriptorType()
    {
        return (byte) (this.descriptor.bDescriptorType() & 0xff);
    }
}
