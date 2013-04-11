/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.exceptions;

import de.ailis.usb4java.libusb.LibUSB;

/**
 * libusb-specific USB runtime exception.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaRuntimeException extends RuntimeException
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The error code. */
    private final int errorCode;

    /**
     * Constructor.
     *
     * @param message
     *            The error message.
     * @param errorCode
     *            The error code.
     */
    public Usb4JavaRuntimeException(final String message, final int errorCode)
    {
        super(String.format("USB error %d: %s: %s", -errorCode, message,
            LibUSB.errorName(errorCode)));
        this.errorCode = errorCode;
    }

    /**
     * Constructor.
     *
     * @param message
     *            The error message.
     */
    public Usb4JavaRuntimeException(final String message)
    {
        super(String.format("USB error: %s", message));
        this.errorCode = 0;
    }

    /**
     * Constructor.
     *
     * @param errorCode
     *            The error code.
     */
    public Usb4JavaRuntimeException(final int errorCode)
    {
        super(String.format("USB error %d: %s", -errorCode, 
            LibUSB.errorName(errorCode)));
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
