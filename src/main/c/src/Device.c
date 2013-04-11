/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Device.h"

jobject wrapDevice(JNIEnv* env, libusb_device* device)
{
    WRAP_POINTER(env, device, "Device");
}

libusb_device* unwrapDevice(JNIEnv* env, jobject device)
{
    UNWRAP_POINTER(env, device, libusb_device*);
}
