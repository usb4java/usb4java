/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB_Device
 *
 * Native methods for the USB_Device class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Bus.h"
#include "USB_Device_Descriptor.h"
#include "USB_Config_Descriptor.h"


/**
 * Creates and returns a new USB device wrapper object.
 *
 * @param env
 *            The JNI environment.
 * @param device
 *            The USB device.
 * @return The USB device wrapper object.
 */

jobject wrap_usb_device(JNIEnv *env, struct usb_device *device)
{
    if (!device) return NULL;
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/USB_Device");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, device,
        sizeof(struct usb_device));
    return (*env)->NewObject(env, cls, constructor, buffer);

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

struct usb_device *unwrap_usb_device(JNIEnv *env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "device",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct usb_device *) (*env)->GetDirectBufferAddress(env, buffer);
}


/**
 * Creates and returns an array with device wrapper objects.
 * 
 * @param env
 *            The JNI environment
 * @param num_devices
 *            The number of devices
 * @param devices
 *            The devices to wrap.
 * @return The array with the USB device wrappers.
 */

static jobjectArray wrap_usb_devices(JNIEnv *env, uint8_t num_devices,
    struct usb_device **devices)
{
    int i;
    
    jobjectArray array = (jobjectArray) (*env)->NewObjectArray(env, num_devices,
        (*env)->FindClass(env, PACKAGE_DIR"/USB_Device"), NULL);
    for (i = 0; i < num_devices; i++)
        (*env)->SetObjectArrayElement(env, array, i,
            wrap_usb_device(env, devices[i]));
    return array;
}


/**
 * string filename()
 */
 
JNIEXPORT jstring JNICALL METHOD_NAME(USB_1Device, filename)
(
    JNIEnv *env, jobject this
)
{
    return (*env)->NewStringUTF(env, unwrap_usb_device(env, this)->filename);
}


/**
 * USB_Device next()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Device, next)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_device(env, unwrap_usb_device(env, this)->next);
}


/**
 * USB_Device prev()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Device, prev)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_device(env, unwrap_usb_device(env, this)->prev);
}


/**
 * USB_Bus bus().
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Device, bus)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_bus(env, unwrap_usb_device(env, this)->bus);
}


/**
 * short devnum()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device, devnum)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device(env, this)->devnum;
}


/**
 * short num_children()
 */
 
JNIEXPORT jshort JNICALL METHOD_NAME(USB_1Device, num_1children)
(
    JNIEnv *env, jobject this
)
{
    return (jshort) unwrap_usb_device(env, this)->num_children;
}


/**
 * USB_Device children()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Device, children)
(
    JNIEnv *env, jobject this
)
{
    struct usb_device *device = unwrap_usb_device(env, this);
    return wrap_usb_devices(env, device->num_children, device->children);
}


/**
 * USB_Config_Descriptor[] config()
 */
 
JNIEXPORT jobjectArray JNICALL METHOD_NAME(USB_1Device, config)
(
    JNIEnv *env, jobject this
)
{
    int i;
    
    struct usb_device *device = unwrap_usb_device(env, this);
    struct usb_config_descriptor *descriptors = device->config;
    unsigned char config_count = device->descriptor.bNumConfigurations;
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/USB_Config_Descriptor");
    jobjectArray configs = (*env)->NewObjectArray(env, config_count, cls, 0);
    for (i = 0; i < config_count; i++)
    {
        (*env)->SetObjectArrayElement(env, configs, i,
            wrap_usb_config_descriptor(env, &descriptors[i]));
    }
    return configs;
}


/**
 * USB_Device_Descriptor descriptor()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB_1Device, descriptor)
(
    JNIEnv *env, jobject this
)
{
    return wrap_usb_device_descriptor(env, &(unwrap_usb_device(env,
        this)->descriptor));
}
