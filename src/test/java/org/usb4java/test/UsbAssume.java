/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.test;

import static org.junit.Assume.assumeTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.usb4java.LibUsb;

/**
 * USB-related assumptions.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class UsbAssume
{
    /** If USB tests are to be executed. */
    private static Boolean usbTests;

    /** If TCK tests are to be executed. */
    private static Boolean tckTests;
    
    /**
     * Check if USB tests are enabled.
     * 
     * USB tests can be controlled with the system property USB_TESTS. When
     * set to true then USB tests are always run, if set to false then they
     * are never run. If this property is not set then the command-line tool
     * lsusb is called. If this tool returned at least two lines of text then
     * USB tests are enabled. In all other cases they are disabled.
     * 
     * @return True if USB tests are enabled, false if not.
     */
    public static boolean isUsbTestsEnabled()
    {
        if (usbTests == null && System.getProperty("USB_TESTS") != null)
            usbTests = Boolean.valueOf(System.getProperty("USB_TESTS"));
        if (usbTests == null)
        {
            usbTests = false;
            try
            {
                final Process proc = Runtime.getRuntime().exec("lsusb");
                if (proc.waitFor() == 0)
                {
                    InputStream inputStream = proc.getInputStream();
                    try
                    {
                        BufferedReader reader =
                            new BufferedReader(new InputStreamReader(
                                inputStream));
                        try
                        {
                            int lines = 0;
                            while (reader.readLine() != null)
                                lines++;
                            usbTests = lines >= 2;
                        }
                        finally
                        {
                            reader.close();
                        }
                    }
                    finally
                    {
                        inputStream.close();
                    }
                }
            }
            catch (Exception e)
            {
                // Ignored. USB tests are silently disabled when lsusb could
                // not be run.
            }
        }
        return usbTests;
    }

    /**
     * Assume that USB tests are enabled. Call this in the first line of
     * tests method or preparation methods when you want to ignore the
     * tests when the system is not able to run the tests anyway.
     * 
     * USB tests can be controlled with the system property USB_TESTS. When
     * set to true then USB tests are always run, if set to false then they
     * are never run. If this property is not set then the command-line tool
     * lsusb is called. If this tool returned at least two lines of text then
     * USB tests are enabled. In all other cases they are disabled.
     */
    public static void assumeUsbTestsEnabled()
    {
        if (!isUsbTestsEnabled())
        {
            assumeTrue("This test is ignored when USB_TESTS property is not set",
                usbTests);
        }
    }

    /**
     * Assume that libusb supports hotplug on the current system.
     */
    public static void assumeHotplugAvailable()
    {
        assumeUsbTestsEnabled();
        assumeTrue("This test is ignored because libusb doesn't support " +
            "hotplug on this system",
            LibUsb.hasCapability(LibUsb.CAP_HAS_HOTPLUG));
    }

    /**
     * Assume that TCK tests are enabled. Call this in the first line of
     * tests method or preparation methods when you want to ignore the
     * tests when the system is not able to run the TCK tests anyway.
     * 
     * TCK tests can be controlled with the system property TCK_TESTS. When
     * set to true then TCK tests are always run, if set to false then they
     * are never run. If this property is not set then the command-line tool
     * lsusb is called to scan for the TCK hardware. If this tool found the
     * TCK hardware then TCK tests are enabled. In all other cases they are
     * disabled.
     */
    public static void assumeTckTestsEnabled()
    {
        assumeUsbTestsEnabled();
        if (tckTests == null && System.getProperty("TCK_TESTS") != null)
            tckTests = Boolean.valueOf(System.getProperty("TCK_TESTS"));
        if (tckTests == null)
        {
            tckTests = false;
            try
            {
                final Process proc = Runtime.getRuntime().exec("lsusb");
                if (proc.waitFor() == 0)
                {
                    InputStream inputStream = proc.getInputStream();
                    try
                    {
                        BufferedReader reader =
                            new BufferedReader(new InputStreamReader(
                                inputStream));
                        try
                        {
                            String line = reader.readLine();
                            while (line != null)
                            {
                                if (line.contains("0547:1002"))
                                {
                                    tckTests = true;
                                    break;
                                }
                                line = reader.readLine();
                            }
                        }
                        finally
                        {
                            reader.close();
                        }
                    }
                    finally
                    {
                        inputStream.close();
                    }
                }
            }
            catch (Exception e)
            {
                // Ignored. USB tests are silently disabled when lsusb could
                // not be run.
            }
        }
        assumeTrue("This test is ignored when TCK_TESTS property is not set",
            tckTests);
    }
}
