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
 * Tests the {@link Version} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class VersionTest
{
    /** The test subject. */
    private Version version;

    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        assumeUsbTestsEnabled();
        LibUsb.init(null);
        this.version = new Version();
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
     * {@link Version#major()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMajor()
    {
        assumeUsbTestsEnabled();
        this.version.major();
    }

    /**
     * Tests uninitialized access to
     * {@link Version#minor()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMinor()
    {
        assumeUsbTestsEnabled();
        this.version.minor();
    }

    /**
     * Tests uninitialized access to
     * {@link Version#micro()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMicro()
    {
        assumeUsbTestsEnabled();
        this.version.micro();
    }

    /**
     * Tests uninitialized access to
     * {@link Version#rc()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedRc()
    {
        assumeUsbTestsEnabled();
        this.version.rc();
    }
}
