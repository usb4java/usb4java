/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.utils;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

import org.junit.Test;

import de.ailis.usb4java.descriptors.SimpleUsbConfigurationDescriptor;
import de.ailis.usb4java.descriptors.SimpleUsbDeviceDescriptor;
import de.ailis.usb4java.descriptors.SimpleUsbEndpointDescriptor;
import de.ailis.usb4java.descriptors.SimpleUsbInterfaceDescriptor;
import de.ailis.usb4java.libusb.LibUsb;

/**
 * Tests the {@link DescriptorUtils} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class DescriptorUtilsTest
{
    /**
     * Tests the {@link DescriptorUtils#decodeBCD(short)} method.
     */
    @Test
    public void testDecodeBCD()
    {
        assertEquals("10.20", DescriptorUtils.decodeBCD((short) 0x1020));
    }

    /**
     * Tests the {@link DescriptorUtils#getSpeedName(int)} method.
     */
    @Test
    public void testGetSpeedName()
    {
        assertEquals("Super", DescriptorUtils.getSpeedName(LibUsb.SPEED_SUPER));
        assertEquals("Full", DescriptorUtils.getSpeedName(LibUsb.SPEED_FULL));
        assertEquals("High", DescriptorUtils.getSpeedName(LibUsb.SPEED_HIGH));
        assertEquals("Low", DescriptorUtils.getSpeedName(LibUsb.SPEED_LOW));
        assertEquals("Unknown",
            DescriptorUtils.getSpeedName(LibUsb.SPEED_UNKNOWN));
    }

    /**
     * Tests the {@link DescriptorUtils#getSynchTypeName(byte)} method.
     */
    @Test
    public void testGetSynchTypeName()
    {
        assertEquals("None", DescriptorUtils.getSynchTypeName((byte) 0));
        assertEquals("Asynchronous", DescriptorUtils.getSynchTypeName((byte) 4));
        assertEquals("Adaptive", DescriptorUtils.getSynchTypeName((byte) 8));
        assertEquals("Synchronous", DescriptorUtils.getSynchTypeName((byte) 12));
    }

    /**
     * Tests the {@link DescriptorUtils#getUsageTypeName(byte)} method.
     */
    @Test
    public void testGetUsageTypeName()
    {
        assertEquals("Data", DescriptorUtils.getUsageTypeName((byte) 0));
        assertEquals("Feedback", DescriptorUtils.getUsageTypeName((byte) 16));
        assertEquals("Implicit Feedback Data",
            DescriptorUtils.getUsageTypeName((byte) 32));
        assertEquals("Reserved", DescriptorUtils.getUsageTypeName((byte) 48));
    }


    /**
     * Tests the {@link DescriptorUtils#getTransferTypeName(byte)} method.
     */
    @Test
    public void testGetTransferTypeName()
    {
        assertEquals("Control", DescriptorUtils.getTransferTypeName((byte) 0));
        assertEquals("Isochronous", DescriptorUtils.getTransferTypeName((byte) 1));
        assertEquals("Bulk", DescriptorUtils.getTransferTypeName((byte) 2));
        assertEquals("Interrupt", DescriptorUtils.getTransferTypeName((byte) 3));
    }

    /**
     * Tests the {@link DescriptorUtils#getUSBClassName(byte)} method.
     */
    @Test
    public void testGetUSBClassName()
    {
        assertEquals("Audio",
            DescriptorUtils.getUSBClassName(LibUsb.CLASS_AUDIO));
        assertEquals("Unknown", DescriptorUtils.getUSBClassName((byte) 0xF3));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(java.nio.ByteBuffer)} method.
     */
    @Test
    public void testDumpByteBuffer()
    {
        ByteBuffer bytes = ByteBuffer.allocate(0);
        assertEquals("", DescriptorUtils.dump(bytes));

        bytes = ByteBuffer.allocate(8);
        bytes.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals("01 02 03 04 05 06 07 08", DescriptorUtils.dump(bytes));

        bytes = ByteBuffer.allocate(16);
        bytes.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16 });
        assertEquals("01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10",
            DescriptorUtils.dump(bytes));

        bytes = ByteBuffer.allocate(20);
        bytes.put(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20 });
        assertEquals("01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10\n" +
            "11 12 13 14", DescriptorUtils.dump(bytes));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(javax.usb.UsbDeviceDescriptor)}
     * method.
     */
    @Test
    public void testDumpUsbDeviceDescriptor()
    {
        assertEquals("Device Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  bcdUSB                0.02\n"
            + "  bDeviceClass             3 HID\n"
            + "  bDeviceSubClass          4\n"
            + "  bDeviceProtocol          5\n"
            + "  bMaxPacketSize0          6\n"
            + "  idVendor            0x0007\n"
            + "  idProduct           0x0008\n"
            + "  bcdDevice             0.09\n"
            + "  iManufacturer           10\n"
            + "  iProduct                11\n"
            + "  iSerial                 12\n"
            + "  bNumConfigurations      13",
            DescriptorUtils.dump(new SimpleUsbDeviceDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12,
                (byte) 13)));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(javax.usb.UsbDeviceDescriptor)}
     * method.
     */
    @Test
    public void testDumpUsbDeviceDescriptorWithNames()
    {
        assertEquals("Device Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  bcdUSB                0.02\n"
            + "  bDeviceClass             3 HID\n"
            + "  bDeviceSubClass          4\n"
            + "  bDeviceProtocol          5\n"
            + "  bMaxPacketSize0          6\n"
            + "  idVendor            0x0007\n"
            + "  idProduct           0x0008\n"
            + "  bcdDevice             0.09\n"
            + "  iManufacturer           10 Manufacturer\n"
            + "  iProduct                11 Product\n"
            + "  iSerial                 12 Serial\n"
            + "  bNumConfigurations      13",
            DescriptorUtils.dump(new SimpleUsbDeviceDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12,
                (byte) 13), "Manufacturer", "Product", "Serial"));
    }

    /**
     * Tests the
     * {@link DescriptorUtils#dump(javax.usb.UsbConfigurationDescriptor)}
     * method.
     */
    @Test
    public void testDumpUsbConfigurationDescriptor()
    {
        assertEquals("Configuration Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  wTotalLength             2\n"
            + "  bNumInterfaces           3\n"
            + "  bConfigurationValue      4\n"
            + "  iConfiguration           5\n"
            + "  bmAttributes          0x06\n"
            + "    (Bus Powered)\n"
            + "  bMaxPower               14mA",
            DescriptorUtils.dump(new SimpleUsbConfigurationDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7)));
    }

    /**
     * Tests the
     * {@link DescriptorUtils#dump(javax.usb.UsbConfigurationDescriptor)}
     * method with self-powered attribute.
     */
    @Test
    public void testDumpUsbConfigurationDescriptorWithSelfPowering()
    {
        assertEquals("Configuration Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  wTotalLength             2\n"
            + "  bNumInterfaces           3\n"
            + "  bConfigurationValue      4\n"
            + "  iConfiguration           5\n"
            + "  bmAttributes          0x40\n"
            + "    Self Powered\n"
            + "  bMaxPower               14mA",
            DescriptorUtils.dump(new SimpleUsbConfigurationDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 0x40,
                (byte) 7)));
    }

    /**
     * Tests the
     * {@link DescriptorUtils#dump(javax.usb.UsbConfigurationDescriptor)}
     * method with remote wakup attribute.
     */
    @Test
    public void testDumpUsbConfigurationDescriptorWithRemoteWakeup()
    {
        assertEquals("Configuration Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  wTotalLength             2\n"
            + "  bNumInterfaces           3\n"
            + "  bConfigurationValue      4\n"
            + "  iConfiguration           5\n"
            + "  bmAttributes          0x20\n"
            + "    (Bus Powered)\n"
            + "    Remote Wakeup\n"
            + "  bMaxPower               14mA",
            DescriptorUtils.dump(new SimpleUsbConfigurationDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 0x20,
                (byte) 7)));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(javax.usb.UsbInterfaceDescriptor)}
     * method.
     */
    @Test
    public void testDumpUsbInterfaceDescriptor()
    {
        assertEquals("Interface Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  bInterfaceNumber         2\n"
            + "  bAlternateSetting        3\n"
            + "  bNumEndpoints            4\n"
            + "  bInterfaceClass          5 Physical\n"
            + "  bInterfaceSubClass       6\n"
            + "  bInterfaceProtocol       7\n"
            + "  iInterface               8",
            DescriptorUtils.dump(new SimpleUsbInterfaceDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7, (byte) 8)));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(javax.usb.UsbEndpointDescriptor)}
     * method.
     */
    @Test
    public void testDumpUsbEndpointDescriptor()
    {
        assertEquals("Endpoint Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  bEndpointAddress      0x02  EP 2 OUT\n"
            + "  bmAttributes             3\n"
            + "    Transfer Type             Interrupt\n"
            + "    Synch Type                None\n"
            + "    Usage Type                Data\n"
            + "  wMaxPacketSize           4\n"
            + "  bInterval                5",
            DescriptorUtils.dump(new SimpleUsbEndpointDescriptor((byte) 0,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5)));
    }

    /**
     * Tests the {@link DescriptorUtils#dump(javax.usb.UsbEndpointDescriptor)}
     * method with IN endpoint.
     */
    @Test
    public void testDumpUsbEndpointDescriptorWithIN()
    {
        assertEquals("Endpoint Descriptor:\n"
            + "  bLength                  0\n"
            + "  bDescriptorType          1\n"
            + "  bEndpointAddress      0x82  EP 2 IN\n"
            + "  bmAttributes             3\n"
            + "    Transfer Type             Interrupt\n"
            + "    Synch Type                None\n"
            + "    Usage Type                Data\n"
            + "  wMaxPacketSize           4\n"
            + "  bInterval                5",
            DescriptorUtils.dump(new SimpleUsbEndpointDescriptor((byte) 0,
                (byte) 1, (byte) 0x82, (byte) 3, (byte) 4, (byte) 5)));
    }

    /**
     * Ensure constructor is private.
     * 
     * @throws Exception
     *             When constructor test fails.
     */
    @Test
    public void testPrivateConstructor() throws Exception
    {
        assertEquals(0, DescriptorUtils.class.getConstructors().length);
        Constructor<?> c = DescriptorUtils.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }
}
