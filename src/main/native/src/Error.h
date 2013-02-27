/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_ERROR_H
#define USB4JAVA_ERROR_H

#include <jni.h>
#include <libusb.h>

jobject wrap_error(JNIEnv*, const int);

#endif
