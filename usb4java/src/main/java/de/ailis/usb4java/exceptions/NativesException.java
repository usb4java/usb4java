/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.exceptions;

/**
 * Exception thrown when something goes wrong while loading native libraries.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class NativesException extends RuntimeException
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message
     *            The error message.
     */
    public NativesException(final String message)
    {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The root cause.
     */
    public NativesException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
