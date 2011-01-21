/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesListener;



/**
 * UsbServices implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbServicesImpl implements UsbServices
{
    /** The minimum implemented API version. */
    private final static String API_VERSION = "1.0.0";

    /** The implementation description. */
    private final static String IMPL_DESCRIPTION =
            "de.ailis.libusb JSR80 Implementation";

    /** The implementation version. */
    private final static String IMPL_VERSION = "1.0.0";

    /** The virtual root USB hub. */
    private final UsbHubImpl rootUsbHub = new VirtualRootUsbHub();

    /** The list of registered USB services listeners. */
    private final UsbServicesListenerList usbServicesListeners = new UsbServicesListenerList();


    /**
     * Constructor.
     */

    public UsbServicesImpl()
    {
        USB.usb_init();
        startUsbPolling();
    }


    /**
     * Starts the thread which polls for USB changes in regular intervals.
     */

    private void startUsbPolling()
    {
        final Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    pollUsb();
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (final InterruptedException e)
                    {
                        // Ignored
                    }
                }
            }
        };

        final Thread pollThread = new Thread(runnable);
        pollThread.setDaemon(true);
        pollThread.setName("usb4java USB Polling");
        pollThread.start();
    }


    /**
     * Polls USB for changes.
     */

    void pollUsb()
    {
        // Poll for USB changes
        final int busCount = USB.usb_find_busses();
        final int deviceCount = USB.usb_find_devices();

        // If nothing was changed then exit immediately
        if (busCount + deviceCount == 0) return;

        // Iterate over all USB busses
        USB_Bus bus = USB.usb_get_busses();
        while (bus != null)
        {
            // Iterate over all USB devices of current bus
            USB_Device device = bus.devices();
            while (device != null)
            {
                if (device.config() != null)
                {
                    final UsbDevice usbDevice = createDevice(device);
                }
                device = device.next();
            }
            bus = bus.next();
        }
    }


    /**
     * Creates the JSR80 USB device from the specified low-level USB device.
     *
     * @param device
     *            The low-level USB device
     * @return The JSR80 USB device.
     */

    private UsbDevice createDevice(final USB_Device device)
    {
        final USB_Device_Descriptor descriptor = device.descriptor();
        if (descriptor.bDeviceClass() == USB.USB_CLASS_HUB)
        {
            return new UsbHubImpl(device);
        }
        else
        {
            final UsbDeviceImpl usbDevice = new UsbDeviceImpl(device);
            for (int i = 0; i < descriptor.bNumConfigurations(); i++)
            {
                usbDevice.addUsbConfiguration(createConfig(device.config()[i]));
            }
            return usbDevice;
        }
    }

    /**
     * Creates the JSR80 USB configuration from the specified low-level USB
     * configuration descriptor.
     *
     * @param config
     *            The low-level configuration descriptor.
     * @return The JSR80 USB configuration.
     */

    private UsbConfiguration createConfig(final USB_Config_Descriptor config)
    {
        final UsbConfigurationDescriptor descriptor = new UsbConfigurationDescriptorImpl(
            config);
        final UsbConfigurationImpl usbConfig = new UsbConfigurationImpl(
            descriptor);

        for (int i = 0, iMax = config.bNumInterfaces(); i < iMax; i++)
        {
            final USB_Interface iface = config.interface_()[i];
            for (int j = 0, jMax = iface.num_altsetting(); j < jMax; j++)
            {
                final USB_Interface_Descriptor ifaceDescriptor = iface
                        .altsetting()[j];
                usbConfig.addUsbInterface(createInterface(ifaceDescriptor));
            }
        }

        return usbConfig;
    }


    /**
     * Creates the JSR80 USB interface for the specified low-level USB interface
     * descriptor.
     *
     * @param descriptor
     *            The low-level USB interface descriptor.
     * @return The JSR80 USB interface.
     */

    private UsbInterface createInterface(final USB_Interface_Descriptor descriptor)
    {
        final UsbInterfaceDescriptor ifaceDescriptor = new UsbInterfaceDescriptorImpl(
            descriptor);
        final UsbInterface iface = new UsbInterfaceImpl(ifaceDescriptor);

        // TODO Implement me!
        throw new UnsupportedOperationException();

        // return iface;
    }


    /**
     * @see javax.usb.UsbServices#getRootUsbHub()
     */

    @Override
    public UsbHub getRootUsbHub() throws UsbException, SecurityException
    {
        return this.rootUsbHub;
    }


    /**
     * @see javax.usb.UsbServices#addUsbServicesListener(javax.usb.event.UsbServicesListener)
     */

    @Override
    public void addUsbServicesListener(final UsbServicesListener listener)
    {
        this.usbServicesListeners.addListener(listener);
    }


    /**
     * @see javax.usb.UsbServices#removeUsbServicesListener(javax.usb.event.UsbServicesListener)
     */

    @Override
    public void removeUsbServicesListener(final UsbServicesListener listener)
    {
        this.usbServicesListeners.removeListener(listener);
    }


    /**
     * @see javax.usb.UsbServices#getApiVersion()
     */

    @Override
    public String getApiVersion()
    {
        return API_VERSION;
    }


    /**
     * @see javax.usb.UsbServices#getImpVersion()
     */

    @Override
    public String getImpVersion()
    {
        return IMPL_VERSION;
    }


    /**
     * @see javax.usb.UsbServices#getImpDescription()
     */

    @Override
    public String getImpDescription()
    {
        return IMPL_DESCRIPTION;
    }
}
