/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Usb20ExtensionDescriptor.h"

void setUsb20ExtensionDescriptor(JNIEnv* env,
    struct libusb_usb_2_0_extension_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "usb20ExtensionDescriptorPointer");
}

struct libusb_usb_2_0_extension_descriptor* unwrapUsb20ExtensionDescriptor(
    JNIEnv* env, jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor,
         struct libusb_usb_2_0_extension_descriptor*,
        "usb20ExtensionDescriptorPointer");
}

void resetUsb20ExtensionDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "usb20ExtensionDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(Usb20ExtensionDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_usb_2_0_extension_descriptor *descriptor =
        unwrapUsb20ExtensionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(Usb20ExtensionDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_usb_2_0_extension_descriptor *descriptor =
        unwrapUsb20ExtensionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * byte bDevCapabilityType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(Usb20ExtensionDescriptor,
    bDevCapabilityType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_usb_2_0_extension_descriptor *descriptor =
        unwrapUsb20ExtensionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDevCapabilityType;
}

/**
 * int bDevCapabilityType()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Usb20ExtensionDescriptor,
    bmAttributes)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_usb_2_0_extension_descriptor *descriptor =
        unwrapUsb20ExtensionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bmAttributes;
}
