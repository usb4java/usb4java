/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

/**
 * Hotplug callback.
 * 
 * When requesting hotplug event notifications, you pass a callback of this
 * type.
 * 
 * @author Luca Longinotti (l@longi.li)
 */
public interface HotplugCallback
{
    /**
     * Process a hotplug event.
     * 
     * This callback may be called by an internal event thread and as such it 
     * is recommended the callback do minimal processing before returning.
     * 
     * libusb will call this function later, when a matching event had happened
     * on a matching device.
     * 
     * It is safe to call either
     * {@link LibUsb#hotplugRegisterCallback(Context, int, int, int, int, int, 
     * HotplugCallback, Object, HotplugCallbackHandle)} or
     * {@link LibUsb#hotplugDeregisterCallback(Context, HotplugCallbackHandle)}
     * from within a callback.
     * 
     * @param context
     *            Context of this notification.
     * @param device
     *            Device this event occurred on.
     * @param event
     *            Event that occurred.
     * @param userData
     *            user data provided when this callback was registered
     * @return Whether this callback is finished processing events. Returning 1
     *         will cause this callback to be deregistered.
     */
    int processEvent(Context context, Device device, int event, 
        Object userData);
}
