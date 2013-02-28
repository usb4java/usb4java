/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package de.ailis.usb4java.libusb;

import java.nio.IntBuffer;
import java.util.Arrays;

/**
 */
public class Test
{
    /**
     * @param args command line args
     */
    public static void main(String[] args)
    {
        System.out.println(LibUSB.getVersion());
        Context context = new Context();
        System.out.println(LibUSB.init(context));
        LibUSB.setDebug(context, LibUSB.LOG_LEVEL_WARNING);
        
        DeviceList list = new DeviceList();
        LibUSB.getDeviceList(context, list);
        System.out.println(list.getSize());
        for (int i = 0; i < 11; i++) System.out.println(list.get(i));
        for (Device device: list)
        {
            System.out.println(device);
            System.out.println("Bus: " + LibUSB.getBusNumber(device));
            System.out.println("Port: " + LibUSB.getPortNumber(device));
            System.out.println("Parent: " + LibUSB.getParent(device));
            System.out.println("Address: " + LibUSB.getDeviceAddress(device));
            System.out.println("Speed: " + LibUSB.getDeviceSpeed(device));
            System.out.println("Max packet size: " + LibUSB.getMaxPacketSize(device, 0x01));
            System.out.println("Max ISO packet size: " + LibUSB.getMaxIsoPacketSize(device, 0x01));
            byte path[] = new byte[8];
            System.out.println("GetPortPath: " + LibUSB.getPortPath(context,  device, path));
            System.out.println("PortPath: " + Arrays.toString(path));
            LibUSB.refDevice(device);
            LibUSB.unrefDevice(device);
            
            DeviceHandle handle = new DeviceHandle();
            System.out.println("Open: " + LibUSB.open(device,  handle));
            LibUSB.close(handle);
        }
        LibUSB.freeDeviceList(list, true);
        
        System.out.println("1");
        System.out.println("OPen missing device: " + LibUSB.openDeviceWithVidPid(context, 0, 0));
        System.out.println("2");
        DeviceHandle handle = LibUSB.openDeviceWithVidPid(context, 0x0a5c, 0x5800);
        System.out.println("OPen existing device: " + handle);
        System.out.println("Device: " + LibUSB.getDevice(handle));
        IntBuffer config = IntBuffer.allocate(1);
        config.put(0, 43);
        System.out.println("Set Config: " + LibUSB.setConfiguration(handle, 0));
        System.out.println("Get Config: " + LibUSB.getConfiguration(handle, config));
        System.out.println("Config: " + config.get());
        System.out.println("Claim Interface: " + LibUSB.claimInterface(handle, 1));
        System.out.println("SetAlt: " + LibUSB.setInterfaceAltSetting(handle,  1,  0));
        System.out.println("Clear halt: " + LibUSB.clearHalt(handle, 0x82));
        System.out.println("Reset: " + LibUSB.resetDevice(handle));
        System.out.println("Kernel driver active: " + LibUSB.kernelDriverActive(handle, 0));
        System.out.println("Detach: " + LibUSB.detachKernelDriver(handle, 0));
        System.out.println("Attach: " + LibUSB.attachKernelDriver(handle, 0));
        DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
        Device device = LibUSB.getDevice(handle);
        System.out.println("Get device desc: " + LibUSB.getDeviceDescriptor(device,  deviceDescriptor));
        System.out.println("idVendor: " + String.format("%04x", deviceDescriptor.idProduct()));
        System.out.println("iManufacturer: " + deviceDescriptor.iManufacturer());
        System.out.println("iProduct: " + deviceDescriptor.iProduct());
        System.out.println(deviceDescriptor.dump(handle));
        System.out.println(LibUSB.getStringDescriptor(handle, deviceDescriptor.iProduct()));
        System.out.println(LibUSB.getStringDescriptor(handle, deviceDescriptor.iProduct()).length());
        
        System.out.println("Release Interface: " + LibUSB.releaseInterface(handle, 1));
        LibUSB.close(handle);
        System.out.println("Has CAP: " + LibUSB.hasCapability(LibUSB.CAP_HAS_CAPABILITY));
        System.out.println("Has not CAP: " + LibUSB.hasCapability(LibUSB.CAP_HAS_CAPABILITY + 1));
        System.out.println("Error 0:" + LibUSB.errorName(0));
        System.out.println("Error -1:" + LibUSB.errorName(-1));
        System.out.println("Error 1:" + LibUSB.errorName(1));
        System.out.println(String.format("Little endian: %04x", LibUSB.cpuToLe16(0x8765)));
        System.out.println(String.format("Host endian: %04x", LibUSB.le16ToCpu(0x8765)));
        LibUSB.exit(context);
    }
}
