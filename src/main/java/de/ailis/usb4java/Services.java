/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import static de.ailis.usb4java.jni.USB.usb_init;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import de.ailis.usb4java.support.Config;
import de.ailis.usb4java.support.UsbServicesListenerList;
import de.ailis.usb4java.topology.UsbDeviceScanner;
import de.ailis.usb4java.topology.VirtualRootHub;

/**
 * usb4java implementation of JSR-80 UsbServices.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Services implements UsbServices
{
    /** The implementation description. */
    private static final String IMP_DESCRIPTION =
        "usb4java JSR-80 implementation";

    /** The implementation version. */
    private static final String IMP_VERSION = "0.5.0";

    /** The API version. */
    private static final String API_VERSION = "1.0.2";

    /** The USB services listeners. */
    private final UsbServicesListenerList listeners =
        new UsbServicesListenerList();

    /** The virtual USB root hub. */
    private final VirtualRootHub rootHub;

    /** The USB device scanner. */
    private final UsbDeviceScanner deviceScanner;

    /** If devices should be scanned by hierarchy. */
    private final Config config;

    /**
     * Constructor.
     *
     * @throws UsbException
     *             When properties could not be loaded.
     */
    public Services() throws UsbException
    {
        this.config = new Config(UsbHostManager.getProperties());
        usb_init();
        this.rootHub = new VirtualRootHub();
        this.deviceScanner =
            new UsbDeviceScanner(this, this.rootHub, this.config);
        this.deviceScanner.start();
    }

    /**
     * @see UsbServices#getRootUsbHub()
     */
    @Override
    public UsbHub getRootUsbHub()
    {
        this.deviceScanner.firstScan();
        return this.rootHub;
    }

    /**
     * @see UsbServices#addUsbServicesListener(UsbServicesListener)
     */
    @Override
    public void addUsbServicesListener(final UsbServicesListener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * @see UsbServices#removeUsbServicesListener(UsbServicesListener)
     */
    @Override
    public void removeUsbServicesListener(final UsbServicesListener listener)
    {
        this.listeners.remove(listener);
    }

    /**
     * @see UsbServices#getApiVersion()
     */
    @Override
    public String getApiVersion()
    {
        return API_VERSION;
    }

    /**
     * @see UsbServices#getImpVersion()
     */
    @Override
    public String getImpVersion()
    {
        return IMP_VERSION;
    }

    /**
     * @see UsbServices#getImpDescription()
     */
    @Override
    public String getImpDescription()
    {
        return IMP_DESCRIPTION;
    }

    /**
     * Informs listeners about a new attached device.
     *
     * @param device
     *            The new attached device.
     */
    public void usbDeviceAttached(final UsbDevice device)
    {
        this.listeners.usbDeviceAttached(new UsbServicesEvent(this, device));
    }

    /**
     * Informs listeners about a detached device.
     *
     * @param device
     *            The detached device.
     */
    public void usbDeviceDetached(final UsbDevice device)
    {
        this.listeners.usbDeviceDetached(new UsbServicesEvent(this, device));
    }

    /**
     * Returns the configuration.
     *
     * @return The configuration.
     */
    public Config getConfig()
    {
        return this.config;
    }
}
