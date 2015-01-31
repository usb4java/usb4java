/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Static class providing the constants and functions of libusb.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Luca Longinotti (l@longi.li)
 */
public final class LibUsb
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

    // Error codes. Most libusb functions return 0 on success or one of these
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

    /** Total number of error codes. */
    public static final int ERROR_COUNT = 14;

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

    // Supported speeds (wSpeedSupported) bitfield. Indicates what speeds the
    // device supports.

    /** Low speed operation supported (1.5MBit/s). */
    public static final short LOW_SPEED_OPERATION = 1;

    /** Full speed operation supported (12MBit/s). */
    public static final short FULL_SPEED_OPERATION = 2;

    /** High speed operation supported (480MBit/s). */
    public static final short HIGH_SPEED_OPERATION = 4;

    /** Superspeed operation supported (5000MBit/s). */
    public static final short SUPER_SPEED_OPERATION = 8;

    // Masks for the bits of the bmAttributes field of the USB 2.0 Extension
    // descriptor.

    /** Supports Link Power Management (LPM). */
    public static final int BM_LPM_SUPPORT = 2;

    // Masks for the bits of the bmAttributes field field of the SuperSpeed USB
    // Device Capability descriptor.

    /** Supports Latency Tolerance Messages (LTM). */
    public static final byte BM_LTM_SUPPORT = 2;

    // USB capability types.

    /** Wireless USB device capability. */
    public static final byte BT_WIRELESS_USB_DEVICE_CAPABILITY = 1;

    /** USB 2.0 extensions. */
    public static final byte BT_USB_2_0_EXTENSION = 2;

    /** SuperSpeed USB device capability. */
    public static final byte BT_SS_USB_DEVICE_CAPABILITY = 3;

    /** Container ID type. */
    public static final byte BT_CONTAINER_ID = 4;

    // Standard requests, as defined in table 9-5 of the USB 3.0 specifications.

    /** Request status of the specific recipient. */
    public static final byte REQUEST_GET_STATUS = 0x00;

    /** Clear or disable a specific feature. */
    public static final byte REQUEST_CLEAR_FEATURE = 0x01;

    /** Set or enable a specific feature. */
    public static final byte REQUEST_SET_FEATURE = 0x03;

    /** Set device address for all future accesses. */
    public static final byte REQUEST_SET_ADDRESS = 0x05;

    /** Set device address for all future accesses. */
    public static final byte REQUEST_GET_DESCRIPTOR = 0x06;

    /** Set device address for all future accesses. */
    public static final byte REQUEST_SET_DESCRIPTOR = 0x07;

    /** Get the current device configuration value. */
    public static final byte REQUEST_GET_CONFIGURATION = 0x08;

    /** Get the current device configuration value. */
    public static final byte REQUEST_SET_CONFIGURATION = 0x09;

    /** Return the selected alternate setting for the specified interface. */
    public static final byte REQUEST_GET_INTERFACE = 0x0A;

    /** Select an alternate interface for the specified interface. */
    public static final byte REQUEST_SET_INTERFACE = 0x0B;

    /** Set then report an endpoint's synchronization frame. */
    public static final byte REQUEST_SYNCH_FRAME = 0x0C;

    /** Sets both the U1 and U2 Exit Latency. */
    public static final byte REQUEST_SET_SEL = 0x30;

    /**
     * Delay from the time a host transmits a packet to the time it is received
     * by the device.
     */
    public static final byte SET_ISOCH_DELAY = 0x31;

    // Request type bits of the bmRequestType field in control transfers.

    /** Standard. */
    public static final byte REQUEST_TYPE_STANDARD = 0;

    /** Class. */
    public static final byte REQUEST_TYPE_CLASS = 32;

    /** Vendor. */
    public static final byte REQUEST_TYPE_VENDOR = 64;

    /** Reserved. */
    public static final byte REQUEST_TYPE_RESERVED = 96;

    // Recipient bits of the bmRequestType field in control transfers.
    // Values 4 through 31 are reserved.

    /** Device. */
    public static final byte RECIPIENT_DEVICE = 0x00;

    /** Interface. */
    public static final byte RECIPIENT_INTERFACE = 0x01;

    /** Endpoint. */
    public static final byte RECIPIENT_ENDPOINT = 0x02;

    /** Other. */
    public static final byte RECIPIENT_OTHER = 0x03;

    // Capabilities supported by an instance of libusb on the current running
    // platform. Test if the loaded library supports a given capability by
    // calling hasCapability.

    /** The {@link #hasCapability(int)} API is available. */
    public static final int CAP_HAS_CAPABILITY = 0x0000;

    /** Hotplug support is available on this platform. */
    public static final int CAP_HAS_HOTPLUG = 0x0001;

    /**
     * The library can access HID devices without requiring user intervention.
     * Note that before being able to actually access an HID device, you may
     * still have to call additional libusb functions such as
     * {@link #detachKernelDriver(DeviceHandle, int)}.
     */
    public static final int CAP_HAS_HID_ACCESS = 0x0100;

    /**
     * The library supports detaching of the default USB driver, using
     * {@link #detachKernelDriver(DeviceHandle, int)}, if one is set by the OS
     * kernel.
     */
    public static final int CAP_SUPPORTS_DETACH_KERNEL_DRIVER = 0x0101;

    /** The size of a control setup packet. */
    public static final short CONTROL_SETUP_SIZE = 8;

    // Device and/or Interface Class codes.

    /**
     * In the context of a device descriptor, this bDeviceClass value indicates
     * that each interface specifies its own class information and all
     * interfaces operate independently.
     */
    public static final byte CLASS_PER_INTERFACE = 0;

    /** Audio class. */
    public static final byte CLASS_AUDIO = 1;

    /** Communications class. */
    public static final byte CLASS_COMM = 2;

    /** Human Interface Device class. */
    public static final byte CLASS_HID = 3;

    /** Physical. */
    public static final byte CLASS_PHYSICAL = 5;

    /** Image class. */
    public static final byte CLASS_PTP = 6;

    /** Image class. */
    public static final byte CLASS_IMAGE = 6;

    /** Printer class. */
    public static final byte CLASS_PRINTER = 7;

    /** Mass storage class. */
    public static final byte CLASS_MASS_STORAGE = 8;

    /** Hub class. */
    public static final byte CLASS_HUB = 9;

    /** Data class. */
    public static final byte CLASS_DATA = 10;

    /** Smart Card. */
    public static final byte CLASS_SMART_CARD = 0x0B;

    /** Content Security. */
    public static final byte CLASS_CONTENT_SECURITY = 0x0D;

    /** Video. */
    public static final byte CLASS_VIDEO = 0x0E;

    /** Personal Healthcare. */
    public static final byte CLASS_PERSONAL_HEALTHCARE = 0x0F;

    /** Diagnostic Device. */
    public static final byte CLASS_DIAGNOSTIC_DEVICE = (byte) 0xDC;

    /** Wireless class. */
    public static final byte CLASS_WIRELESS = (byte) 0xE0;

    /** Application class. */
    public static final byte CLASS_APPLICATION = (byte) 0xFE;

    /** Class is vendor-specific. */
    public static final byte CLASS_VENDOR_SPEC = (byte) 0xFF;

    // Descriptor types as defined by the USB specification.

    /**
     * Device descriptor.
     *
     * @see DeviceDescriptor
     */
    public static final byte DT_DEVICE = 0x01;

    /**
     * Configuration descriptor.
     *
     * @see ConfigDescriptor
     */
    public static final byte DT_CONFIG = 0x02;

    /** String descriptor. */
    public static final byte DT_STRING = 0x03;

    /**
     * Interface descriptor.
     *
     * @see InterfaceDescriptor
     */
    public static final byte DT_INTERFACE = 0x04;

    /**
     * Endpoint descriptor.
     *
     * @see EndpointDescriptor
     */
    public static final byte DT_ENDPOINT = 0x05;

    /**
     * BOS descriptor.
     *
     * @see BosDescriptor
     */
    public static final byte DT_BOS = 0x0F;

    /**
     * Device Capability descriptor.
     *
     * @see BosDevCapabilityDescriptor
     */
    public static final byte DT_DEVICE_CAPABILITY = 0x10;

    /** HID descriptor. */
    public static final byte DT_HID = 0x21;

    /** HID report descriptor. */
    public static final byte DT_REPORT = 0x22;

    /** Physical descriptor. */
    public static final byte DT_PHYSICAL = 0x23;

    /** Hub descriptor. */
    public static final byte DT_HUB = 0x29;

    /** SuperSpeed Hub descriptor. */
    public static final byte DT_SUPERSPEED_HUB = 0x2A;

    /**
     * SuperSpeed Endpoint Companion descriptor.
     *
     * @see SsEndpointCompanionDescriptor
     */
    public static final byte DT_SS_ENDPOINT_COMPANION = 0x30;

    // Descriptor sizes per descriptor type

    /** Size of a device descriptor. */
    public static final byte DT_DEVICE_SIZE = 18;

    /** Size of a config descriptor. */
    public static final byte DT_CONFIG_SIZE = 9;

    /** Size of an interface descriptor. */
    public static final byte DT_INTERFACE_SIZE = 9;

    /** Size of an endpoint descriptor. */
    public static final byte DT_ENDPOINT_SIZE = 7;

    /** Size of an endpoint descriptor with audio extension. */
    public static final byte DT_ENDPOINT_AUDIO_SIZE = 9;

    /** Size of a hub descriptor. */
    public static final byte DT_HUB_NONVAR_SIZE = 7;

    /** Size of a SuperSpeed endpoint companion descriptor. */
    public static final byte DT_SS_ENDPOINT_COMPANION_SIZE = 6;

    /** Size of a BOS descriptor. */
    public static final byte DT_BOS_SIZE = 5;

    /** Size of a device capability descriptor. */
    public static final byte DT_DEVICE_CAPABILITY_SIZE = 3;

    // BOS descriptor sizes

    /** Size of a BOS descriptor. */
    public static final byte BT_USB_2_0_EXTENSION_SIZE = 7;

    /** Size of a BOS descriptor. */
    public static final byte BT_SS_USB_DEVICE_CAPABILITY_SIZE = 10;

    /** Size of a BOS descriptor. */
    public static final byte BT_CONTAINER_ID_SIZE = 20;

    /** We unwrap the BOS => define its maximum size. */
    public static final byte DT_BOS_MAX_SIZE = DT_BOS_SIZE
        + BT_USB_2_0_EXTENSION_SIZE + BT_SS_USB_DEVICE_CAPABILITY_SIZE
        + BT_CONTAINER_ID_SIZE;

    // Endpoint direction. Values for bit 7 of the endpoint address scheme.

    /** In: device-to-host. */
    public static final byte ENDPOINT_IN = (byte) 0x80;

    /** Out: host-to-device. */
    public static final byte ENDPOINT_OUT = 0x00;

    // === Masks =============================================================

    /** Endpoint address mask. */
    public static final byte ENDPOINT_ADDRESS_MASK = 0x0F;

    /** Endpoint direction mask. */
    public static final byte ENDPOINT_DIR_MASK = (byte) 0x80;

    /** Transfer type mask. */
    public static final byte TRANSFER_TYPE_MASK = 0x03;

    // Endpoint transfer type. Values for bits 0:1 of the endpoint attributes
    // field.

    /** Control endpoint. */
    public static final byte TRANSFER_TYPE_CONTROL = 0;

    /** Isochronous endpoint. */
    public static final byte TRANSFER_TYPE_ISOCHRONOUS = 1;

    /** Bulk endpoint. */
    public static final byte TRANSFER_TYPE_BULK = 2;

    /** Interrupt endpoint. */
    public static final byte TRANSFER_TYPE_INTERRUPT = 3;

    /** Stream endpoint. */
    public static final byte TRANSFER_TYPE_BULK_STREAM = 4;

    // Synchronization type for isochronous endpoints.
    // Values for bits 2:3 of the bmAttributes field in
    // EndpointDescriptor.

    /** The mask used to filter out sync types from attributes. */
    public static final byte ISO_SYNC_TYPE_MASK = 0x0C;

    /** No synchronization. */
    public static final byte ISO_SYNC_TYPE_NONE = 0;

    /** Asynchronous. */
    public static final byte ISO_SYNC_TYPE_ASYNC = 1;

    /** Adaptive. */
    public static final byte ISO_SYNC_TYPE_ADAPTIVE = 2;

    /** Synchronous. */
    public static final byte ISO_SYNC_TYPE_SYNC = 3;

    // Usage type for isochronous endpoints. Values for bits 4:5 of the
    // bmAttributes field in EndpointDescriptor.

    /** The mask used to filter out usage types from attributes. */
    public static final byte ISO_USAGE_TYPE_MASK = 0x30;

    /** Data endpoint. */
    public static final byte ISO_USAGE_TYPE_DATA = 0;

    /** Feedback endpoint. */
    public static final byte ISO_USAGE_TYPE_FEEDBACK = 1;

    /** Implicit feedback Data endpoint. */
    public static final byte ISO_USAGE_TYPE_IMPLICIT = 2;

    /** Report short frames as errors. */
    public static final byte TRANSFER_SHORT_NOT_OK = 1;

    // Transfer flags

    /**
     * Automatically free transfer buffer during {@link #freeTransfer(Transfer)}
     *
     * Please note that this flag (which is originally 2) is effectively a no-op
     * (set to zero) here in the Java wrapper, since the ByteBuffer that acts as
     * a buffer for transfers is allocated by the JVM and is subject to garbage
     * collection like any other object at some point. Nulling the reference is
     * the only needed action to take, and it is already done by the
     * TRANSFER_FREE_TRANSFER flag.
     */
    public static final byte TRANSFER_FREE_BUFFER = 0;

    /**
     * Automatically call {@link #freeTransfer(Transfer)} after callback
     * returns.
     *
     * If this flag is set, it is illegal to call
     * {@link #freeTransfer(Transfer)} from your transfer callback, as this will
     * result in a double-free when this flag is acted upon.
     */
    public static final byte TRANSFER_FREE_TRANSFER = 4;

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
    public static final byte TRANSFER_ADD_ZERO_PACKET = 8;

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

    // Flags for hotplug events

    /**
     * Default value when not using any flags.
     */
    public static final int HOTPLUG_NO_FLAGS = 0;

    /**
     * Arm the callback and fire it for all matching currently attached devices.
     */
    public static final int HOTPLUG_ENUMERATE = 1;

    // Hotplug events

    /** A device has been plugged in and is ready to use. */
    public static final int HOTPLUG_EVENT_DEVICE_ARRIVED = 0x01;

    /**
     * A device has left and is no longer available.
     * It is the user's responsibility to call {@link #close(DeviceHandle)} on
     * any handle associated with a disconnected device.
     * It is safe to call {@link #getDeviceDescriptor(Device, DeviceDescriptor)}
     * on a device that has left.
     */
    public static final int HOTPLUG_EVENT_DEVICE_LEFT = 0x02;

    // Wildcard matching for hotplug events

    /** Match any vendorId or productId or deviceClass. */
    public static final int HOTPLUG_MATCH_ANY = -1;

    /** The next global hotplug ID to use. */
    private static long globalHotplugId = 1;

    /**
     * Hotplug callbacks (to correctly manage calls and additional data).
     */
    private static final ConcurrentMap<Long,
        ImmutablePair<HotplugCallback, Object>> hotplugCallbacks =
        new ConcurrentHashMap<Long, ImmutablePair<HotplugCallback, Object>>();

    /**
     * Pollfd listeners (to support different listeners for different contexts).
     */
    private static final ConcurrentMap<Long,
        ImmutablePair<PollfdListener, Object>> pollfdListeners =
        new ConcurrentHashMap<Long, ImmutablePair<PollfdListener, Object>>();

    static
    {
        Loader.load();
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private LibUsb()
    {
        // Empty
    }

    /**
     * Returns the API version of the underlying libusb library. It is defined
     * as follows: (major << 24) | (minor << 16) | (16 bit incremental)
     *
     * @return The API version of the underlying libusb library.
     */
    public static native int getApiVersion();

    /**
     * Initialize libusb.
     *
     * This function must be called before calling any other libusb function.
     *
     * If you do not provide an output location for a {@link Context}, a default
     * context will be created. If there was already a default context, it will
     * be reused (and nothing will be initialized/reinitialized).
     *
     * @param context
     *            Optional output location for context pointer. Null to use
     *            default context. Only valid on return code 0.
     * @return 0 on success or a error code on failure.
     */
    public static synchronized native int init(final Context context);

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
    public static synchronized native void exit(final Context context);

    /**
     * Set log message verbosity.
     *
     * The default level is {@link #LOG_LEVEL_NONE}, which means no messages are
     * ever printed. If you choose to increase the message verbosity level,
     * ensure that your application does not close the stdout/stderr file
     * descriptors.
     *
     * You are advised to use level {@link #LOG_LEVEL_WARNING}. libusb is
     * conservative with its message logging and most of the time, will only log
     * messages that explain error conditions and other oddities. This will help
     * you debug your software.
     *
     * If the {@link #LOG_LEVEL_DEBUG} environment variable was set when libusb
     * was initialized, this function does nothing: the message verbosity is
     * fixed to the value in the environment variable.
     *
     * If libusb was compiled without any message logging, this function does
     * nothing: you'll never get any messages.
     *
     * If libusb was compiled with verbose debug message logging, this function
     * does nothing: you'll always get messages from all levels.
     *
     * @param context
     *            The {@link Context} to operate on, or NULL for the default
     *            context.
     * @param level
     *            The log level to set.
     */
    public static native void setDebug(final Context context, final int level);

    /**
     * Returns the version of the libusb runtime.
     *
     * @return The version of the libusb runtime.
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
        final boolean unrefDevices);

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
     * @param device
     *            A device.
     * @param path
     *            The array that should contain the port numbers. As per the USB
     *            3.0 specs, the current maximum limit for the depth is 7.
     * @return The number of elements filled, {@link #ERROR_OVERFLOW} if the
     *         array is too small
     */
    public static native int getPortNumbers(final Device device,
        final ByteBuffer path);

    /**
     * Get the list of all port numbers from root for the specified device.
     *
     * @deprecated Please use {@link #getPortNumbers(Device, ByteBuffer)}
     *             instead.
     *
     * @param context
     *            The context.
     * @param device
     *            A device.
     * @param path
     *            The array that should contain the port numbers. As per the USB
     *            3.0 specs, the current maximum limit for the depth is 7.
     * @return The number of elements filled, {@link #ERROR_OVERFLOW} if the
     *         array is too small
     */
    @Deprecated
    public static int getPortPath(final Context context, final Device device,
        final ByteBuffer path)
    {
        return getPortNumbers(device, path);
    }

    /**
     * Get the the parent from the specified device [EXPERIMENTAL].
     *
     * Please note that the reference count of the returned device is not
     * increased. As such, do not *ever* call {@link #unrefDevice(Device)}
     * directly on the returned Device.
     *
     * @param device
     *            A device
     * @return The device parent or NULL if not available. You should issue a
     *         {@link #getDeviceList(Context, DeviceList)} before calling this
     *         function and make sure that you only access the parent before
     *         issuing {@link #freeDeviceList(DeviceList, boolean)}. The reason
     *         is that libusb currently does not maintain a permanent list of
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
     * want {@link #getMaxIsoPacketSize(Device, byte)} instead.
     *
     * @param device
     *            A device.
     * @param endpoint
     *            Address of the endpoint in question.
     * @return the wMaxPacketSize value {@link #ERROR_NOT_FOUND} if the endpoint
     *         does not exist {@link #ERROR_OTHER} on other failure
     */
    public static native int getMaxPacketSize(final Device device,
        final byte endpoint);

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
     * {@link #setIsoPacketLengths(Transfer, int)} in order to set the length
     * field of every isochronous packet in a transfer.
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
        final byte endpoint);

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
     * @return 0 on success, {@link #ERROR_NO_MEM} on memory allocation failure,
     *         {@link #ERROR_ACCESS} if the user has insufficient permissions,
     *         {@link #ERROR_NO_DEVICE} if the device has been disconnected,
     *         another error on other failure
     */
    public static native int open(final Device device,
        final DeviceHandle handle);

    /**
     * Convenience function for finding a device with a particular
     * idVendor/idProduct combination.
     *
     * This function is intended for those scenarios where you are using libusb
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
     * @return A handle for the first found device or NULL on error or if the
     *         device could not be found.
     */
    public static native DeviceHandle openDeviceWithVidPid(
        final Context context, final short vendorId, final short productId);

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
     * Please note that the reference count of the returned device is not
     * increased. As such, do not *ever* call {@link #unrefDevice(Device)}
     * directly on the returned Device.
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
     * case libusb just returns 0 without doing anything.
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
        final int interfaceNumber, final int alternateSetting);

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
    public static native int clearHalt(final DeviceHandle handle,
        final byte endpoint);

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
     * Allocate up to numStreams USB bulk streams on the specified endpoints.
     * This function takes an array of endpoints rather then a single endpoint
     * because some protocols require that endpoints are setup with similar
     * stream ids. All endpoints passed in must belong to the same interface.
     *
     * Note that this function may return less streams then requested.
     *
     * Stream id 0 is reserved, and should not be used to communicate with
     * devices. If LibUsb.allocStreams() returns with a value of N, you may
     * use stream ids 1 to N.
     *
     * @param handle
     *            a device handle
     * @param numStreams
     *            number of streams to try to allocate
     * @param endpoints
     *            array of endpoints to allocate streams on
     * @return The number of streams allocated, or a LIBUSB_ERROR code
     * on failure.
     */
    public static native int allocStreams(final DeviceHandle handle,
        final int numStreams, final byte[] endpoints);

    /**
     * Free USB bulk streams allocated with LibUsb.allocStreams().
     *
     * Note streams are automatically free-ed when releasing an interface.
     *
     * @param handle
     *            a device handle
     * @param endpoints
     *            array of endpoints to allocate streams on
     * @return 0 on success, or a LIBUSB_ERROR code on failure.
     */
    public static native int freeStreams(final DeviceHandle handle,
        final byte[] endpoints);

    /**
     * Determine if a kernel driver is active on an interface.
     *
     * If a kernel driver is active, you cannot claim the interface, and libusb
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
     * Note that libusb itself also talks to the device through a special
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
     * Enable/disable libusb's automatic kernel driver detachment.
     *
     * When this is enabled libusb will automatically detach the kernel driver
     * on an interface when claiming the interface, and attach it when releasing
     * the interface.
     *
     * Automatic kernel driver detachment is disabled on newly opened device
     * handles by default.
     *
     * On platforms which do not have {@link #CAP_SUPPORTS_DETACH_KERNEL_DRIVER}
     * this function will return {@link #ERROR_NOT_SUPPORTED}, and libusb will
     * continue as if this function was never called.
     *
     * @param handle
     *            A device handle.
     * @param enable
     *            Whether to enable or disable auto kernel driver detachment
     * @return {@link #SUCCESS} on success, {@link #ERROR_NOT_SUPPORTED} on
     *         platforms where the functionality is not available.
     */
    public static native int setAutoDetachKernelDriver(
        final DeviceHandle handle, final boolean enable);

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
     * Set the language, and only the language, not the encoding! used for
     * translatable libusb messages.
     *
     * This takes a locale string in the default setlocale format: lang[-region]
     * or lang[_country_region][.codeset]. Only the lang part of the string is
     * used, and only 2 letter ISO 639-1 codes are accepted for it, such as
     * "de". The optional region, country_region or codeset parts are ignored.
     * This means that functions which return translatable strings will NOT
     * honor the specified encoding. All strings returned are encoded as UTF-8
     * strings.
     *
     * If {@link #setLocale(String)} is not called, all messages will be in
     * English.
     *
     * The following functions return translatable strings: libusb_strerror().
     * Note that the libusb log messages controlled through
     * {@link #setDebug(Context, int)} are not translated, they are always in
     * English.
     *
     * @param locale
     *            locale-string in the form of lang[_country_region][.codeset]
     *            or lang[-region], where lang is a 2 letter ISO 639-1 code.
     * @return {@link #SUCCESS} on success, {@link #ERROR_INVALID_PARAM} if the
     *         locale doesn't meet the requirements, {@link #ERROR_NOT_FOUND} if
     *         the requested language is not supported, a error code on other
     *         errors.
     */
    public static native int setLocale(final String locale);

    /**
     * Returns a string with a short description of the given error code, this
     * description is intended for displaying to the end user and will be in the
     * language set by {@link #setLocale(String)}.
     *
     * The messages always start with a capital letter and end without any dot.
     *
     * @param errcode
     *            The error code whose description is desired.
     * @return A short description of the error code.
     */
    public static native String strError(final int errcode);

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
    public static native short le16ToCpu(final short x);

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
    public static native short cpuToLe16(final short x);

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
     * Returns the size in bytes of the buffer that's required to hold all
     * of a device descriptor's data.
     *
     * @return buffer size in bytes
     */
    static native int deviceDescriptorStructSize();

    /**
     * Retrieve a string descriptor in C style ASCII.
     *
     * @param handle
     *            A device handle.
     * @param index
     *            The index of the descriptor to retrieve.
     * @param string
     *            Output buffer for ASCII string descriptor.
     * @return Number of bytes returned in data, or ERROR code on failure.
     */
    public static native int getStringDescriptorAscii(
        final DeviceHandle handle, final byte index, final StringBuffer string);

    /**
     * A simple wrapper around
     * {@link #getStringDescriptorAscii(DeviceHandle, byte, StringBuffer)}.
     * It simply returns the string (maximum length of 127) if possible. If not
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
        final byte index)
    {
        if ((handle == null) || (index == 0))
        {
            return null;
        }

        final StringBuffer buffer = new StringBuffer();

        if (getStringDescriptorAscii(handle, index, buffer) >= 0)
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
     * @see #getConfigDescriptor(Device, byte, ConfigDescriptor)
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
     * @see #getConfigDescriptorByValue(Device, byte, ConfigDescriptor)
     */
    public static native int getConfigDescriptor(final Device device,
        final byte index, final ConfigDescriptor descriptor);

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
     * @see #getConfigDescriptor(Device, byte, ConfigDescriptor)
     */
    public static native int getConfigDescriptorByValue(final Device device,
        final byte value, final ConfigDescriptor descriptor);

    /**
     * Free a configuration descriptor obtained from
     * {@link #getConfigDescriptor(Device, byte, ConfigDescriptor)} or
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
     * Get an endpoints superspeed endpoint companion descriptor (if any).
     *
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param endpointDescriptor
     *            Endpoint descriptor from which to get the superspeed endpoint
     *            companion descriptor.
     * @param companionDescriptor
     *            Output location for the superspeed endpoint companion
     *            descriptor. Only valid if 0 was returned. Must be freed with
     *            {@link #freeSsEndpointCompanionDescriptor
     *            (SsEndpointCompanionDescriptor)}
     *            after use.
     * @return {@link #SUCCESS} on success, {@link #ERROR_NOT_FOUND} if the
     *         descriptor does not exist, another error code on error
     */
    public static native int getSsEndpointCompanionDescriptor(
        final Context context, final EndpointDescriptor endpointDescriptor,
        final SsEndpointCompanionDescriptor companionDescriptor);

    /**
     * Free a superspeed endpoint companion descriptor obtained from
     * {@link #getSsEndpointCompanionDescriptor(Context, EndpointDescriptor,
     * SsEndpointCompanionDescriptor)}.
     *
     * It is safe to call this function with a NULL parameter, in which case the
     * function simply returns.
     *
     * @param companionDescriptor
     *            The superspeed endpoint companion descriptor to free
     */
    public static native void freeSsEndpointCompanionDescriptor(
        final SsEndpointCompanionDescriptor companionDescriptor);

    /**
     * Get a Binary Object Store (BOS) descriptor. This is a BLOCKING function,
     * which will send requests to the device.
     *
     * @param handle
     *            The handle of an open libusb device.
     * @param descriptor
     *            Output location for the BOS descriptor. Only valid if 0 was
     *            returned. Must be freed with
     *            {@link #freeBosDescriptor(BosDescriptor)} after use.
     * @return {@link #SUCCESS} on success, {@link #ERROR_NOT_FOUND} if the
     *         device doesn't have a BOS descriptor, another error code on error
     */
    public static native int getBosDescriptor(final DeviceHandle handle,
        final BosDescriptor descriptor);

    /**
     * Free a BOS descriptor obtained from
     * {@link #getBosDescriptor(DeviceHandle, BosDescriptor)}.
     *
     * It is safe to call this function with a NULL parameter, in which case the
     * function simply returns.
     *
     * @param descriptor
     *            The BOS descriptor to free.
     */
    public static native void freeBosDescriptor(final BosDescriptor descriptor);

    /**
     * Get an USB 2.0 Extension descriptor.
     *
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param devCapDescriptor
     *            Device Capability descriptor with a bDevCapabilityType of
     *            {@link #BT_USB_2_0_EXTENSION}.
     * @param extensionDescriptor
     *            Output location for the USB 2.0 Extension descriptor. Only
     *            valid if 0 was returned. Must be freed with
     *            {@link #freeUsb20ExtensionDescriptor(
     *            Usb20ExtensionDescriptor)} after use.
     * @return 0 on success a LIBUSB_ERROR code on error
     */
    public static native int getUsb20ExtensionDescriptor(final Context context,
        final BosDevCapabilityDescriptor devCapDescriptor,
        final Usb20ExtensionDescriptor extensionDescriptor);

    /**
     * Free a USB 2.0 Extension descriptor obtained from
     * {@link #getUsb20ExtensionDescriptor(Context, BosDevCapabilityDescriptor,
     * Usb20ExtensionDescriptor)}.
     *
     * It is safe to call this function with a NULL parameter, in which case
     * the function simply returns.
     *
     * @param extensionDescriptor
     *            The USB 2.0 Extension descriptor to free.
     */
    public static native void freeUsb20ExtensionDescriptor(
        final Usb20ExtensionDescriptor extensionDescriptor);

    /**
     * Get a SuperSpeed USB Device Capability descriptor.
     *
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param devCapDescriptor
     *            Device Capability descriptor with a bDevCapabilityType of
     *            {@link #BT_SS_USB_DEVICE_CAPABILITY}.
     * @param ssUsbDeviceCapabilityDescriptor
     *            Output location for the SuperSpeed USB Device Capability
     *            descriptor. Only valid if {@link #SUCCESS} was returned.
     *            Must be freed with
     *            {@link #freeSsUsbDeviceCapabilityDescriptor(
     *            SsUsbDeviceCapabilityDescriptor)} after use.
     * @return {@link #SUCCESS} on success, an error code on error.
     */
    public static native int getSsUsbDeviceCapabilityDescriptor(
        final Context context,
        final BosDevCapabilityDescriptor devCapDescriptor,
        final SsUsbDeviceCapabilityDescriptor ssUsbDeviceCapabilityDescriptor);

    /**
     * Free a SuperSpeed USB Device Capability descriptor obtained from
     * {@link #getSsUsbDeviceCapabilityDescriptor(Context,
     * BosDevCapabilityDescriptor, SsUsbDeviceCapabilityDescriptor)}.
     *
     * It is safe to call this function with a NULL parameter,
     * in which case the function simply returns.
     *
     * @param ssUsbDeviceCapabilityDescriptor
     *            The descriptor to free.
     */
    public static native void freeSsUsbDeviceCapabilityDescriptor(
        final SsUsbDeviceCapabilityDescriptor ssUsbDeviceCapabilityDescriptor);

    /**
     * Get a Container ID descriptor.
     *
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param devCapDescriptor
     *            Device Capability descriptor with a bDevCapabilityType of
     *            {@link #BT_CONTAINER_ID}.
     * @param containerIdDescriptor
     *            Output location for the Container ID descriptor. Only valid if
     *            {@link #SUCCESS} was returned. Must be freed with
     *            {@link #freeContainerIdDescriptor(ContainerIdDescriptor)}
     *            after use.
     * @return {@link #SUCCESS} on success or an error code on error
     */
    public static native int getContainerIdDescriptor(final Context context,
        final BosDevCapabilityDescriptor devCapDescriptor,
        final ContainerIdDescriptor containerIdDescriptor);

    /**
     * Free a Container ID descriptor obtained from
     * {@link #getContainerIdDescriptor(Context, BosDevCapabilityDescriptor,
     * ContainerIdDescriptor)}.
     *
     * It is safe to call this function with a NULL parameter, in which case
     * the function simply returns.
     *
     * @param containerIdDescriptor
     *            The descriptor to free.
     */
    public static native void freeContainerIdDescriptor(
        final ContainerIdDescriptor containerIdDescriptor);

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
    public static int getDescriptor(final DeviceHandle handle, final byte type,
        final byte index, final ByteBuffer data)
    {
        return controlTransfer(handle, ENDPOINT_IN, REQUEST_GET_DESCRIPTOR,
            (short) (((type & 0xff) << 8) | (index & 0xff)), (short) 0,
            data, 1000);
    }

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
     * @see #getStringDescriptorAscii(DeviceHandle, byte, StringBuffer)
     */
    public static int getStringDescriptor(final DeviceHandle handle,
        final byte index, final short langId, final ByteBuffer data)
    {
        return controlTransfer(handle, ENDPOINT_IN, REQUEST_GET_DESCRIPTOR,
            (short) ((DT_STRING << 8) | (index & 0xff)), langId, data, 1000);
    }

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
        final byte bmRequestType, final byte bRequest, final short wValue,
        final short wIndex, final ByteBuffer data, final long timeout);

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
     * Also check transferred when dealing with a timeout error code. libusb
     * may have to split your transfer into a number of chunks to satisfy
     * underlying O/S requirements, meaning that the timeout may expire after
     * the first few chunks have completed. libusb is careful not to lose any
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
        final byte endpoint, final ByteBuffer data,
        final IntBuffer transferred, final long timeout);

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
     * Also check transferred when dealing with a timeout error code. libusb
     * may have to split your transfer into a number of chunks to satisfy
     * underlying O/S requirements, meaning that the timeout may expire after
     * the first few chunks have completed. libusb is careful not to lose any
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
        final byte endpoint, final ByteBuffer data,
        final IntBuffer transferred, final long timeout);

    /**
     * Attempt to acquire the event handling lock.
     *
     * This lock is used to ensure that only one thread is monitoring libusb
     * event sources at any one time.
     *
     * You only need to use this lock if you are developing an application which
     * calls poll() or select() on libusb's file descriptors directly. If you
     * stick to libusb's event handling loop functions (e.g.
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
     * This lock is used to ensure that only one thread is monitoring libusb
     * event sources at any one time.
     *
     * You only need to use this lock if you are developing an application which
     * calls poll() or select() on libusb's file descriptors directly. If you
     * stick to libusb's event handling loop functions (e.g.
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
     * Sometimes, libusb needs to temporarily pause all event handlers, and
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
     * calls poll() or select() on libusb's file descriptors directly, and may
     * potentially be handling events from 2 threads simultaenously. If you
     * stick to libusb's event handling loop functions (e.g.
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
     * libusb determines "pending events" by checking if any timeouts have
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
     * The only way to implement this in Java is by passing a direct buffer, and
     * then accessing memory directly. IntBuffers can be direct, if they are
     * created as a view of a direct ByteBuffer, by using BufferUtils:
     * {@link BufferUtils#allocateIntBuffer()}
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
     *            The maximum time (In microseconds) to block waiting for
     *            events, or an all zero timeval struct for non-blocking mode
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
     * taken the event lock and are calling poll()/select() directly on libusb's
     * file descriptors (as opposed to using {@link #handleEvents(Context)} or
     * similar). You detect events on libusb's descriptors, so you then call
     * this function with a zero timeout value (while still holding the event
     * lock).
     *
     * @param context
     *            The context to operate on, or NULL for the default context.
     * @param timeout
     *            The maximum time (In microseconds) to block waiting for
     *            events, or zero for non-blocking mode
     * @return 0 on success, or a ERROR code on failure.
     */
    public static native int handleEventsLocked(final Context context,
        final long timeout);

    /**
     * Determines whether your application must apply special timing
     * considerations when monitoring libusb's file descriptors.
     *
     * This function is only useful for applications which retrieve and poll
     * libusb's file descriptors in their own main loop (The more advanced
     * option).
     *
     * Ordinarily, libusb's event handler needs to be called into at specific
     * moments in time (in addition to times when there is activity on the file
     * descriptor set). The usual approach is to use
     * {@link #getNextTimeout(Context, LongBuffer)} to learn about when the next
     * timeout occurs, and to adjust your poll()/select() timeout accordingly so
     * that you can make a call into the library at that time.
     *
     * Some platforms supported by libusb do not come with this baggage - any
     * events relevant to timing will be represented by activity on the file
     * descriptor set, and {@link #getNextTimeout(Context, LongBuffer)} will
     * always return 0. This function allows you to detect whether you are
     * running on such a platform.
     *
     * @param context
     *            The context to operate on, or NULL for the default context
     * @return 0 if you must call into libusb at times determined by
     *         {@link #getNextTimeout(Context, LongBuffer)}, or 1 if all timeout
     *         events are handled internally or through regular activity on the
     *         file descriptors.
     */
    public static native int pollfdsHandleTimeouts(final Context context);

    /**
     * Determine the next internal timeout that libusb needs to handle.
     *
     * You only need to use this function if you are calling poll() or select()
     * or similar on libusb's file descriptors yourself - you do not need to
     * use it if you are calling {@link #handleEvents(Context)} or a variant
     * directly.
     *
     * You should call this function in your main loop in order to determine how
     * long to wait for select() or poll() to return results. libusb needs to
     * be called into at this timeout, so you should use it as an upper bound on
     * your select() or poll() call.
     *
     * When the timeout has expired, call into
     * {@link #handleEventsTimeout(Context, long)} (perhaps in non-blocking
     * mode) so that libusb can handle the timeout.
     *
     * This function may return 1 (success) and an all-zero timeval. If this is
     * the case, it indicates that libusb has a timeout that has already
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
     *            in which libusb must be called into in order to process
     *            timeout events
     * @return 0 if there are no pending timeouts, 1 if a timeout was returned,
     *         or {@link #ERROR_OTHER} failure
     */
    public static native int getNextTimeout(final Context context,
        final LongBuffer timeout);

    /**
     * Register notification functions for file descriptor additions/removals.
     *
     * These functions will be invoked for every new or removed file descriptor
     * that libusb uses as an event source.
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
    public static synchronized void setPollfdNotifiers(final Context context,
        final PollfdListener listener, final Object userData)
    {
        long contextId;

        if (context == null)
        {
            // NULL pointer has value 0
            contextId = 0;
        }
        else
        {
            contextId = context.getPointer();
        }

        if (listener == null)
        {
            unsetPollfdNotifiersNative(context);

            pollfdListeners.remove(contextId);
        }
        else
        {
            setPollfdNotifiersNative(context, contextId);

            pollfdListeners.put(contextId,
                new ImmutablePair<PollfdListener, Object>(listener, userData));
        }
    }

    /**
     * Callback function, invoked when a new file descriptor should be added to
     * the set of file descriptors monitored for events.
     *
     * @param fd
     *            The new file descriptor,
     * @param events
     *            events to monitor for, see libusb_pollfd for a description
     * @param contextId
     *            A unique identifier for the originating context.
     */
    static void triggerPollfdAdded(final FileDescriptor fd, final int events,
        final long contextId)
    {
        final ImmutablePair<PollfdListener, Object> listener = pollfdListeners
            .get(contextId);

        if (listener != null)
        {
            listener.left.pollfdAdded(fd, events, listener.right);
        }
    }

    /**
     * Called internally from JNI when a pollfd was removed.
     *
     * @param fd
     *            The removed file descriptor.
     * @param contextId
     *            A unique identifier for the originating context.
     */
    static void triggerPollfdRemoved(final FileDescriptor fd,
        final long contextId)
    {
        final ImmutablePair<PollfdListener, Object> listener = pollfdListeners
            .get(contextId);

        if (listener != null)
        {
            listener.left.pollfdRemoved(fd, listener.right);
        }
    }

    /**
     * Configures libusb to inform this class about pollfd additions and
     * removals.
     *
     * @param context
     *            The context to operate on, or NULL for the default context
     * @param contextId
     *            A unique identifier for the given context.
     */
    static native void setPollfdNotifiersNative(final Context context,
        final long contextId);

    /**
     * Tells libusb to stop informing this class about pollfd additions and
     * removals.
     *
     * @param context
     *            The context to operate on, or NULL for the default context
     */
    static native void unsetPollfdNotifiersNative(final Context context);

    /**
     * Allocate a libusb transfer without support for isochronous transfers.
     *
     * The returned transfer is pre-initialized for you. When the new transfer
     * is no longer needed, it should be freed with
     * {@link #freeTransfer(Transfer)}.
     *
     * @return A newly allocated transfer, or NULL on error
     */
    public static Transfer allocTransfer()
    {
        return allocTransfer(0);
    }

    /**
     * Allocate a libusb transfer with a specified number of isochronous packet
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
     * {@link Transfer#setType(byte)} methods accordingly.
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
     * Please refer to {@link #TRANSFER_FREE_BUFFER} for an explanation
     * of how buffers are freed.
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

    /**
     * Submit a transfer.
     *
     * This function will fire off the USB transfer and then return immediately.
     *
     * @param transfer
     *            The transfer to submit
     * @return 0 on success, {@link #ERROR_NO_DEVICE} if the device has
     *         been
     *         disconnected, {@link #ERROR_BUSY} if the transfer has
     *         already been
     *         submitted. {@link #ERROR_NOT_SUPPORTED} if the transfer
     *         flags are
     *         not supported by the operating system. Another LIBUSB_ERROR code
     *         on failure.
     */
    public static native int submitTransfer(final Transfer transfer);

    /**
     * Asynchronously cancel a previously submitted transfer.
     *
     * This function returns immediately, but this does not indicate
     * cancellation
     * is complete. Your callback function will be invoked at some later time
     * with a transfer status of {@link #TRANSFER_CANCELLED}.
     *
     * @param transfer
     *            The transfer to cancel
     * @return 0 on success, {@link #ERROR_NOT_FOUND} if the transfer is
     *         already complete or cancelled. Another LIBUSB_ERROR code on
     *         failure.
     */
    public static native int cancelTransfer(final Transfer transfer);

    /**
     * Get the data section of a control transfer.
     *
     * This convenience function is here to remind you that the data does not
     * start until 8 bytes into the actual buffer, as the setup packet comes
     * first.
     *
     * Calling this function only makes sense from a transfer callback function,
     * or situations where you have already allocated a suitably sized buffer at
     * {@link Transfer#buffer()}.
     *
     * @param transfer
     *            A transfer.
     * @return The data section.
     */
    public static ByteBuffer controlTransferGetData(final Transfer transfer)
    {
        return BufferUtils.slice(transfer.buffer(), CONTROL_SETUP_SIZE,
            transfer.buffer().limit() - CONTROL_SETUP_SIZE);
    }

    /**
     * Get the control setup packet of a control transfer.
     *
     * This convenience function is here to remind you that the control setup
     * occupies the first 8 bytes of the transfer data buffer.
     *
     * Calling this function only makes sense from a transfer callback function,
     * or situations where you have already allocated a suitably sized buffer at
     * {@link Transfer#buffer()}.
     *
     * @param transfer
     *            A transfer.
     * @return The setup section.
     */
    public static ControlSetup controlTransferGetSetup(final Transfer transfer)
    {
        return new ControlSetup(transfer.buffer());
    }

    /**
     * Helper function to populate the setup packet (first 8 bytes of the data
     * buffer) for a control transfer.
     *
     * The wIndex, wValue and wLength values should be given in host-endian byte
     * order.
     *
     * @param buffer
     *            Buffer to output the setup packet into.
     * @param bmRequestType
     *            See {@link ControlSetup#bmRequestType()}.
     * @param bRequest
     *            See {@link ControlSetup#bRequest()}.
     * @param wValue
     *            See {@link ControlSetup#wValue()}.
     * @param wIndex
     *            See {@link ControlSetup#wIndex()}.
     * @param wLength
     *            See {@link ControlSetup#wLength()}.
     */
    public static void fillControlSetup(final ByteBuffer buffer,
        final byte bmRequestType, final byte bRequest, final short wValue,
        final short wIndex, final short wLength)
    {
        final ControlSetup setup = new ControlSetup(buffer);
        setup.setBmRequestType(bmRequestType);
        setup.setBRequest(bRequest);
        setup.setWValue(wValue);
        setup.setWIndex(wIndex);
        setup.setWLength(wLength);
    }

    /**
     * Helper function to populate the required {@link Transfer} fields for a
     * control transfer.
     *
     * If you pass a transfer buffer to this function, the first 8 bytes will be
     * interpreted as a control setup packet, and the wLength field will be used
     * to automatically populate the length field of the transfer. Therefore the
     * recommended approach is:
     *
     * 1. Allocate a suitably sized data buffer (including space for control
     * setup).
     *
     * 2. Call
     * {@link #fillControlSetup(ByteBuffer, byte, byte, short, short, short)}.
     *
     * 3. If this is a host-to-device transfer with a data stage, put the data
     * in place after the setup packet.
     *
     * 4. Call this function.
     *
     * 5. Call {@link #submitTransfer(Transfer)}.
     *
     * It is also legal to pass a NULL buffer to this function, in which case
     * this function will not attempt to populate the length field. Remember
     * that you must then populate the buffer and length fields later.
     *
     * @param transfer
     *            The transfer to populate.
     * @param handle
     *            Handle of the device that will handle the transfer.
     * @param buffer
     *            Data buffer. If provided, this function will interpret the
     *            first 8 bytes as a setup packet and infer the transfer length
     *            from that.
     * @param callback
     *            callback function to be invoked on transfer completion.
     * @param userData
     *            User data to pass to callback function.
     * @param timeout
     *            Timeout for the transfer in milliseconds.
     */
    public static void fillControlTransfer(final Transfer transfer,
        final DeviceHandle handle, final ByteBuffer buffer,
        final TransferCallback callback, final Object userData,
        final long timeout)
    {
        transfer.setDevHandle(handle);
        transfer.setEndpoint((byte) 0);
        transfer.setType(TRANSFER_TYPE_CONTROL);
        transfer.setTimeout(timeout);
        transfer.setBuffer(buffer);
        transfer.setUserData(userData);
        transfer.setCallback(callback);

        // Set length based on wLength from Control Setup.
        final ControlSetup setup = new ControlSetup(buffer);
        transfer.setLength(CONTROL_SETUP_SIZE + (setup.wLength() & 0xFFFF));
    }

    /**
     * Helper function to populate the required {@link Transfer} fields for a
     * bulk transfer.
     *
     * @param transfer
     *            The transfer to populate.
     * @param handle
     *            Handle of the device that will handle the transfer.
     * @param endpoint
     *            Address of the endpoint where this transfer will be sent.
     * @param buffer
     *            Data buffer.
     * @param callback
     *            Callback function to be invoked on transfer completion.
     * @param userData
     *            User data to pass to callback function.
     * @param timeout
     *            Timeout for the transfer in milliseconds.
     */
    public static void fillBulkTransfer(final Transfer transfer,
        final DeviceHandle handle, final byte endpoint,
        final ByteBuffer buffer, final TransferCallback callback,
        final Object userData, final long timeout)
    {
        transfer.setDevHandle(handle);
        transfer.setEndpoint(endpoint);
        transfer.setType(TRANSFER_TYPE_BULK);
        transfer.setTimeout(timeout);
        transfer.setBuffer(buffer);
        transfer.setUserData(userData);
        transfer.setCallback(callback);
    }

    /**
     * Helper function to populate the required {@link Transfer} fields
     * for a bulk transfer using bulk streams.
     *
     * @param transfer
     *            The transfer to populate.
     * @param handle
     *            Handle of the device that will handle the transfer.
     * @param endpoint
     *            Address of the endpoint where this transfer will be sent.
     * @param streamId
     *            Bulk stream id for this transfer.
     * @param buffer
     *            Data buffer.
     * @param callback
     *            Callback function to be invoked on transfer completion.
     * @param userData
     *            User data to pass to callback function.
     * @param timeout
     *            Timeout for the transfer in milliseconds.
     */
    public static void fillBulkStreamTransfer(final Transfer transfer,
        final DeviceHandle handle, final byte endpoint, final int streamId,
        final ByteBuffer buffer, final TransferCallback callback,
        final Object userData, final long timeout)
    {
        fillBulkTransfer(transfer, handle, endpoint, buffer, callback, userData, timeout);
        transfer.setType(TRANSFER_TYPE_BULK_STREAM);
        transfer.setStreamId(streamId);
    }

    /**
     * Helper function to populate the required {@link Transfer} fields for an
     * interrupt transfer.
     *
     * @param transfer
     *            The transfer to populate.
     * @param handle
     *            Handle of the device that will handle the transfer.
     * @param endpoint
     *            Address of the endpoint where this transfer will be sent.
     * @param buffer
     *            Data buffer.
     * @param callback
     *            Callback function to be invoked on transfer completion.
     * @param userData
     *            User data to pass to callback function.
     * @param timeout
     *            Timeout for the transfer in milliseconds.
     */
    public static void fillInterruptTransfer(final Transfer transfer,
        final DeviceHandle handle, final byte endpoint,
        final ByteBuffer buffer, final TransferCallback callback,
        final Object userData, final long timeout)
    {
        transfer.setDevHandle(handle);
        transfer.setEndpoint(endpoint);
        transfer.setType(TRANSFER_TYPE_INTERRUPT);
        transfer.setTimeout(timeout);
        transfer.setBuffer(buffer);
        transfer.setUserData(userData);
        transfer.setCallback(callback);
    }

    /**
     * Helper function to populate the required {@link Transfer} fields for an
     * isochronous transfer.
     *
     * @param transfer
     *            The transfer to populate.
     * @param handle
     *            Handle of the device that will handle the transfer.
     * @param endpoint
     *            Address of the endpoint where this transfer will be sent.
     * @param buffer
     *            Data buffer.
     * @param numIsoPackets
     *            The number of isochronous packets.
     * @param callback
     *            Callback function to be invoked on transfer completion.
     * @param userData
     *            User data to pass to callback function.
     * @param timeout
     *            Timeout for the transfer in milliseconds.
     */
    public static void fillIsoTransfer(final Transfer transfer,
        final DeviceHandle handle, final byte endpoint,
        final ByteBuffer buffer, final int numIsoPackets,
        final TransferCallback callback, final Object userData,
        final long timeout)
    {
        transfer.setDevHandle(handle);
        transfer.setEndpoint(endpoint);
        transfer.setType(TRANSFER_TYPE_ISOCHRONOUS);
        transfer.setTimeout(timeout);
        transfer.setBuffer(buffer);
        transfer.setNumIsoPackets(numIsoPackets);
        transfer.setUserData(userData);
        transfer.setCallback(callback);
    }

    /**
     * Convenience function to set the length of all packets in an isochronous
     * transfer, based on the {@link Transfer#numIsoPackets()} field.
     *
     * @param transfer
     *            A transfer.
     * @param length
     *            The length to set in each isochronous packet descriptor.
     * @see #getMaxPacketSize(Device, byte)
     */
    public static void setIsoPacketLengths(final Transfer transfer,
        final int length)
    {
        for (final IsoPacketDescriptor isoDesc : transfer.isoPacketDesc())
        {
            isoDesc.setLength(length);
        }
    }

    /**
     * Convenience function to locate the position of an isochronous packet
     * within the buffer of an isochronous transfer.
     *
     * This is a thorough function which loops through all preceding packets,
     * accumulating their lengths to find the position of the specified packet.
     * Typically you will assign equal lengths to each packet in the transfer,
     * and hence the above method is sub-optimal. You may wish to use
     * {@link #getIsoPacketBufferSimple(Transfer, int)} instead.
     *
     * @param transfer
     *            A transfer.
     * @param packet
     *            The packet to return the address of.
     * @return The base address of the packet buffer inside the transfer buffer,
     *         or NULL if the packet does not exist.
     * @see #getIsoPacketBufferSimple(Transfer, int)
     */
    public static ByteBuffer getIsoPacketBuffer(final Transfer transfer,
        final int packet)
    {
        if (packet >= transfer.numIsoPackets())
        {
            return null;
        }

        final IsoPacketDescriptor[] isoDescriptors = transfer.isoPacketDesc();
        int offset = 0;

        for (int i = 0; i < packet; i++)
        {
            offset += isoDescriptors[i].length();
        }

        return BufferUtils.slice(transfer.buffer(), offset,
            isoDescriptors[packet].length());
    }

    /**
     * Convenience function to locate the position of an isochronous packet
     * within the buffer of an isochronous transfer, for transfers where each
     * packet is of identical size.
     *
     * This function relies on the assumption that every packet within the
     * transfer is of identical size to the first packet. Calculating the
     * location of the packet buffer is then just a simple calculation: buffer +
     * (packet_size * packet)
     *
     * Do not use this function on transfers other than those that have
     * identical packet lengths for each packet.
     *
     * @param transfer
     *            A transfer.
     * @param packet
     *            The packet to return the address of.
     * @return The base address of the packet buffer inside the transfer buffer,
     *         or NULL if the packet does not exist.
     * @see #getIsoPacketBuffer(Transfer, int)
     */
    public static ByteBuffer getIsoPacketBufferSimple(final Transfer transfer,
        final int packet)
    {
        if (packet >= transfer.numIsoPackets())
        {
            return null;
        }

        final IsoPacketDescriptor[] isoDescriptors = transfer.isoPacketDesc();
        final int offset = isoDescriptors[0].length() * packet;

        return BufferUtils.slice(transfer.buffer(), offset,
            isoDescriptors[packet].length());
    }

    /**
     * Processes a hotplug event from native code.
     *
     * @param context
     *            Context of this notification.
     * @param device
     *            The device this event occurred on.
     * @param event
     *            Event that occurred
     * @param hotplugId
     *            The hotplug ID.
     * @return Whether this callback is finished processing events. Returning 1
     *         will cause this callback to be deregistered.
     */
    static int hotplugCallback(final Context context, final Device device,
        final int event, final long hotplugId)
    {
        final ImmutablePair<HotplugCallback, Object> callback = hotplugCallbacks
            .get(hotplugId);

        int result = 0;

        if (callback != null)
        {
            result = callback.left.processEvent(context, device, event,
                callback.right);
        }

        // If callback indicates it is finished, it will get deregistered
        // automatically. As such, we have to remove it from the Java
        // map, like when deregistering manually.
        if (result == 1)
        {
            hotplugCallbacks.remove(hotplugId);
        }

        return result;
    }

    /**
     * Register a hotplug callback function.
     *
     * Register a callback with the {@link Context}. The callback will fire
     * when a matching event occurs on a matching device. The callback is
     * armed until either it is deregistered with
     * {@link #hotplugDeregisterCallback(Context, HotplugCallbackHandle)} or the
     * supplied callback returns 1 to indicate it is finished processing
     * events.
     *
     * @param context
     *            Context to register this callback with.
     * @param events
     *            Bitwise or of events that will trigger this callback.
     * @param flags
     *            Hotplug callback flags.
     * @param vendorId
     *            The vendor id to match or {@link #HOTPLUG_MATCH_ANY}.
     * @param productId
     *            The product id to match or {@link #HOTPLUG_MATCH_ANY}.
     * @param deviceClass
     *            The device class to match or {@link #HOTPLUG_MATCH_ANY}.
     * @param callback
     *            The function to be invoked on a matching event/device
     * @param userData
     *            User data to pass to the callback function.
     * @param callbackHandle
     *            Hotplug callback handle of the allocated callback. Only needed
     *            if you later want to deregister this callback, can be NULL.
     * @return {@link #SUCCESS} on success, some ERROR code on failure.
     */
    public static synchronized int hotplugRegisterCallback(
        final Context context, final int events, final int flags,
        final int vendorId, final int productId, final int deviceClass,
        final HotplugCallback callback, final Object userData,
        final HotplugCallbackHandle callbackHandle)
    {
        if (callback == null)
        {
            throw new IllegalArgumentException("callback must not be null");
        }

        // Callback must be added to our own list before registering it in
        // libusb because otherwise we won't get the enumeration events
        hotplugCallbacks.put(globalHotplugId,
            new ImmutablePair<HotplugCallback, Object>(callback, userData));

        // Mask the values for conversion to int in libusb API.
        final int result = hotplugRegisterCallbackNative(
            context, events, flags,
            (vendorId == LibUsb.HOTPLUG_MATCH_ANY) ? (LibUsb.HOTPLUG_MATCH_ANY)
                : (vendorId & 0xFFFF),
            (productId == LibUsb.HOTPLUG_MATCH_ANY) ? (LibUsb.HOTPLUG_MATCH_ANY)
                : (productId & 0xFFFF),
            (deviceClass == LibUsb.HOTPLUG_MATCH_ANY) ?
                (LibUsb.HOTPLUG_MATCH_ANY) : (deviceClass & 0xFF),
            callbackHandle, globalHotplugId);

        if (result == LibUsb.SUCCESS)
        {
            // Increment globalHotplugId by one, like the libusb handle.
            globalHotplugId++;
        }
        else
        {
            // When registration failed then remove the hotplug callback from
            // our list.
            hotplugCallbacks.remove(globalHotplugId);
        }

        return result;
    }

    /**
     * Internally called native method for registering a hotplug callback.
     *
     * @param context
     *            Context to register this callback with.
     * @param events
     *            Bitwise or of events that will trigger this callback.
     * @param flags
     *            Hotplug callback flags.
     * @param vendorId
     *            The vendor id to match or {@link #HOTPLUG_MATCH_ANY}.
     * @param productId
     *            The product id to match or {@link #HOTPLUG_MATCH_ANY}.
     * @param deviceClass
     *            The device class to match or {@link #HOTPLUG_MATCH_ANY}.
     * @param callbackHandle
     *            Hotplug callback handle of the allocated callback. Only needed
     *            if you later want to deregister this callback, can be NULL.
     * @param hotplugId
     *            The hotplug callback ID.
     * @return {@link #SUCCESS} on success, some ERROR code on failure.
     */
    static native int hotplugRegisterCallbackNative(final Context context,
        final int events, final int flags, final int vendorId,
        final int productId, final int deviceClass,
        final HotplugCallbackHandle callbackHandle, final long hotplugId);

    /**
     * Deregisters a hotplug callback.
     *
     * Deregister a callback from a {@link Context}. This function is safe to
     * call from within a hotplug callback.
     *
     * @param context
     *            context this callback is registered with
     * @param callbackHandle
     *            the handle of the callback to deregister
     */
    public static void hotplugDeregisterCallback(final Context context,
        final HotplugCallbackHandle callbackHandle)
    {
        final long handle = hotplugDeregisterCallbackNative(context,
            callbackHandle);

        // When a handle is assigned by a register call, its value is the same
        // as the one of globalHotplugId at that moment, which is what's used
        // to identify data in the hotplugCallbacks map.
        // This is because globalHotplugId pretty much mirrors the behavior of
        // the handle: integer starting at 1, incremented each time by one.
        // Problems could arise from concurrency, but are completely avoided by
        // fully serializing register calls.
        // As such, we can use the handle value from callbackHandle to
        // correctly remove the data from the hotplugCallbacks map.
        hotplugCallbacks.remove(handle);
    }

    /**
     * Internally called native method for unregistering a hotplug callback.
     *
     * Deregister a callback from a {@link Context}. This function is safe to
     * call from within a hotplug callback.
     *
     * @param context
     *            context this callback is registered with
     * @param callbackHandle
     *            the handle of the callback to deregister
     * @return The hotplug callback ID.
     */
    static native long hotplugDeregisterCallbackNative(final Context context,
        final HotplugCallbackHandle callbackHandle);
}
