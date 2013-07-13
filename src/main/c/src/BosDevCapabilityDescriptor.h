/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_BOS_DEV_CAPABILITY_DESCRIPTOR_H
#define USB4JAVA_BOS_DEV_CAPABILITY_DESCRIPTOR_H

#include "usb4java.h"

jobjectArray wrapBosDevCapabilityDescriptors(JNIEnv*, int,
    struct libusb_bos_dev_capability_descriptor**);
struct libusb_bos_dev_capability_descriptor
    *unwrapBosDevCapabilityDescriptor(JNIEnv *, jobject);

#endif
