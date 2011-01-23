/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_DEVICE_H
#define USB_DEVICE_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_device(JNIEnv *env, struct usb_device *device);
extern jobjectArray wrap_usb_devices(JNIEnv *env, uint8_t num_devices,
    struct usb_device **devices);
extern struct usb_device *unwrap_usb_device(JNIEnv *env, jobject obj);

#endif
