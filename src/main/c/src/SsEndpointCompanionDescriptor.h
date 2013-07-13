/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_SS_ENDPOINT_COMPANION_DESCRIPTOR_H
#define USB4JAVA_SS_ENDPOINT_COMPANION_DESCRIPTOR_H

#include "usb4java.h"

void setSsEndpointCompanionDescriptor(JNIEnv*,
    struct libusb_ss_endpoint_companion_descriptor*, jobject);
struct libusb_ss_endpoint_companion_descriptor*
    unwrapSsEndpointCompanionDescriptor(JNIEnv*, jobject);
void resetSsEndpointCompanionDescriptor(JNIEnv*, jobject);

#endif
