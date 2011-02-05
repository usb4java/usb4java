/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import static de.ailis.usb4java.USB.usb_strerror;

import javax.usb.UsbException;


/**
 * libusb-specific UsbException.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class LibUsbException extends UsbException
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The error code. */
    private final int errorCode;


    /**
     * Constructor.
     *
     * @param errorCode
     *            The error code.
     */

    public LibUsbException(final int errorCode)
    {
        super(String.format("USB error %d: %s", errorCode, usb_strerror()));
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
