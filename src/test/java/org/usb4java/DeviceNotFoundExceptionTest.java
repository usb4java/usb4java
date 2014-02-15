/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.usb4java.descriptors.SimpleUsbDeviceDescriptor;

/**
 * Tests the {@link DeviceNotFoundException} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceNotFoundExceptionTest
{
    /**
     * Tests the {@link DeviceNotFoundException#getId() method}.
     */
    @Test
    public void testGetId()
    {
        final byte d = 0;
        final DeviceId id = new DeviceId(0, 1, 2, 
            new SimpleUsbDeviceDescriptor(d, d, d, d, d, d, d, d, d, d, d, d, 
                d, d));
        assertSame(id, new DeviceNotFoundException(id).getId());
    }
}
