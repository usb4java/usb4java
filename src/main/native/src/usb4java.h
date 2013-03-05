/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#ifndef USB4JAVA_H
#define USB4JAVA_H

#include <jni.h>
#include <libusb.h>

#define PACKAGE_DIR "de/ailis/usb4java/libusb"
#define METHOD_NAME(CLASS_NAME, METHOD_NAME) Java_de_ailis_usb4java_libusb_##CLASS_NAME##_##METHOD_NAME

#define SET_POINTER(ENV, PTR, OBJECT) \
{ \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, "pointer", "J"); \
    (*ENV)->SetLongField(ENV, OBJECT, field, (jlong) PTR); \
}

#define WRAP_POINTER(ENV, PTR, CLASS_NAME) \
{ \
    if (!PTR) return NULL; \
    jclass cls = (*ENV)->FindClass(ENV, PACKAGE_DIR"/"CLASS_NAME); \
    if (cls == NULL) return NULL; \
    jmethodID constructor = (*ENV)->GetMethodID(ENV, cls, "<init>", "()V"); \
    if (constructor == NULL) return NULL; \
    jobject object = (*ENV)->NewObject(ENV, cls, constructor); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, "pointer", "J"); \
    (*ENV)->SetLongField(ENV, object, field, (jlong) PTR); \
    return object; \
}

#define UNWRAP_POINTER(ENV, OBJECT, TYPE) \
{ \
    if (!OBJECT) return NULL; \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, "pointer", "J"); \
    return (TYPE) (*ENV)->GetLongField(ENV, OBJECT, field); \
}

#define SET_DATA(ENV, PTR, SIZE, OBJECT) \
{ \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, "data", \
        "Ljava/nio/ByteBuffer;"); \
    jobject buffer = (*ENV)->NewDirectByteBuffer(env, PTR, SIZE); \
    (*ENV)->SetObjectField(ENV, OBJECT, field, buffer); \
}

#define UNWRAP_DATA(ENV, OBJECT, TYPE) \
{ \
    jclass cls = (*ENV)->GetObjectClass(ENV, OBJECT); \
    jfieldID field = (*ENV)->GetFieldID(ENV, cls, "data", \
        "Ljava/nio/ByteBuffer;"); \
    jobject buffer = (*ENV)->GetObjectField(ENV, OBJECT, field); \
    return (TYPE) (*ENV)->GetDirectBufferAddress(ENV, buffer); \
}

#define DIRECT_BUFFER(ENV, VAR, ACTION) \
{ \
    jclass cls = (*ENV)->GetObjectClass(ENV, VAR); \
    jmethodID method = (*ENV)->GetMethodID(ENV, cls, "isDirect", \
        "()Z"); \
    if (!(*ENV)->CallBooleanMethod(ENV, VAR, method)) \
    { \
        illegalArgument(ENV, #VAR" must be a direct buffer"); \
        ACTION; \
    } \
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
    if (getEnvResult == JNI_EDETACHED) (*jvm)->DetachCurrentThread(jvm);

jint illegalArgument(JNIEnv *env, char *message);

#endif
