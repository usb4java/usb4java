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

import java.nio.ByteBuffer;

import javax.usb.UsbDeviceDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    /** The native data of the descriptor structure. */
    private ByteBuffer deviceDescriptorData;

    /**
     * Constructs a new device descriptor which can be passed to the
     * {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)} method.
     */
    public DeviceDescriptor()
    {
        // Empty
    }

    /**
     * Returns the native data of the descriptor structure.
     * 
     * @return The native data.
     */
    public ByteBuffer getData()
    {
        return this.deviceDescriptorData;
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
        final String sManufacturer = LibUsb.getStringDescriptor(handle, 
            iManufacturer());
        final String sProduct = LibUsb.getStringDescriptor(handle, iProduct());
        final String sSerialNumber = LibUsb.getStringDescriptor(handle, 
            iSerialNumber());
        return DescriptorUtils.dump(this, sManufacturer, sProduct, 
            sSerialNumber);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final DeviceDescriptor other = (DeviceDescriptor) obj;
        return new EqualsBuilder()
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bLength(), other.bLength())
            .append(idProduct(), other.idProduct())
            .append(idVendor(), other.idVendor())
            .append(bcdDevice(), other.bcdDevice())
            .append(bcdUSB(), other.bcdUSB())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(bDeviceClass(), other.bDeviceClass())
            .append(bDeviceProtocol(), other.bDeviceProtocol())
            .append(bDeviceSubClass(), other.bDeviceSubClass())
            .append(bLength(), other.bLength())
            .append(bMaxPacketSize0(), other.bMaxPacketSize0())
            .append(bNumConfigurations(), other.bNumConfigurations())
            .append(iManufacturer(), other.iManufacturer())
            .append(iProduct(), other.iProduct())
            .append(iSerialNumber(), other.iSerialNumber()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bLength())
            .append(bDescriptorType())
            .append(bcdUSB())
            .append(bDeviceClass())
            .append(bDeviceSubClass())
            .append(bDeviceProtocol())
            .append(bMaxPacketSize0())
            .append(idVendor())
            .append(idProduct())
            .append(bcdDevice())
            .append(iManufacturer())
            .append(iProduct())
            .append(iSerialNumber())
            .append(bNumConfigurations()).toHashCode();
    }

    @Override
    public String toString()
    {
        return dump();
    }
}
