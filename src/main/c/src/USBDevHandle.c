/*
 * $Id$
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USBDevHandle
 *
 * Native methods for the USBDevHandle class.
 *
 * @author Klaus Reimer <k@ailis.de>
 * @version 0.1
 */

#include <jni.h>
#include <usb.h>


/**
 * Creates and returns a new USB device wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param device
 *            The USB device.
 * @return The USB device wrapper object.
 */

jobject wrap_usb_dev_handle(JNIEnv *env, struct usb_dev_handle *device)
{
    if (!device) return NULL;
    jclass cls = (*env)->FindClass(env, "de/ailis/libusb/jni/USBDevHandle");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) device);
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
  
struct usb_dev_handle *unwrap_usb_dev_handle(JNIEnv *env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_dev_handle *) ((*env)->GetLongField(env, obj, field));
}
