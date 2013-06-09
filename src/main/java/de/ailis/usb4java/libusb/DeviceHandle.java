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
    private long deviceHandlePointer;

    /**
     * Constructs a new device handle. Must be passed to
     * {@link LibUsb#open(Device, DeviceHandle)} before passing it to any other
     * method.
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
        return deviceHandlePointer;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + (int) (deviceHandlePointer ^ (deviceHandlePointer >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final DeviceHandle other = (DeviceHandle) obj;
        if (deviceHandlePointer != other.deviceHandlePointer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb device handle 0x%x", deviceHandlePointer);
    }
}
