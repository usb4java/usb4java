/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.usb4java.test.UsbAssume.assumeUsbTestsEnabled;
import static org.usb4java.test.UsbAssume.isUsbTestsEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.usb4java.ConfigDescriptor;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.EndpointDescriptor;
import org.usb4java.Interface;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

/**
 * Tests the device-scope methods of the {@link LibUsb} class which need a open
 * USB context and a device to run the tests on.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUsbDeviceTest
{
    /** The libusb contxet. */
    private Context context;

    /** The device to run the tests on. */
    private Device device;

    /** The device endpoint to test with. */
    private byte endpoint;

    /** The value of the active configuration. */
    private byte configValue;

    /** The vendor ID of the device we test. */
    private short vendorId;

    /** The manufacturer ID of the device we test. */
    private short productId;

    /**
     * Set up the test.
     */
    @Before
    public void setUp()
    {
        if (isUsbTestsEnabled())
        {
            this.context = new Context();
            LibUsb.init(this.context);
            try
            {
                this.device = this.findTestDevice();
                if (this.device == null)
                {
                    throw new IllegalStateException("Need at least one USB device "
                        + "with at least one endpoint to execute this test");
                }
            }
            catch (final Throwable e)
            {
                this.device = null;
            }
        }
    }

    /**
     * Finds a test device with at least one endpoint.
     *
     * @return The test device or null if none.
     */
    private Device findTestDevice()
    {
        final DeviceList list = new DeviceList();
        if (LibUsb.getDeviceList(this.context, list) <= 0)
        {
            return null;
        }
        try
        {
            for (final Device device : list)
            {
                final DeviceDescriptor descriptor = new DeviceDescriptor();
                if (LibUsb.getDeviceDescriptor(device, descriptor) != 0)
                {
                    continue;
                }
                this.vendorId = descriptor.idVendor();
                this.productId = descriptor.idProduct();
                final ConfigDescriptor config = new ConfigDescriptor();
                if (LibUsb.getActiveConfigDescriptor(device, config) < 0)
                {
                    return null;
                }
                try
                {
                    this.configValue = config.bConfigurationValue();
                    for (int j = 0; j < config.bNumInterfaces(); j++)
                    {
                        final Interface iface = config.iface()[j];
                        for (int k = 0; k < iface.numAltsetting(); k++)
                        {
                            final InterfaceDescriptor ifaceDescriptor = iface
                                .altsetting()[k];
                            if (ifaceDescriptor.bNumEndpoints() > 1)
                            {
                                this.endpoint = ifaceDescriptor.endpoint()[0]
                                    .bEndpointAddress();
                                return LibUsb.refDevice(device);
                            }
                        }
                    }
                }
                finally
                {
                    LibUsb.freeConfigDescriptor(config);
                }
            }
        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }
        return null;
    }

    /**
     * Tear down the test.
     */
    @After
    public void tearDown()
    {
        if (isUsbTestsEnabled())
        {
            if (this.device != null)
            {
                LibUsb.unrefDevice(this.device);
            }
            if (this.context != null)
            {
                LibUsb.exit(this.context);
            }
        }
    }

    /**
     * Tests the {@link LibUsb#getBusNumber(Device)} method.
     */
    @Test
    public void testGetBusNumber()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertTrue(LibUsb.getBusNumber(this.device) >= 0);
    }

    /**
     * Tests the {@link LibUsb#getPortNumber(Device)} method.
     */
    @Test
    public void testGetPortNumber()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertTrue(LibUsb.getPortNumber(this.device) >= 0);
    }

    /**
     * Tests the {@link LibUsb#getPortNumbers(Device, ByteBuffer)} method.
     */
    @Test
    public void testGetPortNumbers()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final ByteBuffer path = BufferUtils.allocateByteBuffer(8);
        final int result = LibUsb.getPortNumbers(this.device, path);
        assertTrue(result > 0);
        assertTrue(result <= path.capacity());
    }

    /**
     * Tests the {@link LibUsb#getPortNumbers(Device, ByteBuffer)} method with
     * 0-sized path buffer.
     */
    @Test
    public void testGetPortNumbersWithTooSmallBuffer()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final ByteBuffer path = BufferUtils.allocateByteBuffer(0);
        final int result = LibUsb.getPortNumbers(this.device, path);
        assertEquals(LibUsb.ERROR_INVALID_PARAM, result);
    }

    /**
     * Tests the {@link LibUsb#getPortNumbers(Device, ByteBuffer)} method
     * without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPortNumbersWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getPortNumbers(null, BufferUtils.allocateByteBuffer(8));
    }

    /**
     * Tests the {@link LibUsb#getPortNumbers(Device, ByteBuffer)} method
     * without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPortNumbersWithoutBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.getPortNumbers(this.device, null);
    }

    /**
     * Tests {@link LibUsb#getPortNumbers(Device, ByteBuffer)} method with
     * uninitialized device.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetPortNumbersWithUninitializedDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getPortNumbers(new Device(), BufferUtils.allocateByteBuffer(16));
    }

    /**
     * Tests the {@link LibUsb#getParent(Device)} method.
     */
    @Test
    public void testGetParent()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final DeviceList list = new DeviceList();
        LibUsb.getDeviceList(this.context, list);
        try
        {
            final Device parent = LibUsb.getParent(this.device);

            // We cannot test anything else here. Parent can be null if our
            // test device is a root device. We just make sure that it can't
            // be the device itself.
            assertNotEquals(parent, this.device);
        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }
    }

    /**
     * Tests the {@link LibUsb#getParent(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetParentWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getParent(null);
    }

    /**
     * Tests the {@link LibUsb#getDeviceAddress(Device)} method.
     */
    @Test
    public void testGetDeviceAddress()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertTrue(LibUsb.getDeviceAddress(this.device) >= 0);
    }

    /**
     * Tests the {@link LibUsb#getDeviceAddress(Device)} method without a
     * device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceAddressWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceAddress(null);
    }

    /**
     * Tests the {@link LibUsb#getDeviceSpeed(Device)} method.
     */
    @Test
    public void testGetDeviceSpeed()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final int speed = LibUsb.getDeviceSpeed(this.device);
        assertTrue((speed >= LibUsb.SPEED_UNKNOWN)
            && (speed <= LibUsb.SPEED_SUPER));
    }

    /**
     * Tests the {@link LibUsb#getDeviceSpeed(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceSpeedWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceSpeed(null);
    }

    /**
     * Tests the {@link LibUsb#getMaxPacketSize(Device, byte)} method.
     */
    @Test
    public void testGetMaxPacketSizeWithInvalidEndpoint()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertEquals(LibUsb.ERROR_NOT_FOUND,
            LibUsb.getMaxPacketSize(this.device, (byte) 0));
    }

    /**
     * Tests the {@link LibUsb#getMaxPacketSize(Device, byte)} method.
     */
    @Test
    public void testGetMaxPacketSize()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertTrue(LibUsb.getMaxPacketSize(this.device, this.endpoint) > 0);
    }

    /**
     * Tests the {@link LibUsb#getMaxPacketSize(Device, byte)} method without a
     * device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxPacketSizeWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getMaxPacketSize(null, (byte) 0);
    }

    /**
     * Tests the {@link LibUsb#getMaxIsoPacketSize(Device, byte)} method.
     */
    @Test
    public void testGetMaxIsoPacketSizeWithInvalidEndpoint()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertEquals(LibUsb.ERROR_NOT_FOUND,
            LibUsb.getMaxIsoPacketSize(this.device, (byte) 0));
    }

    /**
     * Tests the {@link LibUsb#getMaxIsoPacketSize(Device, byte)} method.
     */
    @Test
    public void testGetMaxIsoPacketSize()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        assertTrue(LibUsb.getMaxIsoPacketSize(this.device, this.endpoint) > 0);
    }

    /**
     * Tests the {@link LibUsb#getMaxIsoPacketSize(Device, byte)} method without
     * a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxIsoPacketSizeWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getMaxIsoPacketSize(null, (byte) 0);
    }

    /**
     * Tests the {@link LibUsb#refDevice(Device)} and
     * {@link LibUsb#unrefDevice(Device)} methods.
     */
    @Test
    public void testRefUnRefDevice()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final Device device = LibUsb.refDevice(this.device);
        try
        {
            assertEquals(this.device, device);
        }
        finally
        {
            LibUsb.unrefDevice(device);
        }
    }

    /**
     * Tests the {@link LibUsb#refDevice(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRefDeviceWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.refDevice(null);
    }

    /**
     * Tests the {@link LibUsb#unrefDevice(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnrefDeviceWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.unrefDevice(null);
    }

    /**
     * Tests the {@link LibUsb#open(Device, DeviceHandle)} method. This can't be
     * really tested in a simple unit test like this. Most likely the user has
     * no access to the device. So this test succeeds on SUCCESS and on
     * ERROR_ACCESS. At least we have to make sure then open() method doesn't
     * crash.
     */
    @Test
    public void testOpenAndClose()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final DeviceHandle handle = new DeviceHandle();
        final int result = LibUsb.open(this.device, handle);
        assertTrue((result == LibUsb.SUCCESS)
            || (result == LibUsb.ERROR_ACCESS));
        if (result == LibUsb.SUCCESS)
        {
            LibUsb.close(handle);

            try
            {
                LibUsb.close(handle);
                fail("Double-close should throw IllegalStateException");
            }
            catch (final IllegalStateException e)
            {
                // Expected behavior
            }
        }
    }

    /**
     * Tests the {@link LibUsb#open(Device, DeviceHandle)} method without a
     * device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutDevice()
    {
        assumeUsbTestsEnabled();
        final DeviceHandle handle = new DeviceHandle();
        LibUsb.open(null, handle);
    }

    /**
     * Tests the {@link LibUsb#open(Device, DeviceHandle)} method without a
     * handle
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.open(this.device, null);
    }

    /**
     * Tests the {@link LibUsb#close(DeviceHandle)} method without a handle
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloseWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.close(null);
    }

    /**
     * Tests the {@link LibUsb#openDeviceWithVidPid(Context, short, short)}
     * method.
     * We can't test anything here because the device most likely can't be
     * opened anyway. We just make sure it does not crash.
     */
    @Test
    public void testOpenDeviceWithVidPid()
    {
        assumeUsbTestsEnabled();
        final DeviceHandle handle = LibUsb.openDeviceWithVidPid(this.context,
            this.vendorId, this.productId);
        if (handle != null)
        {
            LibUsb.close(handle);
        }
    }

    /**
     * Tests the {@link LibUsb#getDevice(DeviceHandle)} method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDevice(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getConfiguration(DeviceHandle, java.nio.IntBuffer)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfiguration(null, BufferUtils.allocateIntBuffer());
    }

    /**
     * Tests the
     * {@link LibUsb#getConfiguration(DeviceHandle, java.nio.IntBuffer)} method
     * without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationWithoutBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfiguration(new DeviceHandle(), null);
    }

    /**
     * Tests the {@link LibUsb#setConfiguration(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetConfigurationWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.setConfiguration(null, 0);
    }

    /**
     * Tests the {@link LibUsb#claimInterface(DeviceHandle, int)} method without
     * a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClaimInterfaceWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.claimInterface(null, 0);
    }

    /**
     * Tests the {@link LibUsb#releaseInterface(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseInterfaceWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.releaseInterface(null, 0);
    }

    /**
     * Tests the {@link LibUsb#setInterfaceAltSetting(DeviceHandle, int, int)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInterfaceAltSettingWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.setInterfaceAltSetting(null, 0, 0);
    }

    /**
     * Tests the {@link LibUsb#clearHalt(DeviceHandle, byte)} method without a
     * handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClearHaltWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.clearHalt(null, (byte) 0);
    }

    /**
     * Tests the {@link LibUsb#resetDevice(DeviceHandle)} method without a
     * handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testResetDeviceWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.resetDevice(null);
    }

    /**
     * Tests the {@link LibUsb#kernelDriverActive(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testKernelDriverActiveWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.kernelDriverActive(null, 0);
    }

    /**
     * Tests the {@link LibUsb#detachKernelDriver(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDetachKernelDriverWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.detachKernelDriver(null, 0);
    }

    /**
     * Tests the {@link LibUsb#attachKernelDriver(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAttachKernelDriverWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.attachKernelDriver(null, 0);
    }

    /**
     * Tests the {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceDescriptorWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceDescriptor(null, new DeviceDescriptor());
    }

    /**
     * Tests the {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceDescriptorWithoutDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDeviceDescriptor(this.device, null);
    }

    /**
     * Tests the {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method.
     *
     * Most descriptor fields can be anything because we are not testing a
     * specific device. We call each method anyway to make sure it is connected
     * to JNI and doesn't crash.
     */
    @Test
    public void testGetDeviceDescriptor()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final DeviceDescriptor desc = new DeviceDescriptor();
        LibUsb.getDeviceDescriptor(this.device, desc);
        desc.bcdDevice();
        desc.bcdUSB();
        assertEquals(LibUsb.DT_DEVICE, desc.bDescriptorType());
        desc.bDeviceClass();
        desc.bDeviceProtocol();
        desc.bDeviceSubClass();
        assertTrue(desc.bLength() > 0);
        desc.bMaxPacketSize0();
        desc.bNumConfigurations();
    }

    /**
     * Tests the
     * {@link LibUsb#getActiveConfigDescriptor(Device, ConfigDescriptor)} method
     * without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetActiveConfigDescriptorWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getActiveConfigDescriptor(null, new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getActiveConfigDescriptor(Device, ConfigDescriptor)} method
     * without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetActiveConfigDescriptorWithoutDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.getActiveConfigDescriptor(this.device, null);
    }

    /**
     * Validates the specified config descriptor.
     *
     * @param desc
     *            The config descriptor to validate.
     */
    private void validateConfigDescriptor(final ConfigDescriptor desc)
    {
        desc.bConfigurationValue();
        assertEquals(LibUsb.DT_CONFIG, desc.bDescriptorType());
        assertTrue(desc.bLength() > 0);
        desc.bmAttributes();
        desc.bMaxPower();
        assertEquals(desc.extraLength(), desc.extra().limit());
        desc.iConfiguration();
        assertEquals(desc.bNumInterfaces(), desc.iface().length);
        assertTrue(desc.wTotalLength() >= desc.bLength());

        for (final Interface iface : desc.iface())
        {
            this.validateInterface(iface);
        }
    }

    /**
     * Validates the specified interface.
     *
     * @param iface
     *            The interface to validate.
     */
    private void validateInterface(final Interface iface)
    {
        assertEquals(iface.numAltsetting(), iface.altsetting().length);
        for (final InterfaceDescriptor desc : iface.altsetting())
        {
            this.validateInterfaceDescriptor(desc);
        }
    }

    /**
     * Validates the specified interface descriptor.
     *
     * @param desc
     *            The interface descriptor to validate.
     */
    private void validateInterfaceDescriptor(final InterfaceDescriptor desc)
    {
        desc.bAlternateSetting();
        assertEquals(LibUsb.DT_INTERFACE, desc.bDescriptorType());
        desc.bInterfaceClass();
        desc.bInterfaceNumber();
        desc.bInterfaceProtocol();
        desc.bInterfaceSubClass();
        assertTrue(desc.bLength() > 0);
        assertEquals(desc.bNumEndpoints(), desc.endpoint().length);
        assertEquals(desc.extraLength(), desc.extra().limit());
        desc.iInterface();

        for (final EndpointDescriptor endDesc : desc.endpoint())
        {
            this.validateEndpointDescriptor(endDesc);
        }
    }

    /**
     * Validates the specified endpoint desriptor.
     *
     * @param desc
     *            The endpoint descriptor to validate.
     */
    private void validateEndpointDescriptor(final EndpointDescriptor desc)
    {
        assertEquals(LibUsb.DT_ENDPOINT, desc.bDescriptorType());
        desc.bEndpointAddress();
        desc.bInterval();
        assertTrue(desc.bLength() > 0);
        desc.bmAttributes();
        desc.bRefresh();
        desc.bSynchAddress();
        assertEquals(desc.extraLength(), desc.extra().limit());
        desc.wMaxPacketSize();
    }

    /**
     * Tests the
     * {@link LibUsb#getActiveConfigDescriptor(Device, ConfigDescriptor)}
     * method.
     */
    @Test
    public void testGetActiveConfigDescriptor()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final ConfigDescriptor desc = new ConfigDescriptor();
        LibUsb.getActiveConfigDescriptor(this.device, desc);
        try
        {
            this.validateConfigDescriptor(desc);
        }
        finally
        {
            LibUsb.freeConfigDescriptor(desc);
        }
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptor(Device, byte, ConfigDescriptor)} method
     * without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfigDescriptor(null, (byte) 0, new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptor(Device, byte, ConfigDescriptor)} method
     * without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorWithoutDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfigDescriptor(this.device, (byte) 0, null);
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptor(Device, byte, ConfigDescriptor)}
     * method.
     */
    @Test
    public void testGetConfigDescriptor()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final ConfigDescriptor desc = new ConfigDescriptor();
        LibUsb.getConfigDescriptor(this.device, (byte) 0, desc);
        try
        {
            this.validateConfigDescriptor(desc);
        }
        finally
        {
            LibUsb.freeConfigDescriptor(desc);
        }
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptorByValue(Device, byte, ConfigDescriptor)}
     * method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorByValueWithoutDevice()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfigDescriptorByValue(null, (byte) 0,
            new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptorByValue(Device, byte, ConfigDescriptor)}
     * method without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorByValueWithoutDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.getConfigDescriptorByValue(this.device, (byte) 0, null);
    }

    /**
     * Tests the
     * {@link LibUsb#getConfigDescriptorByValue(Device, byte, ConfigDescriptor)}
     * method.
     */
    @Test
    public void testGetConfigDescriptorByValue()
    {
        assumeUsbTestsEnabled();
        assumeNotNull(this.device);
        final ConfigDescriptor desc = new ConfigDescriptor();
        LibUsb.getConfigDescriptorByValue(this.device, this.configValue, desc);
        try
        {
            this.validateConfigDescriptor(desc);
        }
        finally
        {
            LibUsb.freeConfigDescriptor(desc);

            try
            {
                LibUsb.freeConfigDescriptor(desc);
                fail("Double-free should throw IllegalStateException");
            }
            catch (final IllegalStateException e)
            {
                // Expected behavior
            }
        }
    }

    /**
     * Tests the {@link LibUsb#freeConfigDescriptor(ConfigDescriptor)} method
     * without a descriptor.
     */
    @Test
    public void testFreeConfigDescriptorWithoutDescriptor()
    {
        assumeUsbTestsEnabled();
        LibUsb.freeConfigDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUsb#getStringDescriptorAscii(DeviceHandle, byte, StringBuffer)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringDescriptorAsciiWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getStringDescriptorAscii(null, (byte) 0, new StringBuffer());
    }

    /**
     * Tests the
     * {@link LibUsb#getStringDescriptorAscii(DeviceHandle, byte, StringBuffer)}
     * method without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringDescriptorAsciiWithoutBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.getStringDescriptorAscii(new DeviceHandle(), (byte) 0, null);
    }

    /**
     * Tests the
     * {@link LibUsb#getDescriptor(DeviceHandle, byte, byte, ByteBuffer)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDescriptorWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDescriptor(null, (byte) 0, (byte) 0,
            BufferUtils.allocateByteBuffer(18));
    }

    /**
     * Tests the
     * {@link LibUsb#getDescriptor(DeviceHandle, byte, byte, ByteBuffer)} method
     * without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDescriptorWithoutBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.getDescriptor(new DeviceHandle(), (byte) 0, (byte) 0, null);
    }

    /**
     * Tests the
     * {@link LibUsb#getStringDescriptor(DeviceHandle, byte, short, ByteBuffer)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringDescriptorWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.getStringDescriptor(null, (byte) 0, (short) 0,
            BufferUtils.allocateByteBuffer(18));
    }

    /**
     * Tests the
     * {@link LibUsb#getStringDescriptor(DeviceHandle, byte, short, ByteBuffer)}
     * method without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringDescriptorWithoutBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.getStringDescriptor(new DeviceHandle(), (byte) 0, (short) 0,
            null);
    }

    /**
     * Tests the
     * {@link LibUsb#controlTransfer(DeviceHandle, byte, byte, short, short, ByteBuffer, long)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testControlTransferWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.controlTransfer(null, (byte) 0, (byte) 0, (short) 0, (short) 0,
            BufferUtils.allocateByteBuffer(0), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#controlTransfer(DeviceHandle, byte, byte, short, short, ByteBuffer, long)}
     * method without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testControlTransferWithoutBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.controlTransfer(new DeviceHandle(), (byte) 0, (byte) 0,
            (short) 0, (short) 0, null, 0);
    }

    /**
     * Tests the
     * {@link LibUsb#controlTransfer(DeviceHandle, byte, byte, short, short, ByteBuffer, long)}
     * method with an indirect buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testControlTransferWithIndirectBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.controlTransfer(new DeviceHandle(), (byte) 0, (byte) 0,
            (short) 0, (short) 0, ByteBuffer.allocate(0), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#bulkTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBulkTransferWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.bulkTransfer(null, (byte) 0, BufferUtils.allocateByteBuffer(0),
            BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#bulkTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method without a data buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBulkTransferWithoutDataBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.bulkTransfer(new DeviceHandle(), (byte) 0, null,
            BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#bulkTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method with an indirect data buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBulkTransferWithIndirectDataBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.bulkTransfer(new DeviceHandle(), (byte) 0,
            ByteBuffer.allocate(0), BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#bulkTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method without a transferred buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBulkTransferWithoutTransferredBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.bulkTransfer(new DeviceHandle(), (byte) 0,
            BufferUtils.allocateByteBuffer(0), null, 0);
    }

    /**
     * Tests the
     * {@link LibUsb#interruptTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInterruptTransferWithoutHandle()
    {
        assumeUsbTestsEnabled();
        LibUsb.interruptTransfer(null, (byte) 0,
            BufferUtils.allocateByteBuffer(0), BufferUtils.allocateIntBuffer(),
            0);
    }

    /**
     * Tests the
     * {@link LibUsb#interruptTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method without a data buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInterruptTransferWithoutDataBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.interruptTransfer(new DeviceHandle(), (byte) 0, null,
            BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#interruptTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method with an indirect data buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInterruptTransferWithIndirectDataBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.interruptTransfer(new DeviceHandle(), (byte) 0,
            ByteBuffer.allocate(0), BufferUtils.allocateIntBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUsb#interruptTransfer(DeviceHandle, byte, ByteBuffer, IntBuffer, long)}
     * method without a transferred buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInterruptTransferWithoutTransferredBuffer()
    {
        assumeUsbTestsEnabled();
        LibUsb.interruptTransfer(new DeviceHandle(), (byte) 0,
            BufferUtils.allocateByteBuffer(0), null, 0);
    }

    /**
     * Tests the {@link LibUsb#getDeviceList(Context, DeviceList)} and
     * LibUsb#freeDeviceList(DeviceList, boolean)} methods.
     */
    @Test
    public void testGetAndFreeDeviceList()
    {
        assumeUsbTestsEnabled();
        final DeviceList list = new DeviceList();
        assertTrue(LibUsb.getDeviceList(this.context, list) >= 0);
        LibUsb.freeDeviceList(list, true);

        try
        {
            LibUsb.freeDeviceList(list, true);
            fail("Double-free should throw IllegalStateException");
        }
        catch (final IllegalStateException e)
        {
            // Expected behavior
        }
    }

    /**
     * Tests the {@link LibUsb#getDeviceList(Context, DeviceList)} and
     * LibUsb#freeDeviceList(DeviceList, boolean)} methods with the default
     * context.
     */
    @Test
    public void testGetAndFreeDeviceListWithDefaultContext()
    {
        assumeUsbTestsEnabled();
        final DeviceList list = new DeviceList();
        assertEquals(0, LibUsb.init(null));
        try
        {
            assertTrue(LibUsb.getDeviceList(null, list) >= 0);
            LibUsb.freeDeviceList(list, true);

            try
            {
                LibUsb.freeDeviceList(list, true);
                fail("Double-free should throw IllegalStateException");
            }
            catch (final IllegalStateException e)
            {
                // Expected behavior
            }
        }
        finally
        {
            LibUsb.exit(null);
        }
    }
}
