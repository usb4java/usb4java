/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include "usb4java.h"
#include "Error.h"

jobject wrap_error(JNIEnv* env, const int code)
{
    jclass cls = (*env)->FindClass(env, PACKAGE_DIR"/Error");
    jmethodID method = (*env)->GetStaticMethodID(env, cls, "valueOf",
        "(I)L"PACKAGE_DIR"/Error;");
    if (!method) return NULL;
    return (*env)->CallStaticObjectMethod(env, cls, method, code);
}
