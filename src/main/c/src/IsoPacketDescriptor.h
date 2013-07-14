#ifndef USB4JAVA_ISO_PACKET_DESCRIPTOR_H
#define USB4JAVA_ISO_PACKET_DESCRIPTOR_H

#include "usb4java.h"

jobject wrapIsoPacketDescriptor(JNIEnv*,
    const struct libusb_iso_packet_descriptor*);
jobjectArray wrapIsoPacketDescriptors(JNIEnv*, int,
    const struct libusb_iso_packet_descriptor*);
struct libusb_iso_packet_descriptor *unwrapIsoPacketDescriptor(JNIEnv*, jobject);

#endif
