/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */

package de.ailis.usb4java.libusb;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Static class providing the constants and functions of libusbx.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class LibUSB
{
    // Log message levels.

    /** No messages ever printed by the library (default). */
    public static final int LOG_LEVEL_NONE = 0;

    /** Error messages are printed to stderr. */
    public static final int LOG_LEVEL_ERROR = 1;

    /** Warning and error messages are printed to stderr. */
    public static final int LOG_LEVEL_WARNING = 2;

    /**
     * Informational messages are printed to stdout, warning and error messages
     * are printed to stderr.
     */
    public static final int LOG_LEVEL_INFO = 3;

    /**
     * Debug and informational messages are printed to stdout, warnings and
     * errors to stderr.
     */
    public static final int LOG_LEVEL_DEBUG = 4;

    // Error codes. Most libusbx functions return 0 on success or one of these
    // codes on failure. You can call errorName() to retrieve a string
    // representation of an error code.

    /** Success (no error). */
    public static final int SUCCESS = 0;

    /** Input/output error. */
    public static final int ERROR_IO = -1;

    /** Invalid parameter. */
    public static final int ERROR_INVALID_PARAM = -2;

    /** Access denied (insufficient permissions). */
    public static final int ERROR_ACCESS = -3;

    /** No such device (it may have been disconnected). */
    public static final int ERROR_NO_DEVICE = -4;

    /** Entity not found. */
    public static final int ERROR_NOT_FOUND = -5;

    /** Resource busy. */
    public static final int ERROR_BUSY = -6;

    /** Operation timed out. */
    public static final int ERROR_TIMEOUT = -7;

    /** Overflow. */
    public static final int ERROR_OVERFLOW = -8;

    /** Pipe error. */
    public static final int ERROR_PIPE = -9;

    /** System call interrupted (perhaps due to signal). */
    public static final int ERROR_INTERRUPTED = -10;

    /** Insufficient memory. */
    public static final int ERROR_NO_MEM = -11;

    /** Operation not supported or unimplemented on this platform. */
    public static final int ERROR_NOT_SUPPORTED = -12;

    /** Other error. */
    public static final int ERROR_OTHER = -99;

    // Speed codes. Indicates the speed at which the device is operating.

    /** The OS doesn't report or know the device speed. */
    public static final int SPEED_UNKNOWN = 0;

    /** The device is operating at low speed (1.5MBit/s). */
    public static final int SPEED_LOW = 1;

    /** The device is operating at full speed (12MBit/s). */
    public static final int SPEED_FULL = 2;

    /** The device is operating at high speed (480MBit/s). */
    public static final int SPEED_HIGH = 3;

    /** The device is operating at super speed (5000MBit/s). */
    public static final int SPEED_SUPER = 4;

    // Standard requests, as defined in table 9-5 of the USB 3.0 specifications.

    /** Request status of the specific recipient. */
    public static final int REQUEST_GET_STATUS = 0x00;

    /** Clear or disable a specific feature. */
    public static final int REQUEST_CLEAR_FEATURE = 0x01;

    /** Set or enable a specific feature. */
    public static final int REQUEST_SET_FEATURE = 0x03;

    /** Set device address for all future accesses. */
    public static final int REQUEST_SET_ADDRESS = 0x05;

    /** Set device address for all future accesses. */
    public static final int REQUEST_GET_DESCRIPTOR = 0x06;

    /** Set device address for all future accesses. */
    public static final int REQUEST_SET_DESCRIPTOR = 0x07;

    /** Get the current device configuration value. */
    public static final int REQUEST_GET_CONFIGURATION = 0x08;

    /** Get the current device configuration value. */
    public static final int REQUEST_SET_CONFIGURATION = 0x09;

    /** Return the selected alternate setting for the specified interface. */
    public static final int REQUEST_GET_INTERFACE = 0x0a;

    /** Select an alternate interface for the specified interface. */
    public static final int REQUEST_SET_INTERFACE = 0x0b;

    /** Set then report an endpoint's synchronization frame. */
    public static final int REQUEST_SYNCH_FRAME = 0x0c;

    /** Sets both the U1 and U2 Exit Latency. */
    public static final int REQUEST_SET_SEL = 0x30;

    /**
     * Delay from the time a host transmits a packet to the time it is received
     * by the device.
     */
    public static final int SET_ISOCH_DELAY = 0x31;

    // Request type bits of the bmRequestType field in control transfers.

    /** Standard. */
    public static final int REQUEST_TYPE_STANDARD = 0;

    /** Class. */
    public static final int REQUEST_TYPE_CLASS = 32;

    /** Vendor. */
    public static final int REQUEST_TYPE_VENDOR = 64;

    /** Reserved. */
    public static final int REQUEST_TYPE_RESERVED = 96;

    // Recipient bits of the bmRequestType field in control transfers.
    // Values 4 through 31 are reserved.

    /** Device. */
    public static final int RECIPIENT_DEVICE = 0x00;

    /** Interface. */
    public static final int RECIPIENT_INTERFACE = 0x01;

    /** Endpoint. */
    public static final int RECIPIENT_ENDPOINT = 0x02;

    /** Other. */
    public static final int RECIPIENT_OTHER = 0x03;

    // Capabilities supported by this instance of libusb. Test if the loaded
    // library supports a given capability by calling hasCapability().

    /** The hasCapability() API is available. */
    public static final int CAP_HAS_CAPABILITY = 0x00;

    // Device and/or Interface Class codes.

    /**
     * In the context of a device descriptor, this bDeviceClass value indicates
     * that each interface specifies its own class information and all
     * interfaces operate independently.
     */
    public static final int CLASS_PER_INTERFACE = 0;

    /** Audio class. */
    public static final int CLASS_AUDIO = 1;

    /** Communications class. */
    public static final int CLASS_COMM = 2;

    /** Human Interface Device class. */
    public static final int CLASS_HID = 3;

    /** Physical. */
    public static final int CLASS_PHYSICAL = 5;

    /** Image class. */
    public static final int CLASS_PTP = 6;

    /** Image class. */
    public static final int CLASS_IMAGE = 6;

    /** Printer class. */
    public static final int CLASS_PRINTER = 7;

    /** Mass storage class. */
    public static final int CLASS_MASS_STORAGE = 8;

    /** Hub class. */
    public static final int CLASS_HUB = 9;

    /** Data class. */
    public static final int CLASS_DATA = 10;

    /** Smart Card. */
    public static final int CLASS_SMART_CARD = 0x0b;

    /** Content Security. */
    public static final int CLASS_CONTENT_SECURITY = 0x0d;

    /** Video. */
    public static final int CLASS_VIDEO = 0x0e;

    /** Personal Healthcare. */
    public static final int CLASS_PERSONAL_HEALTHCARE = 0x0f;

    /** Diagnostic Device. */
    public static final int CLASS_DIAGNOSTIC_DEVICE = 0xdc;

    /** Wireless class. */
    public static final int CLASS_WIRELESS = 0xe0;

    /** Application class. */
    public static final int CLASS_APPLICATION = 0xfe;

    /** Class is vendor-specific. */
    public static final int CLASS_VENDOR_SPEC = 0xff;

    // Descriptor types as defined by the USB specification.

    /**
     * Device descriptor.
     * 
     * @see DeviceDescriptor
     */
    public static final int DT_DEVICE = 0x01;

    /**
     * Configuration descriptor.
     * 
     * @see ConfigDescriptor
     */
    public static final int DT_CONFIG = 0x02;

    /** String descriptor. */
    public static final int DT_STRING = 0x03;

    /**
     * Interface descriptor.
     * 
     * @see InterfaceDescriptor
     */
    public static final int DT_INTERFACE = 0x04;

    /**
     * Endpoint descriptor.
     * 
     * @see EndpointDescriptor
     */
    public static final int DT_ENDPOINT = 0x05;

    /** HID descriptor. */
    public static final int DT_HID = 0x21;

    /** HID report descriptor. */
    public static final int DT_REPORT = 0x22;

    /** Physical descriptor. */
    public static final int DT_PHYSICAL = 0x23;

    /** Hub descriptor. */
    public static final int DT_HUB = 0x29;

    /** Hub descriptor. */
    public static final int DT_SUPERSPEED_HUB = 0x2a;

    // Descriptor sizes per descriptor type

    /** Size of a device descriptor. */
    public static final int DT_DEVICE_SIZE = 18;

    /** Size of a config descriptor. */
    public static final int DT_CONFIG_SIZE = 9;

    /** Size of an interface descriptor. */
    public static final int DT_INTERFACE_SIZE = 9;

    /** Size of an interface descriptor. */
    public static final int DT_ENDPOINT_SIZE = 7;

    /** Size of an interface descriptor. */
    public static final int DT_ENDPOINT_AUDIO_SIZE = 9;

    /** Size of an interface descriptor. */
    public static final int DT_HUB_NONVAR_SIZE = 7;

    // Endpoint direction. Values for bit 7 of the endpoint address scheme.

    /** In: device-to-host. */
    public static final int ENDPOINT_IN = 0x80;

    /** Out: host-to-device. */
    public static final int ENDPOINT_OUT = 0x00;

    // === Masks =============================================================

    /** Endpoint address mask. */
    public static final int ENDPOINT_ADDRESS_MASK = 0x0f;

    /** Endpoint direction mask. */
    public static final int ENDPOINT_DIR_MASK = 0x80;

    /** Transfer type mask. */
    public static final int TRANSFER_TYPE_MASK = 0x03;

    // Endpoint transfer type. Values for bits 0:1 of the endpoint attributes
    // field.

    /** Control endpoint. */
    public static final int TRANSFER_TYPE_CONTROL = 0;

    /** Isochronous endpoint. */
    public static final int TRANSFER_TYPE_ISOCHRONOUS = 1;

    /** Bulk endpoint. */
    public static final int TRANSFER_TYPE_BULK = 2;

    /** Interrupt endpoint. */
    public static final int TRANSFER_TYPE_INTERRUPT = 3;

    // Synchronization type for isochronous endpoints.
    // Values for bits 2:3 of the bmAttributes field in
    // EndpointDescriptor.

    /** No synchronization. */
    public static final int ISO_SYNC_TYPE_NONE = 0;

    /** Asynchronous. */
    public static final int ISO_SYNC_TYPE_ASYNC = 1;

    /** Adaptive. */
    public static final int ISO_SYNC_TYPE_ADAPTIVE = 2;

    /** Synchronous. */
    public static final int ISO_SYNC_TYPE_SYNC = 3;

    // Usage type for isochronous endpoints. Values for bits 4:5 of the
    // bmAttributes field in EndpointDescriptor.

    /** Data endpoint. */
    public static final int ISO_USAGE_TYPE_DATA = 0;

    /** Feedback endpoint. */
    public static final int ISO_USAGE_TYPE_FEEDBACK = 1;

    /** Implicit feedback Data endpoint. */
    public static final int ISO_USAGE_TYPE_IMPLICIT = 2;

    /** Report short frames as errors. */
    public static final int TRANSFER_SHORT_NOT_OK = 1;

    // Transfer flags

    /**
     * Automatically free transfer buffer during {@link #freeTransfer(Transfer)}
     * TODO Not sure how to do this memory management between Java and C.
     */
    public static final int TRANSFER_FREE_BUFFER = 2;

    /**
     * Automatically call {@link #freeTransfer(Transfer)} after callback
     * returns.
     * 
     * If this flag is set, it is illegal to call
     * {@link #freeTransfer(Transfer)} from your transfer callback, as this will
     * result in a double-free when this flag is acted upon.
     */
    public static final int TRANSFER_FREE_TRANSFER = 4;

    /**
     * Terminate transfers that are a multiple of the endpoint's wMaxPacketSize
     * with an extra zero length packet.
     * 
     * This is useful when a device protocol mandates that each logical request
     * is terminated by an incomplete packet (i.e. the logical requests are not
     * separated by other means).
     * 
     * This flag only affects host-to-device transfers to bulk and interrupt
     * endpoints. In other situations, it is ignored.
     * 
     * This flag only affects transfers with a length that is a multiple of the
     * endpoint's wMaxPacketSize. On transfers of other lengths, this flag has
     * no effect. Therefore, if you are working with a device that needs a ZLP
     * whenever the end of the logical request falls on a packet boundary, then
     * it is sensible to set this flag on every transfer (you do not have to
     * worry about only setting it on transfers that end on the boundary).
     * 
     * This flag is currently only supported on Linux. On other systems,
     * libusb_submit_transfer() will return {@link #ERROR_NOT_SUPPORTED} for
     * every transfer where this flag is set.
     */
    public static final int TRANSFER_ADD_ZERO_PACKET = 8;

    // Transfer status codes

    /**
     * Transfer completed without error. Note that this does not indicate that
     * the entire amount of requested data was transferred.
     */
    public static final int TRANSFER_COMPLETED = 0;

    /** Transfer failed. */
    public static final int TRANSFER_ERROR = 1;

    /** Transfer timed out. */
    public static final int TRANSFER_TIMED_OUT = 2;

    /** Transfer was cancelled. */
    public static final int TRANSFER_CANCELLED = 3;

    /**
     * For bulk/interrupt endpoints: halt condition detected (endpoint stalled).
     * For control endpoints: control request not supported.
     */
    public static final int TRANSFER_STALL = 4;

    /** Device was disconnected. */
    public static final int TRANSFER_NO_DEVICE = 5;

    /** Device sent more data than requested. */
    public static final int TRANSFER_OVERFLOW = 6;

    /** The maximum size of a string (Unicode). */
    private static final int MAX_STRING_SIZE = 126;

    /** The currently set pollfd listener. */
    private static PollfdListener pollfdListener;

    /** The currently set pollfd listener user data. */
    private static Object pollfdListenerUserData;

    static
    {
        Loader.load();
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private LibUSB()
    {
        // Empty
    }

    /**
     * Initialize libusb.
     * 
     * This function must be called before calling any other libusbx function.
     * 
     * If you do not provide an output location for a {@link Context}, a default
     * context will be created. If there was already a default context, it will
     * be reused (and nothing will be initialized/reinitialized).
     * 
     * @param context
     *            Optional output location for context pointer. Null to use
     *            default context. Only valid on return code 0.
     * @return 0 on success or a error code on failure.
     * 
     * @see <a href="http://libusbx.sf.net/api-1.0/contexts.html">Contexts</a>
     */
    public static native int init(final Context context);

    /**
     * Deinitialize libusb.
     * 
     * Should be called after closing all open devices and before your
     * application terminates.
     * 
     * @param context
     *            The {@link Context} to deinitialize, or NULL for the default
     *            context.
     */
    public static native void exit(final Context context);

    /**
     * Set log message verbosity.
     * 
     * The default level is {@link #LOG_LEVEL_NONE}, which means no messages are
     * ever printed. If you choose to increase the message verbosity level,
     * ensure that your application does not close the stdout/stderr file
     * descriptors.
     * 
     * You are advised to use level {@link #LOG_LEVEL_WARNING}. libusbx is
     * conservative with its message logging and most of the time, will only log
     * messages that explain error conditions and other oddities. This will help
     * you debug your software.
     * 
     * If the {@link #LOG_LEVEL_DEBUG} environment variable was set when libusbx
     * was initialized, this function does nothing: the message verbosity is
     * fixed to the value in the environment variable.
     * 
     * If libusbx was compiled without any message logging, this function does
     * nothing: you'll never get any messages.
     * 
     * If libusbx was compiled with verbose debug message logging, this function
     * does nothing: you'll always get messages from all levels.
     * 
     * @param context
     *            The {@link Context} to operate on, or NULL for the default
     *            context.
     * @param level
     *            The log level to set.
     */
    public static native void setDebug(final Context context,
        final int level);

    /**
     * Returns the version of the libusbx runtime.
     * 
     * @return The version of the libusbx runtime.
     */
    public static native Version getVersion();

    /**
     * Returns a list of USB devices currently attached to the system.
     * 
     * This is your entry point into finding a USB device to operate.
     * 
     * You are expected to unreference all the devices when you are done with
     * them, and then free the list with
     * {@link #freeDeviceList(DeviceList, boolean)}. Note that
     * {@link #freeDeviceList(DeviceList, boolean)} can unref all the devices
     * for you. Be careful not to unreference a device you are about to open
     * until after you have opened it.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param list
     *            Output location for a list of devices. Must be later freed
     *            with {@link #freeDeviceList(DeviceList, boolean)}.
     * @return The number of devices in the outputted list, or any ERROR code
     *         according to errors encountered by the backend.
     */
    public static native int getDeviceList(final Context context,
        final DeviceList list);

    /**
     * Frees a list of devices previously discovered using
     * {@link #getDeviceList(Context, DeviceList)}.
     * 
     * If the unref_devices parameter is set, the reference count of each device
     * in the list is decremented by 1.
     * 
     * @param list
     *            The list to free.
     * @param unrefDevices
     *            Whether to unref the devices in the list.
     */
    public static native void freeDeviceList(final DeviceList list,
        boolean unrefDevices);

    /**
     * Get the number of the bus that a device is connected to.
     * 
     * @param device
     *            A device.
     * @return The bus number
     */
    public static native int getBusNumber(final Device device);

    /**
     * Get the number of the port that a device is connected to.
     * 
     * @param device
     *            A device
     * @return The port number (0 if not available).
     */
    public static native int getPortNumber(final Device device);

    /**
     * Get the list of all port numbers from root for the specified device.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     * @param device
     *            A device.
     * @param path
     *            The array that should contain the port numbers. As per the USB
     *            3.0 specs, the current maximum limit for the depth is 7.
     * @return The number of elements filled, {@link #ERROR_OVERFLOW} if the
     *         array is too small
     */
    public static native int getPortPath(final Context context,
        final Device device, byte[] path);

    /**
     * Get the the parent from the specified device [EXPERIMENTAL].
     * 
     * @param device
     *            A device
     * @return The device parent or NULL if not available. You should issue a
     *         {@link #getDeviceList(Context, DeviceList)} before calling this
     *         function and make sure that you only access the parent before
     *         issuing {@link #freeDeviceList(DeviceList, boolean)}. The reason
     *         is that libusbx currently does not maintain a permanent list of
     *         device instances, and therefore can only guarantee that parents
     *         are fully instantiated within a
     *         {@link #getDeviceList(Context, DeviceList)} -
     *         {@link #freeDeviceList(DeviceList, boolean)} block.
     */
    public static native Device getParent(final Device device);

    /**
     * Get the address of the device on the bus it is connected to.
     * 
     * @param device
     *            A device.
     * @return The device address
     */
    public static native int getDeviceAddress(final Device device);

    /**
     * Get the negotiated connection speed for a device.
     * 
     * @param device
     *            A device.
     * @return A SPEED code, where {@link #SPEED_UNKNOWN} means that the OS
     *         doesn't know or doesn't support returning the negotiated speed.
     */
    public static native int getDeviceSpeed(final Device device);

    /**
     * Convenience function to retrieve the wMaxPacketSize value for a
     * particular endpoint in the active device configuration.
     * 
     * This function was originally intended to be of assistance when setting up
     * isochronous transfers, but a design mistake resulted in this function
     * instead. It simply returns the wMaxPacketSize value without considering
     * its contents. If you're dealing with isochronous transfers, you probably
     * want {@link #getMaxIsoPacketSize(Device, int)} instead.
     * 
     * @param device
     *            A device.
     * @param endpoint
     *            Address of the endpoint in question.
     * @return the wMaxPacketSize value {@link #ERROR_NOT_FOUND} if the endpoint
     *         does not exist {@link #ERROR_OTHER} on other failure
     */
    public static native int getMaxPacketSize(final Device device,
        int endpoint);

    /**
     * Calculate the maximum packet size which a specific endpoint is capable
     * sending or receiving in the duration of 1 microframe.
     * 
     * Only the active configuration is examined. The calculation is based on
     * the wMaxPacketSize field in the endpoint descriptor as described in
     * section 9.6.6 in the USB 2.0 specifications.
     * 
     * If acting on an isochronous or interrupt endpoint, this function will
     * multiply the value found in bits 0:10 by the number of transactions per
     * microframe (determined by bits 11:12). Otherwise, this function just
     * returns the numeric value found in bits 0:10.
     * 
     * This function is useful for setting up isochronous transfers, for example
     * you might pass the return value from this function to
     * libusb_set_iso_packet_lengths() in order to set the length field of every
     * isochronous packet in a transfer.
     * 
     * TODO Link to libusb_set_iso_packet_lengths when implemented
     * 
     * @param device
     *            A device.
     * @param endpoint
     *            Address of the endpoint in question.
     * @return The maximum packet size which can be sent/received on this
     *         endpoint {@link #ERROR_NOT_FOUND} if the endpoint does not exist
     *         {@link #ERROR_OTHER} on other failure.
     */
    public static native int getMaxIsoPacketSize(final Device device,
        int endpoint);

    /**
     * Increment the reference count of a device.
     * 
     * @param device
     *            The device to reference.
     * @return The same device.
     */
    public static native Device refDevice(final Device device);

    /**
     * Decrement the reference count of a device.
     * 
     * If the decrement operation causes the reference count to reach zero, the
     * device shall be destroyed.
     * 
     * @param device
     *            the device to unreference.
     */
    public static native void unrefDevice(final Device device);

    /**
     * Open a device and obtain a device handle.
     * 
     * A handle allows you to perform I/O on the device in question.
     * 
     * Internally, this function adds a reference to the device and makes it
     * available to you through {@link #getDevice(DeviceHandle)}. This reference
     * is removed during {@link #close(DeviceHandle)}.
     * 
     * This is a non-blocking function; no requests are sent over the bus.
     * 
     * @param device
     *            The device to open.
     * @param handle
     *            Output location for the returned device handle pointer. Only
     *            populated when the return code is 0.
     * @return 0 on success {@link #ERROR_NO_MEM} on memory allocation failure
     *         {@link #ERROR_ACCESS} if the user has insufficient permissions
     *         {@link #ERROR_NO_DEVICE} if the device has been disconnected
     *         another error on other failure
     */
    public static native int open(final Device device,
        final DeviceHandle handle);

    /**
     * Convenience function for finding a device with a particular
     * idVendor/idProduct combination.
     * 
     * This function is intended for those scenarios where you are using libusbx
     * to knock up a quick test application - it allows you to avoid calling
     * {@link #getDeviceList(Context, DeviceList)} and worrying about
     * traversing/freeing the list.
     * 
     * This function has limitations and is hence not intended for use in real
     * applications: if multiple devices have the same IDs it will only give you
     * the first one, etc.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param vendorId
     *            The idVendor value to search for.
     * @param productId
     *            The idProduct value to search for.
     * @return A handle for the first found device, or NULL on error or if the
     *         device could not be found.
     */
    public static native DeviceHandle openDeviceWithVidPid(
        final Context context, final int vendorId, final int productId);

    /**
     * Close a device handle.
     * 
     * Should be called on all open handles before your application exits.
     * 
     * Internally, this function destroys the reference that was added by
     * {@link #open(Device, DeviceHandle)} on the given device.
     * 
     * This is a non-blocking function; no requests are sent over the bus.
     * 
     * @param handle
     *            The handle to close.
     */
    public static native void close(final DeviceHandle handle);

    /**
     * Get the underlying device for a handle.
     * 
     * This function does not modify the reference count of the returned device,
     * so do not feel compelled to unreference it when you are done.
     * 
     * @param handle
     *            a device handle.
     * @return The underlying device.
     */
    public static native Device getDevice(final DeviceHandle handle);

    /**
     * Determine the bConfigurationValue of the currently active configuration.
     * 
     * You could formulate your own control request to obtain this information,
     * but this function has the advantage that it may be able to retrieve the
     * information from operating system caches (no I/O involved).
     * 
     * If the OS does not cache this information, then this function will block
     * while a control transfer is submitted to retrieve the information.
     * 
     * This function will return a value of 0 in the config output parameter if
     * the device is in unconfigured state.
     * 
     * @param handle
     *            a device handle.
     * @param config
     *            output location for the bConfigurationValue of the active
     *            configuration (only valid for return code 0)
     * @return 0 on success {@link #ERROR_NO_DEVICE} if the device has been
     *         disconnected another error code on other failure
     */
    public static native int getConfiguration(final DeviceHandle handle,
        final IntBuffer config);

    /**
     * Set the active configuration for a device.
     * 
     * The operating system may or may not have already set an active
     * configuration on the device. It is up to your application to ensure the
     * correct configuration is selected before you attempt to claim interfaces
     * and perform other operations.
     * 
     * If you call this function on a device already configured with the
     * selected configuration, then this function will act as a lightweight
     * device reset: it will issue a SET_CONFIGURATION request using the current
     * configuration, causing most USB-related device state to be reset
     * (altsetting reset to zero, endpoint halts cleared, toggles reset).
     * 
     * You cannot change/reset configuration if your application has claimed
     * interfaces - you should free them with
     * {@link #releaseInterface(DeviceHandle, int)} first. You cannot
     * change/reset configuration if other applications or drivers have claimed
     * interfaces.
     * 
     * A configuration value of -1 will put the device in unconfigured state.
     * The USB specifications state that a configuration value of 0 does this,
     * however buggy devices exist which actually have a configuration 0.
     * 
     * You should always use this function rather than formulating your own
     * SET_CONFIGURATION control request. This is because the underlying
     * operating system needs to know when such changes happen.
     * 
     * This is a blocking function.
     * 
     * @param handle
     *            a device handle.
     * @param config
     *            the bConfigurationValue of the configuration you wish to
     *            activate, or -1 if you wish to put the device in unconfigured
     *            state
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the requested
     *         configuration does not exist, {@link #ERROR_BUSY} if interfaces
     *         are currently claimed, {@link #ERROR_NO_DEVICE} if the device has
     *         been disconnected, another error code on other failure
     */
    public static native int setConfiguration(final DeviceHandle handle,
        final int config);

    /**
     * Claim an interface on a given device handle.
     * 
     * You must claim the interface you wish to use before you can perform I/O
     * on any of its endpoints.
     * 
     * It is legal to attempt to claim an already-claimed interface, in which
     * case libusbx just returns 0 without doing anything.
     * 
     * Claiming of interfaces is a purely logical operation; it does not cause
     * any requests to be sent over the bus. Interface claiming is used to
     * instruct the underlying operating system that your application wishes to
     * take ownership of the interface.
     * 
     * This is a non-blocking function.
     * 
     * @param handle
     *            A device handle.
     * @param iface
     *            The bInterfaceNumber of the interface you wish to claim.
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the requested interface
     *         does not exist, {@link #ERROR_BUSY} if another program or driver
     *         has claimed the interface, {@link #ERROR_NO_DEVICE} if the device
     *         has been disconnected, another error code on other failure
     */
    public static native int claimInterface(final DeviceHandle handle,
        final int iface);

    /**
     * Release an interface previously claimed with
     * {@link #claimInterface(DeviceHandle, int)}.
     * 
     * You should release all claimed interfaces before closing a device handle.
     * 
     * This is a blocking function. A SET_INTERFACE control request will be sent
     * to the device, resetting interface state to the first alternate setting.
     * 
     * @param handle
     *            a device handle.
     * @param iface
     *            The bInterfaceNumber of the previously-claimed interface
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the interface was not
     *         claimed, {@link #ERROR_NO_DEVICE} if the device has been
     *         disconnected, another ERROR code on other failure
     */
    public static native int releaseInterface(final DeviceHandle handle,
        final int iface);

    /**
     * Activate an alternate setting for an interface.
     * 
     * The interface must have been previously claimed with
     * {@link #claimInterface(DeviceHandle, int)}.
     * 
     * You should always use this function rather than formulating your own
     * SET_INTERFACE control request. This is because the underlying operating
     * system needs to know when such changes happen.
     * 
     * This is a blocking function.
     * 
     * @param handle
     *            A device handle.
     * @param interfaceNumber
     *            The bInterfaceNumber of the previously-claimed interface
     * @param alternateSetting
     *            The bAlternateSetting of the alternate setting to activate
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the interface was not
     *         claimed, or the requested alternate setting does not exist
     *         {@link #ERROR_NO_DEVICE} if the device has been disconnected,
     *         another ERROR code on other failure
     */
    public static native int setInterfaceAltSetting(final DeviceHandle handle,
        int interfaceNumber, int alternateSetting);

    /**
     * Clear the halt/stall condition for an endpoint.
     * 
     * Endpoints with halt status are unable to receive or transmit data until
     * the halt condition is stalled.
     * 
     * You should cancel all pending transfers before attempting to clear the
     * halt condition.
     * 
     * This is a blocking function.
     * 
     * @param handle
     *            A device handle.
     * @param endpoint
     *            The endpoint to clear halt status
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the endpoint does not
     *         exist, {@link #ERROR_NO_DEVICE} if the device has been
     *         disconnected, another ERROR code on other failure.
     */
    public static native int clearHalt(final DeviceHandle handle, int endpoint);

    /**
     * Perform a USB port reset to reinitialize a device.
     * 
     * The system will attempt to restore the previous configuration and
     * alternate settings after the reset has completed.
     * 
     * If the reset fails, the descriptors change, or the previous state cannot
     * be restored, the device will appear to be disconnected and reconnected.
     * This means that the device handle is no longer valid (you should close
     * it) and rediscover the device. A return code of {@link #ERROR_NOT_FOUND}
     * indicates when this is the case.
     * 
     * This is a blocking function which usually incurs a noticeable delay.
     * 
     * @param handle
     *            a handle of the device to reset
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if re-enumeration is
     *         required, or if the device has been disconnected another ERROR
     *         code on other failure
     */
    public static native int resetDevice(final DeviceHandle handle);

    /**
     * Determine if a kernel driver is active on an interface.
     * 
     * If a kernel driver is active, you cannot claim the interface, and libusbx
     * will be unable to perform I/O.
     * 
     * This functionality is not available on Windows.
     * 
     * @param handle
     *            A device handle.
     * @param interfaceNumber
     *            The interface to check.
     * @return 0 if no kernel driver is active, 1 if a kernel driver is active,
     *         {@link #ERROR_NO_DEVICE} if the device has been disconnected,
     *         {@link #ERROR_NOT_SUPPORTED} on platforms where the functionality
     *         is not available, another ERROR code on other failure
     * 
     * @see #detachKernelDriver(DeviceHandle, int)
     */
    public static native int kernelDriverActive(final DeviceHandle handle,
        final int interfaceNumber);

    /**
     * Detach a kernel driver from an interface.
     * 
     * If successful, you will then be able to claim the interface and perform
     * I/O.
     * 
     * This functionality is not available on Darwin or Windows.
     * 
     * Note that libusbx itself also talks to the device through a special
     * kernel driver, if this driver is already attached to the device, this
     * call will not detach it and return {@link #ERROR_NOT_FOUND}.
     * 
     * @param handle
     *            a device handle
     * @param interfaceNumber
     *            the interface to detach the driver from
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if no kernel driver was
     *         active, {@link #ERROR_INVALID_PARAM} if the interface does not
     *         exist, {@link #ERROR_NO_DEVICE} if the device has been
     *         disconnected, {@link #ERROR_NOT_SUPPORTED} on platforms where the
     *         functionality is not available, another ERROR code on other
     *         failure
     * 
     * @see #kernelDriverActive(DeviceHandle, int)
     */
    public static native int detachKernelDriver(final DeviceHandle handle,
        final int interfaceNumber);

    /**
     * Re-attach an interface's kernel driver, which was previously detached
     * using {@link #detachKernelDriver(DeviceHandle, int)}.
     * 
     * This call is only effective on Linux and returns
     * {@link #ERROR_NOT_SUPPORTED} on all other platforms.
     * 
     * This functionality is not available on Darwin or Windows.
     * 
     * @param handle
     *            A device handle
     * @param interfaceNumber
     *            the interface to attach the driver from
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if no kernel driver was
     *         active, {@link #ERROR_INVALID_PARAM} if the interface does not
     *         exist, {@link #ERROR_NO_DEVICE} if the device has been
     *         disconnected, {@link #ERROR_NOT_SUPPORTED} on platforms where the
     *         functionality is not available, {@link #ERROR_BUSY} if the driver
     *         cannot be attached because the interface is claimed by a program
     *         or driver, anotherERROR code on other failure
     * 
     * @see #kernelDriverActive(DeviceHandle, int)
     */
    public static native int attachKernelDriver(final DeviceHandle handle,
        final int interfaceNumber);

    /**
     * Check at runtime if the loaded library has a given capability.
     * 
     * @param capability
     *            The capability to check for.
     * @return True if the running library has the capability, false otherwise.
     */
    public static native boolean hasCapability(final int capability);

    /**
     * Returns a string with the ASCII name of a libusb error or transfer status
     * code.
     * 
     * @param errorCode
     *            The libusb error or libusb transfer status code to return the
     *            name of.
     * @return The error name, or the string **UNKNOWN** if the value of
     *         errorCode is not a known error / status code.
     */
    public static native String errorName(final int errorCode);

    /**
     * Convert a 16-bit value from little-endian to host-endian format.
     * 
     * On little endian systems, this function does nothing. On big endian
     * systems, the bytes are swapped.
     * 
     * @param x
     *            The little-endian value to convert
     * @return the value in host-endian byte order
     */
    public static native int le16ToCpu(final int x);

    /**
     * Convert a 16-bit value from host-endian to little-endian format.
     * 
     * On little endian systems, this function does nothing. On big endian
     * systems, the bytes are swapped.
     * 
     * @param x
     *            The host-endian value to convert
     * @return the value in little-endian byte order
     */
    public static native int cpuToLe16(final int x);

    /**
     * Get the USB device descriptor for a given device.
     * 
     * This is a non-blocking function; the device descriptor is cached in
     * memory.
     * 
     * @param device
     *            the device
     * @param descriptor
     *            output location for the descriptor data
     * @return 0 on success or a ERROR code on failure
     */
    public static native int getDeviceDescriptor(final Device device,
        final DeviceDescriptor descriptor);

    /**
     * Retrieve a string descriptor in C style ASCII.
     * 
     * @param handle
     *            A device handle.
     * @param index
     *            The index of the descriptor to retrieve.
     * @param string
     *            Output buffer for ASCII string descriptor.
     * @param length
     *            Maximum number of bytes to read.
     * @return Number of bytes returned in data, or ERROR code on failure.
     */
    public static native int getStringDescriptorAscii(
        final DeviceHandle handle, final int index, final StringBuffer string,
        final int length);

    /**
     * A simple wrapper around
     * {@link #getStringDescriptorAscii(DeviceHandle, int, StringBuffer, int)}
     * Simply returns the string (Maximum length of 126) if possible. If not
     * possible (NULL handle or 0-index specified or error occured) then null is
     * returned.
     * 
     * This method is not part of libusb.
     * 
     * @param handle
     *            The device handle.
     * @param index
     *            The string descriptor index.
     * @return The string or null if it could not be read.
     */
    public static String getStringDescriptor(final DeviceHandle handle,
        final int index)
    {
        if (handle == null || index == 0) return null;
        final StringBuffer buffer = new StringBuffer();
        if (getStringDescriptorAscii(handle, index, buffer,
            MAX_STRING_SIZE) >= 0)
        {
            return buffer.toString();
        }
        return null;
    }

    /**
     * Get the USB configuration descriptor for the currently active
     * configuration.
     * 
     * This is a non-blocking function which does not involve any requests being
     * sent to the device.
     * 
     * @param device
     *            A device.
     * @param descriptor
     *            Output location for the USB configuration descriptor. Only
     *            valid if 0 was returned. Must be freed with
     *            {@link #freeConfigDescriptor(ConfigDescriptor)} after use.
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the device is in
     *         unconfigured state another ERROR code on error
     * 
     * @see #getConfigDescriptor(Device, int, ConfigDescriptor)
     */
    public static native int getActiveConfigDescriptor(final Device device,
        final ConfigDescriptor descriptor);

    /**
     * Get a USB configuration descriptor based on its index.
     * 
     * This is a non-blocking function which does not involve any requests being
     * sent to the device.
     * 
     * @param device
     *            A device.
     * @param index
     *            The index of the configuration you wish to retrieve
     * @param descriptor
     *            Output location for the USB configuration descriptor. Only
     *            valid if 0 was returned. Must be freed with
     *            {@link #freeConfigDescriptor(ConfigDescriptor)} after use.
     * @return 0 on success {@link #ERROR_NOT_FOUND} if the configuration does
     *         not exist another ERROR code on error.
     * 
     * @see #getActiveConfigDescriptor(Device, ConfigDescriptor)
     * @see #getConfigDescriptorByValue(Device, int, ConfigDescriptor)
     */
    public static native int getConfigDescriptor(final Device device,
        final int index, final ConfigDescriptor descriptor);

    /**
     * Get a USB configuration descriptor with a specific bConfigurationValue.
     * 
     * This is a non-blocking function which does not involve any requests being
     * sent to the device.
     * 
     * @param device
     *            A device.
     * @param value
     *            The bConfigurationValue of the configuration you wish to
     *            retrieve.
     * @param descriptor
     *            Output location for the USB configuration descriptor. Only
     *            valid if 0 was returned. Must be freed with
     *            {@link #freeConfigDescriptor(ConfigDescriptor)} after use.
     * @return 0 on success {@link #ERROR_NOT_FOUND} if the configuration does
     *         not exist another ERROR code on error See also:
     * 
     * @see #getActiveConfigDescriptor(Device, ConfigDescriptor)
     * @see #getConfigDescriptor(Device, int, ConfigDescriptor)
     */
    public static native int getConfigDescriptorByValue(final Device device,
        final int value, final ConfigDescriptor descriptor);

    /**
     * Free a configuration descriptor obtained from
     * {@link #getConfigDescriptor(Device, int, ConfigDescriptor)} or
     * {@link #getActiveConfigDescriptor(Device, ConfigDescriptor)}.
     * 
     * It is safe to call this function with a NULL config parameter, in which
     * case the function simply returns.
     * 
     * @param descriptor
     *            The configuration descriptor to free
     */
    public static native void freeConfigDescriptor(
        final ConfigDescriptor descriptor);

    /**
     * Retrieve a descriptor from the default control pipe.
     * 
     * This is a convenience function which formulates the appropriate control
     * message to retrieve the descriptor.
     * 
     * @param handle
     *            A device handle.
     * @param type
     *            The descriptor type, see DT_* constants.
     * @param index
     *            The index of the descriptor to retrieve.
     * @param data
     *            Output buffer for descriptor
     * @return number of bytes returned in data, or ERROR code on failure
     * 
     */
    public static native int getDescriptor(final DeviceHandle handle,
        final int type, final int index, final ByteBuffer data);

    /**
     * Retrieve a descriptor from a device.
     * 
     * This is a convenience function which formulates the appropriate control
     * message to retrieve the descriptor. The string returned is Unicode, as
     * detailed in the USB specifications.
     * 
     * @param handle
     *            A device handle.
     * @param index
     *            The index of the descriptor to retrieve.
     * @param langId
     *            The language ID for the string descriptor.
     * @param data
     *            Output buffer for descriptor.
     * @return number of bytes returned in data, or LIBUSB_ERROR code on failure
     * @see #getStringDescriptorAscii(DeviceHandle, int, StringBuffer, int)
     */
    public static native int getStringDescriptor(final DeviceHandle handle,
        final int index, final int langId, final ByteBuffer data);

    /**
     * Perform a USB control transfer.
     * 
     * The direction of the transfer is inferred from the bmRequestType field of
     * the setup packet.
     * 
     * The wValue, wIndex and wLength fields values should be given in
     * host-endian byte order.
     * 
     * @param handle
     *            A handle for the device to communicate with.
     * @param bmRequestType
     *            The request type field for the setup packet.
     * @param bRequest
     *            The request field for the setup packet.
     * @param wValue
     *            The value field for the setup packet.
     * @param wIndex
     *            The index field for the setup packet.
     * @param data
     *            A suitably-sized data buffer for either input or output
     *            (depending on direction bits within bmRequestType).
     * @param timeout
     *            Timeout (in millseconds) that this function should wait before
     *            giving up due to no response being received. For an unlimited
     *            timeout, use value 0.
     * @return on success the number of bytes actually transferred,
     *         {@link #ERROR_TIMEOUT} if the transfer timed out,
     *         {@link #ERROR_PIPE} if the control request was not supported by
     *         the device, {@link #ERROR_NO_DEVICE} if the device has been
     *         disconnected, another ERROR code on other failures
     */
    public static native int controlTransfer(final DeviceHandle handle,
        final int bmRequestType, final int bRequest, final int wValue,
        int wIndex, final ByteBuffer data, final int timeout);

    /**
     * Perform a USB bulk transfer.
     * 
     * The direction of the transfer is inferred from the direction bits of the
     * endpoint address.
     * 
     * For bulk reads, the length field indicates the maximum length of data you
     * are expecting to receive. If less data arrives than expected, this
     * function will return that data, so be sure to check the transferred
     * output parameter.
     * 
     * You should also check the transferred parameter for bulk writes. Not all
     * of the data may have been written.
     * 
     * Also check transferred when dealing with a timeout error code. libusbx
     * may have to split your transfer into a number of chunks to satisfy
     * underlying O/S requirements, meaning that the timeout may expire after
     * the first few chunks have completed. libusbx is careful not to lose any
     * data that may have been transferred; do not assume that timeout
     * conditions indicate a complete lack of I/O.
     * 
     * @param handle
     *            A handle for the device to communicate with.
     * @param endpoint
     *            The address of a valid endpoint to communicate with.
     * @param data
     *            A suitably-sized data buffer for either input or output
     *            (depending on endpoint).
     * @param transferred
     *            Output location for the number of bytes actually transferred.
     * @param timeout
     *            timeout (in millseconds) that this function should wait before
     *            giving up due to no response being received. For an unlimited
     *            timeout, use value 0.
     * @return 0 on success (and populates transferred), {@link #ERROR_TIMEOUT}
     *         if the transfer timed out (and populates transferred),
     *         {@link #ERROR_PIPE} if the endpoint halted,
     *         {@link #ERROR_OVERFLOW} if the device offered more data, see
     *         Packets and overflows, {@link #ERROR_NO_DEVICE} if the device has
     *         been disconnected, another ERROR code on other failures.
     */
    public static native int bulkTransfer(final DeviceHandle handle,
        final int endpoint, final ByteBuffer data, final IntBuffer transferred,
        final int timeout);

    /**
     * Perform a USB interrupt transfer.
     * 
     * The direction of the transfer is inferred from the direction bits of the
     * endpoint address.
     * 
     * For interrupt reads, the length field indicates the maximum length of
     * data you are expecting to receive. If less data arrives than expected,
     * this function will return that data, so be sure to check the transferred
     * output parameter.
     * 
     * You should also check the transferred parameter for interrupt writes. Not
     * all of the data may have been written.
     * 
     * Also check transferred when dealing with a timeout error code. libusbx
     * may have to split your transfer into a number of chunks to satisfy
     * underlying O/S requirements, meaning that the timeout may expire after
     * the first few chunks have completed. libusbx is careful not to lose any
     * data that may have been transferred; do not assume that timeout
     * conditions indicate a complete lack of I/O.
     * 
     * The default endpoint bInterval value is used as the polling interval.
     * 
     * @param handle
     *            A handle for the device to communicate with.
     * @param endpoint
     *            The address of a valid endpoint to communicate with.
     * @param data
     *            A suitably-sized data buffer for either input or output
     *            (depending on endpoint).
     * @param transferred
     *            Output location for the number of bytes actually transferred.
     * @param timeout
     *            Timeout (in millseconds) that this function should wait before
     *            giving up due to no response being received. For an unlimited
     *            timeout, use value 0.
     * @return 0 on success (and populates transferred), {@link #ERROR_TIMEOUT}
     *         if the transfer timed out, {@link #ERROR_PIPE} if the endpoint
     *         halted, {@link #ERROR_OVERFLOW} if the device offered more data,
     *         see Packets and overflows, {@link #ERROR_NO_DEVICE} if the device
     *         has been disconnected, another ERROR code on other error
     */
    public static native int interruptTransfer(final DeviceHandle handle,
        final int endpoint, final ByteBuffer data, final IntBuffer transferred,
        final int timeout);

    /**
     * Attempt to acquire the event handling lock.
     * 
     * This lock is used to ensure that only one thread is monitoring libusbx
     * event sources at any one time.
     * 
     * You only need to use this lock if you are developing an application which
     * calls poll() or select() on libusbx's file descriptors directly. If you
     * stick to libusbx's event handling loop functions (e.g.
     * {@link #handleEvents(Context)}) then you do not need to be concerned with
     * this locking.
     * 
     * While holding this lock, you are trusted to actually be handling events.
     * If you are no longer handling events, you must call
     * {@link #unlockEvents(Context)} as soon as possible.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @return 0 if the lock was obtained successfully, 1 if the lock was not
     *         obtained (i.e. another thread holds the lock)
     */
    public static native int tryLockEvents(final Context context);

    /**
     * Acquire the event handling lock, blocking until successful acquisition if
     * it is contended.
     * 
     * This lock is used to ensure that only one thread is monitoring libusbx
     * event sources at any one time.
     * 
     * You only need to use this lock if you are developing an application which
     * calls poll() or select() on libusbx's file descriptors directly. If you
     * stick to libusbx's event handling loop functions (e.g.
     * {@link #handleEvents(Context)}) then you do not need to be concerned with
     * this locking.
     * 
     * While holding this lock, you are trusted to actually be handling events.
     * If you are no longer handling events, you must call
     * {@link #unlockEvents(Context)} as soon as possible.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     */
    public static native void lockEvents(final Context context);

    /**
     * Release the lock previously acquired with {@link #tryLockEvents(Context)}
     * or {@link #lockEvents(Context)}.
     * 
     * Releasing this lock will wake up any threads blocked on
     * {@link #waitForEvent(Context, long)}.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     */
    public static native void unlockEvents(final Context context);

    /**
     * Determine if it is still OK for this thread to be doing event handling.
     * 
     * Sometimes, libusbx needs to temporarily pause all event handlers, and
     * this is the function you should use before polling file descriptors to
     * see if this is the case.
     * 
     * If this function instructs your thread to give up the events lock, you
     * should just continue the usual logic that is documented in Multi-threaded
     * applications and asynchronous I/O. On the next iteration, your thread
     * will fail to obtain the events lock, and will hence become an event
     * waiter.
     * 
     * This function should be called while the events lock is held: you don't
     * need to worry about the results of this function if your thread is not
     * the current event handler.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @return 1 if event handling can start or continue, 0 if this thread must
     *         give up the events lock
     */
    public static native int eventHandlingOk(final Context context);

    /**
     * Determine if an active thread is handling events (i.e. if anyone is
     * holding the event handling lock).
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @return 1 if a thread is handling events, 0 if there are no threads
     *         currently handling events.
     */
    public static native int eventHandlerActive(final Context context);

    /**
     * Acquire the event waiters lock.
     * 
     * This lock is designed to be obtained under the situation where you want
     * to be aware when events are completed, but some other thread is event
     * handling so calling {@link #handleEvents(Context)} is not allowed.
     * 
     * You then obtain this lock, re-check that another thread is still handling
     * events, then call {@link #waitForEvent(Context, long)}.
     * 
     * You only need to use this lock if you are developing an application which
     * calls poll() or select() on libusbx's file descriptors directly, and may
     * potentially be handling events from 2 threads simultaenously. If you
     * stick to libusbx's event handling loop functions (e.g.
     * {@link #handleEvents(Context)}) then you do not need to be concerned with
     * this locking.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     */
    public static native void lockEventWaiters(final Context context);

    /**
     * Release the event waiters lock.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     */
    public static native void unlockEventWaiters(final Context context);

    /**
     * Wait for another thread to signal completion of an event.
     * 
     * Must be called with the event waiters lock held, see
     * {@link #lockEventWaiters(Context)}.
     * 
     * This function will block until any of the following conditions are met:
     * 
     * The timeout expires A transfer completes A thread releases the event
     * handling lock through {@link #unlockEvents(Context)} Condition 1 is
     * obvious. Condition 2 unblocks your thread after the callback for the
     * transfer has completed. Condition 3 is important because it means that
     * the thread that was previously handling events is no longer doing so, so
     * if any events are to complete, another thread needs to step up and start
     * event handling.
     * 
     * This function releases the event waiters lock before putting your thread
     * to sleep, and reacquires the lock as it is being woken up.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param timeout
     *            Maximum timeout for this blocking function. A 0 value
     *            indicates unlimited timeout.
     * 
     * @return 0 after a transfer completes or another thread stops event
     *         handling, 1 if the timeout expired
     */
    public static native int waitForEvent(final Context context,
        final long timeout);

    /**
     * Handle any pending events.
     * 
     * libusbx determines "pending events" by checking if any timeouts have
     * expired and by checking the set of file descriptors for activity.
     * 
     * If a zero timeval is passed, this function will handle any
     * already-pending events and then immediately return in non-blocking style.
     * 
     * If a non-zero timeval is passed and no events are currently pending, this
     * function will block waiting for events to handle up until the specified
     * timeout. If an event arrives or a signal is raised, this function will
     * return early.
     * 
     * If the parameter completed is not NULL then after obtaining the event
     * handling lock this function will return immediately if the integer
     * pointed to is not 0. This allows for race free waiting for the completion
     * of a specific transfer.
     * 
     * @param context
     *            the context to operate on, or NULL for the default context
     * @param timeout
     *            the maximum time to block waiting for events, or 0 for
     *            non-blocking mode.
     * @param completed
     *            Buffer for completion integer to check, or NULL.
     * @return 0 on success, or a ERROR code on failure
     */
    public static native int handleEventsTimeoutCompleted(
        final Context context, final long timeout, final IntBuffer completed);

    /**
     * Handle any pending events.
     * 
     * Like {@link #handleEventsTimeoutCompleted(Context, long, IntBuffer)}, but
     * without the completed parameter, calling this function is equivalent to
     * calling {@link #handleEventsTimeoutCompleted(Context, long, IntBuffer)}
     * with a NULL completed parameter.
     * 
     * This function is kept primarily for backwards compatibility. All new code
     * should call {@link #handleEventsCompleted(Context, IntBuffer)} or
     * {@link #handleEventsTimeoutCompleted(Context, long, IntBuffer)} to avoid
     * race conditions.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     * @param timeout
     *            The maximum time to block waiting for events, or an all zero
     *            timeval struct for non-blocking mode
     * @return 0 on success, or a ERROR code on failure
     */
    public static native int handleEventsTimeout(final Context context,
        final long timeout);

    /**
     * Handle any pending events in blocking mode.
     * 
     * There is currently a timeout hardcoded at 60 seconds but we plan to make
     * it unlimited in future. For finer control over whether this function is
     * blocking or non-blocking, or for control over the timeout, use
     * {@link #handleEventsTimeoutCompleted(Context, long, IntBuffer)} instead.
     * 
     * This function is kept primarily for backwards compatibility. All new code
     * should call {@link #handleEventsCompleted(Context, IntBuffer)} or
     * {@link #handleEventsTimeoutCompleted(Context, long, IntBuffer)} to avoid
     * race conditions.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @return 0 on success, or a ERROR code on failure.
     */
    public static native int handleEvents(final Context context);

    /**
     * Handle any pending events in blocking mode.
     * 
     * Like {@link #handleEvents(Context)}, with the addition of a completed
     * parameter to allow for race free waiting for the completion of a specific
     * transfer.
     * 
     * See {@link #handleEventsTimeoutCompleted(Context, long, IntBuffer)} for
     * details on the completed parameter.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param completed
     *            Buffer for completion integer to check, or NULL.
     * @return 0 on success, or a ERROR code on failure.
     */
    public static native int handleEventsCompleted(final Context context,
        final IntBuffer completed);

    /**
     * Handle any pending events by polling file descriptors, without checking
     * if any other threads are already doing so.
     * 
     * Must be called with the event lock held, see {@link #lockEvents(Context)}
     * .
     * 
     * This function is designed to be called under the situation where you have
     * taken the event lock and are calling poll()/select() directly on
     * libusbx's file descriptors (as opposed to using
     * {@link #handleEvents(Context)} or similar). You detect events on
     * libusbx's descriptors, so you then call this function with a zero timeout
     * value (while still holding the event lock).
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param timeout
     *            The maximum time to block waiting for events, or zero for
     *            non-blocking mode
     * @return 0 on success, or a ERROR code on failure.
     */
    public static native int handleEventsLocked(final Context context,
        final long timeout);

    /**
     * Determines whether your application must apply special timing
     * considerations when monitoring libusbx's file descriptors.
     * 
     * This function is only useful for applications which retrieve and poll
     * libusbx's file descriptors in their own main loop (The more advanced
     * option).
     * 
     * Ordinarily, libusbx's event handler needs to be called into at specific
     * moments in time (in addition to times when there is activity on the file
     * descriptor set). The usual approach is to use
     * {@link #getNextTimeout(Context, IntBuffer)} to learn about when the next
     * timeout occurs, and to adjust your poll()/select() timeout accordingly so
     * that you can make a call into the library at that time.
     * 
     * Some platforms supported by libusbx do not come with this baggage - any
     * events relevant to timing will be represented by activity on the file
     * descriptor set, and {@link #getNextTimeout(Context, IntBuffer)} will
     * always return 0. This function allows you to detect whether you are
     * running on such a platform.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     * @return 0 if you must call into libusbx at times determined by
     *         {@link #getNextTimeout(Context, IntBuffer)}, or 1 if all timeout
     *         events are handled internally or through regular activity on the
     *         file descriptors.
     */
    public static native int pollfdsHandleTimeouts(final Context context);

    /**
     * Determine the next internal timeout that libusbx needs to handle.
     * 
     * You only need to use this function if you are calling poll() or select()
     * or similar on libusbx's file descriptors yourself - you do not need to
     * use it if you are calling {@link #handleEvents(Context)} or a variant
     * directly.
     * 
     * You should call this function in your main loop in order to determine how
     * long to wait for select() or poll() to return results. libusbx needs to
     * be called into at this timeout, so you should use it as an upper bound on
     * your select() or poll() call.
     * 
     * When the timeout has expired, call into
     * {@link #handleEventsTimeout(Context, long)} (perhaps in non-blocking
     * mode) so that libusbx can handle the timeout.
     * 
     * This function may return 1 (success) and an all-zero timeval. If this is
     * the case, it indicates that libusbx has a timeout that has already
     * expired so you should call {@link #handleEventsTimeout(Context, long)} or
     * similar immediately. A return code of 0 indicates that there are no
     * pending timeouts.
     * 
     * On some platforms, this function will always returns 0 (no pending
     * timeouts). See Notes on time-based events.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     * @param timeout
     *            Output location for a relative time against the current clock
     *            in which libusbx must be called into in order to process
     *            timeout events
     * @return 0 if there are no pending timeouts, 1 if a timeout was returned,
     *         or {@link #ERROR_OTHER} failure
     */
    public static native int getNextTimeout(final Context context,
        final IntBuffer timeout);

    /**
     * Register notification functions for file descriptor additions/removals.
     * 
     * These functions will be invoked for every new or removed file descriptor
     * that libusbx uses as an event source.
     * 
     * To remove notifiers, pass NULL values for the function pointers.
     * 
     * Note that file descriptors may have been added even before you register
     * these notifiers (e.g. at {@link #init(Context)} time).
     * 
     * Additionally, note that the removal notifier may be called during
     * {@link #exit(Context)} (e.g. when it is closing file descriptors that
     * were opened and added to the poll set at {@link #init(Context)} time). If
     * you don't want this, remove the notifiers immediately before calling
     * {@link #exit(Context)}.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param listener
     *            The listener for addition and removal notifications.
     * @param userData
     *            User data to be passed back to callbacks (useful for passing
     *            context information).
     */
    public static void setPollfdNotifiers(final Context context,
        final PollfdListener listener, final Object userData)
    {
        pollfdListener = listener;
        pollfdListenerUserData = userData;
        if (listener == null)
            unsetPollfdNotifiers(context);
        else
            setPollfdNotifiers(context);
    }

    /**
     * Callback function, invoked when a new file descriptor should be added to
     * the set of file descriptors monitored for events.
     * 
     * @param fd
     *            The new file descriptor,
     * @param events
     *            events to monitor for, see libusb_pollfd for a description
     */
    static void triggerPollfdAdded(final FileDescriptor fd, final int events)
    {
        if (pollfdListener != null)
            pollfdListener.pollfdAdded(fd, events, pollfdListenerUserData);
    }

    /**
     * Called internally from JNI when a pollfd was removed.
     * 
     * @param fd
     *            The removed file descriptor.
     */
    static void triggerPollfdRemoved(final FileDescriptor fd)
    {
        if (pollfdListener != null)
            pollfdListener.pollfdRemoved(fd, pollfdListenerUserData);
    }

    /**
     * Configures libusbx to inform this class about pollfd additions and
     * removals.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     */
    static native void setPollfdNotifiers(final Context context);

    /**
     * Tells libusbx to stop informing this class about pollfd additions and
     * removals.
     * 
     * @param context
     *            The context to operate on, or NULL for the default context
     */
    static native void unsetPollfdNotifiers(final Context context);

    /**
     * Allocate a libusbx transfer with a specified number of isochronous packet
     * descriptors.
     * 
     * The returned transfer is pre-initialized for you. When the new transfer
     * is no longer needed, it should be freed with
     * {@link #freeTransfer(Transfer)}.
     * 
     * Transfers intended for non-isochronous endpoints (e.g. control, bulk,
     * interrupt) should specify an iso_packets count of zero.
     * 
     * For transfers intended for isochronous endpoints, specify an appropriate
     * number of packet descriptors to be allocated as part of the transfer. The
     * returned transfer is not specially initialized for isochronous I/O; you
     * are still required to call the {@link Transfer#setNumIsoPackets(int)} a
     * {@link Transfer#setType(int)} methods accordingly.
     * 
     * It is safe to allocate a transfer with some isochronous packets and then
     * use it on a non-isochronous endpoint. If you do this, ensure that at time
     * of submission, numIsoPackets is 0 and that type is set appropriately.
     * 
     * @param isoPackets
     *            Number of isochronous packet descriptors to allocate.
     * @return A newly allocated transfer, or NULL on error
     */
    public static native Transfer allocTransfer(final int isoPackets);

    /**
     * Free a transfer structure.
     * 
     * This should be called for all transfers allocated with
     * {@link #allocTransfer(int)}.
     * 
     * If the LIBUSB_TRANSFER_FREE_BUFFER flag is set and the transfer buffer is
     * non-NULL, this function will also free the transfer buffer using the
     * standard system memory allocator (e.g. free()).
     * 
     * It is legal to call this function with a NULL transfer. In this case, the
     * function will simply return safely.
     * 
     * It is not legal to free an active transfer (one which has been submitted
     * and has not yet completed).
     * 
     * @param transfer
     *            The transfer to free
     */
    public static native void freeTransfer(final Transfer transfer);
}
