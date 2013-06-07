/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name LibUsb
 *
 * Native methods for the LibUsb class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <stdlib.h>
#include <time.h>
#include "usb4java.h"
#include "Version.h"
#include "Context.h"
#include "Device.h"
#include "DeviceHandle.h"
#include "DeviceList.h"
#include "DeviceDescriptor.h"
#include "ConfigDescriptor.h"
#include "Transfer.h"

static JavaVM *jvm;

/**
 * Version getVersion()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, getVersion)
(
    JNIEnv *env, jclass class
)
{
    return wrapVersion(env, libusb_get_version());
}

/**
 * int init(Context)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, init)
(
    JNIEnv *env, jclass class, jobject context
)
{
    if (!context)
    {
        return libusb_init(NULL);
    }
    else
    {
    	NOT_SET(env, context, "contextPointer", return 0);

        libusb_context *ctx;
        int result = libusb_init(&ctx);
        if (!result) setContext(env, ctx, context);
        return result;
    }
}

/**
 * void exit(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, exit)
(
    JNIEnv *env, jclass class, jobject context
)
{
	if (!context)
	{
		libusb_exit(NULL);
	}
	else
	{
		libusb_context *ctx = unwrapContext(env, context);
		if (!ctx) return;

        libusb_exit(ctx);

        resetContext(env, context);
	}
}

/**
 * void setDebug(Context, int)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, setDebug)
(
    JNIEnv *env, jclass class, jobject context, jint level
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_set_debug(ctx, level);
}

/**
 * int getDeviceList(Context, DeviceList)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getDeviceList)
(
    JNIEnv *env, jclass class, jobject context, jobject deviceList
)
{
    NOT_NULL(env, deviceList, return 0);
    NOT_SET(env, deviceList, "deviceListPointer", return 0);
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    libusb_device **list;
    ssize_t result = libusb_get_device_list(ctx, &list);
    if (result >= 0) setDeviceList(env, list, result, deviceList);
    return result;
}

/**
 * void freeDeviceList(DeviceList, boolean)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeDeviceList)
(
    JNIEnv *env, jclass class, jobject deviceList, jboolean unrefDevices
)
{
    NOT_NULL(env, deviceList, return);
    libusb_device **list = unwrapDeviceList(env, deviceList);
    if (!list) return;

    libusb_free_device_list(list, unrefDevices);
    resetDeviceList(env, deviceList);
}

/**
 * int getBusNumber(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getBusNumber)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    return libusb_get_bus_number(dev);
}

/**
 * int getPortNumber(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getPortNumber)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    #if defined(LIBUSBX_API_VERSION)
        return libusb_get_port_number(dev);
    #else
        return 0;
    #endif
}

/**
 * int getPortPath(Context, Device, ByteBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getPortPath)
(
    JNIEnv *env, jclass class, jobject context, jobject device, jobject path
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, path, return 0);
    DIRECT_BUFFER(env, path, ptr, return 0);
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    #if defined(LIBUSBX_API_VERSION)
        jlong size = (*env)->GetDirectBufferCapacity(env, path);

        return libusb_get_port_path(ctx, dev, ptr, size);
    #else
        return 0;
    #endif
}

/**
 * Device getParent(Device)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, getParent)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return NULL);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return NULL;

    #if defined(LIBUSBX_API_VERSION)
        return wrapDevice(env, libusb_get_parent(dev));
    #else
        return NULL;
    #endif
}

/**
 * int getDeviceAddress(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getDeviceAddress)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    return libusb_get_device_address(dev);
}

/**
 * int getDeviceSpeed(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getDeviceSpeed)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    return libusb_get_device_speed(dev);
}

/**
 * int getMaxPacketSize(Device, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getMaxPacketSize)
(
    JNIEnv *env, jclass class, jobject device, jint endpoint
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    return libusb_get_max_packet_size(dev, endpoint);
}

/**
 * int getMaxIsoPacketSize(Device, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getMaxIsoPacketSize)
(
    JNIEnv *env, jclass class, jobject device, jint endpoint
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    return libusb_get_max_iso_packet_size(dev, endpoint);
}

/**
 * Device refDevice(Device)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, refDevice)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return NULL);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return NULL;

    libusb_ref_device(dev);
    return device;
}

/**
 * void unrefDevice(Device)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, unrefDevice)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return;

    libusb_unref_device(dev);
}

/**
 * int open(Device, DeviceHandle)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, open)
(
    JNIEnv *env, jclass class, jobject device, jobject handle
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, handle, return 0);
    NOT_SET(env, handle, "deviceHandlePointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    libusb_device_handle *deviceHandle;
    int result = libusb_open(dev, &deviceHandle);
    if (!result) setDeviceHandle(env, deviceHandle, handle);
    return result;
}

/**
 * DeviceHandle openDeviceWithVidPid(Context, int, int)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, openDeviceWithVidPid)
(
    JNIEnv *env, jclass class, jobject context, jint vendorId,
    jint productId
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return NULL;

    return wrapDeviceHandle(env, libusb_open_device_with_vid_pid(
        ctx, vendorId, productId));
}

/**
 * void close(DeviceHandle)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, close)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    NOT_NULL(env, handle, return);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return;

    libusb_close(dev_handle);
    resetDeviceHandle(env, handle);
}

/**
 * Device getDevice(DeviceHandle)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, getDevice)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    NOT_NULL(env, handle, return NULL);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return NULL;

    return wrapDevice(env, libusb_get_device(dev_handle));
}

/**
 * int getConfiguration(DeviceHandle, IntBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getConfiguration)
(
    JNIEnv *env, jclass class, jobject handle, jobject buffer
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, buffer, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    int config;
    int result = libusb_get_configuration(dev_handle, &config);
    if (!result)
    {
        jclass cls = (*env)->GetObjectClass(env, buffer);
        jmethodID method = (*env)->GetMethodID(env, cls, "put", "(II)Ljava/nio/IntBuffer;");
        (*env)->CallVoidMethod(env, buffer, method, 0, config);
    }
    return result;
}

/**
 * int setConfiguration(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, setConfiguration)
(
    JNIEnv *env, jclass class, jobject handle, jint config
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_set_configuration(dev_handle, config);
}

/**
 * int claimInterface(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, claimInterface)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_claim_interface(dev_handle, iface);
}

/**
 * int releaseInterface(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, releaseInterface)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_release_interface(dev_handle, iface);
}

/**
 * int setInterfaceAltSetting(DeviceHandle, int, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, setInterfaceAltSetting)
(
    JNIEnv *env, jclass class, jobject handle, jint iface, jint setting
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_set_interface_alt_setting(dev_handle, iface, setting);
}

/**
 * int clearHalt(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, clearHalt)
(
    JNIEnv *env, jclass class, jobject handle, jint endpoint
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_clear_halt(dev_handle, endpoint);
}

/**
 * int resetDevice(DeviceHandle)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, resetDevice)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_reset_device(dev_handle);
}

/**
 * int kernelDriverActive(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, kernelDriverActive)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_kernel_driver_active(dev_handle, iface);
}

/**
 * int detachKernelDriver(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, detachKernelDriver)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_detach_kernel_driver(dev_handle, iface);
}

/**
 * int attachKernelDriver(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, attachKernelDriver)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    return libusb_attach_kernel_driver(dev_handle, iface);
}

/**
 * boolean hasCapability(int)
 */
JNIEXPORT jboolean JNICALL METHOD_NAME(LibUsb, hasCapability)
(
    JNIEnv *env, jclass class, jint capability
)
{
    return libusb_has_capability(capability);
}

/**
 * string errorName(int)
 */
JNIEXPORT jstring JNICALL METHOD_NAME(LibUsb, errorName)
(
    JNIEnv *env, jobject this, jint code
)
{
    return (*env)->NewStringUTF(env, libusb_error_name(code));
}

/**
 * int le16ToCpu(int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, le16ToCpu)
(
    JNIEnv *env, jobject this, jint x
)
{
    return libusb_le16_to_cpu(x);
}

/**
 * int cpuToLe16(int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, cpuToLe16)
(
    JNIEnv *env, jobject this, jint x
)
{
    return libusb_cpu_to_le16(x);
}

/**
 * int getDeviceDescriptor(Device, DeviceDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getDeviceDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jobject descriptor
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    NOT_SET(env, descriptor, "deviceDescriptorPointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    struct libusb_device_descriptor *data =
        malloc(sizeof(struct libusb_device_descriptor));
    int result = libusb_get_device_descriptor(dev, data);
    if (!result) setDeviceDescriptor(env, data, descriptor);
    return result;
}

/**
 * void freeDeviceDescriptor(DeviceDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeDeviceDescriptor)
(
    JNIEnv *env, jclass class, jobject descriptor
)
{
    NOT_NULL(env, descriptor, return);
    struct libusb_device_descriptor *data = unwrapDeviceDescriptor(env,
        descriptor);
    if (!data) return;

    free(data);
    resetDeviceDescriptor(env, descriptor);
}

/**
 * int getStringDescriptorAscii(DeviceHandle, int, StringBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getStringDescriptorAscii)
(
    JNIEnv *env, jclass class, jobject handle, jint index, jobject string
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, string, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    // Maximum size of a descriptor is 256 bytes, -2 for length/type = 254, /2 because of Unicode = 127 characters
    // and then +1 for the terminating NUL byte for C strings (the descriptor itself doesn't necessarily have one!).
    unsigned char buffer[127 + 1];
    int result = libusb_get_string_descriptor_ascii(
        dev_handle, index, buffer, 127);
    if (result >= 0)
    {
        buffer[result] = 0x00;
        jobject tmp = (*env)->NewStringUTF(env, (char *) buffer);
        jclass cls = (*env)->GetObjectClass(env, string);
        jmethodID method = (*env)->GetMethodID(env, cls, "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
        (*env)->CallObjectMethod(env, string, method, tmp);
    }
    return result;
}

/**
 * int getActiveConfigDescriptor(Device, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getActiveConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jobject descriptor)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    NOT_SET(env, descriptor, "configDescriptorPointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    struct libusb_config_descriptor *config;
    int result = libusb_get_active_config_descriptor(dev, &config);
    if (!result) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * int getConfigDescriptor(Device, int, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jint index, jobject descriptor
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    NOT_SET(env, descriptor, "configDescriptorPointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    struct libusb_config_descriptor *config;
    int result = libusb_get_config_descriptor(dev, index, &config);
    if (!result) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * int getConfigDescriptorByValue(Device, int, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getConfigDescriptorByValue)
(
    JNIEnv *env, jclass class, jobject device, jint index, jobject descriptor
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    NOT_SET(env, descriptor, "configDescriptorPointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;

    struct libusb_config_descriptor *config;
    int result = libusb_get_config_descriptor_by_value(
        dev, index, &config);
    if (!result) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * void freeConfigDescriptor(ConfigDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject descriptor
)
{
    NOT_NULL(env, descriptor, return);
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, 
        descriptor);
    if (!config) return;

    libusb_free_config_descriptor(config);
    resetConfigDescriptor(env, descriptor);
}

/**
 * int getDescriptor(DeviceHandle, int, int, ByteBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getDescriptor)
(
    JNIEnv *env, jclass class, jobject handle, jint type, jint index,
    jobject data
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    DIRECT_BUFFER(env, data, ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    jlong size = (*env)->GetDirectBufferCapacity(env, data);
    return libusb_get_descriptor(dev_handle, type, index, ptr, size);
}

/**
 * int getStringDescriptor(DeviceHandle, int, int, ByteBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getStringDescriptor)
(
    JNIEnv *env, jclass class, jobject handle, jint index, jint langId,
    jobject data
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    DIRECT_BUFFER(env, data, ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    jlong size = (*env)->GetDirectBufferCapacity(env, data);
    return libusb_get_string_descriptor(dev_handle, index, langId, ptr, size);
}

/**
 * int controlTransfer(DeviceHandle, int, int, int, int, ByteBuffer, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, controlTransfer)
(
    JNIEnv *env, jclass class, jobject handle, jint bmRequestType,
    jint bRequest, jint wValue, jint wIndex, jobject data, jint timeout
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    DIRECT_BUFFER(env, data, ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    jlong size = (*env)->GetDirectBufferCapacity(env, data);
    return libusb_control_transfer(dev_handle, bmRequestType, bRequest, 
        wValue, wIndex, ptr, size, timeout);
}

/**
 * int bulkTransfer(DeviceHandle, int, ByteBuffer, IntBuffer, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, bulkTransfer)
(
    JNIEnv *env, jclass class, jobject handle, jint endpoint,
    jobject data, jobject transferred, jint timeout
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    NOT_NULL(env, transferred, return 0);
    DIRECT_BUFFER(env, data, ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    int sent;
    jlong size = (*env)->GetDirectBufferCapacity(env, data);
    int result = libusb_bulk_transfer(dev_handle, endpoint, ptr, size, &sent, 
        timeout);
    if (!result)
    {
        jclass cls = (*env)->GetObjectClass(env, transferred);
        jmethodID method = (*env)->GetMethodID(env, cls, "put",
            "(II)Ljava/nio/IntBuffer;");
        (*env)->CallVoidMethod(env, transferred, method, 0, sent);
    }
    return result;
}

/**
 * int interruptTransfer(DeviceHandle, int, ByteBuffer, IntBuffer, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, interruptTransfer)
(
    JNIEnv *env, jclass class, jobject handle, jint endpoint,
    jobject data, jobject transferred, jint timeout
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    NOT_NULL(env, transferred, return 0);
    DIRECT_BUFFER(env, data, ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;

    int sent;
    jlong size = (*env)->GetDirectBufferCapacity(env, data);
    int result = libusb_interrupt_transfer(dev_handle, endpoint, ptr, size, 
        &sent, timeout);
    if (!result)
    {
        jclass cls = (*env)->GetObjectClass(env, transferred);
        jmethodID method = (*env)->GetMethodID(env, cls, "put",
            "(II)Ljava/nio/IntBuffer;");
        (*env)->CallVoidMethod(env, transferred, method, 0, sent);
    }
    return result;
}

/**
 * int tryLockEvents(Context)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, tryLockEvents)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    return libusb_try_lock_events(ctx);
}

/**
 * void lockEvents(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, lockEvents)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_lock_events(ctx);
}

/**
 * void unlockEvents(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, unlockEvents)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_unlock_events(ctx);
}

/**
 * int eventHandlingOk(Context)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, eventHandlingOk)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    return libusb_event_handling_ok(ctx);
}

/**
 * int eventHandlerActive(Context)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, eventHandlerActive)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    return libusb_event_handler_active(ctx);
}

/**
 * void lockEventWaiters(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, lockEventWaiters)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_lock_event_waiters(ctx);
}

/**
 * void unlockEventWaiters(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, unlockEventWaiters)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_unlock_event_waiters(ctx);
}

/**
 * int waitForEvent(Context, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, waitForEvent)
(
    JNIEnv *env, jclass class, jobject context, jlong timeout
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    struct timeval tv;
    tv.tv_sec = timeout / 1000000;
    tv.tv_usec = timeout % 1000000;
    return libusb_wait_for_event(ctx, &tv);
}

/**
 * int handleEventsTimeoutCompleted(Context, long, IntBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEventsTimeoutCompleted)
(
    JNIEnv *env, jclass class, jobject context, jlong timeout,
    jobject completed
)
{
    int *complete = NULL;
    if (completed) {
        DIRECT_BUFFER(env, completed, complete_tmp, return 0);
        complete = (int *) complete_tmp;
    }

    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    struct timeval tv;
    tv.tv_sec = timeout / 1000000;
    tv.tv_usec = timeout % 1000000;

    return libusb_handle_events_timeout_completed(ctx, &tv, complete);
}

/**
 * int handleEventsTimeout(Context, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEventsTimeout)
(
    JNIEnv *env, jclass class, jobject context, jlong timeout
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    struct timeval tv;
    tv.tv_sec = timeout / 1000000;
    tv.tv_usec = timeout % 1000000;
    return libusb_handle_events_timeout(ctx, &tv);
}

/**
 * int handleEvents(Context)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEvents)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    return libusb_handle_events(ctx);
}

/**
 * int handleEventsCompleted(Context, IntBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEventsCompleted)
(
    JNIEnv *env, jclass class, jobject context, jobject completed
)
{
    int *complete = NULL;
    if (completed) {
        DIRECT_BUFFER(env, completed, complete_tmp, return 0);
        complete = (int *) complete_tmp;
    }

    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    return libusb_handle_events_completed(ctx, complete);
}

/**
 * int handleEventsLocked(Context, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEventsLocked)
(
    JNIEnv *env, jclass class, jobject context, jlong timeout
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    struct timeval tv;
    tv.tv_sec = timeout / 1000000;
    tv.tv_usec = timeout % 1000000;
    return libusb_handle_events_locked(ctx, &tv);
}

/**
 * int pollfdsHandleTimeouts(Context)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, pollfdsHandleTimeouts)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    return libusb_pollfds_handle_timeouts(ctx);
}

/**
 * int getNextTimeout(Context, LongBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getNextTimeout)
(
    JNIEnv *env, jclass class, jobject context, jobject timeout
)
{
    NOT_NULL(env, timeout, return 0);
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;

    struct timeval tv;
    int result = libusb_get_next_timeout(ctx, &tv);
    if (result == 1)
    {
        jclass cls = (*env)->GetObjectClass(env, timeout);
        jmethodID method = (*env)->GetMethodID(env, cls, "put",
            "(IJ)Ljava/nio/LongBuffer;");
        (*env)->CallVoidMethod(env, timeout, method, 0,
            tv.tv_sec * 1000000 + tv.tv_usec);
    }
    return result;
}

static void LIBUSB_CALL triggerPollfdAdded(int fd, short events, void *user_data)
{
    THREAD_BEGIN(env)

    jclass fdcls = (*env)->FindClass(env, "java/io/FileDescriptor");
    jmethodID constructor = (*env)->GetMethodID(env, fdcls, "<init>", "(I)V");
    jobject object = (*env)->NewObject(env, fdcls, constructor, fd);

    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/LibUsb");
    jmethodID method = (*env)->GetStaticMethodID(env, cls,
        "triggerPollfdAdded", "(Ljava/io/FileDescriptor;I)V");
    (*env)->CallStaticVoidMethod(env, cls, method, object, events);

    THREAD_END
}

static void LIBUSB_CALL triggerPollfdRemoved(int fd, void *user_data)
{
    THREAD_BEGIN(env)

    jclass fdcls = (*env)->FindClass(env, "java/io/FileDescriptor");
    jmethodID constructor = (*env)->GetMethodID(env, fdcls, "<init>", "(I)V");
    jobject object = (*env)->NewObject(env, fdcls, constructor, fd);

    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/LibUsb");
    jmethodID method = (*env)->GetStaticMethodID(env, cls,
        "triggerPollfdRemoved", "(Ljava/io/FileDescriptor;)V");
    (*env)->CallStaticVoidMethod(env, cls, method, object);

    THREAD_END
}

/**
 * void setPollfdNotifiers(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, setPollfdNotifiers)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    (*env)->GetJavaVM(env, &jvm);
    libusb_set_pollfd_notifiers(ctx, &triggerPollfdAdded, &triggerPollfdRemoved,
        NULL);
}

/**
 * void unsetPollfdNotifiers(Context)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, unsetPollfdNotifiers)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_set_pollfd_notifiers(ctx, NULL, NULL, NULL);
}

/**
 * Transfer allocTransfer(int)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, allocTransfer)
(
    JNIEnv *env, jclass class, jint isoPackets
)
{
    return wrapTransfer(env, libusb_alloc_transfer(isoPackets));
}

/**
 * void freeTransfer(Transfer)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeTransfer)
(
    JNIEnv *env, jclass class, jobject transfer
)
{
    NOT_NULL(env, transfer, return);
    struct libusb_transfer *handle = unwrapTransfer(env, transfer);
    if (!handle) return;

    libusb_free_transfer(handle);
    resetTransfer(env, transfer);
}
