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

import com.sun.jna.Pointer;

/**
 * Structure representing a USB device detected on the system.
 *
 * This is an opaque type for which you are only ever provided with a pointer, usually originating from
 * {@link LibUsb#getDeviceList(Context, DeviceList)}.
 *
 * Certain operations can be performed on a device, but in order to do any I/O you will have to first obtain a device
 * handle using {@link LibUsb#open(Device, DeviceHandle)}.
 *
 * Devices are reference counted with {@link LibUsb#refDevice(Device)} and {@link LibUsb#unrefDevice(Device)}, and are
 * freed when the reference count reaches 0. New devices presented by {@link LibUsb#getDeviceList(Context, DeviceList)}
 * have a reference count of 1, and {@link LibUsb#freeDeviceList(DeviceList, boolean)} can optionally decrease the
 * reference count on all devices in the list. {@link LibUsb#open(Device, DeviceHandle)} adds another reference which is
 * later destroyed by {@link LibUsb#close(DeviceHandle)}.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Device {
    /** The native pointer to the device structure. */
    private final Pointer nativeDevicePointer;

    /**
     * Creates a new device.
     * 
     * @param nativeDevicePointer The native device pointer.
     */
    Device(final Pointer nativeDevicePointer) {
        this.nativeDevicePointer = nativeDevicePointer;
    }

    /**
     * Returns the native pointer to the device structure.
     *
     * @return The native pointer to the device structure.
     */
    Pointer getNative() {
        return this.nativeDevicePointer;
    }

    @Override
    public int hashCode() {
        final long nativePointer = Pointer.nativeValue(this.nativeDevicePointer);
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
        final Device other = (Device) obj;
        if (Pointer.nativeValue(this.nativeDevicePointer) != Pointer.nativeValue(other.nativeDevicePointer)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("libusb device 0x%x", Pointer.nativeValue(this.nativeDevicePointer));
    }
}
