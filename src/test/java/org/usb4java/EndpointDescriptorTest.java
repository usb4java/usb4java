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
import org.usb4java.EndpointDescriptor;
import org.usb4java.LibUsb;

/**
 * Tests the {@link EndpointDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class EndpointDescriptorTest
{
    /** The test subject. */
    private EndpointDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new EndpointDescriptor();
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
     * Tests uninitialized access to {@link EndpointDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to
     * {@link EndpointDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to
     * {@link EndpointDescriptor#bEndpointAddress()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedEndpointAddress()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bEndpointAddress();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#bmAttributes()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedAttributes()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bmAttributes();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#wMaxPacketSize()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMaxPacketSize()
    {
        assumeUsbTestsEnabled();
        this.descriptor.wMaxPacketSize();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#bInterval()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedInterval()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bInterval();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#bRefresh()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedRefresh()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bRefresh();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#bSynchAddress()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedSynchAddress()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bSynchAddress();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#extra()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedExtra()
    {
        assumeUsbTestsEnabled();
        this.descriptor.extra();
    }

    /**
     * Tests uninitialized access to {@link EndpointDescriptor#extraLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedExtraLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.extraLength();
    }
}
