/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbPort;


/**
 * usb4java implementation of UsbPort.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbPortImpl implements UsbPort
{
    /** The USB hub this port belongs to. */
    private final UsbHub hub;

    /** The port number. */
    private final byte portNumber;

    /** The attached device. */
    private UsbDevice device;


    /**
     * Constructor.
     *
     * @param hub
     *            The USB hub this port belongs to.
     * @param portNumber
     *            The port number.
     */

    public UsbPortImpl(final UsbHub hub, final byte portNumber)
    {
        this.hub = hub;
        this.portNumber = portNumber;
    }


    /**
     * @see UsbPort#getPortNumber()
     */

    @Override
    public byte getPortNumber()
    {
        return this.portNumber;
    }


    /**
     * @see UsbPort#getUsbHub()
     */

    @Override
    public UsbHub getUsbHub()
    {
        return this.hub;
    }


    /**
     * @see UsbPort#getUsbDevice()
     */

    @Override
    public UsbDevice getUsbDevice()
    {
        return this.device;
    }


    /**
     * @see UsbPort#isUsbDeviceAttached()
     */

    @Override
    public boolean isUsbDeviceAttached()
    {
        return this.device != null;
    }


    /**
     * Connects the specified device to this port.
     *
     * @param device
     *            The device to connect.
     */

    void connectUsbDevice(final UsbDevice device)
    {
        if (device == null)
            throw new IllegalArgumentException("device must not be null");
        if (this.device != null)
            throw new IllegalStateException(
                "Port already has a connected device");
        this.device = device;
        ((AbstractDevice) device).setParentUsbPort(this);
    }


    /**
     * Disconnects the currently connected device.
     */

    void disconnectUsbDevice()
    {
        if (this.device == null)
            throw new IllegalStateException("Port has no connected device");
        final AbstractDevice device = (AbstractDevice) this.device;
        this.device = null;
        device.setParentUsbPort(null);
    }
}
