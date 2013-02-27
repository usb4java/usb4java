/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

/**
 * @name Version
 *
 * Native methods for the Version class.
 *
 * @author Klaus Reimer <k@ailis.de>
 */

#include "usb4java.h"
#include "Version.h"

/**
 * Wraps the specified native data into a Java object and returns it.
 *
 * @param env
 *            The JNI environment.
 * @param version
 *            The native data to wrap.
 * @return The wrapper object.
 */
jobject wrap_version(JNIEnv* env, const struct libusb_version* data)
{
    if (!data) return NULL;
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/Version");
    if (cls == NULL) return NULL;
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>",
        "(Ljava/nio/ByteBuffer;)V");
    if (constructor == NULL) return NULL;
    jobject buffer = (*env)->NewDirectByteBuffer(env, data,
        sizeof(struct libusb_version));
    return (*env)->NewObject(env, cls, constructor, buffer);
}

/**
 * Returns the wrapped native data from the specified wrapper object.
 * 
 * @param env
 *            The JNI environment.
 * @param obj
 *            The wrapper object.
 * @return The wrapped object.
 */
const struct libusb_version* unwrap_version(JNIEnv* env, jobject obj)
{
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "data",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (struct libusb_version *) 
         (*env)->GetDirectBufferAddress(env, buffer);
}

/**
 * int major()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, major)
(
    JNIEnv *env, jobject this
)
{
    return unwrap_version(env, this)->major;
}

/**
 * int minor()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, minor)
(
    JNIEnv *env, jobject this
)
{
    return unwrap_version(env, this)->minor;
}

/**
 * int micro()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, micro)
(
    JNIEnv *env, jobject this
)
{
    return unwrap_version(env, this)->micro;
}

/**
 * string rc()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Version, rc)
(
    JNIEnv *env, jobject this
)
{
    return (*env)->NewStringUTF(env, unwrap_version(env, this)->rc);
}


