package de.ailis.usb4java.utils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

public final class BufferUtils
{
    private static int intSize = Integer.SIZE / Byte.SIZE;
    private static int longSize = Long.SIZE / Byte.SIZE;

    public static ByteBuffer allocateByteBuffer(final int bytes)
    {
        return ByteBuffer.allocateDirect(bytes);
    }

    public static IntBuffer allocateIntBuffer()
    {
        return ByteBuffer.allocateDirect(intSize).asIntBuffer();
    }

    public static LongBuffer allocateLongBuffer()
    {
        return ByteBuffer.allocateDirect(longSize).asLongBuffer();
    }

    public static ByteBuffer slice(final ByteBuffer buffer, final int offset,
        final int length)
    {
        int oldPosition = buffer.position();
        int oldLimit = buffer.limit();

        buffer.position(offset);
        buffer.limit(offset + length);

        final ByteBuffer slice = buffer.slice();

        buffer.position(oldPosition);
        buffer.limit(oldLimit);

        return slice;
    }
}
