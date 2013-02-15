/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;

/**
 * usb4java implementation of UsbEndpoint.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaEndpoint implements UsbEndpoint
{
    /** The USB interface this endpoint belongs to. */
    private final Usb4JavaInterface iface;

    /** The USB endpoint descriptor. */
    private final UsbEndpointDescriptor descriptor;

    /** The USB pipe for this endpoint. */
    private final Usb4JavaPipe pipe;

    /**
     * Constructor.
     *
     * @param iface
     *            The USB interface this endpoint belongs to.
     * @param descriptor
     *            The USB endpoint descriptor.
     * @param device
     *            The USB device.
     */
    public Usb4JavaEndpoint(final Usb4JavaInterface iface,
        final UsbEndpointDescriptor descriptor, final Usb4JavaDevice device)
    {
        this.iface = iface;
        this.descriptor = descriptor;
        this.pipe = new Usb4JavaPipe(this, device);
    }

    /**
     * @see UsbEndpoint#getUsbInterface()
     */
    @Override
    public Usb4JavaInterface getUsbInterface()
    {
        return this.iface;
    }

    /**
     * @see UsbEndpoint#getUsbEndpointDescriptor()
     */
    @Override
    public UsbEndpointDescriptor getUsbEndpointDescriptor()
    {
        return this.descriptor;
    }

    /**
     * @see UsbEndpoint#getDirection()
     */
    @Override
    public byte getDirection()
    {
        final byte address = this.descriptor.bEndpointAddress();
        return (byte) (address & UsbConst.ENDPOINT_DIRECTION_MASK);
    }

    /**
     * @see UsbEndpoint#getType()
     */
    @Override
    public byte getType()
    {
        final byte attribs = this.descriptor.bmAttributes();
        return (byte) (attribs & UsbConst.ENDPOINT_TYPE_MASK);
    }

    /**
     * @see UsbEndpoint#getUsbPipe()
     */
    @Override
    public Usb4JavaPipe getUsbPipe()
    {
        return this.pipe;
    }
}
