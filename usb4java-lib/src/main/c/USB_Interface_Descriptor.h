/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_INTERFACE_DESCRIPTOR_H
#define USB_INTERFACE_DESCRIPTOR_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_interface_descriptor(JNIEnv *,
    struct usb_interface_descriptor *);
extern jobjectArray wrap_usb_interface_descriptors(JNIEnv *, uint8_t,
    struct usb_interface_descriptor *);
extern struct usb_interface_descriptor *unwrap_usb_interface_descriptor(
    JNIEnv *, jobject);

#endif
