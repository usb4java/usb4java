/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name LibUSB
 *
 * Native methods for the LibUSB class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <stdlib.h>
#include "usb4java.h"
#include "Version.h"
#include "Context.h"
#include "Device.h"
#include "DeviceHandle.h"
#include "DeviceList.h"
#include "DeviceDescriptor.h"
#include "ConfigDescriptor.h"

/**
 * Version getVersion()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, getVersion)
(
    JNIEnv *env, jclass class
)
{
    return wrapVersion(env, libusb_get_version());
}

/**
 * int init()
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, init)
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
        libusb_context *ctx;
        int result = libusb_init(&ctx);
        setContext(env, ctx, context);
        return result;
    }
}

/**
 * void exit()
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, exit)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_exit(unwrapContext(env, context));
}

/**
 * void setDebug(Context, int)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, setDebug)
(
    JNIEnv *env, jclass class, jobject context, jint level
)
{
    libusb_set_debug(unwrapContext(env, context), level);
}

/**
 * int getDeviceList(Context, DeviceList)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getDeviceList)
(
    JNIEnv *env, jclass class, jobject context, jobject deviceList
)
{
    NOT_NULL(env, deviceList, return 0);
    libusb_device **list;
    ssize_t result = libusb_get_device_list(unwrapContext(env, context),
        &list);
    if (result >= 0) setDeviceList(env, list, result, deviceList);
    return result;
}

/**
 * void freeDeviceList(DeviceList, boolean)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, freeDeviceList)
(
    JNIEnv *env, jclass class, jobject deviceList, jboolean unrefDevices
)
{
    NOT_NULL(env, deviceList, return);
    libusb_free_device_list(unwrapDeviceList(env, deviceList), unrefDevices);
}

/**
 * int getBusNumber(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getBusNumber)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    return libusb_get_bus_number(unwrapDevice(env, device));
}

/**
 * int getPortNumber(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getPortNumber)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    return libusb_get_port_number(unwrapDevice(env, device));
}

/**
 * int getPortPath(Context, Device, byte[])
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getPortPath)
(
    JNIEnv *env, jclass class, jobject context, jobject device, jbyteArray path
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, path, return 0);
    jsize size = (*env)->GetArrayLength(env, path);
    unsigned char buffer[size];
    int result = libusb_get_port_path(unwrapContext(env, context),
        unwrapDevice(env, device), buffer, size);
    if (result > 0) (*env)->SetByteArrayRegion(env, path, 0, result, (jbyte *) buffer);
    return result;
}

/**
 * int getPortNumber(Device)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, getParent)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return NULL);
    return wrapDevice(env, libusb_get_parent(unwrapDevice(env, device)));
}

/**
 * int getDeviceAddress(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getDeviceAddress)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    return libusb_get_device_address(unwrapDevice(env, device));
}

/**
 * int getDeviceSpeed(Device)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getDeviceSpeed)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return 0);
    return libusb_get_device_speed(unwrapDevice(env, device));
}

/**
 * int getMaxPacketSize(Device, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getMaxPacketSize)
(
    JNIEnv *env, jclass class, jobject device, jint endpoint
)
{
    NOT_NULL(env, device, return 0);
    return libusb_get_max_packet_size(unwrapDevice(env, device), endpoint);
}

/**
 * int getMaxIsoPacketSize(Device, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getMaxIsoPacketSize)
(
    JNIEnv *env, jclass class, jobject device, jint endpoint
)
{
    NOT_NULL(env, device, return 0);
    return libusb_get_max_iso_packet_size(unwrapDevice(env, device), endpoint);
}

/**
 * Device refDevice(Device)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, refDevice)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return NULL);
    libusb_ref_device(unwrapDevice(env, device));
    return device;
}

/**
 * void unrefDevice(Device)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, unrefDevice)
(
    JNIEnv *env, jclass class, jobject device
)
{
    NOT_NULL(env, device, return);
    libusb_unref_device(unwrapDevice(env, device));
}

/**
 * int open(Device, DeviceHandle)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, open)
(
    JNIEnv *env, jclass class, jobject device, jobject handle
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *deviceHandle;
    int result = libusb_open(unwrapDevice(env, device), &deviceHandle);
    if (!result) setDeviceHandle(env, deviceHandle, handle);
    return result;
}

/**
 * DeviceHandle open(Context, int, int)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, openDeviceWithVidPid)
(
    JNIEnv *env, jclass class, jobject context, jint vendorId,
    jint productId
)
{
    return wrapDeviceHandle(env, libusb_open_device_with_vid_pid(
        unwrapContext(env, context), vendorId, productId));
}

/**
 * void close(DeviceHandle)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, close)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    NOT_NULL(env, handle, return);
    libusb_close(unwrapDeviceHandle(env, handle));
}

/**
 * Device getDevice(DeviceHandle)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, getDevice)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    NOT_NULL(env, handle, return NULL);
    return wrapDevice(env, libusb_get_device(unwrapDeviceHandle(env, handle)));
}

/**
 * int getConfiguration(DeviceHandle, IntBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getConfiguration)
(
    JNIEnv *env, jclass class, jobject handle, jobject buffer
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, buffer, return 0);
    int config;
    int result = libusb_get_configuration(unwrapDeviceHandle(env, handle),
        &config);
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
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, setConfiguration)
(
    JNIEnv *env, jclass class, jobject handle, jint config
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_set_configuration(unwrapDeviceHandle(env, handle), config);
}

/**
 * int claimInterface(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, claimInterface)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_claim_interface(unwrapDeviceHandle(env, handle), iface);
}

/**
 * int releaseInterface(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, releaseInterface)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_release_interface(unwrapDeviceHandle(env, handle), iface);
}

/**
 * int setInterfaceAltSetting(DeviceHandle, int, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, setInterfaceAltSetting)
(
    JNIEnv *env, jclass class, jobject handle, jint iface, jint setting
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_set_interface_alt_setting(unwrapDeviceHandle(env, handle),
        iface, setting);
}

/**
 * int clearHalt(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, clearHalt)
(
    JNIEnv *env, jclass class, jobject handle, jint endpoint
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_clear_halt(unwrapDeviceHandle(env, handle), endpoint);
}

/**
 * int resetDevice(DeviceHandle)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, resetDevice)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_reset_device(unwrapDeviceHandle(env, handle));
}

/**
 * int kernelDriverActive(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, kernelDriverActive)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_kernel_driver_active(unwrapDeviceHandle(env, handle), iface);
}

/**
 * int detachKernelDriver(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, detachKernelDriver)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_detach_kernel_driver(unwrapDeviceHandle(env, handle), iface);
}

/**
 * int attachKernelDriver(DeviceHandle, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, attachKernelDriver)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    NOT_NULL(env, handle, return 0);
    return libusb_attach_kernel_driver(unwrapDeviceHandle(env, handle), iface);
}

/**
 * boolean hasCapability(int)
 */
JNIEXPORT jboolean JNICALL METHOD_NAME(LibUSB, hasCapability)
(
    JNIEnv *env, jclass class, jint capability
)
{
    return libusb_has_capability(capability);
}

/**
 * string errorName(int)
 */
JNIEXPORT jstring JNICALL METHOD_NAME(LibUSB, errorName)
(
    JNIEnv *env, jobject this, jint code
)
{
    return (*env)->NewStringUTF(env, libusb_error_name(code));
}

/**
 * int le16ToCpu(int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, le16ToCpu)
(
    JNIEnv *env, jobject this, jint x
)
{
    return libusb_le16_to_cpu(x);
}

/**
 * int cpuToLe16(int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, cpuToLe16)
(
    JNIEnv *env, jobject this, jint x
)
{
    return libusb_cpu_to_le16(x);
}

/**
 * int getDeviceDescriptor(Device, DeviceDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getDeviceDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jobject descriptor
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    struct libusb_device_descriptor *data = malloc(sizeof(struct libusb_device_descriptor));
    int result = libusb_get_device_descriptor(unwrapDevice(env, device), data);
    if (!result) setDeviceDescriptor(env, data, descriptor);
    return result;
}

/**
 * int getStringDescriptorAscii(DeviceHandle, int, StringBuffer, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getStringDescriptorAscii)
(
    JNIEnv *env, jclass class, jobject handle, jint index, jobject string,
    jint length
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, string, return 0);
    unsigned char buffer[length + 1];
    int result = libusb_get_string_descriptor_ascii(
        unwrapDeviceHandle(env, handle), index, buffer, length);
    if (result >= 0)
    {
        buffer[result] = 0;
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
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getActiveConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jobject descriptor)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    struct libusb_config_descriptor *config;
    int result = libusb_get_active_config_descriptor(
        unwrapDevice(env, device), &config);
    if (!result) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * int getConfigDescriptor(Device, int, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jint index, jobject descriptor)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    struct libusb_config_descriptor *config;
    int result = libusb_get_config_descriptor(
        unwrapDevice(env, device), index, &config);
    if (!result) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * int getConfigDescriptorByValue(Device, int, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, getConfigDescriptorByValue)
(
    JNIEnv *env, jclass class, jobject device, jint index, jobject descriptor)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    struct libusb_config_descriptor *config;
    int result = libusb_get_config_descriptor_by_value(
        unwrapDevice(env, device), index, &config);
    if (!result) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * void freeConfigDescriptor(ConfigDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, freeConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject descriptor)
{
    NOT_NULL(env, descriptor, return);
    libusb_free_config_descriptor(unwrapConfigDescriptor(env, descriptor));
}
