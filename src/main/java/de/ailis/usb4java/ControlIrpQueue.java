/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;

import javax.usb.UsbControlIrp;
import javax.usb.UsbException;
import javax.usb.UsbIrp;
import javax.usb.UsbShortPacketException;
import javax.usb.event.UsbDeviceDataEvent;

import de.ailis.usb4java.libusb.DeviceHandle;
import de.ailis.usb4java.libusb.LibUSB;
import de.ailis.usb4java.libusb.LibUsbException;
import de.ailis.usb4java.support.UsbDeviceListenerList;

/**
 * A queue for USB control I/O request packets.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class ControlIrpQueue extends AbstractIrpQueue<UsbControlIrp>
{
    /** The USB device listener list. */
    private final UsbDeviceListenerList listeners;

    /**
     * Constructor.
     * 
     * @param device
     *            The USB device.
     * @param listeners
     *            The USB device listener list.
     */
    ControlIrpQueue(final AbstractDevice device,
        final UsbDeviceListenerList listeners)
    {
        super(device);
        this.listeners = listeners;
    }

    @Override
    protected void processIrp(final UsbControlIrp irp) throws UsbException
    {
        final ByteBuffer buffer =
            ByteBuffer.allocateDirect(irp.getLength());
        buffer.put(irp.getData(), irp.getOffset(), irp.getLength());
        buffer.rewind();
        final DeviceHandle handle = getDevice().open();
        final int result = LibUSB.controlTransfer(handle, irp.bmRequestType(),
            irp.bRequest(), irp.wValue(), irp.wIndex(), buffer,
            getConfig().getTimeout());
        if (result < 0)
        {
            throw new LibUsbException("Unable to submit control message",
                result);
        }
        buffer.rewind();
        buffer.get(irp.getData(), irp.getOffset(), result);
        irp.setActualLength(result);
        if (irp.getActualLength() != irp.getLength()
            && !irp.getAcceptShortPacket())
        {
            throw new UsbShortPacketException();
        }
    }

    @Override
    protected void finishIrp(final UsbIrp irp)
    {
        this.listeners.dataEventOccurred(new UsbDeviceDataEvent(
            getDevice(), (UsbControlIrp) irp));
    }
}
