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
 * Tests the {@link SimpleUsbConfigurationDescriptor}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class SimpleUsbConfigurationDescriptorTest
{
    /** The test subject. */
    private static SimpleUsbConfigurationDescriptor descriptor;

    /** Value for {@link SimpleUsbConfigurationDescriptor#bLength()}. */
    private static final byte LENGTH = 1;

    /** Value for {@link SimpleUsbConfigurationDescriptor#bDescriptorType()}. */
    private static final byte DESCRIPTOR_TYPE = 2;

    /** Value for {@link SimpleUsbConfigurationDescriptor#wTotalLength()}. */
    private static final short TOTAL_LENGTH = 3;

    /** Value for {@link SimpleUsbConfigurationDescriptor#bNumInterfaces()}. */
    private static final byte NUM_INTERFACES = 4;

    /**
     * Value for {@link SimpleUsbConfigurationDescriptor#bConfigurationValue()}.
     */
    private static final byte CONFIGURATION_VALUE = 5;

    /** Value for {@link SimpleUsbConfigurationDescriptor#iConfiguration()}. */
    private static final byte CONFIGURATION = 6;

    /** Value for {@link SimpleUsbConfigurationDescriptor#bmAttributes()}. */
    private static final byte ATTRIBUTES = 7;

    /** Value for {@link SimpleUsbConfigurationDescriptor#bMaxPower()}. */
    private static final byte MAX_POWER = 8;

    /** A wrong byte for equality test. */
    private static final byte WRONG_BYTE = 0;

    /** A wrong short for equality test. */
    private static final short WRONG_SHORT = 0;

    /**
     * Setup the test subject.
     */
    @BeforeClass
    public static void setUp()
    {
        descriptor = new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
            CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER);
    }

    /**
     * Tests the copy constructor.
     */
    @Test
    public void testCopyConstructor()
    {
        final SimpleUsbConfigurationDescriptor copy =
            new SimpleUsbConfigurationDescriptor(descriptor);
        assertNotSame(copy, descriptor);
        assertEquals(copy, descriptor);
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#bLength()} method.
     */
    @Test
    public void testLength()
    {
        assertEquals(LENGTH, descriptor.bLength());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#bDescriptorType()}
     * method.
     */
    @Test
    public void testDescriptorType()
    {
        assertEquals(DESCRIPTOR_TYPE, descriptor.bDescriptorType());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#wTotalLength()} method.
     */
    @Test
    public void testTotalLength()
    {
        assertEquals(TOTAL_LENGTH, descriptor.wTotalLength());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#bNumInterfaces()}
     * method.
     */
    @Test
    public void testNumInterfaces()
    {
        assertEquals(NUM_INTERFACES, descriptor.bNumInterfaces());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#bConfigurationValue()}
     * method.
     */
    @Test
    public void testConfigurationValue()
    {
        assertEquals(CONFIGURATION_VALUE, descriptor.bConfigurationValue());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#iConfiguration()}
     * method.
     */
    @Test
    public void testConfiguration()
    {
        assertEquals(CONFIGURATION, descriptor.iConfiguration());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#bmAttributes()} method.
     */
    @Test
    public void testAttributes()
    {
        assertEquals(ATTRIBUTES, descriptor.bmAttributes());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#bMaxPower()} method.
     */
    @Test
    public void testMaxPower()
    {
        assertEquals(MAX_POWER, descriptor.bMaxPower());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        final int code = descriptor.hashCode();
        assertEquals(code, descriptor.hashCode());
        assertEquals(code,
            new SimpleUsbConfigurationDescriptor(descriptor).hashCode());
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#equals(Object)} method.
     */
    @Test
    public void testEquals()
    {
        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(new Object()));
        assertTrue(descriptor.equals(descriptor));
        assertTrue(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            descriptor)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            WRONG_BYTE, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
            CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, WRONG_BYTE, TOTAL_LENGTH, NUM_INTERFACES,
            CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, WRONG_SHORT, NUM_INTERFACES,
            CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, WRONG_BYTE,
            CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
            WRONG_BYTE, CONFIGURATION, ATTRIBUTES, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
            CONFIGURATION_VALUE, WRONG_BYTE, ATTRIBUTES, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
            CONFIGURATION_VALUE, CONFIGURATION, WRONG_BYTE, MAX_POWER)));
        assertFalse(descriptor.equals(new SimpleUsbConfigurationDescriptor(
            LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
            CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, WRONG_BYTE)));
    }

    /**
     * Tests the {@link SimpleUsbConfigurationDescriptor#toString()} method.
     */
    @Test
    public void testToString()
    {
        assertEquals(descriptor.toString(), descriptor.toString());
        assertEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(descriptor).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                WRONG_BYTE, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
                CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)
                .toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, WRONG_BYTE, TOTAL_LENGTH, NUM_INTERFACES,
                CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)
                .toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, DESCRIPTOR_TYPE, WRONG_SHORT, NUM_INTERFACES,
                CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)
                .toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, WRONG_BYTE,
                CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, MAX_POWER)
                .toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
                WRONG_BYTE, CONFIGURATION, ATTRIBUTES, MAX_POWER).toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
                CONFIGURATION_VALUE, WRONG_BYTE, ATTRIBUTES, MAX_POWER)
                .toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
                CONFIGURATION_VALUE, CONFIGURATION, WRONG_BYTE, MAX_POWER)
                .toString());
        assertNotEquals(descriptor.toString(),
            new SimpleUsbConfigurationDescriptor(
                LENGTH, DESCRIPTOR_TYPE, TOTAL_LENGTH, NUM_INTERFACES,
                CONFIGURATION_VALUE, CONFIGURATION, ATTRIBUTES, WRONG_BYTE)
                .toString());
    }
}
