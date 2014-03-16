/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
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
 * Structure representing a libusb session. The concept of individual libusb
 * sessions allows for your program to use two libraries (or dynamically load
 * two modules) which both independently use libusb. This will prevent
 * interference between the individual libusb users - for example
 * {@link LibUsb#setDebug(Context, int)} will not affect the other user of the
 * library, and {@link LibUsb#exit(Context)} will not destroy resources that the
 * other user is still using.
 *
 * Sessions are created by {@link LibUsb#init(Context)} and destroyed through
 * {@link LibUsb#exit(Context)}. If your application is guaranteed to only ever
 * include a single libusb user (i.e. you), you do not have to worry about
 * contexts: pass NULL in every function call where a context is required. The
 * default context will be used.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Context
{
    /** The native pointer to the context structure. */
    private long contextPointer;

    /**
     * Constructs a new libusb context. Must be passed to
     * {@link LibUsb#init(Context)} before passing it to any other method.
     */
    public Context()
    {
        // Empty
    }

    /**
     * Returns the native pointer to the context structure.
     *
     * @return The native pointer to the context structure.
     */
    public long getPointer()
    {
        return this.contextPointer;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + (int) (this.contextPointer ^ (this.contextPointer >>> 32));
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
        final Context other = (Context) obj;
        if (this.contextPointer != other.contextPointer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb context 0x%x", this.contextPointer);
    }
}
