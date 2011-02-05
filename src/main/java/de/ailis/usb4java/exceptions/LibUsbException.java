/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.exceptions;

import static de.ailis.usb4java.jni.USB.usb_strerror;

import javax.usb.UsbException;


/**
 * libusb-specific UsbException.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class LibUsbException extends UsbException
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

    public LibUsbException(final String message, final int errorCode)
    {
        super(String.format("USB error %d: %s: %s", -errorCode, message,
            usb_strerror()));
        this.errorCode = errorCode;
    }


    /**
     * Constructor.
     *
     * @param message
     *            The error message.
     */

    public LibUsbException(final String message)
    {
        super(String.format("USB error: %s: %s", message, usb_strerror()));
        this.errorCode = 0;
    }


    /**
     * Constructor.
     *
     * @param errorCode
     *            The error code.
     */

    public LibUsbException(final int errorCode)
    {
        super(String.format("USB error %d: %s", -errorCode, usb_strerror()));
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
