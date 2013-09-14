/*
 * Copyright (C) 2013 Luca Longinotti (l@longi.li)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_HOTPLUG_CALLBACK_HANDLE_H
#define USB4JAVA_HOTPLUG_CALLBACK_HANDLE_H

#include "usb4java.h"

void setHotplugCallbackHandle(JNIEnv*, const libusb_hotplug_callback_handle, jobject);
libusb_hotplug_callback_handle unwrapHotplugCallbackHandle(JNIEnv*, jobject);
void resetHotplugCallbackHandle(JNIEnv*, jobject);

#endif
