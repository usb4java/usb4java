/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import de.ailis.usb4java.libusb.Device;
import de.ailis.usb4java.libusb.LibUsbException;

/**
 * A non-hub USB device.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class Usb4JavaNonHub extends Usb4JavaDevice
{
    /**
     * Constructs a new non-hub USB device.
     * 
     * @param manager
     *            The USB device manager which is responsible for this device.
     * @param id
     *            The device id. Must not be null.
     * @param parentId
     *            The parent device id. May be null if this device has no parent
     *            (Because it is a root device).
     * @param speed
     *            The device speed.
     * @param device
     *            The libusb device. This reference is only valid during the
     *            constructor execution, so don't store it in a property or
     *            something like that.
     * @throws LibUsbException
     *             When device configuration could not be read.
     */
    Usb4JavaNonHub(final UsbDeviceManager manager, final DeviceId id,
        final DeviceId parentId, final int speed, final Device device)
        throws LibUsbException
    {
        super(manager, id, parentId, speed, device);
    }

    @Override
    public final int hashCode()
    {
        return getId().hashCode();
    }

    @Override
    public final boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Usb4JavaNonHub other = (Usb4JavaNonHub) obj;
        return getId().equals(other.getId());
    }

    @Override
    public final boolean isUsbHub()
    {
        return false;
    }
}
