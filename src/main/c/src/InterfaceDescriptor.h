/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_INTERFACE_DESCRIPTOR_H
#define USB4JAVA_INTERFACE_DESCRIPTOR_H

#include "usb4java.h"

jobjectArray wrapInterfaceDescriptors(JNIEnv*, int,
    const struct libusb_interface_descriptor*);

#endif
