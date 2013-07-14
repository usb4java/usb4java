/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "DeviceList.h"
#include "Device.h"

void setDeviceList(JNIEnv* env, libusb_device* const * list, jint size, jobject object)
{
    SET_POINTER(env, list, object, "deviceListPointer");

    // We already have the class from the previous call.
    field = (*env)->GetFieldID(env, cls, "size", "I");
    (*env)->SetIntField(env, object, field, size);
}

libusb_device** unwrapDeviceList(JNIEnv* env, jobject list)
{
    UNWRAP_POINTER(env, list, libusb_device**, "deviceListPointer");
}

void resetDeviceList(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "deviceListPointer");

    // We already have the class from the previous call.
    // Reset size field to zero too.
    field = (*env)->GetFieldID(env, cls, "size", "I");
    (*env)->SetIntField(env, obj, field, 0);
}

/**
 * Device get(index)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(DeviceList, get)
(
    JNIEnv *env, jobject this, jint index
)
{
    libusb_device **list = unwrapDeviceList(env, this);
    if (!list) return NULL;

    jclass cls = (*env)->GetObjectClass(env, this);
    jfieldID field = (*env)->GetFieldID(env, cls, "size", "I");
    int size = (*env)->GetIntField(env, this, field);
    if (index < 0 || index >= size) return NULL;

    return wrapDevice(env, list[index]);
}
