/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */

package de.ailis.usb4java.libusb;

import java.util.Iterator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * List of devices as returned by
 * {@link LibUSB#getDeviceList(Context, DeviceList)}.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceList implements Iterable<Device>
{
    /** The native pointer to the devices array. */
    private long pointer;

    /** The number of devices in the list. */
    private int size;

    /**
     * Constructs a new device list. Must be passed to
     * {@link LibUSB#getDeviceList(Context, DeviceList)} before using it.
     */
    public DeviceList()
    {
        // Empty
    }       

    /**
     * Returns the native pointer.
     * 
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.pointer;
    }
        
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

    @Override
    public Iterator<Device> iterator()
    {
        return new DeviceListIterator(this);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.pointer).toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final DeviceList other = (DeviceList) obj;
        return this.pointer == other.pointer;
    }
}
