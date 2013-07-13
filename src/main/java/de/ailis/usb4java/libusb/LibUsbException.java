/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import javax.usb.UsbException;

/**
 * libusb-specific USB exception.
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
