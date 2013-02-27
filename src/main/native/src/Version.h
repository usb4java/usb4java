/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_VERSION_H
#define USB4JAVA_VERSION_H

#include <jni.h>
#include <libusb.h>

jobject wrap_version(JNIEnv*, const struct libusb_version*);
const struct libusb_version* unwrap_version(JNIEnv*, jobject);

#endif
