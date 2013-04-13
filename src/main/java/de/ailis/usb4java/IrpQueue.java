/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbIrp;
import javax.usb.UsbShortPacketException;

import de.ailis.usb4java.libusb.DeviceHandle;
import de.ailis.usb4java.libusb.LibUsb;
import de.ailis.usb4java.libusb.LibUsbException;

/**
 * A queue for USB I/O request packets.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class IrpQueue extends AbstractIrpQueue<UsbIrp>
{
    /** The USB pipe. */
    private final Pipe pipe;

    /**
     * Constructor.
     * 
     * @param pipe
     *            The USB pipe
     */
    IrpQueue(final Pipe pipe)
    {
        super(pipe.getDevice());
        this.pipe = pipe;
    }

    @Override
    protected void finishIrp(final UsbIrp irp)
    {
        this.pipe.sendEvent(irp);
    }

    @Override
    protected void processIrp(final UsbIrp irp) throws UsbException
    {
        final UsbEndpoint endpoint = this.pipe.getUsbEndpoint();
        final byte direction = endpoint.getDirection();
        final byte type = endpoint.getType();
        if (type == UsbConst.ENDPOINT_TYPE_CONTROL)
        {
            processControlIrp((UsbControlIrp) irp);
            return;
        }

        switch (direction)
        {
            case UsbConst.ENDPOINT_DIRECTION_OUT:
                irp.setActualLength(write(irp.getData(), irp.getOffset(),
                    irp.getLength()));
                if (irp.getActualLength() < irp.getLength()
                    && !irp.getAcceptShortPacket())
                {
                    throw new UsbShortPacketException();
                }
                break;

            case UsbConst.ENDPOINT_DIRECTION_IN:
                irp.setActualLength(read(irp.getData(), irp.getOffset(),
                    irp.getLength()));
                if (irp.getActualLength() < irp.getLength()
                    && !irp.getAcceptShortPacket())
                {
                    throw new UsbShortPacketException();
                }
                break;

            default:
                throw new UsbException("Invalid direction: "
                    + direction);
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
     * Processes the control IRP.
     * 
     * @param irp
     *            The IRP to process.
     * @throws UsbException
     *             When processing the IRP fails.
     */
    private void processControlIrp(final UsbControlIrp irp) throws UsbException
    {
        final ByteBuffer buffer =
            ByteBuffer.allocateDirect(irp.getLength());
        buffer.put(irp.getData(), irp.getOffset(), irp.getLength());
        buffer.rewind();
        final DeviceHandle handle = getDevice().open();
        final int result = LibUsb.controlTransfer(handle, irp.bmRequestType(),
            irp.bRequest(), irp.wValue(), irp.wIndex(), buffer,
            getConfig().getTimeout());
        if (result < 0)
            throw new LibUsbException(
                "Unable to submit control message", result);
        buffer.rewind();
        buffer.get(irp.getData(), irp.getOffset(), result);
        irp.setActualLength(result);
        if (irp.getActualLength() != irp.getLength()
            && !irp.getAcceptShortPacket())
            throw new UsbShortPacketException();
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
    private int read(final byte[] data, final int offset, final int len)
        throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final byte type = this.pipe.getUsbEndpoint().getType();
        final DeviceHandle handle = getDevice().open();
        int read = 0;
        while (read < len)
        {
            final int size =
                Math.min(len - read, descriptor.wMaxPacketSize() & 0xffff);
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            final IntBuffer transferred = IntBuffer.allocate(1);
            int result;
            if (type == UsbConst.ENDPOINT_TYPE_BULK)
            {
                result = LibUsb.bulkTransfer(handle, 
                    descriptor.bEndpointAddress(), buffer, transferred,
                    getConfig().getTimeout());
                if (result < 0)
                {
                    throw new LibUsbException(
                        "Unable to read from bulk endpoint", result);
                }
            }
            else if (type == UsbConst.ENDPOINT_TYPE_INTERRUPT)
            {
                result = LibUsb.interruptTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred,
                    getConfig().getTimeout());
                if (result < 0)
                {
                    throw new LibUsbException(
                        "Unable to read from interrupt endpoint", result);
                }
            }
            else
            {
                throw new UsbException("Unsupported endpoint type: " + type);
            }
            result = transferred.get(0);
            buffer.rewind();
            buffer.get(data, offset + read, result);
            read += result;

            // Short packet detected, aborting
            if (result < size) break;
        }
        return read;
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
    private int write(final byte[] data, final int offset, final int len)
        throws UsbException
    {
        final UsbEndpointDescriptor descriptor = getEndpointDescriptor();
        final byte type = this.pipe.getUsbEndpoint().getType();
        final DeviceHandle handle = getDevice().open();
        int written = 0;
        while (written < len)
        {
            final int size =
                Math.min(len - written, descriptor.wMaxPacketSize() & 0xffff);
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            buffer.put(data, offset + written, size);
            buffer.rewind();
            final IntBuffer transferred = IntBuffer.allocate(1);
            int result;
            if (type == UsbConst.ENDPOINT_TYPE_BULK)
            {
                result = LibUsb.bulkTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred,
                    getConfig().getTimeout());
                if (result < 0) 
                {
                    throw new LibUsbException(
                        "Unable to write to bulk endpoint", result);
                }
            }
            else if (type == UsbConst.ENDPOINT_TYPE_INTERRUPT)
            {
                result = LibUsb.interruptTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred,
                    getConfig().getTimeout());
                if (result < 0) 
                {
                    throw new LibUsbException(
                        "Unable to write to interrupt endpoint", result);
                }
            }
            else
            {
                throw new UsbException("Unsupported endpoint type: " + type);
            }
            result = transferred.get(0);
            written += result;

            // Short packet detected, aborting
            if (result < size) break;
        }
        return written;
    }
}
