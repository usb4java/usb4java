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
 * A structure representing the SuperSpeed USB Device Capability descriptor. This descriptor is documented in section
 * 9.6.2.2 of the USB 3.0 specification.
 *
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class NativeSsUsbDeviceCapabilityDescriptor extends Structure {
    public NativeSsUsbDeviceCapabilityDescriptor(final Pointer pointer) {
        super(pointer);
        read();
    }

    /** The size of this descriptor (in bytes). */
    public byte bLength;

    /** The descriptor type. */
    public byte bDescriptorType;

    /** The device capability type. */
    public byte bDevCapabilityType;

    /** The bitmap of supported device level features. */
    public byte bmAttributes;

    /** The bitmap encoding of the speed supported by this device when operating in SuperSpeed mode. */
    public short wSpeedSupported;

    /** The lowest speed at which all the functionality supported by the device is available to the user. */
    public byte bFunctionalitySupport;

    /** The U1 Device Exit Latency. */
    public byte bU1DevExitLat;

    /** The U2 Device Exit Latency. */
    public short bU2DevExitLat;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "bLength", "bDescriptorType", "bDevCapabilityType", "bmAttributes",
                "wSpeedSupported", "bFunctionalitySupport", "bU1DevExitLat", "bU2DevExitLat" });
    }}
