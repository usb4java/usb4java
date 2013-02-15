/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

/**
 * USB services listener list.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbServicesListenerList extends
        EventListenerList<UsbServicesListener> implements UsbServicesListener
{
    /**
     * @see EventListenerList#toArray()
     */
    @Override
    public UsbServicesListener[] toArray()
    {
        return this.listeners.toArray(new UsbServicesListener[this.listeners
                .size()]);
    }

    /**
     * @see UsbServicesListener#usbDeviceAttached(UsbServicesEvent)
     */
    @Override
    public void usbDeviceAttached(final UsbServicesEvent event)
    {
        for (final UsbServicesListener listener : toArray())
            listener.usbDeviceAttached(event);
    }

    /**
     * @see UsbServicesListener#usbDeviceDetached(UsbServicesEvent)
     */
    @Override
    public void usbDeviceDetached(final UsbServicesEvent event)
    {
        for (final UsbServicesListener listener : toArray())
            listener.usbDeviceDetached(event);
    }
}
