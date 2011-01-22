/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_INTERFACE_H
#define USB_INTERFACE_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_interface(JNIEnv *env, struct usb_interface *interface);
extern jobjectArray wrap_usb_interfaces(JNIEnv *env, uint8_t num_children,
    struct usb_interface *interfaces);

#endif
