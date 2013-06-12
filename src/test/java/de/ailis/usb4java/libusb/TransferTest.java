/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static de.ailis.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
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
        context = new Context();
        try
        {
            LibUsb.init(context);
        }
        catch (Throwable e)
        {
            context = null;
        }
    }

    /**
     * Tear down the test.
     */
    @After
    public void tearDown()
    {
        if (context != null)
        {
            LibUsb.exit(context);
        }
    }

    /**
     * Tests allocating and freeing a transfer object.
     */
    @Test
    public void testAllocAndFree()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertNotNull(transfer);
        LibUsb.freeTransfer(transfer);

        try
        {
            LibUsb.freeTransfer(transfer);
            fail("Double-free should throw IllegalStateException");
        }
        catch (IllegalStateException e)
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
    private void setPointer(DeviceHandle handle, long pointer)
    {
        try
        {
            Field field =
                DeviceHandle.class.getDeclaredField("deviceHandlePointer");
            field.setAccessible(true);
            field.set(handle, pointer);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.toString(), e);
        }
    }

    /**
     * Tests the {@link Transfer#setDevHandle(DeviceHandle)} and
     * {@link Transfer#getDevHandle()} methods.
     */
    @Test
    public void testDevHandle()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        DeviceHandle handle = new DeviceHandle();
        setPointer(handle, 1);
        DeviceHandle handle2 = new DeviceHandle();
        setPointer(handle2, 2);
        assertNull(transfer.devHandle());
        transfer.setDevHandle(handle);
        assertNotNull(transfer.devHandle());
        assertNotSame(handle, transfer.devHandle());
        assertNotEquals(handle2, transfer.devHandle());
        assertEquals(handle, transfer.devHandle());

        try
        {
            transfer.setDevHandle(null);
            fail("Setting devHandle to null should throw IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected behavior
        }

        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setFlags(byte)} and {@link Transfer#getFlags()}
     * methods.
     */
    @Test
    public void testFlags()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.flags());
        transfer.setFlags((byte) 1);
        assertEquals(1, transfer.flags());
        transfer.setFlags((byte) 0);
        assertEquals(0, transfer.flags());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setEndpoint(byte)} and
     * {@link Transfer#getEndpoint()} methods.
     */
    @Test
    public void testEndpoint()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.endpoint());
        transfer.setEndpoint((byte) 1);
        assertEquals(1, transfer.endpoint());
        transfer.setEndpoint((byte) 0);
        assertEquals(0, transfer.endpoint());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setType(byte)} and {@link Transfer#getType()}
     * methods.
     */
    @Test
    public void testType()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.type());
        transfer.setType((byte) 1);
        assertEquals(1, transfer.type());
        transfer.setType((byte) 0);
        assertEquals(0, transfer.type());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setTimeout(int)} and
     * {@link Transfer#getTimeout()} methods.
     */
    @Test
    public void testTimeout()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.timeout());
        transfer.setTimeout(1);
        assertEquals(1, transfer.timeout());
        transfer.setTimeout(0);
        assertEquals(0, transfer.timeout());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#getStatus()} methods.
     */
    @Test
    public void testGetStatus()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.status());
        LibUsb.freeTransfer(transfer);
    }
}
