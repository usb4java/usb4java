/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://www.libusb.org/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2011 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2012 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2012 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2012 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package de.ailis.usb4java.libusb;

import java.nio.ByteBuffer;

import javax.usb.UsbDeviceDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ailis.usb4java.utils.BufferUtils;
import de.ailis.usb4java.utils.DescriptorUtils;

/**
 * A structure representing the standard USB device descriptor.
 *
 * This descriptor is documented in section 9.6.1 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceDescriptor implements UsbDeviceDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long deviceDescriptorPointer;

    /** The Java ByteBuffer which contains the descriptor structure. */
    private final ByteBuffer deviceDescriptorBuffer;

    /**
     * Constructs a new device descriptor which can be passed to the
     * {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)} method.
     */
    public DeviceDescriptor()
    {
        // Assign new buffer.
        this.deviceDescriptorBuffer = BufferUtils.allocateByteBuffer(LibUsb.deviceDescriptorStructSize());
    }

    /**
     * Returns the native pointer.
     *
     * @return The native pointer.
     */
    public long getPointer()
    {
        return this.deviceDescriptorPointer;
    }

    @Override
    public native byte bLength();

    @Override
    public native byte bDescriptorType();

    @Override
    public native short bcdUSB();

    @Override
    public native byte bDeviceClass();

    @Override
    public native byte bDeviceSubClass();

    @Override
    public native byte bDeviceProtocol();

    @Override
    public native byte bMaxPacketSize0();

    @Override
    public native short idVendor();

    @Override
    public native short idProduct();

    @Override
    public native short bcdDevice();

    @Override
    public native byte iManufacturer();

    @Override
    public native byte iProduct();

    @Override
    public native byte iSerialNumber();

    @Override
    public native byte bNumConfigurations();

    /**
     * Returns a dump of this descriptor.
     *
     * @return The descriptor dump.
     */
    public String dump()
    {
        return this.dump(null);
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
        final String sManufacturer = LibUsb.getStringDescriptor(handle,
            this.iManufacturer());
        final String sProduct = LibUsb.getStringDescriptor(handle,
            this.iProduct());
        final String sSerialNumber = LibUsb.getStringDescriptor(handle,
            this.iSerialNumber());
        return DescriptorUtils.dump(this, sManufacturer, sProduct,
            sSerialNumber);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(this.bLength())
            .append(this.bDescriptorType())
            .append(this.bcdUSB())
            .append(this.bDeviceClass())
            .append(this.bDeviceSubClass())
            .append(this.bDeviceProtocol())
            .append(this.bMaxPacketSize0())
            .append(this.idVendor())
            .append(this.idProduct())
            .append(this.bcdDevice())
            .append(this.iManufacturer())
            .append(this.iProduct())
            .append(this.iSerialNumber())
            .append(this.bNumConfigurations())
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }

        final DeviceDescriptor other = (DeviceDescriptor) obj;

        return new EqualsBuilder()
            .append(this.bLength(), other.bLength())
            .append(this.bDescriptorType(), other.bDescriptorType())
            .append(this.bcdUSB(), other.bcdUSB())
            .append(this.bDeviceClass(), other.bDeviceClass())
            .append(this.bDeviceSubClass(), other.bDeviceSubClass())
            .append(this.bDeviceProtocol(), other.bDeviceProtocol())
            .append(this.bMaxPacketSize0(), other.bMaxPacketSize0())
            .append(this.idVendor(), other.idVendor())
            .append(this.idProduct(), other.idProduct())
            .append(this.bcdDevice(), other.bcdDevice())
            .append(this.iManufacturer(), other.iManufacturer())
            .append(this.iProduct(), other.iProduct())
            .append(this.iSerialNumber(), other.iSerialNumber())
            .append(this.bNumConfigurations(), other.bNumConfigurations())
            .isEquals();
    }

    @Override
    public String toString()
    {
        return this.dump();
    }
}
