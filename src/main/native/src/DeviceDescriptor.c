/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "DeviceDescriptor.h"

void setDeviceDescriptor(JNIEnv* env,
    struct libusb_device_descriptor* descriptor, jobject object)
{
    SET_DATA(env, descriptor, sizeof(struct libusb_device_descriptor), object);
}

struct libusb_device_descriptor* unwrapDeviceDescriptor(JNIEnv* env,
    jobject descriptor)
{
    UNWRAP_DATA(env, descriptor, struct libusb_device_descriptor*);
}

/**
 * byte bLength()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bDescriptorType;
}

/**
 * short bcdUSB()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, bcdUSB)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bcdUSB;
}

/**
 * byte bDeviceClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceClass)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bDeviceClass;
}

/**
 * byte bDeviceSubClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceSubClass)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bDeviceSubClass;
}

/**
 * byte bDeviceProtocol()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceProtocol)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bDeviceProtocol;
}

/**
 * byte bMaxPacketSize0()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bMaxPacketSize0)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bMaxPacketSize0;
}

/**
 * short idVendor()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, idVendor)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->idVendor;
}

/**
 * short idProduct()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, idProduct)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->idProduct;
}

/**
 * short bcdDevice()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, bcdDevice)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bcdDevice;
}


/**
 * byte iManufacturer()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iManufacturer)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->iManufacturer;
}

/**
 * byte iProduct()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iProduct)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->iProduct;
}

/**
 * byte iSerialNumber()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iSerialNumber)
  (JNIEnv *env, jobject this)
{
    return unwrapDeviceDescriptor(env, this)->iSerialNumber;
}

/**
 * byte bNumConfigurations()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bNumConfigurations)
(
    JNIEnv *env, jobject this
)
{
    return unwrapDeviceDescriptor(env, this)->bNumConfigurations;
}
