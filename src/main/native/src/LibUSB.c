/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name LibUSB
 *
 * Native methods for the LibUSB class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include <jni.h>
#include <libusb.h>
#include "usb4java.h"
#include "Version.h"
#include "Context.h"

/**
 * Version getVersion()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, getVersion)
(
    JNIEnv *env, jclass class
)
{
    return wrap_version(env, libusb_get_version());
}

/**
 * int init()
 */
JNIEXPORT jint JNICALL METHOD_NAME(LibUSB, init)
(
    JNIEnv *env, jclass class, jobject context
)
{
    if (!context)
    {
        return wrap_error(env, libusb_init(NULL));
    }
    else
    {
        libusb_context *ctx;
        int result = libusb_init(&ctx);
        wrap_context(env, ctx, context);
        return wrap_error(env, result);
    }
}

/**
 * void exit()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(LibUSB, exit)
(
    JNIEnv *env, jclass class, jobject context
)
{
    libusb_exit(unwrap_context(env, context));
}

/**
 * void setDebug(Context, LogLevel)
 */
JNIEXPORT void JNICALL METHOD_NAME(LibUSB, setDebug)
(
    JNIEnv *env, jclass class, jobject context, jobject logLevel
)
{
    if (!logLevel) return;
    jclass cls = (*env)->GetObjectClass(env, logLevel);
    jfieldID field = (*env)->GetFieldID(env, cls, "level", "I");
    jint level = (*env)->GetObjectField(env, logLevel, field);
    libusb_set_debug(unwrap_context(env, context), level);
}
