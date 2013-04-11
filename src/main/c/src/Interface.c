/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Interface.h"
#include "InterfaceDescriptor.h"

jobject wrapInterface(JNIEnv *env, const struct libusb_interface *iface)
{
    WRAP_POINTER(env, iface, "Interface");
}

jobjectArray wrapInterfaces(JNIEnv *env, int count,
    const struct libusb_interface *interfaces)
{
    int i;

    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env,
        count, (*env)->FindClass(env, PACKAGE_DIR"/Interface"),
        NULL);
    for (i = 0; i < count; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrapInterface(env, &interfaces[i]));
    return array;
}

struct libusb_interface *unwrapInterface(JNIEnv *env, jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_interface*);
}

JNIEXPORT jshort JNICALL METHOD_NAME(Interface, numAltsetting)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrapInterface(env, this)->num_altsetting;
}

JNIEXPORT jobjectArray JNICALL METHOD_NAME(Interface, altsetting)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_interface* interface = unwrapInterface(env, this);
    return wrapInterfaceDescriptors(env, interface->num_altsetting,
        interface->altsetting);
}
