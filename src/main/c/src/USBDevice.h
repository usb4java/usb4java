#ifndef USB_DEVICE_H
#define USB_DEVICE_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_device(JNIEnv *env, struct usb_device *device);
extern struct usb_device *unwrap_usb_device(JNIEnv *env, jobject obj);

#endif
