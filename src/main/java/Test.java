import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
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
 * @author k
 */

public class Test
{
    private static int indentLevel = 0;

    public static String indent()
    {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) str.append("  ");
        return str.toString();
    }

    public static String getSpeedString(final Object speed)
    {
        if (speed == UsbConst.DEVICE_SPEED_FULL)
            return "Full";
        else if (speed == UsbConst.DEVICE_SPEED_LOW)
            return "Low";
        else if (speed == UsbConst.DEVICE_SPEED_UNKNOWN)
            return "Unknown";
        else
            throw new RuntimeException("Unknown speed object: " + speed);
    }

    public static void dumpHub(final UsbHub hub) throws Exception
    {
        System.out.println(indent() + "Manufacturer: " + hub.getManufacturerString());
        System.out.println(indent() + "Product: " + hub.getProductString());
        System.out.println(indent() + "Serial number: " + hub.getSerialNumberString());
        System.out.println(indent() + "Speed: " + getSpeedString(hub.getSpeed()));
        System.out.println(indent() + "Devices: " + hub.getAttachedUsbDevices().size());
        System.exit(1);

        indentLevel++;
        for (byte i = 0; i < hub.getAttachedUsbDevices().size(); i++)
        {
            System.out.println(indent() + "Device " + i + ":");
            indentLevel++;
            final UsbDevice device = (UsbDevice) hub.getAttachedUsbDevices().get(i);
            dumpDevice(device);
            indentLevel--;
        }
        indentLevel--;

        /*
        System.out.println(indent() + "Ports: " + hub.getUsbPorts().size());

        indentLevel++;
        for (byte i = 1; i <= hub.getNumberOfPorts(); i++)
        {
            System.out.println(indent() + "Port " + i + ":");
            indentLevel++;
            final UsbPort port = hub.getUsbPort(i);
            dumpPort(port);
            indentLevel--;
        }
        indentLevel--;
        */
    }

    public static void dumpPort(final UsbPort port) throws Exception
    {
        System.out.println(indent() + "Port number: " + port.getPortNumber());
        final UsbDevice device = port.getUsbDevice();
        if (device != null) dumpDevice(device);
    }

    public static void dumpDevice(final UsbDevice device) throws Exception
    {
        System.out.println(indent() + "Configured: " + device.isConfigured());
        System.out.println(indent() + "Is Hub: " + device.isUsbHub());
        if (device.isUsbHub())
        {
         //   dumpHub((UsbHub) device);
        }
        else
        {
            System.out.println(indent() + "Manufacturer: " + device.getManufacturerString());
            System.out.println(indent() + "Product: " + device.getProductString());
            System.out.println(indent() + "Serial number: " + device.getSerialNumberString());
            System.out.println(indent() + "Speed: " + getSpeedString(device.getSpeed()));
        }
        final UsbDeviceDescriptor descriptor = device.getUsbDeviceDescriptor();
        System.out.println(indent() + "VendorId: " + String.format("%04x", descriptor.idVendor()));
        System.out.println(indent() + "ProductId: " + String.format("%04x", descriptor.idProduct()));
    }

    public static void main(final String args[]) throws Exception
    {
        final UsbServices services = UsbHostManager.getUsbServices();

//        System.out.println("Implementation Description: ");
//        System.out.println(services.getImpDescription());
//        System.out.println("Implementation version: " + services.getImpVersion());
//        System.out.println("API version: " + services.getApiVersion());
        final UsbHub rootUsbHub = services.getRootUsbHub();
        if (rootUsbHub != null)
        {
            System.out.println("Root hub:");
            //indentLevel++;
            System.out.println("POrts:" + rootUsbHub.getNumberOfPorts());
            for (final Object item: rootUsbHub.getUsbPorts())
            {
                final UsbPort port = (UsbPort) item;
                System.out.println(port.getUsbDevice().getSerialNumberString());
                System.out.println(port.getPortNumber());
            }
            //dumpHub(rootUsbHub);
            //indentLevel--;
        }

        /*
        services.addUsbServicesListener(new UsbServicesListener()
        {

            @Override
            public void usbDeviceDetached(final UsbServicesEvent event)
            {
                try
                {
                    System.out.println("Detached: " +event.getUsbDevice().getProductString());
                }
                catch (final Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void usbDeviceAttached(final UsbServicesEvent event)
            {
                try
                {
                    System.out.println("Attached: " +event.getUsbDevice().getProductString());
                }
                catch (final Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
        */

//        while (true)
//        {
//            Thread.sleep(100);
//        }
    }
}
