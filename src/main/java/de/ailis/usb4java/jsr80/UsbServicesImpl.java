/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesListener;


/**
 * usb4java implementation of JSR-80 UsbServices
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbServicesImpl implements UsbServices
{
    /** The implementation description. */
    private static final String IMP_DESCRIPTION = "usb4java JSR-80 implementation";

    /** The implementation version. */
    private static final String IMP_VERSION = "0.1.12-1";

    /** The API version. */
    private static final String API_VERSION = "1.0.2";

    /** The USB services listeners. */
    private final UsbServicesListenerList listeners = new UsbServicesListenerList();


    /**
     * @see UsbServices#getRootUsbHub()
     */

    @Override
    public final UsbHub getRootUsbHub() throws UsbException, SecurityException
    {
        // TODO Implement me
        throw new UnsupportedOperationException();
    }


    /**
     * @see UsbServices#addUsbServicesListener(UsbServicesListener)
     */

    @Override
    public final void addUsbServicesListener(final UsbServicesListener listener)
    {
        this.listeners.add(listener);
    }


    /**
     * @see UsbServices#removeUsbServicesListener(UsbServicesListener)
     */

    @Override
    public final void removeUsbServicesListener(final UsbServicesListener listener)
    {
        this.listeners.remove(listener);
    }


    /**
     * @see UsbServices#getApiVersion()
     */

    @Override
    public final String getApiVersion()
    {
        return API_VERSION;
    }


    /**
     * @see UsbServices#getImpVersion()
     */

    @Override
    public final String getImpVersion()
    {
        return IMP_VERSION;
    }


    /**
     * @see UsbServices#getImpDescription()
     */

    @Override
    public final String getImpDescription()
    {
        return IMP_DESCRIPTION;
    }
}
