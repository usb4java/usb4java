/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbPipe;

import de.ailis.usb4java.descriptors.SimpleUsbEndpointDescriptor;
import de.ailis.usb4java.libusb.EndpointDescriptor;

/**
 * usb4java implementation of UsbEndpoint.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaEndpoint implements UsbEndpoint
{
    /** The interface this endpoint belongs to. */
    private final Usb4JavaInterface iface;

    /** The endpoint descriptor. */
    private final UsbEndpointDescriptor descriptor;

    /** The USB pipe for this endpoint. */
    private final Usb4JavaPipe pipe;
    
    /**
     * Constructor.
     * 
     * @param iface
     *            The interface this endpoint belongs to.
     * @param descriptor
     *            The libusb endpoint descriptor.
     */
    Usb4JavaEndpoint(final Usb4JavaInterface iface,
        final EndpointDescriptor descriptor)
    {
        this.iface = iface;
        this.descriptor = new SimpleUsbEndpointDescriptor(descriptor);
        this.pipe = new Usb4JavaPipe(this);
    }

    /**
     * @see javax.usb.UsbEndpoint#getUsbInterface()
     */
    @Override
    public Usb4JavaInterface getUsbInterface()
    {
        return this.iface;
    }

    /**
     * @see javax.usb.UsbEndpoint#getUsbEndpointDescriptor()
     */
    @Override
    public UsbEndpointDescriptor getUsbEndpointDescriptor()
    {
        return this.descriptor;
    }

    /**
     * @see javax.usb.UsbEndpoint#getDirection()
     */
    @Override
    public byte getDirection()
    {
        final byte address = this.descriptor.bEndpointAddress();
        return (byte) (address & UsbConst.ENDPOINT_DIRECTION_MASK);
    }

    /**
     * @see javax.usb.UsbEndpoint#getType()
     */
    @Override
    public byte getType()
    {
        final byte attribs = this.descriptor.bmAttributes();
        return (byte) (attribs & UsbConst.ENDPOINT_TYPE_MASK);
    }

    /**
     * @see javax.usb.UsbEndpoint#getUsbPipe()
     */
    @Override
    public UsbPipe getUsbPipe()
    {
        return this.pipe;
    }
}
