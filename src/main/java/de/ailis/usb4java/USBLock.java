/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.util.concurrent.locks.ReentrantLock;


/**
 * Global locking mechanism for the USB sub system.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USBLock
{
    /** Synchronization mutex. */
    private static final ReentrantLock lock = new ReentrantLock();


    /**
     * Private constructor to prevent instantiation.
     */

    private USBLock()
    {
        // Empty
    }


    /**
     * Tries to acquire a lock on the USB sub system. Returns as soon as the
     * current thread has acquired the lock. If the USB sub system is already
     * locked by the same thread then this method returns immediately and
     * increments the lock counter.
     */

    public static void acquire()
    {
        lock.lock();
    }


    /**
     * Releases an acquired lock. This must be called every time an acquired
     * lock is no longer needed. Use a try/finally block to ensure this.
     */

    public static void release()
    {
        lock.unlock();
    }
}
