/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.junit.Test;
import org.usb4java.BosDescriptor;
import org.usb4java.BosDevCapabilityDescriptor;
import org.usb4java.ConfigDescriptor;
import org.usb4java.ContainerIdDescriptor;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.EndpointDescriptor;
import org.usb4java.LibUsb;
import org.usb4java.PollfdListener;
import org.usb4java.SsEndpointCompanionDescriptor;
import org.usb4java.SsUsbDeviceCapabilityDescriptor;
import org.usb4java.Transfer;
import org.usb4java.Usb20ExtensionDescriptor;
import org.usb4java.Version;
import org.usb4java.mocks.PollfdListenerMock;

/**
 * Tests the {@link LibUsb} class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUsbTest
{
    /**
     * Tests the constant values.
     */
    @Test
    public void testConstants()
    {
        // Log levels
        assertEquals(0, LibUsb.LOG_LEVEL_NONE);
        assertEquals(1, LibUsb.LOG_LEVEL_ERROR);
        assertEquals(2, LibUsb.LOG_LEVEL_WARNING);
        assertEquals(3, LibUsb.LOG_LEVEL_INFO);
        assertEquals(4, LibUsb.LOG_LEVEL_DEBUG);

        // Speed codes
        assertEquals(0, LibUsb.SPEED_UNKNOWN);
        assertEquals(1, LibUsb.SPEED_LOW);
        assertEquals(2, LibUsb.SPEED_FULL);
        assertEquals(3, LibUsb.SPEED_HIGH);
        assertEquals(4, LibUsb.SPEED_SUPER);

        // Standard requests
        assertEquals(0x00, LibUsb.REQUEST_GET_STATUS);
        assertEquals(0x01, LibUsb.REQUEST_CLEAR_FEATURE);
        assertEquals(0x03, LibUsb.REQUEST_SET_FEATURE);
        assertEquals(0x05, LibUsb.REQUEST_SET_ADDRESS);
        assertEquals(0x06, LibUsb.REQUEST_GET_DESCRIPTOR);
        assertEquals(0x07, LibUsb.REQUEST_SET_DESCRIPTOR);
        assertEquals(0x08, LibUsb.REQUEST_GET_CONFIGURATION);
        assertEquals(0x09, LibUsb.REQUEST_SET_CONFIGURATION);
        assertEquals(0x0A, LibUsb.REQUEST_GET_INTERFACE);
        assertEquals(0x0B, LibUsb.REQUEST_SET_INTERFACE);
        assertEquals(0x0C, LibUsb.REQUEST_SYNCH_FRAME);
        assertEquals(0x30, LibUsb.REQUEST_SET_SEL);
        assertEquals(0x31, LibUsb.SET_ISOCH_DELAY);

        // Request type
        assertEquals(0x00 << 5, LibUsb.REQUEST_TYPE_STANDARD);
        assertEquals(0x01 << 5, LibUsb.REQUEST_TYPE_CLASS);
        assertEquals(0x02 << 5, LibUsb.REQUEST_TYPE_VENDOR);
        assertEquals(0x03 << 5, LibUsb.REQUEST_TYPE_RESERVED);

        // Recipient bits
        assertEquals(0x00, LibUsb.RECIPIENT_DEVICE);
        assertEquals(0x01, LibUsb.RECIPIENT_INTERFACE);
        assertEquals(0x02, LibUsb.RECIPIENT_ENDPOINT);
        assertEquals(0x03, LibUsb.RECIPIENT_OTHER);

        // Error codes
        assertEquals(0, LibUsb.SUCCESS);
        assertEquals(-1, LibUsb.ERROR_IO);
        assertEquals(-2, LibUsb.ERROR_INVALID_PARAM);
        assertEquals(-3, LibUsb.ERROR_ACCESS);
        assertEquals(-4, LibUsb.ERROR_NO_DEVICE);
        assertEquals(-5, LibUsb.ERROR_NOT_FOUND);
        assertEquals(-6, LibUsb.ERROR_BUSY);
        assertEquals(-7, LibUsb.ERROR_TIMEOUT);
        assertEquals(-8, LibUsb.ERROR_OVERFLOW);
        assertEquals(-9, LibUsb.ERROR_PIPE);
        assertEquals(-10, LibUsb.ERROR_INTERRUPTED);
        assertEquals(-11, LibUsb.ERROR_NO_MEM);
        assertEquals(-12, LibUsb.ERROR_NOT_SUPPORTED);
        assertEquals(-99, LibUsb.ERROR_OTHER);

        // Capabilities
        assertEquals(0, LibUsb.CAP_HAS_CAPABILITY);

        // Device and/or Interface class codes
        assertEquals(0, LibUsb.CLASS_PER_INTERFACE);
        assertEquals(1, LibUsb.CLASS_AUDIO);
        assertEquals(2, LibUsb.CLASS_COMM);
        assertEquals(3, LibUsb.CLASS_HID);
        assertEquals(5, LibUsb.CLASS_PHYSICAL);
        assertEquals(7, LibUsb.CLASS_PRINTER);
        assertEquals(6, LibUsb.CLASS_PTP);
        assertEquals(6, LibUsb.CLASS_IMAGE);
        assertEquals(8, LibUsb.CLASS_MASS_STORAGE);
        assertEquals(9, LibUsb.CLASS_HUB);
        assertEquals(10, LibUsb.CLASS_DATA);
        assertEquals(0x0B, LibUsb.CLASS_SMART_CARD);
        assertEquals(0x0D, LibUsb.CLASS_CONTENT_SECURITY);
        assertEquals(0x0E, LibUsb.CLASS_VIDEO);
        assertEquals(0x0F, LibUsb.CLASS_PERSONAL_HEALTHCARE);
        assertEquals((byte) 0xDC, LibUsb.CLASS_DIAGNOSTIC_DEVICE);
        assertEquals((byte) 0xE0, LibUsb.CLASS_WIRELESS);
        assertEquals((byte) 0xFE, LibUsb.CLASS_APPLICATION);
        assertEquals((byte) 0xFF, LibUsb.CLASS_VENDOR_SPEC);

        // Descriptor types
        assertEquals(0x01, LibUsb.DT_DEVICE);
        assertEquals(0x02, LibUsb.DT_CONFIG);
        assertEquals(0x03, LibUsb.DT_STRING);
        assertEquals(0x04, LibUsb.DT_INTERFACE);
        assertEquals(0x05, LibUsb.DT_ENDPOINT);
        assertEquals(0x21, LibUsb.DT_HID);
        assertEquals(0x22, LibUsb.DT_REPORT);
        assertEquals(0x23, LibUsb.DT_PHYSICAL);
        assertEquals(0x29, LibUsb.DT_HUB);
        assertEquals(0x2A, LibUsb.DT_SUPERSPEED_HUB);

        // Endpoint direction
        assertEquals((byte) 0x80, LibUsb.ENDPOINT_IN);
        assertEquals(0x00, LibUsb.ENDPOINT_OUT);

        // Transfer types
        assertEquals(0, LibUsb.TRANSFER_TYPE_CONTROL);
        assertEquals(1, LibUsb.TRANSFER_TYPE_ISOCHRONOUS);
        assertEquals(2, LibUsb.TRANSFER_TYPE_BULK);
        assertEquals(3, LibUsb.TRANSFER_TYPE_INTERRUPT);

        // ISO Sync types
        assertEquals(0, LibUsb.ISO_SYNC_TYPE_NONE);
        assertEquals(1, LibUsb.ISO_SYNC_TYPE_ASYNC);
        assertEquals(2, LibUsb.ISO_SYNC_TYPE_ADAPTIVE);
        assertEquals(3, LibUsb.ISO_SYNC_TYPE_SYNC);

        // ISO usage types
        assertEquals(0, LibUsb.ISO_USAGE_TYPE_DATA);
        assertEquals(1, LibUsb.ISO_USAGE_TYPE_FEEDBACK);
        assertEquals(2, LibUsb.ISO_USAGE_TYPE_IMPLICIT);
    }

    /**
     * Tests the {@link LibUsb#getVersion()} method.
     */
    @Test
    public void testGetVersion()
    {
        assumeUsbTestsEnabled();
        final Version version = LibUsb.getVersion();
        assertNotNull(version);
        assertEquals(1, version.major());
        assertEquals(0, version.minor());
        assertTrue((version.micro() > 0) && (version.micro() < 100));
        assertNotNull(version.rc());
        assertTrue(version.toString().startsWith("1.0."));
    }

    /**
     * Tests the {@link LibUsb#getApiVersion()} method.
     */
    @Test
    public void testGetApiVersion()
    {
        assumeUsbTestsEnabled();
        assertTrue(LibUsb.getApiVersion() >= 0x1000102);
    }

    /**
     * Tests the initialization and deinitialization of libusb with default
     * context.
     */
    @Test
    public void testInitDeinitWithDefaultContext()
    {
        assumeUsbTestsEnabled();
        assertEquals(LibUsb.SUCCESS, LibUsb.init(null));
        LibUsb.exit(null);

        try
        {
            // Double-exit should throw exception
            LibUsb.exit(null);
            fail("Double-exit should throw IllegalStateException");
        }
        catch (final IllegalStateException e)
        {
            // Expected behavior
        }
    }

    /**
     * Tests the initialization and deinitialization of libusb with a custom USB
     * context.
     */
    @Test
    public void testInitDeinitWithContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        assertEquals(LibUsb.SUCCESS, LibUsb.init(context));
        LibUsb.exit(context);

        try
        {
            LibUsb.exit(context);
            fail("Double-exit should throw IllegalStateException");
        }
        catch (final IllegalStateException e)
        {
            // Expected behavior
        }
    }

    /**
     * Tests {@link LibUsb#exit(Context)} method with uninitialized Context
     */
    @Test(expected = IllegalStateException.class)
    public void testExitWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.exit(context);
    }

    /**
     * Tests {@link LibUsb#setDebug(Context, int)} method with uninitialized USB
     * context
     */
    @Test(expected = IllegalStateException.class)
    public void testSetDebugWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.setDebug(context, 0);
    }

    /**
     * Tests {@link LibUsb#getDeviceList(Context, DeviceList)} method with
     * uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDeviceListWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.getDeviceList(context, new DeviceList());
    }

    /**
     * Tests {@link LibUsb#freeDeviceList(DeviceList, boolean)} method with
     * uninitialized list.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeDeviceListWithUninitializedList()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeDeviceList(new DeviceList(), true);
    }

    /**
     * Tests {@link LibUsb#freeDeviceList(DeviceList, boolean)} method without
     * list.
     */
    @Test
    public void testFreeDeviceListWithoutList()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeDeviceList(null, true);
    }

    /**
     * Tests the {@link LibUsb#getBusNumber(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBusNumberWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getBusNumber(null);
    }

    /**
     * Tests the {@link LibUsb#getBusNumber(Device)} method with uninitialized
     * device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetBusNumberWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getBusNumber(new Device());
    }

    /**
     * Tests the {@link LibUsb#getPortNumber(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPortNumberWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getPortNumber(null);
    }

    /**
     * Tests the {@link LibUsb#getPortNumber(Device)} method with uninitialized
     * device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetPortNumberWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getPortNumber(new Device());
    }

    /**
     * Tests the {@link LibUsb#getParent(Device)} method with uninitialized
     * device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetParentWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getParent(new Device());
    }

    /**
     * Tests the {@link LibUsb#getDeviceAddress(Device)} method with
     * uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDeviceAddressWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceAddress(new Device());
    }

    /**
     * Tests the {@link LibUsb#getDeviceSpeed(Device)} method with uninitialized
     * device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDeviceDeviceSpeedWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceSpeed(new Device());
    }

    /**
     * Tests the {@link LibUsb#getMaxPacketSize(Device, byte)} method with
     * uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testMaxPacketSizeWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getMaxPacketSize(new Device(), (byte) 0);
    }

    /**
     * Tests the {@link LibUsb#getMaxIsoPacketSize(Device, byte)} method with
     * uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testMaxIsoPacketSizeWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getMaxIsoPacketSize(new Device(), (byte) 0);
    }

    /**
     * Tests the {@link LibUsb#refDevice(Device)} method with uninitialized
     * device.
     */
    @Test(expected = IllegalStateException.class)
    public void testRefDeviceWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.refDevice(new Device());
    }

    /**
     * Tests the {@link LibUsb#unrefDevice(Device)} method with uninitialized
     * device.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnrefDeviceWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.unrefDevice(new Device());
    }

    /**
     * Tests the {@link LibUsb#open(Device, DeviceHandle)} method with
     * uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testOpenWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.open(new Device(), new DeviceHandle());
    }

    /**
     * Tests the {@link LibUsb#close(DeviceHandle)} method with uninitialized
     * device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.close(new DeviceHandle());
    }

    /**
     * Tests the {@link LibUsb#getDevice(DeviceHandle)} method with
     * uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDeviceWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDevice(new DeviceHandle());
    }

    /**
     * Tests the {@link LibUsb#getConfiguration(DeviceHandle, IntBuffer)} method
     * with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetConfigurationWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfiguration(new DeviceHandle(),
            BufferUtils.allocateIntBuffer());
    }

    /**
     * Tests the {@link LibUsb#setConfiguration(DeviceHandle, int)} method with
     * uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetConfigurationWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.setConfiguration(new DeviceHandle(), 0);
    }

    /**
     * Tests the {@link LibUsb#claimInterface(DeviceHandle, int)} method with
     * uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testClaimInterfaceWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.claimInterface(new DeviceHandle(), 0);
    }

    /**
     * Tests the {@link LibUsb#releaseInterface(DeviceHandle, int)} method with
     * uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testReleaseInterfaceWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.releaseInterface(new DeviceHandle(), 0);
    }

    /**
     * Tests the {@link LibUsb#setInterfaceAltSetting(DeviceHandle, int, int)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetInterfaceAltSettingWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.setInterfaceAltSetting(new DeviceHandle(), 0, 0);
    }

    /**
     * Tests the {@link LibUsb#clearHalt(DeviceHandle, byte)} method with
     * uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testClearHaltWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.clearHalt(new DeviceHandle(), (byte) 0);
    }

    /**
     * Tests the {@link LibUsb#resetDevice(DeviceHandle)} method with
     * uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testResetDeviceWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.resetDevice(new DeviceHandle());
    }

    /**
     * Tests the {@link LibUsb#kernelDriverActive(DeviceHandle, int)} method
     * with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testKernelDriverActiveWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.kernelDriverActive(new DeviceHandle(), 0);
    }

    /**
     * Tests the {@link LibUsb#detachKernelDriver(DeviceHandle, int)} method
     * with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testDetachKernelDriverWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.detachKernelDriver(new DeviceHandle(), 0);
    }

    /**
     * Tests the {@link LibUsb#attachKernelDriver(DeviceHandle, int)} method
     * with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testAttachKernelDriverWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.attachKernelDriver(new DeviceHandle(), 0);
    }

    /**
     * Tests the {@link LibUsb#setAutoDetachKernelDriver(DeviceHandle, boolean)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetAutoDetachKernelDriverWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.setAutoDetachKernelDriver(new DeviceHandle(), true);
    }

    /**
     * Tests the {@link LibUsb#setAutoDetachKernelDriver(DeviceHandle, boolean)}
     * method without a device handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetAutoDetachKernelDriverWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.setAutoDetachKernelDriver(null, true);
    }

    /**
     * Tests the {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method with uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDeviceDescriptorWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceDescriptor(new Device(), new DeviceDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getStringDescriptorAscii(DeviceHandle, byte, StringBuffer)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetStringDescriptorAsciiWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getStringDescriptorAscii(new DeviceHandle(), (byte) 0,
            new StringBuffer());
    }

    /**
     * Tests the
     * {@link LibUsb#getActiveConfigDescriptor(Device, ConfigDescriptor)} method
     * with uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetActiveConfigDescriptorWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getActiveConfigDescriptor(new Device(), new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptor(Device, byte, ConfigDescriptor)} method
     * with uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetConfigDescriptorWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfigDescriptor(new Device(), (byte) 0,
            new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptorByValue(Device, byte, ConfigDescriptor)}
     * method with uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetConfigDescriptorByValueWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfigDescriptorByValue(new Device(), (byte) 0,
            new ConfigDescriptor());
    }

    /**
     * Tests the {@link LibUsb#freeConfigDescriptor(ConfigDescriptor)} method
     * with uninitialized descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeConfigDescriptorWithUninitializedDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeConfigDescriptor(new ConfigDescriptor());
    }

    /**
     * Tests the {@link LibUsb#freeConfigDescriptor(ConfigDescriptor)} method
     * with null parameter. Must do nothing.
     */
    @Test
    public void testFreeConfigDescriptorWithNull()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeConfigDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getSsEndpointCompanionDescriptor(Context, EndpointDescriptor, SsEndpointCompanionDescriptor)}
     * method with uninitialized endpoint.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSsEndpointCompanionDescriptorWithUninitializedEndpoint()
    {
        assumeUsbTestsEnabled();
        LibUsb.getSsEndpointCompanionDescriptor(null, new EndpointDescriptor(),
            new SsEndpointCompanionDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getSsEndpointCompanionDescriptor(Context, EndpointDescriptor, SsEndpointCompanionDescriptor)}
     * method without descriptors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSsEndpointCompanionDescriptorWithoutDescriptors()
    {
        assumeUsbTestsEnabled();
        LibUsb.getSsEndpointCompanionDescriptor(null, null, null);
    }

    /**
     * Tests the
     * {@link LibUsb#freeSsEndpointCompanionDescriptor(SsEndpointCompanionDescriptor)}
     * method with uninitialized descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeSsEndpointCompanionDescriptorWithUninitializedDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb
            .freeSsEndpointCompanionDescriptor(new SsEndpointCompanionDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#freeSsEndpointCompanionDescriptor(SsEndpointCompanionDescriptor)}
     * method with null parameter. Must do nothing.
     */
    @Test
    public void testFreeSsEndpointCompanionDescriptorWithNull()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeSsEndpointCompanionDescriptor(null);
    }

    /**
     * Tests the {@link LibUsb#getBosDescriptor(DeviceHandle, BosDescriptor)}
     * method with uninitialized handled.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetBosDescriptorWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getBosDescriptor(new DeviceHandle(), new BosDescriptor());
    }

    /**
     * Tests the {@link LibUsb#getBosDescriptor(DeviceHandle, BosDescriptor)}
     * method without handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBosDescriptorWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getBosDescriptor(null, new BosDescriptor());
    }

    /**
     * Tests the {@link LibUsb#freeBosDescriptor(BosDescriptor)} method with
     * uninitialized descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeBosDescriptorWithUninitializedDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeBosDescriptor(new BosDescriptor());
    }

    /**
     * Tests the {@link LibUsb#freeBosDescriptor(BosDescriptor)} method with
     * null parameter. Must do nothing.
     */
    @Test
    public void testFreeBosDescriptorWithNull()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeBosDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getUsb20ExtensionDescriptor(Context, BosDevCapabilityDescriptor, Usb20ExtensionDescriptor)}
     * method with uninitialized device capability descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetUsb20ExtensionDescriptorWithUninitializedEndpoint()
    {
        assumeUsbTestsEnabled();
        LibUsb.getUsb20ExtensionDescriptor(null,
            new BosDevCapabilityDescriptor(), new Usb20ExtensionDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getUsb20ExtensionDescriptor(Context, BosDevCapabilityDescriptor, Usb20ExtensionDescriptor)}
     * method without descriptors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUsb20ExtensionDescriptorWithoutDescriptors()
    {
        assumeUsbTestsEnabled();
        LibUsb.getUsb20ExtensionDescriptor(null, null, null);
    }

    /**
     * Tests the
     * {@link LibUsb#freeUsb20ExtensionDescriptor(Usb20ExtensionDescriptor)}
     * method with uninitialized descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeUsb20ExtensionDescriptorWithUninitializedDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeUsb20ExtensionDescriptor(new Usb20ExtensionDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#freeUsb20ExtensionDescriptor(Usb20ExtensionDescriptor)}
     * method with null parameter. Must do nothing.
     */
    @Test
    public void testFreeUsb20ExtensionDescriptorWithNull()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeUsb20ExtensionDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getSsUsbDeviceCapabilityDescriptor(Context, BosDevCapabilityDescriptor, SsUsbDeviceCapabilityDescriptor)}
     * method with uninitialized device capability descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSsUsbDeviceCapabilityDescriptorWithUninitializedEndpoint()
    {
        assumeUsbTestsEnabled();
        LibUsb.getSsUsbDeviceCapabilityDescriptor(null,
            new BosDevCapabilityDescriptor(),
            new SsUsbDeviceCapabilityDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getSsUsbDeviceCapabilityDescriptor(Context, BosDevCapabilityDescriptor, SsUsbDeviceCapabilityDescriptor)}
     * method without descriptors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSsUsbDeviceCapabilityDescriptorWithoutDescriptors()
    {
        assumeUsbTestsEnabled();
        LibUsb.getSsUsbDeviceCapabilityDescriptor(null, null, null);
    }

    /**
     * Tests the
     * {@link LibUsb#freeSsUsbDeviceCapabilityDescriptor(SsUsbDeviceCapabilityDescriptor)}
     * method with uninitialized descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeSsUsbDeviceCapabilityDescriptorWithUninitializedDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb
            .freeSsUsbDeviceCapabilityDescriptor(new SsUsbDeviceCapabilityDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#freeSsUsbDeviceCapabilityDescriptor(SsUsbDeviceCapabilityDescriptor)}
     * method with null parameter. Must do nothing.
     */
    @Test
    public void testFreeSsUsbDeviceCapabilityDescriptorWithNull()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeSsUsbDeviceCapabilityDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getContainerIdDescriptor(Context, BosDevCapabilityDescriptor, ContainerIdDescriptor)}
     * method with uninitialized device capability descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetContainerIdDescriptorWithUninitializedEndpoint()
    {
        assumeUsbTestsEnabled();
        LibUsb.getContainerIdDescriptor(null, new BosDevCapabilityDescriptor(),
            new ContainerIdDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getContainerIdDescriptor(Context, BosDevCapabilityDescriptor, ContainerIdDescriptor)}
     * method without descriptors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetContainerIdDescriptorWithoutDescriptors()
    {
        assumeUsbTestsEnabled();
        LibUsb.getContainerIdDescriptor(null, null, null);
    }

    /**
     * Tests the {@link LibUsb#freeContainerIdDescriptor(ContainerIdDescriptor)}
     * method with uninitialized descriptor.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeContainerIdDescriptorWithUninitializedDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeContainerIdDescriptor(new ContainerIdDescriptor());
    }

    /**
     * Tests the {@link LibUsb#freeContainerIdDescriptor(ContainerIdDescriptor)}
     * method with null parameter. Must do nothing.
     */
    @Test
    public void testFreeContainerIdDescriptorWithNull()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeContainerIdDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getDescriptor(DeviceHandle, byte, byte, ByteBuffer)} method
     * with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDescriptorWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDescriptor(new DeviceHandle(), (byte) 0, (byte) 0,
            ByteBuffer.allocateDirect(1));
    }

    /**
     * Tests the
     * {@link LibUsb#getStringDescriptor(DeviceHandle, byte, short, ByteBuffer)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetStringDescriptorWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getStringDescriptor(new DeviceHandle(), (byte) 0, (short) 0,
            ByteBuffer.allocateDirect(1));
    }

    /**
     * Tests the
     * {@link LibUsb#controlTransfer(DeviceHandle, byte, byte, short, short, ByteBuffer, long)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testControlTransferWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.controlTransfer(new DeviceHandle(), (byte) 0, (byte) 0,
            (short) 0, (short) 0, ByteBuffer.allocateDirect(1), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#bulkTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testBulkTransferWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.bulkTransfer(new DeviceHandle(), (byte) 0,
            ByteBuffer.allocateDirect(1), BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#interruptTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method with uninitialized device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testInterruptTransferWithUninitializedHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.interruptTransfer(new DeviceHandle(), (byte) 0,
            ByteBuffer.allocateDirect(1), BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the {@link LibUsb#freeTransfer(Transfer)} method with uninitialized
     * device handle.
     */
    @Test(expected = IllegalStateException.class)
    public void testFreeTransferWithUninitializedTransfer()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeTransfer(new Transfer());
    }

    /**
     * Tests {@link LibUsb#openDeviceWithVidPid(Context, short, short)} with
     * uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testOpenDeviceWithVidPid()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.openDeviceWithVidPid(context, (short) 0, (short) 0);
    }

    /**
     * Tests {@link LibUsb#tryLockEvents(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testTryLockEventsWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.tryLockEvents(context);
    }

    /**
     * Tests {@link LibUsb#lockEvents(Context)} with uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testLockEventsWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.lockEvents(context);
    }

    /**
     * Tests {@link LibUsb#unlockEvents(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnlockEventsWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.unlockEvents(context);
    }

    /**
     * Tests {@link LibUsb#eventHandlingOk(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testEventHandlingOkWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.eventHandlingOk(context);
    }

    /**
     * Tests {@link LibUsb#eventHandlerActive(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testEventHandlerActiveWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.eventHandlerActive(context);
    }

    /**
     * Tests {@link LibUsb#lockEventWaiters(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testLockEventWaitersWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.lockEventWaiters(context);
    }

    /**
     * Tests {@link LibUsb#unlockEventWaiters(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnlockEventWaitersWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.unlockEventWaiters(context);
    }

    /**
     * Tests {@link LibUsb#waitForEvent(Context, long)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testWaitForEventWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.waitForEvent(context, 53);
    }

    /**
     * Tests
     * {@link LibUsb#handleEventsTimeoutCompleted(Context, long, IntBuffer)}
     * with uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testHandleEventsTimeoutCompletedWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.handleEventsTimeoutCompleted(context, 53,
            BufferUtils.allocateIntBuffer());
    }

    /**
     * Tests {@link LibUsb#handleEventsTimeout(Context, long)} with
     * uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testHandleEventsTimeoutWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.handleEventsTimeout(context, 53);
    }

    /**
     * Tests {@link LibUsb#handleEvents(Context)} with uninitialized USB
     * context.
     */
    @Test(expected = IllegalStateException.class)
    public void testHandleEventsWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.handleEvents(context);
    }

    /**
     * Tests {@link LibUsb#handleEventsCompleted(Context, IntBuffer)} with
     * uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testHandleEventsCompletedWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.handleEventsCompleted(context, BufferUtils.allocateIntBuffer());
    }

    /**
     * Tests {@link LibUsb#handleEventsLocked(Context, long)} with uninitialized
     * USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testHandleEventsLockedWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.handleEventsLocked(context, 53);
    }

    /**
     * Tests {@link LibUsb#pollfdsHandleTimeouts(Context)} with uninitialized
     * USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testPollfdsHandleTimeoutsWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.pollfdsHandleTimeouts(context);
    }

    /**
     * Tests {@link LibUsb#getNextTimeout(Context, LongBuffer)} with
     * uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetNextTimeoutWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.getNextTimeout(context, BufferUtils.allocateLongBuffer());
    }

    /**
     * Tests {@link LibUsb#setPollfdNotifiersNative(Context, long)}
     * with uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetPollfdNotifiersWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.setPollfdNotifiersNative(context, context.getPointer());
    }

    /**
     * Tests {@link LibUsb#unsetPollfdNotifiersNative(Context)} with
     * uninitialized USB context.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsetPollfdNotifiersWithUninitializedContext()
    {
        assumeUsbTestsEnabled();
        final Context context = new Context();
        LibUsb.unsetPollfdNotifiersNative(context);
    }

    /**
     * Tests the
     * {@link LibUsb#setPollfdNotifiers(Context, PollfdListener, Object)}
     * method.
     */
    @Test
    public void testPollFdNotifiers()
    {
        assumeUsbTestsEnabled();
        final PollfdListenerMock listener = new PollfdListenerMock();
        final Context context = new Context();
        LibUsb.init(context);
        LibUsb.setPollfdNotifiers(context, listener, "test");

        FileDescriptor fd = new FileDescriptor();
        LibUsb.triggerPollfdAdded(fd, 53, context.getPointer());
        assertEquals(53, listener.addedEvents);
        assertSame(fd, listener.addedFd);
        assertSame("test", listener.addedUserData);
        assertNull(listener.removedFd);
        assertNull(listener.removedUserData);

        listener.reset();

        fd = new FileDescriptor();
        LibUsb.triggerPollfdRemoved(fd, context.getPointer());
        assertEquals(0, listener.addedEvents);
        assertNull(listener.addedFd);
        assertNull(listener.addedUserData);
        assertSame(fd, listener.removedFd);
        assertSame("test", listener.removedUserData);

        LibUsb.setPollfdNotifiers(context, null, null);
        listener.reset();

        fd = new FileDescriptor();
        LibUsb.triggerPollfdAdded(fd, 53, context.getPointer());
        assertEquals(0, listener.addedEvents);
        assertNull(listener.addedFd);
        assertNull(listener.addedUserData);
        assertNull(listener.removedFd);
        assertNull(listener.removedUserData);

        listener.reset();

        fd = new FileDescriptor();
        LibUsb.triggerPollfdRemoved(fd, context.getPointer());
        assertEquals(0, listener.addedEvents);
        assertNull(listener.addedFd);
        assertNull(listener.addedUserData);
        assertNull(listener.removedFd);
        assertNull(listener.removedUserData);
    }
}
