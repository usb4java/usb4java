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
import org.usb4java.Usb20ExtensionDescriptor;

/**
 * Tests the {@link Usb20ExtensionDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class Usb20ExtensionDescriptorTest
{
    /** The test subject. */
    private Usb20ExtensionDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new Usb20ExtensionDescriptor();
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
     * Tests uninitialized access to {@link Usb20ExtensionDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to
     * {@link Usb20ExtensionDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to
     * {@link Usb20ExtensionDescriptor#bDevCapabilityType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDevCapabilityType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDevCapabilityType();
    }

    /**
     * Tests uninitialized access to
     * {@link Usb20ExtensionDescriptor#bmAttributes()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedAttributes()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bmAttributes();
    }
}
