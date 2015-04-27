/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertNotNull;
import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.usb4java.test.UsbAssume.isUsbTestsEnabled;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link DeviceListIterator} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceListIteratorTest
{
    /** The test subject. */
    private DeviceListIterator iterator;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            final DeviceList list = new DeviceList();
            LibUsb.getDeviceList(null, list);
            this.iterator = new DeviceListIterator(list);
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
     * Tests the {@link DeviceListIterator#hasNext()} method. It just checks
     * that this call doesn't crash because we can't assume that a device is
     * connected at all.
     */
    @Test
    public void testHasNext()
    {
        assumeUsbTestsEnabled();
        this.iterator.hasNext();
    }

    /**
     * Tests the {@link DeviceListIterator#next()} method.
     */
    @Test
    public void testNext()
    {
        assumeUsbTestsEnabled();
        while (this.iterator.hasNext())
        {
            assertNotNull(this.iterator.next());
        }
    }

    /**
     * Ensures that {@link DeviceListIterator#next()} method throws
     * a {@link NoSuchElementException} when accessing elements after the end
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextAfterend()
    {
        assumeUsbTestsEnabled();
        while (this.iterator.hasNext()) {
            this.iterator.next();
        }
        this.iterator.next();
    }

    /**
     * Ensures that {@link DeviceListIterator#remove()} throws an exception
     * because the list is read-only.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove()
    {
        assumeUsbTestsEnabled();
        this.iterator.remove();
    }
}
