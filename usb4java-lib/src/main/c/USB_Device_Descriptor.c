/*
 * $Id$
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USBDeviceDescriptor
 *
 * Native methods for the USBDeviceDescriptor class.
 *
 * @author Klaus Reimer <k@ailis.de>
 * @version 0.1
 */

#include <jni.h>
#include <usb.h>
#include "USB_Bus.h"


/**
 * Creates and returns a new USB device descriptor wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param device
 *            The USB device descriptor.
 * @return The USB device descriptor wrapper object.
 */

jobject wrap_usb_device_descriptor(JNIEnv *env,
    struct usb_device_descriptor *descriptor)
{
    if (!descriptor) return NULL;
    jclass cls = (*env)->FindClass(env,
        "de/ailis/libusb/jni/USBDeviceDescriptor");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) descriptor);
}


/**
 * Returns the wrapped USB device descriptor object from the specified
 * wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB device descriptor wrapper object.
 * @return The USB device descriptor object.
 */
  
struct usb_device_descriptor *unwrap_usb_device_descriptor(JNIEnv *env,
    jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_device_descriptor *) ((*env)->GetLongField(env,
         obj, field));
}


/**
 * Returns the bLength.
 *
 * @return The bLength.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bLength(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bLength;
}


/**
 * Returns the bDescriptorType.
 *
 * @return The bDescriptorType.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bDescriptorType(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDescriptorType;
}


/**
 * Returns the bcdUSB.
 *
 * @return The bcdUSB.
 */
 
JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bcdUSB(
    JNIEnv *env, jobject this)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->bcdUSB;
}


/**
 * Returns the bDeviceClass.
 *
 * @return The bDeviceClass.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bDeviceClass(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDeviceClass;
}


/**
 * Returns the bDeviceSubClass.
 *
 * @return The bDeviceSubClass.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bDeviceSubClass(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDeviceSubClass;
}


/**
 * Returns the bDeviceProtocol.
 *
 * @return The bDeviceProtocol.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bDeviceProtocol(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDeviceProtocol;
}


/**
 * Returns the bMaxPacketSize0.
 *
 * @return The bMaxPacketSize0.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bMaxPacketSize0(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bMaxPacketSize0;
}


/**
 * Returns the idVendor.
 *
 * @return The idVendor.
 */
 
JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_idVendor(
    JNIEnv *env, jobject this)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->idVendor;
}

/**
 * Returns the idProduct.
 *
 * @return The idProduct.
 */
 
JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_idProduct(
    JNIEnv *env, jobject this)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->idProduct;
}


/**
 * Returns the bcdDevice.
 *
 * @return The bcdDevice.
 */
 
JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bcdDevice(
    JNIEnv *env, jobject this)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->bcdDevice;
}


/**
 * Returns the iManufacturer.
 *
 * @return The iManufacturer.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_iManufacturer(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->iManufacturer;
}


/**
 * Returns the iProduct.
 *
 * @return The iProduct.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_iProduct(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->iProduct;
}


/**
 * Returns the iSerialNumber.
 *
 * @return The iSerialNumber.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_iSerialNumber(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->iSerialNumber;
}


/**
 * Returns the bNumConfigurations.
 *
 * @return The bNumConfigurations.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDeviceDescriptor_bNumConfigurations(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bNumConfigurations;
}
