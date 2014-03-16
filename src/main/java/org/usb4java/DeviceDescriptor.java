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

package org.usb4java;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A structure representing the standard USB device descriptor.
 *
 * This descriptor is documented in section 9.6.1 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceDescriptor
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
        this.deviceDescriptorBuffer = BufferUtils.allocateByteBuffer(
            LibUsb.deviceDescriptorStructSize());
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
    
    /**
     * Returns the Java byte buffer which contains the descriptor structure.
     *
     * @return The descriptor structur buffer.
     */
    public ByteBuffer getBuffer()
    {
        return this.deviceDescriptorBuffer;
    }

    /**
     * Returns the size of this descriptor (in bytes).
     * 
     * @return The size of this descriptor (in bytes). 
     */
    public native byte bLength();

    /**
     * Returns the descriptor type. Will have value {@link LibUsb#DT_DEVICE}
     * in this context. 
     * 
     * @return The descriptor type.
     */
    public native byte bDescriptorType();

    /**
     * Returns the USB specification release number in binary-coded decimal.
     * A value of 0x0200 indicates USB 2.0, 0x0110 indicates USB 1.1, etc.
     * 
     * @return The USB specification release number.
     */    
    public native short bcdUSB();

    /**
     * Returns the USB-IF class code for the device. See LibUSB.CLASS_*
     * constants.
     * 
     * @return The USB-IF class code.
     */
    public native byte bDeviceClass();

    /**
     * Returns the USB-IF subclass code for the device, qualified by the 
     * bDeviceClass value.
     * 
     * @return The USB-IF subclass code.
     */ 
    public native byte bDeviceSubClass();

    /**
     * Returns the USB-IF protocol code for the device, qualified by the 
     * bDeviceClass and bDeviceSubClass values.
     * 
     * @return The USB-IF protocol code.
     */
    public native byte bDeviceProtocol();

    /**
     * Returns the maximum packet size for endpoint 0.
     * 
     * @return The maximum packet site for endpoint 0.
     */
    public native byte bMaxPacketSize0();

    /**
     * Returns the USB-IF vendor ID.
     * 
     * @return The vendor ID
     */
    public native short idVendor();

    /**
     * Returns the USB-IF product ID.
     * 
     * @return The product ID.
     */
    public native short idProduct();

    /**
     * Returns the device release number in binary-coded decimal.
     * 
     * @return The device release number.
     */
    public native short bcdDevice();

    /**
     * Returns the index of the string descriptor describing manufacturer.
     * 
     * @return The manufacturer string descriptor index.
     */
    public native byte iManufacturer();

    /**
     * Returns the index of the string descriptor describing product.
     * 
     * @return The product string descriptor index.
     */
    public native byte iProduct();

    /**
     * Returns the index of the string descriptor containing device serial 
     * number.
     * 
     * @return The serial number string descriptor index.
     */
    public native byte iSerialNumber();

    /**
     * Returns the number of possible configurations.
     * 
     * @return The number of possible configurations.
     */
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
