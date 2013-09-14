/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Context.h"

void setContext(JNIEnv* env, const libusb_context* context, jobject object)
{
    SET_POINTER(env, context, object, "contextPointer");
}

jobject wrapContext(JNIEnv* env, const libusb_context* context)
{
    WRAP_POINTER(env, context, "Context", "contextPointer");
}

libusb_context* unwrapContext(JNIEnv* env, jobject context)
{
    UNWRAP_POINTER(env, context, libusb_context*, "contextPointer");
}

void resetContext(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "contextPointer");
}
