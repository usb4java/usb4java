/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.libusb;

import static de.ailis.usb4java.UsbAssume.assumeUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the global-scope methods of the {@link LibUSB} class which need a 
 * open USB context.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUSBGlobalTest
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
            LibUSB.init(this.context);
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
        if (this.context != null) LibUSB.exit(this.context);
    }

    /**
     * Tests the {@link LibUSB#setDebug(Context, int)} method.
     */
    @Test
    public void testSetDebug()
    {
        assumeUsbTestsEnabled();
        LibUSB.setDebug(this.context, LibUSB.LOG_LEVEL_DEBUG);
        LibUSB.setDebug(this.context, LibUSB.LOG_LEVEL_INFO);
        LibUSB.setDebug(this.context, LibUSB.LOG_LEVEL_WARNING);
        LibUSB.setDebug(this.context, LibUSB.LOG_LEVEL_ERROR);
        LibUSB.setDebug(this.context, LibUSB.LOG_LEVEL_NONE);
    }

    /**
     * Test getting and freeing the device list.
     */
    @Test
    public void testDeviceList()
    {
        assumeUsbTestsEnabled();
        final DeviceList list = new DeviceList();
        final int result = LibUSB.getDeviceList(this.context, list);
        assertTrue(
            "At least one USB device must be present for the simple unit tests",
            result > 0);
        assertEquals(result, list.getSize());
        int i = 0;
        for (Device device: list)
        {
            assertNotNull(device);
            assertEquals(device, list.get(i));
            assertEquals(Device.class, device.getClass());
            i++;
        }
        LibUSB.freeDeviceList(list, true);
    }

    /**
     * Test {@link LibUSB#getDeviceList(Context, DeviceList)} without specifying
     * a list container.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceListWithoutList()
    {
        assumeUsbTestsEnabled();
        LibUSB.getDeviceList(this.context, null);
    }

    /**
     * Checks the {@link LibUSB#le16ToCpu(int)} and
     * {@link LibUSB#cpuToLe16(int)} methods.
     */
    @Test
    public void testEndianConversion()
    {
        assumeUsbTestsEnabled();
        assertEquals(0x1234, LibUSB.le16ToCpu(LibUSB.cpuToLe16(0x1234)));
    }

    /**
     * Tests the {@link LibUSB#hasCapability(int)} method.
     */
    @Test
    public void testHasCapability()
    {
        assumeUsbTestsEnabled();
        assertTrue(LibUSB.hasCapability(LibUSB.CAP_HAS_CAPABILITY));
        assertFalse(LibUSB.hasCapability(0x12345678));
    }

    /**
     * Tests the {@link LibUSB#errorName(int)} method.
     */
    @Test
    public void testErrorName()
    {
        assumeUsbTestsEnabled();
        assertEquals("LIBUSB_ERROR_IO", LibUSB.errorName(LibUSB.ERROR_IO));
        assertEquals("**UNKNOWN**", LibUSB.errorName(0x1234));
    }
}
