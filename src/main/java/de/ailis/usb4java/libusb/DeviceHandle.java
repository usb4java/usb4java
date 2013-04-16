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

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Structure representing a handle on a USB device.
 * 
 * This is an opaque type for which you are only ever provided with a pointer,
 * usually originating from {@link LibUsb#open(Device, DeviceHandle)}.
 * 
 * A device handle is used to perform I/O and other operations. When finished
 * with a device handle, you should call {@link LibUsb#close(DeviceHandle)}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceHandle
{
    /** The native pointer to the device handle structure. */
    private long handlePointer;

    /**
     * Constructs a new device handle. Must be passed to
     * {@link LibUsb#open(Device, DeviceHandle)} before passing it to any
     * other method.
     */
    public DeviceHandle()
    {
        // Empty
    }

    /**
     * Returns the native pointer to the device handle structure.
     * 
     * @return The native pointer to the device handle structure.
     */
    public long getPointer()
    {
        return this.handlePointer;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.handlePointer).toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final DeviceHandle other = (DeviceHandle) obj;
        return this.handlePointer == other.handlePointer;
    }

    @Override
    public String toString()
    {
        return String.format("libusb handle 0x%x", this.handlePointer);
    }
}
