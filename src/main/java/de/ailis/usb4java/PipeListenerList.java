/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

/**
 * USB pipe listener list.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class PipeListenerList extends
    EventListenerList<UsbPipeListener> implements UsbPipeListener
{
    /**
     * Constructs a new USB pipe listener list.
     */
    PipeListenerList()
    {
        super();
    }
    
    @Override
    public UsbPipeListener[] toArray()
    {
        return getListeners().toArray(
            new UsbPipeListener[getListeners().size()]);
    }

    @Override
    public void errorEventOccurred(final UsbPipeErrorEvent event)
    {
        for (final UsbPipeListener listener: toArray())
        {
            listener.errorEventOccurred(event);
        }
    }

    @Override
    public void dataEventOccurred(final UsbPipeDataEvent event)
    {
        for (final UsbPipeListener listener: toArray())
        {
            listener.dataEventOccurred(event);
        }
    }
}
