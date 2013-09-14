/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * Copyright (C) 2013 Luca Longinotti (l@longi.li)
 * See COPYING file for copying conditions
 */

#include "usb4java.h"

JavaVM *jvm = NULL;

jclass jClassLibUsb = NULL;
jmethodID jMethodTriggerPollfdAdded = NULL;
jmethodID jMethodTriggerPollfdRemoved = NULL;
jmethodID jMethodHotplugCallback = NULL;

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

jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    // Set JVM to the current one.
    jvm = vm;

    // Get the current environment.
    JNIEnv *env;
    jint getEnvResult = (*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6);
    if (getEnvResult != JNI_OK)
    {
        // Send unrecognized version to signal error and deny library load.
        return -1;
    }

    // Find classes and methods and cache them.
    // Persistence is guaranteed by global references.
    jClassLibUsb = (*env)->FindClass(env, PACKAGE_DIR"/LibUsb");
    jClassLibUsb = (*env)->NewGlobalRef(env, jClassLibUsb);

    jMethodTriggerPollfdAdded = (*env)->GetStaticMethodID(env, jClassLibUsb,
        "triggerPollfdAdded", "(Ljava/io/FileDescriptor;IJ)V");
    jMethodTriggerPollfdRemoved = (*env)->GetStaticMethodID(env, jClassLibUsb,
        "triggerPollfdRemoved", "(Ljava/io/FileDescriptor;J)V");
    jMethodHotplugCallback = (*env)->GetStaticMethodID(env, jClassLibUsb,
        "hotplugCallback", "(L"PACKAGE_DIR"/Context;L"PACKAGE_DIR"/Device;IJ)I");

    return JNI_VERSION_1_6;
}

void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
    // Get the current environment.
    JNIEnv *env;
    jint getEnvResult = (*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6);
    if (getEnvResult != JNI_OK)
    {
        return;
    }

    // Cleanup all global references.
    (*env)->DeleteGlobalRef(env, jClassLibUsb);
}
