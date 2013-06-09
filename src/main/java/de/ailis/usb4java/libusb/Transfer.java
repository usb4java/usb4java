/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
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
    private long transferPointer;

    private TransferCallback callback;
    private Object callbackUserData;

    // Keeping a reference to the buffer has multiple benefits: faster get(), GC
    // prevention
    // (while Transfer is alive) and you can check the buffer's original
    // capacity.
    private ByteBuffer buffer;

    // This is needed to check setNumIsoPackets() against the original number of
    // Iso Packets,
    // since memory is only allocated for up to that number and going above is
    // an error.
    private int maxNumIsoPackets = -1;

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
        return transferPointer;
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
    public native byte getFlags();

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
    public native byte getEndpoint();

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
    public native byte getType();

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
    public native int getTimeout();

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
    public void setLength(final int length)
    {
        // Verify that the new length doesn't exceed the current buffer's
        // capacity.
        if (length != 0)
        {
            if (buffer == null)
            {
                throw new IllegalArgumentException(
                    "buffer is null, only a length of 0 is allowed");
            }

            if (buffer.capacity() < length)
            {
                throw new IllegalArgumentException(
                    "buffer too small for requested length");
            }
        }

        // Native call.
        setLengthNative(length);
    }

    native void setLengthNative(final int length);

    /**
     * Returns the actual length of data that was transferred. Read-only, and
     * only for use within transfer callback function. Not valid for isochronous
     * endpoint transfers.
     * 
     * @return The actual length of the transferred data.
     */
    public native int getActualLength();

    public TransferCallback getCallback()
    {
        return callback;
    }

    public void setCallback(final TransferCallback cb)
    {
        // Call native method to enable callback and set Transfer correctly.
        if (cb == null)
        {
            unsetCallbackNative();
        }
        else
        {
            setCallbackNative();
        }

        // Once we know the native calls have gone through, update the
        // reference.
        callback = cb;
    }

    native void setCallbackNative();

    native void unsetCallbackNative();

    void transferCallback()
    {
        if (callback != null)
        {
            callback.processTransfer(this);
        }
    }

    public Object getUserData()
    {
        return callbackUserData;
    }

    public void setUserData(final Object userData)
    {
        callbackUserData = userData;
    }

    /**
     * Returns the data buffer.
     * 
     * @return The data buffer.
     */
    public ByteBuffer getBuffer()
    {
        return buffer;
    }

    /**
     * Sets the data buffer.
     * 
     * @param buffer
     *            The data buffer to set.
     */
    public void setBuffer(final ByteBuffer transferBuffer)
    {
        // Native call.
        setBufferNative(transferBuffer);

        // Once we know the native calls have gone through, update the
        // reference.
        buffer = transferBuffer;
    }

    native void setBufferNative(final ByteBuffer buffer);

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
    public void setNumIsoPackets(final int numIsoPackets)
    {
        if (maxNumIsoPackets == -1)
        {
            // maxNumIsoPackets is not yet set and changing the way we interact
            // with
            // constructors in JNI for this one case just isn't worth it.
            maxNumIsoPackets = getNumIsoPackets();
        }

        // Check that the new number doesn't exceed the maximum.
        if (numIsoPackets > maxNumIsoPackets)
        {
            throw new IllegalArgumentException(
                "numIsoPackets exceeds maximum specified with allocTransfer()");
        }

        // Native call.
        setNumIsoPacketsNative(numIsoPackets);
    }

    native void setNumIsoPacketsNative(final int numIsoPackets);
}
