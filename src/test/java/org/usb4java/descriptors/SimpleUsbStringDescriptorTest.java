/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.descriptors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link SimpleUsbStringDescriptor}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class SimpleUsbStringDescriptorTest
{
    /** The test subject. */
    private static SimpleUsbStringDescriptor descriptor;

    /** Value for {@link SimpleUsbStringDescriptor#bLength()}. */
    private static final byte LENGTH = 1;

    /** Value for {@link SimpleUsbStringDescriptor#bDescriptorType()}. */
    private static final byte DESCRIPTOR_TYPE = 2;

    /** Value for {@link SimpleUsbStringDescriptor#bString()}. */
    private static final String STRING = "usb4java";

    /** A wrong value for equality test. */
    private static final byte WRONG = 0;

    /**
     * Setup the test subject.
     * 
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    @BeforeClass
    public static void setUp() throws UnsupportedEncodingException
    {
        descriptor = new SimpleUsbStringDescriptor(
            LENGTH, DESCRIPTOR_TYPE, STRING);
    }

    /**
     * Tests the copy constructor.
     */
    @Test
    public void testCopyConstructor()
    {
        final SimpleUsbStringDescriptor copy =
            new SimpleUsbStringDescriptor(descriptor);
        assertNotSame(copy, descriptor);
        assertEquals(copy, descriptor);
    }

    /**
     * Tests the byte buffer constructor.
     * 
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    @Test
    public void testByteBufferConstructor() throws UnsupportedEncodingException
    {
        ByteBuffer buffer = ByteBuffer.allocate(STRING.length() * 2 + 2);
        buffer.put((byte) (STRING.length() * 2 + 2));
        buffer.put(DESCRIPTOR_TYPE);
        buffer.put(STRING.getBytes("UTF-16LE"));
        final SimpleUsbStringDescriptor descriptor =
            new SimpleUsbStringDescriptor(buffer);
        assertEquals((byte) (STRING.length() * 2 + 2), descriptor.bLength());
        assertEquals(DESCRIPTOR_TYPE, descriptor.bDescriptorType());
        assertEquals(STRING, descriptor.getString());
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#bLength()} method.
     */
    @Test
    public void testLength()
    {
        assertEquals(LENGTH, descriptor.bLength());
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#bDescriptorType()} method.
     */
    @Test
    public void testDescriptorType()
    {
        assertEquals(DESCRIPTOR_TYPE, descriptor.bDescriptorType());
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#bString()} method.
     * 
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    @Test
    public void testString() throws UnsupportedEncodingException
    {
        assertArrayEquals(STRING.getBytes("UTF-16LE"), descriptor.bString());
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#getString()} method.
     * 
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    @Test
    public void testGetString() throws UnsupportedEncodingException
    {
        assertEquals(STRING, descriptor.getString());
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        final int code = descriptor.hashCode();
        assertEquals(code, descriptor.hashCode());
        assertEquals(code,
            new SimpleUsbStringDescriptor(descriptor).hashCode());
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#equals(Object)} method.
     * 
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    @Test
    public void testEquals() throws UnsupportedEncodingException
    {
        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(new Object()));
        assertTrue(descriptor.equals(descriptor));
        assertTrue(descriptor.equals(new SimpleUsbStringDescriptor(
            descriptor)));
        assertFalse(descriptor.equals(new SimpleUsbStringDescriptor(
            WRONG, DESCRIPTOR_TYPE, STRING)));
        assertFalse(descriptor.equals(new SimpleUsbStringDescriptor(
            LENGTH, WRONG, STRING)));
        assertFalse(descriptor.equals(new SimpleUsbStringDescriptor(
            LENGTH, DESCRIPTOR_TYPE, "wrong")));
    }

    /**
     * Tests the {@link SimpleUsbStringDescriptor#toString()} method.
     * 
     * @throws UnsupportedEncodingException
     *             When system does not support UTF-16LE encoding.
     */
    @Test
    public void testToString() throws UnsupportedEncodingException
    {
        assertEquals(descriptor.getString(), descriptor.toString());
    }
}
