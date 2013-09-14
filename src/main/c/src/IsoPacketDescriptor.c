/*
 * Copyright (C) 2013 Luca Longinotti (l@longi.li)
 * See COPYING file for copying conditions
 */

#include "IsoPacketDescriptor.h"

jobject wrapIsoPacketDescriptor(JNIEnv *env,
    const struct libusb_iso_packet_descriptor *descriptor)
{
    WRAP_POINTER(env, descriptor, "IsoPacketDescriptor",
        "isoPacketDescriptorPointer");
}

jobjectArray wrapIsoPacketDescriptors(JNIEnv *env, int count,
    const struct libusb_iso_packet_descriptor *descriptors)
{
    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env, count,
        (*env)->FindClass(env, PACKAGE_DIR"/IsoPacketDescriptor"), NULL);

    for (int i = 0; i < count; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrapIsoPacketDescriptor(env, &descriptors[i]));

    return array;
}

struct libusb_iso_packet_descriptor *unwrapIsoPacketDescriptor(JNIEnv *env,
    jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_iso_packet_descriptor*,
        "isoPacketDescriptorPointer");
}

/**
 * int length()
 */
JNIEXPORT jint JNICALL METHOD_NAME(IsoPacketDescriptor, length)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_iso_packet_descriptor *isopacket =
        unwrapIsoPacketDescriptor(env, this);
    if (!isopacket) return 0;

    return (jint) isopacket->length;
}

/**
 * void setLength(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(IsoPacketDescriptor, setLength)
(
    JNIEnv *env, jobject this, jint length
)
{
    struct libusb_iso_packet_descriptor *isopacket =
        unwrapIsoPacketDescriptor(env, this);
    if (!isopacket) return;

    isopacket->length = (unsigned int) length;
}

/**
 * int actualLength()
 */
JNIEXPORT jint JNICALL METHOD_NAME(IsoPacketDescriptor, actualLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_iso_packet_descriptor *isopacket =
        unwrapIsoPacketDescriptor(env, this);
    if (!isopacket) return 0;

    return (jint) isopacket->actual_length;
}

/**
 * int status()
 */
JNIEXPORT jint JNICALL METHOD_NAME(IsoPacketDescriptor, status)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_iso_packet_descriptor *isopacket =
        unwrapIsoPacketDescriptor(env, this);
    if (!isopacket) return 0;

    return isopacket->status;
}
