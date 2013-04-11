/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Context.h"

void setContext(JNIEnv* env, libusb_context* context, jobject object)
{
    SET_POINTER(env, context, object);
}

libusb_context* unwrapContext(JNIEnv* env, jobject context)
{
    UNWRAP_POINTER(env, context, libusb_context*);
}
