/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.descriptors;

import javax.usb.UsbDeviceDescriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.libusb4java.DeviceDescriptor;
import org.libusb4java.utils.DescriptorUtils;

/**
 * Simple USB device descriptor.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbDeviceDescriptor extends SimpleUsbDescriptor
    implements UsbDeviceDescriptor
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The USB specification version number. */
    private final short bcdUSB;

    /** The device class. */
    private final byte bDeviceClass;

    /** The device sub class. */
    private final byte bDeviceSubClass;

    /** The device protocol. */
    private final byte bDeviceProtocol;

    /** The maximum packet size for endpoint zero. */
    private final byte bMaxPacketSize0;

    /** The vendor ID. */
    private final short idVendor;

    /** The product ID. */
    private final short idProduct;

    /** The device release number. */
    private final short bcdDevice;

    /** The manufacturer string descriptor index. */
    private final byte iManufacturer;

    /** The product string descriptor index. */
    private final byte iProduct;

    /** The serial number string descriptor index. */
    private final byte iSerialNumber;

    /** The number of configurations. */
    private final byte bNumConfigurations;

    /**
     * Constructor.
     * 
     * @param bLength
     *            The descriptor length.
     * @param bDescriptorType
     *            The descriptor type.
     * @param bcdUSB
     *            The USB specification version number.
     * @param bDeviceClass
     *            The device class.
     * @param bDeviceSubClass
     *            The device sub class.
     * @param bDeviceProtocol
     *            The device protocol.
     * @param bMaxPacketSize0
     *            The maximum packet size for endpoint zero.
     * @param idVendor
     *            The vendor ID.
     * @param idProduct
     *            The product ID.
     * @param bcdDevice
     *            The device release number.
     * @param iManufacturer
     *            The manufacturer string descriptor index.
     * @param iProduct
     *            The product string descriptor index.
     * @param iSerialNumber
     *            The serial number string descriptor index.
     * @param bNumConfigurations
     *            The number of configurations.
     */
    public SimpleUsbDeviceDescriptor(final byte bLength,
        final byte bDescriptorType, final short bcdUSB,
        final byte bDeviceClass, final byte bDeviceSubClass,
        final byte bDeviceProtocol, final byte bMaxPacketSize0,
        final short idVendor, final short idProduct, final short bcdDevice,
        final byte iManufacturer, final byte iProduct,
        final byte iSerialNumber, final byte bNumConfigurations)
    {
        super(bLength, bDescriptorType);
        this.bcdUSB = bcdUSB;
        this.bDeviceClass = bDeviceClass;
        this.bDeviceSubClass = bDeviceSubClass;
        this.bDeviceProtocol = bDeviceProtocol;
        this.bMaxPacketSize0 = bMaxPacketSize0;
        this.idVendor = idVendor;
        this.idProduct = idProduct;
        this.bcdDevice = bcdDevice;
        this.iManufacturer = iManufacturer;
        this.iProduct = iProduct;
        this.iSerialNumber = iSerialNumber;
        this.bNumConfigurations = bNumConfigurations;
    }

    /**
     * Construct from a libusb4java device descriptor.
     * 
     * @param descriptor
     *            The descriptor from which to copy the data.
     */
    public SimpleUsbDeviceDescriptor(DeviceDescriptor descriptor)
    {
        this(descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.bcdUSB(),
            descriptor.bDeviceClass(),
            descriptor.bDeviceSubClass(),
            descriptor.bDeviceProtocol(),
            descriptor.bMaxPacketSize0(),
            descriptor.idVendor(),
            descriptor.idProduct(),
            descriptor.bcdDevice(),
            descriptor.iManufacturer(),
            descriptor.iProduct(),
            descriptor.iSerialNumber(),
            descriptor.bNumConfigurations());
    }

    @Override
    public short bcdUSB()
    {
        return this.bcdUSB;
    }

    @Override
    public byte bDeviceClass()
    {
        return this.bDeviceClass;
    }

    @Override
    public byte bDeviceSubClass()
    {
        return this.bDeviceSubClass;
    }

    @Override
    public byte bDeviceProtocol()
    {
        return this.bDeviceProtocol;
    }

    @Override
    public byte bMaxPacketSize0()
    {
        return this.bMaxPacketSize0;
    }

    @Override
    public short idVendor()
    {
        return this.idVendor;
    }

    @Override
    public short idProduct()
    {
        return this.idProduct;
    }

    @Override
    public short bcdDevice()
    {
        return this.bcdDevice;
    }

    @Override
    public byte iManufacturer()
    {
        return this.iManufacturer;
    }

    @Override
    public byte iProduct()
    {
        return this.iProduct;
    }

    @Override
    public byte iSerialNumber()
    {
        return this.iSerialNumber;
    }

    @Override
    public byte bNumConfigurations()
    {
        return this.bNumConfigurations;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(bDescriptorType())
            .append(bLength())
            .append(this.bDeviceClass)
            .append(this.bDeviceProtocol)
            .append(this.bDeviceSubClass)
            .append(this.bMaxPacketSize0)
            .append(this.bNumConfigurations)
            .append(this.bcdDevice)
            .append(this.bcdUSB)
            .append(this.iManufacturer)
            .append(this.iProduct)
            .append(this.iSerialNumber)
            .append(this.idProduct)
            .append(this.idVendor)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SimpleUsbDeviceDescriptor other = (SimpleUsbDeviceDescriptor) obj;
        return new EqualsBuilder()
            .append(bLength(), other.bLength())
            .append(bDescriptorType(), other.bDescriptorType())
            .append(this.bDeviceClass, other.bDeviceClass)
            .append(this.bDeviceProtocol, other.bDeviceProtocol)
            .append(this.bDeviceSubClass, other.bDeviceSubClass)
            .append(this.bMaxPacketSize0, other.bMaxPacketSize0)
            .append(this.bNumConfigurations, other.bNumConfigurations)
            .append(this.bcdDevice, other.bcdDevice)
            .append(this.bcdUSB, other.bcdUSB)
            .append(this.iManufacturer, other.iManufacturer)
            .append(this.iProduct, other.iProduct)
            .append(this.iSerialNumber, other.iSerialNumber)
            .append(this.idProduct, other.idProduct)
            .append(this.idVendor, other.idVendor)
            .isEquals();
    }

    @Override
    public String toString()
    {
        return dump(this);
    }

    /**
     * Dumps the specified USB device descriptor into a string and returns it.
     *
     * @param descriptor
     *            The USB device descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbDeviceDescriptor descriptor)
    {
        return String.format(
            "Device Descriptor:%n" +
            "  bLength %18d%n" +
            "  bDescriptorType %10d%n" +
            "  bcdUSB %19s%n" +
            "  bDeviceClass %13d %s%n" +
            "  bDeviceSubClass %10d%n" +
            "  bDeviceProtocol %10d%n" +
            "  bMaxPacketSize0 %10d%n" +
            "  idVendor %17s%n" +
            "  idProduct %16s%n" +
            "  bcdDevice %16s%n" +
            "  iManufacturer %12d%n" +
            "  iProduct %17d%n" +
            "  iSerial %18d%n" +
            "  bNumConfigurations %7d%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            DescriptorUtils.decodeBCD(descriptor.bcdUSB()),
            descriptor.bDeviceClass() & 0xff,
            DescriptorUtils.getUSBClassName(descriptor.bDeviceClass()),
            descriptor.bDeviceSubClass() & 0xff,
            descriptor.bDeviceProtocol() & 0xff,
            descriptor.bMaxPacketSize0() & 0xff,
            String.format("0x%04x", descriptor.idVendor() & 0xffff),
            String.format("0x%04x", descriptor.idProduct() & 0xffff),
            DescriptorUtils.decodeBCD(descriptor.bcdDevice()),
            descriptor.iManufacturer() & 0xff,
            descriptor.iProduct() & 0xff,
            descriptor.iSerialNumber() & 0xff,
            descriptor.bNumConfigurations() & 0xff);
    }
}
