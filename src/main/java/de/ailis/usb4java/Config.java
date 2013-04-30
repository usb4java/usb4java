/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Configuration.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
final class Config
{
    /** Base key name for properties. */
    private static final String KEY_BASE = "de.ailis.usb4java.";

    /** Key name for scanHierarchy property. */
    private static final String SCAN_HIERARCHY_KEY = KEY_BASE + "scanHierarchy";

    /** Key name for vendors filter property. */
    private static final String VENDORS_KEY = KEY_BASE + "vendors";

    /** Key name for product filter property. */
    private static final String PRODUCTS_KEY = KEY_BASE + "products";

    /** Key name for USB communication timeout. */
    private static final String TIMEOUT_KEY = KEY_BASE + "timeout";

    /** If device hierarchy should be scanned. */
    private boolean scanHierarchy;

    /** The vendor filter list or null if not filtering. */
    private Set<Integer> vendors;

    /** The product filter list or null if not filtering. */
    private Set<Integer> products;

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
        // Read scanHierarchy property. If not set then hierarchy scan is
        // disabled on OS X (because libusb always returns a null root_dev
        // on this platform) and true for all other platforms.
        if (properties.containsKey(SCAN_HIERARCHY_KEY))
            this.scanHierarchy = Boolean.parseBoolean(properties.getProperty(
                SCAN_HIERARCHY_KEY));
        else
            this.scanHierarchy =
                !System.getProperty("os.name").startsWith("Mac OS X");

        // Read vendor filter list
        if (properties.containsKey(VENDORS_KEY))
        {
            final Set<Integer> vendors = new HashSet<Integer>();
            final String value = properties.getProperty(VENDORS_KEY);
            for (final String item : value.trim().split("\\s+"))
            {
                final Integer vendor = Integer.valueOf(item, 16);
                vendors.add(vendor);
            }
            this.vendors = Collections.unmodifiableSet(vendors);
        }

        // Read product filter list
        if (properties.containsKey(PRODUCTS_KEY))
        {
            final Set<Integer> products = new HashSet<Integer>();
            final String value = properties.getProperty(PRODUCTS_KEY);
            for (final String item : value.trim().split("\\s+"))
            {
                if (item.trim().isEmpty()) continue;
                final Integer product = Integer.valueOf(item, 16);
                products.add(product);
            }
            this.products = Collections.unmodifiableSet(products);
        }

        // Read the USB communication timeout
        if (properties.containsKey(TIMEOUT_KEY))
        {
            this.timeout = Integer.valueOf(properties.getProperty(TIMEOUT_KEY));
        }
    }

    /**
     * Checks if device hierarchy should be scanned.
     *
     * @return True if device hierarchy should be scanned, false if not.
     */
    public boolean getScanHierarchy()
    {
        return this.scanHierarchy;
    }

    /**
     * Returns the vendor filter list or null if vendors must not be
     * filtered.
     *
     * @return The vendor filter list or null if unfiltered.
     */
    public Set<Integer> getVendors()
    {
        return this.vendors;
    }

    /**
     * Returns the product filter list or null if products must not be
     * filtered.
     *
     * @return The product filter list or null if unfiltered.
     */
    public Set<Integer> getProducts()
    {
        return this.products;
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
