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
#include "Transfer.h"

static JavaVM *jvm;

static int defaultContextInitialized = 0;

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
 * int init()
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, init)
(
    JNIEnv *env, jclass class, jobject context
)
{
    if (!context)
    {
        if (defaultContextInitialized)
        {
            return illegalState(env, "Default context already initialized");
        }
        int result = libusb_init(NULL);
        if (!result) defaultContextInitialized = 1;
        return result;
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
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, exit)
(
    JNIEnv *env, jclass class, jobject context
)
{
    struct libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;

    if (!context && !defaultContextInitialized)
    {
        illegalState(env, "Default context not initialized");
        return;
    }
    libusb_exit(ctx);
    if (context)
        resetContext(env, context);
    else
        defaultContextInitialized = 0;
}

/**
 * void setDebug(Context, int)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUsb, setDebug)
(
    JNIEnv *env, jclass class, jobject context, jint level
)
{
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    return libusb_get_port_number(dev);
}

/**
 * int getPortNumbers(Device, byte[])
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getPortNumbers)
(
    JNIEnv *env, jclass class, jobject device, jbyteArray path
)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, path, return 0);
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    jsize size = (*env)->GetArrayLength(env, path);
    unsigned char buffer[size];
    int result = libusb_get_port_numbers(dev, buffer, size);
    if (result > 0) (*env)->SetByteArrayRegion(env, path, 0, result, (jbyte *) buffer);
    return result;
}

/**
 * int getPortNumber(Device)
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
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    libusb_device_handle *deviceHandle;
    int result = libusb_open(dev, &deviceHandle);
    if (!result) setDeviceHandle(env, deviceHandle, handle);
    return result;
}

/**
 * DeviceHandle open(Context, int, int)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUsb, openDeviceWithVidPid)
(
    JNIEnv *env, jclass class, jobject context, jint vendorId,
    jint productId
)
{
    struct libusb_context *ctx = unwrapContext(env, context);
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
 * int attachKernelDriver(DeviceHandle, int)
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
 * int setLocale(string)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, setLocale)
(
    JNIEnv *env, jobject this, jstring locale
)
{
    const char *nativeLocale = (*env)->GetStringUTFChars(env, locale, 0);
    int result = libusb_setlocale(nativeLocale);
    (*env)->ReleaseStringUTFChars(env, locale, nativeLocale);
    return result;
}

/**
 * string strError(int)
 */
JNIEXPORT jstring JNICALL METHOD_NAME(LibUsb, strError)
(
    JNIEnv *env, jobject this, jint code
)
{
    return (*env)->NewStringUTF(env, libusb_strerror(code));
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
    libusb_device *dev = unwrapDevice(env, device);
    if (!dev) return 0;
    struct libusb_device_descriptor *data = 
        malloc(sizeof(struct libusb_device_descriptor));
    int result = libusb_get_device_descriptor(dev, data);
    if (!result) setDeviceDescriptor(env, data, descriptor);
    return result;
}

/**
 * int getStringDescriptorAscii(DeviceHandle, int, StringBuffer, int)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getStringDescriptorAscii)
(
    JNIEnv *env, jclass class, jobject handle, jint index, jobject string,
    jint length
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, string, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    unsigned char buffer[length + 1];
    int result = libusb_get_string_descriptor_ascii(
        dev_handle, index, buffer, length);
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
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getActiveConfigDescriptor)
(
    JNIEnv *env, jclass class, jobject device, jobject descriptor)
{
    NOT_NULL(env, device, return 0);
    NOT_NULL(env, descriptor, return 0);
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
 * int getSsEndpointCompanionDescriptor(Device, int, SsEndpointCompanionDescriptor)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, getSsEndpointCompanionDescriptor)
(
    JNIEnv *env, jclass class, jobject context, jobject endpointDescriptor,
    jobject companionDescriptor
)
{
    NOT_NULL(env, endpointDescriptor, return 0);
    NOT_NULL(env, companionDescriptor, return 0);
    libusb_context *ctx = unwrapContext(env, context);
    struct libusb_endpoint_descriptor *endpoint_descriptor =
        unwrapEndpointDescriptor(env, endpointDescriptor);
    if (!endpoint_descriptor) return 0;
    struct libusb_ss_endpoint_companion_descriptor *companion_descriptor;
    int result = libusb_get_ss_endpoint_companion_descriptor(ctx,
        endpoint_descriptor, &companion_descriptor);
    if (!result) setSsEndpointCompanionDescriptor(env, companion_descriptor,
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
    JNIEnv *env, jclass class, jobject handle, jobject descriptor
)
{
    NOT_NULL(env, handle, return 0);
    NOT_NULL(env, descriptor, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    struct libusb_bos_descriptor *bos_descriptor;
    int result = libusb_get_bos_descriptor(dev_handle, &bos_descriptor);
    if (!result) setBosDescriptor(env, bos_descriptor, descriptor);
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
    NOT_NULL(env, devCapDescriptor, return 0);
    NOT_NULL(env, extensionDescriptor, return 0);
    libusb_context *ctx = unwrapContext(env, context);
    struct libusb_bos_dev_capability_descriptor *devcap_descriptor =
        unwrapBosDevCapabilityDescriptor(env, devCapDescriptor);
    if (!devcap_descriptor) return 0;
    struct libusb_usb_2_0_extension_descriptor *extension_descriptor;
    int result = libusb_get_usb_2_0_extension_descriptor(ctx,
        devcap_descriptor, &extension_descriptor);
    if (!result) setUsb20ExtensionDescriptor(env, extension_descriptor,
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
    DIRECT_BUFFER(env, data, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    unsigned char *ptr = (*env)->GetDirectBufferAddress(env, data);
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
    DIRECT_BUFFER(env, data, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    unsigned char *ptr = (*env)->GetDirectBufferAddress(env, data);
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
    DIRECT_BUFFER(env, data, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    unsigned char *ptr = (*env)->GetDirectBufferAddress(env, data);
    jlong size = (*env)->GetDirectBufferCapacity(env, data);
    return libusb_control_transfer(dev_handle, bmRequestType, bRequest, 
        wValue, wIndex, ptr, size, timeout);
}

/**
 * int bulkTransfer(DeviceHandle, int, int, int, int, ByteBuffer, int)
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
    DIRECT_BUFFER(env, data, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    int sent;
    unsigned char *ptr = (*env)->GetDirectBufferAddress(env, data);
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
 * int interruptTransfer(DeviceHandle, int, int, int, int, ByteBuffer, int)
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
    DIRECT_BUFFER(env, data, return 0);
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle) return 0;
    int sent;
    unsigned char *ptr = (*env)->GetDirectBufferAddress(env, data);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    struct timeval tv;
    tv.tv_sec = timeout / 1000000;
    tv.tv_usec = timeout % 1000000;
    int complete;
    int result = libusb_handle_events_timeout_completed(ctx, &tv, &complete);
    if (!result && completed)
    {
        jclass cls = (*env)->GetObjectClass(env, completed);
        jmethodID method = (*env)->GetMethodID(env, cls, "put", "(II)Ljava/nio/IntBuffer;");
        (*env)->CallVoidMethod(env, completed, method, 0, complete);
    }
    return result;
}

/**
 * int handleEventsTimeout(Context, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEventsTimeout)
(
    JNIEnv *env, jclass class, jobject context, jlong timeout
)
{
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    int complete;
    int result = libusb_handle_events_completed(ctx, &complete);
    if (!result && completed)
    {
        jclass cls = (*env)->GetObjectClass(env, completed);
        jmethodID method = (*env)->GetMethodID(env, cls, "put", "(II)Ljava/nio/IntBuffer;");
        (*env)->CallVoidMethod(env, completed, method, 0, complete);
    }
    return result;
}

/**
 * int handleEventsLocked(Context, long)
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUsb, handleEventsLocked)
(
    JNIEnv *env, jclass class, jobject context, jlong timeout
)
{
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return 0;
    struct timeval tv;
    int result = libusb_get_next_timeout(ctx, &tv);
    if (result == 1)
    {
        jclass cls = (*env)->GetObjectClass(env, timeout);
        jmethodID method = (*env)->GetMethodID(env, cls, "put",
            "(II)Ljava/nio/LongBuffer;");
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
        "triggerPollfdAdded", "(Ljava/io/FileDescriptor;)V");
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
    struct libusb_context *ctx = unwrapContext(env, context);
    if (!ctx && context) return;
    (*env)->GetJavaVM(env, &jvm);
    libusb_set_pollfd_notifiers(ctx, triggerPollfdAdded, triggerPollfdRemoved, 
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
    struct libusb_context *ctx = unwrapContext(env, context);
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
    struct libusb_transfer *handle = unwrapTransfer(env, transfer);
    if (!handle) return;
    libusb_free_transfer(handle);
    resetTransfer(env, transfer);
}
