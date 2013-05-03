/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link ServicesException} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class ServicesExceptionTest
{
    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor()
    {
        final Throwable cause = new RuntimeException("");
        final String message = "Bang";
        final ServicesException exception =
            new ServicesException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
