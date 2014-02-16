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
 * Tests the {@link SimpleUsbDeviceDescriptor}.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class SimpleUsbDeviceDescriptorTest
{
    /** The test subject. */
    private static SimpleUsbDeviceDescriptor descriptor;

    /** Value for {@link SimpleUsbDeviceDescriptor#bLength()}. */
    private static final byte LENGTH = 1;

    /** Value for {@link SimpleUsbDeviceDescriptor#bDescriptorType()}. */
    private static final byte DESCRIPTOR_TYPE = 2;

    /** Value for {@link SimpleUsbDeviceDescriptor#bcdUSB()}. */
    private static final short USB = 3;

    /** Value for {@link SimpleUsbDeviceDescriptor#bDeviceClass()}. */
    private static final byte DEVICE_CLASS = 4;

    /** Value for {@link SimpleUsbDeviceDescriptor#bDeviceSubClass()}. */
    private static final byte DEVICE_SUB_CLASS = 5;

    /** Value for {@link SimpleUsbDeviceDescriptor#bDeviceProtocol()}. */
    private static final byte DEVICE_PROTOCOL = 6;

    /** Value for {@link SimpleUsbDeviceDescriptor#bMaxPacketSize0()}. */
    private static final byte MAX_PACKET_SIZE0 = 7;

    /** Value for {@link SimpleUsbDeviceDescriptor#idVendor()}. */
    private static final short ID_VENDOR = 8;

    /** Value for {@link SimpleUsbDeviceDescriptor#idProduct()}. */
    private static final short ID_PRODUCT = 9;

    /** Value for {@link SimpleUsbDeviceDescriptor#bcdDevice()}. */
    private static final short DEVICE = 10;

    /** Value for {@link SimpleUsbDeviceDescriptor#iManufacturer()}. */
    private static final byte MANUFACTURER = 11;

    /** Value for {@link SimpleUsbDeviceDescriptor#iProduct()}. */
    private static final byte PRODUCT = 12;

    /** Value for {@link SimpleUsbDeviceDescriptor#iSerialNumber()}. */
    private static final byte SERIAL_NUMBER = 13;

    /** Value for {@link SimpleUsbDeviceDescriptor#bNumConfigurations()}. */
    private static final byte NUM_CONFIGURATIONS = 14;

    /** A wrong value for equality test. */
    private static final byte WRONG = 0;

    /**
     * Setup the test subject.
     */
    @BeforeClass
    public static void setUp()
    {
        descriptor = new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS);
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bLength()} method.
     */
    @Test
    public void testLength()
    {
        assertEquals(LENGTH, descriptor.bLength());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bDescriptorType()} method.
     */
    @Test
    public void testDescriptorType()
    {
        assertEquals(DESCRIPTOR_TYPE, descriptor.bDescriptorType());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bcdUSB()} method.
     */
    @Test
    public void testUSB()
    {
        assertEquals(USB, descriptor.bcdUSB());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bDeviceClass()} method.
     */
    @Test
    public void testDeviceClass()
    {
        assertEquals(DEVICE_CLASS, descriptor.bDeviceClass());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bDeviceSubClass()} method.
     */
    @Test
    public void testDeviceSubClass()
    {
        assertEquals(DEVICE_SUB_CLASS, descriptor.bDeviceSubClass());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bMaxPacketSize0()} method.
     */
    @Test
    public void testMaxPacketSize0()
    {
        assertEquals(MAX_PACKET_SIZE0, descriptor.bMaxPacketSize0());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#idVendor()} method.
     */
    @Test
    public void testIdVendor()
    {
        assertEquals(ID_VENDOR, descriptor.idVendor());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#idProduct()} method.
     */
    @Test
    public void testIdProduct()
    {
        assertEquals(ID_PRODUCT, descriptor.idProduct());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bcdDevice()} method.
     */
    @Test
    public void testDevice()
    {
        assertEquals(DEVICE, descriptor.bcdDevice());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#iManufacturer()} method.
     */
    @Test
    public void testManufacturer()
    {
        assertEquals(MANUFACTURER, descriptor.iManufacturer());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#iProduct()} method.
     */
    @Test
    public void testProduct()
    {
        assertEquals(PRODUCT, descriptor.iProduct());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#iSerialNumber()} method.
     */
    @Test
    public void testSerialNumber()
    {
        assertEquals(SERIAL_NUMBER, descriptor.iSerialNumber());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#bNumConfigurations()} method.
     */
    @Test
    public void testNumConfigurations()
    {
        assertEquals(NUM_CONFIGURATIONS, descriptor.bNumConfigurations());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        final int code = descriptor.hashCode();
        assertEquals(code, descriptor.hashCode());
        assertEquals(code,new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, 
            NUM_CONFIGURATIONS).hashCode());
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#equals(Object)} method.
     */
    @Test
    public void testEquals()
    {
        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(new Object()));
        assertTrue(descriptor.equals(descriptor));
        assertTrue(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            WRONG, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, WRONG, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, WRONG, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, WRONG, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, WRONG,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            WRONG, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, WRONG, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, WRONG, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, WRONG, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, WRONG,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            WRONG, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, WRONG, SERIAL_NUMBER, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, WRONG, NUM_CONFIGURATIONS)));
        assertFalse(descriptor.equals(new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, WRONG)));
    }

    /**
     * Tests the {@link SimpleUsbDeviceDescriptor#toString()} method.
     */
    @Test
    public void testToString()
    {
        assertEquals(descriptor.toString(), descriptor.toString());
        assertEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, 
            NUM_CONFIGURATIONS).toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            WRONG, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, WRONG, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, WRONG, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, WRONG, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, WRONG,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            WRONG, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, WRONG, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, WRONG, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, WRONG, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, WRONG,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            WRONG, PRODUCT, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, WRONG, SERIAL_NUMBER, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, WRONG, NUM_CONFIGURATIONS)
            .toString());
        assertNotEquals(descriptor.toString(), new SimpleUsbDeviceDescriptor(
            LENGTH, DESCRIPTOR_TYPE, USB, DEVICE_CLASS, DEVICE_SUB_CLASS,
            DEVICE_PROTOCOL, MAX_PACKET_SIZE0, ID_VENDOR, ID_PRODUCT, DEVICE,
            MANUFACTURER, PRODUCT, SERIAL_NUMBER, WRONG)
            .toString());
    }
}
