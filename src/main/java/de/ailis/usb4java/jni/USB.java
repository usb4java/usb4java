/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;


/**
 * This is the main class of the usb4java JNI wrapper. It wraps the necessary
 * global functions of libusb 0.x as static methods. Usage is the same as
 * documented by libusb.
 *
 * You can use these classes to access USB at a low level but it is recommended
 * to use javax.usb (JSR80) instead which usb4java implements. To do this put
 * the file javax.usb.resources into the classpath with the following content:
 *
 * javax.usb.services = de.ailis.usb4java.UsbServicesImpl
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB
{
    /** USB_CLASS_PER_INTERFACE constant. */
    public static final short USB_CLASS_PER_INTERFACE = 0;

    /** USB_CLASS_AUDIO constant. */
    public static final short USB_CLASS_AUDIO = 1;

    /** USB_CLASS_COMM constant. */
    public static final short USB_CLASS_COMM = 2;

    /** USB_CLASS_HID constant. */
    public static final short USB_CLASS_HID = 3;

    /** USB_CLASS_PRINTER constant. */
    public static final short USB_CLASS_PRINTER = 7;

    /** USB_CLASS_PTP constant. */
    public static final short USB_CLASS_PTP = 6;

    /** USB_CLASS_MASS_STORAGE constant. */
    public static final short USB_CLASS_MASS_STORAGE = 8;

    /** USB_CLASS_HUB constant. */
    public static final short USB_CLASS_HUB = 9;

    /** USB_CLASS_DATA constant. */
    public static final short USB_CLASS_DATA = 10;

    /** USB_CLASS_VENDOR_SPEC constant. */
    public static final short USB_CLASS_VENDOR_SPEC = 0xff;


    static
    {
        try
        {
            System.loadLibrary("libusb-java");
        }
        catch (final UnsatisfiedLinkError e)
        {
            System.loadLibrary("usb-java");
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

    public static native USBBus usb_get_busses();


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

    public static native USBDevHandle usb_open(USBDevice device);


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

    public static native int usb_close(USBDevHandle handle);


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
     * @param buflen
     *            The maximum number of bytes to read.
     * @return The number of bytes read or < 0 on error.
     */

    public static native int usb_get_string(USBDevHandle handle,
        int index, int langid, byte[] buffer, int buflen);


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
     * @param buflen
     *            The maximum number of bytes to read.
     * @return The number of bytes read or < 0 on error.
     */

    public static native int usb_get_string_simple(USBDevHandle handle,
        int index, byte[] buffer, int buflen);
}
