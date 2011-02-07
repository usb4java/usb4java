/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.descriptors;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.ailis.usb4java.jni.USB_Descriptor_Header;
import de.ailis.usb4java.jni.USB_Endpoint_Descriptor;


/**
 * Tests the LibUsbEndpointDescriptor class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( { USB_Endpoint_Descriptor.class, USB_Descriptor_Header.class })
public class LibUsbEndpointDescriptorTest
{
    /** The descriptor to test. */
    private static LibUsbEndpointDescriptor descriptor;


    /**
     * Prepares the test subject.
     */

    @BeforeClass
    public static void beforeClass()
    {
        final USB_Endpoint_Descriptor mock = PowerMockito.mock(USB_Endpoint_Descriptor.class);
        when(mock.bEndpointAddress()).thenReturn(200);
        when(mock.bmAttributes()).thenReturn(201);
        when(mock.wMaxPacketSize()).thenReturn(40000);
        when(mock.bInterval()).thenReturn(202);
        descriptor = new LibUsbEndpointDescriptor(mock);
    }


    /**
     * Tests the bEndpointAddress() method.
     */

    @Test
    public void testBEndpointAddress()
    {
        assertEquals((byte) 200, descriptor.bEndpointAddress());
    }


    /**
     * Tests the bmAttributes() method.
     */

    @Test
    public void testBmAttributes()
    {
        assertEquals((byte) 201, descriptor.bmAttributes());
    }


    /**
     * Tests the wMaxPacketSize() method.
     */

    @Test
    public void testWMaxPacketSize()
    {
        assertEquals((short) 40000, descriptor.wMaxPacketSize());
    }


    /**
     * Tests the bInterval() method.
     */

    @Test
    public void testBInterval()
    {
        assertEquals((byte) 202, descriptor.bInterval());
    }
}
