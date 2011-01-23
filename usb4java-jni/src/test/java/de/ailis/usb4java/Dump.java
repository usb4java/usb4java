/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import static de.ailis.usb4java.USB.usb_close;
import static de.ailis.usb4java.USB.usb_find_busses;
import static de.ailis.usb4java.USB.usb_find_devices;
import static de.ailis.usb4java.USB.usb_get_busses;
import static de.ailis.usb4java.USB.usb_get_string_simple;
import static de.ailis.usb4java.USB.usb_init;
import static de.ailis.usb4java.USB.usb_open;


/**
 * This program dumps all available information about all USB busses and
 * devices to the console. This is uses to compare the output to the output
 * of the "dump" program which is written in plain C. If the output is
 * exactly the same then it is most likely that the JNI wrapper works
 * correctly.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class Dump
{
    /** The current indentation level. */
    private static int level = 0;

    /**
     * Indents the current line.
     */

    private static void indent()
    {
        int i;

        for (i = 0; i < level; i++) System.out.format("  ");
    }


    /**
     * Dumps the specified device descriptor.
     *
     * @param descriptor
     *            The device descriptor to dump
     */

    private static void dump_device_descriptor(final USB_Device_Descriptor descriptor)
    {
        indent(); System.out.format("bLength: 0x%02x\n", descriptor.bLength());
        indent(); System.out.format("bDescriptorType: 0x%02x\n", descriptor.bDescriptorType());
        indent(); System.out.format("bcdUSB: 0x%04x\n", descriptor.bcdUSB());
        indent(); System.out.format("bDeviceClass: 0x%02x\n", descriptor.bDeviceClass());
        indent(); System.out.format("bDeviceSubClass: 0x%02x\n", descriptor.bDeviceSubClass());
        indent(); System.out.format("bDeviceProtocol: 0x%02x\n", descriptor.bDeviceProtocol());
        indent(); System.out.format("bMaxPacketSize0: 0x%02x\n", descriptor.bMaxPacketSize0());
        indent(); System.out.format("idVendor: 0x%04x\n", descriptor.idVendor());
        indent(); System.out.format("idProduct: 0x%04x\n", descriptor.idProduct());
        indent(); System.out.format("bcdDevice: 0x%04x\n", descriptor.bcdDevice());
        indent(); System.out.format("iManufacturer: 0x%02x\n", descriptor.iManufacturer());
        indent(); System.out.format("iProduct: 0x%02x\n", descriptor.iProduct());
        indent(); System.out.format("iSerialNumber: 0x%02x\n", descriptor.iSerialNumber());
        indent(); System.out.format("bNumConfigurations: 0x%02x\n", descriptor.bNumConfigurations());
    }


    /**
     * Dumps the specified endpoint descriptor.
     *
     * @param descriptor
     *            The endpoint descriptor to dump
     */

    private static void dump_endpoint_descriptor(final USB_Endpoint_Descriptor descriptor)
    {
        int i;

        indent(); System.out.format("Endpoint\n");
        level++;
        indent(); System.out.format("bLength: 0x%02x\n", descriptor.bLength());
        indent(); System.out.format("bDescriptorType: 0x%02x\n", descriptor.bDescriptorType());
        indent(); System.out.format("bEndpointAddress: 0x%02x\n", descriptor.bEndpointAddress());
        indent(); System.out.format("bmAttributes: 0x%02x\n", descriptor.bmAttributes());
        indent(); System.out.format("wMaxPacketSize: 0x%04x\n", descriptor.wMaxPacketSize());
        indent(); System.out.format("bInterval: 0x%02x\n", descriptor.bInterval());
        indent(); System.out.format("bRefresh: 0x%02x\n", descriptor.bRefresh());
        indent(); System.out.format("bSynchAddress: 0x%02x\n", descriptor.bSynchAddress());
        indent(); System.out.format("extralen: 0x%08x\n", descriptor.extralen());
        indent(); System.out.format("extra:");
        for (i = 0; i < descriptor.extralen(); i++)
            System.out.format(" %02x", descriptor.extra().get(i));
        System.out.format("\n");
        level--;
    }


    /**
     * Dumps the specified interface descriptor.
     *
     * @param descriptor
     *            The interface descriptor to dump.
     */

    private static void dump_interface_descriptor(final USB_Interface_Descriptor descriptor)
    {
        int i;

        indent(); System.out.format("Interface descriptor:\n");
        level++;
        indent(); System.out.format("bLength: 0x%02x\n", descriptor.bLength());
        indent(); System.out.format("bDescriptorType: 0x%02x\n", descriptor.bDescriptorType());
        indent(); System.out.format("bInterfaceNumber: 0x%02x\n", descriptor.bInterfaceNumber());
        indent(); System.out.format("bAlternateSetting: 0x%02x\n", descriptor.bAlternateSetting());
        indent(); System.out.format("bNumEndpoints: 0x%02x\n", descriptor.bNumEndpoints());
        indent(); System.out.format("bInterfaceClass: 0x%02x\n", descriptor.bInterfaceClass());
        indent(); System.out.format("bInterfaceSubClass: 0x%02x\n", descriptor.bInterfaceSubClass());
        indent(); System.out.format("bInterfaceProtocol: 0x%02x\n", descriptor.bInterfaceProtocol());
        indent(); System.out.format("iInterface: 0x%02x\n", descriptor.iInterface());
        indent(); System.out.format("extralen: 0x%08x\n", descriptor.extralen());
        indent(); System.out.format("extra:");
        for (i = 0; i < descriptor.extralen(); i++)
            System.out.format(" %02x", descriptor.extra().get(i));
        System.out.format("\n");
        indent(); System.out.format("Endpoints:\n");
        level++;
        for (i = 0; i < descriptor.bNumEndpoints(); i++)
            dump_endpoint_descriptor(descriptor.endpoint()[i]);
        level--;
        level--;
    }


    /**
     * Dumps the specified interface.
     *
     * @param iface
     *            The interface to dump.
     */

    private static void dump_interface(final USB_Interface iface)
    {
        int i;

        indent(); System.out.format("Interface:\n");
        level++;
        indent(); System.out.format("num_altsetting: 0x%08x\n", iface.num_altsetting());
        indent(); System.out.format("altsetting:\n");
        level++;
        for (i = 0; i < iface.num_altsetting(); i++)
            dump_interface_descriptor(iface.altsetting()[i]);
        level--;
        level--;
    }


    /**
     * Dumps the specified config descriptor.
     *
     * @param config
     *            The config descriptor to dump.
     */

    private static void dump_config_descriptor(final USB_Config_Descriptor config)
    {
        int i;

        indent(); System.out.format("Config Descriptor:\n");
        level++;
        indent(); System.out.format("bLength: 0x%02x\n", config.bLength());
        indent(); System.out.format("bDescriptorType: 0x%02x\n", config.bDescriptorType());
        indent(); System.out.format("wTotalLength: 0x%04x\n", config.wTotalLength());
        indent(); System.out.format("bNumInterfaces: 0x%02x\n", config.bNumInterfaces());
        indent(); System.out.format("bConfigurationValue: 0x%02x\n", config.bConfigurationValue());
        indent(); System.out.format("iConfiguration: 0x%02x\n", config.iConfiguration());
        indent(); System.out.format("bmAttributes: 0x%02x\n", config.bmAttributes());
        indent(); System.out.format("MaxPower: 0x%02x\n", config.bMaxPower());
        indent(); System.out.format("extralen: 0x%08x\n", config.extralen());
        indent(); System.out.format("extra:");
        for (i = 0; i < config.extralen(); i++)
            System.out.format(" %02x", config.extra().get(i));
        System.out.format("\n");
        indent(); System.out.format("Interfaces:\n");
        level++;
        for (i = 0; i < config.bNumInterfaces(); i++)
            dump_interface(config.iface()[i]);
        level--;
        level--;
    }


    /**
     * Dumps the specified USB device.
     *
     * @param device
     *            The USB device to dump.
     */

    private static void dump_device(final USB_Device device)
    {
        int i;

        indent(); System.out.format("Device:\n");
        level++;
        indent(); System.out.format("filename: %s\n", device.filename());
        indent(); System.out.format("bus: %s\n", device.bus().dirname());
        indent(); System.out.format("devnum: %d\n", device.devnum());
        indent(); System.out.format("num_children: %d\n", device.num_children());
        indent(); System.out.format("descriptor:\n");
        level++;
        dump_device_descriptor(device.descriptor());
        level--;
        final USB_Dev_Handle handle = usb_open(device);
        final String manufacturer = usb_get_string_simple(handle, device.descriptor().iManufacturer());
        indent(); System.out.format("Manufacturer: %s\n", manufacturer != null ? manufacturer : "Unknown");
        final String product = usb_get_string_simple(handle, device.descriptor().iProduct());
        indent(); System.out.format("Product: %s\n", product != null ? product : "Unknown");
        final String serialNumber = usb_get_string_simple(handle, device.descriptor().iSerialNumber());
        indent(); System.out.format("Serial: %s\n", serialNumber != null ? serialNumber : "Unknown");
        indent(); System.out.print("Languages:");
        final short[] languages = USB.usb_get_languages(handle);
        if (languages != null)
        {
            for (final short language: languages)
                System.out.format(" 0x%04x", language);
            System.out.println();
        }
        else
            System.out.println("Unknown");

        usb_close(handle);
        indent(); System.out.format("config descriptors:\n");
        level++;
        final int max = device.descriptor().bNumConfigurations();
        if (max == 0)
        {
            indent(); System.out.format("None\n");
        }
        else
        {
            for (i = 0; i < max; i++)
            {
                dump_config_descriptor(device.config()[i]);
            }
        }
        level--;
        indent(); System.out.format("children:\n");
        level++;
        if (device.num_children() == 0)
        {
            indent(); System.out.format("None\n");
        }
        else
        {
            for (i = 0; i< device.num_children(); i++)
                dump_device(device.children()[i]);
        }
        level--;
        level--;
    }


    /**
     * Main method.
     *
     * @param args
     *            The command line arguments (Ignored.
     */

    public static void main(final String[] args)
    {
        usb_init();
        final int bus_count = usb_find_busses();
        System.out.format("Found %d busses\n", bus_count);
        final int dev_count = usb_find_devices();
        System.out.format("Found %d devices\n", dev_count);

        USB_Bus bus = usb_get_busses();
        while (bus != null)
        {
            System.out.format("Bus:\n");
            level++;
            indent(); System.out.format("dirname: %s\n", bus.dirname());
            indent(); System.out.format("location: %d\n", bus.location());
            indent(); System.out.format("Root device: ");
            level++;
            if (bus.root_dev() != null)
                System.out.format("%s/%s\n", bus.root_dev().bus().dirname(),
                    bus.root_dev().filename());
            else
                System.out.format("None\n");
            level--;
            indent(); System.out.format("devices: \n");
            level++;
            USB_Device device = bus.devices();
            while (device != null)
            {
                dump_device(device);
                device = device.next();
            }
            level--;
            level--;

            System.out.format("\n");
            bus = bus.next();
        }
    }
}
