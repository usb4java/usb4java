/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import de.ailis.usb4java.libusb.Interface;

/**
 * USB interface.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * 
 * @deprecated Use the new libusb 1.0 API or the JSR 80 API.
 */
@Deprecated
public final class USB_Interface
{
    /** The number of available interface descriptors. */
    private final int num_altsetting;

    /** The available interface descriptors. */
    private final USB_Interface_Descriptor[] altsetting;

    /**
     * Constructor.
     * 
     * @param iface
     *            The low-level interface structure.
     */
    USB_Interface(Interface iface)
    {
        this.num_altsetting = iface.numAltsetting();
        this.altsetting = new USB_Interface_Descriptor[this.num_altsetting];
        for (int i = 0; i < this.num_altsetting; i++)
        {
            this.altsetting[i] =
                new USB_Interface_Descriptor(iface.altsetting()[i]);
        }
    }

    /**
     * Returns the array with all available interface descriptors.
     * 
     * @return The array with the interface descriptors.
     */
    public USB_Interface_Descriptor[] altsetting()
    {
        return this.altsetting;
    }

    /**
     * Returns the number of available interface descriptors.
     * 
     * @return The number of available interface descriptors.
     */
    public int num_altsetting()
    {
        return this.num_altsetting;
    }

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
    public String dump(final USB_Dev_Handle handle)
    {
        final StringBuilder builder = new StringBuilder();
        for (final USB_Interface_Descriptor descriptor: altsetting())
        {
            builder.append(descriptor.dump(handle));
        }
        return builder.toString();
    }
}
