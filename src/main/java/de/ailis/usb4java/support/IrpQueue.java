/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import static de.ailis.usb4java.jni.USB.usb_bulk_read;
import static de.ailis.usb4java.jni.USB.usb_bulk_write;
import static de.ailis.usb4java.jni.USB.usb_interrupt_read;
import static de.ailis.usb4java.jni.USB.usb_interrupt_write;

import java.nio.ByteBuffer;

import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbIrp;

import de.ailis.usb4java.exceptions.Usb4JavaException;
import de.ailis.usb4java.jni.USB_Dev_Handle;
import de.ailis.usb4java.topology.Usb4JavaDevice;
import de.ailis.usb4java.topology.Usb4JavaPipe;

/**
 * A queue for USB I/O request packets.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class IrpQueue extends AbstractIrpQueue<UsbIrp>
{
    /** The USB pipe. */
    private final Usb4JavaPipe pipe;

    /**
     * Constructor.
     *
     * @param device
     *            The USB device.
     * @param pipe
     *            The USB pipe
     */
    public IrpQueue(final Usb4JavaDevice device, final Usb4JavaPipe pipe)
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
                        irp.setActualLength(bulkWrite(irp.getData(),
                            irp.getOffset(), irp.getLength()));
                        break;

                    case UsbConst.ENDPOINT_DIRECTION_IN:
                        irp.setActualLength(bulkRead(irp.getData(),
                            irp.getOffset(), irp.getLength()));
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
                        irp.setActualLength(interruptWrite(irp.getData(),
                            irp.getOffset(), irp.getLength()));
                        break;

                    case UsbConst.ENDPOINT_DIRECTION_IN:
                        irp.setActualLength(interruptRead(irp.getData(),
                            irp.getOffset(), irp.getLength()));
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
     * Returns the USB endpoint descriptor.
     *
     * @return The USB endpoint descriptor.
     */
    private UsbEndpointDescriptor getEndpointDescriptor()
    {
        return this.pipe.getUsbEndpoint().getUsbEndpointDescriptor();
    }

    /**
     * Reads bytes from a bulk endpoint into the specified data array.
     *
     * @param data
     *            The data array to write the read bytes to.
     * @param offset
     *            The offset in the data array to write the read bytes to.
     * @param len
     *            The number of bytes to read.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of read bytes.
     */
    private int bulkRead(final byte[] data, final int offset, final int len)
        throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int size = Math.min(len, descriptor.wMaxPacketSize()
            & 0xffff);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        int read = 0;
        while (read < len)
        {
            buffer.rewind();
            final int result = usb_bulk_read(this.device.open(),
                descriptor.bEndpointAddress(), buffer, getConfig().getTimeout());
            if (result < 0) throw new Usb4JavaException(
                "Unable to read from interrupt endpoint", result);
            buffer.rewind();
            buffer.get(data, offset + read, result);
            read += result;
        }
        return read;
    }

    /**
     * Writes the specified bytes to a bulk endpoint.
     *
     * @param data
     *            The data array with the bytes to write.
     * @param offset
     *            The offset in the data array to write.
     * @param len
     *            The number of bytes to write.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of written bytes.
     */
    private int bulkWrite(final byte[] data, final int offset, final int len)
        throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int total = len;
        final int size = Math.min(total, descriptor.wMaxPacketSize() & 0xffff);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final USB_Dev_Handle handle = this.device.open();
        int written = 0;
        while (written < total)
        {
            buffer.put(data, offset + written, Math.min(total - written, size));
            buffer.rewind();
            final int result = usb_bulk_write(handle,
                descriptor.bEndpointAddress(), buffer,
                getConfig().getTimeout());
            if (result < 0) throw new Usb4JavaException(
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
     * @param offset
     *            The offset in the data array to write the read bytes to.
     * @param len
     *            The number of bytes to read.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of read bytes.
     */
    private int
        interruptRead(final byte[] data, final int offset, final int len)
            throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int size = Math.min(len, descriptor.wMaxPacketSize()
            & 0xffff);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final int result = usb_interrupt_read(this.device.open(),
            descriptor.bEndpointAddress(), buffer, getConfig().getTimeout());
        if (result < 0) throw new Usb4JavaException(
            "Unable to read from interrupt endpoint", result);
        buffer.rewind();
        buffer.get(data, offset, result);
        return result;
    }

    /**
     * Writes the specified bytes to a interrupt endpoint.
     *
     * @param data
     *            The data array with the bytes to write.
     * @param offset
     *            The offset in the data array to write.
     * @param len
     *            The number of bytes to write.
     * @throws UsbException
     *             When transfer fails.
     * @return The number of written bytes.
     */
    private int interruptWrite(final byte[] data, final int offset,
        final int len)
        throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final int total = len;
        final int size = Math.min(total, descriptor.wMaxPacketSize() & 0xffff);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        int written = 0;
        while (written < total)
        {
            buffer.put(data, offset + written, Math.min(total - written, size));
            buffer.rewind();
            final int result = usb_interrupt_write(this.device.open(),
                descriptor.bEndpointAddress(), buffer,
                getConfig().getTimeout());
            if (result < 0) throw new Usb4JavaException(
                "Unable to write to interrupt endpoint", result);
            written += result;
            buffer.rewind();
        }
        return written;
    }
}
