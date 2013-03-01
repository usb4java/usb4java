/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static org.junit.Assert.assertEquals;
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
        LibUSB.init(this.context);
    }

    /**
     * Tear down the test.
     */
    @After
    public void tearDown()
    {
        LibUSB.exit(this.context);
    }

    /**
     * Tests the {@link LibUSB#setDebug(Context, int)} method.
     */
    @Test
    public void testSetDebug()
    {
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
        LibUSB.getDeviceList(this.context, null);
    }
}
