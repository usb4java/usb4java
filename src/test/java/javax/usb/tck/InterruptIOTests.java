/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
 
package javax.usb.tck;
 
import java.util.*;
 
import javax.usb.*;

import org.junit.runner.RunWith;

import de.ailis.usb4java.test.TCKRunner;
 
 
 
import junit.framework.*;
 
/**
 * Interrupt IO Test -- Synchronous and asynchronous byte[], IRP, and IRP List submissions
 * <p>
 * The goal of the Bulk, Interrupt, and Isochronous IO test is to
 * verify that IN and OUT pipes can be opened and closed, and verify
 * that bulk, interrupt, and isochronous transfer operations work successfully, proper
 * events are generated, and proper exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */
 
 
@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class InterruptIOTests extends TestCase
{
    public void setUp() throws Exception
    {
        endpointType = UsbConst.ENDPOINT_TYPE_INTERRUPT;
        usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        Assert.assertNotNull("Device required for test not found",usbDevice);
        IOMethods.createListofAllAvailablePipesOfSpecifiedEndpointType(usbDevice, endpointType, usbPipeListGlobal);
        super.setUp();
    }
    public void tearDown() throws Exception
    {
        IOMethods.releaseListOfPipes(usbPipeListGlobal);
        super.tearDown();
    }
 
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer8bytes_passthrough()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer8bytes_passthrough();
    };
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer16bytes_invertBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer16bytes_invertBits();
    };
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer24bytes_invertAltBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer24bytes_invertAltBits();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer17bytes_passthrough()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer17bytes_passthrough();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer66bytes_invertBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer66bytes_invertBits();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer129bytes_invertAltBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer129bytes_invertAltBits();
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer8bytes_invertBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer8bytes_invertBits();
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer16bytes_invertAltBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer16bytes_invertAltBits();
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer64bytes_passthrough()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer64bytes_passthrough();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer25bytes_invertBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer25bytes_invertBits();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer72bytes_invertAltBits()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer72bytes_invertAltBits();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer130bytes_passthrough()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer130bytes_passthrough();
    };
    public void testIRPListBuffersMultiplesOfMaxPacketSizeOf8bytes()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.testIRPListBuffersMultiplesOfMaxPacketSizeOf8bytes();
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testIRPListBuffersNOTMultiplesOfMaxPacketSizeOf64bytes()
    {
        BulkIOTests thisInterruptTest = new BulkIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisInterruptTest.disabled_testIRPListBuffersNOTMultiplesOfMaxPacketSizeOf64bytes();
    };
 
 
 
    private List usbPipeListGlobal = new ArrayList();
    private byte endpointType;
    private UsbDevice usbDevice;
 
 
}
