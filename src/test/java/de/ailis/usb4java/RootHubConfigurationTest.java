/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link RootHubConfiguration} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class RootHubConfigurationTest
{
    /** The test subject. */
    private RootHubConfiguration config;

    /**
     * Initialize the test.
     */
    @Before
    public void init()
    {
        this.config = new RootHubConfiguration(Mockito.mock(UsbDevice.class));
    }

    /**
     * Tests the {@link RootHubConfiguration#isActive()} method.
     */
    @Test
    public void testIsActive()
    {
        assertTrue(this.config.isActive());
    }

    /**
     * Tests the {@link RootHubConfiguration#getUsbInterfaces()} method.
     */
    @Test
    public void testGetUsbInterfaces()
    {
        final List<UsbInterface> ifaces = this.config.getUsbInterfaces();
        assertEquals(1, ifaces.size());
        assertNotNull(ifaces.get(0));
    }

    /**
     * Tests the {@link RootHubConfiguration#getUsbInterface(byte)} method.
     */
    @Test
    public void testGetUsbInterface()
    {
        assertNotNull(this.config.getUsbInterface((byte) 0));
        assertNull(this.config.getUsbInterface((byte) 1));
    }

    /**
     * Tests the {@link RootHubConfiguration#containsUsbInterface(byte)} method.
     */
    @Test
    public void testContainsUsbInterface()
    {
        assertTrue(this.config.containsUsbInterface((byte) 0));
        assertFalse(this.config.containsUsbInterface((byte) 1));
    }

    /**
     * Tests the {@link RootHubConfiguration#getUsbDevice()} method.
     */
    @Test
    public void testGetUsbDevice()
    {
        assertNotNull(this.config.getUsbDevice());
    }

    /**
     * Tests the {@link RootHubConfiguration#getUsbConfigurationDescriptor()}
     * method.
     */
    @Test
    public void testGetUsbConfigurationDescriptor()
    {
        assertNotNull(this.config.getUsbConfigurationDescriptor());
    }

    /**
     * Tests the {@link RootHubConfiguration#getConfigurationString()}
     * method.
     */
    @Test
    public void testGetConfigurationString()
    {
        assertNull(this.config.getConfigurationString());
    }
}
