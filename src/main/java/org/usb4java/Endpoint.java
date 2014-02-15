/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

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
final class Endpoint implements UsbEndpoint
{
    /** The interface this endpoint belongs to. */
    private final Interface iface;

    /** The endpoint descriptor. */
    private final UsbEndpointDescriptor descriptor;

    /** The USB pipe for this endpoint. */
    private final Pipe pipe;
    
    /**
     * Constructor.
     * 
     * @param iface
     *            The interface this endpoint belongs to.
     * @param descriptor
     *            The libusb endpoint descriptor.
     */
    Endpoint(final Interface iface,
        final EndpointDescriptor descriptor)
    {
        this.iface = iface;
        this.descriptor = new SimpleUsbEndpointDescriptor(descriptor);
        this.pipe = new Pipe(this);
    }

    @Override
    public Interface getUsbInterface()
    {
        return this.iface;
    }

    @Override
    public UsbEndpointDescriptor getUsbEndpointDescriptor()
    {
        return this.descriptor;
    }

    @Override
    public byte getDirection()
    {
        final byte address = this.descriptor.bEndpointAddress();
        return (byte) (address & UsbConst.ENDPOINT_DIRECTION_MASK);
    }

    @Override
    public byte getType()
    {
        final byte attribs = this.descriptor.bmAttributes();
        return (byte) (attribs & UsbConst.ENDPOINT_TYPE_MASK);
    }

    @Override
    public UsbPipe getUsbPipe()
    {
        return this.pipe;
    }
}
