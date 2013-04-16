/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static de.ailis.usb4java.UsbAssume.assumeUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

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
        this.context = new Context();
        try
        {
            LibUsb.init(this.context);
        }
        catch (Throwable e)
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
        if (this.context != null) LibUsb.exit(this.context);
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
            Field field = DeviceHandle.class.getDeclaredField("handlePointer");
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
        assertNull(transfer.getDevHandle());
        transfer.setDevHandle(handle);
        assertNotNull(transfer.getDevHandle());
        assertNotSame(handle, transfer.getDevHandle());
        assertNotEquals(handle2, transfer.getDevHandle());
        assertEquals(handle, transfer.getDevHandle());
        transfer.setDevHandle(null);
        assertNull(transfer.getDevHandle());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setFlags(int)} and {@link Transfer#getFlags()}
     * methods.
     */
    @Test
    public void testFlags()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.getFlags());
        transfer.setFlags(1);
        assertEquals(1, transfer.getFlags());
        transfer.setFlags(0);
        assertEquals(0, transfer.getFlags());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setEndpoint(int)} and
     * {@link Transfer#getEndpoint()} methods.
     */
    @Test
    public void testEndpoint()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.getEndpoint());
        transfer.setEndpoint(1);
        assertEquals(1, transfer.getEndpoint());
        transfer.setEndpoint(0);
        assertEquals(0, transfer.getEndpoint());
        LibUsb.freeTransfer(transfer);
    }

    /**
     * Tests the {@link Transfer#setType(int)} and {@link Transfer#getType()}
     * methods.
     */
    @Test
    public void testType()
    {
        assumeUsbTestsEnabled();
        Transfer transfer = LibUsb.allocTransfer(0);
        assertEquals(0, transfer.getType());
        transfer.setType(1);
        assertEquals(1, transfer.getType());
        transfer.setType(0);
        assertEquals(0, transfer.getType());
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
        assertEquals(0, transfer.getTimeout());
        transfer.setTimeout(1);
        assertEquals(1, transfer.getTimeout());
        transfer.setTimeout(0);
        assertEquals(0, transfer.getTimeout());
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
        assertEquals(0, transfer.getStatus());
        LibUsb.freeTransfer(transfer);
    }
}
