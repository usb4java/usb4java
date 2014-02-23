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
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

/**
 * Tests the {@link InterfaceDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class InterfaceDescriptorTest
{
    /** The test subject. */
    private InterfaceDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new InterfaceDescriptor();
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
     * Tests uninitialized access to {@link InterfaceDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to
     * {@link InterfaceDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to
     * {@link InterfaceDescriptor#bInterfaceNumber()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedInterfaceNumber()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bInterfaceNumber();
    }

    /**
     * Tests uninitialized access to
     * {@link InterfaceDescriptor#bAlternateSetting()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedAlternateSetting()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bAlternateSetting();
    }

    /**
     * Tests uninitialized access to {@link InterfaceDescriptor#bNumEndpoints()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedNumEndpoints()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bNumEndpoints();
    }

    /**
     * Tests uninitialized access to
     * {@link InterfaceDescriptor#bInterfaceClass()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedInterfaceClass()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bInterfaceClass();
    }

    /**
     * Tests uninitialized access to
     * {@link InterfaceDescriptor#bInterfaceSubClass()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedInterfaceSubClass()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bInterfaceSubClass();
    }

    /**
     * Tests uninitialized access to
     * {@link InterfaceDescriptor#bInterfaceProtocol()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedInterfaceProtocol()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bInterfaceProtocol();
    }

    /**
     * Tests uninitialized access to {@link InterfaceDescriptor#iInterface()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedInterface()
    {
        assumeUsbTestsEnabled();
        this.descriptor.iInterface();
    }

    /**
     * Tests uninitialized access to {@link InterfaceDescriptor#endpoint()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedEndpoint()
    {
        assumeUsbTestsEnabled();
        this.descriptor.endpoint();
    }

    /**
     * Tests uninitialized access to {@link InterfaceDescriptor#extra()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedExtra()
    {
        assumeUsbTestsEnabled();
        this.descriptor.extra();
    }

    /**
     * Tests uninitialized access to {@link InterfaceDescriptor#extraLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedExtraLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.extraLength();
    }
}
