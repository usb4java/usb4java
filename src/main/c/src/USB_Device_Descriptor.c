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
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, descriptor,
        sizeof(struct usb_device_descriptor));
    return (*env)->NewObject(env, cls, constructor, buffer);
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
     jfieldID field = (*env)->GetFieldID(env, cls, "data",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_device_descriptor *)
         (*env)->GetDirectBufferAddress(env, buffer);
}
