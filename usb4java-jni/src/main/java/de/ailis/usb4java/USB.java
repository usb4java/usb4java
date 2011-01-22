/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * This is the main class of the usb4java JNI wrapper. It wraps the global
 * functions of libusb 0.x and defines all the constants. Usage is the same as
 * documented by libusb.
 *
 * You can use these classes to access USB at a low level but it is recommended
 * to use javax.usb (JSR80) instead which usb4java implements. To do this put
 * the file javax.usb.resources into the class path with the following content:
 *
 * javax.usb.services = de.ailis.usb4java.UsbServicesImpl
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB
{
    /** The maximum size of a descriptor. */
    private static final int MAX_DESCRIPTOR_SIZE = 256;

    /** The maximum size of a string. */
    private static final int MAX_STRING_SIZE = (MAX_DESCRIPTOR_SIZE - 2) / 2;

    // === USB class constants ===============================================

    /** Per interface class. */
    public static final short USB_CLASS_PER_INTERFACE = 0;

    /** Audio class. */
    public static final short USB_CLASS_AUDIO = 1;

    /** Comm class. */
    public static final short USB_CLASS_COMM = 2;

    /** HID class. */
    public static final short USB_CLASS_HID = 3;

    /** Printer class. */
    public static final short USB_CLASS_PRINTER = 7;

    /** PTP class. */
    public static final short USB_CLASS_PTP = 6;

    /** Mass storage class. */
    public static final short USB_CLASS_MASS_STORAGE = 8;

    /** HUB class. */
    public static final short USB_CLASS_HUB = 9;

    /** Data class. */
    public static final short USB_CLASS_DATA = 10;

    /** Vendor specific class. */
    public static final short USB_CLASS_VENDOR_SPEC = 0xff;


    // === Device descriptor type constants ==================================


    /** Device descriptor type. */
    public static final short USB_DT_DEVICE = 0x01;

    /** Config descriptor type. */
    public static final short USB_DT_CONFIG = 0x02;

    /** String descriptor type. */
    public static final short USB_DT_STRING = 0x03;

    /** Interface descriptor type. */
    public static final short USB_DT_INTERFACE = 0x04;

    /** Endpoint descriptor type. */
    public static final short USB_DT_ENDPOINT = 0x05;

    /** HID descriptor type. */
    public static final short USB_DT_HID = 0x21;

    /** Report descriptor type. */
    public static final short USB_DT_REPORT = 0x22;

    /** Physical descriptor type. */
    public static final short USB_DT_PHYSICAL = 0x23;

    /** Hub descriptor type. */
    public static final short USB_DT_HUB = 0x29;


    // === Descriptor sizes per descriptor type ==============================

    /** Device type descriptor size. */
    public static final short USB_DT_DEVICE_SIZE = 18;

    /** Config type descriptor size. */
    public static final short USB_DT_CONFIG_SIZE = 9;

    /** Interface type descriptor size. */
    public static final short USB_DT_INTERFACE_SIZE = 9;

    /** Endpoint type descriptor size. */
    public static final short USB_DT_ENDPOINT_SIZE = 7;

    /** Audio endpoint type descriptor size. */
    public static final short USB_DT_ENDPOINT_AUDIO_SIZE = 9;

    /** Hub Non-Var type descriptor size. */
    public static final short USB_DT_HUB_NONVAR_SIZE = 7;


    static
    {
        try
        {
            System.loadLibrary("libusb4java");
        }
        catch (final UnsatisfiedLinkError e)
        {
            System.loadLibrary("usb4java");
        }
    }


    /**
     * Initialize libusb.
     *
     * Just like the name implies, usb_init sets up some internal structures.
     * usb_init must be called before any other libusb functions.
     */

    public static native void usb_init();


    /**
     * Finds all USB busses on system.
     *
     * usb_find_busses will find all of the busses on the system. Returns the
     * number of changes since previous call to this function (total of new
     * busses and busses removed).
     *
     * @return The number of changes since previous call.
     */

    public static native int usb_find_busses();


    /**
     * Find all devices on all USB devices.
     *
     * usb_find_devices will find all of the devices on each bus. This should be
     * called after usb_find_busses. Returns the number of changes since the
     * previous call to this function (total of new device and devices removed).
     *
     * @return The number of changes since previous call.
     */

    public static native int usb_find_devices();


    /**
     * Return the list of USB busses found.
     *
     * usb_get_busses simply returns the value of the global variable
     * usb_busses. This was implemented for those languages that support C
     * calling convention and can use shared libraries, but don't support C
     * global variables (like Delphi).
     *
     * @return The list of USB busses found.
     */

    public static native USB_Bus usb_get_busses();


    /**
     * Opens a USB device.
     *
     * usb_open is to be used to open up a device for use. usb_open must be
     * called before attempting to perform any operations to the device. Returns
     * a handle used in future communication with the device.
     *
     * @param device
     *            The USB device.
     * @return The USB device handle.
     */

    public static native USB_Dev_Handle usb_open(USB_Device device);


    /**
     * Closes a USB device.
     *
     * usb_close closes a device opened with usb_open. No further operations may
     * be performed on the handle after usb_close is called. Returns 0 on
     * success or < 0 on error.
     *
     * @param handle
     *            The USB device handle.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_close(USB_Dev_Handle handle);


    /**
     * Retrieves a string descriptor from a device.
     *
     * usb_get_string retrieves the string descriptor specified by index and
     * langid from a device. The string will be returned in Unicode as specified
     * by the USB specification. Returns the number of bytes returned in buf or
     * < 0 on error.
     *
     * @param handle
     *            The USB device handle.
     * @param index
     *            The string description index.
     * @param langid
     *            The language id.
     * @param buffer
     *            The buffer to write the string to.
     * @return The number of bytes read or < 0 on error.
     */

    public static native int usb_get_string(USB_Dev_Handle handle,
        int index, int langid, ByteBuffer buffer);


    /**
     * Returns a string descriptor from a device.
     *
     * @param handle
     *            The USB device handle.
     * @param index
     *            The string description index.
     * @param langid
     *            The language id.
     * @param size
     *            The maximum number of bytes to read.
     * @return The string descriptor or null if an error occurred.
     */

    public static USB_String_Descriptor usb_get_string(final USB_Dev_Handle handle,
        final int index, final int langid, final int size)
    {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        final int len = usb_get_string(handle, index, langid, buffer);
        if (len < 0) return null;
        return new USB_String_Descriptor(buffer);
    }


    /**
     * Returns a string descriptor from a device.
     *
     * @param handle
     *            The USB device handle.
     * @param index
     *            The string description index.
     * @param langid
     *            The language id.
     * @return The string descriptor or null if an error occurred.
     */

    public static USB_String_Descriptor usb_get_string(final USB_Dev_Handle handle,
        final int index, final int langid)
    {
        return usb_get_string(handle, index, langid, MAX_STRING_SIZE * 2);
    }


    /**
     * Retrieves a string descriptor from a device using the first language.
     *
     * usb_get_string_simple is a wrapper around usb_get_string that retrieves
     * the string description specified by index in the first language for the
     * descriptor and converts it into C style ASCII. Returns number of bytes
     * returned in buf or < 0 on error.
     *
     * @param handle
     *            The USB device handle.
     * @param index
     *            The string description index.
     * @param buffer
     *            The buffer to write the string to.
     * @return The number of bytes read or < 0 on error.
     */

    public static native int usb_get_string_simple(USB_Dev_Handle handle,
        int index, ByteBuffer buffer);


    /**
     * Returns a string descriptor from a device using the first language.
     * Unlike the native usb_get_string_simple() function this method returns
     * the string in correct unicode encoding.
     *
     * @param handle
     *            The USB device handle.
     * @param index
     *            The string description index.
     * @param size
     *            The maximum number of characters to read.
     * @return The string or null if an error occurred.
     */

    public static String usb_get_string_simple(final USB_Dev_Handle handle,
        final int index, final int size)
    {
        final short[] languages = usb_get_languages(handle);
        if (languages == null) return null;
        final short langid = languages.length == 0 ? 0 : languages[0];
        final USB_String_Descriptor descriptor = usb_get_string(handle, index, langid, size * 2);
        if (descriptor == null) return null;
        return descriptor.toString();
    }


    /**
     * Returns a string descriptor from a device using the first language.
     * Unlike the native usb_get_string_simple() function this method returns
     * the string in correct unicode encoding.
     *
     * @param handle
     *            The USB device handle.
     * @param index
     *            The string description index.
     * @return The string or null if an error occurred.
     */

    public static String usb_get_string_simple(final USB_Dev_Handle handle,
        final int index)
    {
        return usb_get_string_simple(handle, index, MAX_STRING_SIZE);
    }


    /**
     * Returns the languages the specified device supports.
     *
     * @param handle
     *            The USB device handle.
     * @return Array with supported language codes.
     */

    public static short[] usb_get_languages(final USB_Dev_Handle handle)
    {
        final ByteBuffer buffer = ByteBuffer
                .allocateDirect(MAX_DESCRIPTOR_SIZE);
        final int len = usb_get_descriptor(handle, USB_DT_STRING, 0, buffer);
        if (len < 0) return null;
        final short[] languages = new short[(len - 2) / 2];
        if (languages.length == 0) return languages;
        buffer.position(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(languages);
        return languages;
    }


    /**
     * Retrieves a descriptor from a device's default control pipe.
     *
     * usb_get_descriptor retrieves a descriptor from the device identified by
     * the type and index of the descriptor from the default control pipe.
     * Returns number of bytes read for the descriptor or < 0 on error.
     *
     * @param handle
     *            The device handle.
     * @param type
     *            The descriptor type.
     * @param index
     *            The descriptor index.
     * @param buffer
     *            The buffer to put the read bytes in.
     * @return Number of bytes read or < 0 on error.
     */

    public static native int usb_get_descriptor(USB_Dev_Handle handle,
        int type, int index, ByteBuffer buffer);
}
