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
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bLength;
}

/**
 * byte bDescriptorType()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bDescriptorType;
}

/**
 * short bcdUSB()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, bcdUSB)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jshort) device->bcdUSB;
}

/**
 * byte bDeviceClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceClass)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bDeviceClass;
}

/**
 * byte bDeviceSubClass()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceSubClass)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bDeviceSubClass;
}

/**
 * byte bDeviceProtocol()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bDeviceProtocol)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bDeviceProtocol;
}

/**
 * byte bMaxPacketSize0()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bMaxPacketSize0)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bMaxPacketSize0;
}

/**
 * short idVendor()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, idVendor)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jshort) device->idVendor;
}

/**
 * short idProduct()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, idProduct)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jshort) device->idProduct;
}

/**
 * short bcdDevice()
 */
JNIEXPORT jshort JNICALL METHOD_NAME(DeviceDescriptor, bcdDevice)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jshort) device->bcdDevice;
}


/**
 * byte iManufacturer()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iManufacturer)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->iManufacturer;
}

/**
 * byte iProduct()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iProduct)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->iProduct;
}

/**
 * byte iSerialNumber()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, iSerialNumber)
  (JNIEnv *env, jobject this)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->iSerialNumber;
}

/**
 * byte bNumConfigurations()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(DeviceDescriptor, bNumConfigurations)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_device_descriptor *device = unwrapDeviceDescriptor(env, this);
    if (!device) return 0;

    return (jbyte) device->bNumConfigurations;
}
