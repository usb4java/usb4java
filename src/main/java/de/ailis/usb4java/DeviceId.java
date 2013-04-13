/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ailis.usb4java.descriptors.SimpleUsbDeviceDescriptor;

/**
 * Unique USB device ID.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class DeviceId implements Serializable
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
    DeviceId(final int busNumber, final int deviceAddress,
        final int portNumber, final SimpleUsbDeviceDescriptor deviceDescriptor)
    {
        if (deviceDescriptor == null)
            throw new IllegalArgumentException("deviceDescriptor must be set");
        this.busNumber = busNumber;
        this.portNumber = portNumber;
        this.deviceAddress = deviceAddress;
        this.deviceDescriptor = deviceDescriptor;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(this.busNumber)
            .append(this.deviceAddress)
            .append(this.portNumber)
            .append(this.deviceDescriptor)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final DeviceId other = (DeviceId) obj;
        return new EqualsBuilder()
            .append(this.busNumber, other.busNumber)
            .append(this.deviceAddress, other.deviceAddress)
            .append(this.portNumber, other.portNumber)
            .append(this.deviceDescriptor, other.deviceDescriptor)
            .isEquals();
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

    @Override
    public String toString()
    {
        return String.format("Bus %03d Device %03d: ID %04x:%04x",
            this.busNumber, this.deviceAddress,
            this.deviceDescriptor.idVendor(),
            this.deviceDescriptor.idProduct());
    }
}
