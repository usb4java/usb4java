#ifndef USB_CONFIG_DESCRIPTOR_H
#define USB_CONFIG_DESCRIPTOR_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_config_descriptor(JNIEnv *env, struct usb_config_descriptor *device);
extern struct usb_config_descriptor *unwrap_usb_config_descriptor(JNIEnv *env, jobject obj);

#endif
