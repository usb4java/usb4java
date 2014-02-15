/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import java.util.List;

import javax.usb.UsbHub;

import org.libusb4java.Device;
import org.libusb4java.LibUsbException;

/**
 * usb4java implementation of JSR-80 UsbHub.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class Hub extends AbstractDevice implements UsbHub,
    UsbPorts<Port, AbstractDevice>
{
    /** The hub ports. */
    private final Ports ports = new Ports(this);

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
    Hub(final DeviceManager manager, final DeviceId id,
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
    public List<Port> getUsbPorts()
    {
        return this.ports.getUsbPorts();
    }

    @Override
    public Port getUsbPort(final byte number)
    {
        return this.ports.getUsbPort(number);
    }

    @Override
    public List<AbstractDevice> getAttachedUsbDevices()
    {
        return this.ports.getAttachedUsbDevices();
    }
    
    @Override
    public boolean isUsbDeviceAttached(final AbstractDevice device)
    {
        return this.ports.isUsbDeviceAttached(device);
    }

    @Override
    public boolean isRootUsbHub()
    {
        return false;
    }

    @Override
    public void connectUsbDevice(final AbstractDevice device)
    {
        this.ports.connectUsbDevice(device);
    }

    @Override
    public void disconnectUsbDevice(final AbstractDevice device)
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
        if (obj == null || getClass() != obj.getClass()) return false;
        final AbstractDevice other = (AbstractDevice) obj;
        return getId().equals(other.getId());
    }    

    @Override
    public boolean isUsbHub()
    {
        return true;
    }
}
