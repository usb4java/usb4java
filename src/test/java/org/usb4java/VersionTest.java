/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.usb4java.test.UsbAssume.isUsbTestsEnabled;

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
    /**
     * Setup test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            LibUsb.init(null);
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
     * Tests the {@link Version#major()} method.
     */
    @Test
    public void testMajor()
    {
        assumeUsbTestsEnabled();
        assertTrue(LibUsb.getVersion().major() > 0);
    }

    /**
     * Tests uninitialized access to {@link Version#major()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMajor()
    {
        assumeUsbTestsEnabled();
        new Version().major();
    }

    /**
     * Tests the {@link Version#minor()} method.
     */
    @Test
    public void testMinor()
    {
        assumeUsbTestsEnabled();
        assertTrue(LibUsb.getVersion().minor() >= 0);
    }

    /**
     * Tests uninitialized access to {@link Version#minor()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMinor()
    {
        assumeUsbTestsEnabled();
        new Version().minor();
    }

    /**
     * Tests the {@link Version#micro()} method.
     */
    @Test
    public void testMicro()
    {
        assumeUsbTestsEnabled();
        assertTrue(LibUsb.getVersion().micro() >= 0);
    }

    /**
     * Tests uninitialized access to {@link Version#micro()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedMicro()
    {
        assumeUsbTestsEnabled();
        new Version().micro();
    }

    /**
     * Tests the {@link Version#micro()} method.
     */
    @Test
    public void testRc()
    {
        assumeUsbTestsEnabled();
        assertNotNull(LibUsb.getVersion().rc());
    }

    /**
     * Tests uninitialized access to {@link Version#rc()}
     */
    @Test(expected = IllegalStateException.class)
    public void testUninitializedRc()
    {
        assumeUsbTestsEnabled();
        new Version().rc();
    }
    
    /**
     * Tests the {@link Version#equals(Object)} method. This equals test
     * is not complete because we can't generate a version object with a
     * different LibUSB version.
     */
    @Test
    public void testEquals()
    {
        assumeUsbTestsEnabled();
        Version version = LibUsb.getVersion();
        assertTrue(version.equals(version));
        assertTrue(version.equals(LibUsb.getVersion()));
        assertFalse(version.equals(null));
        assertFalse(version.equals(""));        
    }
    
    /**
     * Tests the {@link Version#hashCode()} method.
     */
    @Test
    public void testHashCode()
    {
        assumeUsbTestsEnabled();
        Version version = LibUsb.getVersion();
        assertEquals(version.hashCode(), version.hashCode());
    }
    
    /**
     * Tests the {@link Version#toString()} method
     */
    @Test
    public void testToString()
    {
        assumeUsbTestsEnabled();
        Version version = LibUsb.getVersion();
        assertNotNull(version.toString());
        assertTrue(version.toString().length() > 0);
    }
    
    /**
     * Tests the {@link Version#getPointer()} method
     */
    @Test
    public void testGetPointer()
    {
        assumeUsbTestsEnabled();
        assertEquals(0, new Version().getPointer());
        assertNotEquals(0, LibUsb.getVersion().getPointer());
    }
}
