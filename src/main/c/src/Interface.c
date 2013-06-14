/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Interface.h"
#include "InterfaceDescriptor.h"

jobject wrapInterface(JNIEnv *env, const struct libusb_interface *iface)
{
    WRAP_POINTER(env, iface, "Interface", "interfacePointer");
}

jobjectArray wrapInterfaces(JNIEnv *env, int count,
    const struct libusb_interface *interfaces)
{
    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env, count,
        (*env)->FindClass(env, PACKAGE_DIR"/Interface"), NULL);

    for (int i = 0; i < count; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrapInterface(env, &interfaces[i]));

    return array;
}

struct libusb_interface *unwrapInterface(JNIEnv *env, jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_interface*, "interfacePointer");
}

JNIEXPORT jint JNICALL METHOD_NAME(Interface, numAltsetting)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface *iface = unwrapInterface(env, this);
    if (!iface) return 0;

    return iface->num_altsetting;
}

JNIEXPORT jobjectArray JNICALL METHOD_NAME(Interface, altsetting)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface *iface = unwrapInterface(env, this);
    if (!iface) return NULL;

    return wrapInterfaceDescriptors(env, iface->num_altsetting,
        iface->altsetting);
}
