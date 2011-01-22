/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_BUS_H
#define USB_BUS_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_bus(JNIEnv *env, struct usb_bus *bus);
extern struct usb_bus *unwrap_usb_bus(JNIEnv *env, jobject obj);

#endif
