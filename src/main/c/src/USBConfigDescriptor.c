/*
 * $Id$
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USBConfigDescriptor
 *
 * Native methods for the USBConfigDescriptor class.
 *
 * @author Klaus Reimer <k@ailis.de>
 * @version 0.1
 */

#include <jni.h>
#include <usb.h>


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
        "de/ailis/libusb/jni/USBConfigDescriptor");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) descriptor);
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
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_config_descriptor *) ((*env)->GetLongField(env,
         obj, field));
}


/**
 * Returns the bLength.
 *
 * @return The bLength.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_bLength(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->bLength;
}


/**
 * Returns the bDescriptorType.
 *
 * @return The bDescriptorType.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_bDescriptorType(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->bDescriptorType;
}


/**
 * Returns the wTotalLength.
 *
 * @return The wTotalLength.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_wTotalLength(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->wTotalLength;
}


/**
 * Returns the bNumInterfaces.
 *
 * @return The bNumInterfaces.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_bNumInterfaces(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->bNumInterfaces;
}


/**
 * Returns the bConfigurationValue.
 *
 * @return The bConfigurationValue.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_bConfigurationValue(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->bConfigurationValue;
}


/**
 * Returns the iConfiguration.
 *
 * @return The iConfiguration.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_iConfiguration(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->iConfiguration;
}


/**
 * Returns the bmAttributes.
 *
 * @return The bmAttributes.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_bmAttributes(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->bmAttributes;
}


/**
 * Returns the MaxPower.
 *
 * @return The MaxPower.
 */
 
JNIEXPORT jbyte JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_MaxPower(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->MaxPower;
}


/**
 * Returns the extralen.
 *
 * @return The idProduct.
 */
 
JNIEXPORT jshort JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_extralen(
    JNIEnv *env, jobject this)
{
    return unwrap_usb_config_descriptor(env, this)->extralen;
}


/**
 * Returns the extra descriptors.
 *
 * @return The extra descriptors.
 */
 
JNIEXPORT jbyteArray JNICALL Java_de_ailis_usb4java_jni_USBConfigDescriptor_extra(
    JNIEnv *env, jobject this)
{
    struct usb_config_descriptor *descriptor = unwrap_usb_config_descriptor(env, this);
    jbyteArray array = (*env)->NewByteArray(env, descriptor->extralen);
    (*env)->SetByteArrayRegion(env, array, 0, descriptor->extralen,
        (const jbyte *) descriptor->extra);
    return array;
}
