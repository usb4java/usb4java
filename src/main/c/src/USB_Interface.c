/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Interface
 *
 * Native methods for the USB_Interface class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Interface_Descriptor.h"


/**
 * Creates and returns a new USB interface wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param iface
 *            The USB interface.
 * @return The USB interface wrapper object.
 */

jobject wrap_usb_interface(JNIEnv *env, struct usb_interface *iface)
{
    if (!iface) return NULL;
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/USB_Interface");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, iface, 0);
    return (*env)->NewObject(env, cls, constructor, buffer);
}


/**
 * Returns the wrapped USB interface object from the specified wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param obj
 *            The USB interface wrapper object.
 * @return The USB interface object.
 */

struct usb_interface *unwrap_usb_interface(JNIEnv *env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "iface",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_interface *) (*env)->GetDirectBufferAddress(env, buffer);
}


/**
 * Creates and returns an array with USB interface wrapper objects.
 * 
 * @param env
 *            The JNI environment
 * @param num_interfaces
 *            The number of interfaces
 * @param interfaces
 *            The interfaces to wrap.
 * @return The array with the USB interface wrappers.
 */

jobjectArray wrap_usb_interfaces(JNIEnv *env, uint8_t num_interfaces,
    struct usb_interface *interfaces)
{
    int i;

    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env,
        num_interfaces, (*env)->FindClass(env, PACKAGE_DIR"/USB_Interface"),
        NULL);
    for (i = 0; i < num_interfaces; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrap_usb_interface(env, &interfaces[i]));
    return array;
}


/**
 * Returns the number of available interface descriptors.
 *
 * @return The number of available interface descriptors.
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Interface, num_1altsetting)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_interface(env, this)->num_altsetting;
}


/**
 * Returns the array with all available interface descriptors.
 *
 * @return The array with the interface descriptors.
 */
 
JNIEXPORT jobjectArray JNICALL METHOD_NAME(USB_1Interface, altsetting)
(
    JNIEnv *env, jobject this
)
{
    struct usb_interface *interface = unwrap_usb_interface(env, this);
    return wrap_usb_interface_descriptors(env, interface->num_altsetting,
        interface->altsetting);
}
