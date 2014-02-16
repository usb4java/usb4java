/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

/**
 * Asynchronous transfer callback.
 * 
 * When submitting asynchronous transfers, you pass a callback of this type via
 * the callback member of the {@link Transfer} structure.
 * 
 * @author Luca Longinotti (l@longi.li)
 */
public interface TransferCallback
{
    /**
     * Processes a transfer notification.
     * 
     * libusb will call this function later, when the transfer has completed or
     * failed.
     * 
     * @param transfer
     *            The {@link Transfer} the callback is being notified about.
     */
    void processTransfer(Transfer transfer);
}
