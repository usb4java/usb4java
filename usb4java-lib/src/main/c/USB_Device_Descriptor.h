#ifndef USB_DEVICE_DESCRIPTOR_H
#define USB_DEVICE_DESCRIPTOR_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_device_descriptor(JNIEnv *env, struct usb_device_descriptor *device);
extern struct usb_device_descriptor *unwrap_usb_device_descriptor(JNIEnv *env, jobject obj);

#endif
