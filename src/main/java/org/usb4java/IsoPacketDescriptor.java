/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
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
 * Isochronous packet descriptor.
 * 
 * @author Luca Longinotti (l@longi.li)
 */
public final class IsoPacketDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long isoPacketDescriptorPointer;

    /**
     * Package-private constructor to prevent manual instantiation.
     * IsoPacketDescriptors are always created by JNI.
     */
    IsoPacketDescriptor()
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
        return this.isoPacketDescriptorPointer;
    }

    /**
     * Returns the length of data to request in this packet.
     * 
     * @return The length of data to request in this packet.
     */
    public native int length();

    /**
     * Sets the length of data to request in this packet.
     * 
     * @param length
     *            The length to set.
     */
    // Theoretically the right representation for a C unsigned int would be a
    // Java long, but the maximum length for ISO Packets is 1024 bytes, so an
    // int more than suffices to hold any possible valid values here.
    public native void setLength(final int length);

    /**
     * Returns the amount of data that was actually transferred.
     * 
     * @return The amount of data that was actually transferred.
     */
    public native int actualLength();

    /**
     * Returns the status code for this packet.
     * 
     * @return The status code for this packet.
     */
    public native int status();

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (this.isoPacketDescriptorPointer ^ 
            (this.isoPacketDescriptorPointer >>> 32));
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
        final IsoPacketDescriptor other = (IsoPacketDescriptor) obj;
        if (this.isoPacketDescriptorPointer != other.isoPacketDescriptorPointer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb iso packet descriptor 0x%x",
            this.isoPacketDescriptorPointer);
    }
}
