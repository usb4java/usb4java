/*
 * $Id$
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USBBus
 *
 * Native methods for the USBBus class.
 *
 * @author Klaus Reimer <k@ailis.de>
 * @version 0.1
 */

#include <jni.h>
#include <usb.h>
#include "USBDevice.h"


/**
 * Creates and returns a new USB bus wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param bus
 *            The USB bus.
 * @return The USB bus wrapper object.
 */

jobject wrap_usb_bus(JNIEnv *env, struct usb_bus *bus)
{
    if (!bus) return NULL;
    jclass cls = (*env)->FindClass(env, "de/ailis/libusb/jni/USBBus");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) bus);
}


/**
 * Returns the wrapped USB bus object from the specified wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB bus wrapper object.
 * @return The USB bus object.
 */
  
struct usb_bus *unwrap_usb_bus(JNIEnv *env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_bus *) ((*env)->GetLongField(env, obj, field));
}


/**
 * Returns the usb bus dirname.
 *
 * @return The usb bus dirname.
 */
 
JNIEXPORT jstring JNICALL Java_de_ailis_usb4java_jni_USBBus_dirname(
    JNIEnv *env, jobject this)
{
    return (*env)->NewStringUTF(env, unwrap_usb_bus(env, this)->dirname);
}


/**
 * Returns the next usb bus.
 *
 * @return The next usb bus.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBBus_next(
    JNIEnv *env, jobject this)
{
    return wrap_usb_bus(env, unwrap_usb_bus(env, this)->next);
}


/**
 * Returns the previous usb bus.
 *
 * @return The previous usb bus.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBBus_prev(
    JNIEnv *env, jobject this)
{
    return wrap_usb_bus(env, unwrap_usb_bus(env, this)->prev);
}


/**
 * Returns the usb bus location.
 *
 * @return The usb bus location.
 */
 
JNIEXPORT jlong JNICALL Java_de_ailis_usb4java_jni_USBBus_location(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_bus(env, this)->location;
}


/**
 * Returns the usb devices.
 *
 * @return The usb devices.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBBus_devices(
    JNIEnv *env, jobject this)
{
    return wrap_usb_device(env, unwrap_usb_bus(env, this)->devices);
}


/**
 * Returns the usb root device.
 *
 * @return The usb root device.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USBBus_root_1dev(
    JNIEnv *env, jobject this)
{
    return wrap_usb_device(env, unwrap_usb_bus(env, this)->root_dev);
}

