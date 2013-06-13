/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include <stdlib.h>
#include "Transfer.h"
#include "DeviceHandle.h"
#include "IsoPacketDescriptor.h"

static void LIBUSB_CALL cleanupCallback(struct libusb_transfer *transfer);
static void LIBUSB_CALL transferCallback(struct libusb_transfer *transfer);

jobject wrapTransfer(JNIEnv* env, const struct libusb_transfer* transfer)
{
    WRAP_POINTER(env, transfer, "Transfer", "transferPointer");
}

struct libusb_transfer* unwrapTransfer(JNIEnv* env, jobject obj)
{
    UNWRAP_POINTER(env, obj, struct libusb_transfer*, "transferPointer");
}

void resetTransfer(JNIEnv* env, jobject obj)
{
    RESET_POINTER(env, obj, "transferPointer");

    // We already have the class from the previous call.
    // Reset transferBuffer field to NULL too.
    field = (*env)->GetFieldID(env, cls, "transferBuffer",
        "Ljava/nio/ByteBuffer;");
    (*env)->SetObjectField(env, obj, field, NULL);
}

/**
 * void setDevHandle(DeviceHandle)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setDevHandle)
(
    JNIEnv *env, jobject this, jobject handle
)
{
    libusb_device_handle *dev_handle = unwrapDeviceHandle(env, handle);
    if (!dev_handle && handle) return;
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->dev_handle = dev_handle;
}

/**
 * DeviceHandle devHandle()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(Transfer, devHandle)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return NULL;

    return wrapDeviceHandle(env, transfer->dev_handle);
}

/**
 * void setFlags(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setFlags)
(
    JNIEnv *env, jobject this, jint flags
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->flags = (uint8_t) flags;
}

/**
 * byte flags()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(Transfer, flags)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return (jbyte) transfer->flags;
}

/**
 * void setEndpoint(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setEndpoint)
(
    JNIEnv *env, jobject this, jint endpoint
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->endpoint = (unsigned char) endpoint;
}

/**
 * byte endpoint()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(Transfer, endpoint)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return (jbyte) transfer->endpoint;
}

/**
 * void setType(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setType)
(
    JNIEnv *env, jobject this, jint type
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->type = (unsigned char) type;
}

/**
 * byte type()
 */
JNIEXPORT jbyte JNICALL METHOD_NAME(Transfer, type)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return (jbyte) transfer->type;
}

/**
 * void setTimeout(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setTimeout)
(
    JNIEnv *env, jobject this, jint timeout
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->timeout = (unsigned int) timeout;
}

/**
 * int timeout()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Transfer, timeout)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return (jint) transfer->timeout;
}

/**
 * int status()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Transfer, status)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return transfer->status;
}

/**
 * void setLengthNative(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setLengthNative)
(
    JNIEnv *env, jobject this, jint length
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->length = length;
}

/**
 * int length()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Transfer, length)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return transfer->length;
}

/**
 * int actualLength()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Transfer, getActualLength)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return transfer->actual_length;
}

void cleanupGlobalReferences(JNIEnv *env, jobject obj)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, obj);
    if (!transfer) return;

    struct transfer_data *transferData =
        ((struct transfer_data *) transfer->user_data);

    // Cleanup all global references, if any currently exist.
    if (transferData->callbackObject != NULL)
    {
        (*env)->DeleteGlobalRef(env, transferData->callbackObject);
    }

    if (transferData->callbackUserDataObject != NULL)
    {
        (*env)->DeleteGlobalRef(env, transferData->callbackUserDataObject);
    }

    if (transferData->transferObject != NULL)
    {
        (*env)->DeleteGlobalRef(env, transferData->transferObject);
    }
}

void cleanupCallbackEnable(JNIEnv *env, jobject obj)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, obj);
    if (!transfer) return;

    struct transfer_data *transferData =
        ((struct transfer_data *) transfer->user_data);

    transferData->transferObject = (*env)->NewGlobalRef(env, obj);

    transfer->callback = &cleanupCallback;

    transferData->callbackObject = NULL;
    transferData->callbackObjectMethod = 0;
}

static void LIBUSB_CALL cleanupCallback(struct libusb_transfer *transfer)
{
    THREAD_BEGIN(env)

    struct transfer_data *transferData =
        ((struct transfer_data *) transfer->user_data);

    // The saved reference to the Java Transfer object.
    jobject jTransfer = transferData->transferObject;

    // Cleanup Java Transfer object too, if requested.
    if (transfer->flags & LIBUSB_TRANSFER_FREE_TRANSFER)
    {
        cleanupGlobalReferences(env, jTransfer);
        resetTransfer(env, jTransfer);
        free(transferData);
    }

    THREAD_END
}

static void LIBUSB_CALL transferCallback(struct libusb_transfer *transfer)
{
    THREAD_BEGIN(env)

    struct transfer_data *transferData =
        ((struct transfer_data *) transfer->user_data);

    // The saved references to the Java TransferCallback object.
    jobject jCallback = transferData->callbackObject;
    jmethodID jCallbackMethod = transferData->callbackObjectMethod;

    // The saved reference to the Java Transfer object.
    jobject jTransfer = transferData->transferObject;

    // Call back into Java.
    (*env)->CallVoidMethod(env, jCallback, jCallbackMethod, jTransfer);

    // Cleanup Java Transfer object too, if requested.
    if (transfer->flags & LIBUSB_TRANSFER_FREE_TRANSFER)
    {
        cleanupGlobalReferences(env, jTransfer);
        resetTransfer(env, jTransfer);
        free(transferData);
    }

    THREAD_END
}

/**
 * void setCallback(TransferCallback)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setCallback)
(
    JNIEnv *env, jobject this, jobject callback
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    struct transfer_data *transferData =
        ((struct transfer_data *) transfer->user_data);

    if (transferData->transferObject != NULL)
    {
        (*env)->DeleteGlobalRef(env, transferData->transferObject);
    }

    if (transferData->callbackObject != NULL)
    {
        (*env)->DeleteGlobalRef(env, transferData->callbackObject);
    }

    if (callback != NULL)
    {
        transferData->transferObject = (*env)->NewGlobalRef(env, this);

        transfer->callback = &transferCallback;

        jclass cls = (*env)->GetObjectClass(env, callback);
        jmethodID method = (*env)->GetMethodID(env, cls, "processTransfer",
            "(L"PACKAGE_DIR"/Transfer;)V");

        transferData->callbackObject = (*env)->NewGlobalRef(env, callback);
        transferData->callbackObjectMethod = method;
    }
    else
    {
        cleanupCallbackEnable(env, this);
    }
}

/**
 * TransferCallback callback()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(Transfer, callback)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return NULL;

    return ((struct transfer_data *) transfer->user_data)->callbackObject;
}

/**
 * void setUserData(Object)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setUserData)
(
    JNIEnv *env, jobject this, jobject userData
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    struct transfer_data *transferData =
        ((struct transfer_data *) transfer->user_data);

    if (transferData->callbackUserDataObject != NULL)
    {
        (*env)->DeleteGlobalRef(env, transferData->callbackUserDataObject);
    }

    if (userData != NULL)
    {
        transferData->callbackUserDataObject = (*env)->NewGlobalRef(env,
            userData);
    }
    else
    {
        transferData->callbackUserDataObject = NULL;
    }
}

/**
 * Object userData()
 */
JNIEXPORT jobject JNICALL METHOD_NAME(Transfer, userData)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return NULL;

    return ((struct transfer_data *) transfer->user_data)->callbackUserDataObject;
}

/**
 * void setBufferNative(ByteBuffer)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setBufferNative)
(
    JNIEnv *env, jobject this, jobject buffer
)
{
    unsigned char *buffer_ptr = NULL;
    if (buffer)
    {
        DIRECT_BUFFER(env, buffer, buffer_tmp, return);
        buffer_ptr = buffer_tmp;
    }

    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    transfer->buffer = buffer_ptr;
}

/**
 * void setNumIsoPackets(int)
 */
JNIEXPORT void JNICALL METHOD_NAME(Transfer, setNumIsoPackets)
(
    JNIEnv *env, jobject this, jint numIsoPackets
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return;

    // Check that calls to setNumIsoPackets() never set a number exceeding
    // the maximum, which was originally set at allocTransfer() time.
    if (((struct transfer_data *) transfer->user_data)->maxNumIsoPackets
        < (size_t) numIsoPackets)
    {
        illegalArgument(env,
            "numIsoPackets exceeds maximum allowed number set with allocTransfer()");
        return;
    }

    transfer->num_iso_packets = numIsoPackets;
}

/**
 * int numIsoPackets()
 */
JNIEXPORT jint JNICALL METHOD_NAME(Transfer, numIsoPackets)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return 0;

    return transfer->num_iso_packets;
}

/**
 * IsoPacketDescriptor[] isoPacketDesc()
 */
JNIEXPORT jobjectArray JNICALL METHOD_NAME(Transfer, isoPacketDesc)
(
    JNIEnv *env, jobject this
)
{
    struct libusb_transfer *transfer = unwrapTransfer(env, this);
    if (!transfer) return NULL;

    return wrapIsoPacketDescriptors(env, transfer->num_iso_packets,
        transfer->iso_packet_desc);
}
