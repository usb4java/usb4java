/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "BosDescriptor.h"
#include "BosDevCapabilityDescriptor.h"

void setBosDescriptor(JNIEnv* env,
    struct libusb_bos_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "bosDescriptorPointer");
}

struct libusb_bos_descriptor* unwrapBosDescriptor(JNIEnv* env,
    jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor, struct libusb_bos_descriptor*,
        "bosDescriptorPointer");
}

void resetBosDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "bosDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BosDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBosDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BosDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBosDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * short wTotalLength()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(BosDescriptor, wTotalLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBosDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->wTotalLength;
}

/**
 * byte bNumDeviceCaps()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BosDescriptor, bNumDeviceCaps)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor =
        unwrapBosDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bNumDeviceCaps;
}

/**
 * BosDevCapabilityDescriptor[] devCapability()
 */
JNIEXPORT jobjectArray JNICALL METHOD_NAME(BosDescriptor, devCapability)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_descriptor *descriptor = unwrapBosDescriptor(
        env, this);
    if (!descriptor) return NULL;
    return wrapBosDevCapabilityDescriptors(env, descriptor->bNumDeviceCaps,
        descriptor->dev_capability);
}
