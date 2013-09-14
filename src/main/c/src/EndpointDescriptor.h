/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_ENDPOINT_DESCRIPTOR_H
#define USB4JAVA_ENDPOINT_DESCRIPTOR_H

#include "usb4java.h"

jobject wrapEndpointDescriptor(JNIEnv*,
    const struct libusb_endpoint_descriptor*);
jobjectArray wrapEndpointDescriptors(JNIEnv*, int,
    const struct libusb_endpoint_descriptor*);
struct libusb_endpoint_descriptor* unwrapEndpointDescriptor(JNIEnv*,
    jobject);

#endif
