/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.nio.IntBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the device-scope methods of the {@link LibUSB} class which need a open
 * USB context and a device to run the tests on.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUSBDevicelTest
{
    /** The libusb contxet. */
    private Context context;

    /** The device to run the tests on. */
    private Device device;

    /** The device endpoint to test with. */
    private int endpoint;

    /** The value of the active configuration. */
    private int configValue;

    /** The vendor ID of the device we test. */
    private int vendorId;

    /** The manufacturer ID of the device we test. */
    private int productId;

    /**
     * Set up the test.
     */
    @Before
    public void setUp()
    {
        this.context = new Context();
        LibUSB.init(this.context);
        this.device = findTestDevice();
        if (this.device == null)
            throw new IllegalStateException("Need at least one USB device " +
                "with at least one endpoint to execute this test");
    }

    /**
     * Finds a test device with at least one endpoint.
     * 
     * @return The test device or null if none.
     */
    private Device findTestDevice()
    {
        DeviceList list = new DeviceList();
        if (LibUSB.getDeviceList(this.context, list) <= 0) return null;
        try
        {
            for (Device device: list)
            {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                if (LibUSB.getDeviceDescriptor(device, descriptor) != 0)
                    continue;
                this.vendorId = descriptor.idVendor();
                this.productId = descriptor.idProduct();
                ConfigDescriptor config = new ConfigDescriptor();
                if (LibUSB.getActiveConfigDescriptor(device, config) < 0)
                    return null;
                try
                {
                    this.configValue = config.bConfigurationValue();
                    for (int j = 0; j < config.bNumInterfaces(); j++)
                    {
                        Interface iface = config.iface()[j];
                        for (int k = 0; k < iface.numAltsetting(); k++)
                        {
                            InterfaceDescriptor ifaceDescriptor =
                                iface.altsetting()[k];
                            if (ifaceDescriptor.bNumEndpoints() > 1)
                            {
                                this.endpoint = ifaceDescriptor.endpoint()[0].
                                    bEndpointAddress();
                                return LibUSB.refDevice(device);
                            }
                        }
                    }
                }
                finally
                {
                    LibUSB.freeConfigDescriptor(config);
                }
            }
        }
        finally
        {
            LibUSB.freeDeviceList(list, true);
        }
        return null;
    }

    /**
     * Tear down the test.
     */
    @After
    public void tearDown()
    {
        if (this.device != null) LibUSB.unrefDevice(this.device);
        LibUSB.exit(this.context);
    }

    /**
     * Tests the {@link LibUSB#getBusNumber(Device)} method.
     */
    @Test
    public void testGetBusNumber()
    {
        assertTrue(LibUSB.getBusNumber(this.device) >= 0);
    }

    /**
     * Tests the {@link LibUSB#getBusNumber(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBusNumberWithoutDevice()
    {
        LibUSB.getBusNumber(null);
    }

    /**
     * Tests the {@link LibUSB#getPortNumber(Device)} method.
     */
    @Test
    public void testGetPortNumber()
    {
        assertTrue(LibUSB.getPortNumber(this.device) >= 0);
    }

    /**
     * Tests the {@link LibUSB#getPortNumber(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPortNumberWithoutDevice()
    {
        LibUSB.getPortNumber(null);
    }

    /**
     * Tests the {@link LibUSB#getPortPath(Context, Device, byte[])} method.
     */
    @Test
    public void testGetPortPath()
    {
        byte[] path = new byte[8];
        int result = LibUSB.getPortPath(this.context, this.device, path);
        assertTrue(result > 0);
        assertTrue(result <= path.length);
    }

    /**
     * Tests the {@link LibUSB#getPortPath(Context, Device, byte[])} method with
     * 0-sized path buffer.
     */
    @Test
    public void testGetPortPathWithTooSmallBuffer()
    {
        byte[] path = new byte[0];
        int result = LibUSB.getPortPath(this.context, this.device, path);
        assertEquals(LibUSB.ERROR_OVERFLOW, result);
    }

    /**
     * Tests the {@link LibUSB#getPortPath(Context, Device, byte[])} method
     * without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPortPathWithoutDevice()
    {
        LibUSB.getPortPath(this.context, null, new byte[8]);
    }

    /**
     * Tests the {@link LibUSB#getPortPath(Context, Device, byte[])} method
     * without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPortPathWithoutBuffer()
    {
        LibUSB.getPortPath(this.context, this.device, null);
    }

    /**
     * Tests the {@link LibUSB#getParent(Device)} method.
     */
    @Test
    public void testGetParent()
    {
        DeviceList list = new DeviceList();
        LibUSB.getDeviceList(this.context, list);
        try
        {
            Device parent = LibUSB.getParent(this.device);

            // We cannot test anything else here. Parent can be null if our
            // test device is a root device. We just make sure that it can't
            // be the device itself.
            assertNotEquals(parent, this.device);
        }
        finally
        {
            LibUSB.freeDeviceList(list, true);
        }
    }

    /**
     * Tests the {@link LibUSB#getParent(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetParentWithoutDevice()
    {
        DeviceList list = new DeviceList();
        LibUSB.getDeviceList(this.context, list);
        try
        {
            LibUSB.getParent(null);
        }
        finally
        {
            LibUSB.freeDeviceList(list, true);
        }
    }

    /**
     * Tests the {@link LibUSB#getDeviceAddress(Device)} method.
     */
    @Test
    public void testGetDeviceAddress()
    {
        assertTrue(LibUSB.getDeviceAddress(this.device) >= 0);
    }

    /**
     * Tests the {@link LibUSB#getDeviceAddress(Device)} method without a
     * device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceAddressWithoutDevice()
    {
        LibUSB.getDeviceAddress(null);
    }

    /**
     * Tests the {@link LibUSB#getDeviceSpeed(Device)} method.
     */
    @Test
    public void testGetDeviceSpeed()
    {
        int speed = LibUSB.getDeviceSpeed(this.device);
        assertTrue(speed >= LibUSB.SPEED_UNKNOWN && speed <= LibUSB.SPEED_SUPER);
    }

    /**
     * Tests the {@link LibUSB#getDeviceSpeed(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceSpeedWithoutDevice()
    {
        LibUSB.getDeviceSpeed(null);
    }

    /**
     * Tests the {@link LibUSB#getMaxPacketSize(Device, int)} method.
     */
    @Test
    public void testGetMaxPacketSizeWithInvalidEndpoint()
    {
        assertEquals(LibUSB.ERROR_NOT_FOUND,
            LibUSB.getMaxPacketSize(this.device, 0));
    }

    /**
     * Tests the {@link LibUSB#getMaxPacketSize(Device, int)} method.
     */
    @Test
    public void testGetMaxPacketSize()
    {
        assertTrue(LibUSB.getMaxPacketSize(this.device, this.endpoint) > 0);
    }

    /**
     * Tests the {@link LibUSB#getMaxPacketSize(Device, int)} method without a
     * device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxPacketSizeWithoutDevice()
    {
        LibUSB.getMaxPacketSize(null, 0);
    }

    /**
     * Tests the {@link LibUSB#getMaxIsoPacketSize(Device, int)} method.
     */
    @Test
    public void testGetMaxIsoPacketSizeWithInvalidEndpoint()
    {
        assertEquals(LibUSB.ERROR_NOT_FOUND,
            LibUSB.getMaxIsoPacketSize(this.device, 0));
    }

    /**
     * Tests the {@link LibUSB#getMaxIsoPacketSize(Device, int)} method.
     */
    @Test
    public void testGetMaxIsoPacketSize()
    {
        assertTrue(LibUSB.getMaxIsoPacketSize(this.device, this.endpoint) > 0);
    }

    /**
     * Tests the {@link LibUSB#getMaxIsoPacketSize(Device, int)} method without
     * a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxIsoPacketSizeWithoutDevice()
    {
        LibUSB.getMaxIsoPacketSize(null, 0);
    }

    /**
     * Tests the {@link LibUSB#refDevice(Device)} and
     * {@link LibUSB#unrefDevice(Device)} methods.
     */
    @Test
    public void testRefUnRefDevice()
    {
        Device device = LibUSB.refDevice(this.device);
        try
        {
            assertEquals(this.device, device);
        }
        finally
        {
            LibUSB.unrefDevice(device);
        }

    }

    /**
     * Tests the {@link LibUSB#refDevice(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRefDeviceWithoutDevice()
    {
        LibUSB.refDevice(null);
    }

    /**
     * Tests the {@link LibUSB#unrefDevice(Device)} method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnrefDeviceWithoutDevice()
    {
        LibUSB.unrefDevice(null);
    }

    /**
     * Tests the {@link LibUSB#open(Device, DeviceHandle)} method. This can't be
     * really tested in a simple unit test like this. Most likely the user has
     * no access to the device. So this test succeeds on SUCCESS and on
     * ERROR_ACCESS. At least we have to make sure then open() method doesn't
     * crash.
     */
    @Test
    public void testOpen()
    {
        DeviceHandle handle = new DeviceHandle();
        int result = LibUSB.open(this.device, handle);
        assertTrue(result == LibUSB.SUCCESS || result == LibUSB.ERROR_ACCESS);
        if (result == LibUSB.SUCCESS) LibUSB.close(handle);
    }

    /**
     * Tests the {@link LibUSB#open(Device, DeviceHandle)} method without a
     * device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutDevice()
    {
        DeviceHandle handle = new DeviceHandle();
        LibUSB.open(null, handle);
    }

    /**
     * Tests the {@link LibUSB#open(Device, DeviceHandle)} method without a
     * handle
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenWithoutHandle()
    {
        LibUSB.open(this.device, null);
    }

    /**
     * Tests the {@link LibUSB#close(DeviceHandle)} method without a handle
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloseWithoutHandle()
    {
        LibUSB.close(null);
    }

    /**
     * Tests the {@link LibUSB#openDeviceWithVidPid(Context, int, int)} method.
     * We can't test anything here because the device most likely can't be
     * opened anyway. We just make sure it does not crash.
     */
    @Test
    public void testOpenDeviceWithVidPid()
    {
        DeviceHandle handle = LibUSB.openDeviceWithVidPid(this.context,
            this.vendorId, this.productId);
        if (handle != null) LibUSB.close(handle);
    }

    /**
     * Tests the {@link LibUSB#getDevice(DeviceHandle)} method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceWithoutHandle()
    {
        LibUSB.getDevice(null);
    }

    /**
     * Tests the
     * {@link LibUSB#getConfiguration(DeviceHandle, java.nio.IntBuffer)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationWithoutHandle()
    {
        LibUSB.getConfiguration(null, IntBuffer.allocate(1));
    }

    /**
     * Tests the
     * {@link LibUSB#getConfiguration(DeviceHandle, java.nio.IntBuffer)} method
     * without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationWithoutBuffer()
    {
        LibUSB.getConfiguration(new DeviceHandle(), null);
    }

    /**
     * Tests the {@link LibUSB#setConfiguration(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetConfigurationWithoutHandle()
    {
        LibUSB.setConfiguration(null, 0);
    }

    /**
     * Tests the {@link LibUSB#claimInterface(DeviceHandle, int)} method without
     * a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClaimInterfaceWithoutHandle()
    {
        LibUSB.claimInterface(null, 0);
    }

    /**
     * Tests the {@link LibUSB#releaseInterface(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseInterfaceWithoutHandle()
    {
        LibUSB.releaseInterface(null, 0);
    }

    /**
     * Tests the {@link LibUSB#setInterfaceAltSetting(DeviceHandle, int, int)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInterfaceAltSettingWithoutHandle()
    {
        LibUSB.setInterfaceAltSetting(null, 0, 0);
    }

    /**
     * Tests the {@link LibUSB#clearHalt(DeviceHandle, int)} method without a
     * handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClearHaltWithoutHandle()
    {
        LibUSB.clearHalt(null, 0);
    }

    /**
     * Tests the {@link LibUSB#resetDevice(DeviceHandle)} method without a
     * handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testResetDeviceWithoutHandle()
    {
        LibUSB.resetDevice(null);
    }

    /**
     * Tests the {@link LibUSB#kernelDriverActive(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testKernelDriverActiveWithoutHandle()
    {
        LibUSB.kernelDriverActive(null, 0);
    }

    /**
     * Tests the {@link LibUSB#detachKernelDriver(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDetachKernelDriverWithoutHandle()
    {
        LibUSB.detachKernelDriver(null, 0);
    }

    /**
     * Tests the {@link LibUSB#attachKernelDriver(DeviceHandle, int)} method
     * without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAttachKernelDriverWithoutHandle()
    {
        LibUSB.attachKernelDriver(null, 0);
    }

    /**
     * Tests the {@link LibUSB#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceDescriptorWithoutDevice()
    {
        LibUSB.getDeviceDescriptor(null, new DeviceDescriptor());
    }

    /**
     * Tests the {@link LibUSB#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDeviceDescriptorWithoutDescriptor()
    {
        LibUSB.getDeviceDescriptor(this.device, null);
    }

    /**
     * Tests the {@link LibUSB#getDeviceDescriptor(Device, DeviceDescriptor)}
     * method.
     * 
     * Most descriptor fields can be anything because we are not testing a
     * specific device. We call each method anyway to make sure it is connected
     * to JNI and doesn't crash.
     */
    @Test
    public void testGetDeviceDescriptor()
    {
        DeviceDescriptor desc = new DeviceDescriptor();
        LibUSB.getDeviceDescriptor(this.device, desc);
        desc.bcdDevice();
        desc.bcdUSB();
        assertEquals(LibUSB.DT_DEVICE, desc.bDescriptorType());
        desc.bDeviceClass();
        desc.bDeviceProtocol();
        desc.bDeviceSubClass();
        assertEquals(desc.data.limit(), desc.bLength());
        desc.bMaxPacketSize0();
        desc.bNumConfigurations();
    }

    /**
     * Tests the
     * {@link LibUSB#getActiveConfigDescriptor(Device, ConfigDescriptor)} method
     * without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetActiveConfigDescriptorWithoutDevice()
    {
        LibUSB.getActiveConfigDescriptor(null, new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUSB#getActiveConfigDescriptor(Device, ConfigDescriptor)} method
     * without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetActiveConfigDescriptorWithoutDescriptor()
    {
        LibUSB.getActiveConfigDescriptor(this.device, null);
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
        assertEquals(LibUSB.DT_CONFIG, desc.bDescriptorType());
        assertTrue(desc.bLength() > 0);
        desc.bmAttributes();
        desc.bMaxPower();
        assertEquals(desc.extraLength(), desc.extra().limit());
        desc.iConfiguration();
        assertEquals(desc.bNumInterfaces(), desc.iface().length);
        assertTrue(desc.wTotalLength() >= desc.bLength());

        for (Interface iface: desc.iface())
            validateInterface(iface);
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
        for (InterfaceDescriptor desc: iface.altsetting())
        {
            validateInterfaceDescriptor(desc);
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
        assertEquals(LibUSB.DT_INTERFACE, desc.bDescriptorType());
        desc.bInterfaceClass();
        desc.bInterfaceNumber();
        desc.bInterfaceProtocol();
        desc.bInterfaceSubClass();
        assertTrue(desc.bLength() > 0);
        assertEquals(desc.bNumEndpoints(), desc.endpoint().length);
        assertEquals(desc.extraLength(), desc.extra().limit());
        desc.iInterface();

        for (EndpointDescriptor endDesc: desc.endpoint())
        {
            validateEndpointDescriptor(endDesc);
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
        assertEquals(LibUSB.DT_ENDPOINT, desc.bDescriptorType());
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
     * {@link LibUSB#getActiveConfigDescriptor(Device, ConfigDescriptor)}
     * method.
     */
    @Test
    public void testGetActiveConfigDescriptor()
    {
        ConfigDescriptor desc = new ConfigDescriptor();
        LibUSB.getActiveConfigDescriptor(this.device, desc);
        try
        {
            validateConfigDescriptor(desc);
        }
        finally
        {
            LibUSB.freeConfigDescriptor(desc);
        }
    }

    /**
     * Tests the
     * {@link LibUSB#getConfigDescriptor(Device, int, ConfigDescriptor)} method
     * without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorWithoutDevice()
    {
        LibUSB.getConfigDescriptor(null, 0, new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUSB#getConfigDescriptor(Device, int, ConfigDescriptor)} method
     * without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorWithoutDescriptor()
    {
        LibUSB.getConfigDescriptor(this.device, 0, null);
    }

    /**
     * Tests the
     * {@link LibUSB#getConfigDescriptor(Device, int, ConfigDescriptor)} method.
     */
    @Test
    public void testGetConfigDescriptor()
    {
        ConfigDescriptor desc = new ConfigDescriptor();
        LibUSB.getConfigDescriptor(this.device, 0, desc);
        try
        {
            validateConfigDescriptor(desc);
        }
        finally
        {
            LibUSB.freeConfigDescriptor(desc);
        }
    }

    /**
     * Tests the
     * {@link LibUSB#getConfigDescriptorByValue(Device, int, ConfigDescriptor)}
     * method without a device.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorByValueWithoutDevice()
    {
        LibUSB.getConfigDescriptorByValue(null, 0, new ConfigDescriptor());
    }

    /**
     * Tests the
     * {@link LibUSB#getConfigDescriptorByValue(Device, int, ConfigDescriptor)}
     * method without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigDescriptorByValueWithoutDescriptor()
    {
        LibUSB.getConfigDescriptorByValue(this.device, 0, null);
    }

    /**
     * Tests the
     * {@link LibUSB#getConfigDescriptorByValue(Device, int, ConfigDescriptor)}
     * method.
     */
    @Test
    public void testGetConfigDescriptorByValue()
    {
        ConfigDescriptor desc = new ConfigDescriptor();
        LibUSB.getConfigDescriptorByValue(this.device, this.configValue, desc);
        try
        {
            validateConfigDescriptor(desc);
        }
        finally
        {
            LibUSB.freeConfigDescriptor(desc);
        }
    }

    /**
     * Tests the {@link LibUSB#freeConfigDescriptor(ConfigDescriptor)} method
     * without a descriptor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFreeConfigDescriptorWithoutDescriptor()
    {
        LibUSB.freeConfigDescriptor(null);
    }

    /**
     * Tests the
     * {@link LibUSB#getStringDescriptorAscii(DeviceHandle, int, StringBuffer, int)}
     * method without a handle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringDescriptorAsciiWithoutHandle()
    {
        LibUSB.getStringDescriptorAscii(null, 0, new StringBuffer(), 0);
    }

    /**
     * Tests the
     * {@link LibUSB#getStringDescriptorAscii(DeviceHandle, int, StringBuffer, int)}
     * method without a buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringDescriptorAsciiWithoutBuffer()
    {
        LibUSB.getStringDescriptorAscii(new DeviceHandle(), 0, null, 0);
    }
}
