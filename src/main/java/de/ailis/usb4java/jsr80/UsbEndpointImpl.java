/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;


/**
 * usb4java implementation of UsbEndpoint.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbEndpointImpl implements UsbEndpoint
{
    /** The USB interface this endpoint belongs to. */
    private final UsbInterfaceImpl iface;

    /** The USB endpoint descriptor. */
    private final UsbEndpointDescriptor descriptor;

    /** The USB pipe for this endpoint. */
    private final UsbPipe pipe;


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

    public UsbEndpointImpl(final UsbInterfaceImpl iface,
        final UsbEndpointDescriptor descriptor, final AbstractDevice device)
    {
        this.iface = iface;
        this.descriptor = descriptor;
        this.pipe = new UsbPipeImpl(this, device);
    }


    /**
     * @see UsbEndpoint#getUsbInterface()
     */

    @Override
    public UsbInterface getUsbInterface()
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
        return (byte) (this.descriptor.bEndpointAddress() & UsbConst.ENDPOINT_DIRECTION_MASK);
    }


    /**
     * @see UsbEndpoint#getType()
     */

    @Override
    public byte getType()
    {
        return (byte) (this.descriptor.bmAttributes() & UsbConst.ENDPOINT_TYPE_MASK);
    }


    /**
     * @see UsbEndpoint#getUsbPipe()
     */

    @Override
    public UsbPipe getUsbPipe()
    {
        return this.pipe;
    }
}
