/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "usb4java.h"

JavaVM *jvm = NULL;

jint illegalArgument(JNIEnv *env, const char *message)
{
    jclass cls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    return (*env)->ThrowNew(env, cls, message);
}

jint illegalState(JNIEnv *env, const char *message)
{
    jclass cls = (*env)->FindClass(env, "java/lang/IllegalStateException");
    return (*env)->ThrowNew(env, cls, message);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    jvm = vm;
    return JNI_VERSION_1_4;
}
