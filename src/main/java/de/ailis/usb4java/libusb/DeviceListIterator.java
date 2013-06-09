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
    private int nextIndex;

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

    @Override
    public boolean hasNext()
    {
        return nextIndex < devices.getSize();
    }

    @Override
    public Device next()
    {
        return devices.get(nextIndex++);
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
