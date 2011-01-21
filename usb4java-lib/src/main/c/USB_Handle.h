#ifndef USB_DEV_HANDLE_H
#define USB_DEV_HANDLE_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_dev_handle(JNIEnv *env, struct usb_dev_handle *device);
extern struct usb_dev_handle *unwrap_usb_dev_handle(JNIEnv *env, jobject obj);

#endif
