/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_USB_2_0_EXTENSION_DESCRIPTOR_H
#define USB4JAVA_USB_2_0_EXTENSION_DESCRIPTOR_H

#include "usb4java.h"

void setUsb20ExtensionDescriptor(JNIEnv*,
    struct libusb_usb_2_0_extension_descriptor*, jobject);
struct libusb_usb_2_0_extension_descriptor*
    unwrapUsb20ExtensionDescriptor(JNIEnv*, jobject);
void resetUsb20ExtensionDescriptor(JNIEnv*, jobject);

#endif
