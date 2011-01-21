/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import de.ailis.usb4java.jni.USB;
import de.ailis.usb4java.jni.USBBus;
import de.ailis.usb4java.jni.USBConfigDescriptor;
import de.ailis.usb4java.jni.USBDevHandle;
import de.ailis.usb4java.jni.USBDevice;
import de.ailis.usb4java.jni.USBDeviceDescriptor;
import de.ailis.usb4java.jni.USBEndpointDescriptor;
import de.ailis.usb4java.jni.USBInterface;
import de.ailis.usb4java.jni.USBInterfaceDescriptor;


/**
 * Dump
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class Dump
{
    private static int level = 0;

    private static void indent()
    {
        int i;

        for (i = 0; i < level; i++) System.out.format("  ");
    }

    private static void dump_device_descriptor(final USBDeviceDescriptor descriptor)
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

    private static void dump_endpoint_descriptor(final USBEndpointDescriptor descriptor)
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
        indent(); System.out.format("extralen: 0x%04x\n", descriptor.extralen());
        indent(); System.out.format("extra:");
        for (i = 0; i < descriptor.extralen(); i++)
            System.out.format(" %02x", descriptor.extra()[i]);
        System.out.format("\n");
        level--;
    }

    private static void dump_interface_descriptor(final USBInterfaceDescriptor descriptor)
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
        indent(); System.out.format("extralen: 0x%04x\n", descriptor.extralen());
        indent(); System.out.format("extra:");
        for (i = 0; i < descriptor.extralen(); i++)
            System.out.format(" %02x", descriptor.extra()[i]);
        System.out.format("\n");
        indent(); System.out.format("Endpoints:\n");
        level++;
        for (i = 0; i < descriptor.bNumEndpoints(); i++)
            dump_endpoint_descriptor(descriptor.endpoint()[i]);
        level--;
        level--;
    }

    private static void dump_interface(final USBInterface iface)
    {
        int i;

        indent(); System.out.format("Interface:\n");
        level++;
        indent(); System.out.format("num_altsetting: 0x%04x\n", iface.num_altsetting());
        indent(); System.out.format("altsetting:\n");
        level++;
        for (i = 0; i < iface.num_altsetting(); i++)
            dump_interface_descriptor(iface.altsetting()[i]);
        level--;
        level--;
    }

    private static void dump_config_descriptor(final USBConfigDescriptor config)
    {
        int i;
        final int max;

        indent(); System.out.format("Config Descriptor:\n");
        level++;
        indent(); System.out.format("bLength: 0x%02x\n", config.bLength());
        indent(); System.out.format("bDescriptorType: 0x%02x\n", config.bDescriptorType());
        indent(); System.out.format("wTotalLength: 0x%04x\n", config.wTotalLength());
        indent(); System.out.format("bNumInterfaces: 0x%02x\n", config.bNumInterfaces());
        indent(); System.out.format("bConfigurationValue: 0x%02x\n", config.bConfigurationValue());
        indent(); System.out.format("iConfiguration: 0x%02x\n", config.iConfiguration());
        indent(); System.out.format("bmAttributes: 0x%02x\n", config.bmAttributes());
        indent(); System.out.format("MaxPower: 0x%02x\n", config.MaxPower());
        indent(); System.out.format("extralen: 0x%04x\n", config.extralen());
        indent(); System.out.format("extra:");
        for (i = 0; i < config.extralen(); i++)
            System.out.format(" %02x", config.extra()[i]);
        System.out.format("\n");
        indent(); System.out.format("Interfaces:\n");
        level++;
        for (i = 0; i < config.bNumInterfaces(); i++)
            dump_interface(config.interface_()[i]);
        level--;
        level--;
    }

    private static void dump_device(final USBDevice device)
    {
        int i;
        final byte[] buffer = new byte[256];

        indent(); System.out.format("Device:\n");
        level++;
        indent(); System.out.format("filename: %s\n", device.filename());
        indent(); System.out.format("bus: %s\n", device.bus().dirname());
        indent(); System.out.format("devnum: %i\n", device.devnum());
        indent(); System.out.format("num_children: %i\n", device.num_children());
        indent(); System.out.format("descriptor:\n");
        level++;
        dump_device_descriptor(device.descriptor());
        level--;
        // Rename me to USBHandle
        final USBDevHandle handle = USB.usb_open(device);
        i = USB.usb_get_string_simple(handle, device.descriptor().iManufacturer(), buffer, 255);
        indent(); System.out.format("Manufacturer: %s\n", i > 0 ? buffer : "Unknown");
        i = USB.usb_get_string_simple(handle, device.descriptor().iProduct(), buffer, 255);
        indent(); System.out.format("Product: %s\n", i > 0 ? buffer : "Unknown");
        i = USB.usb_get_string_simple(handle, device.descriptor().iSerialNumber(), buffer, 255);
        indent(); System.out.format("Serial: %s\n", i > 0 ? buffer : "Unknown");
        USB.usb_close(handle);
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
     */

    public static void main(final String[] args)
    {
        USB.usb_init();
        final int bus_count = USB.usb_find_busses();
        System.out.format("Found %i busses\n", bus_count);
        final int dev_count = USB.usb_find_devices();
        System.out.format("Found %i devices\n", dev_count);

        USBBus bus = USB.usb_get_busses();
        while (bus != null)
        {
            System.out.format("Bus:\n");
            level++;
            indent(); System.out.format("dirname: %s\n", bus.dirname());
            indent(); System.out.format("location: %i\n", bus.location());
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
            USBDevice device = bus.devices();
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
