/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Context.h"

void setContext(JNIEnv* env, libusb_context* context, jobject object)
{
    SET_POINTER(env, context, object, "contextPointer");
}

libusb_context* unwrapContext(JNIEnv* env, jobject context)
{
    UNWRAP_POINTER(env, context, libusb_context*, "contextPointer");
}

void resetContext(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "contextPointer");
}
