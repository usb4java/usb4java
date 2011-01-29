/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import static de.ailis.usb4java.USB.usb_get_string_simple;

import java.nio.ByteBuffer;



/**
 * The USB device descriptor contains global information about a USB device and
 * all its configurations. A USB device can only have a single device
 * descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Device_Descriptor extends USB_Descriptor_Header
{
    /**
     * Constructor.
     *
     * @param data
     *            The descriptor data.
     */

    public USB_Device_Descriptor(final ByteBuffer data)
    {
        super(data);
    }


    /**
     * Returns the release number of the USB specification with which the device
     * and its descriptors are compliant. The number is returned as a
     * Binary-Coded Decimal. For example 1.51 is decoded as 0x0151.
     *
     * @return The USB specification release number (unsigned short).
     */

    public native int bcdUSB();


    /**
     * Returns the device class code as assigned by the USB-IF.
     *
     * If the code is set to 0x00 then each interface within a configuration
     * operate independently and specifies its own class information.
     *
     * If the code is between 0x01 and 0xfe then the device supports different
     * class specifications or different interfaces and the interfaces may not
     * operate independently. The code identifies the class definition used for
     * the aggregate interfaces.
     *
     * If the code is set to 0xff then the device class is vendor-specific.
     *
     * @return The device class code (unsigned byte).
     */

    public native int bDeviceClass();


    /**
     * Returns the device sub class code as assigned by the USB-IF.
     *
     * The codes are qualified by the value of the
     * {@link USB_Device_Descriptor#bDeviceClass} field.
     *
     * If the bDeviceClass field is set to zero then this field must also be set
     * to zero.
     *
     * If the bDeviceClass field is not set to 0xff, all values are reserved for
     * assignment by the USB-IF.
     *
     * @return The device subclass code (unsigned byte).
     */

    public native int bDeviceSubClass();


    /**
     * Returns the device protocol code as assigned by the USB-IF.
     *
     * The codes are qualified by the value of the
     * {@link USB_Device_Descriptor#bDeviceClass} and the bDeviceSubClass
     * fields. If a device supports class-specific protocols on a device basis
     * as opposed to an interface basis then this code identifies the protocols
     * that the device uses as defined by the specification of the device class.
     *
     * If set to zero then the device does not use class-specific protocols on a
     * device basis but may use class-specific protocols on an interface basis.
     *
     * If set to 0xff then the device uses a vendor-specific protocol on a
     * device basis.
     *
     * @return The device protocol code (unsigned byte).
     */

    public native int bDeviceProtocol();


    /**
     * Returns the maximum packet size for endpoint zero. Only sizes of 8, 16,
     * 32, or 64 are valid.
     *
     * @return The maximum packet size for endpoint zero (unsigned byte).
     */

    public native int bMaxPacketSize0();


    /**
     * Returns the vendor ID as assigned by the USB-IF.
     *
     * @return The vendor ID (unsigned short).
     */

    public native int idVendor();


    /**
     * Returns the product ID as assigned by the manufacturer.
     *
     * @return The product ID (unsigned short).
     */

    public native int idProduct();


    /**
     * Returns the device release number in binary-coded decimal.
     *
     * @return THe device release number (unsigned short).
     */

    public native int bcdDevice();


    /**
     * Returns the index of the manufacturer string descriptor.
     *
     * @return The index of the manufacturer string descriptor (unsigned byte).
     */

    public native int iManufacturer();


    /**
     * Returns the index of the product string descriptor.
     *
     * @return The index of the product string descriptor (unsigned byte).
     */

    public native int iProduct();


    /**
     * Returns the index of the serial number string descriptor.
     *
     * @return The index of the serial number string descriptor (unsigned byte).
     */

    public native int iSerialNumber();


    /**
     * Returns the number of configurations.
     *
     * @return The number of configurations (unsigned byte).
     */

    public native int bNumConfigurations();


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return toString(null);
    }


    /**
     * Returns a dump of this descriptor.
     *
     * @param handle
     *            The USB device handle for resolving string descriptors. If
     *            null then no strings are resolved.
     * @return The descriptor dump.
     */

    public String toString(final USB_Dev_Handle handle)
    {
        final int iManufacturer = iManufacturer();
        String sManufacturer = handle == null || iManufacturer == 0 ? "" :
            usb_get_string_simple(handle, iManufacturer);
        if (sManufacturer == null) sManufacturer = "";
        final int iProduct = iProduct();
        String sProduct = handle == null || iProduct == 0 ? "" :
            usb_get_string_simple(handle, iProduct);
        if (sProduct == null) sProduct = "";
        final int iSerialNumber = iSerialNumber();
        String sSerialNumber = handle == null || iSerialNumber == 0 ? "" :
            usb_get_string_simple(handle, iSerialNumber);
        if (sSerialNumber == null) sSerialNumber = "";
        return String.format("Device Descriptor:%n" +
            "  bLength               %5d%n" +
            "  bDescriptorType       %5d%n" +
            "  bcdUSB                %5s%n" +
            "  bDeviceClass          %5d %s%n" +
            "  bDeviceSubClass       %5d%n" +
            "  bDeviceProtocol       %5d%n" +
            "  bMaxPacketSize0       %5d%n" +
            "  idVendor             %#06x%n" +
            "  idProduct            %#06x%n" +
            "  bcdDevice             %5s%n" +
            "  iManufacturer         %5d %s%n" +
            "  iProduct              %5d %s%n" +
            "  iSerialNumber         %5d %s%n" +
            "  bNumConfigurations    %5d%n",
            bLength(), bDescriptorType(), USBUtils.decodeBCD(bcdUSB()),
            bDeviceClass(), USBUtils.getUSBClassName(bDeviceClass()),
            bDeviceSubClass(), bDeviceProtocol(),
            bMaxPacketSize0(), idVendor(), idProduct(),
            USBUtils.decodeBCD(bcdDevice()), iManufacturer, sManufacturer,
            iProduct, sProduct, iSerialNumber, sSerialNumber,
            bNumConfigurations());
    }
}
