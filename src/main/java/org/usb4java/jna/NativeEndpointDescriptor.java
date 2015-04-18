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

import org.usb4java.LibUsb;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;

/**
 * A structure representing the standard USB endpoint descriptor.
 *
 * This descriptor is documented in section 9.6.6 of the USB 3.0 specification. All multiple-byte fields are represented
 * in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class NativeEndpointDescriptor extends Structure implements ByReference {
    /** The size of this descriptor (in bytes). */
    public byte bLength;

    /** The descriptor type. Will have value {@link LibUsb#DT_ENDPOINT} in this context. */
    public byte bDescriptorType;

    /**
     * The address of the endpoint described by this descriptor. Bits 0:3 are the endpoint number. Bits 4:6 are
     * reserved. Bit 7 indicates direction (Either {@link LibUsb#ENDPOINT_IN} or {@link LibUsb#ENDPOINT_OUT}).
     */
    public byte bEndpointAddress;

    /**
     * Attributes which apply to the endpoint when it is configured using the bConfigurationValue. Bits 0:1 determine
     * the transfer type and correspond to the LibUsb.TRANSFER_TYPE_* constants. Bits 2:3 are only used for isochronous
     * endpoints and correspond to the LibUsb.ISO_SYNC_TYPE_* constants. Bits 4:5 are also only used for isochronous
     * endpoints and correspond to the LibUsb.ISO_USAGE_TYPE_* constants. Bits 6:7 are reserved.
     */
    public byte bmAttributes;

    /** The maximum packet size this endpoint is capable of sending/receiving. */
    public short wMaxPacketSize;

    /** The interval for polling endpoint for data transfers. */
    public byte bInterval;

    /** For audio devices only: the rate at which synchronization feedback is provided. */
    public byte bRefresh;

    /** For audio devices only: the address of the synch endpoint. */
    public byte bSynchAddress;

    /**
     * Extra descriptors. If libusb encounters unknown endpoint descriptors, it will store them here, should you wish to
     * parse them.
     */
    public Pointer extra;

    /** Length of the extra descriptors, in bytes. */
    public int extra_length;
    

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "bLength", "bDescriptorType", "bEndpointAddress", "bmAttributes",
            "wMaxPacketSize", "bInterval", "bRefresh", "bSynchAddress", "extra", "extra_length" });
    }    
}
