/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_H
#define USB4JAVA_H

#include <jni.h>
#include <libusb.h>
#include "config.h"

#define PACKAGE_DIR "de/ailis/usb4java/libusb"
#define METHOD_NAME(CLASS_NAME, METHOD_NAME) Java_de_ailis_usb4java_libusb_##CLASS_NAME##_##METHOD_NAME

#if SIZEOF_VOID_P == 4
#  define jptr jint
#elif SIZEOF_VOID_P == 8
#  define jptr jlong
#endif

#define SET_POINTER(ENV, PTR, OBJECT, FIELD) \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, FIELD, "J"); \
    (*ENV)->SetLongField(ENV, OBJECT, field, (jptr) PTR);

#define RESET_POINTER(ENV, OBJECT, FIELD) \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, FIELD, "J"); \
    (*ENV)->SetLongField(ENV, OBJECT, field, 0);

#define WRAP_POINTER(ENV, PTR, CLASS_NAME, FIELD) \
    if (!PTR) return NULL; \
    jclass cls = (*ENV)->FindClass(ENV, PACKAGE_DIR"/"CLASS_NAME); \
    if (cls == NULL) return NULL; \
    jmethodID constructor = (*ENV)->GetMethodID(ENV, cls, "<init>", "()V"); \
    if (constructor == NULL) return NULL; \
    jobject object = (*ENV)->NewObject(ENV, cls, constructor); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, FIELD, "J"); \
    (*ENV)->SetLongField(ENV, object, field, (jptr) PTR); \
    return object;

#define UNWRAP_POINTER(ENV, OBJECT, TYPE, FIELD) \
    if (!OBJECT) return NULL; \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, FIELD, "J"); \
    jptr ptr = (jptr) (*ENV)->GetLongField(ENV, OBJECT, field); \
    if (!ptr) illegalState(ENV, FIELD" is not initialized"); \
    return (TYPE) ptr;

#define SET_DATA(ENV, PTR, SIZE, OBJECT, FIELD) \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, FIELD, \
        "Ljava/nio/ByteBuffer;"); \
    jobject buffer = (*ENV)->NewDirectByteBuffer(env, PTR, SIZE); \
    (*ENV)->SetObjectField(ENV, OBJECT, field, buffer);

#define UNWRAP_DATA(ENV, OBJECT, TYPE, FIELD) \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, FIELD, \
        "Ljava/nio/ByteBuffer;"); \
    jobject buffer = (*ENV)->GetObjectField(ENV, OBJECT, field); \
    return (TYPE) (*ENV)->GetDirectBufferAddress(ENV, buffer);

// GetDirectBufferAddress returns NULL if called on a non-direct buffer.
#define DIRECT_BUFFER(ENV, VAR, BUFFER, ACTION) \
    unsigned char *BUFFER = (*ENV)->GetDirectBufferAddress(ENV, VAR); \
	if (!BUFFER) \
	{ \
	    illegalArgument(ENV, #VAR" must be a direct buffer"); \
	    ACTION; \
	}

#define NOT_NULL(ENV, VAR, ACTION) \
    if (!VAR) \
    { \
        illegalArgument(ENV, #VAR" must not be null"); \
        ACTION; \
    }

#define THREAD_BEGIN(ENV) \
    JNIEnv *ENV; \
    jint getEnvResult = (*jvm)->GetEnv(jvm, (void **) &ENV, JNI_VERSION_1_4); \
    if (getEnvResult == JNI_EDETACHED) \
        (*jvm)->AttachCurrentThread(jvm, (void**) &ENV, NULL);

#define THREAD_END \
    if (getEnvResult == JNI_EDETACHED) \
        (*jvm)->DetachCurrentThread(jvm);

jint illegalArgument(JNIEnv *env, char *message);
jint illegalState(JNIEnv *env, char *message);

#endif
