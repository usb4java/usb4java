/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusbx;


/**
 * Most libusbx functions return 0 on success or one of these codes on failure.
 * You can call libusb_error_name() to retrieve a string representation of an
 * error code.
 */
public enum Error
{
    /** Success (no error). */
    SUCCESS(0),

    /** Input/output error. */
    ERROR_IO(-1),

    /** Invalid parameter. */
    ERROR_INVALID_PARAM(-2),

    /** Access denied (insufficient permissions). */
    ERROR_ACCESS(-3),

    /** No such device (it may have been disconnected). */
    ERROR_NO_DEVICE(-4),

    /** Entity not found. */
    ERROR_NOT_FOUND(-5),

    /** Resource busy. */
    ERROR_BUSY(-6),

    /** Operation timed out. */
    ERROR_TIMEOUT(-7),

    /** Overflow. */
    ERROR_OVERFLOW(-8),

    /** Pipe error. */
    ERROR_PIPE(-9),

    /** System call interrupted (perhaps due to signal) */
    ERROR_INTERRUPTED(-10),

    /** Insufficient memory. */
    ERROR_NO_MEM(-11),

    /** Operation not supported or unimplemented on this platform. */
    ERROR_NOT_SUPPORTED(-12),

    /** Other error. */
    ERROR_OTHER(-99);

    /** The internal error code. */
    private int code;

    /**
     * Constructor.
     * 
     * @param code
     *            The internal error code.
     */
    private Error(final int code)
    {
        this.code = code;
    }

    /**
     * Returns the internal error code.
     * 
     * @return The internal error code.
     */
    public int getCode()
    {
        return this.code;
    }

    /**
     * Returns the error enum for the specified error code.
     * 
     * @param code
     *            The internal error code.
     * @return The error enum.
     */
    public static Error valueOf(final int code)
    {
        for (Error error: values())
            if (error.getCode() == code) return error;
        throw new IllegalArgumentException("Invalid error code: " + code);
    }
}
