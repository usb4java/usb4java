/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package de.ailis.usb4java.adapter;

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;

import org.junit.Test;

/**
 * Test the {@link UsbDeviceAdapter} class. There is not really anything to
 * test there. This class just ensures that the class exists and provides
 * the needed methods.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class UsbDeviceAdapterTest
{
    /**
     * Ensure the existence of the needed methods.
     */
    @Test
    public void testAbstractMethods()
    {
        final UsbDeviceListener adapter = new UsbDeviceAdapter()
        {
            // Empty
        };
        adapter.usbDeviceDetached((UsbDeviceEvent) null);
        adapter.dataEventOccurred((UsbDeviceDataEvent) null);
        adapter.errorEventOccurred((UsbDeviceErrorEvent) null);
    }
}
