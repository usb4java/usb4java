/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.Deque;
import java.util.LinkedList;

import javax.usb.UsbException;
import javax.usb.UsbIrp;

import de.ailis.usb4java.USBLock;


/**
 * Abstract base class for IRP queues.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @param <T>
 *            The type of IRPs this queue holds.
 */

abstract class AbstractIrpQueue<T extends UsbIrp>
{
    /** The queued packets. */
    private final Deque<T> irps = new LinkedList<T>();

    /** The queue processor thread. */
    private Thread processor;

    /** The USB device. */
    protected final AbstractDevice device;


    /**
     * Constructor.
     *
     * @param device
     *            The USB device.
     */

    public AbstractIrpQueue(final AbstractDevice device)
    {
        this.device = device;
    }


    /**
     * Queues the specified control IRP for processing.
     *
     * @param irp
     *            The control IRP to queue.
     */

    public void add(final T irp)
    {
        synchronized (this.irps)
        {
            this.irps.add(irp);

            // Start the queue processor if not already running.
            if (this.processor == null)
            {
                this.processor = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        process();
                    }
                });
                this.processor.setDaemon(true);
                this.processor.start();
            }
        }
    }


    /**
     * Processes the queue. Methods returns when the queue is empty.
     */

    void process()
    {
        while (true)
        {
            synchronized (this.irps)
            {
                // Get the next IRP
                final T irp = this.irps.poll();

                // Process the IRP
                try
                {
                    USBLock.acquire();
                    try
                    {
                        processIrp(irp);
                    }
                    finally
                    {
                        USBLock.release();
                    }
                }
                catch (final UsbException e)
                {
                    irp.setUsbException(e);
                }
                irp.complete();
                finishIrp(irp);

                // When no more IRPs are present in the queue then terminate
                // the thread.
                if (this.irps.isEmpty())
                {
                    this.processor = null;
                    this.irps.notifyAll();
                    break;
                }
            }
        }
    }


    /**
     * Processes the IRP.
     *
     * @param irp
     *            The IRP to process.
     * @throws UsbException
     *             When processing the IRP fails.
     */

    protected abstract void processIrp(final T irp) throws UsbException;


    /**
     * Called after IRP has finished. This can be implemented to send events for
     * example.
     *
     * @param irp
     *            The IRP which has been finished.
     */

    protected abstract void finishIrp(final UsbIrp irp);


    /**
     * Aborts all queued IRPs. The IRP which is currently processed can't be
     * aborted. This method returns as soon as no more IRPs are in the queue and
     * no more are processed.
     */

    public void abort()
    {
        synchronized (this.irps)
        {
            this.irps.clear();
            while (isBusy())
            {
                try
                {
                    this.irps.wait();
                }
                catch (final InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    /**
     * Checks if queue is busy. A busy queue is a queue which is currently
     * processing IRPs or which still has IRPs in the queue.
     *
     * @return True if queue is busy, false if not.
     */

    public boolean isBusy()
    {
        synchronized (this.irps)
        {
            return this.irps.isEmpty() || this.processor != null;
        }
    }
}
