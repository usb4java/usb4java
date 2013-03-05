/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import java.io.FileDescriptor;

/**
 * Listener interface for pollfd notifications.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public interface PollfdListener
{
    /**
     * Callback function, invoked when a new file descriptor should be added to
     * the set of file descriptors monitored for events.
     * 
     * @param fd
     *            the new file descriptor.
     * @param events
     *            events to monitor for.
     * @param userData
     *            User data pointer specified in
     *            {@link LibUSB#setPollfdNotifiers(Context, PollfdListener, Object)}
     *            call.
     */
    void pollfdAdded(FileDescriptor fd, int events, Object userData);

    /**
     * Callback function, invoked when a file descriptor should be removed from
     * the set of file descriptors being monitored for events.
     * 
     * After returning from this callback, do not use that file descriptor
     * again.
     * 
     * @param fd
     *            The file descriptor to stop monitoring.
     * @param userData
     *            User data pointer specified in
     *            {@link LibUSB#setPollfdNotifiers(Context, PollfdListener, Object)}
     *            call
     * 
     */
    void pollfdRemoved(FileDescriptor fd, Object userData);
}
