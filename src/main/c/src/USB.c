/*
 * $Id$
 * Copyright (C) 2011 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name USB
 *
 * Native methods for the USB class.
 *
 * @author Klaus Reimer <k@ailis.de>
 * @version 0.1
 */

#include <jni.h>
#include <usb.h>
#include "USBBus.h"
#include "USBDevice.h"
#include "USBDevHandle.h"


/**
 * Initialize libusb.
 *
 * Just like the name implies, usb_init sets up some internal structures. 
 * usb_init must be called before any other libusb functions.
 */

JNIEXPORT void JNICALL Java_de_ailis_usb4java_jni_USB_usb_1init(
    JNIEnv *env, jobject jobj)
{
    usb_init(); 
}


/**
 * Finds all USB busses on system.
 *
 * usb_find_busses will find all of the busses on the system.  Returns the
 * number of changes since previous call to this function (total of new
 * busses and busses removed).
 *
 * @return The number of of changes since previous call.
 */

JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USB_usb_1find_1busses(
    JNIEnv *env, jobject jobj)
{
    return usb_find_busses();
}


/**
 * Find all devices on all USB devices.
 *
 * usb_find_devices will find all of the devices on each bus. This should be
 * called after usb_find_busses.  Returns the number of changes since the
 * previous call to this function (total of new device and devices removed).
 *
 * @return The number of changes since previous call.
 */

JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USB_usb_1find_1devices(
    JNIEnv *env, jobject jobj)
{
    return usb_find_devices();
}


/**
 * Return the list of USB busses found
 *
 * usb_get_busses simply returns the value of the global variable
 * usb_busses.  This was implemented for those languages that support C
 * calling convention and can use shared libraries, but don't support C
 * global variables (like Delphi).
 *
 * @return The list of USB busses found.
 */
 
JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USB_usb_1get_1busses(
    JNIEnv *env, jclass jcls)
{
    return wrap_usb_bus(env, usb_get_busses());
}


/**
 * Opens a USB device.
 *
 * usb_open is to be used to open up a device for use. usb_open must be
 * called before attempting to perform any operations to the device. Returns
 * a handle used in future communication with the device.
 *
 * @param device
 *            The USB device.
 * @return The USB device handle.
 */

JNIEXPORT jobject JNICALL Java_de_ailis_usb4java_jni_USB_usb_1open(
    JNIEnv *env, jclass jcls, jobject device)
{
    return wrap_usb_dev_handle(env, usb_open(unwrap_usb_device(env, device)));
}


/**
 * Closes a USB device.
 *
 * usb_close closes a device opened with usb_open. No further operations may
 * be performed on the handle after usb_close is called. Returns 0 on
 * success or < 0 on error.
 *
 * @param handle
 *            The USB device handle.
 * @return 0 on success or < 0 on error.
 */

JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USB_usb_1close(
    JNIEnv *env, jclass jcls, jobject handle)
{
    return usb_close(unwrap_usb_dev_handle(env, handle));
}


/**
 * Retrieves a string descriptor from a device using the first language.
 *
 * usb_get_string_simple is a wrapper around usb_get_string that retrieves
 * the string description specified by index in the first language for the
 * descriptor and converts it into C style ASCII. Returns number of bytes
 * returned in buf or < 0 on error.

 * @param handle
 *            The USB device handle.
 * @param index
 *            The string description index.
 * @param langid
 *            The language id.
 * @param buffer
 *            The buffer to write the string to.
 * @param buflen
 *            The maximum number of bytes to read.
 * @return The number of bytes read or < 0 on error.
 */

JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USB_usb_1get_1string(
    JNIEnv *env, jclass cls, jobject handle, jint index, jint langid,
    jbyteArray buffer, jint buflen)
{
    char *buf = (char*) malloc(buflen);
    int result = usb_get_string(unwrap_usb_dev_handle(env, handle),
        index, langid, buf, buflen);
    if (result >= 0)
    {
        (*env)->SetByteArrayRegion(env, buffer, 0, result,
            (const jbyte *) buf);
    }
    free(buf);
    return result;
}


/**
 * Retrieves a string descriptor from a device using the first language.
 *
 * usb_get_string_simple is a wrapper around usb_get_string that retrieves
 * the string description specified by index in the first language for the
 * descriptor and converts it into C style ASCII. Returns number of bytes
 * returned in buf or < 0 on error.
 *
 * @param handle
 *            The USB device handle.
 * @param index
 *            The string description index.
 * @param buffer
 *            The buffer to write the string to.
 * @param buflen
 *            The maximum number of bytes to read.
 * @return The number of bytes read or < 0 on error.
 */

JNIEXPORT jint JNICALL Java_de_ailis_usb4java_jni_USB_usb_1get_1string_1simple(
    JNIEnv *env, jclass cls, jobject handle, jint index, jbyteArray buffer,
    jint buflen)
{
    char *buf = (char*) malloc(buflen);
    int result = usb_get_string_simple(unwrap_usb_dev_handle(env, handle),
        index, buf, buflen);
    if (result >= 0)
    {
        (*env)->SetByteArrayRegion(env, buffer, 0, result,
            (const jbyte *) buf);
    }
    free(buf);
    return result;
}
