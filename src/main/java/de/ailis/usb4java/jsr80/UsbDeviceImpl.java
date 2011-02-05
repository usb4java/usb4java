/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import de.ailis.usb4java.USB_Device;


/**
 * usb4java implemention of JSR-80 UsbDevice.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class UsbDeviceImpl extends AbstractDevice
{
    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */

    public UsbDeviceImpl(final USB_Device device)
    {
        super(device);
    }


    /**
     * @see javax.usb.UsbDevice#isUsbHub()
     */

    @Override
    public boolean isUsbHub()
    {
        return false;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final UsbDeviceImpl other = (UsbDeviceImpl) obj;
        return this.device.equals(other.device);
    }


    /**
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode()
    {
        return this.device.hashCode();
    }
}
