/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbHub;



/**
 * A list of USB ports.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class LibUsbPorts implements UsbPorts<LibUsbPort, LibUsbDevice>
{
    /** The hub ports. */
    private final List<LibUsbPort> ports = new ArrayList<LibUsbPort>();

    /** The hub these ports belong to. */
    private final UsbHub hub;


    /**
     * Constructor.
     *
     * @param hub
     *            The hub the port belongs to.
     */

    public LibUsbPorts(final UsbHub hub)
    {
        this.hub = hub;
        addPort();
    }


    /**
     * Adds a new port and returns it.
     *
     * @return The added port.
     */

    private LibUsbPort addPort()
    {
        final byte portNo = (byte) (this.ports.size() + 1);
        final LibUsbPort port = new LibUsbPort(this.hub, portNo);
        this.ports.add(port);
        return port;
    }


    /**
     * Returns the first free port or adds a new one if no free port was found.
     *
     * @return The first free port.
     */

    private LibUsbPort getFreePort()
    {
        for (final LibUsbPort port : this.ports)
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
    public List<LibUsbPort> getUsbPorts()
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
    public LibUsbPort getUsbPort(final byte number)
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
    public List<LibUsbDevice> getAttachedUsbDevices()
    {
        final List<LibUsbDevice> devices = new ArrayList<LibUsbDevice>();
        synchronized (this.ports)
        {
            for (final LibUsbPort port : this.ports)
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
    public void connectUsbDevice(final LibUsbDevice device)
    {
        synchronized (this.ports)
        {
            final LibUsbPort port = getFreePort();
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
    public void disconnectUsbDevice(final LibUsbDevice device)
    {
        synchronized (this.ports)
        {
            for (final LibUsbPort port : this.ports)
            {
                if (device.equals(port.getUsbDevice()))
                {
                    port.disconnectUsbDevice();
                }
            }
        }
    }
}
