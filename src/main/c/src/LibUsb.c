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
#include "EndpointDescriptor.h"
#include "SsEndpointCompanionDescriptor.h"
#include "BosDescriptor.h"
#include "BosDevCapabilityDescriptor.h"
#include "Usb20ExtensionDescriptor.h"
#include "SsUsbDeviceCapabilityDescriptor.h"
#include "ContainerIdDescriptor.h"
#include "Transfer.h"

static int defaultContextRefcnt = 0;

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
 * int getApiVersion()
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getApiVersion)
(
    JNIEnv *env, jclass class
)
{
    return LIBUSBX_API_VERSION;
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
        int result = libusb_init(NULL);
        if (result == LIBUSB_SUCCESS) defaultContextRefcnt++;
        return result;
    }
    else
    {
        NOT_SET(env, context, "contextPointer", return 0);

        libusb_context *ctx;
        int result = libusb_init(&ctx);
        if (result == LIBUSB_SUCCESS) setContext(env, ctx, context);
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
        if (defaultContextRefcnt <= 0)
        {
            illegalState(env, "default context is not initialized");
            return;
        }

        libusb_exit(NULL);

        defaultContextRefcnt--;
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
    if (result >= 0) setDeviceList(env, list, (jint) result, deviceList);
    return (jint) result;
}

/**
 * void freeDeviceList(DeviceList, boolean)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeDeviceList)
(
    JNIEnv *env, jclass class, jobject deviceList, jboolean unrefDevices
)
{
    if (!deviceList) return;
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
    return libusb_get_port_number(dev);
}

/**
 * int getPortNumbers(Device, ByteBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getPortNumbers)
(
    JNIEnv *env, jclass class, jobject device, jobject path
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, path, return 0);
    DIRECT_BUFFER(env, path, path_ptr, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    jlong path_size = (*env)->GetDirectBufferCapacity(env, path);
    return libusb_get_port_numbers(dev, path_ptr, (int) path_size);
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
    return wrapDevice(env, libusb_get_parent(dev));
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
 * int getMaxPacketSize(Device, byte)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getMaxPacketSize)
(
    JNIEnv *env, jclass class, jobject device, jbyte endpoint
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    return libusb_get_max_packet_size(dev, (unsigned char) endpoint);
}

/**
 * int getMaxIsoPacketSize(Device, byte)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getMaxIsoPacketSize)
(
    JNIEnv *env, jclass class, jobject device, jbyte endpoint
)
{
    NOT_NULL(env, device, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    return libusb_get_max_iso_packet_size(dev, (unsigned char) endpoint);
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
    return wrapDevice(env, libusb_ref_device(dev));
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
    resetDevice(env, device);
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
    if (result == LIBUSB_SUCCESS) setDeviceHandle(env, deviceHandle, handle);
    return result;
}

/**
 * DeviceHandle openDeviceWithVidPid(Context, short, short)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, openDeviceWithVidPid)
(
    JNIEnv *env, jclass class, jobject context, jshort vendorId,
    jshort productId
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return NULL;
    return wrapDeviceHandle(env, libusb_open_device_with_vid_pid(
        ctx, (uint16_t) vendorId, (uint16_t) productId));
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
    if (result == LIBUSB_SUCCESS)
    {
        jclass cls = (*env)->GetObjectClass(env, buffer);
        jmethodID method = (*env)->GetMethodID(env, cls, "put", "(II)Ljava/nio/IntBuffer;");
        (*env)->CallObjectMethod(env, buffer, method, 0, config);
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
 * int clearHalt(DeviceHandle, byte)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, clearHalt)
(
    JNIEnv *env, jclass class, jobject handle, jbyte endpoint
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    return libusb_clear_halt(dev_handle, (unsigned char) endpoint);
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
 * int setAutoDetachKernelDriver(DeviceHandle, boolean)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, setAutoDetachKernelDriver)
(
    JNIEnv *env, jclass class, jobject handle, jboolean enable
)
{
    NOT_NULL(env, handle, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    return libusb_set_auto_detach_kernel_driver(dev_handle, enable);
}

/**
 * boolean hasCapability(int)
 */
JNIEXPORT jboolean JNICALL METHOD_NAME(LibUsb, hasCapability)
(
    JNIEnv *env, jclass class, jint capability
)
{
    return (jboolean) libusb_has_capability((uint32_t) capability);
}

/**
 * string errorName(int)
 */
JNIEXPORT jstring JNICALL METHOD_NAME(LibUsb, errorName)
(
    JNIEnv *env, jclass class, jint code
)
{
    return (*env)->NewStringUTF(env, libusb_error_name(code));
}

/**
 * int setLocale(String)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, setLocale)
(
    JNIEnv *env, jobject class, jstring locale
)
{
    NOT_NULL(env, locale, return 0);
    const char *nativeLocale = (*env)->GetStringUTFChars(env, locale, NULL);
    int result = libusb_setlocale(nativeLocale);
    (*env)->ReleaseStringUTFChars(env, locale, nativeLocale);
    return result;
}

/**
 * String strError(int)
 */
JNIEXPORT jstring JNICALL METHOD_NAME(LibUsb, strError)
(
    JNIEnv *env, jobject class, jint code
)
{
    return (*env)->NewStringUTF(env, libusb_strerror(code));
}

/**
 * short le16ToCpu(short)
 */
JNIEXPORT jshort JNICALL METHOD_NAME(LibUsb, le16ToCpu)
(
    JNIEnv *env, jclass class, jshort x
)
{
    return (jshort) libusb_le16_to_cpu((uint16_t) x);
}

/**
 * short cpuToLe16(short)
 */
JNIEXPORT jshort JNICALL METHOD_NAME(LibUsb, cpuToLe16)
(
    JNIEnv *env, jclass class, jshort x
)
{
    return (jshort) libusb_cpu_to_le16((uint16_t) x);
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

    struct libusb_device_descriptor *dev_desc = calloc(1, sizeof(*dev_desc));
    if (!dev_desc) return LIBUSB_ERROR_NO_MEM;

    int result = libusb_get_device_descriptor(dev, dev_desc);
    if (result == LIBUSB_SUCCESS)
    {
    	setDeviceDescriptor(env, dev_desc, descriptor);
    }
    else
    {
        // Free memory again on error.
    	free(dev_desc);
    }
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
    if (!descriptor) return;
    struct libusb_device_descriptor *dev_desc = unwrapDeviceDescriptor(env,
        descriptor);
    if (!dev_desc) return;

    free(dev_desc);
    resetDeviceDescriptor(env, descriptor);
}

/**
 * int getStringDescriptorAscii(DeviceHandle, byte, StringBuffer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getStringDescriptorAscii)
(
    JNIEnv *env, jclass class, jobject handle, jbyte index, jobject string
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
        dev_handle, (uint8_t) index, buffer, 127);
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
    if (result == LIBUSB_SUCCESS) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * int getConfigDescriptor(Device, byte, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jbyte index, jobject descriptor
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    NOT_SET(env, descriptor, "configDescriptorPointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    struct libusb_config_descriptor *config;
    int result = libusb_get_config_descriptor(dev, (uint8_t) index, &config);
    if (result == LIBUSB_SUCCESS) setConfigDescriptor(env, config, descriptor);
    return result;
}

/**
 * int getConfigDescriptorByValue(Device, byte, ConfigDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getConfigDescriptorByValue)
(
    JNIEnv *env, jclass class, jobject device, jbyte index, jobject descriptor
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
    NOT_SET(env, descriptor, "configDescriptorPointer", return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    struct libusb_config_descriptor *config;
    int result = libusb_get_config_descriptor_by_value(
        dev, (uint8_t) index, &config);
    if (result == LIBUSB_SUCCESS) setConfigDescriptor(env, config, descriptor);
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
    if (!descriptor) return;
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env,
        descriptor);
    if (!config) return;
    libusb_free_config_descriptor(config);
    resetConfigDescriptor(env, descriptor);
}

/**
 * int getSsEndpointCompanionDescriptor(Context, EndpointDescriptor, SsEndpointCompanionDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getSsEndpointCompanionDescriptor)
(
    JNIEnv *env, jclass class, jobject context, jobject endpointDescriptor,
    jobject companionDescriptor
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    NOT_NULL(env, endpointDescriptor, return 0);
    NOT_NULL(env, companionDescriptor, return 0);
    NOT_SET(env, companionDescriptor, "ssEndpointCompanionDescriptorPointer", return 0);

    struct libusb_endpoint_descriptor *endpoint_descriptor =
        unwrapEndpointDescriptor(env, endpointDescriptor);
    if (!endpoint_descriptor) return 0;
    struct libusb_ss_endpoint_companion_descriptor *companion_descriptor;
    int result = libusb_get_ss_endpoint_companion_descriptor(ctx,
        endpoint_descriptor, &companion_descriptor);
    if (result == LIBUSB_SUCCESS) setSsEndpointCompanionDescriptor(env, companion_descriptor,
        companionDescriptor);
    return result;
}

/**
 * void freeSsEndpointCompanionDescriptor(SsEndpointCompanionDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeSsEndpointCompanionDescriptor)
(
    JNIEnv *env, jclass class, jobject companionDescriptor
)
{
    if (!companionDescriptor) return;
    struct libusb_ss_endpoint_companion_descriptor *companion_descriptor =
        unwrapSsEndpointCompanionDescriptor(env, companionDescriptor);
    if (!companion_descriptor) return;
    libusb_free_ss_endpoint_companion_descriptor(companion_descriptor);
    resetSsEndpointCompanionDescriptor(env, companionDescriptor);
}

/**
 * int getBosDescriptor(DeviceHandle, BosDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getBosDescriptor)
(
    JNIEnv *env, jclass class, jobject handle, jobject bosDescriptor
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, bosDescriptor, return 0);
    NOT_SET(env, bosDescriptor, "bosDescriptorPointer", return 0);

    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    struct libusb_bos_descriptor *bos_descriptor;
    int result = libusb_get_bos_descriptor(dev_handle, &bos_descriptor);
    if (result == LIBUSB_SUCCESS) setBosDescriptor(env, bos_descriptor, bosDescriptor);
    return result;
}

/**
 * void freeBosDescriptor(BosDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeBosDescriptor)
(
    JNIEnv *env, jclass class, jobject bosDescriptor
)
{
    if (!bosDescriptor) return;
    struct libusb_bos_descriptor *bos_descriptor =
        unwrapBosDescriptor(env, bosDescriptor);
    if (!bos_descriptor) return;
    libusb_free_bos_descriptor(bos_descriptor);
    resetBosDescriptor(env, bosDescriptor);
}

/**
 * int getUsb20ExtensionDescriptor(Context, BosDevCapabilityDescriptor, Usb20ExtensionDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getUsb20ExtensionDescriptor)
(
    JNIEnv *env, jclass class, jobject context, jobject devCapDescriptor,
    jobject extensionDescriptor
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    NOT_NULL(env, devCapDescriptor, return 0);
    NOT_NULL(env, extensionDescriptor, return 0);
    NOT_SET(env, extensionDescriptor, "usb20ExtensionDescriptorPointer", return 0);

    struct libusb_bos_dev_capability_descriptor *devcap_descriptor =
        unwrapBosDevCapabilityDescriptor(env, devCapDescriptor);
    if (!devcap_descriptor) return 0;
    struct libusb_usb_2_0_extension_descriptor *extension_descriptor;
    int result = libusb_get_usb_2_0_extension_descriptor(ctx,
        devcap_descriptor, &extension_descriptor);
    if (result == LIBUSB_SUCCESS) setUsb20ExtensionDescriptor(env, extension_descriptor,
        extensionDescriptor);
    return result;
}

/**
 * void freeUsb20ExtensionDescriptor(Usb20ExtensionDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeUsb20ExtensionDescriptor)
(
    JNIEnv *env, jclass class, jobject extensionDescriptor
)
{
    if (!extensionDescriptor) return;
    struct libusb_usb_2_0_extension_descriptor *extension_descriptor =
        unwrapUsb20ExtensionDescriptor(env, extensionDescriptor);
    if (!extension_descriptor) return;
    libusb_free_usb_2_0_extension_descriptor(extension_descriptor);
    resetUsb20ExtensionDescriptor(env, extensionDescriptor);
}

/**
 * int getSsUsbDeviceCapabilityDescriptor(Context, BosDevCapabilityDescriptor, SsUsbDeviceCapabilityDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getSsUsbDeviceCapabilityDescriptor)
(
    JNIEnv *env, jclass class, jobject context, jobject devCapDescriptor,
    jobject ssUsbDeviceCapabilityDescriptor
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    NOT_NULL(env, devCapDescriptor, return 0);
    NOT_NULL(env, ssUsbDeviceCapabilityDescriptor, return 0);
    NOT_SET(env, ssUsbDeviceCapabilityDescriptor, "ssUsbDeviceCapabilityDescriptorPointer", return 0);

    struct libusb_bos_dev_capability_descriptor *devcap_descriptor =
        unwrapBosDevCapabilityDescriptor(env, devCapDescriptor);
    if (!devcap_descriptor) return 0;
    struct libusb_ss_usb_device_capability_descriptor *descriptor;
    int result = libusb_get_ss_usb_device_capability_descriptor(ctx,
        devcap_descriptor, &descriptor);
    if (result == LIBUSB_SUCCESS) setSsUsbDeviceCapabilityDescriptor(env, descriptor,
        ssUsbDeviceCapabilityDescriptor);
    return result;
}

/**
 * void freeSsUsbDeviceCapabilityDescriptor(SsUsbDeviceCapabilityDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeSsUsbDeviceCapabilityDescriptor)
(
    JNIEnv *env, jclass class, jobject ssUsbDeviceCapabilityDescriptor
)
{
    if (!ssUsbDeviceCapabilityDescriptor) return;
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, ssUsbDeviceCapabilityDescriptor);
    if (!descriptor) return;
    libusb_free_ss_usb_device_capability_descriptor(descriptor);
    resetSsUsbDeviceCapabilityDescriptor(env, ssUsbDeviceCapabilityDescriptor);
}

/**
 * int getContainerIdDescriptor(Context, BosDevCapabilityDescriptor, ContainerIdDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getContainerIdDescriptor)
(
    JNIEnv *env, jclass class, jobject context, jobject devCapDescriptor,
    jobject containerIdDescriptor
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    NOT_NULL(env, devCapDescriptor, return 0);
    NOT_NULL(env, containerIdDescriptor, return 0);
    NOT_SET(env, containerIdDescriptor, "containerIdDescriptorPointer", return 0);

    struct libusb_bos_dev_capability_descriptor *devcap_descriptor =
        unwrapBosDevCapabilityDescriptor(env, devCapDescriptor);
    if (!devcap_descriptor) return 0;
    struct libusb_container_id_descriptor *container_id_descriptor;
    int result = libusb_get_container_id_descriptor(ctx,
        devcap_descriptor, &container_id_descriptor);
    if (result == LIBUSB_SUCCESS) setContainerIdDescriptor(env, container_id_descriptor,
        containerIdDescriptor);
    return result;
}

/**
 * void freeContainerIdDescriptor(ContainerIdDescriptor)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeContainerIdDescriptor)
(
    JNIEnv *env, jclass class, jobject containerIdDescriptor
)
{
    if (!containerIdDescriptor) return;
    struct libusb_container_id_descriptor *container_id_descriptor =
        unwrapContainerIdDescriptor(env, containerIdDescriptor);
    if (!container_id_descriptor) return;
    libusb_free_container_id_descriptor(container_id_descriptor);
    resetContainerIdDescriptor(env, containerIdDescriptor);
}

/**
 * int controlTransfer(DeviceHandle, byte, byte, short, short, ByteBuffer, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, controlTransfer)
(
    JNIEnv *env, jclass class, jobject handle, jbyte bmRequestType,
    jbyte bRequest, jshort wValue, jshort wIndex, jobject data, jlong timeout
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    DIRECT_BUFFER(env, data, data_ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    jlong data_size = (*env)->GetDirectBufferCapacity(env, data);
    return libusb_control_transfer(dev_handle, (uint8_t) bmRequestType,
        (uint8_t) bRequest, (uint16_t) wValue, (uint16_t) wIndex, data_ptr,
        (uint16_t) data_size, (unsigned int) timeout);
}

/**
 * int bulkTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, bulkTransfer)
(
    JNIEnv *env, jclass class, jobject handle, jbyte endpoint,
    jobject data, jobject transferred, jlong timeout
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    NOT_NULL(env, transferred, return 0);
    DIRECT_BUFFER(env, data, data_ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    int sent;
    jlong data_size = (*env)->GetDirectBufferCapacity(env, data);
    int result = libusb_bulk_transfer(dev_handle, (unsigned char) endpoint,
        data_ptr, (int) data_size, &sent, (unsigned int) timeout);
    if (result == LIBUSB_SUCCESS)
    {
        jclass cls = (*env)->GetObjectClass(env, transferred);
        jmethodID method = (*env)->GetMethodID(env, cls, "put",
            "(II)Ljava/nio/IntBuffer;");
        (*env)->CallObjectMethod(env, transferred, method, 0, sent);
    }
    return result;
}

/**
 * int interruptTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, interruptTransfer)
(
    JNIEnv *env, jclass class, jobject handle, jbyte endpoint,
    jobject data, jobject transferred, jlong timeout
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, data, return 0);
    NOT_NULL(env, transferred, return 0);
    DIRECT_BUFFER(env, data, data_ptr, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    int sent;
    jlong data_size = (*env)->GetDirectBufferCapacity(env, data);
    int result = libusb_interrupt_transfer(dev_handle, (unsigned char) endpoint,
        data_ptr, (int) data_size, &sent, (unsigned int) timeout);
    if (result == LIBUSB_SUCCESS)
    {
        jclass cls = (*env)->GetObjectClass(env, transferred);
        jmethodID method = (*env)->GetMethodID(env, cls, "put",
            "(II)Ljava/nio/IntBuffer;");
        (*env)->CallObjectMethod(env, transferred, method, 0, sent);
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
    tv.tv_sec = (long int) timeout / 1000000;
    tv.tv_usec = (long int) timeout % 1000000;

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
    if (completed)
    {
        DIRECT_BUFFER(env, completed, complete_tmp, return 0);
        complete = (int *) complete_tmp;
    }

    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    struct timeval tv;
    tv.tv_sec = (long int) timeout / 1000000;
    tv.tv_usec = (long int) timeout % 1000000;

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
    tv.tv_sec = (long int) timeout / 1000000;
    tv.tv_usec = (long int) timeout % 1000000;

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
    if (completed)
    {
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
    tv.tv_sec = (long int) timeout / 1000000;
    tv.tv_usec = (long int) timeout % 1000000;

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
        (*env)->CallObjectMethod(env, timeout, method, 0,
            (jlong) (tv.tv_sec * 1000000 + tv.tv_usec));
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
        "triggerPollfdAdded", "(Ljava/io/FileDescriptor;IJ)V");
    (*env)->CallStaticVoidMethod(env, cls, method, object, (jint) events,
        (jlong) (intptr_t) user_data);

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
        "triggerPollfdRemoved", "(Ljava/io/FileDescriptor;J)V");
    (*env)->CallStaticVoidMethod(env, cls, method, object,
        (jlong) (intptr_t) user_data);

    THREAD_END
}

/**
 * void setPollfdNotifiers(Context, long)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, setPollfdNotifiers)
(
    JNIEnv *env, jclass class, jobject context, jlong context_id
)
{
    libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    libusb_set_pollfd_notifiers(ctx, &triggerPollfdAdded, &triggerPollfdRemoved,
        (void *) (intptr_t) context_id);
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
    struct libusb_transfer *transfer = libusb_alloc_transfer(isoPackets);
    if (!transfer) return NULL;

    struct transfer_data *transferData = calloc(1, sizeof(*transferData));
    if (!transferData)
    {
        libusb_free_transfer(transfer);
        return NULL;
    }

    transfer->user_data = transferData;
    transferData->maxNumIsoPackets = (size_t) isoPackets;

    jobject transferObject = wrapTransfer(env, transfer);

    // Make sure the cleanup callback is always there, as it's perfectly legal
    // to not set any callback and still enable the FREE_TRANSFER flag, in which
    // case one would expect the Java Transfer object to be properly cleaned up.
    cleanupCallbackEnable(env, transferObject);

    return transferObject;
}

/**
 * void freeTransfer(Transfer)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, freeTransfer)
(
    JNIEnv *env, jclass class, jobject trans
)
{
    if (!trans) return;
    struct libusb_transfer *transfer = unwrapTransfer(env, trans);
    if (!transfer) return;

    cleanupGlobalReferences(env, trans);
    resetTransfer(env, trans);
    free(transfer->user_data);
    libusb_free_transfer(transfer);
}

/**
 * int submitTransfer(Transfer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, submitTransfer)
(
    JNIEnv *env, jclass class, jobject trans
)
{
    NOT_NULL(env, trans, return 0);
    struct libusb_transfer *transfer = unwrapTransfer(env, trans);
    if (!transfer) return 0;

    return libusb_submit_transfer(transfer);
}

/**
 * int cancelTransfer(Transfer)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, cancelTransfer)
(
    JNIEnv *env, jclass class, jobject trans
)
{
    NOT_NULL(env, trans, return 0);
    struct libusb_transfer *transfer = unwrapTransfer(env, trans);
    if (!transfer) return 0;

    return libusb_cancel_transfer(transfer);
}
