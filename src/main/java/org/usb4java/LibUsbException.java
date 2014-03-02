/*
 * Copyright 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

/**
 * A runtime exception which automatically outputs the libusb error string.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class LibUsbException extends RuntimeException
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The libusb error code. */
    private final int errorCode;
    
    /**
     * Constructs a libusb exception which just outputs the error code and
     * the error message from libusb.
     *
     * @param errorCode
     *            The error code.
     */
    public LibUsbException(final int errorCode)
    {
        super(String.format("USB error %d: %s", -errorCode,
            LibUsb.strError(errorCode)));
        this.errorCode = errorCode;
    }

    /**
     * Constructs a libusb exception which outputs the error code and
     * the error message from libusb together with a custom error message.
     *
     * @param message
     *            The error message.
     * @param errorCode
     *            The error code.
     */
    public LibUsbException(final String message, final int errorCode)
    {
        super(String.format("USB error %d: %s: %s", -errorCode, message,
            LibUsb.strError(errorCode)));
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code.
     *
     * @return The error code
     */
    public int getErrorCode()
    {
        return this.errorCode;
    }    
}
