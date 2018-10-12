/*
 * Copyright 2018 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import java.util.Iterator;

/**
 * List of poll file descriptors as returned by {@link LibUsb#getPollfds(Context)}.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Pollfds implements Iterable<Pollfd>
{
    /** The native pointer to the pollfd array. */
    private long pollfdsPointer;

    /** The number of file descriptors in the list. */
    private int size;

    /**
     * Package-private constructor to prevent manual instantiation. Structures are
     * always created by JNI.
     */
    Pollfds()
    {
        // Empty
    }

    /**
     * Returns the native pointer.
     *
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.pollfdsPointer;
    }

    /**
     * Returns the number of poll file descriptors in the list.
     *
     * @return The number of poll file descriptors in the list.
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Returns the poll file descriptor with the specified index.
     *
     * @param index
     *            The index.
     * @return The poll file descriptor or null when index is out of bounds.
     */
    public native Pollfd get(final int index);

    @Override
    public Iterator<Pollfd> iterator()
    {
        return new PollfdsIterator(this);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + (int) (this.pollfdsPointer ^ (this.pollfdsPointer >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final Pollfds other = (Pollfds) obj;
        return this.pollfdsPointer == other.pollfdsPointer;
    }

    @Override
    public String toString()
    {
        return String.format("libusb pollfd list 0x%x with size %d",
            this.pollfdsPointer, this.size);
    }
}
