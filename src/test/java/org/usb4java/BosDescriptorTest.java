/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.usb4java.test.UsbAssume.isUsbTestsEnabled;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link BosDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class BosDescriptorTest
{
    /** The test subject. */
    private BosDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new BosDescriptor();
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
     * Tests uninitialized access to {@link BosDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to {@link BosDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to {@link BosDescriptor#wTotalLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedTotalLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.wTotalLength();
    }

    /**
     * Tests uninitialized access to {@link BosDescriptor#bNumDeviceCaps()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedNumDeviceCaps()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bNumDeviceCaps();
    }

    /**
     * Tests uninitialized access to {@link BosDescriptor#devCapability()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDevCapability()
    {
        assumeUsbTestsEnabled();
        this.descriptor.devCapability();
    }
}
