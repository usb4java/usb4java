/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_BOS_DESCRIPTOR_H
#define USB4JAVA_BOS_DESCRIPTOR_H

#include "usb4java.h"

void setBOSDescriptor(JNIEnv*, struct libusb_bos_descriptor*, jobject);
struct libusb_bos_descriptor* unwrapBOSDescriptor(JNIEnv*, jobject);
void resetBOSDescriptor(JNIEnv*, jobject);

#endif
