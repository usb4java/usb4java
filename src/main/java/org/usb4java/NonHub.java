/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import org.usb4java.libusb.Device;
import org.usb4java.libusb.LibUsbException;

/**
 * A non-hub USB device.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
class NonHub extends AbstractDevice
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
    NonHub(final DeviceManager manager, final DeviceId id,
        final DeviceId parentId, final int speed, final Device device)
        throws LibUsbException
    {
        super(manager, id, parentId, speed, device);
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final NonHub other = (NonHub) obj;
        return getId().equals(other.getId());
    }

    @Override
    public boolean isUsbHub()
    {
        return false;
    }
}
