/*
 * Copyright (C) 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.usb4java.test.UsbAssume.isUsbTestsEnabled;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link DeviceList} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceListTest
{
    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
        }
    }

    /**
     * Tear down test.
     */
    @After
    public void tearDown()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.exit(null);
        }
    }

    /**
     * Tests the {@link DeviceList#equals(Object)} method.
     */
    @Test
    public void testEquals()
    {
        assumeUsbTestsEnabled();
        DeviceList a = new DeviceList();
        DeviceList b = new DeviceList();
        DeviceList c = new DeviceList();
        DeviceList d = new DeviceList();
        LibUsb.getDeviceList(null, a);
        LibUsb.getDeviceList(null, b);
        try
        {
            assertTrue(c.equals(d));
            assertTrue(a.equals(a));
            assertTrue(b.equals(b));
            assertFalse(a.equals(b));
            assertFalse(a.equals(null));
            assertFalse(a.equals(""));
        }
        finally
        {
            LibUsb.freeDeviceList(a, true);
            LibUsb.freeDeviceList(b, true);
        }
    }
    
    /**
     * Tests the {@link DeviceList#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        assumeUsbTestsEnabled();
        DeviceList list = new DeviceList();
        LibUsb.getDeviceList(null, list);
        try
        {
            assertEquals(list.hashCode(), list.hashCode());
        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }
    }
    
    /**
     * Tests the {@link DeviceList#toString()} method
     */
    @Test
    public void testToString()
    {
        assumeUsbTestsEnabled();
        DeviceList list = new DeviceList();
        LibUsb.getDeviceList(null, list);
        try
        {
            assertNotNull(list.toString());
            assertTrue(list.toString().length() > 0);
        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }
    }
    
    /**
     * Tests the {@link DeviceList#getPointer()} method
     */
    @Test
    public void testGe()
    {
        assumeUsbTestsEnabled();
        assumeUsbTestsEnabled();
        DeviceList list = new DeviceList();
        assertEquals(0, list.getPointer());
        LibUsb.getDeviceList(null, list);
        try
        {
            assertNotEquals(0, list.getPointer());
        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }
    }
}
