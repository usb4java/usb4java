/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

/**
 * Thrown when usb4java services could not be created.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ServicesException extends RuntimeException
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The root cause.
     */
    ServicesException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
