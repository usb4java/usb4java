/*
 * Copyright 2018 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java;

/**
 * File descriptor for polling.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Pollfd
{
    /** There is data to read */
    public static final int POLLIN = 1;

    /** Writing now will not block. */
    public static final int POLLOUT = 4;

    /** The native pointer to the pollfd structure. */
    private long pollfdPointer;

    /**
     * Package-private constructor to prevent manual instantiation. Structures are
     * always created by JNI.
     */
    Pollfd()
    {
        // Empty
    }

    /**
     * Returns the native pointer to the pollfd structure.
     *
     * @return The native pointer to the pollfd structure.
     */
    public long getPointer()
    {
        return this.pollfdPointer;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + (int) (this.pollfdPointer ^ (this.pollfdPointer >>> 32));
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
        final Pollfd other = (Pollfd) obj;
        if (this.pollfdPointer != other.pollfdPointer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb pollfd 0x%x", this.pollfdPointer);
    }

    /**
     * Returns the numeric file descriptor.
     *
     * @return The numeric file descriptor.
     */
    public native byte fd();

    /**
     * Returns the event flags to poll.
     *
     * {@link #POLLIN} indicates that you should monitor this file descriptor for becoming ready to read from, and
     * {@link #POLLOUT} indicates that you should monitor this file descriptor for non-blocking write readiness.
     *
     * @return The event flags to poll.
     */
    public native short events();
}
