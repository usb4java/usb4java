/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import static de.ailis.usb4java.USB.USB_CLASS_HUB;
import static de.ailis.usb4java.USB.usb_find_busses;
import static de.ailis.usb4java.USB.usb_find_devices;
import static de.ailis.usb4java.USB.usb_get_busses;
import de.ailis.usb4java.USBLock;
import de.ailis.usb4java.USB_Bus;
import de.ailis.usb4java.USB_Device;
import de.ailis.usb4java.USB_Device_Descriptor;


/**
 * USB Device scanner.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

class UsbDeviceScanner
{
    /** The virtual USB root hub. */
    private final VirtualRootHub rootHub;


    /**
     * Constructor.
     *
     * @param rootHub
     *            The virtual USB root hub.
     */

    public UsbDeviceScanner(final VirtualRootHub rootHub)
    {
        this.rootHub = rootHub;
        scan();
    }


    /**
     * Scans for USB device connection changes.
     */

    private void scan()
    {
        USBLock.acquire();
        try
        {
            final int bussesChanged = usb_find_busses();
            final int devicesChanged = usb_find_devices();
            if (bussesChanged + devicesChanged == 0) return;

            USB_Bus bus = usb_get_busses();
            while (bus != null)
            {
                final USB_Device device = bus.root_dev();
                if (device != null)
                {
                    addDevice(this.rootHub, device);
                }
                bus = bus.next();
            }
        }
        finally
        {
            USBLock.release();
        }
    }


    /**
     * Adds the specified device to the specified hub and recursively scans for
     * more devices.
     *
     * @param parentHub
     *            The parent hub
     * @param device
     *            The device to add and scan.
     */

    private void addDevice(final UsbPorts parentHub, final USB_Device device)
    {
        final USB_Device_Descriptor descriptor = device.descriptor();
        if (descriptor.bDeviceClass() == USB_CLASS_HUB)
        {
            final UsbHubImpl hub = new UsbHubImpl(device);
            parentHub.connectUsbDevice(hub);
            for (final USB_Device child : device.children())
                addDevice(hub, child);
        }
        else
        {
            final UsbDeviceImpl dev = new UsbDeviceImpl(device);
            parentHub.connectUsbDevice(dev);
        }
    }
}
