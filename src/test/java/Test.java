import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbPort;
import javax.usb.UsbServices;

/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */


/**
 * Test
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class Test
{
    public static void dumpHub(final UsbHub rootHub, final String indent) throws Exception
    {
        final List<UsbPort> ports = rootHub.getUsbPorts();
        for (final UsbPort port: ports)
        {
            System.out.print(indent);
            System.out.format(" \\__%d: ", port.getPortNumber());
            if (port.isUsbDeviceAttached())
            {
                final UsbDevice device = port.getUsbDevice();
                System.out.println(device.getProductString());
                if (device.isUsbHub())
                {
                    final UsbHub hub = (UsbHub) device;
                    dumpHub(hub, indent + "    ");
                }
            }
            else System.out.println("No device attached");
        }
    }

    public static void main(final String[] args) throws Exception
    {
        final UsbServices services = UsbHostManager.getUsbServices();
        final UsbHub rootHub = services.getRootUsbHub();
        System.out.println(rootHub.getProductString());
        dumpHub(rootHub, "");
    }
}

