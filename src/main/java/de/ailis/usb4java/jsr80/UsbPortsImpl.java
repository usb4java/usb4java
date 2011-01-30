/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbPort;


/**
 * A list of USB ports.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class UsbPortsImpl implements UsbPorts
{
    /** The hub ports. */
    private final List<UsbPort> ports = new ArrayList<UsbPort>();

    /** The hub these ports belong to. */
    private final UsbHub hub;


    /**
     * Constructor.
     *
     * @param hub
     *            The hub the port belongs to.
     */

    UsbPortsImpl(final UsbHub hub)
    {
        this.hub = hub;
        addPort();
    }


    /**
     * Adds a new port and returns it.
     *
     * @return The added port.
     */

    private UsbPort addPort()
    {
        final byte portNo = (byte) (this.ports.size() + 1);
        final UsbPortImpl port = new UsbPortImpl(this.hub, portNo);
        this.ports.add(port);
        return port;
    }


    /**
     * Returns the first free port or adds a new one if no free port was found.
     *
     * @return The first free port.
     */

    private UsbPort getFreePort()
    {
        for (final UsbPort port : this.ports)
        {
            if (!port.isUsbDeviceAttached()) return port;
        }
        return addPort();
    }


    /**
     * Returns the number of ports.
     *
     * @return The number of ports.
     */

    @Override
    public byte getNumberOfPorts()
    {
        return (byte) this.ports.size();
    }


    /**
     * Returns the ports.
     *
     * @return The ports.
     */

    @Override
    public List<UsbPort> getUsbPorts()
    {
        return Collections.unmodifiableList(this.ports);
    }


    /**
     * Returns the USB port with the specified port number.
     *
     * @param number
     *            The USB port number.
     * @return The USB port or null if no such port.
     */

    @Override
    public UsbPort getUsbPort(final byte number)
    {
        final int index = (number & 0xff) - 1;
        if (index < 0 || index >= this.ports.size()) return null;
        return this.ports.get(index);
    }


    /**
     * Returns the attached USB devices.
     *
     * @return The attached USB devices.
     */

    @Override
    public List<UsbDevice> getAttachedUsbDevices()
    {
        final List<UsbDevice> devices = new ArrayList<UsbDevice>();
        synchronized (this.ports)
        {
            for (final UsbPort port : this.ports)
            {
                if (port.isUsbDeviceAttached())
                {
                    devices.add(port.getUsbDevice());
                }
            }
        }
        return Collections.unmodifiableList(devices);
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
        synchronized (this.ports)
        {
            final UsbPortImpl port = (UsbPortImpl) getFreePort();
            port.connectUsbDevice(device);
        }
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
        synchronized (this.ports)
        {
            for (final UsbPort port : this.ports)
            {
                if (device.equals(port.getUsbDevice()))
                {
                    ((UsbPortImpl) port).disconnectUsbDevice();
                }
            }
        }
    }
}
