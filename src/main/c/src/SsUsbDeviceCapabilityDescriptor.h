/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_SS_USB_DEVICE_CAPABILITY_DESCRIPTOR_H
#define USB4JAVA_SS_USB_DEVICE_CAPABILITY_DESCRIPTOR_H

#include "usb4java.h"

void setSsUsbDeviceCapabilityDescriptor(JNIEnv*,
    struct libusb_ss_usb_device_capability_descriptor*, jobject);
struct libusb_ss_usb_device_capability_descriptor*
    unwrapSsUsbDeviceCapabilityDescriptor(JNIEnv*, jobject);
void resetSsUsbDeviceCapabilityDescriptor(JNIEnv*, jobject);

#endif
