/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java;

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
     *            User data pointer.
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
     *            User data pointer.
     */
    void pollfdRemoved(FileDescriptor fd, Object userData);
}
