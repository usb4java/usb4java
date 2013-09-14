/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

public interface HotplugCallback
{
    int processEvent(Context context, Device device, int event, Object userData);
}
