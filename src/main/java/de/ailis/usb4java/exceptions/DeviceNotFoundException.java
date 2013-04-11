/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.exceptions;

import de.ailis.usb4java.topology.DeviceId;

/**
 * Thrown when a USB device was not found by id.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceNotFoundException extends RuntimeException
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The device id. */
    private final DeviceId id;

    /**
     * Constructor.
     *
     * @param id
     *            The ID of the device which was not found.
     */
    public DeviceNotFoundException(final DeviceId id)
    {
        super("USB Device " + id + " not found");
        this.id = id;
    }

    /**
     * Returns the device id.
     *
     * @return The device id.
     */
    public DeviceId getId()
    {
        return this.id;
    }
}
