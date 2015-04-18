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

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * A structure representing the Container ID descriptor.
 *
 * This descriptor is documented in section 9.6.2.3 of the USB 3.0
 * specification. All multiple-byte fields, except UUIDs, are represented in
 * host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class NativeContainerIdDescriptor extends Structure {
    public NativeContainerIdDescriptor(final Pointer pointer) {
        super(pointer);
        read();
    }

    /** The size of this descriptor (in bytes). */
    public byte bLength;

    /** The descriptor type. */
    public byte bDescriptorType;

    /** The device capability type. */
    public byte bDevCapabilityType;

    /** The reserved field. */
    public byte bReserved;

    /** The 128 bit UUID. */
    public Pointer containerId;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "bLength", "bDescriptorType", "bDevCapabilityType", "bReserved",
            "containerId" });
    }
}
