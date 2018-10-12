/*
 * Copyright (C) 2018 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator for {@link Pollfds}.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
final class PollfdsIterator implements Iterator<Pollfd>
{
    /** The file descriptor list. */
    private final Pollfds pollfds;

    /** The current index. */
    private int nextIndex;

    /**
     * Constructor.
     *
     * @param pollfds
     *            The file descriptor list list.
     */
    PollfdsIterator(final Pollfds pollfds)
    {
        this.pollfds = pollfds;
    }

    @Override
    public boolean hasNext()
    {
        return this.nextIndex < this.pollfds.getSize();
    }

    @Override
    public Pollfd next()
    {
        if (!hasNext()) throw new NoSuchElementException();
        return this.pollfds.get(this.nextIndex++);
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
