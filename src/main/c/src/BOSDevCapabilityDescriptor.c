/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "BOSDevCapabilityDescriptor.h"

jobject wrapBOSDevCapabilityDescriptor(JNIEnv *env,
    const struct libusb_bos_dev_capability_descriptor *descriptor)
{
    WRAP_POINTER(env, descriptor, "BOSDevCapabilityDescriptor",
        "bosDevCapabilityDescriptorPointer");
}

jobjectArray wrapBOSDevCapabilityDescriptors(JNIEnv *env, int count,
    struct libusb_bos_dev_capability_descriptor **descriptors)
{
    int i;

    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env,
        count, (*env)->FindClass(env, PACKAGE_DIR"/BOSDevCapabilityDescriptor"),
        NULL);
    for (i = 0; i < count; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrapBOSDevCapabilityDescriptor(env, descriptors[i]));
    return array;
}

struct libusb_bos_dev_capability_descriptor
    *unwrapBOSDevCapabilityDescriptor(JNIEnv *env, jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_bos_dev_capability_descriptor*,
        "bosDevCapabilityDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BOSDevCapabilityDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_dev_capability_descriptor* descriptor =
        unwrapBOSDevCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BOSDevCapabilityDescriptor,
    bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_dev_capability_descriptor* descriptor =
        unwrapBOSDevCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * byte bDevCapabilityType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(BOSDevCapabilityDescriptor,
    bDevCapabilityType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_dev_capability_descriptor* descriptor =
        unwrapBOSDevCapabilityDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDevCapabilityType;
}

/**
 * ByteBuffer devCapabilityData()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(BOSDevCapabilityDescriptor,
    devCapabilityData)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_bos_dev_capability_descriptor *descriptor =
        unwrapBOSDevCapabilityDescriptor(env, this);
    if (!descriptor) return NULL;
    return (*env)->NewDirectByteBuffer(env, (void *)
        descriptor->dev_capability_data, descriptor->bLength - 3);
}
