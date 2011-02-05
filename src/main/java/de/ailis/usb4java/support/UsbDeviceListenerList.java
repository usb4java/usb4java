/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;



/**
 * USB device listener list.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbDeviceListenerList extends
        EventListenerList<UsbDeviceListener> implements UsbDeviceListener
{
    /**
     * @see EventListenerList#toArray()
     */

    @Override
    public UsbDeviceListener[] toArray()
    {
        return this.listeners.toArray(new UsbDeviceListener[this.listeners
                .size()]);
    }


    /**
     * @see UsbDeviceListener#usbDeviceDetached(UsbDeviceEvent)
     */

    @Override
    public void usbDeviceDetached(final UsbDeviceEvent event)
    {
        for (final UsbDeviceListener listener : toArray())
            listener.usbDeviceDetached(event);
    }


    /**
     * @see UsbDeviceListener#errorEventOccurred(UsbDeviceErrorEvent)
     */

    @Override
    public void errorEventOccurred(final UsbDeviceErrorEvent event)
    {
        for (final UsbDeviceListener listener : toArray())
            listener.errorEventOccurred(event);
    }


    /**
     * @see UsbDeviceListener#dataEventOccurred(UsbDeviceDataEvent)
     */

    @Override
    public void dataEventOccurred(final UsbDeviceDataEvent event)
    {
        for (final UsbDeviceListener listener : toArray())
            listener.dataEventOccurred(event);
    }
}
