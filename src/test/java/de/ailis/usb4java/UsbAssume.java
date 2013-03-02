/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import static org.junit.Assume.assumeTrue;

/**
 * USB-related assumptions.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class UsbAssume
{
    /**
     * Assume that USB tests are enabled. Call this in the first line of
     * tests method or preparation methods when you want to ignore the
     * tests when the property USB_TESTS is not set.
     */
    public static void assumeUsbTestsEnabled()
    {
        assumeTrue("This test is ignored when USB_TESTS property is not set",
            System.getProperty("USB_TESTS") != null);
    }
}
