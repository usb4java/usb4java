/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "BOSDescriptor.h"
#include "BOSDevCapabilityDescriptor.h"

void setBOSDescriptor(JNIEnv* env,
    struct libusb_bos_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "bosDescriptorPointer");
}

struct libusb_bos_descriptor* unwrapBOSDescriptor(JNIEnv* env,
    jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor, struct libusb_bos_descriptor*,
        "bosDescriptorPointer");
}

void resetBOSDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "bosDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BOSDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBOSDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BOSDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBOSDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * short wTotalLength()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(BOSDescriptor, wTotalLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBOSDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->wTotalLength;
}

/**
 * byte bNumDeviceCaps()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BOSDescriptor, bNumDeviceCaps)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBOSDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bNumDeviceCaps;
}

/**
 * BOSDevCapabilityDescriptor[] devCapability()
 */
JNIEXPORT jobjectArray JNICALL METHOD_NAME(BOSDescriptor, devCapability)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor = unwrapBOSDescriptor(
        env, this);
    if (!descriptor) return NULL;
    return wrapBOSDevCapabilityDescriptors(env, descriptor->bNumDeviceCaps,
        descriptor->dev_capability);
}
