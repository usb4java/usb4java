/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.adapter;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

/**
 * An abstract adapter class for receiving USB service events. The methods in
 * this class are empty. This class exists as convenience for creating listener
 * objects.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public abstract class UsbServicesAdapter implements UsbServicesListener
{
    @Override
    public void usbDeviceAttached(final UsbServicesEvent event)
    {
        // Empty
    }

    @Override
    public void usbDeviceDetached(final UsbServicesEvent event)
    {
        // Empty
    }
}
