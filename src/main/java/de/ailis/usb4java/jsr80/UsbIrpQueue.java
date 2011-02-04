/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.Deque;
import java.util.LinkedList;

import javax.usb.UsbIrp;


/**
 * USB I/O request packet queue.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class UsbIrpQueue
{
    /** The queued packets. */
    private final Deque<UsbIrp> irps = new LinkedList<UsbIrp>();


    /**
     * Adds a request packet to the queue.
     *
     * @param irp
     *            The packet to add to the queue.
     */

    public void add(final UsbIrp irp)
    {
        synchronized (this.irps)
        {
            this.irps.add(irp);
        }
    }


    /**
     * Returns the next request packet to process.
     *
     * @return The next packet to process or null if queue is empty.
     */

    public UsbIrp get()
    {
        synchronized (this.irps)
        {
            return this.irps.poll();
        }
    }


    /**
     * Removes all remaining unprocessed requests.
     */

    public void clear()
    {
        synchronized (this.irps)
        {
            this.irps.clear();
        }
    }


    /**
     * Checks if queue is empty.
     *
     * @return True if queue is empty, false if not.
     */

    public boolean isEmpty()
    {
        return this.irps.isEmpty();
    }
}
