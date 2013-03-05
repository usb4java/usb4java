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

/**
 * Structure representing a handle on a USB device.
 * 
 * This is an opaque type for which you are only ever provided with a pointer,
 * usually originating from {@link LibUSB#open(Device, DeviceHandle)}.
 * 
 * A device handle is used to perform I/O and other operations. When finished
 * with a device handle, you should call {@link LibUSB#close(DeviceHandle)}.
 */
public final class DeviceHandle
{
    /** The native pointer to the device handle structure. */
    long pointer;
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.pointer ^ (this.pointer >>> 32));
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
        DeviceHandle other = (DeviceHandle) obj;
        if (this.pointer != other.pointer) return false;
        return true;
    }
}
