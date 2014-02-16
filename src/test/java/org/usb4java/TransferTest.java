/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.usb4java.Context;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.Transfer;

/**
 * Tests the {@link Transfer} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class TransferTest
{
    /** The libusb contxet. */
    private Context context;

    /**
     * Set up the test.
     */
    @Before
    public void setUp()
    {
        this.context = new Context();
        try
        {
            LibUsb.init(this.context);
        }
        catch (final Throwable e)
        {
            this.context = null;
        }
    }

    /**
     * Tear down the test.
     */
    @After
    public void tearDown()
    {
        if (this.context != null)
        {
            LibUsb.exit(this.context);
        }
    }

    /**
     * Tests allocating and freeing a transfer object.
     */
    @Test
    public void testAllocAndFree()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        assertNotNull(transfer);
        LibUsb.freeTransfer(transfer);

        try
        {
            LibUsb.freeTransfer(transfer);
            fail("Double-free should throw IllegalStateException");
        }
        catch (final IllegalStateException e)
        {
            // Expected behavior
        }
    }

    /**
     * Sets the device handle pointer via reflection.
     *
     * @param handle
     *            The device handle pointer.
     * @param pointer
     *            The pointer to set.
     */
    private void setPointer(final DeviceHandle handle, final long pointer)
    {
        try
        {
            final Field field = DeviceHandle.class
                .getDeclaredField("deviceHandlePointer");
            field.setAccessible(true);
            field.set(handle, pointer);
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.toString(), e);
        }
    }

    /**
     * Tests the {@link Transfer#setDevHandle(DeviceHandle)} and
     * {@link Transfer#devHandle()} methods.
     */
    @Test
    public void testDevHandle()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        final DeviceHandle handle = new DeviceHandle();
        this.setPointer(handle, 1);
        final DeviceHandle handle2 = new DeviceHandle();
        this.setPointer(handle2, 2);
        assertNull(transfer.devHandle());
        transfer.setDevHandle(handle);
        assertNotNull(transfer.devHandle());
        assertNotSame(handle, transfer.devHandle());
        assertNotEquals(handle2, transfer.devHandle());
        assertEquals(handle, transfer.devHandle());
        transfer.setDevHandle(null);
        assertNull(transfer.devHandle());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setFlags(byte)} and {@link Transfer#flags()}
     * methods.
     */
    @Test
    public void testFlags()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.flags());
        transfer.setFlags((byte) 1);
        assertEquals(1, transfer.flags());
        transfer.setFlags((byte) 0);
        assertEquals(0, transfer.flags());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setEndpoint(byte)} and
     * {@link Transfer#endpoint()} methods.
     */
    @Test
    public void testEndpoint()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.endpoint());
        transfer.setEndpoint((byte) 1);
        assertEquals(1, transfer.endpoint());
        transfer.setEndpoint((byte) 0);
        assertEquals(0, transfer.endpoint());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setType(byte)} and {@link Transfer#type()}
     * methods.
     */
    @Test
    public void testType()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.type());
        transfer.setType((byte) 1);
        assertEquals(1, transfer.type());
        transfer.setType((byte) 0);
        assertEquals(0, transfer.type());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setTimeout(long)} and
     * {@link Transfer#timeout()} methods.
     */
    @Test
    public void testTimeout()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.timeout());
        transfer.setTimeout(1);
        assertEquals(1, transfer.timeout());
        transfer.setTimeout(0);
        assertEquals(0, transfer.timeout());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#status()} methods.
     */
    @Test
    public void testGetStatus()
    {
        assumeUsbTestsEnabled();
        final Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.status());
        LibUsb.freeTransfer(transfer);
    }
}
