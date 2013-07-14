/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Device.h"

jobject wrapDevice(JNIEnv* env, const libusb_device* device)
{
    WRAP_POINTER(env, device, "Device", "devicePointer");
}

libusb_device* unwrapDevice(JNIEnv* env, jobject device)
{
    UNWRAP_POINTER(env, device, libusb_device*, "devicePointer");
}

void resetDevice(JNIEnv* env, jobject object)
{
    RESET_POINTER(env, object, "devicePointer");
}
