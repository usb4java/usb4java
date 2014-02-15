/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

package javax.usb.tck;

import javax.usb.*;
import javax.usb.util.*;

import org.junit.runner.RunWith;

import org.usb4java.test.TCKRunner;

import junit.framework.TestCase;

/**
 * Default Control Pipe Test -- Synchronous and asynchronous IRP submissions
 * using UsbUtilSynchronizedUsbDevice
 * <p>
 * This test is the same as the DefaultControlPipeTestIRP except a 
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
public class DefaultControlPipeTestIRPwithSynchronizedUsbDevice extends TestCase
{
    public void setUp() throws Exception
    {
        originalUsbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        usbDevice = new UsbUtil.SynchronizedUsbDevice(originalUsbDevice);
        assertNotNull("Device required for test not found",usbDevice);
        DCPIRPTest = new DefaultControlPipeTestIRP(usbDevice);
        super.setUp();

    }
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testBuffersMultiplesOfMaxPacketSize_1packets()
    {

        DCPIRPTest.testBuffersMultiplesOfMaxPacketSize_1packets();

    };
    public void testBuffersMultiplesOfMaxPacketSize_2packets()
    {
        DCPIRPTest.testBuffersMultiplesOfMaxPacketSize_2packets();
    };

    public void testBuffersMultiplesOfMaxPacketSize_moreThan2Packets()
    {
        DCPIRPTest.testBuffersMultiplesOfMaxPacketSize_moreThan2Packets();
    };

    public void testBuffersNotMultiplesOfMaxPacketSize_1packets()
    {
        DCPIRPTest.testBuffersNotMultiplesOfMaxPacketSize_1packets();
    };

    public void testBuffersNotMultiplesOfMaxPacketSize_2packets()
    {
        DCPIRPTest.testBuffersNotMultiplesOfMaxPacketSize_2packets();
    };

    public void testBuffersNotMultiplesOfMaxPacketSize_MoreThan2packets()
    {
        DCPIRPTest.testBuffersNotMultiplesOfMaxPacketSize_MoreThan2packets();
    };

    public void testRequestClearFeature()
    {
        DCPIRPTest.testRequestClearFeature();
    };

    public void testRequestGetDescriptor()
    {
        DCPIRPTest.testRequestGetDescriptor();
    };


    private UsbDevice originalUsbDevice;
    private UsbDevice usbDevice;
    DefaultControlPipeTestIRP DCPIRPTest;
}