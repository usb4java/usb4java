/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbHub;

/**
 * A list of USB ports.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class Ports
    implements UsbPorts<Port, AbstractDevice>
{
    /** The hub ports. */
    private final List<Port> ports = new ArrayList<Port>();

    /** The hub these ports belong to. */
    private final UsbHub hub;

    /**
     * Constructor.
     * 
     * @param hub
     *            The hub the port belongs to.
     */
    Ports(final UsbHub hub)
    {
        this.hub = hub;
        addPort();
    }

    /**
     * Adds a new port and returns it.
     * 
     * @return The added port.
     */
    private Port addPort()
    {
        final byte portNo = (byte) (this.ports.size() + 1);
        final Port port = new Port(this.hub, portNo);
        this.ports.add(port);
        return port;
    }

    /**
     * Returns the first free port or adds a new one if no free port was found.
     * 
     * @return The first free port.
     */
    private Port getFreePort()
    {
        for (final Port port: this.ports)
        {
            if (!port.isUsbDeviceAttached()) return port;
        }
        return addPort();
    }

    @Override
    public byte getNumberOfPorts()
    {
        return (byte) this.ports.size();
    }

    @Override
    public List<Port> getUsbPorts()
    {
        return Collections.unmodifiableList(this.ports);
    }

    @Override
    public Port getUsbPort(final byte number)
    {
        final int index = (number & 0xff) - 1;
        if (index < 0 || index >= this.ports.size()) return null;
        return this.ports.get(index);
    }

    @Override
    public List<AbstractDevice> getAttachedUsbDevices()
    {
        final List<AbstractDevice> devices = new ArrayList<AbstractDevice>();
        synchronized (this.ports)
        {
            for (final Port port: this.ports)
            {
                if (port.isUsbDeviceAttached())
                {
                    devices.add(port.getUsbDevice());
                }
            }
        }
        return Collections.unmodifiableList(devices);
    }
    
    @Override
    public boolean isUsbDeviceAttached(final AbstractDevice device)
    {
        synchronized (this.ports)
        {
            for (final Port port: this.ports)
            {
                if (device.equals(port.getUsbDevice())) return true;
            }            
        }
        return false;
    }

    @Override
    public void connectUsbDevice(final AbstractDevice device)
    {
        synchronized (this.ports)
        {
            final Port port = getFreePort();
            port.connectUsbDevice(device);
        }
    }

    @Override
    public void disconnectUsbDevice(final AbstractDevice device)
    {
        synchronized (this.ports)
        {
            for (final Port port: this.ports)
            {
                if (device.equals(port.getUsbDevice()))
                {
                    port.disconnectUsbDevice();
                }
            }
        }
    }
}
