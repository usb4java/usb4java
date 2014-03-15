/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

/**
 * Tests the global-scope methods of the {@link LibUsb} class which need a
 * open USB context.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUsbGlobalTest
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
     * Tests the {@link LibUsb#setDebug(Context, int)} method.
     */
    @Test
    public void testSetDebug()
    {
        assumeUsbTestsEnabled();
        LibUsb.setDebug(this.context, LibUsb.LOG_LEVEL_DEBUG);
        LibUsb.setDebug(this.context, LibUsb.LOG_LEVEL_INFO);
        LibUsb.setDebug(this.context, LibUsb.LOG_LEVEL_WARNING);
        LibUsb.setDebug(this.context, LibUsb.LOG_LEVEL_ERROR);
        LibUsb.setDebug(this.context, LibUsb.LOG_LEVEL_NONE);
    }

    /**
     * Test getting and freeing the device list.
     */
    @Test
    public void testDeviceList()
    {
        assumeUsbTestsEnabled();
        final DeviceList list = new DeviceList();
        final int result = LibUsb.getDeviceList(this.context, list);
        assertTrue(
            "At least one USB device must be present for the simple unit tests",
            result > 0);
        assertEquals(result, list.getSize());
        int i = 0;
        for (final Device device : list)
        {
            assertNotNull(device);
            assertEquals(device, list.get(i));
            assertEquals(Device.class, device.getClass());
            i++;
        }
        LibUsb.freeDeviceList(list, true);
    }

    /**
     * Test {@link LibUsb#getDeviceList(Context, DeviceList)} without specifying
     * a list container.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceListWithoutList()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceList(this.context, null);
    }

    /**
     * Checks the {@link LibUsb#le16ToCpu(short)} and
     * {@link LibUsb#cpuToLe16(short)} methods.
     */
    @Test
    public void testEndianConversion()
    {
        assumeUsbTestsEnabled();
        assertEquals(0x1234, LibUsb.le16ToCpu(LibUsb.cpuToLe16((short) 0x1234)));
    }

    /**
     * Tests the {@link LibUsb#hasCapability(int)} method.
     */
    @Test
    public void testHasCapability()
    {
        assumeUsbTestsEnabled();
        assertTrue(LibUsb.hasCapability(LibUsb.CAP_HAS_CAPABILITY));
        assertFalse(LibUsb.hasCapability(0x12345678));
    }

    /**
     * Tests the {@link LibUsb#errorName(int)} method.
     */
    @Test
    public void testErrorName()
    {
        assumeUsbTestsEnabled();
        assertEquals("LIBUSB_ERROR_IO", LibUsb.errorName(LibUsb.ERROR_IO));
        assertEquals("**UNKNOWN**", LibUsb.errorName(0x1234));
    }

    /**
     * Tests the {@link LibUsb#strError(int)} method.
     */
    @Test
    public void testStrError()
    {
        assumeUsbTestsEnabled();
        assertEquals("Input/Output Error", LibUsb.strError(LibUsb.ERROR_IO));
        assertEquals("Other error", LibUsb.strError(0x1234));
    }

    /**
     * Tests the {@link LibUsb#setLocale(String)} method.
     */
    @Test
    public void testSetLocale()
    {
        assumeUsbTestsEnabled();
        assertEquals(LibUsb.SUCCESS, LibUsb.setLocale("en"));
        assertEquals(LibUsb.ERROR_NOT_FOUND, LibUsb.setLocale("zz"));
        assertEquals(LibUsb.ERROR_INVALID_PARAM, LibUsb.setLocale("zzz"));
    }
}
