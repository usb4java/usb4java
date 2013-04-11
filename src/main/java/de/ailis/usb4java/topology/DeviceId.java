/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.topology;

import java.io.Serializable;

import de.ailis.usb4java.descriptors.SimpleUsbDeviceDescriptor;

/**
 * Unique USB device ID.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceId implements Serializable
{
    /** The serial versionUID. */
    private static final long serialVersionUID = 1L;

    /** The bus number. */
    private final int busNumber;

    /** The device address. */
    private final int deviceAddress;

    /** The port this device is connected to. 0 if unknown. */
    private final int portNumber;

    /** The device descriptor. */
    private final SimpleUsbDeviceDescriptor deviceDescriptor;

    /**
     * Constructs a new device id.
     * 
     * @param busNumber
     *            The number of the bus the device is connected to.
     * @param deviceAddress
     *            The address of the device.
     * @param portNumber
     *            The number of the port the device is connected to. 0 if
     *            unknown.
     * @param deviceDescriptor
     *            The device descriptor. Must not be null.
     */
    public DeviceId(final int busNumber, final int deviceAddress,
        int portNumber,
        final SimpleUsbDeviceDescriptor deviceDescriptor)
    {
        if (deviceDescriptor == null)
            throw new IllegalArgumentException("deviceDescriptor must be set");
        this.busNumber = busNumber;
        this.portNumber = portNumber;
        this.deviceAddress = deviceAddress;
        this.deviceDescriptor = deviceDescriptor;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.busNumber;
        result = prime * result + this.deviceAddress;
        result = prime * result + this.portNumber;
        result = prime * result + ((this.deviceDescriptor == null) ? 0 :
            this.deviceDescriptor.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DeviceId other = (DeviceId) obj;
        if (this.busNumber != other.busNumber) return false;
        if (this.deviceAddress != other.deviceAddress) return false;
        if (this.portNumber != other.portNumber) return false;
        if (this.deviceDescriptor == null)
        {
            if (other.deviceDescriptor != null) return false;
        }
        else if (!this.deviceDescriptor.equals(other.deviceDescriptor))
            return false;
        return true;
    }

    /**
     * Returns the bus number.
     * 
     * @return The bus number.
     */
    public int getBusNumber()
    {
        return this.busNumber;
    }

    /**
     * Returns the device address.
     * 
     * @return The device address.
     */
    public int getDeviceAddress()
    {
        return this.deviceAddress;
    }

    /**
     * Returns the number of the port the device is connected to.
     * 
     * @return The port number or 0 if unknown.
     */
    public int getPortNumber()
    {
        return this.portNumber;
    }

    /**
     * Returns the device descriptor.
     * 
     * @return The device descriptor. Never null.
     */
    public SimpleUsbDeviceDescriptor getDeviceDescriptor()
    {
        return this.deviceDescriptor;
    }
}
