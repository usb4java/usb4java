/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.util.List;

import javax.usb.UsbHub;

import de.ailis.usb4java.jni.USB_Device;

/**
 * usb4java implementation of JSR-80 UsbHub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaHub extends Usb4JavaDevice implements UsbHub,
    UsbPorts<Usb4JavaPort, Usb4JavaDevice>
{
    /** The hub ports. */
    private final Usb4JavaPorts ports = new Usb4JavaPorts(this);

    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */
    public Usb4JavaHub(final USB_Device device)
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
    public List<Usb4JavaPort> getUsbPorts()
    {
        return this.ports.getUsbPorts();
    }

    /**
     * @see UsbHub#getUsbPort(byte)
     */
    @Override
    public Usb4JavaPort getUsbPort(final byte number)
    {
        return this.ports.getUsbPort(number);
    }

    /**
     * @see UsbHub#getAttachedUsbDevices()
     */
    @Override
    public List<Usb4JavaDevice> getAttachedUsbDevices()
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
     * @see UsbPorts#connectUsbDevice(javax.usb.UsbDevice)
     */
    @Override
    public void connectUsbDevice(final Usb4JavaDevice device)
    {
        this.ports.connectUsbDevice(device);
    }

    /**
     * @see UsbPorts#disconnectUsbDevice(javax.usb.UsbDevice)
     */
    @Override
    public void disconnectUsbDevice(final Usb4JavaDevice device)
    {
        this.ports.disconnectUsbDevice(device);
    }

    /**
     * @see javax.usb.UsbDevice#isUsbHub()
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
        final Usb4JavaHub other = (Usb4JavaHub) obj;
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
