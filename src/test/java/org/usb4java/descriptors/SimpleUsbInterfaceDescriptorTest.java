/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.descriptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link SimpleUsbInterfaceDescriptor}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class SimpleUsbInterfaceDescriptorTest
{
    /** The test subject. */
    private static SimpleUsbInterfaceDescriptor descriptor;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bLength()}. */
    private static final byte LENGTH = 1;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bDescriptorType()}. */
    private static final byte DESCRIPTOR_TYPE = 2;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bInterfaceNumber()}. */
    private static final byte INTERFACE_NUMBER = 3;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bAlternateSetting()}. */
    private static final byte ALTERNATE_SETTING = 4;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bNumEndpoints()}. */
    private static final byte NUM_ENDPOINTS = 5;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bInterfaceClass()}. */
    private static final byte INTERFACE_CLASS = 6;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bInterfaceSubClass()}. */
    private static final byte INTERFACE_SUB_CLASS = 7;

    /** Value for {@link SimpleUsbInterfaceDescriptor#bInterfaceProtocol()}. */
    private static final byte INTERFACE_PROTOCOL = 8;

    /** Value for {@link SimpleUsbInterfaceDescriptor#iInterface()}. */
    private static final byte INTERFACE = 9;

    /** A wrong value for equality test. */
    private static final byte WRONG = 0;

    /**
     * Setup the test subject.
     */
    @BeforeClass
    public static void setUp()
    {
        descriptor = new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE);
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bLength()} method.
     */
    @Test
    public void testLength()
    {
        assertEquals(LENGTH, descriptor.bLength());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bDescriptorType()} method.
     */
    @Test
    public void testDescriptorType()
    {
        assertEquals(DESCRIPTOR_TYPE, descriptor.bDescriptorType());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bInterfaceNumber()} method.
     */
    @Test
    public void testInterfaceNumber()
    {
        assertEquals(INTERFACE_NUMBER, descriptor.bInterfaceNumber());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bAlternateSetting()}
     * method.
     */
    @Test
    public void testAlternateSetting()
    {
        assertEquals(ALTERNATE_SETTING, descriptor.bAlternateSetting());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bNumEndpoints()} method.
     */
    @Test
    public void testNumEndpoints()
    {
        assertEquals(NUM_ENDPOINTS, descriptor.bNumEndpoints());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bInterfaceClass()} method.
     */
    @Test
    public void testInterfaceClass()
    {
        assertEquals(INTERFACE_CLASS, descriptor.bInterfaceClass());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bInterfaceSubClass()}
     * method.
     */
    @Test
    public void testInterfaceSubClass()
    {
        assertEquals(INTERFACE_SUB_CLASS, descriptor.bInterfaceSubClass());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#bInterfaceProtocol()}
     * method.
     */
    @Test
    public void testInterfaceProtocol()
    {
        assertEquals(INTERFACE_PROTOCOL, descriptor.bInterfaceProtocol());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#iInterface()} method.
     */
    @Test
    public void testInterface()
    {
        assertEquals(INTERFACE, descriptor.iInterface());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        final int code = descriptor.hashCode();
        assertEquals(code, descriptor.hashCode());
        assertEquals(code, new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE).hashCode());
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#equals(Object)} method.
     */
    @Test
    public void testEquals()
    {
        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(new Object()));
        assertTrue(descriptor.equals(descriptor));
        assertTrue(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            WRONG, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, WRONG, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, WRONG, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, WRONG,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            WRONG, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, WRONG, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, WRONG,
            INTERFACE_PROTOCOL, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            WRONG, INTERFACE)));
        assertFalse(descriptor.equals(new SimpleUsbInterfaceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
            NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
            INTERFACE_PROTOCOL, WRONG)));
    }

    /**
     * Tests the {@link SimpleUsbInterfaceDescriptor#toString()} method.
     */
    @Test
    public void testToString()
    {
        assertEquals(descriptor.toString(), descriptor.toString());
        assertEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                WRONG, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, WRONG, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, WRONG, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, WRONG,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                WRONG, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, WRONG, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, WRONG,
                INTERFACE_PROTOCOL, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                WRONG, INTERFACE).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbInterfaceDescriptor(
                LENGTH, DESCRIPTOR_TYPE, INTERFACE_NUMBER, ALTERNATE_SETTING,
                NUM_ENDPOINTS, INTERFACE_CLASS, INTERFACE_SUB_CLASS,
                INTERFACE_PROTOCOL, WRONG).toString());
    }
}
