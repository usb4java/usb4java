/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;


/**
 * USB device descriptor.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB_Device_Descriptor
{
    /** The low-level pointer to the C structure. */
    final long pointer;


    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Device_Descriptor(final long pointer)
    {
        this.pointer = pointer;
    }

    public native short bLength();

    public native short bDescriptorType();

    public native int bcdUSB();

    public native short bDeviceClass();

    public native short bDeviceSubClass();

    public native short bDeviceProtocol();

    public native short bMaxPacketSize0();

    public native int idVendor();

    public native int idProduct();

    public native int bcdDevice();

    public native short iManufacturer();

    public native short iProduct();

    public native short iSerialNumber();

    public native short bNumConfigurations();
}
