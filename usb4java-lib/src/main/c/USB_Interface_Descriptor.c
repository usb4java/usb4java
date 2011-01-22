/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Interface_Descriptor
 *
 * Native methods for the USB_Interface_Descriptor class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Endpoint_Descriptor.h"


/**
 * Creates and returns a new USB interface descriptor wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param descriptor
 *            The USB interface descriptor.
 * @return The USB interface descriptor wrapper object.
 */

jobject wrap_usb_interface_descriptor(JNIEnv *env,
    struct usb_interface_descriptor *descriptor)
{
    if (!descriptor) return NULL;
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/USB_Interface_Descriptor");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return (*env)->NewObject(env, cls, constructor, (long) descriptor);
}


/**
 * Creates and returns an array with USB interface descriptor wrapper objects.
 *
 * @param env
 *            The JNI environment
 * @param num_descriptors
 *            The number of descriptors
 * @param descriptors
 *            The descriptors to wrap.
 * @return The array with the USB interface descriptor wrappers.
 */

jobjectArray wrap_usb_interface_descriptors(JNIEnv *env, uint8_t num_descriptors,
    struct usb_interface_descriptor *descriptors)
{
    int i;

    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env,
        num_descriptors, (*env)->FindClass(env,
        PACKAGE_DIR"/USB_Interface_Descriptor"), NULL);
    for (i = 0; i < num_descriptors; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrap_usb_interface_descriptor(env, &descriptors[i]));
    return array;
}


/**
 * Returns the wrapped USB interface descriptor object from the specified
 * wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB interface descriptor wrapper object.
 * @return The USB interface descriptor object.
 */
  
struct usb_interface_descriptor *unwrap_usb_interface_descriptor(JNIEnv *env,
    jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "pointer", "J");
     return (struct usb_interface_descriptor *) ((*env)->GetLongField(env,
         obj, field));
}


/**
 * short bInterfaceNumber()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, bInterfaceNumber)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->bInterfaceNumber;
}


/**
 * short bAlternateSetting()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, bAlternateSetting)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->bAlternateSetting;
}


/**
 * short bNumEndpoints()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, bNumEndpoints)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->bNumEndpoints;
}


/**
 * short bInterfaceClass()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, bInterfaceClass)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->bInterfaceClass;
}


/**
 * short bInterfaceSubClass()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, bInterfaceSubClass)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->bInterfaceSubClass;
}


/**
 * short bInterfaceProtocol()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, bInterfaceProtocol)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->bInterfaceProtocol;
}


/**
 * short iInterface()
 */

JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface_1Descriptor, iInterface)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface_descriptor(env, this)->iInterface;
}


/**
 * int extralen()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB_1Interface_1Descriptor, extralen)
(
    JNIEnv *env, jobject this
)
{
    return unwrap_usb_interface_descriptor(env, this)->extralen;
}


/**
 * byte[] extra()
 */

JNIEXPORT jbyteArray JNICALL METHOD_NAME(USB_1Interface_1Descriptor, extra)
(
    JNIEnv *env, jobject this
)
{
    struct usb_interface_descriptor *descriptor =
        unwrap_usb_interface_descriptor(env, this);
    jbyteArray array = (*env)->NewByteArray(env, descriptor->extralen);
    (*env)->SetByteArrayRegion(env, array, 0, descriptor->extralen,
        (const jbyte *) descriptor->extra);
    return array;
}


/**
 * USB_Endpoint_Descriptor[] endpoint()
 */

JNIEXPORT jobjectArray JNICALL METHOD_NAME(USB_1Interface_1Descriptor, endpoint)
(
    JNIEnv *env, jobject this
)
{
    struct usb_interface_descriptor *descriptor =
        unwrap_usb_interface_descriptor(env, this);
    return wrap_usb_endpoint_descriptors(env, descriptor->bNumEndpoints,
        descriptor->endpoint);
}
