/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package de.ailis.usb4java.libusb;

import java.nio.ByteBuffer;

/**
 * Utility functions used for descriptor dumps.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
final class DumpUtils
{
    /**
     * Private constructor to prevent instantiation.
     */
    private DumpUtils()
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
    static String getUSBClassName(final int usbClass)
    {
        switch (usbClass)
        {
            case LibUSB.CLASS_PER_INTERFACE:
                return "Per interface";
            case LibUSB.CLASS_AUDIO:
                return "Audio";
            case LibUSB.CLASS_COMM:
                return "Communications";
            case LibUSB.CLASS_HID:
                return "HID";
            case LibUSB.CLASS_IMAGE:
                return "Imaging";
            case LibUSB.CLASS_PRINTER:
                return "Printer";
            case LibUSB.CLASS_MASS_STORAGE:
                return "Mass Storage";
            case LibUSB.CLASS_HUB:
                return "Hub";
            case LibUSB.CLASS_DATA:
                return "Data";
            case LibUSB.CLASS_SMART_CARD:
                return "Smart Card";
            case LibUSB.CLASS_CONTENT_SECURITY:
                return "Content Security";
            case LibUSB.CLASS_VIDEO:
                return "Video";
            case LibUSB.CLASS_VENDOR_SPEC:
                return "Vendor-specific";
            case LibUSB.CLASS_APPLICATION:
                return "Application";
            case LibUSB.CLASS_PERSONAL_HEALTHCARE:
                return "Personal Healthcare";
            case LibUSB.CLASS_DIAGNOSTIC_DEVICE:
                return "Diagnostic Device";
            case LibUSB.CLASS_WIRELESS:
                return "Wireless";
            default:
                return "Unknown";
        }
    }

    /**
     * Decodes a binary-coded decimal into a string and returns it.
     * 
     * @param bcd
     *            The binary-coded decimal to decode.
     * @return The decoded binary-coded decimal.
     */
    static String decodeBCD(final int bcd)
    {
        return String.format("%x.%02x", bcd >> Byte.SIZE, bcd & 0xff);
    }
    
    /**
     * Returns a hex dump of the specified byte buffer.
     *
     * @param bytes
     *            The bytes to dump.
     * @return The hex dump.
     */
    static String toHexDump(final ByteBuffer bytes)
    {
        int columns = 16;
        bytes.rewind();
        if (!bytes.hasRemaining()) return "";
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        while (bytes.hasRemaining())
        {
            i++;
            if (i % columns == 0)
                builder.append(String.format("%02x%n", bytes.get()));
            else
                builder.append(String.format("%02x ", bytes.get()));
        }
        if (i % columns != 0) builder.append(String.format("%n"));
        return builder.toString();
    }    
}
