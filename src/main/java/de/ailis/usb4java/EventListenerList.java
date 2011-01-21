/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;


/**
 * Manages event listeners in a list.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @param <T>
 *            The event listener class.
 */

public class EventListenerList<T extends EventListener> implements Serializable
{
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The list of listeners. */
    protected final List<T> listeners = Collections
            .synchronizedList(new ArrayList<T>());


    /**
     * Adds a listener to the list.
     *
     * @param listener
     *            The listener to add. Must not be null.
     * @throws IllegalStateException
     *             If listener can't be added because it is already registered.
     */

    public void addListener(final T listener)
    {
        if (listener == null)
            throw new IllegalArgumentException("listener must not be null");
        if (this.listeners.contains(listener))
            throw new IllegalStateException("listener is already registered");
        this.listeners.add(listener);
    }


    /**
     * Removes the specified listener from the list.
     *
     * @param listener
     *            The listener to remove.
     * @throws IllegalStateException
     *             If listener can't be removed because it is not registered.
     */

    public void removeListener(final T listener)
    {
        if (listener == null)
            throw new IllegalArgumentException("listener must not be null");
        if (!this.listeners.remove(listener))
            throw new IllegalStateException("listener is not registered");
    }
}
