/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "InterfaceDescriptor.h"
#include "Interface.h"
#include "EndpointDescriptor.h"

jobject wrapInterfaceDescriptor(JNIEnv *env,
    const struct libusb_interface_descriptor *descriptor)
{
    WRAP_POINTER(env, descriptor, "InterfaceDescriptor",
        "interfaceDescriptorPointer");
}

jobjectArray wrapInterfaceDescriptors(JNIEnv *env, int count,
    const struct libusb_interface_descriptor *descriptors)
{
    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env, count,
        (*env)->FindClass(env, PACKAGE_DIR"/InterfaceDescriptor"), NULL);

    int i;
    for (i = 0; i < count; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrapInterfaceDescriptor(env, &descriptors[i]));

    return array;
}

struct libusb_interface_descriptor *unwrapInterfaceDescriptor(JNIEnv *env,
    jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_interface_descriptor*,
        "interfaceDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bDescriptorType;
}

/**
 * byte bInterfaceNumber()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bInterfaceNumber)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bInterfaceNumber;
}

/**
 * byte bAlternateSetting()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bAlternateSetting)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bAlternateSetting;
}

/**
 * byte bNumEndpoints()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bNumEndpoints)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bNumEndpoints;
}

/**
 * byte bInterfaceClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bInterfaceClass)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bInterfaceClass;
}

/**
 * byte bInterfaceSubClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bInterfaceSubClass)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bInterfaceSubClass;
}

/**
 * byte bInterfaceProtocol()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, bInterfaceProtocol)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->bInterfaceProtocol;
}

/**
 * byte iInterface()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(InterfaceDescriptor, iInterface)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->iInterface;
}

/**
 * EndpointDescriptor[] endpoint()
 */
JNIEXPORT jobjectArray JNICALL METHOD_NAME(InterfaceDescriptor, endpoint)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return NULL;

    return wrapEndpointDescriptors(env, interface->bNumEndpoints,
        interface->endpoint);
}

/**
 * ByteBuffer extra()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(InterfaceDescriptor, extra)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return NULL;

    return (*env)->NewDirectByteBuffer(env, (void *) interface->extra,
        interface->extra_length);
}

/**
 * int extraLength()
 */
JNIEXPORT jint JNICALL METHOD_NAME(InterfaceDescriptor, extraLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface_descriptor *interface =
        unwrapInterfaceDescriptor(env, this);
    if (!interface) return 0;

    return interface->extra_length;
}
