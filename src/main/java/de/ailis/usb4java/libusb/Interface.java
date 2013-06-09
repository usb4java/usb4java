/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */

package de.ailis.usb4java.libusb;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A collection of alternate settings for a particular USB interface.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Interface
{
    /** The native pointer to the descriptor structure. */
    private long interfacePointer;

    /**
     * Package-private constructor to prevent manual instantiation. Interfaces 
     * are always created by JNI.
     */
    Interface()
    {
        // Empty
    }
    
    /**
     * Returns the native pointer.
     * 
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.interfacePointer;
    }
        
    /**
     * Returns the array with interface descriptors. The length of this array is
     * determined by the {@link #numAltsetting()} field.
     * 
     * @return The array with interface descriptors.
     */
    public native InterfaceDescriptor[] altsetting();

    /**
     * Returns the number of alternate settings that belong to this interface.
     * 
     * @return The number of alternate settings.
     */
    public native int numAltsetting();

    /**
     * Returns a dump of this interface.
     *
     * @return The interface dump.
     */
    public String dump()
    {
        return dump(null);
    }

    /**
     * Returns a dump of this descriptor.
     *
     * @param handle
     *            The USB device handle for resolving string descriptors. If
     *            null then no strings are resolved.
     * @return The descriptor dump.
     */
    public String dump(final DeviceHandle handle)
    {
        final StringBuilder builder = new StringBuilder();
        for (final InterfaceDescriptor descriptor : altsetting())
        {
            builder.append(descriptor.dump());
        }
        return builder.toString();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.interfacePointer).toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Interface other = (Interface) obj;
        return this.interfacePointer == other.interfacePointer;
    }
    
    @Override
    public String toString()
    {
        return dump();
    }
}
