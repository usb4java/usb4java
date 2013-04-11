/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.util.List;

import javax.usb.UsbHub;

import de.ailis.usb4java.exceptions.Usb4JavaException;
import de.ailis.usb4java.libusb.Device;

/**
 * usb4java implementation of JSR-80 UsbHub.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaHub extends Usb4JavaDevice implements UsbHub,
    UsbPorts<Usb4JavaPort, Usb4JavaDevice>
{
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The hub ports. */
    private final Usb4JavaPorts ports = new Usb4JavaPorts(this);

    /**
     * Constructor.
     * 
     * @param manager
     *            The USB device manager which is responsible for this device.
     * @param id
     *            THe device id. Must not be null.
     * @param parentId
     *            The parent id. may be null if this device has no parent.
     * @param speed
     *            The device speed.
     * @param device
     *            The libusb device. This reference is only valid during the
     *            constructor execution, so don't store it in a property or
     *            something like that.
     * @throws Usb4JavaException
     *             When device configuration could not be read.
     */
    public Usb4JavaHub(final UsbDeviceManager manager, final DeviceId id,
        final DeviceId parentId, final int speed, final Device device)
        throws Usb4JavaException
    {
        super(manager, id, parentId, speed, device);
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
}
