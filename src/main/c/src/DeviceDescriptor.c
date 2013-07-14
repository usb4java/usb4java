/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "DeviceDescriptor.h"

void setDeviceDescriptor(JNIEnv* env,
    const struct libusb_device_descriptor* descriptor, jobject object)
{
    SET_POINTER(env, descriptor, object, "deviceDescriptorPointer");
}

struct libusb_device_descriptor* unwrapDeviceDescriptor(JNIEnv *env,
    jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_device_descriptor*,
        "deviceDescriptorPointer");
}

void resetDeviceDescriptor(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "deviceDescriptorPointer");
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bDescriptorType;
}

/**
 * short bcdUSB()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, bcdUSB)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jshort) descriptor->bcdUSB;
}

/**
 * byte bDeviceClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceClass)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bDeviceClass;
}

/**
 * byte bDeviceSubClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceSubClass)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bDeviceSubClass;
}

/**
 * byte bDeviceProtocol()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceProtocol)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bDeviceProtocol;
}

/**
 * byte bMaxPacketSize0()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bMaxPacketSize0)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bMaxPacketSize0;
}

/**
 * short idVendor()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, idVendor)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jshort) descriptor->idVendor;
}

/**
 * short idProduct()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, idProduct)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jshort) descriptor->idProduct;
}

/**
 * short bcdDevice()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, bcdDevice)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jshort) descriptor->bcdDevice;
}


/**
 * byte iManufacturer()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iManufacturer)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->iManufacturer;
}

/**
 * byte iProduct()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iProduct)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->iProduct;
}

/**
 * byte iSerialNumber()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iSerialNumber)
  (JNIEnv *env, jobject this)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->iSerialNumber;
}

/**
 * byte bNumConfigurations()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bNumConfigurations)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor* descriptor =
        unwrapDeviceDescriptor(env, this);
    if (!descriptor) return 0;
    return (jbyte) descriptor->bNumConfigurations;
}
