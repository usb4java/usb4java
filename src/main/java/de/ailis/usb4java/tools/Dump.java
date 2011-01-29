/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.tools;

import static de.ailis.usb4java.USB.usb_find_busses;
import static de.ailis.usb4java.USB.usb_find_devices;
import static de.ailis.usb4java.USB.usb_get_busses;
import static de.ailis.usb4java.USB.usb_init;
import de.ailis.usb4java.USB_Bus;
import de.ailis.usb4java.USB_Device;
import de.ailis.usb4java.USB_Device_Descriptor;


/**
 * Demo program which dumps a detailed list of all USB devices to stdout.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class Dump
{
    /**
     * Dumps the device tree. Please note that the displayed tree can only
     * display devices to which the user has write access. On some platforms
     * the tree can't be dumped at all.
     */

    private static void dumpDeviceTree()
    {
        usb_init();
        usb_find_busses();
        usb_find_devices();
        USB_Bus bus = usb_get_busses();
        while (bus != null)
        {
            final USB_Device root = bus.root_dev();
            if (root != null)
            {
                System.out.format("Bus %s%n", bus.dirname());
                dumpSubDeviceTree(bus.root_dev(), " ");
            }
            bus = bus.next();
        }
    }


    /**
     * Dumps a sub device tree.
     *
     * @param device
     *            The device to dump.
     * @param indent
     *            The indent string.
     */

    private static void dumpSubDeviceTree(final USB_Device device,
        final String indent)
    {
        if (device == null) return;
        final USB_Device_Descriptor descriptor = device.descriptor();
        System.out.format("%s\\__ Dev %s (%04x:%04x)%n", indent,
            device.filename(), descriptor.idVendor(), descriptor.idProduct());
        for (final USB_Device child : device.children())
            dumpSubDeviceTree(child, indent + "     ");
    }


    /**
     * Dumps all busses to stdout.
     */

    private static void dumpBusses()
    {
        usb_init();
        usb_find_busses();
        usb_find_devices();
        USB_Bus bus = usb_get_busses();
        while (bus != null)
        {
            System.out.print(bus.toString());
            bus = bus.next();
        }
    }


    /**
     * Main method.
     *
     * @param args
     *            The command line arguments.
     */

    public static void main(final String[] args)
    {
        dumpDeviceTree();
        System.out.println();
        dumpBusses();
    }
}
