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

import com.sun.jna.Structure;

/**
 * A structure representing the standard USB device descriptor.
 *
 * This descriptor is documented in section 9.6.1 of the USB 3.0 specification. All multiple-byte fields are represented
 * in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class NativeDeviceDescriptor extends Structure {
    public byte bLength;
    public byte bDescriptorType;
    public short bcdUSB;
    public byte bDeviceClass;
    public byte bDeviceSubClass;
    public byte bDeviceProtocol;
    public byte bMaxPacketSize0;
    public short idVendor;
    public short idProduct;
    public short bcdDevice;
    public byte iManufacturer;
    public byte iProduct;
    public byte iSerialNumber;
    public byte bNumConfigurations;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "bLength", "bDescriptorType", "bcdUSB", "bDeviceClass", "bDeviceSubClass",
            "bDeviceProtocol", "bMaxPacketSize0", "idVendor", "idProduct", "bcdDevice", "iManufacturer", "iProduct",
            "iSerialNumber", "bNumConfigurations" });
    }
}
