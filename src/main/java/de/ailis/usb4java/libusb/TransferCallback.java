/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

public interface TransferCallback
{
    void processTransfer(Transfer transfer);
}
