/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import java.util.Iterator;

/**
 * Iterator for device list.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class DeviceListIterator implements Iterator<Device>
{
    /** The devices list. */
    private final DeviceList devices;

    /** The current index. */
    private int index;

    /**
     * Constructor.
     * 
     * @param devices
     *            The devices list.
     */
    DeviceListIterator(final DeviceList devices)
    {
        this.devices = devices;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return this.index < this.devices.getSize() - 1;
    }

    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public Device next()
    {
        return this.devices.get(this.index++);
    }

    /**
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
