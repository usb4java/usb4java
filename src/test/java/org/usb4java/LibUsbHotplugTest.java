/*
 * Copyright (C) 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertEquals;
import static org.usb4java.test.UsbAssume.assumeHotplugAvailable;
import static org.usb4java.test.UsbAssume.isUsbTestsEnabled;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the hotplug part of the {@link LibUsb} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class LibUsbHotplugTest
{
    /** The libusb contxet. */
    private Context context;

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
        }
    }

    /**
     * Tear down the test.
     */
    @After
    public void tearDown()
    {
        if (isUsbTestsEnabled())
        {
            if (this.context != null)
            {
                LibUsb.exit(this.context);
            }
        }
    }

    /**
     * Tests the enumeration of connected devices through the hotplug API. This
     * test simply compares the number of enumerated devices with the device
     * list. When no devices are connected then this test is pretty much useless
     * but won't fail because of that.
     */
    @Test
    public void testHotplugEnumeration()
    {
        assumeHotplugAvailable();

        final List<Integer> events = new ArrayList<Integer>();
        HotplugCallbackHandle callbackHandle = new HotplugCallbackHandle();
        int result = LibUsb.hotplugRegisterCallback(this.context,
            LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED,
            LibUsb.HOTPLUG_ENUMERATE,
            LibUsb.HOTPLUG_MATCH_ANY,
            LibUsb.HOTPLUG_MATCH_ANY,
            LibUsb.HOTPLUG_MATCH_ANY,
            new HotplugCallback()
            {
                @Override
                public int processEvent(Context context, Device device,
                    int event,
                    Object userData)
                {
                    events.add(event);
                    return 0;
                }
            }, null, callbackHandle);
        assertEquals(result, LibUsb.SUCCESS);
        LibUsb.hotplugDeregisterCallback(this.context, callbackHandle);

        DeviceList devices = new DeviceList();
        LibUsb.getDeviceList(this.context, devices);
        int numDevices = devices.getSize();
        LibUsb.freeDeviceList(devices, true);
        assertEquals(numDevices, events.size());
    }

    /**
     * Ensures that no hotplug event is fired when enumeration is deactived.
     * When no devices are connected then this test is pretty much useless but
     * won't fail because of that.
     */
    @Test
    public void testHotplugEnumerationOff()
    {
        assumeHotplugAvailable();

        final List<Integer> events = new ArrayList<Integer>();
        HotplugCallbackHandle callbackHandle = new HotplugCallbackHandle();
        int result = LibUsb.hotplugRegisterCallback(this.context,
            LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED,
            0,
            LibUsb.HOTPLUG_MATCH_ANY,
            LibUsb.HOTPLUG_MATCH_ANY,
            LibUsb.HOTPLUG_MATCH_ANY,
            new HotplugCallback()
            {

                @Override
                public int processEvent(Context context, Device device,
                    int event,
                    Object userData)
                {
                    events.add(event);
                    return 0;
                }
            }, null, callbackHandle);
        assertEquals(result, LibUsb.SUCCESS);
        LibUsb.hotplugDeregisterCallback(this.context, callbackHandle);
        assertEquals(0, events.size());
    }

}
