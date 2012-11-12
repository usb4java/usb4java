/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB_CONFIG_DESCRIPTOR_H
#define USB_CONFIG_DESCRIPTOR_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_config_descriptor(JNIEnv *, struct usb_config_descriptor *);
extern struct usb_config_descriptor *unwrap_usb_config_descriptor(JNIEnv *, jobject);

#endif
