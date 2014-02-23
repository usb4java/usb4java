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
import org.usb4java.ConfigDescriptor;
import org.usb4java.LibUsb;

/**
 * Tests the {@link ConfigDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class ConfigDescriptorTest
{
    /** The test subject. */
    private ConfigDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new ConfigDescriptor();
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
     * Tests uninitialized access to {@link ConfigDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#wTotalLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedTotalLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.wTotalLength();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#bNumInterfaces()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedNumInterfaces()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bNumInterfaces();
    }

    /**
     * Tests uninitialized access to
     * {@link ConfigDescriptor#bConfigurationValue()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedConfigurationValue()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bConfigurationValue();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#iConfiguration()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedConfiguration()
    {
        assumeUsbTestsEnabled();
        this.descriptor.iConfiguration();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#bmAttributes()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorAttributes()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bmAttributes();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#bMaxPower()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorMaxPower()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bMaxPower();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#iface()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorIface()
    {
        assumeUsbTestsEnabled();
        this.descriptor.iface();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#extra()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorExtra()
    {
        assumeUsbTestsEnabled();
        this.descriptor.extra();
    }

    /**
     * Tests uninitialized access to {@link ConfigDescriptor#extraLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorExtraLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.extraLength();
    }
}
