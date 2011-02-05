/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import static de.ailis.usb4java.jni.USB.usb_control_msg;

import java.nio.ByteBuffer;

import javax.usb.UsbControlIrp;
import javax.usb.UsbException;
import javax.usb.UsbIrp;

import de.ailis.usb4java.exceptions.LibUsbException;
import de.ailis.usb4java.jni.USB_Dev_Handle;
import de.ailis.usb4java.topology.LibUsbDevice;


/**
 * A queue for USB control I/O request packets.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class ControlIrpQueue extends AbstractIrpQueue<UsbControlIrp>
{
    /**
     * Constructor.
     *
     * @param device
     *            The USB device.
     */

    public ControlIrpQueue(final LibUsbDevice device)
    {
        super(device);
    }


    /**
     * @see AbstractIrpQueue#processIrp(javax.usb.UsbIrp)
     */

    @Override
    protected void processIrp(final UsbControlIrp irp) throws UsbException
    {
        final ByteBuffer buffer =
            ByteBuffer.allocateDirect(irp.getLength());
        buffer.put(irp.getData(), 0, irp.getLength());
        buffer.rewind();
        final USB_Dev_Handle handle = this.device.open();
        final int len =
            usb_control_msg(handle, irp.bmRequestType(),
                irp.bRequest(), irp.wValue(), irp.wIndex(), buffer, 250);
        if (len < 0)
            throw new LibUsbException(
                "Unable to submit control message", len);
        buffer.rewind();
        buffer.get(irp.getData(), 0, len);
        irp.setActualLength(len);
    }


    /**
     * @see AbstractIrpQueue#finishIrp(javax.usb.UsbIrp)
     */

    @Override
    protected void finishIrp(final UsbIrp irp)
    {
        // Empty
    }
}
