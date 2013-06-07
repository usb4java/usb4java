/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_DEVICE_H
#define USB4JAVA_DEVICE_H

#include "usb4java.h"

jobject wrapDevice(JNIEnv*, libusb_device*);
libusb_device* unwrapDevice(JNIEnv*, jobject);
void resetDevice(JNIEnv*, jobject);

#endif
