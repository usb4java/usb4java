/*
 * Copyright (C) 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link LibUsbException} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUsbExceptionTest
{
    /**
     * Tests the constructor with only an error code.
     */
    @Test
    public void testConstructorWithCode()
    {
        final LibUsbException e =
            new LibUsbException(LibUsb.ERROR_INVALID_PARAM);
        assertEquals(LibUsb.ERROR_INVALID_PARAM, e.getErrorCode());
        assertEquals("USB error 2: Invalid parameter", e.getMessage());

    }

    /**
     * Tests the constructor with an error code and a message.
     */
    @Test
    public void testConstructorWithCodeAndMessage()
    {
        final LibUsbException e =
            new LibUsbException("Custom message", LibUsb.ERROR_INVALID_PARAM);
        assertEquals(LibUsb.ERROR_INVALID_PARAM, e.getErrorCode());
        assertEquals("USB error 2: Custom message: Invalid parameter", 
            e.getMessage());

    }
}
