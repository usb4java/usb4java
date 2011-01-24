/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_DEVICE_H
#define USB_DEVICE_H

#include <stdint.h>
#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_device(JNIEnv *, struct usb_device *);
extern jobjectArray wrap_usb_devices(JNIEnv *, uint8_t, struct usb_device **);
extern struct usb_device *unwrap_usb_device(JNIEnv *, jobject);

#endif
