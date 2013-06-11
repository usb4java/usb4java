/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "ConfigDescriptor.h"
#include "Interface.h"

void setConfigDescriptor(JNIEnv* env,
    const struct libusb_config_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "configDescriptorPointer");
}

struct libusb_config_descriptor* unwrapConfigDescriptor(JNIEnv* env,
    jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor, struct libusb_config_descriptor*,
        "configDescriptorPointer");
}

void resetConfigDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "configDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->bDescriptorType;
}

/**
 * short wTotalLength()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(ConfigDescriptor, wTotalLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->wTotalLength;
}

/**
 * byte bNumInterfaces()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, bNumInterfaces)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->bNumInterfaces;
}

/**
 * byte bConfigurationValue()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, bConfigurationValue)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->bConfigurationValue;
}

/**
 * byte iConfiguration()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, iConfiguration)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->iConfiguration;
}

/**
 * byte bmAttributes()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, bmAttributes)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->bmAttributes;
}

/**
 * byte bMaxPower()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ConfigDescriptor, bMaxPower)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->MaxPower;
}

/**
 * Interface[] iface()
 */
JNIEXPORT jobjectArray JNICALL METHOD_NAME(ConfigDescriptor, iface)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return NULL;

    return wrapInterfaces(env, config->bNumInterfaces,
        config->interface);
}

/**
 * ByteBuffer extra()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(ConfigDescriptor, extra)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return NULL;

    return (*env)->NewDirectByteBuffer(env, (void *) config->extra,
        config->extra_length);
}

/**
 * int extraLength()
 */
JNIEXPORT jint JNICALL METHOD_NAME(ConfigDescriptor, extraLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_config_descriptor *config = unwrapConfigDescriptor(env, this);
    if (!config) return 0;

    return config->extra_length;
}
