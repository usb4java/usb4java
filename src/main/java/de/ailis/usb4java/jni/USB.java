/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * This is the main class of the usb4java JNI wrapper. It wraps the global
 * functions of libusb 0.x and defines all the constants. Usage is the same as
 * documented by libusb.
 *
 * A common error is passing indirect buffers to the methods provided here.
 * You always must use direct buffers which can be obtained by 
 * ByteBuffer.allocateDirect()
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB
{
    /** The logger. */
    private static final Logger LOG = Logger.getLogger("USB");

    /** The maximum size of a descriptor. */
    private static final int MAX_DESCRIPTOR_SIZE = 256;

    /** The maximum size of a string. */
    private static final int MAX_STRING_SIZE = (MAX_DESCRIPTOR_SIZE - 2) / 2;


    // === USB class constants ===============================================

    /** Per interface class. */
    public static final int USB_CLASS_PER_INTERFACE = 0;

    /** Audio class. */
    public static final int USB_CLASS_AUDIO = 1;

    /** Comm class. */
    public static final int USB_CLASS_COMM = 2;

    /** HID class. */
    public static final int USB_CLASS_HID = 3;

    /** PTP class. */
    public static final int USB_CLASS_PTP = 6;

    /** Printer class. */
    public static final int USB_CLASS_PRINTER = 7;

    /** Mass storage class. */
    public static final int USB_CLASS_MASS_STORAGE = 8;

    /** HUB class. */
    public static final int USB_CLASS_HUB = 9;

    /** Data class. */
    public static final int USB_CLASS_DATA = 10;

    /** Vendor specific class. */
    public static final int USB_CLASS_VENDOR_SPEC = 0xff;


    // === Device descriptor type constants ==================================


    /** Device descriptor type. */
    public static final int USB_DT_DEVICE = 0x01;

    /** Config descriptor type. */
    public static final int USB_DT_CONFIG = 0x02;

    /** String descriptor type. */
    public static final short USB_DT_STRING = 0x03;

    /** Interface descriptor type. */
    public static final int USB_DT_INTERFACE = 0x04;

    /** Endpoint descriptor type. */
    public static final int USB_DT_ENDPOINT = 0x05;

    /** HID descriptor type. */
    public static final int USB_DT_HID = 0x21;

    /** Report descriptor type. */
    public static final int USB_DT_REPORT = 0x22;

    /** Physical descriptor type. */
    public static final int USB_DT_PHYSICAL = 0x23;

    /** Hub descriptor type. */
    public static final int USB_DT_HUB = 0x29;


    // === Descriptor sizes per descriptor type ==============================

    /** Device type descriptor size. */
    public static final int USB_DT_DEVICE_SIZE = 18;

    /** Config type descriptor size. */
    public static final int USB_DT_CONFIG_SIZE = 9;

    /** Interface type descriptor size. */
    public static final int USB_DT_INTERFACE_SIZE = 9;

    /** Endpoint type descriptor size. */
    public static final int USB_DT_ENDPOINT_SIZE = 7;

    /** Audio endpoint type descriptor size. */
    public static final int USB_DT_ENDPOINT_AUDIO_SIZE = 9;

    /** Hub Non-Var type descriptor size. */
    public static final int USB_DT_HUB_NONVAR_SIZE = 7;


    // === Standard requests =================================================

    /** Get status request. */
    public static final int USB_REQ_GET_STATUS = 0x00;

    /** Clear feature request. */
    public static final int USB_REQ_CLEAR_FEATURE = 0x01;

    /** Set feature request. */
    public static final int USB_REQ_SET_FEATURE = 0x03;

    /** Set address request. */
    public static final int USB_REQ_SET_ADDRESS = 0x05;

    /** Get descriptor request. */
    public static final int USB_REQ_GET_DESCRIPTOR = 0x06;

    /** Set descriptor request. */
    public static final int USB_REQ_SET_DESCRIPTOR = 0x07;

    /** Get configuration request. */
    public static final int USB_REQ_GET_CONFIGURATION = 0x08;

    /** Set configuration request. */
    public static final int USB_REQ_SET_CONFIGURATION = 0x09;

    /** Get interface request. */
    public static final int USB_REQ_GET_INTERFACE = 0x0a;

    /** Set interface request. */
    public static final int USB_REQ_SET_INTERFACE = 0x0b;

    /** Synch frame request. */
    public static final int USB_REQ_SYNCH_FRAME = 0x0c;


    // === Request types =====================================================

    /** Standard request type. */
    public static final int USB_TYPE_STANDARD = 0;

    /** Class request type. */
    public static final int USB_TYPE_CLASS = 32;

    /** Vendor request type. */
    public static final int USB_TYPE_VENDOR = 64;

    /** Reserved request type. */
    public static final int USB_TYPE_RESERVED = 96;


    // === Request recipients ================================================

    /** Device recipient. */
    public static final int USB_RECIP_DEVICE = 0x00;

    /** Interface recipient. */
    public static final int USB_RECIP_INTERFACE = 0x01;

    /** Endpoint recipient. */
    public static final int USB_RECIP_ENDPOINT = 0x02;

    /** Other recipient. */
    public static final int USB_RECIP_OTHER = 0x03;


    // === Request directions ================================================

    /** Device-to-host (IN) direction. */
    public static final int USB_ENDPOINT_IN = 0x80;

    /** Host-to-Device (OUT) direction. */
    public static final int USB_ENDPOINT_OUT = 0x00;


    // === Masks =============================================================

    /** Endpoint address mask. */
    public static final int USB_ENDPOINT_ADDRESS_MASK = 0x0f;

    /** Endpoint direction mask. */
    public static final int USB_ENDPOINT_DIR_MASK = 0x80;

    /** Endpoint type mask. */
    public static final int USB_ENDPOINT_TYPE_MASK = 0x03;


    // === Endpoint types ====================================================

    /** Endpoint control type. */
    public static final int USB_ENDPOINT_TYPE_CONTROL = 0;

    /** Endpoint isochronous type. */
    public static final int USB_ENDPOINT_TYPE_ISOCHRONOUS = 1;

    /** Endpoint bulk type. */
    public static final int USB_ENDPOINT_TYPE_BULK = 2;

    /** Endpoint interrupt type. */
    public static final int USB_ENDPOINT_TYPE_INTERRUPT = 3;

    /** Possible library names we try to load. */
    private static String[] libNames = new String[] { "usb4java", "usb4java32",
        "libusb4java", "libusb4java32" };

    static
    {
        final List<Throwable> errors = new ArrayList<Throwable>();
        for (final String libName : libNames)
        {
            try
            {
                System.loadLibrary(libName);
                errors.clear();
                break;
            }
            catch (final Throwable e)
            {
                errors.add(e);
            }
        }
        if (!errors.isEmpty())
        {
            final StringWriter out = new StringWriter();
            for (final Throwable error: errors)
            {
                error.printStackTrace(new PrintWriter(out));
            }
            LOG.severe(out.toString());
            throw new RuntimeException(
                "Unable to load JNI library of usb4java");
        }
    }


    /**
     * Private constructor to prevent instantiation.
     */

    private USB()
    {
        // Empty
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

    public static native USB_Dev_Handle usb_open(final USB_Device device);


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

    public static native int usb_close(final USB_Dev_Handle handle);


    /**
     * Sets the active configuration of a device.
     *
     * usb_set_configuration sets the active configuration of a device. The
     * configuration parameter is the value as specified in the descriptor field
     * bConfigurationValue. Returns 0 on success or < 0 on error.
     *
     * @param handle
     *            The USB device handle.
     * @param configuration
     *            The configuration to activate.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_set_configuration(final USB_Dev_Handle handle,
        final int configuration);


    /**
     * Sets the active alternate setting of the current interface Description.
     *
     * usb_set_altinterface sets the active alternate setting of the current
     * interface. The alternate parameter is the value as specified in the
     * descriptor field bAlternateSetting. Returns 0 on success or < 0 on error.
     *
     * @param handle
     *            The USB device handle.
     * @param alternate
     *            The alternate setting to activate.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_set_altinterface(final USB_Dev_Handle handle,
        final int alternate);


    /**
     * Clears any halt status on an endpoint.
     *
     * usb_clear_halt clears any halt status on the specified endpoint. The ep
     * parameter is the value specified in the descriptor field
     * bEndpointAddress. Returns 0 on success or < 0 on error.
     *
     * @param handle
     *            The USB device handle.
     * @param ep
     *            The endpoint.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_clear_halt(final USB_Dev_Handle handle,
        final int ep);


    /**
     * Resets a device.
     *
     * usb_reset resets the specified device by sending a RESET down the port it
     * is connected to. Returns 0 on success or < 0 on error.
     *
     * Causes re-enumeration: After calling usb_reset, the device will need to
     * re-enumerate and thusly, requires you to find the new device and open a
     * new handle. The handle used to call usb_reset will no longer work.
     *
     * @param handle
     *            The USB device handle.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_reset(final USB_Dev_Handle handle);


    /**
     * Claim an interface of a device.
     *
     * usb_claim_interface claims the interface with the Operating System. The
     * interface parameter is the value as specified in the descriptor field
     * bInterfaceNumber. Returns 0 on success or < 0 on error.
     *
     * Must be called!: usb_claim_interface must be called before you perform
     * any operations related to this interface (like usb_set_altinterface,
     * usb_bulk_write, etc).
     *
     * @param handle
     *            The USB device handle.
     * @param iface
     *            The interface to claim.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_claim_interface(final USB_Dev_Handle handle,
        final int iface);


    /**
     * Releases a previously claimed interface.
     *
     * usb_release_interface releases an interface previously claimed with
     * usb_claim_interface. The interface parameter is the value as specified in
     * the descriptor field bInterfaceNumber. Returns 0 on success or < 0 on
     * error.
     *
     * @param handle
     *            The USB device handle.
     * @param iface
     *            The interface to release.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_release_interface(final USB_Dev_Handle handle,
        final int iface);


    /**
     * Send a control message to a device.
     *
     * usb_control_msg performs a control request to the default control pipe on
     * a device. The parameters mirror the types of the same name in the USB
     * specification. Returns number of bytes written/read or < 0 on error.
     *
     * @param handle
     *            The device handle.
     * @param requesttype
     *            The request type (unsigned byte).
     *            <ul>
     *            <li>Bit 7: Data transfer direction (0=Host-to-device,
     *            1=Device-to-host). See USB_ENDPOINT_IN and USB_ENDPOINT_OUT
     *            constants.</li>
     *            <li>Bit 6-5: Type (0=Standard, 1=Class, 2=Vendor, 3=Reserved).
     *            See USB_TYPE_* constants.</li>
     *            <li>Bit 4-0: Recipient (0=Device, 1=Interface, 2=Endpoint,
     *            3=Other, 4-31=Reserved. See USB_RECIP_* constants.</li>
     *            </ul>
     * @param request
     *            The specific request (unsigned byte). See USB_REQ_* constants
     *            for some standard requests.
     * @param value
     *            The value (unsigned short).
     * @param index
     *            The index or offset (unsigned short).
     * @param bytes
     *            The bytes to transfer. This must be a direct buffer
     *            obtained for example by ByteBuffer.allocateDirect().
     * @param timeout
     *            The timeout in milliseconds.
     * @return The number of bytes written/read or < 0 on error.
     */

    public static native int usb_control_msg(final USB_Dev_Handle handle,
        final int requesttype, final int request, final int value,
        final int index, final ByteBuffer bytes, final int timeout);


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
     *            The buffer to write the string to. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @return The number of bytes read or < 0 on error.
     */

    public static native int usb_get_string(final USB_Dev_Handle handle,
        final int index, final int langid, final ByteBuffer buffer);


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

    public static USB_String_Descriptor usb_get_string(
        final USB_Dev_Handle handle,
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

    public static USB_String_Descriptor usb_get_string(
        final USB_Dev_Handle handle, final int index, final int langid)
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
     *            The buffer to write the string to. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @return The number of bytes read or < 0 on error.
     */

    public static native int usb_get_string_simple(final USB_Dev_Handle handle,
        final int index, final ByteBuffer buffer);


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
        final USB_String_Descriptor descriptor = usb_get_string(handle, index,
            langid, size * 2);
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
        if (len < 2) return null;
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
     *            The buffer to put the read bytes in. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @return Number of bytes read or < 0 on error.
     */

    public static native int usb_get_descriptor(final USB_Dev_Handle handle,
        final int type, final int index, final ByteBuffer buffer);


    /**
     * Retrieves a descriptor from a device.
     *
     * usb_get_descriptor_by_endpoint retrieves a descriptor from the device
     * identified by the type and index of the descriptor from the control pipe
     * identified by ep. Returns number of bytes read for the descriptor or < 0
     * on error.
     *
     * @param handle
     *            The device handle.
     * @param ep
     *            The endpoint.
     * @param type
     *            The descriptor type.
     * @param index
     *            The descriptor index.
     * @param buffer
     *            The buffer to put the read bytes in. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @return Number of bytes read or < 0 on error.
     */

    public static native int usb_get_descriptor_by_endpoint(
        final USB_Dev_Handle handle, final int ep, final int type,
        final int index, final ByteBuffer buffer);


    /**
     * Write data to a bulk endpoint.
     *
     * usb_bulk_write performs a bulk write request to the endpoint specified by
     * ep. Returns number of bytes written on success or < 0 on error.
     *
     * @param handle
     *            The device handle.
     * @param ep
     *            The endpoint.
     * @param bytes
     *            The bytes to write. This must be a direct buffer
     *            obtained for example by ByteBuffer.allocateDirect().
     * @param timeout
     *            The timeout in milliseconds.
     * @return Bytes written on success or < 0 on error.
     */

    public static native int usb_bulk_write(final USB_Dev_Handle handle,
        final int ep, final ByteBuffer bytes, final int timeout);


    /**
     * Read data from a bulk endpoint.
     *
     * usb_bulk_read performs a bulk read request to the endpoint specified by
     * ep. Returns number of bytes read on success or < 0 on error.
     *
     * @param handle
     *            The device handle.
     * @param ep
     *            The endpoint.
     * @param bytes
     *            The read bytes. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @param timeout
     *            The timeout in milliseconds.
     * @return Bytes read on success or < 0 on error.
     */

    public static native int usb_bulk_read(final USB_Dev_Handle handle,
        final int ep, final ByteBuffer bytes, final int timeout);


    /**
     * Write data to an interrupt endpoint.
     *
     * usb_interrupt_write performs an interrupt write request to the endpoint
     * specified by ep. Returns number of bytes written on success or < 0 on
     * error.
     *
     * @param handle
     *            The device handle.
     * @param ep
     *            The endpoint.
     * @param bytes
     *            The bytes to write.
     * @param timeout
     *            The timeout in milliseconds.
     * @return Bytes written on success or < 0 on error.
     */

    public static native int usb_interrupt_write(final USB_Dev_Handle handle,
        final int ep, final ByteBuffer bytes, final int timeout);


    /**
     * Read data from an interrupt endpoint.
     *
     * usb_interrupt_read performs a interrupt read request to the endpoint
     * specified by ep. Returns number of bytes read on success or < 0 on error.
     *
     * @param handle
     *            The device handle.
     * @param ep
     *            The endpoint.
     * @param bytes
     *            The read bytes. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @param timeout
     *            The timeout in milliseconds.
     * @return Bytes read on success or < 0 on error.
     */

    public static native int usb_interrupt_read(final USB_Dev_Handle handle,
        final int ep, final ByteBuffer bytes, final int timeout);


    /**
     * Returns the last error message.
     *
     * @return The last error message.
     */

    public static native String usb_strerror();


    /**
     * Get driver name bound to interface.
     *
     * This function will obtain the name of the driver bound to the interface
     * specified by the parameter interface and place it into the specified
     * buffer. Returns 0 on success or < 0 on error.
     *
     * Implemented on Linux only. On other platforms an UnsatisfiedLinkError
     * exception will be thrown.
     *
     * @param handle
     *            The USB device handle.
     * @param iface
     *            The interface number.
     * @param buffer
     *            The buffer to place the name in. This must be a direct
     *            buffer obtained for example by ByteBuffer.allocateDirect().
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_get_driver_np(final USB_Dev_Handle handle,
        final int iface, final ByteBuffer buffer);


    /**
     * Get driver name bound to interface.
     *
     * This function will obtain the name of the driver bound to the interface
     * specified by the parameter interface.
     *
     * Implemented on Linux only. On other platforms an UnsatisfiedLinkError
     * exception will be thrown.
     *
     * @param handle
     *            The USB device handle.
     * @param iface
     *            The interface number.
     * @return The driver name or null on error.
     */

    public static String usb_get_driver_np(final USB_Dev_Handle handle,
        final int iface)
    {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_STRING_SIZE);
        final int result = usb_get_driver_np(handle, iface, buffer);
        if (result < 0) return null;
        buffer.rewind();
        int size = 0;
        while (buffer.get() != 0)
            size++;
        buffer.rewind();
        final byte[] bytes = new byte[size];
        buffer.get(bytes, 0, size);
        return new String(bytes);
    }


    /**
     * Detach kernel driver from interface.
     *
     * This function will detach a kernel driver from the interface specified by
     * parameter interface. Applications using libusb can then try claiming the
     * interface. Returns 0 on success or < 0 on error.
     *
     * Implemented on Linux only. On other platforms an UnsatisfiedLinkError
     * exception will be thrown.
     *
     * @param handle
     *            The device handle.
     * @param iface
     *            The interface number.
     * @return 0 on success or < 0 on error.
     */

    public static native int usb_detach_kernel_driver_np(
        final USB_Dev_Handle handle, final int iface);


    /**
     * Checks if libusb supports the usb_get_driver_np function.
     *
     * @return True if support is present, false if not.
     */

    public static native boolean libusb_has_get_driver_np();


    /**
     * Checks if libusb supports the usb_detach_kernel_driver_np function.
     *
     * @return True if support is present, false if not.
     */

    public static native boolean libusb_has_detach_kernel_driver_np();
}
