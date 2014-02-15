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

import org.usb4java.test.TCKRunner;

import junit.framework.*;
 
/**
 * Bulk IO Test -- Synchronous and asynchronous byte[], IRP, and IRP List submissions
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
public class BulkIOTests extends TestCase
{
    public void setUp() throws Exception
    {
        endpointType = UsbConst.ENDPOINT_TYPE_BULK;
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
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {8};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
 
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer16bytes_invertBits()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {16};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer24bytes_invertAltBits()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {24};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer17bytes_passthrough()
    {
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {17};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer66bytes_invertBits()
    {
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {66};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer129bytes_invertAltBits()
    {
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {129};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer8bytes_invertBits()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {8};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer16bytes_invertAltBits()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {16};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf8bytes_buffer64bytes_passthrough()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {64};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer25bytes_invertBits()
    {
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {25};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer72bytes_invertAltBits()
    {
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {72};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer130bytes_passthrough()
    {
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {130};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {OUTOffset[0]};
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testIRPListBuffersMultiplesOfMaxPacketSizeOf8bytes()
    {
 
        byte testType = IOTests.IRPLIST;
        /*
         * values from table
         */
        int numberOfIrps = 3;
        int endpointmaxPacketSize = 8;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS, IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS,
            IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true, true, true};
        boolean[] verifyAcceptShortPacket = {true, true, true};
 
 
        int []OUTLength = {8,16, 24};
        int []OUTOffset = {0, 8, 24};
        int []OUTExpectedActualLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        Exception[] OUTExpectedException = {null, null, null};
 
        int []INLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        int []INOffset = {OUTOffset[0], OUTOffset[1], OUTOffset[2]};
        int []INExpectedActualLength = {OUTLength[0],OUTLength[1], OUTLength[2]};
        Exception[] INExpectedException = {null, null, null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    // TODO Disabled because 64 byte packet size not supported by test firmware
    public void disabled_testIRPListBuffersNOTMultiplesOfMaxPacketSizeOf64bytes()
    {
        byte testType = IOTests.IRPLIST;
        /*
         * values from table
         */
        int numberOfIrps = 3;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS, IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS,
            IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true, true, true};
        boolean[] verifyAcceptShortPacket = {true, true, true};
 
 
        int []OUTLength = {12, 75, 130};
        int []OUTOffset = {0, 12, 87};
        int []OUTExpectedActualLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        Exception[] OUTExpectedException = {null, null, null};
 
        int []INLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        int []INOffset = {OUTOffset[0], OUTOffset[1], OUTOffset[2]};
        int []INExpectedActualLength = {OUTLength[0],OUTLength[1], OUTLength[2]};
        Exception[] INExpectedException = {null, null, null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
    };
    /**
     * Constructor
     */
    public BulkIOTests()
    {
        super();
    };
 
    protected BulkIOTests(UsbDevice newUsbDevice, List newUsbPipeList, byte newEndpointType)
    {
        usbPipeListGlobal = newUsbPipeList;
        usbDevice = newUsbDevice;
        endpointType = newEndpointType;
    };
 
 
 
    private List usbPipeListGlobal = new ArrayList();
    private byte endpointType;
    private UsbDevice usbDevice;
 
 
    private static void printDebug(String infoString)
    {
        if ( printDebug )
        {
            System.out.println(infoString);
        }
    }
    private static boolean printDebug = false;
    //private static boolean printDebug = true;
}
