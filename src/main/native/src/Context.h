/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_CONTEXT_H
#define USB4JAVA_CONTEXT_H

#include <jni.h>
#include <libusb.h>

void wrap_context(JNIEnv*, libusb_context*, jobject);
libusb_context* unwrap_context(JNIEnv*, jobject);

#endif
