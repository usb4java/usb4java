/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_CONTAINER_ID_DESCRIPTOR_H
#define USB4JAVA_CONTAINER_ID_DESCRIPTOR_H

#include "usb4java.h"

void setContainerIdDescriptor(JNIEnv*,
    struct libusb_container_id_descriptor*, jobject);
struct libusb_container_id_descriptor*
    unwrapContainerIdDescriptor(JNIEnv*, jobject);
void resetContainerIdDescriptor(JNIEnv*, jobject);

#endif
