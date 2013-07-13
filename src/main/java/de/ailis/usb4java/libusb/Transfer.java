/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusb <http://www.libusb.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2011 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2012 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2012 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2012 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package de.ailis.usb4java.libusb;

import java.nio.ByteBuffer;

/**
 * The generic USB transfer structure.
 * 
 * The user populates this structure and then submits it in order to request a
 * transfer. After the transfer has completed, the library populates the
 * transfer with the results and passes it back to the user.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Transfer
{
    /** The native pointer to the transfer structure. */
    private long pointer;

    /**
     * Constructs a new transfer structure.
     */
    public Transfer()
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
        return this.pointer;
    }

    /**
     * Returns the handle of the device that this transfer will be submitted to.
     * 
     * @return The handle of the device.
     */
    public native DeviceHandle getDevHandle();

    /**
     * Sets the handle of the device that this transfer will be submitted to.
     * 
     * @param handle
     *            The handle of the device.
     */
    public native void setDevHandle(final DeviceHandle handle);

    /**
     * Returns the bitwise OR combination of libusb transfer flags.
     * 
     * @return The transfer flags.
     */
    public native int getFlags();

    /**
     * Sets the bitwise OR combination of libusb transfer flags.
     * 
     * @param flags
     *            The transfer flags to set.
     */
    public native void setFlags(final int flags);

    /**
     * Returns the address of the endpoint where this transfer will be sent.
     * 
     * @return The endpoint address.
     */
    public native int getEndpoint();

    /**
     * Sets the address of the endpoint where this transfer will be sent.
     * 
     * @param endpoint
     *            The endpoint address to set
     */
    public native void setEndpoint(final int endpoint);

    /**
     * Returns the type of the endpoint.
     * 
     * @return The endpoint type.
     */
    public native int getType();

    /**
     * Sets the type of the endpoint.
     * 
     * @param type
     *            The endpoint type to set.
     */
    public native void setType(final int type);

    /**
     * Returns the timeout for this transfer in milliseconds. A value of 0
     * indicates no timeout.
     * 
     * @return The timeout.
     */
    public native long getTimeout();

    /**
     * Sets the timeout for this transfer in milliseconds. A value of 0
     * indicates no timeout.
     * 
     * @param timeout
     *            The timeout to set.
     */
    public native void setTimeout(final int timeout);

    /**
     * Returns the status of the transfer. Read-only, and only for use within
     * transfer callback function.
     * 
     * If this is an isochronous transfer, this field may read
     * {@link LibUsb#TRANSFER_COMPLETED} even if there were errors in the
     * frames. Use the status field in each packet to determine if errors
     * occurred.
     * 
     * @return The transfer status.
     */
    public native int getStatus();

    /**
     * Returns the length of the data buffer.
     * 
     * @return The data buffer length.
     */
    public native int getLength();

    /**
     * Sets the length of the data buffer.
     * 
     * @param length
     *            The data buffer length to set.
     */
    public native void setLength(final int length);

    /**
     * Returns the actual length of data that was transferred. Read-only, and
     * only for use within transfer callback function. Not valid for isochronous
     * endpoint transfers.
     * 
     * @return The actual length of the transferred data.
     */
    public native int getActualLength();

    /**
     * Returns the data buffer.
     * 
     * @return The data buffer.
     */
    public native ByteBuffer getBuffer();

    /**
     * Sets the data buffer.
     * 
     * @param buffer
     *            The data buffer to set.
     */
    public native void setBuffer(final ByteBuffer buffer);

    /**
     * Returns the number of isochronous packets. Only used for I/O with
     * isochronous endpoints.
     * 
     * @return The number of isochronous packets.
     */
    public native int getNumIsoPackets();

    /**
     * Sets the number of isochronous packets.
     * 
     * @param numIsoPackets
     *            The number of isochronous packets to set.
     */
    public native void setNumIsoPackets(final int numIsoPackets);
}
