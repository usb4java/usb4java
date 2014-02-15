/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

/**
 * Tests the {@link Config} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class ConfigTest
{    
    /**
     * Tests the default configuration
     */
    @Test
    public void testDefaultConfiguration()
    {
        final Properties properties = new Properties();
        final Config config = new Config(properties);
        assertEquals(2500, config.getTimeout());
        assertEquals(500, config.getScanInterval());
    }
    
    /**
     * Tests the scan interval configuration.
     */
    @Test
    public void testScanIntervalConfiguration()
    {
        final Properties properties = new Properties();
        properties.put("org.usb4java.scanInterval", "123");
        final Config config = new Config(properties);
        assertEquals(123, config.getScanInterval());
    }
    
    /**
     * Tests the timeout configuration.
     */
    @Test
    public void testTimeoutConfiguration()
    {
        final Properties properties = new Properties();
        properties.put("org.usb4java.timeout", "1234");
        final Config config = new Config(properties);
        assertEquals(1234, config.getTimeout());
    }
}
