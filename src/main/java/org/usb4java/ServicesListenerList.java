/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

/**
 * USB services listener list.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class ServicesListenerList extends
    EventListenerList<UsbServicesListener> implements UsbServicesListener
{
    /**
     * Constructs a new USB services listener list.
     */
    ServicesListenerList()
    {
        super();
    }

    @Override
    public UsbServicesListener[] toArray()
    {
        return getListeners().toArray(
            new UsbServicesListener[getListeners().size()]);
    }

    @Override
    public void usbDeviceAttached(final UsbServicesEvent event)
    {
        for (final UsbServicesListener listener: toArray())
        {
            listener.usbDeviceAttached(event);
        }
    }

    @Override
    public void usbDeviceDetached(final UsbServicesEvent event)
    {
        for (final UsbServicesListener listener: toArray())
        {
            listener.usbDeviceDetached(event);
        }
    }
}
