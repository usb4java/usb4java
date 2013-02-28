/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */

package de.ailis.usb4java.libusb;

import java.util.Iterator;

/**
 * List of devices as returned by
 * {@link LibUSB#getDeviceList(Context, DeviceList)}
 */
public final class DeviceList implements Iterable<Device>
{
    /** The native pointer to the devices array. */
    long pointer;

    /** The number of devices in the list. */
    int size;

    /**
     * Returns the number of devices in the list.
     * 
     * @return The number of devices in the list.
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Returns the device with the specified index.
     * 
     * @param index
     *            The device index.
     * @return The device or null when index is out of bounds.
     */
    public native Device get(final int index);

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Device> iterator()
    {
        return new DeviceListIterator(this);
    }
}
