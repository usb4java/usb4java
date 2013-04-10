/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import static de.ailis.usb4java.jni.USB.USB_CLASS_AUDIO;
import static de.ailis.usb4java.jni.USB.USB_CLASS_COMM;
import static de.ailis.usb4java.jni.USB.USB_CLASS_DATA;
import static de.ailis.usb4java.jni.USB.USB_CLASS_HID;
import static de.ailis.usb4java.jni.USB.USB_CLASS_HUB;
import static de.ailis.usb4java.jni.USB.USB_CLASS_MASS_STORAGE;
import static de.ailis.usb4java.jni.USB.USB_CLASS_PER_INTERFACE;
import static de.ailis.usb4java.jni.USB.USB_CLASS_PRINTER;
import static de.ailis.usb4java.jni.USB.USB_CLASS_PTP;
import static de.ailis.usb4java.jni.USB.USB_CLASS_VENDOR_SPEC;

import java.nio.ByteBuffer;

import javax.usb.UsbDescriptor;

/**
 * All standard descriptors have the two fields bLength and bDescriptorType in
 * common. So this base class implements them.
 *
 * @author Klaus Reimer (k@ailis.de)
 * 
 * @deprecated Use the new libusb 1.0 API or the JSR 80 API.
 */
@Deprecated
public abstract class USB_Descriptor_Header
{
    /** The descriptor length. */
    private final int bLength;
    
    /** The descriptor type. */
    private final int bDescriptorType;
    
    /**
     * Constructor.
     *
     * @param desc
     *            The wrapped descriptor.
     */
    USB_Descriptor_Header(final UsbDescriptor desc)
    {
        this.bLength = desc.bLength() & 0xff;
        this.bDescriptorType = desc.bDescriptorType() & 0xff;
    }

    /**
     * Returns the size of the descriptor in bytes.
     *
     * @return The size of the descriptor in bytes (unsigned byte).
     */
    public final int bLength()
    {
        return this.bLength;
    }

    /**
     * Returns the interface descriptor type.
     *
     * @return The interface descriptor type (unsigned byte).
     */
    public final int bDescriptorType()
    {
        return this.bDescriptorType;
    }

    /**
     * Returns the name of the specified USB class. "unknown" is returned for a
     * class which is unknown to libusb.
     *
     * @param usbClass
     *            The numeric USB class.
     * @return The USB class name.
     */
    protected static String getUSBClassName(final int usbClass)
    {
        switch (usbClass)
        {
            case USB_CLASS_PER_INTERFACE:
                return "Per interface";
            case USB_CLASS_AUDIO:
                return "Audio";
            case USB_CLASS_COMM:
                return "Communications";
            case USB_CLASS_HID:
                return "HID";
            case USB_CLASS_PTP:
                return "Imaging";
            case USB_CLASS_PRINTER:
                return "Printer";
            case USB_CLASS_MASS_STORAGE:
                return "Mass Storage";
            case USB_CLASS_HUB:
                return "Hub";
            case USB_CLASS_DATA:
                return "Data";
            case USB_CLASS_VENDOR_SPEC:
                return "Vendor-specific";
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
    protected static String decodeBCD(final int bcd)
    {
        return String.format("%x.%02x", bcd >> Byte.SIZE, bcd & 0xff);
    }

    /**
     * Returns a hex dump of the specified byte buffer.
     *
     * @param bytes
     *            The bytes to dump.
     * @param columns
     *            The number of columns in the hex dump.
     * @return The hex dump.
     */
    protected static String toHexDump(final ByteBuffer bytes, final int columns)
    {
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
