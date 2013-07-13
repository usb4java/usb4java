/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static de.ailis.usb4java.test.UsbAssume.assumeUsbTestsEnabled;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link SSEndpointCompanionDescriptor} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class SSEndpointCompanionDescriptorTest
{
    /** The test subject. */
    private SSEndpointCompanionDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        assumeUsbTestsEnabled();
        LibUsb.init(null);
        this.descriptor = new SSEndpointCompanionDescriptor();
    }
    
    /**
     * Tear down test.
     */
    @After    
    public void tearDown()
    {
        LibUsb.exit(null);
    }

    /**
     * Tests uninitialized access to
     * {@link SSEndpointCompanionDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to
     * {@link SSEndpointCompanionDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to
     * {@link SSEndpointCompanionDescriptor#bMaxBurst()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorMaxBurst()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bMaxBurst();
    }

    /**
     * Tests uninitialized access to
     * {@link SSEndpointCompanionDescriptor#bmAttributes()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorAttributes()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bmAttributes();
    }

    /**
     * Tests uninitialized access to
     * {@link SSEndpointCompanionDescriptor#wBytesPerInterval()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorBytesPerInterval()
    {
        assumeUsbTestsEnabled();
        this.descriptor.wBytesPerInterval();
    }
}
