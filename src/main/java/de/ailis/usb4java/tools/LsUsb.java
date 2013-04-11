/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.tools;

import static de.ailis.usb4java.jni.USB.usb_find_busses;
import static de.ailis.usb4java.jni.USB.usb_find_devices;
import static de.ailis.usb4java.jni.USB.usb_get_busses;
import static de.ailis.usb4java.jni.USB.usb_init;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import de.ailis.usb4java.jni.USB_Bus;
import de.ailis.usb4java.jni.USB_Device;
import de.ailis.usb4java.jni.USB_Device_Descriptor;
import de.ailis.usb4java.libusb.Device;
import de.ailis.usb4java.libusb.DeviceDescriptor;
import de.ailis.usb4java.libusb.DeviceList;
import de.ailis.usb4java.libusb.LibUSB;

/**
 * Lists USB devices similar to the  linux command line tool "lsusb".
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class LsUsb
{
    /**
     * Private constructor to prevent instantiation.
     */
    public LsUsb()
    {
        // Empty
    }
    
    public void getDevices(UsbDevice device, List<UsbDevice> devices)
    {
        if (device.isUsbHub())
        {
            UsbHub hub = (UsbHub) device;
            for (Object child: hub.getAttachedUsbDevices())
            {
                devices.add((UsbDevice) child);
                getDevices((UsbDevice) child, devices);
            }
        }
    }

    private void dumpDevice(UsbDevice device, int indent)
    {
        for (int i = 0; i < indent * 2; i++) System.out.print(" ");
        System.out.println(device);
        if (device.isUsbHub())
        {
            for (Object child: ((UsbHub) device).getAttachedUsbDevices())
            {
                dumpDevice((UsbDevice) child, indent + 1);
            }
        }
    }
    
    
    /**
     * Runs the tool with the specified command line arguments.
     * 
     * @param args The command line tools.
     * @throws UsbException 
     * @throws SecurityException 
     */
    public void run(String[] args) throws SecurityException, UsbException
    {
        UsbServices services = UsbHostManager.getUsbServices();
        UsbHub rootHub = services.getRootUsbHub();
        List<UsbDevice> devices = new ArrayList<UsbDevice>();
        getDevices(rootHub, devices);
        for (UsbDevice device: devices)
        {
            
            System.out.println(device.toString());
        }
        
        System.out.println();
        
        DeviceList list = new DeviceList();
        LibUSB.getDeviceList(null, list);
        for (Device device: list)
        {
            Device parent = LibUSB.getParent(device);
            String parStr = "";
            if (parent != null)
            {
                int parentBus = LibUSB.getBusNumber(parent);
                int parentAddress = LibUSB.getDeviceAddress(parent);
                parStr = " (Parent: " + parentBus + "/" + parentAddress + ")";
            }
            DeviceDescriptor desc = new DeviceDescriptor();
            LibUSB.getDeviceDescriptor(device, desc);
            int bus = LibUSB.getBusNumber(device);
            int address = LibUSB.getDeviceAddress(device);
            System.out.println(String.format("Bus %03d Device %03d: ID %04x:%04x %s %s",
                bus, address, desc.idVendor(), desc.idProduct(), parStr, desc.bDeviceClass() == LibUSB.CLASS_HUB ? "hub" : "device"));
        }
        LibUSB.freeDeviceList(list, true);
        
        System.out.println("");
        
        dumpDevice(rootHub, 0);
    }

    /**
     * Main method.
     *
     * @param args
     *            The command line arguments.
     */
    public static void main(final String[] args) throws Exception
    {
        LsUsb tool = new LsUsb();
        tool.run(args);
    }
}
