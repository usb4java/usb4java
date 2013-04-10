/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import de.ailis.usb4java.libusb.DeviceHandle;


/**
 * USB Device handle.
 *
 * @author Klaus Reimer (k@ailis.de)
 * 
 * @deprecated Use the new libusb 1.0 API or the JSR 80 API.
 */
@Deprecated
public final class USB_Dev_Handle
{
    /** The new device handle device handle structure. */
    final DeviceHandle handle;
    
    /** The last claimed interface. */
    int lastClaimedInterface = -1;

    /**
     * Constructor.
     *
     * @param handle
     *            The new handle.
     */
    USB_Dev_Handle(final DeviceHandle handle)
    {
        this.handle = handle;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "USB_Dev_Handle " + this.handle;
    }
}
