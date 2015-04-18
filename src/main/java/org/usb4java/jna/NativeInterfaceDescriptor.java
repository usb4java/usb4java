/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
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

import org.usb4java.LibUsb;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;

/**
 * A structure representing the standard USB interface descriptor.
 *
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification. All multiple-byte fields are represented
 * in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class NativeInterfaceDescriptor extends Structure implements ByReference {

    /** The size of this descriptor (in bytes). */
    public byte bLength;

    /** The descriptor type. Will have value {@link LibUsb#DT_INTERFACE}. */
    public byte bDescriptorType;

    /** The number of this interface. */
    public byte bInterfaceNumber;

    /** The value used to select this alternate setting for this interface. */
    public byte bAlternateSetting;

    /** The number of endpoints used by this interface (excluding the control endpoint). */
    public byte bNumEndpoints;

    /** The USB-IF class code for this interface. See LibUSB.CLASS_* constants. */
    public byte bInterfaceClass;

    /** The USB-IF subclass code for this interface, qualified by the bInterfaceClass value. */
    public byte bInterfaceSubClass;

    /** The USB-IF protocol code for this interface, qualified by thebInterfaceClass and bInterfaceSubClass values. */
    public byte bInterfaceProtocol;

    /** The index of string descriptor describing this interface. */
    public byte iInterface;

    /** The array with endpoints. */
    public NativeEndpointDescriptor endpoint;

    /**
     * Extra descriptors. If libusb encounters unknown interface descriptors, it will store them here, should you wish
     * to parse them.
     */
    public Pointer extra;

    /** Length of the extra descriptors, in bytes. */
    public int extra_length;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "bLength", "bDescriptorType", "bInterfaceNumber", "bAlternateSetting",
            "bNumEndpoints", "bInterfaceClass", "bInterfaceSubClass", "bInterfaceProtocol", "iInterface", "endpoint",
            "extra", "extra_length" });
    }
}
