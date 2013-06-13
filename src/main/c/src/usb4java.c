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

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wcast-qual"
jobject NewDirectReadOnlyByteBuffer(JNIEnv *env, const void *mem,
    int mem_length)
{
    jobject buffer = (*env)->NewDirectByteBuffer(env, (void *) mem, mem_length);

    // Get a read-only buffer from this buffer.
    jclass cls = (*env)->GetObjectClass(env, buffer);
    jmethodID method = (*env)->GetMethodID(env, cls, "asReadOnlyBuffer",
        "()Ljava/nio/ByteBuffer;");
    return (*env)->CallObjectMethod(env, buffer, method);
}
#pragma GCC diagnostic pop

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    jvm = vm;
    return JNI_VERSION_1_4;
}
