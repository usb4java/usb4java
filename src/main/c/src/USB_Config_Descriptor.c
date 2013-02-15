/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Config_Descriptor
 *
 * Native methods for the USB_Config_Descriptor class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Interface.h"

/**
 * Creates and returns a new USB config descriptor wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param device
 *            The USB config descriptor.
 * @return The USB config descriptor wrapper object.
 */

jobject wrap_usb_config_descriptor(JNIEnv *env,
    struct usb_config_descriptor *descriptor)
{
    if (!descriptor) return NULL;
    jclass cls = (*env)->FindClass(env,
        PACKAGE_DIR"/USB_Config_Descriptor");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, descriptor,
        sizeof(struct usb_config_descriptor));
    return (*env)->NewObject(env, cls, constructor, buffer);
}


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
  
struct usb_config_descriptor *unwrap_usb_config_descriptor(JNIEnv *env,
    jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "data",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_config_descriptor *)
         (*env)->GetDirectBufferAddress(env, buffer);
}


/**
 * int extralen()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, extralen)
(
    JNIEnv *env, jobject this
)
{
    return unwrap_usb_config_descriptor(env, this)->extralen;
}


/**
 * ByteBuffer extra()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Config_1Descriptor, extra)
(
    JNIEnv *env, jobject this
)
{
    struct usb_config_descriptor *descriptor =
        unwrap_usb_config_descriptor(env, this);
    return (*env)->NewDirectByteBuffer(env, descriptor, descriptor->extralen);
}


/**
 * USB_Interface[] iface()
 */

JNIEXPORT jobjectArray JNICALL METHOD_NAME(USB_1Config_1Descriptor, iface)
(
    JNIEnv *env, jobject this
)
{
    struct usb_config_descriptor *descriptor = unwrap_usb_config_descriptor(
        env, this);
    return wrap_usb_interfaces(env, descriptor->bNumInterfaces,
        descriptor->interface);
}


/**
 * int bConfigurationValue()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, bConfigurationValue)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_config_descriptor(env, this)->bConfigurationValue;
}


/**
 * int bmAttributes()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, bmAttributes)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_config_descriptor(env, this)->bmAttributes;
}


/**
 * int MaxPower()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, bMaxPower)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_config_descriptor(env, this)->MaxPower;
}


/**
 * int bNumInterfaces()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, bNumInterfaces)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_config_descriptor(env, this)->bNumInterfaces;
}


/**
 * int iConfiguration()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, iConfiguration)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_config_descriptor(env, this)->iConfiguration;
}


/**
 * int wTotalLength()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Config_1Descriptor, wTotalLength)
  (JNIEnv *env, jobject this)
{
	return unwrap_usb_config_descriptor(env, this)->wTotalLength;
}
