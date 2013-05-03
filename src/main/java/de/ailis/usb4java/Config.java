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

    /** Key name for USB communication timeout. */
    private static final String TIMEOUT_KEY = KEY_BASE + "timeout";

    /** The timeout for USB communication in milliseconds. */
    private int timeout = 2500;

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
}
