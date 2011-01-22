/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_String_Descriptor
 *
 * Native methods for the USB_String_Descriptor class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"

/**
 * Creates and returns a new USB string descriptor wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param device
 *            The USB string descriptor.
 * @return The USB string descriptor wrapper object.
 */

jobject wrap_usb_string_descriptor(JNIEnv *env,
    struct usb_string_descriptor *descriptor)
{
    if (!descriptor) return NULL;
    jclass cls = (*env)->FindClass(env,
        PACKAGE_DIR"/USB_String_Descriptor");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) descriptor);
}


/**
 * Returns the wrapped USB string descriptor object from the specified
 * wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB string descriptor wrapper object.
 * @return The USB string descriptor object.
 */
  
struct usb_string_descriptor *unwrap_usb_string_descriptor(JNIEnv *env,
    jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_string_descriptor *) ((*env)->GetLongField(env,
         obj, field));
}


/**
 * char[] wData()
 */
 
JNIEXPORT jcharArray JNICALL METHOD_NAME(USB_1String_1Descriptor, wData)
(
    JNIEnv *env, jobject this
)
{
    struct usb_string_descriptor *descriptor = unwrap_usb_string_descriptor(env, this);
    int size = (descriptor->bLength - 2) / 2;
    jcharArray array = (*env)->NewByteArray(env, size);
    (*env)->SetCharArrayRegion(env, array, 0, size,
        (const jchar *) descriptor->wData);
    return array;
}
