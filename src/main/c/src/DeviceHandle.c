/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "DeviceHandle.h"

void setDeviceHandle(JNIEnv* env, libusb_device_handle* deviceHandle,
    jobject object)
{
    SET_POINTER(env, deviceHandle, object, "deviceHandlePointer");
}

jobject wrapDeviceHandle(JNIEnv* env, libusb_device_handle* deviceHandle)
{
    WRAP_POINTER(env, deviceHandle, "DeviceHandle", "deviceHandlePointer");
}

libusb_device_handle* unwrapDeviceHandle(JNIEnv* env, jobject deviceHandle)
{
    UNWRAP_POINTER(env, deviceHandle, libusb_device_handle*,
        "deviceHandlePointer");
}

void resetDeviceHandle(JNIEnv* env, jobject object)
{
    RESET_POINTER(env, object, "deviceHandlePointer");
}
