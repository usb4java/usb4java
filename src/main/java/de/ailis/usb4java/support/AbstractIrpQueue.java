/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbIrp;

import de.ailis.usb4java.Services;
import de.ailis.usb4java.exceptions.Usb4JavaRuntimeException;
import de.ailis.usb4java.topology.Usb4JavaDevice;

/**
 * Abstract base class for IRP queues.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @param <T>
 *            The type of IRPs this queue holds.
 */
public abstract class AbstractIrpQueue<T extends UsbIrp>
{
    /** The queued packets. */
    private final Queue<T> irps = new ConcurrentLinkedQueue<T>();

    /** The queue processor thread. */
    private Thread processor;

    /** The USB device. */
    private final Usb4JavaDevice device;

    /**
     * Constructor.
     * 
     * @param device
     *            The USB device. Must not be null.
     */
    public AbstractIrpQueue(final Usb4JavaDevice device)
    {
        if (device == null)
            throw new IllegalArgumentException("device must be set");
        this.device = device;
    }

    /**
     * Queues the specified control IRP for processing.
     * 
     * @param irp
     *            The control IRP to queue.
     */
    public final void add(final T irp)
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

    /**
     * Processes the queue. Methods returns when the queue is empty.
     */
    final void process()
    {
        // Get the next IRP
        T irp = this.irps.poll();
        while (irp != null)
        {
            // Process the IRP
            try
            {
                UsbLock.acquire();
                try
                {
                    processIrp(irp);
                }
                finally
                {
                    UsbLock.release();
                }
            }
            catch (final UsbException e)
            {
                irp.setUsbException(e);
            }

            // Get next IRP and mark the thread as closing before sending the
            // events for the previous IRP
            T nextIrp = this.irps.poll();
            if (nextIrp == null) this.processor = null;

            // Finish the previous IRP
            irp.complete();
            finishIrp(irp);

            // Process next IRP (if present)
            irp = nextIrp;
        }

        // No more IRPs are present in the queue so terminate the thread.
        this.processor = null;
        synchronized (this.irps)
        {
            this.irps.notifyAll();
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
    public final void abort()
    {
        this.irps.clear();
        while (isBusy())
        {
            try
            {
                synchronized (this.irps)
                {
                    this.irps.wait();
                }
            }
            catch (final InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Checks if queue is busy. A busy queue is a queue which is currently
     * processing IRPs or which still has IRPs in the queue.
     * 
     * @return True if queue is busy, false if not.
     */
    public final boolean isBusy()
    {
        return !this.irps.isEmpty() || this.processor != null;
    }

    /**
     * Returns the configuration.
     * 
     * @return The configuration.
     */
    protected Config getConfig()
    {
        try
        {
            return ((Services) UsbHostManager.getUsbServices()).getConfig();
        }
        catch (final UsbException e)
        {
            // Can't happen because we can't get to this point when USB
            // services are not available.
            throw new Usb4JavaRuntimeException(e.toString(), e);
        }
    }

    /**
     * Returns the USB device.
     * 
     * @return The USB device. Never null.
     */
    protected Usb4JavaDevice getDevice()
    {
        return this.device;
    }
}
