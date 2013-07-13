/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "SsEndpointCompanionDescriptor.h"
#include "Interface.h"

void setSsEndpointCompanionDescriptor(JNIEnv* env,
    struct libusb_ss_endpoint_companion_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "ssEndpointCompanionDescriptor");
}

struct libusb_ss_endpoint_companion_descriptor*
    unwrapSsEndpointCompanionDescriptor(JNIEnv* env, jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor,
        struct libusb_ss_endpoint_companion_descriptor*,
        "ssEndpointCompanionDescriptor");
}

void resetSsEndpointCompanionDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "ssEndpointCompanionDescriptor");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsEndpointCompanionDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSsEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsEndpointCompanionDescriptor,
    bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSsEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * byte bMaxBurst()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsEndpointCompanionDescriptor, bMaxBurst)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSsEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bMaxBurst;
}

/**
 * byte bmAttributes()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SsEndpointCompanionDescriptor, bmAttributes)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSsEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bmAttributes;
}

/**
 * short wBytesPerInterval()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(SsEndpointCompanionDescriptor, wBytesPerInterval)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSsEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->wBytesPerInterval;
}
