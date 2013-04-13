/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.descriptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link SimpleUsbEndpointDescriptor}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class SimpleUsbEndpointDescriptorTest
{
    /** The test subject. */
    private static SimpleUsbEndpointDescriptor descriptor;

    /** Value for {@link SimpleUsbEndpointDescriptor#bLength()}. */
    private static final byte LENGTH = 1;

    /** Value for {@link SimpleUsbEndpointDescriptor#bDescriptorType()}. */
    private static final byte DESCRIPTOR_TYPE = 2;

    /** Value for {@link SimpleUsbEndpointDescriptor#bEndpointAddress()}. */
    private static final byte ENDPOINT_ADDRESS = 3;

    /** Value for {@link SimpleUsbEndpointDescriptor#bmAttributes()}. */
    private static final byte ATTRIBUTES = 4;

    /** Value for {@link SimpleUsbEndpointDescriptor#wMaxPacketSize()}. */
    private static final short MAX_PACKET_SIZE = 5;

    /** Value for {@link SimpleUsbEndpointDescriptor#bInterval()}. */
    private static final byte INTERVAL = 6;

    /** A wrong short for equality test. */
    private static final byte WRONG = 0;

    /**
     * Setup the test subject.
     */
    @BeforeClass
    public static void setUp()
    {
        descriptor = new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL);
    }

    /**
     * Tests the copy constructor.
     */
    @Test
    public void testCopyConstructor()
    {
        final SimpleUsbEndpointDescriptor copy =
            new SimpleUsbEndpointDescriptor(descriptor);
        assertNotSame(copy, descriptor);
        assertEquals(copy, descriptor);
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#bLength()} method.
     */
    @Test
    public void testLength()
    {
        assertEquals(LENGTH, descriptor.bLength());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#bDescriptorType()} method.
     */
    @Test
    public void testDescriptorType()
    {
        assertEquals(DESCRIPTOR_TYPE, descriptor.bDescriptorType());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#bEndpointAddress()} method.
     */
    @Test
    public void testEndpontAddress()
    {
        assertEquals(ENDPOINT_ADDRESS, descriptor.bEndpointAddress());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#bmAttributes()} method.
     */
    @Test
    public void testAttributes()
    {
        assertEquals(ATTRIBUTES, descriptor.bmAttributes());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#wMaxPacketSize()} method.
     */
    @Test
    public void testMaxPacketSize()
    {
        assertEquals(MAX_PACKET_SIZE, descriptor.wMaxPacketSize());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#bInterval()} method.
     */
    @Test
    public void testInterval()
    {
        assertEquals(INTERVAL, descriptor.bInterval());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        final int code = descriptor.hashCode();
        assertEquals(code, descriptor.hashCode());
        assertEquals(code,
            new SimpleUsbEndpointDescriptor(descriptor).hashCode());
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#equals(Object)} method.
     */
    @Test
    public void testEquals()
    {
        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(new Object()));
        assertTrue(descriptor.equals(descriptor));
        assertTrue(descriptor.equals(new SimpleUsbEndpointDescriptor(
            descriptor)));
        assertFalse(descriptor.equals(new SimpleUsbEndpointDescriptor(
            WRONG, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL)));
        assertFalse(descriptor.equals(new SimpleUsbEndpointDescriptor(
            LENGTH, WRONG, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL)));
        assertFalse(descriptor.equals(new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, WRONG, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL)));
        assertFalse(descriptor.equals(new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, WRONG,
            MAX_PACKET_SIZE, INTERVAL)));
        assertFalse(descriptor.equals(new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            WRONG, INTERVAL)));
        assertFalse(descriptor.equals(new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, WRONG)));
    }

    /**
     * Tests the {@link SimpleUsbEndpointDescriptor#toString()} method.
     */
    @Test
    public void testToString()
    {
        assertEquals(descriptor.toString(), descriptor.toString());
        assertEquals(descriptor.toString(),
            new SimpleUsbEndpointDescriptor(descriptor).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbEndpointDescriptor(
            WRONG, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbEndpointDescriptor(
            LENGTH, WRONG, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, WRONG, ATTRIBUTES,
            MAX_PACKET_SIZE, INTERVAL).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, WRONG,
            MAX_PACKET_SIZE, INTERVAL).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            WRONG, INTERVAL).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbEndpointDescriptor(
            LENGTH, DESCRIPTOR_TYPE, ENDPOINT_ADDRESS, ATTRIBUTES,
            MAX_PACKET_SIZE, WRONG).toString());
    }
}
