/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import static de.ailis.usb4java.USB.usb_bulk_read;
import static de.ailis.usb4java.USB.usb_bulk_write;
import static de.ailis.usb4java.USB.usb_interrupt_read;
import static de.ailis.usb4java.USB.usb_interrupt_write;

import java.nio.ByteBuffer;

import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbIrp;

import de.ailis.usb4java.USB_Dev_Handle;


/**
 * A queue for USB control I/O request packets.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class IrpQueue extends AbstractIrpQueue<UsbIrp>
{
    /** The USB pipe. */
    private final UsbPipeImpl pipe;


    /**
     * Constructor.
     *
     * @param device
     *            The USB device.
     * @param pipe
     *            The USB pipe
     */

    public IrpQueue(final AbstractDevice device, final UsbPipeImpl pipe)
    {
        super(device);
        this.pipe = pipe;
    }


    /**
     * @see AbstractIrpQueue#finishIrp(UsbIrp)
     */

    @Override
    protected void finishIrp(final UsbIrp irp)
    {
        this.pipe.sendEvent(irp);
    }


    /**
     * @see AbstractIrpQueue#processIrp(javax.usb.UsbIrp)
     */

    @Override
    protected void processIrp(final UsbIrp irp) throws UsbException
    {
        final UsbEndpoint endpoint = this.pipe.getUsbEndpoint();
        final byte type = endpoint.getType();
        final byte direction = endpoint.getDirection();
        switch (type)
        {
            case UsbConst.ENDPOINT_TYPE_BULK:
                switch (direction)
                {
                    case UsbConst.ENDPOINT_DIRECTION_OUT:
                        irp.setActualLength(bulkWrite(irp.getData()));
                        break;

                    case UsbConst.ENDPOINT_DIRECTION_IN:
                        irp.setActualLength(bulkRead(irp.getData()));
                        break;

                    default:
                        throw new UsbException("Invalid direction: "
                                + direction);
                }
                break;

            case UsbConst.ENDPOINT_TYPE_INTERRUPT:
                switch (direction)
                {
                    case UsbConst.ENDPOINT_DIRECTION_OUT:
                        irp.setActualLength(interruptWrite(irp.getData()));
                        break;

                    case UsbConst.ENDPOINT_DIRECTION_IN:
                        irp.setActualLength(interruptRead(irp.getData()));
                        break;

                    default:
                        throw new UsbException("Invalid direction: "
                                + direction);
                }
                break;

            default:
                throw new UsbException("Unsupported endpoint type: "
                        + type);
        }
    }


    /**
     * Reads bytes from a bulk endpoint into the specified data array.
     *
     * @param data
     *            The data array to write the read bytes to.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of read bytes.
     */

    private int bulkRead(final byte[] data) throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int size =
                Math.min(data.length, descriptor.wMaxPacketSize() & 0xffff);
        final int ep = descriptor.bEndpointAddress();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final int result = usb_bulk_read(this.device.open(), ep, buffer, 5000);
        if (result < 0)
            throw new LibUsbException("Unable to read from interrupt endpoint",
                result);
        buffer.rewind();
        buffer.get(data, 0, result);
        return result;
    }


    /**
     * Returns the USB endpoint descriptor.
     *
     * @return The USB endpoint descriptor.
     */

    private UsbEndpointDescriptor getEndpointDescriptor()
    {
        return this.pipe.getUsbEndpoint().getUsbEndpointDescriptor();
    }


    /**
     * Writes the specified bytes to a bulk endpoint.
     *
     * @param data
     *            The data array with the bytes to write.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of written bytes.
     */

    private int bulkWrite(final byte[] data) throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int total = data.length;
        final int size = Math.min(total, descriptor.wMaxPacketSize() & 0xffff);
        final int ep = descriptor.bEndpointAddress();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final USB_Dev_Handle handle = this.device.open();
        int written = 0;
        while (written < total)
        {
            buffer.put(data, written, Math.min(total - written, size));
            buffer.rewind();
            final int result = usb_bulk_write(handle, ep, buffer, 5000);
            if (result < 0)
                throw new LibUsbException(
                    "Unable to write to bulk endpoint", result);
            written += result;
            buffer.rewind();
        }
        return written;
    }


    /**
     * Reads bytes from an interrupt endpoint into the specified data array.
     *
     * @param data
     *            The data array to write the read bytes to.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of read bytes.
     */

    private int interruptRead(final byte[] data) throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int size =
                Math.min(data.length, descriptor.wMaxPacketSize() & 0xffff);
        final int ep = descriptor.bEndpointAddress();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final int result =
                usb_interrupt_read(this.device.open(), ep, buffer, 5000);
        if (result < 0)
            throw new LibUsbException("Unable to read from interrupt endpoint",
                result);
        buffer.rewind();
        buffer.get(data, 0, result);
        return result;
    }


    /**
     * Writes the specified bytes to a interrupt endpoint.
     *
     * @param data
     *            The data array with the bytes to write.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of written bytes.
     */

    private int interruptWrite(final byte[] data) throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int total = data.length;
        final int size = Math.min(total, descriptor.wMaxPacketSize() & 0xffff);
        final int ep = descriptor.bEndpointAddress();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final USB_Dev_Handle handle = this.device.open();
        int written = 0;
        while (written < total)
        {
            buffer.put(data, written, Math.min(total - written, size));
            buffer.rewind();
            final int result = usb_interrupt_write(handle, ep, buffer, 5000);
            if (result < 0)
                throw new LibUsbException(
                    "Unable to write to interrupt endpoint", result);
            written += result;
            buffer.rewind();
        }
        return written;
    }
}
