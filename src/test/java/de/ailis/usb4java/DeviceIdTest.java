/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import de.ailis.usb4java.descriptors.SimpleUsbDeviceDescriptor;

/**
 * Tests the {@link DeviceId} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceIdTest
{
    /** A zero byte used in the dummy descriptor. */
    private static final byte ZERO = 0;

    /** A dummy device descriptor. */
    private static final SimpleUsbDeviceDescriptor descriptor =
        new SimpleUsbDeviceDescriptor(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO,
            ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);

    /**
     * Tests the constructor without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutDescriptor()
    {
        assertNotNull(new DeviceId(0, 1, 2, null));
    }

    /**
     * Tests the {@link DeviceId#getBusNumber()} method.
     */
    @Test
    public void testGetBusNumber()
    {
        assertEquals(1, new DeviceId(1, 2, 3, descriptor).getBusNumber());
    }

    /**
     * Tests the {@link DeviceId#getDeviceAddress()} method.
     */
    @Test
    public void testGetDeviceAddress()
    {
        assertEquals(2, new DeviceId(1, 2, 3, descriptor).getDeviceAddress());
    }

    /**
     * Tests the {@link DeviceId#getPortNumber()} method.
     */
    @Test
    public void testGetPortNumber()
    {
        assertEquals(3, new DeviceId(1, 2, 3, descriptor).getPortNumber());
    }

    /**
     * Tests the {@link DeviceId#getDeviceDescriptor()} method.
     */
    @Test
    public void testGetDeviceDescriptor()
    {
        assertSame(descriptor,
            new DeviceId(1, 2, 3, descriptor).getDeviceDescriptor());
    }
}
