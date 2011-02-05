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
 * USB_Dev_Handle usb_open(USB_Device)
 */

JNIEXPORT jobject JNICALL METHOD_NAME(USB, usb_1open)
(
    JNIEnv *env, jclass class, jobject device
)
{
    return wrap_usb_dev_handle(env, usb_open(unwrap_usb_device(env, device)));
}


/**
 * int usb_close(USB_Dev_Handle)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1close)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    return usb_close(unwrap_usb_dev_handle(env, handle));
}


/**
 * int usb_set_configuration(USB_Dev_Handle handle, int configuration)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1set_1configuration)
(
    JNIEnv *env, jclass class, jobject handle, jint configuration
)
{
    return usb_set_configuration(unwrap_usb_dev_handle(env, handle),
        configuration);
}


/**
 * int usb_set_altinterface(USB_Dev_Handle handle, int alternate)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1set_1altinterface)
(
    JNIEnv *env, jclass class, jobject handle, jint alternate
)
{
    return usb_set_altinterface(unwrap_usb_dev_handle(env, handle),
            alternate);
}


/**
 * int usb_clear_halt(USB_Dev_Handle handle, int ep)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1clear_1halt)
(
    JNIEnv *env, jclass class, jobject handle, jint ep
)
{
    return usb_clear_halt(unwrap_usb_dev_handle(env, handle), ep);
}


/**
 * int usb_reset(USB_Dev_Handle handle)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1reset)
(
    JNIEnv *env, jclass class, jobject handle
)
{
    return usb_reset(unwrap_usb_dev_handle(env, handle));
}


/**
 * int usb_claim_interface(USB_Dev_Handle handle, int iface)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1claim_1interface)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    return usb_claim_interface(unwrap_usb_dev_handle(env, handle), iface);
}


/**
 * int usb_release_interface(USB_Dev_Handle handle, int iface)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1release_1interface)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    return usb_release_interface(unwrap_usb_dev_handle(env, handle), iface);
}


/**
 * int usb_control_msg(USB_Device_Handle handle, int requesttype, int request,
 *     int value, int index, ByteBuffer bytes, int timeout)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1control_1msg)
(
    JNIEnv *env, jclass class, jobject handle, jint requesttype, jint request,
    jint value, jint index, jobject bytes, int timeout
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, bytes);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, bytes);
    return usb_control_msg(unwrap_usb_dev_handle(env, handle), requesttype,
        request, value, index, buf, buflen, timeout);
}


/**
 * int usb_get_string(USB_Dev_Handle handle, int index, int langid, byte[] buffer,
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
 * int usb_get_simple_string(USB_Dev_Handle handle, int index, ByteBuffer buffer)
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
 *    ByteBuffer buffer)
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


/**
 * int usb_get_descriptor_by_endpoint(USB_Dev_Handle handle, int ep, int type,
 *     int index, ByteBuffer buffer)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1get_1descriptor_1by_1endpoint)
(
    JNIEnv *env, jclass class, jobject handle, int ep, int type, int index,
    jobject buffer
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, buffer);
    return usb_get_descriptor_by_endpoint(unwrap_usb_dev_handle(env, handle),
        ep, type, index, buf, buflen);
}


/**
 * int usb_bulk_write(USB_Dev_Handle handle, int ep, ByteBuffer bytes,
 *     int timeout)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1bulk_1write)
(
    JNIEnv *env, jclass class, jobject handle, jint ep, jobject bytes,
    int timeout
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, bytes);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, bytes);
    return usb_bulk_write(unwrap_usb_dev_handle(env, handle),
        ep, buf, buflen, timeout);
}


/**
 * int usb_bulk_read(USB_Dev_Handle handle, int ep, ByteBuffer bytes,
 *     int timeout)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1bulk_1read)
(
    JNIEnv *env, jclass class, jobject handle, jint ep, jobject bytes,
    int timeout
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, bytes);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, bytes);
    return usb_bulk_read(unwrap_usb_dev_handle(env, handle),
        ep, buf, buflen, timeout);
}


/**
 * int usb_interrupt_write(USB_Dev_Handle handle, int ep, ByteBuffer bytes,
 *     int timeout)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1interrupt_1write)
(
    JNIEnv *env, jclass class, jobject handle, jint ep, jobject bytes,
    int timeout
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, bytes);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, bytes);
    return usb_interrupt_write(unwrap_usb_dev_handle(env, handle),
        ep, buf, buflen, timeout);
}


/**
 * int usb_interrupt_read(USB_Dev_Handle handle, int ep, ByteBuffer bytes,
 *     int timeout)
 */

JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1interrupt_1read)
(
    JNIEnv *env, jclass class, jobject handle, jint ep, jobject bytes,
    int timeout
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, bytes);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, bytes);
    return usb_interrupt_read(unwrap_usb_dev_handle(env, handle),
        ep, buf, buflen, timeout);
}


/**
 * string usb_strerror()
 */

JNIEXPORT jstring JNICALL METHOD_NAME(USB, usb_1strerror)
(
    JNIEnv *env, jclass class
)
{
    return (*env)->NewStringUTF(env, usb_strerror());
}


/**
 * int usb_detach_kernel_driver_np(USB_Dev_Handle handle, int iface)
 */

#ifdef LIBUSB_HAS_DETACH_KERNEL_DRIVER_NP
JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1detach_1kernel_1driver_1np)
(
    JNIEnv *env, jclass class, jobject handle, jint iface
)
{
    return usb_detach_kernel_driver_np(unwrap_usb_dev_handle(env, handle), iface);
}
#endif


/**
 * int usb_get_driver_np(USB_Dev_Handle handle, int iface, ByteBuffer buffer)
 */

#ifdef LIBUSB_HAS_GET_DRIVER_NP
JNIEXPORT jint JNICALL METHOD_NAME(USB, usb_1get_1driver_1np)
(
    JNIEnv *env, jclass class, jobject handle, jint iface, jobject buffer
)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);
    if (!buf) return -1;
    jlong buflen = (*env)->GetDirectBufferCapacity(env, buffer);
    return usb_get_driver_np(unwrap_usb_dev_handle(env, handle),
         iface, buf, buflen);
}
#endif


/**
 * boolean libusb_has_detach_kernel_driver_np()
 */

JNIEXPORT jboolean JNICALL METHOD_NAME(USB,
    libusb_1has_1detach_1kernel_1driver_1np)
(
    JNIEnv *env, jclass class
)
{
    #ifdef LIBUSB_HAS_DETACH_KERNEL_DRIVER_NP
    return 1;
    #else
    return 0;
    #endif
}


/**
 * boolean libusb_has_get_driver_np()
 */

JNIEXPORT jboolean JNICALL METHOD_NAME(USB, libusb_1has_1get_1driver_1np)
(
    JNIEnv *env, jclass class
)
{
    #ifdef LIBUSB_HAS_GET_DRIVER_NP
    return 1;
    #else
    return 0;
    #endif
}
