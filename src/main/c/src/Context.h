/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_CONTEXT_H
#define USB4JAVA_CONTEXT_H

#include "usb4java.h"

void setContext(JNIEnv*, const libusb_context*, jobject);
libusb_context* unwrapContext(JNIEnv*, jobject);
void resetContext(JNIEnv*, jobject);

#endif
