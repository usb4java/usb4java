/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.util.List;

import javax.usb.UsbHub;

import de.ailis.usb4java.libusb.Device;
import de.ailis.usb4java.libusb.LibUsbException;

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
     * Constructs a new USB hub device.
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
     * @throws LibUsbException
     *             When device configuration could not be read.
     */
    public Usb4JavaHub(final UsbDeviceManager manager, final DeviceId id,
        final DeviceId parentId, final int speed, final Device device)
        throws LibUsbException
    {
        super(manager, id, parentId, speed, device);
    }

    @Override
    public byte getNumberOfPorts()
    {
        return this.ports.getNumberOfPorts();
    }

    @Override
    public List<Usb4JavaPort> getUsbPorts()
    {
        return this.ports.getUsbPorts();
    }

    @Override
    public Usb4JavaPort getUsbPort(final byte number)
    {
        return this.ports.getUsbPort(number);
    }

    @Override
    public List<Usb4JavaDevice> getAttachedUsbDevices()
    {
        return this.ports.getAttachedUsbDevices();
    }

    @Override
    public boolean isRootUsbHub()
    {
        return false;
    }

    @Override
    public void connectUsbDevice(final Usb4JavaDevice device)
    {
        this.ports.connectUsbDevice(device);
    }

    @Override
    public void disconnectUsbDevice(final Usb4JavaDevice device)
    {
        this.ports.disconnectUsbDevice(device);
    }
    
    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }    
    
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Usb4JavaDevice other = (Usb4JavaDevice) obj;
        return getId().equals(other.getId());
    }    

    @Override
    public boolean isUsbHub()
    {
        return true;
    }
}
