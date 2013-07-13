/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import de.ailis.usb4java.libusb.Loader;
import de.ailis.usb4java.libusb.LoaderException;

/**
 * usb4java implementation of JSR-80 UsbServices.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Services implements UsbServices
{
    /** The implementation description. */
    private static final String IMP_DESCRIPTION = "usb4java";

    /** The implementation version. */
    private static final String IMP_VERSION = "1.1.0";

    /** The API version. */
    private static final String API_VERSION = "1.0.2";

    /** The USB services listeners. */
    private final ServicesListenerList listeners =
        new ServicesListenerList();

    /** The virtual USB root hub. */
    private final RootHub rootHub;

    /** The USB device scanner. */
    private final DeviceManager deviceManager;

    /** If devices should be scanned by hierarchy. */
    private final Config config;

    /**
     * Constructor.
     * 
     * @throws UsbException
     *             When properties could not be loaded.
     * @throws LoaderException
     *             When native libraries could not be loaded.
     */
    public Services() throws UsbException
    {
        this.config = new Config(UsbHostManager.getProperties());
        Loader.load();
        this.rootHub = new RootHub();
        this.deviceManager = new DeviceManager(this.rootHub, 
            this.config.getScanInterval());
        this.deviceManager.start();
    }

    @Override
    public UsbHub getRootUsbHub()
    {
        this.deviceManager.firstScan();
        return this.rootHub;
    }

    @Override
    public void addUsbServicesListener(final UsbServicesListener listener)
    {
        this.listeners.add(listener);
    }

    @Override
    public void removeUsbServicesListener(final UsbServicesListener listener)
    {
        this.listeners.remove(listener);
    }

    @Override
    public String getApiVersion()
    {
        return API_VERSION;
    }

    @Override
    public String getImpVersion()
    {
        return IMP_VERSION;
    }

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
    void usbDeviceAttached(final UsbDevice device)
    {
        this.listeners.usbDeviceAttached(new UsbServicesEvent(this, device));
    }

    /**
     * Informs listeners about a detached device.
     * 
     * @param device
     *            The detached device.
     */
    void usbDeviceDetached(final UsbDevice device)
    {
        this.listeners.usbDeviceDetached(new UsbServicesEvent(this, device));
    }

    /**
     * Returns the configuration.
     * 
     * @return The configuration.
     */
    Config getConfig()
    {
        return this.config;
    }

    /**
     * Returns the usb4java services.
     * 
     * @return The usb4java services.
     */
    static Services getInstance()
    {
        try
        {
            return (Services) UsbHostManager.getUsbServices();
        }
        catch (final ClassCastException e)
        {
            throw new ServicesException("Looks like usb4java is not the "
                + "configured USB services implementation: " + e, e);
        }
        catch (final UsbException e)
        {
            throw new ServicesException("Unable to create USB services: "
                + e, e);
        }
    }
    
    /**
     * Manually scans for USB device connection changes.
     */
    public void scan()
    {
        this.deviceManager.scan();
    }
}
