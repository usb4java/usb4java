/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.test;

import static org.junit.Assume.assumeTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * USB-related assumptions.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class UsbAssume
{
    /** If USB tests are to be executed. */
    private static Boolean usbTests;
    
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
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        int lines = 0;
                        while (reader.readLine() != null) lines++;
                        usbTests = lines >= 2;
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
        assumeTrue("This test is ignored when USB_TESTS property is not set",
             usbTests);
    }
}
