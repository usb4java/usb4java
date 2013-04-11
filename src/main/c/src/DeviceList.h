/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_DEVICE_LIST_H
#define USB4JAVA_DEVICE_LIST_H

#include "usb4java.h"

void setDeviceList(JNIEnv*, libusb_device**, int, jobject);
libusb_device** unwrapDeviceList(JNIEnv*, jobject);

#endif
