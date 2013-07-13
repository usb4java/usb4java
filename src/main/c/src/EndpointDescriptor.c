/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "EndpointDescriptor.h"

jobject wrapEndpointDescriptor(JNIEnv *env,
    const struct libusb_endpoint_descriptor *descriptor)
{
    WRAP_POINTER(env, descriptor, "EndpointDescriptor", "endpointDescriptorPointer");
}

jobjectArray wrapEndpointDescriptors(JNIEnv *env, int count,
    const struct libusb_endpoint_descriptor *descriptors)
{
    int i;

    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env,
        count, (*env)->FindClass(env, PACKAGE_DIR"/EndpointDescriptor"),
        NULL);
    for (i = 0; i < count; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrapEndpointDescriptor(env, &descriptors[i]));
    return array;
}

struct libusb_endpoint_descriptor *unwrapEndpointDescriptor(JNIEnv *env,
    jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_endpoint_descriptor*, "endpointDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(EndpointDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(EndpointDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * byte bEndpointAddress()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(EndpointDescriptor, bEndpointAddress)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bEndpointAddress;
}

/**
 * byte bmAttributes()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(EndpointDescriptor, bmAttributes)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bmAttributes;
}

/**
 * short wMaxPacketSize()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(EndpointDescriptor, wMaxPacketSize)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->wMaxPacketSize;
}

/**
 * byte bInterval()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(EndpointDescriptor, bInterval)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bInterval;
}

/**
 * byte bRefresh()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(EndpointDescriptor, bRefresh)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bRefresh;
}

/**
 * byte bSynchAddress()
 */
JNIEXPORT jint JNICALL METHOD_NAME(EndpointDescriptor, bSynchAddress)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bSynchAddress;
}

/**
 * ByteBuffer extra()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(EndpointDescriptor, extra)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor *descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return NULL;
    return (*env)->NewDirectByteBuffer(env, (void *) descriptor->extra,
        descriptor->extra_length);
}

/**
 * int extraLength()
 */
JNIEXPORT jint JNICALL METHOD_NAME(EndpointDescriptor, extraLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_endpoint_descriptor* descriptor =
        unwrapEndpointDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->extra_length;
}
