/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.nio.ByteBuffer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * Tests the Services class.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ USB_Endpoint_Descriptor.class })
public class USB_Endpoint_Descriptor_Test
{
    /** The descriptor to test. */
    private static USB_Endpoint_Descriptor descriptor;


    /**
     * Prepares the test class.
     */

    @BeforeClass
    public static void beforeClass()
    {
        descriptor = new USB_Endpoint_Descriptor(ByteBuffer.allocateDirect(18));
    }


    /**
     * Tests the dump method.
     * @throws Exception
     */

    @Test
    public void testDump() throws Exception
    {
        final USB_Endpoint_Descriptor descriptor = mock(USB_Endpoint_Descriptor.class);
        when(descriptor.bDescriptorType()).thenReturn(1);
        when(descriptor.bEndpointAddress()).thenReturn(2);
        when(descriptor.bInterval()).thenReturn(3);
        when(descriptor.bLength()).thenReturn(4);
        when(descriptor.bmAttributes()).thenReturn(5);
        when(descriptor.bRefresh()).thenReturn(6);
        when(descriptor.bSynchAddress()).thenReturn(7);
        when(descriptor.extra()).thenReturn(ByteBuffer.allocateDirect(4));
        when(descriptor.extralen()).thenReturn(2);
        when(descriptor.dump()).thenCallRealMethod();
        final String actual = descriptor.dump();
        assertNotNull(actual);
    }
}
