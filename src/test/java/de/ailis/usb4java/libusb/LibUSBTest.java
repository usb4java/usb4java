/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static de.ailis.usb4java.UsbAssume.assumeUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the {@link LibUsb} class.
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
        assertEquals(0, LibUsb.LOG_LEVEL_NONE);
        assertEquals(1, LibUsb.LOG_LEVEL_ERROR);
        assertEquals(2, LibUsb.LOG_LEVEL_WARNING);
        assertEquals(3, LibUsb.LOG_LEVEL_INFO);
        assertEquals(4, LibUsb.LOG_LEVEL_DEBUG);

        // Speed codes
        assertEquals(0, LibUsb.SPEED_UNKNOWN);
        assertEquals(1, LibUsb.SPEED_LOW);
        assertEquals(2, LibUsb.SPEED_FULL);
        assertEquals(3, LibUsb.SPEED_HIGH);
        assertEquals(4, LibUsb.SPEED_SUPER);

        // Standard requests
        assertEquals(0x00, LibUsb.REQUEST_GET_STATUS);
        assertEquals(0x01, LibUsb.REQUEST_CLEAR_FEATURE);
        assertEquals(0x03, LibUsb.REQUEST_SET_FEATURE);
        assertEquals(0x05, LibUsb.REQUEST_SET_ADDRESS);
        assertEquals(0x06, LibUsb.REQUEST_GET_DESCRIPTOR);
        assertEquals(0x07, LibUsb.REQUEST_SET_DESCRIPTOR);
        assertEquals(0x08, LibUsb.REQUEST_GET_CONFIGURATION);
        assertEquals(0x09, LibUsb.REQUEST_SET_CONFIGURATION);
        assertEquals(0x0a, LibUsb.REQUEST_GET_INTERFACE);
        assertEquals(0x0b, LibUsb.REQUEST_SET_INTERFACE);
        assertEquals(0x0c, LibUsb.REQUEST_SYNCH_FRAME);
        assertEquals(0x30, LibUsb.REQUEST_SET_SEL);
        assertEquals(0x31, LibUsb.SET_ISOCH_DELAY);

        // Request type
        assertEquals(0x00 << 5, LibUsb.REQUEST_TYPE_STANDARD);
        assertEquals(0x01 << 5, LibUsb.REQUEST_TYPE_CLASS);
        assertEquals(0x02 << 5, LibUsb.REQUEST_TYPE_VENDOR);
        assertEquals(0x03 << 5, LibUsb.REQUEST_TYPE_RESERVED);

        // Recipient bits
        assertEquals(0x00, LibUsb.RECIPIENT_DEVICE);
        assertEquals(0x01, LibUsb.RECIPIENT_INTERFACE);
        assertEquals(0x02, LibUsb.RECIPIENT_ENDPOINT);
        assertEquals(0x03, LibUsb.RECIPIENT_OTHER);

        // Error codes
        assertEquals(0, LibUsb.SUCCESS);
        assertEquals(-1, LibUsb.ERROR_IO);
        assertEquals(-2, LibUsb.ERROR_INVALID_PARAM);
        assertEquals(-3, LibUsb.ERROR_ACCESS);
        assertEquals(-4, LibUsb.ERROR_NO_DEVICE);
        assertEquals(-5, LibUsb.ERROR_NOT_FOUND);
        assertEquals(-6, LibUsb.ERROR_BUSY);
        assertEquals(-7, LibUsb.ERROR_TIMEOUT);
        assertEquals(-8, LibUsb.ERROR_OVERFLOW);
        assertEquals(-9, LibUsb.ERROR_PIPE);
        assertEquals(-10, LibUsb.ERROR_INTERRUPTED);
        assertEquals(-11, LibUsb.ERROR_NO_MEM);
        assertEquals(-12, LibUsb.ERROR_NOT_SUPPORTED);
        assertEquals(-99, LibUsb.ERROR_OTHER);

        // Capabilities
        assertEquals(0, LibUsb.CAP_HAS_CAPABILITY);

        // Device and/or Interface class codes
        assertEquals(0, LibUsb.CLASS_PER_INTERFACE);
        assertEquals(1, LibUsb.CLASS_AUDIO);
        assertEquals(2, LibUsb.CLASS_COMM);
        assertEquals(3, LibUsb.CLASS_HID);
        assertEquals(5, LibUsb.CLASS_PHYSICAL);
        assertEquals(7, LibUsb.CLASS_PRINTER);
        assertEquals(6, LibUsb.CLASS_PTP);
        assertEquals(6, LibUsb.CLASS_IMAGE);
        assertEquals(8, LibUsb.CLASS_MASS_STORAGE);
        assertEquals(9, LibUsb.CLASS_HUB);
        assertEquals(10, LibUsb.CLASS_DATA);
        assertEquals(0x0b, LibUsb.CLASS_SMART_CARD);
        assertEquals(0x0d, LibUsb.CLASS_CONTENT_SECURITY);
        assertEquals(0x0e, LibUsb.CLASS_VIDEO);
        assertEquals(0x0f, LibUsb.CLASS_PERSONAL_HEALTHCARE);
        assertEquals(0xdc, LibUsb.CLASS_DIAGNOSTIC_DEVICE);
        assertEquals(0xe0, LibUsb.CLASS_WIRELESS);
        assertEquals(0xfe, LibUsb.CLASS_APPLICATION);
        assertEquals(0xff, LibUsb.CLASS_VENDOR_SPEC);

        // Descriptor types
        assertEquals(0x01, LibUsb.DT_DEVICE);
        assertEquals(0x02, LibUsb.DT_CONFIG);
        assertEquals(0x03, LibUsb.DT_STRING);
        assertEquals(0x04, LibUsb.DT_INTERFACE);
        assertEquals(0x05, LibUsb.DT_ENDPOINT);
        assertEquals(0x21, LibUsb.DT_HID);
        assertEquals(0x22, LibUsb.DT_REPORT);
        assertEquals(0x23, LibUsb.DT_PHYSICAL);
        assertEquals(0x29, LibUsb.DT_HUB);
        assertEquals(0x2a, LibUsb.DT_SUPERSPEED_HUB);

        // Endpoint direction
        assertEquals(0x80, LibUsb.ENDPOINT_IN);
        assertEquals(0x00, LibUsb.ENDPOINT_OUT);

        // Transfer types
        assertEquals(0, LibUsb.TRANSFER_TYPE_CONTROL);
        assertEquals(1, LibUsb.TRANSFER_TYPE_ISOCHRONOUS);
        assertEquals(2, LibUsb.TRANSFER_TYPE_BULK);
        assertEquals(3, LibUsb.TRANSFER_TYPE_INTERRUPT);

        // ISO Sync types
        assertEquals(0, LibUsb.ISO_SYNC_TYPE_NONE);
        assertEquals(1, LibUsb.ISO_SYNC_TYPE_ASYNC);
        assertEquals(2, LibUsb.ISO_SYNC_TYPE_ADAPTIVE);
        assertEquals(3, LibUsb.ISO_SYNC_TYPE_SYNC);

        // ISO usage types
        assertEquals(0, LibUsb.ISO_USAGE_TYPE_DATA);
        assertEquals(1, LibUsb.ISO_USAGE_TYPE_FEEDBACK);
        assertEquals(2, LibUsb.ISO_USAGE_TYPE_IMPLICIT);
    }

    /**
     * Tests the {@link LibUsb#getVersion()} method.
     */
    @Test
    public void testGetVersion()
    {
        assumeUsbTestsEnabled();
        final Version version = LibUsb.getVersion();
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
        assumeUsbTestsEnabled();
        assertEquals(LibUsb.SUCCESS, LibUsb.init(null));
        LibUsb.exit(null);
    }

    /**
     * Tests the initialization and deinitialization of libusb with a custom
     * context.
     */
    @Test
    public void testInitDeinitWithContext()
    {
        assumeUsbTestsEnabled();
        Context context = new Context();
        assertEquals(LibUsb.SUCCESS, LibUsb.init(context));
        LibUsb.exit(context);
    }
}
