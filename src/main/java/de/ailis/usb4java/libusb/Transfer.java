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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    // Keeping a reference to the buffer has multiple benefits: faster get(), GC
    // prevention (while Transfer is alive) and you can check the buffer's
    // original capacity (needed to check setLength() properly).
    private ByteBuffer transferBuffer;

    /**
     * Package-private constructor to prevent manual instantiation.
     * Transfers are always created by JNI with allocTransfer().
     */
    Transfer()
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
    public native DeviceHandle devHandle();

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
    public native byte flags();

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
    public native byte endpoint();

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
    public native byte type();

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
    public native int timeout();

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
    public native int status();

    /**
     * Returns the length of the data buffer.
     * 
     * @return The data buffer length.
     */
    public native int length();

    /**
     * Sets the length of the data buffer.
     * 
     * This is checked against the maximum capacity of the supplied ByteBuffer.
     * 
     * @param length
     *            The data buffer length to set.
     */
    public void setLength(final int length)
    {
        // Verify that the new length doesn't exceed the current buffer's
        // maximum capacity.
        if (length != 0)
        {
            if (transferBuffer == null)
            {
                throw new IllegalArgumentException(
                    "buffer is null, only a length of 0 is allowed");
            }

            if (transferBuffer.capacity() < length)
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
    public native int actualLength();


    /**
     * Returns the current callback object.
     * 
     * @return The current callback object.
     */
    public native TransferCallback callback();

    /**
     * Sets the callback object.
     * 
     * This will be invoked when the transfer completes, fails, or is cancelled.
     * 
     * @param callback
     *            The callback object to use.
     */
    public native void setCallback(final TransferCallback callback);

    /**
     * Returns the current user data object.
     * 
     * @return The current user data object.
     */
    public native Object userData();

    /**
     * Sets the user data object, representing user context data to pass to
     * the callback function and that can be accessed from there.
     * 
     * @param userData
     *            The user data object to set.
     */
    public native void setUserData(final Object userData);

    /**
     * Returns the data buffer.
     * 
     * @return The data buffer.
     */
    public ByteBuffer buffer()
    {
        return transferBuffer;
    }

    /**
     * Sets the data buffer.
     * 
     * @param buffer
     *            The data buffer to set.
     */
    public void setBuffer(final ByteBuffer buffer)
    {
        // Native call.
        setBufferNative(buffer);

        if (buffer != null)
        {
            // Set new length based on buffer's capacity.
            setLengthNative(buffer.capacity());
        }
        else
        {
            setLengthNative(0);
        }

        // Once we know the native calls have gone through, update the
        // reference.
        transferBuffer = buffer;
    }

    native void setBufferNative(final ByteBuffer buffer);

    /**
     * Returns the number of isochronous packets. Only used for I/O with
     * isochronous endpoints.
     * 
     * @return The number of isochronous packets.
     */
    public native int numIsoPackets();

    /**
     * Sets the number of isochronous packets.
     * 
     * @param numIsoPackets
     *            The number of isochronous packets to set.
     */
    public native void setNumIsoPackets(final int numIsoPackets);

    /**
     * Array of isochronous packet descriptors, for isochronous transfers only.
     * 
     * @return The array of isochronous packet descriptors.
     */
    public native IsoPacketDescriptor[] isoPacketDesc();

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(devHandle())
        .append(flags())
        .append(endpoint())
        .append(type())
        .append(timeout())
        .append(status())
        .append(length())
        .append(actualLength())
        .append(callback())
        .append(userData())
        .append(buffer())
        .append(numIsoPackets())
        .append(isoPacketDesc())
        .toHashCode();
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
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final Transfer other = (Transfer) obj;

        return new EqualsBuilder()
        .append(devHandle(), other.devHandle())
        .append(flags(), other.flags())
        .append(endpoint(), other.endpoint())
        .append(type(), other.type())
        .append(timeout(), other.timeout())
        .append(status(), other.status())
        .append(length(), other.length())
        .append(actualLength(), other.actualLength())
        .append(callback(), other.callback())
        .append(userData(), other.userData())
        .append(buffer(), other.buffer())
        .append(numIsoPackets(), other.numIsoPackets())
        .append(isoPacketDesc(), other.isoPacketDesc())
        .isEquals();
    }
}
