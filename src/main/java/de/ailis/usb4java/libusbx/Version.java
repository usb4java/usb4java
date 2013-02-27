/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.libusbx;

import java.nio.ByteBuffer;

/**
 * Structure providing the version of the libusbx runtime.
 */
public final class Version
{
    /** The version data. */
    private final ByteBuffer data;

    /**
     * Constructor.
     *
     * @param data
     *            The version data.
     */
    private Version(final ByteBuffer data)
    {
        this.data = data;
    }

    /**
     * Returns the library major version.
     * 
     * @return The library major version.
     */
    public native int major();
    
    /**
     * Returns the library minor version.
     * 
     * @return The library minor version.
     */
    public native int minor();
    
    /**
     * Returns the library micro version.
     * 
     * @return The library micro version.
     */
    public native int micro();
    
    /**
     * Returns the release candidate suffix string, e.g. "-rc4".
     * 
     * @return The release candidate suffix string.
     */
    public native String rc();   

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return major() + "." + minor() + "." + micro() + rc();
    }
}
