/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_INTERFACE_DESCRIPTOR_H
#define USB4JAVA_INTERFACE_DESCRIPTOR_H

#include "usb4java.h"

jobject wrapInterfaceDescriptor(JNIEnv*,
    const struct libusb_interface_descriptor*);
jobjectArray wrapInterfaceDescriptors(JNIEnv*, int,
    const struct libusb_interface_descriptor*);
struct libusb_interface_descriptor *unwrapInterfaceDescriptor(JNIEnv*, jobject);

#endif
