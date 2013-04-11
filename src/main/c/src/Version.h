/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_VERSION_H
#define USB4JAVA_VERSION_H

#include "usb4java.h"

jobject wrapVersion(JNIEnv*, const struct libusb_version*);
const struct libusb_version* unwrapVersion(JNIEnv*, jobject);

#endif
