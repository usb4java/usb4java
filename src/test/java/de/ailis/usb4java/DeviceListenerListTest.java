/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceListener;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link DeviceListenerList} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class DeviceListenerListTest
{
    /** The test subject. */
    private DeviceListenerList list;

    /**
     * Set up the test.
     */
    @Before
    public void setUp()
    {
        this.list = new DeviceListenerList();
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
        final UsbDeviceListener a = mock(UsbDeviceListener.class);
        this.list.add(a);
        assertEquals(1, this.list.getListeners().size());
        assertSame(a, this.list.getListeners().get(0));
        
        // Add same listener again
        this.list.add(a);
        assertEquals(1, this.list.getListeners().size());
        
        // Add second listener
        final UsbDeviceListener b = mock(UsbDeviceListener.class);
        this.list.add(b);
        assertEquals(2, this.list.getListeners().size());
        
        // Remove first listener
        this.list.remove(a);
        assertEquals(1, this.list.getListeners().size());
        assertSame(b, this.list.getListeners().get(0));
        
        // Add first listener again and check array conversion 
        this.list.add(a);
        assertArrayEquals(new UsbDeviceListener[] { b, a }, this.list.toArray());
        
        // Clear the list
        this.list.clear();
        assertSame(0, this.list.getListeners().size());
    }
    
    /**
     * Tests the data event.
     */
    @Test
    public void testDataEvent()
    {
        final UsbDeviceDataEvent event = new UsbDeviceDataEvent(
            mock(UsbDevice.class), mock(UsbControlIrp.class));
        final UsbDeviceListener a = mock(UsbDeviceListener.class);
        final UsbDeviceListener b = mock(UsbDeviceListener.class);
        this.list.add(a);
        this.list.add(b);
        this.list.dataEventOccurred(event);
        verify(a).dataEventOccurred(event);
        verify(b).dataEventOccurred(event);
    }
    
    /**
     * Tests the error event.
     */
    @Test
    public void testErrorEvent()
    {
        final UsbDeviceErrorEvent event = new UsbDeviceErrorEvent(
            mock(UsbDevice.class), mock(UsbControlIrp.class));
        final UsbDeviceListener a = mock(UsbDeviceListener.class);
        final UsbDeviceListener b = mock(UsbDeviceListener.class);
        this.list.add(a);
        this.list.add(b);
        this.list.errorEventOccurred(event);
        verify(a).errorEventOccurred(event);
        verify(b).errorEventOccurred(event);
    }

    /**
     * Tests the detached event.
     */
    @Test
    public void testDetachedEvent()
    {
        final UsbDeviceErrorEvent event = new UsbDeviceErrorEvent(
            mock(UsbDevice.class), mock(UsbControlIrp.class));
        final UsbDeviceListener a = mock(UsbDeviceListener.class);
        final UsbDeviceListener b = mock(UsbDeviceListener.class);
        this.list.add(a);
        this.list.add(b);
        this.list.usbDeviceDetached(event);
        verify(a).usbDeviceDetached(event);
        verify(b).usbDeviceDetached(event);
    }
}
