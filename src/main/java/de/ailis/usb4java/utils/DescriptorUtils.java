/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.utils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbInterfaceDescriptor;

import de.ailis.usb4java.libusb.LibUsb;

/**
 * Utility methods used for descriptor dumps.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DescriptorUtils
{
    /** Mapping from USB class id to USB class name. */
    private static final Map<Integer, String> CLASS_NAMES =
        new HashMap<Integer, String>();

    static
    {
        CLASS_NAMES.put(LibUsb.CLASS_PER_INTERFACE, "Per interface");
        CLASS_NAMES.put(LibUsb.CLASS_AUDIO, "Audio");
        CLASS_NAMES.put(LibUsb.CLASS_COMM, "Communications");
        CLASS_NAMES.put(LibUsb.CLASS_HID, "HID");
        CLASS_NAMES.put(LibUsb.CLASS_IMAGE, "Imaging");
        CLASS_NAMES.put(LibUsb.CLASS_PRINTER, "Printer");
        CLASS_NAMES.put(LibUsb.CLASS_MASS_STORAGE, "Mass Storage");
        CLASS_NAMES.put(LibUsb.CLASS_HUB, "Hub");
        CLASS_NAMES.put(LibUsb.CLASS_DATA, "Data");
        CLASS_NAMES.put(LibUsb.CLASS_SMART_CARD, "Smart Card");
        CLASS_NAMES.put(LibUsb.CLASS_CONTENT_SECURITY, "Content Security");
        CLASS_NAMES.put(LibUsb.CLASS_VIDEO, "Video");
        CLASS_NAMES.put(LibUsb.CLASS_VENDOR_SPEC, "Vendor-specific");
        CLASS_NAMES.put(LibUsb.CLASS_APPLICATION, "Application");
        CLASS_NAMES.put(LibUsb.CLASS_PERSONAL_HEALTHCARE,
            "Personal Healthcare");
        CLASS_NAMES.put(LibUsb.CLASS_DIAGNOSTIC_DEVICE, "Diagnostic Device");
        CLASS_NAMES.put(LibUsb.CLASS_WIRELESS, "Wireless");

    }

    /**
     * Private constructor to prevent instantiation.
     */
    private DescriptorUtils()
    {
        // Empty
    }

    /**
     * Returns the name of the specified USB class. "unknown" is returned for a
     * class which is unknown to libusb.
     * 
     * @param usbClass
     *            The numeric USB class.
     * @return The USB class name.
     */
    public static String getUSBClassName(final int usbClass)
    {
        final String name = CLASS_NAMES.get(usbClass);
        if (name == null) return "Unknown";
        return name;
    }

    /**
     * Decodes a binary-coded decimal into a string and returns it.
     * 
     * @param bcd
     *            The binary-coded decimal to decode.
     * @return The decoded binary-coded decimal.
     */
    public static String decodeBCD(final int bcd)
    {
        return String.format("%x.%02x", (bcd & 0xff00) >> 8, bcd & 0xff);
    }

    /**
     * Dumps the specified byte buffer into a hex string and returns it.
     * 
     * @param bytes
     *            The bytes to dump.
     * @return The hex dump.
     */
    public static String dump(final ByteBuffer bytes)
    {
        final int columns = 16;
        bytes.rewind();
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        while (bytes.hasRemaining())
        {
            if (i % columns != 0)
                builder.append(' ');
            else if (i >= columns)
                builder.append(String.format("%n"));
            builder.append(String.format("%02x", bytes.get()));
            i++;
        }
        return builder.toString();
    }
    
    /**
     * Dumps the specified USB device descriptor into a string and
     * returns it.
     * 
     * @param descriptor
     *            The USB device descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbDeviceDescriptor descriptor)
    {
        return String.format("Device Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bcdDevice %16s%n"
            + "  bDeviceClass %13d%n"
            + "  bDeviceSubClass %10d%n"
            + "  bDeviceProtocol %10d%n"
            + "  bMaxPacketSize0 %10d%n"
            + "  idVendor %17s%n"
            + "  idProduct %16s%n"
            + "  bcdDevice %16s%n"
            + "  iManufacturer %12d%n"
            + "  iProduct %17d%n"
            + "  iSerial %18d%n"
            + "  bNumConfigurations %7d",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            decodeBCD(descriptor.bcdUSB()),
            descriptor.bDeviceClass() & 0xff,
            descriptor.bDeviceSubClass() & 0xff,
            descriptor.bDeviceProtocol() & 0xff,
            descriptor.bMaxPacketSize0() & 0xff,
            String.format("0x%04x", descriptor.idVendor() & 0xffff),
            String.format("0x%04x", descriptor.idProduct() & 0xffff),
            decodeBCD(descriptor.bcdDevice()),
            descriptor.iManufacturer() & 0xff,
            descriptor.iProduct() & 0xff,
            descriptor.iSerialNumber() & 0xff,
            descriptor.bNumConfigurations() & 0xff);
    }
        
    /**
     * Dumps the specified USB configuration descriptor into a string and
     * returns it.
     * 
     * @param descriptor
     *            The USB configuration descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbConfigurationDescriptor descriptor)
    {
        return String.format("Configuration Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  wTotalLength %13d%n"
            + "  bNumInterfaces %11d%n"
            + "  bConfigurationValue %6d%n"
            + "  iConfiguration %11d%n"
            + "  bmAttributes %13s%n"
            + "  bMaxPower %16smA",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.wTotalLength() & 0xffff,
            descriptor.bNumInterfaces() & 0xff,
            descriptor.bConfigurationValue() & 0xff,
            descriptor.iConfiguration() & 0xff,
            String.format("0x%02x", descriptor.bmAttributes() & 0xff),
            (descriptor.bMaxPower() & 0xff) * 2);
    }
    
    /**
     * Dumps the specified USB interface descriptor into a string and
     * returns it.
     * 
     * @param descriptor
     *            The USB interface descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbInterfaceDescriptor descriptor)
    {
        return String.format("Interface Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bInterfaceNumber %9d%n"
            + "  bAlternateSetting %8d%n"
            + "  bNumEndpoints %12d%n"
            + "  bInterfaceClass %10d%n"
            + "  bInterfaceSubClass %7d%n"
            + "  bInterfaceProtocol %7d%n"
            + "  iInterface %15d",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.bInterfaceNumber() & 0xff,
            descriptor.bAlternateSetting() & 0xff,
            descriptor.bNumEndpoints() & 0xff,
            descriptor.bInterfaceClass() & 0xff,
            descriptor.bInterfaceSubClass() & 0xff,
            descriptor.bInterfaceProtocol() & 0xff,
            descriptor.iInterface() & 0xff);
    }
    
    /**
     * Dumps the specified USB endpoint descriptor into a string and
     * returns it.
     * 
     * @param descriptor
     *            The USB endpoint descriptor to dump.
     * @return The descriptor dump.
     */
    public static String dump(final UsbEndpointDescriptor descriptor)
    {
        return String.format("Endpoint Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bEndpointAddress %9s%n"
            + "  bmAttributes %13d%n"
            + "  wMaxPacketSize %11d%n"
            + "  bInterval %16d",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            String.format("0x%02x", descriptor.bEndpointAddress() & 0xff),
            descriptor.bmAttributes() & 0xff,
            descriptor.wMaxPacketSize() & 0xffff,
            descriptor.bInterval() & 0xff);
    }

    /**
     * Returns the name for the specified speed number.
     * 
     * @param speed
     *            The speed number.
     * @return The speed name.
     */
    public static String getSpeedName(final int speed)
    {
        switch (speed)
        {
            case LibUsb.SPEED_SUPER:
                return "Super";
            case LibUsb.SPEED_FULL:
                return "Full";
            case LibUsb.SPEED_HIGH:
                return "High";
            case LibUsb.SPEED_LOW:
                return "Low";
            default:
                return "Unknown";
        }
    }
}
