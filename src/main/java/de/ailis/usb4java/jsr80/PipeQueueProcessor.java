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
 * This thread is responsible for processing the request packet queue of a pipe.
 * The thread is created and started when a pipe is opened and is stopped when
 * the pipe is closed.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class PipeQueueProcessor extends Thread
{
    /** The pipe. */
    private final UsbPipeImpl pipe;

    /** If thread should stop. */
    private boolean stop;

    /** If thread is running or not. */
    private boolean running;

    /** If packets are currently processed. */
    private boolean processing;


    /**
     * Constructor.
     *
     * @param pipe
     *            The pipe.
     */

    public PipeQueueProcessor(final UsbPipeImpl pipe)
    {
        this.pipe = pipe;
        setDaemon(true);
    }


    /**
     * Stops the thread.
     */

    public void shutdown()
    {
        synchronized (this)
        {
            this.stop = true;
            notifyAll();
        }
    }


    /**
     * Stops the thread and waits until it has really stopped.
     */

    public void shutdownAndWait()
    {
        shutdown();
        synchronized (this)
        {
            while (isRunning())
            {
                try
                {
                    wait();
                }
                catch (final InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
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
     * Returns the USB device.
     *
     * @return The USB device.
     */

    private AbstractDevice getDevice()
    {
        return this.pipe.getDevice();
    }


    /**
     * Processes the specified request packet.
     *
     * @param irp
     *            The request packet to process.
     */

    private void processIrp(final UsbIrp irp)
    {
        final UsbEndpoint endpoint = this.pipe.getUsbEndpoint();
        final byte type = endpoint.getType();
        final byte direction = endpoint.getDirection();
        try
        {
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
        catch (final UsbException e)
        {
            irp.setUsbException(e);
        }
        irp.complete();
        this.pipe.sendEvent(irp);
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
        final int result = usb_bulk_read(getDevice().open(), ep, buffer, 5000);
        if (result < 0)
            throw new LibUsbException("Unable to read from interrupt endpoint",
                result);
        buffer.rewind();
        buffer.get(data, 0, result);
        return result;
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
        final USB_Dev_Handle handle = getDevice().open();
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
                usb_interrupt_read(getDevice().open(), ep, buffer, 5000);
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
        final USB_Dev_Handle handle = getDevice().open();
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


    /**
     * @see java.lang.Runnable#run()
     */

    @Override
    public void run()
    {
        synchronized (this)
        {
            this.running = true;
            final UsbIrpQueue queue = this.pipe.getQueue();
            while (!this.stop)
            {
                UsbIrp irp = queue.get();
                while (!this.stop && irp != null)
                {
                    this.processing = true;
                    processIrp(irp);
                    irp = queue.get();
                }
                this.processing = false;
                try
                {
                    wait();
                }
                catch (final InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
            this.running = false;
            notifyAll();
        }
    }


    /**
     * Checks if this thread is running.
     *
     * @return True if thread is running, false if not.
     */

    public boolean isRunning()
    {
        return this.running;
    }


    /**
     * Checks if packets are currently processed.
     *
     * @return If packets are processed.
     */

    public boolean isProcessing()
    {
        return this.processing;
    }
}
