/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "SSEndpointCompanionDescriptor.h"
#include "Interface.h"

void setSSEndpointCompanionDescriptor(JNIEnv* env,
    struct libusb_ss_endpoint_companion_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "ssEndpointCompanionDescriptor");
}

struct libusb_ss_endpoint_companion_descriptor*
    unwrapSSEndpointCompanionDescriptor(JNIEnv* env, jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor,
        struct libusb_ss_endpoint_companion_descriptor*,
        "ssEndpointCompanionDescriptor");
}

void resetSSEndpointCompanionDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "ssEndpointCompanionDescriptor");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SSEndpointCompanionDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSSEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SSEndpointCompanionDescriptor,
    bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSSEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bDescriptorType;
}

/**
 * byte bMaxBurst()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SSEndpointCompanionDescriptor, bMaxBurst)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSSEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bMaxBurst;
}

/**
 * byte bmAttributes()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(SSEndpointCompanionDescriptor, bmAttributes)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSSEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->bmAttributes;
}

/**
 * short wBytesPerInterval()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(SSEndpointCompanionDescriptor, wBytesPerInterval)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_ss_endpoint_companion_descriptor *descriptor =
        unwrapSSEndpointCompanionDescriptor(env, this);
    if (!descriptor) return 0;
    return descriptor->wBytesPerInterval;
}
