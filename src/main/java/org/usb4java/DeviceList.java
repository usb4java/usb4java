/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java;

import java.util.Iterator;

import com.sun.jna.Pointer;

/**
 * List of devices as returned by {@link LibUsb#getDeviceList(Context, DeviceList)}.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceList implements Iterable<Device> {
    /** The native pointer to the devices array. */
    private Pointer deviceListPointer;

    /** The number of devices in the list. */
    private int size;

    /**
     * Constructs a new device list. Must be passed to {@link LibUsb#getDeviceList(Context, DeviceList)} before using
     * it.
     */
    public DeviceList() {
        // Empty
    }

    /**
     * Initializes the device list.
     * 
     * @param deviceListPointer
     *            The native pointer to set.
     * @param size
     *            The number of devices.
     */
    void init(final Pointer deviceListPointer, final int size) {
        this.deviceListPointer = deviceListPointer;
        this.size = size;
    }
    
    /**
     * Returns the native pointer.
     *
     * @return The native pointer.
     */
    public Pointer getPointer() {
        return this.deviceListPointer;
    }

    /**
     * Returns the number of devices in the list.
     *
     * @return The number of devices in the list.
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the device with the specified index.
     *
     * @param index
     *            The device index.
     * @return The device or null when index is out of bounds.
     */
    public Device get(final int index) {
        return new Device(this.deviceListPointer.getPointer(index * Pointer.SIZE));
    }

    @Override
    public Iterator<Device> iterator() {
        return new DeviceListIterator(this);
    }

    @Override
    public int hashCode() {
        final long nativePointer = Pointer.nativeValue(this.deviceListPointer);
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (nativePointer ^ (nativePointer >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final DeviceList other = (DeviceList) obj;
        return Pointer.nativeValue(this.deviceListPointer) == Pointer.nativeValue(other.deviceListPointer);
    }

    @Override
    public String toString() {
        return String.format("libusb device list 0x%x with size %d", Pointer.nativeValue(this.deviceListPointer),
            this.size);
    }
}
