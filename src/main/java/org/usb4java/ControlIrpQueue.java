/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import javax.usb.UsbControlIrp;
import javax.usb.UsbException;
import javax.usb.UsbIrp;
import javax.usb.event.UsbDeviceDataEvent;

/**
 * A queue for USB control I/O request packets.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class ControlIrpQueue extends AbstractIrpQueue<UsbControlIrp>
{
    /** The USB device listener list. */
    private final DeviceListenerList listeners;

    /**
     * Constructor.
     * 
     * @param device
     *            The USB device.
     * @param listeners
     *            The USB device listener list.
     */
    ControlIrpQueue(final AbstractDevice device,
        final DeviceListenerList listeners)
    {
        super(device);
        this.listeners = listeners;
    }

    @Override
    protected void processIrp(final UsbControlIrp irp) throws UsbException
    {
        processControlIrp(irp);
    }

    @Override
    protected void finishIrp(final UsbIrp irp)
    {
        this.listeners.dataEventOccurred(new UsbDeviceDataEvent(
            getDevice(), (UsbControlIrp) irp));
    }
}
