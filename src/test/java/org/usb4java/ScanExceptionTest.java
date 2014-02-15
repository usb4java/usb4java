/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link ScanException} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class ScanExceptionTest
{
    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor()
    {
        final Throwable cause = new RuntimeException("");
        final String message = "Bang";
        final ScanException exception = new ScanException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
