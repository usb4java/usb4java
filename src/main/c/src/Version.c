/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "Version.h"

jobject wrapVersion(JNIEnv* env, const struct libusb_version* pointer)
{
    WRAP_POINTER(env, pointer, "Version", "versionPointer");
}

const struct libusb_version* unwrapVersion(JNIEnv* env, jobject object)
{
    UNWRAP_POINTER(env, object, struct libusb_version*, "versionPointer");
}

/**
 * int major()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, major)
(
    JNIEnv *env, jobject this
)
{
    return unwrapVersion(env, this)->major;
}

/**
 * int minor()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, minor)
(
    JNIEnv *env, jobject this
)
{
    return unwrapVersion(env, this)->minor;
}

/**
 * int micro()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, micro)
(
    JNIEnv *env, jobject this
)
{
    return unwrapVersion(env, this)->micro;
}

/**
 * int nano()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, nano)
(
    JNIEnv *env, jobject this
)
{
    return unwrapVersion(env, this)->nano;
}

/**
 * String rc()
 */
JNIEXPORT jstring JNICALL METHOD_NAME(Version, rc)
(
    JNIEnv *env, jobject this
)
{
    return (*env)->NewStringUTF(env, unwrapVersion(env, this)->rc);
}


