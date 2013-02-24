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

import de.ailis.usb4java.TCKRunner;



import junit.framework.*;

/**
 * Bulk IO Short Packet Test -- Synchronous and asynchronous byte[], IRP, and IRP List submissions
 * <p>
 * The goal of the Bulk, Interrupt, and Isochronous IO test is to 
 * verify that IN and OUT pipes can be opened and closed, and verify
 * that bulk, interrupt, and isochronous transfer operations work successfully, proper
 * events are generated, and proper exceptions are thrown in the operation.
 * 
 * This test verifies that UsbShortPacketException is received when expected.
 * 
 * @author Leslie Blair
 */

@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class BulkShortPacketIOTests extends TestCase
{
    public void setUp() throws Exception
    {
        endpointType = UsbConst.ENDPOINT_TYPE_BULK;
		usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        Assert.assertNotNull("Device required for test not found",usbDevice);
        IOMethods.createListofAllAvailablePipesOfSpecifiedEndpointType(usbDevice, endpointType, usbPipeListGlobal);
        assertFalse("No pipes of specified endpoint were found.", (0 == usbPipeListGlobal.size()));
        super.setUp();
    }
    public void tearDown() throws Exception
    {
        IOMethods.releaseListOfPipes(usbPipeListGlobal);
        super.tearDown();
    }


    public void testShortPacketException()
    {
    	//repeat the test for the number of iteration specified
        for ( int iterations=0; (iterations < IOShortPacketTest.totalIterations); iterations++ )
        {
        	//first, do all transfers sync and then do async
            for ( int syncOrAsync=0; syncOrAsync<IOShortPacketTest.transmitList.length; syncOrAsync++ ) //SYNC first and then ASYNC
            {
                byte testType = IOTests.IRP;  //only sending IRPs for this test
                /*
                 * first, send a short packet with acceptShortPacket set to false; expect UsbShortPacketException.
                 * Endpoint will be cleared after error.
                 */
                int numberOfIrps = 1; 
                int endpointmaxPacketSize = 8; // TODO Should be 64 (My test controller doesn't support large packets)
                byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};

                boolean[] IrpAcceptShortPacket = {false};
                boolean[] verifyAcceptShortPacket = {true};

                int []OUTLength = {20};
                int []OUTOffset = {0};
                int []OUTExpectedActualLength = {OUTLength[0]};
                Exception[] OUTExpectedException = {null};

                int []INLength = {30};
                int []INOffset = {OUTOffset[0]};
                int []INExpectedActualLength = {OUTLength[0]};
                Exception shortPacketException = new UsbShortPacketException();
				Exception[] INExpectedException = {shortPacketException};

                IOShortPacketTest thisIOTest = new IOShortPacketTest(usbDevice, usbPipeListGlobal, endpointType, testType);
                thisIOTest.RoundTripIOTestShortPacket(testType, syncOrAsync, numberOfIrps, endpointmaxPacketSize,
                                                      IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                                      OUTExpectedException,
                                                      INLength, INOffset, INExpectedActualLength,
                                                      INExpectedException,
                                                      transformType );

               /*
                * next, send same data as above with acceptShortPacket set to true; expect successful send
                */
                IrpAcceptShortPacket[0] = true;
                verifyAcceptShortPacket[0] = true;

                OUTLength[0] = 20;
                OUTOffset[0] = 0;
                OUTExpectedActualLength[0] = OUTLength[0];
                OUTExpectedException[0] = null;

                INLength[0] = 30;
                INOffset[0] = OUTOffset[0];
                INExpectedActualLength[0] = OUTLength[0];
                INExpectedException[0] = null;

                thisIOTest = new IOShortPacketTest(usbDevice, usbPipeListGlobal, endpointType, testType);
                thisIOTest.RoundTripIOTestShortPacket(testType, syncOrAsync, numberOfIrps, endpointmaxPacketSize,
                                                      IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                                      OUTExpectedException,
                                                      INLength, INOffset, INExpectedActualLength,
                                                      INExpectedException,
                                                      transformType );

                /*
                * next, send and/receive a packet of expected length with acceptShortPacket set to false; expect successful send
                */

                IrpAcceptShortPacket[0] = false;
                verifyAcceptShortPacket[0] = true;


                OUTLength[0] = 25;
                OUTOffset[0] = 0;
                OUTExpectedActualLength[0] = OUTLength[0];
                OUTExpectedException[0] = null;

                INLength[0] = 25;
                INOffset[0] = OUTOffset[0];
                INExpectedActualLength[0] = OUTLength[0];
                INExpectedException[0] = null;

                thisIOTest = new IOShortPacketTest(usbDevice, usbPipeListGlobal, endpointType, testType);
                thisIOTest.RoundTripIOTestShortPacket(testType, syncOrAsync, numberOfIrps, endpointmaxPacketSize,
                                                      IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                                      OUTExpectedException,
                                                      INLength, INOffset, INExpectedActualLength,
                                                      INExpectedException,
                                                      transformType );
            }

        }
    };

    /**
     * Constructor
     */
    public BulkShortPacketIOTests()
    {
        super();
    };

    protected BulkShortPacketIOTests(UsbDevice newUsbDevice, List newUsbPipeList, byte newEndpointType)
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
