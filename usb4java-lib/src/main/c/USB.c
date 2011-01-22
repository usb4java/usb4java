/*
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB
 *
 * Native methods for the USB class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <usb.h>
#include "usb4java.h"
#include "USB_Bus.h"
#include "USB_Device.h"
#include "USB_Dev_Handle.h"


/**
 * void usb_init()
 */

JNIEXPORT void JNICALL METHOD_NAME(USB, usb_1init)
(
    JNIEnv *env, jclass class
)
{
    usb_init(); 
}


/**
 * int usb_find_busses()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1find_1busses)
(
    JNIEnv *env, jclass class
)
{
    return usb_find_busses();
}


/**
 * int usb_find_devices()
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1find_1devices)
(
    JNIEnv *env, jclass class
)
{
    return usb_find_devices();
}


/**
 * USB_Bus usb_get_busses()
 */
 
JNIEXPORT jobject JNICALL METHOD_NAME(USB, usb_1get_1busses)
(
    JNIEnv *env, jclass class
)
{
    return wrap_usb_bus(env, usb_get_busses());
}


/**
 * USB_Handle usb_open(USB_Device)
 */

JNIEXPORT jobject JNICALL METHOD_NAME(USB, usb_1open)
(
    JNIEnv *env, jclass class, jobject device
)
{
    return wrap_usb_dev_handle(env, usb_open(unwrap_usb_device(env, device)));
}


/**
 * int usb_close(USB_Handle)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1close)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    return usb_close(unwrap_usb_dev_handle(env, handle));
}


/**
 * int usb_get_string(USB_Handle handle, int index, int langid, byte[] buffer,
 *     int buflen)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1get_1string)
(
    JNIEnv *env, jclass class, jobject handle, jint index, jint langid,
    jobject buffer
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, buffer);
    return usb_get_string(unwrap_usb_dev_handle(env, handle),
         index, langid, buf, buflen);
}


/**
 * int usb_get_simple_string(USB_Handle handle, int index, ByteBuffer buffer)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1get_1string_1simple)
(
    JNIEnv *env, jclass class, jobject handle, jint index, jobject buffer
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, buffer);
    return usb_get_string_simple(unwrap_usb_dev_handle(env, handle),
         index, buf, buflen);
}


/**
 * int usb_get_descriptor(USB_Dev_Handle handle, int type, int index,
 *    ByteBuffer buffer);
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1get_1descriptor)
(
    JNIEnv *env, jclass class, jobject handle, int type, int index,
    jobject buffer
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, buffer);
    return usb_get_descriptor(unwrap_usb_dev_handle(env, handle),
        type, index, buf, buflen);
}
