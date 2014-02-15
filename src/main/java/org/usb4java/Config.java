/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import java.util.Properties;

/**
 * Configuration.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
final class Config
{
    /** Base key name for properties. */
    private static final String KEY_BASE = "de.ailis.usb4java.";
    
    /** The default USB communication timeout in milliseconds. */
    private static final int DEFAULT_TIMEOUT = 2500;
    
    /** The default scan interval in milliseconds. */
    private static final int DEFAULT_SCAN_INTERVAL = 500;

    /** Key name for USB communication timeout. */
    private static final String TIMEOUT_KEY = KEY_BASE + "timeout";

    /** Key name for USB communication timeout. */
    private static final String SCAN_INTERVAL_KEY = KEY_BASE + "scanInterval";

    /** The timeout for USB communication in milliseconds. */
    private int timeout = DEFAULT_TIMEOUT;
    
    /** The scan interval in milliseconds. */
    private int scanInterval = DEFAULT_SCAN_INTERVAL;

    /**
     * Constructs new configuration from the specified properties.
     *
     * @param properties
     *            The properties to read the configuration from.
     */
    Config(final Properties properties)
    {
        // Read the USB communication timeout
        if (properties.containsKey(TIMEOUT_KEY))
        {
            this.timeout = Integer.valueOf(properties.getProperty(TIMEOUT_KEY));
        }

        // Read the USB device scan interval
        if (properties.containsKey(SCAN_INTERVAL_KEY))
        {
            this.scanInterval = Integer.valueOf(properties.getProperty(
                SCAN_INTERVAL_KEY));
        }
    }

    /**
     * Returns the USB communication timeout in milliseconds.
     *
     * @return The USB communication timeout in milliseconds.
     */
    public int getTimeout()
    {
        return this.timeout;
    }

    /**
     * Returns the scan interval in milliseconds.
     *
     * @return The scan interval in milliseconds.
     */
    public int getScanInterval()
    {
        return this.scanInterval;
    }
}
