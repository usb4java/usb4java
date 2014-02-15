/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.usb.UsbDevice;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link ServicesListenerList} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class ServicesListenerListTest
{
    /** The test subject. */
    private ServicesListenerList list;

    /**
     * Set up the test.
     */
    @Before
    public void setUp()
    {
        this.list = new ServicesListenerList();
    }

    /**
     * Tests the list functionality.
     */
    @Test
    public void testList()
    {
        // Must be initially empty
        assertEquals(0, this.list.getListeners().size());
        
        // Add first listener
        final UsbServicesListener a = mock(UsbServicesListener.class);
        this.list.add(a);
        assertEquals(1, this.list.getListeners().size());
        assertSame(a, this.list.getListeners().get(0));
        
        // Add same listener again
        this.list.add(a);
        assertEquals(1, this.list.getListeners().size());
        
        // Add second listener
        final UsbServicesListener b = mock(UsbServicesListener.class);
        this.list.add(b);
        assertEquals(2, this.list.getListeners().size());
        
        // Remove first listener
        this.list.remove(a);
        assertEquals(1, this.list.getListeners().size());
        assertSame(b, this.list.getListeners().get(0));
        
        // Add first listener again and check array conversion 
        this.list.add(a);
        assertArrayEquals(new UsbServicesListener[] { b, a }, this.list.toArray());
        
        // Clear the list
        this.list.clear();
        assertSame(0, this.list.getListeners().size());
    }

    /**
     * Tests the detached event.
     */
    @Test
    public void testDetachedEvent()
    {
        final UsbServicesEvent event = new UsbServicesEvent(
            mock(UsbServices.class), mock(UsbDevice.class));
        final UsbServicesListener a = mock(UsbServicesListener.class);
        final UsbServicesListener b = mock(UsbServicesListener.class);
        this.list.add(a);
        this.list.add(b);
        this.list.usbDeviceDetached(event);
        verify(a).usbDeviceDetached(event);
        verify(b).usbDeviceDetached(event);
    }

    /**
     * Tests the attached event.
     */
    @Test
    public void testAttachedEvent()
    {
        final UsbServicesEvent event = new UsbServicesEvent(
            mock(UsbServices.class), mock(UsbDevice.class));
        final UsbServicesListener a = mock(UsbServicesListener.class);
        final UsbServicesListener b = mock(UsbServicesListener.class);
        this.list.add(a);
        this.list.add(b);
        this.list.usbDeviceAttached(event);
        verify(a).usbDeviceAttached(event);
        verify(b).usbDeviceAttached(event);
    }
}
