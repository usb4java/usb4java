/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Bus
 *
 * Native methods for the USB_Bus class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Device.h"


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
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/USB_Bus");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, bus,
        sizeof(struct usb_bus));
    return (*env)->NewObject(env, cls, constructor, buffer);

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
     jfieldID field = (*env)->GetFieldID(env, cls, "bus",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_bus *) (*env)->GetDirectBufferAddress(env, buffer);
}


/**
 * string dirname()
 */
 
JNIEXPORT jstring JNICALL METHOD_NAME(USB_1Bus, dirname)
(
    JNIEnv *env, jobject this
)
{
    return (*env)->NewStringUTF(env, unwrap_usb_bus(env, this)->dirname);
}


/**
 * USB_Bus next()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Bus, next)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_bus(env, unwrap_usb_bus(env, this)->next);
}


/**
 * USB_Bus prev()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Bus, prev)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_bus(env, unwrap_usb_bus(env, this)->prev);
}


/**
 * long location()
 */
 
JNIEXPORT jlong JNICALL METHOD_NAME(USB_1Bus, location)
(
    JNIEnv *env, jobject this
)
{
    return unwrap_usb_bus(env, this)->location;
}


/**
 * USB_Device devices()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Bus, devices)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_device(env, unwrap_usb_bus(env, this)->devices);
}


/**
 * USB_Device root_dev()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Bus, root_1dev)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_device(env, unwrap_usb_bus(env, this)->root_dev);
}
