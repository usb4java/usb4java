package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.util.*;

import org.junit.runner.RunWith;

import de.ailis.usb4java.test.TCKRunner;

import junit.framework.TestCase;

/**
 * Default Control Pipe Test -- Synchronous and asynchronous List of IRPs submissions
 * using UsbUtilSynchronizedUsbDevice
 * <p>
 * This test is the same as the DefaultControlPipeTestIRPList except a 
 * UsbUtil.SynchronizedUsbDevice is used in place of a UsbDevice.
 * <p>
 * This test verifies that control transfers operations work successfully
 * on the Default Control Pipe and that proper events are generated and proper
 * exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */

@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class DefaultControlPipeTestIRPListwithSynchronizedUsbDevice extends TestCase
{


    public void setUp() throws Exception
    {
        originalUsbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        usbDevice = new UsbUtil.SynchronizedUsbDevice(originalUsbDevice);
        assertNotNull("Device required for test not found",usbDevice);
        DCPIRPTestList = new DefaultControlPipeTestIRPList(usbDevice);
        super.setUp();

    }
    public void tearDown() throws Exception
    {
        super.tearDown();
    }



    /**
     * testBuffersMultiplesOfMaxPacketSize()--send OUT data which will be saved in device and made
     * available on next IN request.  Size of OUT data will be a multiple of maxPacketSize (64 bytes) 
     */
    public void testBuffersMultiplesOfMaxPacketSize()
    {
        DCPIRPTestList.testBuffersMultiplesOfMaxPacketSize();
    };

    /**
    * testBuffersNotMultiplesOfMaxPacketSize()--send OUT data which will be saved in device and made
    * available on next IN request.  Size of OUT data will not be a multiple of maxPacketSize (64 bytes). 
    */
    public void testBuffersNotMultiplesOfMaxPacketSize()
    {
        DCPIRPTestList.testBuffersNotMultiplesOfMaxPacketSize();
    };

    private UsbDevice originalUsbDevice;
    private UsbDevice usbDevice;
    DefaultControlPipeTestIRPList DCPIRPTestList;
}

