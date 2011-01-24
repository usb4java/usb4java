/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

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
}
