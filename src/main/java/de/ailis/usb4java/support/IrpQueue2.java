/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbIrp;
import javax.usb.UsbShortPacketException;

import de.ailis.usb4java.exceptions.Usb4JavaException;
import de.ailis.usb4java.libusb.DeviceHandle;
import de.ailis.usb4java.libusb.LibUSB;
import de.ailis.usb4java.topology.Usb4JavaPipe;

/**
 * A queue for USB I/O request packets.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class IrpQueue2 extends AbstractIrpQueue2<UsbIrp>
{
    /** The USB pipe. */
    private final Usb4JavaPipe pipe;

    /**
     * Constructor.
     * 
     * @param pipe
     *            The USB pipe
     */
    public IrpQueue2(final Usb4JavaPipe pipe)
    {
        super(pipe.getDevice());
        this.pipe = pipe;
    }

    /**
     * @see AbstractIrpQueue2#finishIrp(UsbIrp)
     */
    @Override
    protected void finishIrp(final UsbIrp irp)
    {
        this.pipe.sendEvent(irp);
    }

    /**
     * @see AbstractIrpQueue2#processIrp(javax.usb.UsbIrp)
     */
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
                irp.setActualLength(write(irp.getData(),
                    irp.getOffset(), irp.getLength()));
                if (irp.getActualLength() < irp.getLength() && !irp.getAcceptShortPacket())
                    throw new UsbShortPacketException();
                break;

            case UsbConst.ENDPOINT_DIRECTION_IN:
                irp.setActualLength(read(irp.getData(),
                    irp.getOffset(), irp.getLength()));
                if (irp.getActualLength() < irp.getLength() && !irp.getAcceptShortPacket())
                    throw new UsbShortPacketException();
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
        final DeviceHandle handle = this.device.open();
        final int len = LibUSB.controlTransfer(handle, irp.bmRequestType(),
                irp.bRequest(), irp.wValue(), irp.wIndex(), buffer,
                getConfig().getTimeout());
        if (len < 0)
            throw new Usb4JavaException(
                "Unable to submit control message", len);
        buffer.rewind();
        buffer.get(irp.getData(), irp.getOffset(), len);
        irp.setActualLength(len);
        if (irp.getActualLength() != irp.getLength() && !irp.getAcceptShortPacket())
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
        final DeviceHandle handle = this.device.open();
        int read = 0;
        while (read < len)
        {
            final int size = Math.min(len - read, descriptor.wMaxPacketSize() & 0xffff);
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            IntBuffer transferred = IntBuffer.allocate(1);
            int result;
            if (type == UsbConst.ENDPOINT_TYPE_BULK)
            {
                result = LibUSB.bulkTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred, getConfig().getTimeout());
                if (result < 0) throw new Usb4JavaException(
                    "Unable to read from bulk endpoint", result);
            }
            else if (type == UsbConst.ENDPOINT_TYPE_INTERRUPT)
            {
                result = LibUSB.interruptTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred, getConfig().getTimeout());
                if (result < 0) throw new Usb4JavaException(
                    "Unable to read from interrupt endpoint", result);
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
        final DeviceHandle handle = this.device.open();
        int written = 0;
        while (written < len)
        {
            final int size = Math.min(len - written, descriptor.wMaxPacketSize() & 0xffff);
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            buffer.put(data, offset + written, size);
            buffer.rewind();
            IntBuffer transferred = IntBuffer.allocate(1);
            int result;
            if (type == UsbConst.ENDPOINT_TYPE_BULK)
            {
                result = LibUSB.bulkTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred,
                    getConfig().getTimeout());
                if (result < 0) throw new Usb4JavaException(
                    "Unable to write to bulk endpoint", result);
            }
            else if (type == UsbConst.ENDPOINT_TYPE_INTERRUPT)
            {
                result = LibUSB.interruptTransfer(handle,
                    descriptor.bEndpointAddress(), buffer, transferred,
                    getConfig().getTimeout());
                if (result < 0) throw new Usb4JavaException(
                    "Unable to write to interrupt endpoint", result);
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
