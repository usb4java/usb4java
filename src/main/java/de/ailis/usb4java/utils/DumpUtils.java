/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.utils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import de.ailis.usb4java.libusb.LibUSB;

/**
 * Utility functions used for descriptor dumps.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DumpUtils
{
    /** Mapping from USB class id to USB class name. */
    private static final Map<Integer, String> CLASS_NAMES =
        new HashMap<Integer, String>();

    static
    {
        CLASS_NAMES.put(LibUSB.CLASS_PER_INTERFACE, "Per interface");
        CLASS_NAMES.put(LibUSB.CLASS_AUDIO, "Audio");
        CLASS_NAMES.put(LibUSB.CLASS_COMM, "Communications");
        CLASS_NAMES.put(LibUSB.CLASS_HID, "HID");
        CLASS_NAMES.put(LibUSB.CLASS_IMAGE, "Imaging");
        CLASS_NAMES.put(LibUSB.CLASS_PRINTER, "Printer");
        CLASS_NAMES.put(LibUSB.CLASS_MASS_STORAGE, "Mass Storage");
        CLASS_NAMES.put(LibUSB.CLASS_HUB, "Hub");
        CLASS_NAMES.put(LibUSB.CLASS_DATA, "Data");
        CLASS_NAMES.put(LibUSB.CLASS_SMART_CARD, "Smart Card");
        CLASS_NAMES.put(LibUSB.CLASS_CONTENT_SECURITY, "Content Security");
        CLASS_NAMES.put(LibUSB.CLASS_VIDEO, "Video");
        CLASS_NAMES.put(LibUSB.CLASS_VENDOR_SPEC, "Vendor-specific");
        CLASS_NAMES.put(LibUSB.CLASS_APPLICATION, "Application");
        CLASS_NAMES.put(LibUSB.CLASS_PERSONAL_HEALTHCARE,
            "Personal Healthcare");
        CLASS_NAMES.put(LibUSB.CLASS_DIAGNOSTIC_DEVICE, "Diagnostic Device");
        CLASS_NAMES.put(LibUSB.CLASS_WIRELESS, "Wireless");

    }

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
        return String.format("%x.%02x", bcd >> Byte.SIZE, bcd & 0xff);
    }

    /**
     * Returns a hex dump of the specified byte buffer.
     * 
     * @param bytes
     *            The bytes to dump.
     * @return The hex dump.
     */
    public static String toHexDump(final ByteBuffer bytes)
    {
        final int columns = 16;
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
            case LibUSB.SPEED_SUPER:
                return "Super";
            case LibUSB.SPEED_FULL:
                return "Full";
            case LibUSB.SPEED_HIGH:
                return "High";
            case LibUSB.SPEED_LOW:
                return "Low";
            default:
                return "Unknown";
        }
    }
}
