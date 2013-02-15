/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import javax.usb.UsbInterfaceDescriptor;

import de.ailis.usb4java.jni.USB_Interface_Descriptor;

/**
 * USB interface descriptor wrapping a libusb interface descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaInterfaceDescriptor extends
        Usb4JavaDescriptor<USB_Interface_Descriptor> implements
        UsbInterfaceDescriptor
{
    /**
     * Constructor.
     *
     * @param descriptor
     *            The low-level USB interface descriptor.
     */
    public Usb4JavaInterfaceDescriptor(final USB_Interface_Descriptor descriptor)
    {
        super(descriptor);
    }

    /**
     * @see UsbInterfaceDescriptor#bInterfaceNumber()
     */

    @Override
    public byte bInterfaceNumber()
    {
        return (byte) (this.descriptor.bInterfaceNumber() & 0xff);
    }

    /**
     * @see UsbInterfaceDescriptor#bAlternateSetting()
     */
    @Override
    public byte bAlternateSetting()
    {
        return (byte) (this.descriptor.bAlternateSetting() & 0xff);
    }

    /**
     * @see UsbInterfaceDescriptor#bNumEndpoints()
     */
    @Override
    public byte bNumEndpoints()
    {
        return (byte) (this.descriptor.bNumEndpoints() & 0xff);
    }

    /**
     * @see UsbInterfaceDescriptor#bInterfaceClass()
     */
    @Override
    public byte bInterfaceClass()
    {
        return (byte) (this.descriptor.bInterfaceClass() & 0xff);
    }

    /**
     * @see UsbInterfaceDescriptor#bInterfaceSubClass()
     */
    @Override
    public byte bInterfaceSubClass()
    {
        return (byte) (this.descriptor.bInterfaceSubClass() & 0xff);
    }

    /**
     * @see UsbInterfaceDescriptor#bInterfaceProtocol()
     */
    @Override
    public byte bInterfaceProtocol()
    {
        return (byte) (this.descriptor.bInterfaceProtocol() & 0xff);
    }

    /**
     * @see UsbInterfaceDescriptor#iInterface()
     */
    @Override
    public byte iInterface()
    {
        return (byte) (this.descriptor.iInterface() & 0xff);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (this == o) return true;
        if (o.getClass() != getClass()) return false;
        final Usb4JavaInterfaceDescriptor other = (Usb4JavaInterfaceDescriptor) o;
        return this.descriptor.equals(other.descriptor);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.descriptor.hashCode();
    }
}
