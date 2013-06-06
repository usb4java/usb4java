/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_DEVICE_DESCRIPTOR_H
#define USB4JAVA_DEVICE_DESCRIPTOR_H

#include "usb4java.h"

void setDeviceDescriptor(JNIEnv*, struct libusb_device_descriptor*, jobject);
struct libusb_device_descriptor* unwrapDeviceDescriptor(JNIEnv*, jobject);
void resetDeviceDescriptor(JNIEnv*, jobject);

#endif
