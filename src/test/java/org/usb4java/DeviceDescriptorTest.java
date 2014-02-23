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
import org.usb4java.DeviceDescriptor;
import org.usb4java.LibUsb;

/**
 * Tests the {@link DeviceDescriptor} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceDescriptorTest
{
    /** The test subject. */
    private DeviceDescriptor descriptor;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
            this.descriptor = new DeviceDescriptor();
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
     * Tests uninitialized access to {@link DeviceDescriptor#bLength()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedLength()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bLength();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bDescriptorType()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDescriptorType()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDescriptorType();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bcdUSB()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedBcdUSB()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bcdUSB();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bDeviceClass()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDeviceClass()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDeviceClass();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bDeviceSubClass()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDeviceSubClass()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDeviceSubClass();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bDeviceProtocol()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedDeviceProtocol()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bDeviceProtocol();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bMaxPacketSize0()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMaxPacketSize0()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bMaxPacketSize0();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#idVendor()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedVendor()
    {
        assumeUsbTestsEnabled();
        this.descriptor.idVendor();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#idProduct()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedProduct()
    {
        assumeUsbTestsEnabled();
        this.descriptor.idProduct();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#bcdDevice()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedBcdDevice()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bcdDevice();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#iManufacturer()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedManufacturer()
    {
        assumeUsbTestsEnabled();
        this.descriptor.iManufacturer();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#iProduct()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedIProduct()
    {
        assumeUsbTestsEnabled();
        this.descriptor.iProduct();
    }

    /**
     * Tests uninitialized access to {@link DeviceDescriptor#iSerialNumber()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedSerialNumber()
    {
        assumeUsbTestsEnabled();
        this.descriptor.iSerialNumber();
    }

    /**
     * Tests uninitialized access to
     * {@link DeviceDescriptor#bNumConfigurations()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedNumConfigurations()
    {
        assumeUsbTestsEnabled();
        this.descriptor.bNumConfigurations();
    }
}
