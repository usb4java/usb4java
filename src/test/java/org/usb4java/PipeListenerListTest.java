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

import javax.usb.UsbException;
import javax.usb.UsbPipe;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link PipeListenerList} class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class PipeListenerListTest
{
    /** The test subject. */
    private PipeListenerList list;

    /**
     * Set up the test.
     */
    @Before
    public void setUp()
    {
        this.list = new PipeListenerList();
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
        final UsbPipeListener a = mock(UsbPipeListener.class);
        this.list.add(a);
        assertEquals(1, this.list.getListeners().size());
        assertSame(a, this.list.getListeners().get(0));
        
        // Add same listener again
        this.list.add(a);
        assertEquals(1, this.list.getListeners().size());
        
        // Add second listener
        final UsbPipeListener b = mock(UsbPipeListener.class);
        this.list.add(b);
        assertEquals(2, this.list.getListeners().size());
        
        // Remove first listener
        this.list.remove(a);
        assertEquals(1, this.list.getListeners().size());
        assertSame(b, this.list.getListeners().get(0));
        
        // Add first listener again and check array conversion 
        this.list.add(a);
        assertArrayEquals(new UsbPipeListener[] { b, a }, this.list.toArray());
        
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
        final UsbPipeDataEvent event =
            new UsbPipeDataEvent(mock(UsbPipe.class), null);
        final UsbPipeListener a = mock(UsbPipeListener.class);
        final UsbPipeListener b = mock(UsbPipeListener.class);
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
        final UsbPipeErrorEvent event =
            new UsbPipeErrorEvent(mock(UsbPipe.class), new UsbException());
        final UsbPipeListener a = mock(UsbPipeListener.class);
        final UsbPipeListener b = mock(UsbPipeListener.class);
        this.list.add(a);
        this.list.add(b);
        this.list.errorEventOccurred(event);
        verify(a).errorEventOccurred(event);
        verify(b).errorEventOccurred(event);
    }
}
