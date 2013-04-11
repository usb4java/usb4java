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

/**
 * Structure representing a USB device detected on the system.
 * 
 * This is an opaque type for which you are only ever provided with a pointer,
 * usually originating from {@link LibUSB#getDeviceList(Context, DeviceList)}.
 * 
 * Certain operations can be performed on a device, but in order to do any I/O
 * you will have to first obtain a device handle using
 * {@link LibUSB#open(Device, DeviceHandle)}.
 * 
 * Devices are reference counted with {@link LibUSB#refDevice(Device)} and
 * {@link LibUSB#unrefDevice(Device)}, and are freed when the reference count
 * reaches 0. New devices presented by
 * {@link LibUSB#getDeviceList(Context, DeviceList)} have a reference count of
 * 1, and {@link LibUSB#freeDeviceList(DeviceList, boolean)} can optionally
 * decrease the reference count on all devices in the list.
 * {@link LibUSB#open(Device, DeviceHandle)} adds another reference which is
 * later destroyed by {@link LibUSB#close(DeviceHandle)}.
 */
public final class Device
{
    /** The native pointer to the device structure. */
    long pointer;

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.pointer ^ (this.pointer >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Device other = (Device) obj;
        if (this.pointer != other.pointer) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb device 0x%x", this.pointer);
    }
}
