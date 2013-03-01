/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the {@link LibUSB} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUSBTest
{
    /**
     * Tests the constant values.
     */
    @Test
    public void testConstants()
    {
        // Log levels
        assertEquals(0, LibUSB.LOG_LEVEL_NONE);
        assertEquals(1, LibUSB.LOG_LEVEL_ERROR);
        assertEquals(2, LibUSB.LOG_LEVEL_WARNING);
        assertEquals(3, LibUSB.LOG_LEVEL_INFO);
        assertEquals(4, LibUSB.LOG_LEVEL_DEBUG);

        // Speed codes
        assertEquals(0, LibUSB.SPEED_UNKNOWN);
        assertEquals(1, LibUSB.SPEED_LOW);
        assertEquals(2, LibUSB.SPEED_FULL);
        assertEquals(3, LibUSB.SPEED_HIGH);
        assertEquals(4, LibUSB.SPEED_SUPER);

        // Standard requests
        assertEquals(0x00, LibUSB.REQUEST_GET_STATUS);
        assertEquals(0x01, LibUSB.REQUEST_CLEAR_FEATURE);
        assertEquals(0x03, LibUSB.REQUEST_SET_FEATURE);
        assertEquals(0x05, LibUSB.REQUEST_SET_ADDRESS);
        assertEquals(0x06, LibUSB.REQUEST_GET_DESCRIPTOR);
        assertEquals(0x07, LibUSB.REQUEST_SET_DESCRIPTOR);
        assertEquals(0x08, LibUSB.REQUEST_GET_CONFIGURATION);
        assertEquals(0x09, LibUSB.REQUEST_SET_CONFIGURATION);
        assertEquals(0x0a, LibUSB.REQUEST_GET_INTERFACE);
        assertEquals(0x0b, LibUSB.REQUEST_SET_INTERFACE);
        assertEquals(0x0c, LibUSB.REQUEST_SYNCH_FRAME);
        assertEquals(0x30, LibUSB.REQUEST_SET_SEL);
        assertEquals(0x31, LibUSB.SET_ISOCH_DELAY);

        // Request type
        assertEquals(0x00 << 5, LibUSB.REQUEST_TYPE_STANDARD);
        assertEquals(0x01 << 5, LibUSB.REQUEST_TYPE_CLASS);
        assertEquals(0x02 << 5, LibUSB.REQUEST_TYPE_VENDOR);
        assertEquals(0x03 << 5, LibUSB.REQUEST_TYPE_RESERVED);

        // Recipient bits
        assertEquals(0x00, LibUSB.RECIPIENT_DEVICE);
        assertEquals(0x01, LibUSB.RECIPIENT_INTERFACE);
        assertEquals(0x02, LibUSB.RECIPIENT_ENDPOINT);
        assertEquals(0x03, LibUSB.RECIPIENT_OTHER);

        // Error codes
        assertEquals(0, LibUSB.SUCCESS);
        assertEquals(-1, LibUSB.ERROR_IO);
        assertEquals(-2, LibUSB.ERROR_INVALID_PARAM);
        assertEquals(-3, LibUSB.ERROR_ACCESS);
        assertEquals(-4, LibUSB.ERROR_NO_DEVICE);
        assertEquals(-5, LibUSB.ERROR_NOT_FOUND);
        assertEquals(-6, LibUSB.ERROR_BUSY);
        assertEquals(-7, LibUSB.ERROR_TIMEOUT);
        assertEquals(-8, LibUSB.ERROR_OVERFLOW);
        assertEquals(-9, LibUSB.ERROR_PIPE);
        assertEquals(-10, LibUSB.ERROR_INTERRUPTED);
        assertEquals(-11, LibUSB.ERROR_NO_MEM);
        assertEquals(-12, LibUSB.ERROR_NOT_SUPPORTED);
        assertEquals(-99, LibUSB.ERROR_OTHER);

        // Capabilities
        assertEquals(0, LibUSB.CAP_HAS_CAPABILITY);

        // Device and/or Interface class codes
        assertEquals(0, LibUSB.CLASS_PER_INTERFACE);
        assertEquals(1, LibUSB.CLASS_AUDIO);
        assertEquals(2, LibUSB.CLASS_COMM);
        assertEquals(3, LibUSB.CLASS_HID);
        assertEquals(5, LibUSB.CLASS_PHYSICAL);
        assertEquals(7, LibUSB.CLASS_PRINTER);
        assertEquals(6, LibUSB.CLASS_PTP);
        assertEquals(6, LibUSB.CLASS_IMAGE);
        assertEquals(8, LibUSB.CLASS_MASS_STORAGE);
        assertEquals(9, LibUSB.CLASS_HUB);
        assertEquals(10, LibUSB.CLASS_DATA);
        assertEquals(0x0b, LibUSB.CLASS_SMART_CARD);
        assertEquals(0x0d, LibUSB.CLASS_CONTENT_SECURITY);
        assertEquals(0x0e, LibUSB.CLASS_VIDEO);
        assertEquals(0x0f, LibUSB.CLASS_PERSONAL_HEALTHCARE);
        assertEquals(0xdc, LibUSB.CLASS_DIAGNOSTIC_DEVICE);
        assertEquals(0xe0, LibUSB.CLASS_WIRELESS);
        assertEquals(0xfe, LibUSB.CLASS_APPLICATION);
        assertEquals(0xff, LibUSB.CLASS_VENDOR_SPEC);

        // Descriptor types
        assertEquals(0x01, LibUSB.DT_DEVICE);
        assertEquals(0x02, LibUSB.DT_CONFIG);
        assertEquals(0x03, LibUSB.DT_STRING);
        assertEquals(0x04, LibUSB.DT_INTERFACE);
        assertEquals(0x05, LibUSB.DT_ENDPOINT);
        assertEquals(0x21, LibUSB.DT_HID);
        assertEquals(0x22, LibUSB.DT_REPORT);
        assertEquals(0x23, LibUSB.DT_PHYSICAL);
        assertEquals(0x29, LibUSB.DT_HUB);
        assertEquals(0x2a, LibUSB.DT_SUPERSPEED_HUB);

        // Endpoint direction
        assertEquals(0x80, LibUSB.ENDPOINT_IN);
        assertEquals(0x00, LibUSB.ENDPOINT_OUT);

        // Transfer types
        assertEquals(0, LibUSB.TRANSFER_TYPE_CONTROL);
        assertEquals(1, LibUSB.TRANSFER_TYPE_ISOCHRONOUS);
        assertEquals(2, LibUSB.TRANSFER_TYPE_BULK);
        assertEquals(3, LibUSB.TRANSFER_TYPE_INTERRUPT);

        // ISO Sync types
        assertEquals(0, LibUSB.ISO_SYNC_TYPE_NONE);
        assertEquals(1, LibUSB.ISO_SYNC_TYPE_ASYNC);
        assertEquals(2, LibUSB.ISO_SYNC_TYPE_ADAPTIVE);
        assertEquals(3, LibUSB.ISO_SYNC_TYPE_SYNC);

        // ISO usage types
        assertEquals(0, LibUSB.ISO_USAGE_TYPE_DATA);
        assertEquals(1, LibUSB.ISO_USAGE_TYPE_FEEDBACK);
        assertEquals(2, LibUSB.ISO_USAGE_TYPE_IMPLICIT);
    }

    /**
     * Tests the {@link LibUSB#getVersion()} method.
     */
    @Test
    public void testGetVersion()
    {
        final Version version = LibUSB.getVersion();
        assertNotNull(version);
        assertEquals(1, version.major());
        assertEquals(0, version.minor());
        assertTrue(version.micro() > 0 && version.micro() < 100);
        assertNotNull(version.rc());
        assertTrue(version.toString().startsWith("1.0."));
    }

    /**
     * Tests the initialization and deinitialization of libusb with default
     * context.
     */
    @Test
    public void testInitDeinitWithDefaultContext()
    {
        assertEquals(LibUSB.SUCCESS, LibUSB.init(null));
        LibUSB.exit(null);
    }

    /**
     * Tests the initialization and deinitialization of libusb with a custom
     * context.
     */
    @Test
    public void testInitDeinitWithContext()
    {
        Context context = new Context();
        assertEquals(LibUSB.SUCCESS, LibUSB.init(context));
        LibUSB.exit(context);
    }

    /**
     * Checks the {@link LibUSB#le16ToCpu(int)} and
     * {@link LibUSB#cpuToLe16(int)} methods.
     */
    @Test
    public void testEndianConversion()
    {
        assertEquals(0x1234, LibUSB.le16ToCpu(LibUSB.cpuToLe16(0x1234)));
    }

    /**
     * Tests the {@link LibUSB#hasCapability(int)} method.
     */
    @Test
    public void testHasCapability()
    {
        assertTrue(LibUSB.hasCapability(LibUSB.CAP_HAS_CAPABILITY));
        assertFalse(LibUSB.hasCapability(0x12345678));
    }

    /**
     * Tests the {@link LibUSB#errorName(int)} method.
     */
    @Test
    public void testErrorName()
    {
        assertEquals("LIBUSB_SUCCESS", LibUSB.errorName(LibUSB.SUCCESS));
        assertEquals("LIBUSB_ERROR_IO", LibUSB.errorName(LibUSB.ERROR_IO));
        assertEquals("**UNKNOWN**", LibUSB.errorName(0x1234));
    }
}
