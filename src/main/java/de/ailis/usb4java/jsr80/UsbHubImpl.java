/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbPort;

import de.ailis.usb4java.USB_Device;


/**
 * usb4java implementation of JSR-80 UsbHub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class UsbHubImpl extends AbstractDevice implements UsbHub, UsbPorts
{
    /** The hub ports. */
    private final UsbPortsImpl ports = new UsbPortsImpl(this);


    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */

    public UsbHubImpl(final USB_Device device)
    {
        super(device);
    }


    /**
     * @see UsbHub#getNumberOfPorts()
     */

    @Override
    public byte getNumberOfPorts()
    {
        return this.ports.getNumberOfPorts();
    }


    /**
     * @see UsbHub#getUsbPorts()
     */

    @Override
    public List<UsbPort> getUsbPorts()
    {
        return this.ports.getUsbPorts();
    }


    /**
     * @see UsbHub#getUsbPort(byte)
     */

    @Override
    public UsbPort getUsbPort(final byte number)
    {
        return this.ports.getUsbPort(number);
    }


    /**
     * @see UsbHub#getAttachedUsbDevices()
     */

    @Override
    public List<UsbDevice> getAttachedUsbDevices()
    {
        return this.ports.getAttachedUsbDevices();
    }


    /**
     * @see UsbHub#isRootUsbHub()
     */

    @Override
    public boolean isRootUsbHub()
    {
        return false;
    }


    /**
     * Connects a new device to this hub.
     *
     * @param device
     *            The device to add to this hub.
     */

    @Override
    public void connectUsbDevice(final UsbDevice device)
    {
        this.ports.connectUsbDevice(device);
    }


    /**
     * Disconnects the specified device from the hub.
     *
     * @param device
     *            The device to disconnected from the hub.
     */

    @Override
    public void disconnectUsbDevice(final UsbDevice device)
    {
        this.ports.disconnectUsbDevice(device);
    }


    /**
     * @see UsbDevice#isUsbHub()
     */

    @Override
    public boolean isUsbHub()
    {
        return true;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final UsbHubImpl other = (UsbHubImpl) obj;
        return this.device.equals(other.device);
    }


    /**
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode()
    {
        return this.device.hashCode();
    }
}
