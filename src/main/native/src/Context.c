/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "usb4java.h"
#include "Context.h"

void wrap_context(JNIEnv* env, libusb_context* data, jobject obj)
{
    jobject buffer = (*env)->NewDirectByteBuffer(env, data, 0);
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID field = (*env)->GetFieldID(env, cls, "data",
        "Ljava/nio/ByteBuffer;");
    (*env)->SetObjectField(env, obj, field, buffer);
}

libusb_context* unwrap_context(JNIEnv* env, jobject obj)
{
     if (!obj) return NULL;
     jclass cls = (*env)->GetObjectClass(env, obj);
     jfieldID field = (*env)->GetFieldID(env, cls, "data",
         "Ljava/nio/ByteBuffer;");
     jobject buffer = (*env)->GetObjectField(env, obj, field);
     return (libusb_context *)
         (*env)->GetDirectBufferAddress(env, buffer);
}
