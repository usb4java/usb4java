/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_DEVICE_DESCRIPTOR_H
#define USB_DEVICE_DESCRIPTOR_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_device_descriptor(JNIEnv *, struct usb_device_descriptor *);
extern struct usb_device_descriptor *unwrap_usb_device_descriptor(JNIEnv *, jobject);

#endif
