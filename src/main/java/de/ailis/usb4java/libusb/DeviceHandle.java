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

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Structure representing a handle on a USB device.
 * 
 * This is an opaque type for which you are only ever provided with a pointer,
 * usually originating from {@link LibUSB#open(Device, DeviceHandle)}.
 * 
 * A device handle is used to perform I/O and other operations. When finished
 * with a device handle, you should call {@link LibUSB#close(DeviceHandle)}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceHandle
{
    /** The native pointer to the device handle structure. */
    private long pointer;

    /**
     * Returns the native pointer.
     * 
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.pointer;
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
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final DeviceHandle other = (DeviceHandle) obj;
        return this.pointer != other.pointer;
    }
    
    @Override
    public String toString()
    {
        return String.format("libusb handle 0x%x", this.pointer);
    }
}
