/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.junit.Test;
import org.usb4java.BufferUtils;

/**
 * Tests the {@link BufferUtils} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class BufferUtilsTest
{
    /**
     * Tests the {@link BufferUtils#allocateByteBuffer(int)} method.
     */
    @Test
    public void testAllocateByteBuffer()
    {
        ByteBuffer buffer = BufferUtils.allocateByteBuffer(13);
        assertEquals(13, buffer.limit());
    }

    /**
     * Tests the {@link BufferUtils#allocateIntBuffer()} method.
     */
    @Test
    public void testAllocateIntBuffer()
    {
        IntBuffer buffer = BufferUtils.allocateIntBuffer();
        assertEquals(1, buffer.limit());
    }

    /**
     * Tests the {@link BufferUtils#allocateLongBuffer()} method.
     */
    @Test
    public void testAllocateLongBuffer()
    {
        LongBuffer buffer = BufferUtils.allocateLongBuffer();
        assertEquals(1, buffer.limit());
    }

    /**
     * Tests the {@link BufferUtils#slice(ByteBuffer, int, int)} method.
     */
    @Test
    public void testSlice()
    {
        ByteBuffer buffer = BufferUtils.allocateByteBuffer(10);
        buffer.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        buffer.rewind();
        ByteBuffer sliced = BufferUtils.slice(buffer,  2, 3);
        assertEquals(3, sliced.remaining());
        assertEquals(3, sliced.get());
        assertEquals(4, sliced.get());
        assertEquals(5, sliced.get());
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
        assertEquals(0, BufferUtils.class.getConstructors().length);
        Constructor<?> c = BufferUtils.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }
}
