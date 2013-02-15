/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_DEV_HANDLE_H
#define USB_DEV_HANDLE_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_dev_handle(JNIEnv *, struct usb_dev_handle *);
extern struct usb_dev_handle *unwrap_usb_dev_handle(JNIEnv *, jobject);

#endif
