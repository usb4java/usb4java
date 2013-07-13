/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "SsUsbDeviceCapabilityDescriptor.h"

void setSsUsbDeviceCapabilityDescriptor(JNIEnv* env,
    struct libusb_ss_usb_device_capability_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "ssUsbDeviceCapabilityDescriptorPointer");
}

struct libusb_ss_usb_device_capability_descriptor* unwrapSsUsbDeviceCapabilityDescriptor(
    JNIEnv* env, jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor,
         struct libusb_ss_usb_device_capability_descriptor*,
        "ssUsbDeviceCapabilityDescriptorPointer");
}

void resetSsUsbDeviceCapabilityDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "ssUsbDeviceCapabilityDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * byte bDevCapabilityType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor,
    bDevCapabilityType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDevCapabilityType;
}

/**
 * int bmAttributes()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor,
    bmAttributes)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bmAttributes;
}

/**
 * short wSpeedSupported()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor,
    wSpeedSupported)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->wSpeedSupported;
}

/**
 * byte bFunctionalitySupport()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor,
    bFunctionalitySupport)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bFunctionalitySupport;
}

/**
 * byte bU1DevExitLat()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor,
    bU1DevExitLat)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bU1DevExitLat;
}

/**
 * short bU2DevExitLat()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(SsUsbDeviceCapabilityDescriptor,
    bU2DevExitLat)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_usb_device_capability_descriptor *descriptor =
        unwrapSsUsbDeviceCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bU2DevExitLat;
}
