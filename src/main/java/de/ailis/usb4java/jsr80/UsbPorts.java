/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbPort;


/**
 * A list of USB ports.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

interface UsbPorts
{
    /**
     * Returns the number of ports.
     *
     * @return The number of ports.
     */

    byte getNumberOfPorts();


    /**
     * Returns the ports.
     *
     * @return The ports.
     */

    List<UsbPort> getUsbPorts();


    /**
     * Returns the USB port with the specified port number.
     *
     * @param number
     *            The USB port number.
     * @return The USB port or null if no such port.
     */

    UsbPort getUsbPort(final byte number);


    /**
     * Returns the attached USB devices.
     *
     * @return The attached USB devices.
     */

    List<UsbDevice> getAttachedUsbDevices();


    /**
     * Connects a new device to this hub.
     *
     * @param device
     *            The device to add to this hub.
     */

    void connectUsbDevice(final UsbDevice device);


    /**
     * Disconnects the specified device from the hub.
     *
     * @param device
     *            The device to disconnected from the hub.
     */

    void disconnectUsbDevice(final UsbDevice device);
}
