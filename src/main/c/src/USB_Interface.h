/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_INTERFACE_H
#define USB_INTERFACE_H

#include <stdint.h>
#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_interface(JNIEnv *, struct usb_interface *);
extern jobjectArray wrap_usb_interfaces(JNIEnv *, uint8_t,
    struct usb_interface *);

#endif
