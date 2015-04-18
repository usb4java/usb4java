/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
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

    /**
     * Keeping a reference to the buffer has multiple benefits: faster get(), GC
     * prevention (while Transfer is alive) and you can check the buffer's
     * original capacity (needed to check setLength() properly).
     */
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
        return this.transferPointer;
    }

    /**
     * Returns the handle of the device that this transfer will be submitted to.
     *
     * @return The handle of the device.
     */
    public DeviceHandle devHandle() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the handle of the device that this transfer will be submitted to.
     *
     * @param handle
     *            The handle of the device.
     */
    public void setDevHandle(final DeviceHandle handle) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the bitwise OR combination of libusb transfer flags.
     *
     * @return The transfer flags.
     */
    public byte flags() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the bitwise OR combination of libusb transfer flags.
     *
     * @param flags
     *            The transfer flags to set.
     */
    public void setFlags(final byte flags) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the address of the endpoint where this transfer will be sent.
     *
     * @return The endpoint address.
     */
    public byte endpoint() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the address of the endpoint where this transfer will be sent.
     *
     * @param endpoint
     *            The endpoint address to set
     */
    public void setEndpoint(final byte endpoint) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the type of the endpoint.
     *
     * @return The endpoint type.
     */
    public byte type() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the type of the endpoint.
     *
     * @param type
     *            The endpoint type to set.
     */
    public void setType(final byte type) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the timeout for this transfer in milliseconds. A value of 0
     * indicates no timeout.
     *
     * @return The timeout.
     */
    public long timeout() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the timeout for this transfer in milliseconds. A value of 0
     * indicates no timeout.
     *
     * @param timeout
     *            The timeout to set.
     */
    public void setTimeout(final long timeout) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

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
    public int status() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the length of the data buffer.
     *
     * @return The data buffer length.
     */
    public int length() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

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
            if (this.transferBuffer == null)
            {
                throw new IllegalArgumentException(
                    "buffer is null, only a length of 0 is allowed");
            }

            if (this.transferBuffer.capacity() < length)
            {
                throw new IllegalArgumentException(
                    "buffer too small for requested length");
            }
        }

        // Native call.
        this.setLengthNative(length);
    }

    /**
     * Native method called internally to set the length of the data buffer.
     *
     * @param length
     *            The length to set.
     */
    void setLengthNative(final int length) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the actual length of data that was transferred. Read-only, and
     * only for use within transfer callback function. Not valid for isochronous
     * endpoint transfers.
     *
     * @return The actual length of the transferred data.
     */
    public int actualLength() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the current callback object.
     *
     * @return The current callback object.
     */
    public TransferCallback callback() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the callback object.
     *
     * This will be invoked when the transfer completes, fails, or is cancelled.
     *
     * @param callback
     *            The callback object to use.
     */
    public void setCallback(final TransferCallback callback) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the current user data object.
     *
     * @return The current user data object.
     */
    public Object userData() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the user data object, representing user context data to pass to
     * the callback function and that can be accessed from there.
     *
     * @param userData
     *            The user data object to set.
     */
    public void setUserData(final Object userData) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the data buffer.
     *
     * @return The data buffer.
     */
    public ByteBuffer buffer()
    {
        return this.transferBuffer;
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
        this.setBufferNative(buffer);

        if (buffer != null)
        {
            // Set new length based on buffer's capacity.
            this.setLengthNative(buffer.capacity());
        }
        else
        {
            this.setLengthNative(0);
        }

        // Once we know the calls have gone through, update the
        // reference.
        this.transferBuffer = buffer;
    }

    /**
     * Native method called internally to set the data buffer.
     *
     * @param buffer
     *            The data buffer to set.
     */
    void setBufferNative(final ByteBuffer buffer) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Returns the number of isochronous packets. Only used for I/O with
     * isochronous endpoints.
     *
     * @return The number of isochronous packets.
     */
    public int numIsoPackets() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Sets the number of isochronous packets.
     *
     * @param numIsoPackets
     *            The number of isochronous packets to set.
     */
    public void setNumIsoPackets(final int numIsoPackets) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Array of isochronous packet descriptors, for isochronous transfers only.
     *
     * @return The array of isochronous packet descriptors.
     */
    public IsoPacketDescriptor[] isoPacketDesc() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Get a transfer's bulk stream id.
     *
     * @return The stream id for the transfer.
     */
    public int streamId() {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Set a transfer's bulk stream id.
     *
     * Note: users are advised to use LibUsb.fillBulkStreamTransfer() instead
     * of calling this function directly.
     *
     * @param streamId
     *            The stream id to set.
     */
    public void setStreamId(final int streamId) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + (int) (this.transferPointer ^ (this.transferPointer >>> 32));
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
        final Transfer other = (Transfer) obj;
        if (this.transferPointer != other.transferPointer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb transfer 0x%x", this.transferPointer);
    }
}
