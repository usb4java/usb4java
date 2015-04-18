/*
 * Copyright 2015 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java.jna;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * A structure representing the standard USB configuration descriptor.
 *
 * This descriptor is documented in section 9.6.3 of the USB 3.0 specification. All multiple-byte fields are represented
 * in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class NativeConfigDescriptor extends Structure {
    public NativeConfigDescriptor(final Pointer pointer) {
        super(pointer);
        read();
    }

    /** The size of this descriptor (in bytes). */
    public byte bLength;

    /** The descriptor type. */
    public byte bDescriptorType;

    /** The total length of data. */
    public short wTotalLength;

    /** The number of supported interfaces. */
    public byte bNumInterfaces;

    /** The identifier value. */
    public byte bConfigurationValue;

    /** The index of string descriptor describing this configuration. */
    public byte iConfiguration;

    /** The configuration characteristics. */
    public byte bmAttributes;

    /**
     * The maximum power consumption of the USB device from this bus in this configuration when the device is fully
     * operation. Expressed in units of 2 mA.
     */
    public byte bMaxPower;

    /** The array with interfaces supported by this configuration. */
    public NativeInterface iface;

    /**
     * Extra descriptors.
     *
     * If libusb encounters unknown interface descriptors, it will store them here, should you wish to parse them.
     */
    public Pointer extra;

    /** Length of the extra descriptors, in bytes. */
    public int extra_length;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "bLength", "bDescriptorType", "wTotalLength", "bNumInterfaces",
            "bConfigurationValue", "iConfiguration", "bmAttributes", "bMaxPower", "iface", "extra", "extra_length" });
    }

}
