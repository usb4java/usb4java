/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.usb4java.DescriptorUtils;
import org.usb4java.LibUsb;

/**
 * Tests the {@link DescriptorUtils} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class DescriptorUtilsTest
{
    /**
     * Tests the {@link DescriptorUtils#decodeBCD(short)} method.
     */
    @Test
    public void testDecodeBCD()
    {
        assertEquals("10.20", DescriptorUtils.decodeBCD((short) 0x1020));
    }

    /**
     * Tests the {@link DescriptorUtils#getSpeedName(int)} method.
     */
    @Test
    public void testGetSpeedName()
    {
        assertEquals("Super", DescriptorUtils.getSpeedName(LibUsb.SPEED_SUPER));
        assertEquals("Full", DescriptorUtils.getSpeedName(LibUsb.SPEED_FULL));
        assertEquals("High", DescriptorUtils.getSpeedName(LibUsb.SPEED_HIGH));
        assertEquals("Low", DescriptorUtils.getSpeedName(LibUsb.SPEED_LOW));
        assertEquals("Unknown",
            DescriptorUtils.getSpeedName(LibUsb.SPEED_UNKNOWN));
    }

    /**
     * Tests the {@link DescriptorUtils#getDirectionName(byte)} method.
     */
    @Test
    public void testGetDirectionName()
    {
        assertEquals("IN", DescriptorUtils.getDirectionName(LibUsb.ENDPOINT_IN));
        assertEquals("OUT", DescriptorUtils.getDirectionName(LibUsb.ENDPOINT_OUT));
    }

    /**
     * Tests the {@link DescriptorUtils#getSynchTypeName(byte)} method.
     */
    @Test
    public void testGetSynchTypeName()
    {
        assertEquals("None", DescriptorUtils.getSynchTypeName((byte) 0));
        assertEquals("Asynchronous", DescriptorUtils.getSynchTypeName((byte) 4));
        assertEquals("Adaptive", DescriptorUtils.getSynchTypeName((byte) 8));
        assertEquals("Synchronous", DescriptorUtils.getSynchTypeName((byte) 12));
    }

    /**
     * Tests the {@link DescriptorUtils#getUsageTypeName(byte)} method.
     */
    @Test
    public void testGetUsageTypeName()
    {
        assertEquals("Data", DescriptorUtils.getUsageTypeName((byte) 0));
        assertEquals("Feedback", DescriptorUtils.getUsageTypeName((byte) 16));
        assertEquals("Implicit Feedback Data",
            DescriptorUtils.getUsageTypeName((byte) 32));
        assertEquals("Reserved", DescriptorUtils.getUsageTypeName((byte) 48));
    }


    /**
     * Tests the {@link DescriptorUtils#getTransferTypeName(byte)} method.
     */
    @Test
    public void testGetTransferTypeName()
    {
        assertEquals("Control", DescriptorUtils.getTransferTypeName((byte) 0));
        assertEquals("Isochronous", DescriptorUtils.getTransferTypeName((byte) 1));
        assertEquals("Bulk", DescriptorUtils.getTransferTypeName((byte) 2));
        assertEquals("Interrupt", DescriptorUtils.getTransferTypeName((byte) 3));
    }

    /**
     * Tests the {@link DescriptorUtils#getUSBClassName(byte)} method.
     */
    @Test
    public void testGetUSBClassName()
    {
        assertEquals("Audio",
            DescriptorUtils.getUSBClassName(LibUsb.CLASS_AUDIO));
        assertEquals("Unknown", DescriptorUtils.getUSBClassName((byte) 0xF3));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(java.nio.ByteBuffer)} method.
     */
    @Test
    public void testDumpByteBuffer()
    {
        ByteBuffer bytes = ByteBuffer.allocate(0);
        assertEquals("", DescriptorUtils.dump(bytes));

        bytes = ByteBuffer.allocate(8);
        bytes.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals("01 02 03 04 05 06 07 08", DescriptorUtils.dump(bytes));

        bytes = ByteBuffer.allocate(16);
        bytes.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16 });
        assertEquals("01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10",
            DescriptorUtils.dump(bytes));

        bytes = ByteBuffer.allocate(20);
        bytes.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20 });
        assertEquals(String.format("01 02 03 04 05 06 07 08 09 0a 0b 0c 0d " +
            "0e 0f 10%n11 12 13 14"), DescriptorUtils.dump(bytes));
    }

    /**
     * Ensure constructor is private.
     * 
     * @throws Exception
     *             When constructor test fails.
     */
    @Test
    public void testPrivateConstructor() throws Exception
    {
        assertEquals(0, DescriptorUtils.class.getConstructors().length);
        Constructor<?> c = DescriptorUtils.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }
}
