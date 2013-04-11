/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "DeviceList.h"
#include "Device.h"

void setDeviceList(JNIEnv* env, libusb_device** list, int size, jobject object)
{
    SET_POINTER(env, list, object);

    jclass cls = (*env)->GetObjectClass(env, object);
    jfieldID field = (*env)->GetFieldID(env, cls, "size", "I");
    (*env)->SetIntField(env, object, field, size);
}

libusb_device** unwrapDeviceList(JNIEnv* env, jobject list)
{
    UNWRAP_POINTER(env, list, libusb_device**);
}

/**
 * Device get(index)
 */
JNIEXPORT jobject JNICALL METHOD_NAME(DeviceList, get)
(
    JNIEnv *env, jobject this, jint index
)
{
    jclass cls = (*env)->GetObjectClass(env, this);
    jfieldID field = (*env)->GetFieldID(env, cls, "size", "I");
    int size = (*env)->GetIntField(env, this, field);
    if (index < 0 || index >= size) return NULL;
    return wrapDevice(env, unwrapDeviceList(env, this)[index]);
}
