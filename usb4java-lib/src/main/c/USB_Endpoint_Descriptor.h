/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_ENDPOINT_DESCRIPTOR_H
#define USB_ENDPOINT_DESCRIPTOR_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_endpoint_descriptor(JNIEnv *,
    struct usb_endpoint_descriptor *);
extern jobjectArray wrap_usb_endpoint_descriptors(JNIEnv *, uint8_t,
    struct usb_endpoint_descriptor *);
extern struct usb_endpoint_descriptor *unwrap_usb_endpoint_descriptor(
    JNIEnv *, jobject);

#endif
