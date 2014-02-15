/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.adapter;

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;

/**
 * An abstract adapter class for receiving USB device events. The methods in
 * this class are empty. This class exists as convenience for creating listener
 * objects.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public abstract class UsbDeviceAdapter implements UsbDeviceListener
{
    @Override
    public void usbDeviceDetached(final UsbDeviceEvent event)
    {
        // Empty
    }

    @Override
    public void errorEventOccurred(final UsbDeviceErrorEvent event)
    {
        // Empty
    }

    @Override
    public void dataEventOccurred(final UsbDeviceDataEvent event)
    {
        // Empty
    }
}
