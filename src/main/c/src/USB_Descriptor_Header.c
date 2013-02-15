/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Descriptor_Header
 *
 * Native methods for the USB_Descriptor_Header class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"


/**
 * Returns the wrapped USB config descriptor object from the specified
 * wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB config descriptor wrapper object.
 * @return The USB config descriptor object.
 */
  
struct usb_descriptor_header *unwrap_usb_descriptor_header(JNIEnv *env,
    jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "data",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_descriptor_header *)
         (*env)->GetDirectBufferAddress(env, buffer);
}


/**
 * int bLength()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Descriptor_1Header, bLength)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_descriptor_header(env, this)->bLength;
}


/**
 * int bDescriptorType()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Descriptor_1Header, bDescriptorType)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_descriptor_header(env, this)->bDescriptorType;
}
