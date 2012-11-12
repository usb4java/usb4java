/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Dev_Handle
 *
 * Native methods for the USB_Dev_Handle class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"


/**
 * Creates and returns a new USB device handle wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param handle
 *            The USB device handle.
 * @return The USB device handle wrapper object.
 */

jobject wrap_usb_dev_handle(JNIEnv *env, struct usb_dev_handle *handle)
{
    if (!handle) return NULL;
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/USB_Dev_Handle");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, handle, 0);
    return (*env)->NewObject(env, cls, constructor, buffer);
}


/**
 * Returns the wrapped USB device handle object from the specified wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB device handle wrapper object.
 * @return The USB device handle object.
 */

struct usb_dev_handle *unwrap_usb_dev_handle(JNIEnv *env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "handle",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_dev_handle *) (*env)->GetDirectBufferAddress(env, buffer);
}
