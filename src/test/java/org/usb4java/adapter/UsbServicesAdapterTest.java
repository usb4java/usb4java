/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package de.ailis.usb4java.adapter;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import org.junit.Test;

/**
 * Test the {@link UsbServicesAdapter} class. There is not really anything to
 * test there. This class just ensures that the class exists and provides
 * the needed methods.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public class UsbServicesAdapterTest
{
    /**
     * Ensure the existence of the needed methods.
     */
    @Test
    public void testAbstractMethods()
    {
        final UsbServicesListener adapter = new UsbServicesAdapter()
        {
            // Empty
        };
        adapter.usbDeviceAttached((UsbServicesEvent) null);
        adapter.usbDeviceDetached((UsbServicesEvent) null);
    }
}
