/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Device_Descriptor
 *
 * Native methods for the USB_Device_Descriptor class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Bus.h"


/**
 * Creates and returns a new USB device descriptor wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param descriptor
 *            The USB device descriptor.
 * @return The USB device descriptor wrapper object.
 */

jobject wrap_usb_device_descriptor(JNIEnv *env,
    struct usb_device_descriptor *descriptor)
{
    if (!descriptor) return NULL;
    jclass cls = (*env)->FindClass(env,
        PACKAGE_DIR"/USB_Device_Descriptor");
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
 * short bLength()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bLength)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bLength;
}


/**
 * short bDescriptorType()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bDescriptorType)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDescriptorType;
}


/**
 * int bcdUSB()
 */
 
JNIEXPORT jint JNICALL METHOD_NAME(USB_1Device_1Descriptor, bcdUSB)
(
    JNIEnv *env, jobject this
)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->bcdUSB;
}


/**
 * short bDeviceClass()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bDeviceClass)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDeviceClass;
}


/**
 * short bDeviceSubClass()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bDeviceSubClass)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDeviceSubClass;
}


/**
 * short bDeviceProtocol()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bDeviceProtocol)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bDeviceProtocol;
}


/**
 * short bMaxPacketSize0()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bMaxPacketSize0)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bMaxPacketSize0;
}


/**
 * int idVendor()
 */
 
JNIEXPORT jint JNICALL METHOD_NAME(USB_1Device_1Descriptor, idVendor)
(
    JNIEnv *env, jobject this
)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->idVendor;
}


/**
 * int idProduct()
 */
 
JNIEXPORT jint JNICALL METHOD_NAME(USB_1Device_1Descriptor, idProduct)
(
    JNIEnv *env, jobject this
)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->idProduct;
}


/**
 * int bcdDevice()
 */
 
JNIEXPORT jint JNICALL METHOD_NAME(USB_1Device_1Descriptor, bcdDevice)
(
    JNIEnv *env, jobject this
)
{
    return (jint) unwrap_usb_device_descriptor(env, this)->bcdDevice;
}


/**
 * short iManufacturer()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, iManufacturer)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->iManufacturer;
}


/**
 * short iProduct()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, iProduct)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->iProduct;
}


/**
 * short iSerialNumber()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, iSerialNumber)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->iSerialNumber;
}


/**
 * short bNumConfigurations()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device_1Descriptor, bNumConfigurations)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device_descriptor(env, this)->bNumConfigurations;
}
