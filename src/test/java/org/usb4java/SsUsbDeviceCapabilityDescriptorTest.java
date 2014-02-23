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
import org.usb4java.LibUsb;
import org.usb4java.SsUsbDeviceCapabilityDescriptor;

/**
 * Tests the {@link SsUsbDeviceCapabilityDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class SsUsbDeviceCapabilityDescriptorTest
{
    /** The test subject. */
    private SsUsbDeviceCapabilityDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new SsUsbDeviceCapabilityDescriptor();
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
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bDevCapabilityType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDevCapabilityType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDevCapabilityType();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bmAttributes()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedAttributes()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bmAttributes();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#wSpeedSupported()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedSpeedSupported()
    {
        assumeUsbTestsEnabled();
        this.descriptor.wSpeedSupported();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bFunctionalitySupport()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedFunctionalitySupport()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bFunctionalitySupport();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bU1DevExitLat()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedU1DevExitLat()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bU1DevExitLat();
    }

    /**
     * Tests uninitialized access to
     * {@link SsUsbDeviceCapabilityDescriptor#bU2DevExitLat()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedU2DevExitLat()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bU2DevExitLat();
    }
}
