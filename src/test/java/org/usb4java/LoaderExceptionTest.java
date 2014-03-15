/*
 * Copyright (C) 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link LoaderException} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class LoaderExceptionTest
{
    /**
     * Tests the constructor with only an error message.
     */
    @Test
    public void testConstructorWithMessage()
    {
        final LoaderException e = new LoaderException("Custom message");
        assertEquals("Custom message", e.getMessage());
    }

    /**
     * Tests the constructor with an error message and a cause.
     */
    @Test
    public void testConstructorWithMessageAndCause()
    {
        final RuntimeException cause = new RuntimeException();
        final LoaderException e = new LoaderException("Custom message", cause);
        assertEquals("Custom message", e.getMessage());
        assertEquals(cause, e.getCause());
    }
}
