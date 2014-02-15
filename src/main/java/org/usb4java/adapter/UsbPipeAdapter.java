/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.adapter;

import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

/**
 * An abstract adapter class for receiving USB pipe events. The methods in
 * this class are empty. This class exists as convenience for creating listener
 * objects.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public abstract class UsbPipeAdapter implements UsbPipeListener
{
    @Override
    public void errorEventOccurred(final UsbPipeErrorEvent event)
    {
        // Empty
    }

    @Override
    public void dataEventOccurred(final UsbPipeDataEvent event)
    {
        // Empty
    }
}
