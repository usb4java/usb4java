/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.mocks;

import java.io.FileDescriptor;

import org.usb4java.PollfdListener;

/**
 * A mocked pollfd listener implementation.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class PollfdListenerMock implements PollfdListener
{
    /** The file descriptor reported by the added event. */
    public FileDescriptor addedFd;
    
    /** The file descriptor reported by the removed event. */
    public FileDescriptor removedFd;
    
    /** The events number reported by the added event. */
    public int addedEvents;
    
    /** The user data reported by the added event. */
    public Object addedUserData;
    
    /** The user data reported by the removed event. */
    public Object removedUserData;
    
    @Override
    public void pollfdAdded(FileDescriptor fd, int events, Object userData)
    {
        this.addedEvents = events;
        this.addedFd = fd;
        this.addedUserData = userData;
    }

    @Override
    public void pollfdRemoved(FileDescriptor fd, Object userData)
    {
        this.removedFd = fd;
        this.removedUserData = userData;
    }

    /**
     * Resets the mock object state. 
     */
    public void reset()
    {
        this.addedEvents = 0;
        this.addedFd = null;
        this.addedUserData = null;
        this.removedFd = null;
        this.removedUserData = null;
    }
}
