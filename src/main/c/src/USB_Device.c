/*
 * $Id$
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USBDevice
 *
 * Native methods for the USBDevice class.
 *
 * @author Klaus Reimer <k@ailis.de>
 * @version 0.1
 */

#include <jni.h>
#include <usb.h>
#include "USBBus.h"
#include "USBDeviceDescriptor.h"
#include "USBConfigDescriptor.h"


/**
 * Creates and returns a new USB device wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param device
 *            The USB device.
 * @return The USB device wrapper object.
 */

jobject wrap_usb_device(JNIEnv *env, struct usb_device *device)
{
    if (!device) return NULL;
    jclass cls = (*env)->FindClass(env, "de/ailis/libusb/jni/USBDevice");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) device);
}



/**
 * Creates and returns an array with device wrapper objects.
 * 
 * @param env
 *            The JNI environment
 * @param num_devices
 *            The number of devices
 * @param devices
 *            The devices to wrap.
 * @return The array with the USB device wrappers.
 */

static jobjectArray wrap_usb_devices(JNIEnv *env, uint8_t num_children,
    struct usb_device **children)
{
    int i;
    
    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env, num_children,
        (*env)->FindClass(env, "de/ailis/libusb/jni/USBDevice"), NULL);
    for (i = 0; i < num_children; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrap_usb_device(env, children[i]));
    return array;
}


/**
 * Returns the wrapped USB device object from the specified wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB device wrapper object.
 * @return The USB device object.
 */
  
struct usb_device *unwrap_usb_device(JNIEnv *env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_device *) ((*env)->GetLongField(env, obj, field));
}


/**
 * Returns the usb device filename.
 *
 * @return The usb device filename.
 */
 
JNIEXPORT jstring JNICALL Java_de_ailis_usb4java_jni_USBDevice_filename(
    JNIEnv *env, jobject this)
{
    return (*env)->NewStringUTF(env, unwrap_usb_device(env, this)->filename);
}


/**
 * Returns the next usb device.
 *
 * @return The next usb device.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBDevice_next(
    JNIEnv *env, jobject this)
{
    return wrap_usb_device(env, unwrap_usb_device(env, this)->next);
}


/**
 * Returns the previous usb device.
 *
 * @return The previous usb device.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBDevice_prev(
    JNIEnv *env, jobject this)
{
    return wrap_usb_device(env, unwrap_usb_device(env, this)->prev);
}


/**
 * Returns the USB bus.
 *
 * @return The USB bus.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBDevice_bus(
    JNIEnv *env, jobject this)
{
    return wrap_usb_bus(env, unwrap_usb_device(env, this)->bus);
}


/**
 * Returns the device number.
 *
 * @return The device number.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDevice_devnum(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device(env, this)->devnum;
}


/**
 * Returns the number of child devices.
 *
 * @return The number of child devices..
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBDevice_num_1children(
    JNIEnv *env, jobject this)
{
    return (jshort) unwrap_usb_device(env, this)->num_children;
}


/**
 * Returns the child devices.
 *
 * @return The child devices.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBDevice_children(
    JNIEnv *env, jobject this)
{
    struct usb_device *device = unwrap_usb_device(env, this);
    return wrap_usb_devices(env, device->num_children, device->children);
}


/**
 * Returns the configuration descriptors.
 *
 * @return The configuration descriptors.
 */
 
JNIEXPORT jobjectArray JNICALL Java_de_ailis_usb4java_jni_USBDevice_config(
    JNIEnv *env, jobject this)
{
    int i;
    
    struct usb_device *device = unwrap_usb_device(env, this);
    struct usb_config_descriptor *descriptors = device->config;
    unsigned char config_count = device->descriptor.bNumConfigurations;
    jclass cls = (*env)->FindClass(env, "de/ailis/libusb/jni/USBConfigDescriptor");
    jobjectArray configs = (*env)->NewObjectArray(env, config_count, cls, 0);
    for (i = 0; i < config_count; i++)
    {
        (*env)->SetObjectArrayElement(env, configs, i,
            wrap_usb_config_descriptor(env, &descriptors[i]));
    }
    return configs;
}


/**
 * Returns the USB device descriptor.
 *
 * @return The USB device descriptor.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBDevice_descriptor(
    JNIEnv *env, jobject this)
{
    return wrap_usb_device_descriptor(env, &(unwrap_usb_device(env,
        this)->descriptor));
}


