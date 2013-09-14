/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "ContainerIdDescriptor.h"

void setContainerIdDescriptor(JNIEnv* env,
    const struct libusb_container_id_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "containerIdDescriptorPointer");
}

struct libusb_container_id_descriptor* unwrapContainerIdDescriptor(
    JNIEnv* env, jobject descriptor)
{
    UNWRAP_POINTER(env, descriptor,
         struct libusb_container_id_descriptor*,
        "containerIdDescriptorPointer");
}

void resetContainerIdDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "containerIdDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ContainerIdDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_container_id_descriptor *descriptor =
        unwrapContainerIdDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ContainerIdDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_container_id_descriptor *descriptor =
        unwrapContainerIdDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bDescriptorType;
}

/**
 * byte bDevCapabilityType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ContainerIdDescriptor, bDevCapabilityType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_container_id_descriptor *descriptor =
        unwrapContainerIdDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bDevCapabilityType;
}

/**
 * byte bReserved()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(ContainerIdDescriptor, bReserved)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_container_id_descriptor *descriptor =
        unwrapContainerIdDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bReserved;
}

/**
 * ByteBuffer containerId()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(ContainerIdDescriptor, containerId)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_container_id_descriptor *descriptor =
        unwrapContainerIdDescriptor(env, this);
    if (!descriptor) return NULL;
    return NewDirectReadOnlyByteBuffer(env, descriptor->ContainerID, 16);
}
