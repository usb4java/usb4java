/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "usb4java.h"

jint illegalArgument(JNIEnv *env, char *message)
{
    jclass cls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    return (*env)->ThrowNew(env, cls, message);
}

jint illegalState(JNIEnv *env, char *message)
{
    jclass cls = (*env)->FindClass(env, "java/lang/IllegalStateException");
    return (*env)->ThrowNew(env, cls, message);
}

