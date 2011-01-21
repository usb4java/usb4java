#ifndef USB_BUS_H
#define USB_BUS_H

#include <jni.h>
#include <usb.h>

extern jobject wrap_usb_bus(JNIEnv *env, struct usb_bus *bus);
extern struct usb_bus *unwrap_usb_bus(JNIEnv *env, jobject obj);

#endif
