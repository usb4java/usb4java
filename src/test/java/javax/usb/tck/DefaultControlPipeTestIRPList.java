package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

/*
 * Change Activity: See below.
 *
 * FLAG REASON   RELEASE  DATE   WHO      DESCRIPTION
 * ---- -------- -------- ------ -------  ------------------------------------
 * 0000 nnnnnnn           yymmdd          Initial Development
 * $P1           tck.rel1 040804 raulortz Support for UsbDisconnectedException
 */

import java.util.*;

import javax.usb.*;
import javax.usb.event.*;

import org.junit.runner.RunWith;

import org.usb4java.test.TCKRunner;

import junit.framework.TestCase;

/**
 * Default Control Pipe Test -- Synchronous and asynchronous List of IRPs submissions
 * <p>
 * This test verifies that control transfers operations work successfully
 * on the Default Control Pipe and that proper events are generated and proper
 * exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */

@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class DefaultControlPipeTestIRPList extends TestCase
{


    public void setUp() throws Exception
    {
        usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        assertNotNull("Device required for test not found",usbDevice);
        super.setUp();

    }
    public void tearDown() throws Exception
    {
        if ( debug )
        {

            /* 
            *Print out some debug info
            */
            System.out.println("iterations = " + iterations);
            System.out.println("numSubmits = " + numSubmits);
            System.out.println("numDataEvents = " + numDataEvents);
            System.out.println("numExceptionEvents = " + numExceptionEvents);
            System.out.println("numDetachEvents = " + numDetachEvents);

        }
        super.tearDown();

    }



    /**
     * testBuffersMultiplesOfMaxPacketSize()--send OUT data which will be saved in device and made
     * available on next IN request.  Size of OUT data will be a multiple of maxPacketSize (64 bytes) 
     */
    public void testBuffersMultiplesOfMaxPacketSize()
    {
        usbDevice.addUsbDeviceListener(deviceListener);
        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                //set Control IRP specific data
                //Common to all OUT and IN IRPs for this test 
                byte bRequest = VENDOR_REQUEST_TRANSFER_DATA; //Common to all OUT and IN IRPS for this test
                boolean acceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean verifyAcceptShortPacket = true; //verify ShortPacket setting in this test
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;
                Exception expectedException = null; //no exception expected for this test

                //Common to all OUT IRPs for this test
                short OUTwValue = VENDOR_REQUEST_DATA_OUT;
                byte bmRequestTypeOUT =
                    UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;

                //Common to all IN IRPs for this test
                short INwValue = VENDOR_REQUEST_DATA_IN;
                byte bmRequestTypeIN =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;

                //create array for three OUT IRPs to be put in list
                int numIrpPairs = 3;
                short[] OUTwIndex = new short[numIrpPairs];
                short[] OUTwLength = new short[numIrpPairs];
                int[] OUTOffset = new int[numIrpPairs];
                int[] OUTLength = new int[numIrpPairs];
                int[] OUTExpectedActualLength = new int[numIrpPairs];

                //create array for three IN IRPs to be put in list
                short[] INwIndex = new short[numIrpPairs];
                short[] INwLength = new short[numIrpPairs];
                int[] INOffset = new int[numIrpPairs];
                int[] INLength = new int[numIrpPairs];
                int[] INExpectedActualLength = new int[numIrpPairs];

                /*
                 * IRP0
                 */
                //unique OUT data for this IRP
                OUTwIndex[0] = 0;
                OUTwLength[0] = 64;     //used later when creating data buffer
                OUTOffset[0] = 0;
                OUTLength[0] = OUTwLength[0];
                OUTExpectedActualLength[0] = OUTwLength[0];

                //unique IN data for this IRP
                INwIndex[0] = OUTwIndex[0];
                INwLength[0] =OUTwLength[0];
                INOffset[0] = OUTOffset[0];
                INLength[0] = OUTwLength[0];
                INExpectedActualLength[0] = OUTwLength[0];

                /*
                 * IRP1
                 */
                //unique OUT data for this IRP
                OUTwIndex[1] = 0;
                OUTwLength[1] = 128; //used later when creating data buffer
                OUTOffset[1] = 64;
                OUTLength[1] = OUTwLength[1];
                OUTExpectedActualLength[1] = OUTwLength[1];

                //unique IN data for this IRP
                INwIndex[1] = OUTwIndex[1];
                INwLength[1] =OUTwLength[1];
                INOffset[1] = OUTOffset[1];
                INLength[1] = OUTwLength[1];
                INExpectedActualLength[1] = OUTwLength[1];

                /*
                 * IRP2
                 */
                //unique OUT data for this IRP
                OUTwIndex[2] = 0;
                OUTwLength[2] = 192; //used later when creating data buffer
                OUTOffset[2] = 192;
                OUTLength[2] = OUTwLength[2];
                OUTExpectedActualLength[2] = OUTwLength[2];

                //unique IN data for this IRP
                INwIndex[2] = OUTwIndex[2];
                INwLength[2] =OUTwLength[2];
                INOffset[2] = OUTOffset[2];
                INLength[2] = OUTwLength[2];
                INExpectedActualLength[2] = OUTwLength[2];

                RoundTripTestIRPList(transmitList[j], numIrpPairs, bRequest,
                                     acceptShortPacket,
                                     verifyAcceptShortPacket,
                                     bTransformType,
                                     OUTwValue,
                                     bmRequestTypeOUT,
                                     OUTwIndex, OUTwLength, OUTOffset, OUTLength, OUTExpectedActualLength,
                                     expectedException,
                                     INwValue,
                                     bmRequestTypeIN,
                                     INwIndex, INwLength, INOffset, INLength, INExpectedActualLength,
                                     expectedException);
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    /**
    * testBuffersNotMultiplesOfMaxPacketSize()--send OUT data which will be saved in device and made
    * available on next IN request.  Size of OUT data will not be a multiple of maxPacketSize (64 bytes). 
    */
    public void testBuffersNotMultiplesOfMaxPacketSize()
    {
        usbDevice.addUsbDeviceListener(deviceListener);
        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                //set Control IRP specific data
                //Common to all OUT and IN IRPS for this test
                byte bRequest = VENDOR_REQUEST_TRANSFER_DATA;
                boolean acceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean verifyAcceptShortPacket = true;  //verify ShortPacket setting in this test
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;
                Exception expectedException = null; //no exceptions are expected in this test

                //Common to all OUT IRPs for this test
                short OUTwValue = VENDOR_REQUEST_DATA_OUT;
                byte bmRequestTypeOUT =
                    UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;

                //Common to all IN IRPs for this test
                short INwValue = VENDOR_REQUEST_DATA_IN;
                byte bmRequestTypeIN =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;

                //create array for three OUT IRPs to be put in list
                int numIrpPairs = 3;
                short[] OUTwIndex = new short[numIrpPairs];
                short[] OUTwLength = new short[numIrpPairs];
                int[] OUTOffset = new int[numIrpPairs];
                int[] OUTLength = new int[numIrpPairs];
                int[] OUTExpectedActualLength = new int[numIrpPairs];

                //create array for three IN IRPs to be put in list
                short[] INwIndex = new short[numIrpPairs];
                short[] INwLength = new short[numIrpPairs];
                int[] INOffset = new int[numIrpPairs];
                int[] INLength = new int[numIrpPairs];
                int[] INExpectedActualLength = new int[numIrpPairs];

                /*
                 * IRP0
                 */
                //unique OUT data for this IRP
                OUTwIndex[0] = 0;
                OUTwLength[0] = 30;     //used later when creating data buffer
                OUTOffset[0] = 0;
                OUTLength[0] = OUTwLength[0];
                OUTExpectedActualLength[0] = OUTwLength[0];

                //unique IN data for this IRP
                INwIndex[0] = OUTwIndex[0];
                INwLength[0] =OUTwLength[0];
                INOffset[0] = OUTOffset[0];
                INLength[0] = OUTwLength[0];
                INExpectedActualLength[0] = OUTwLength[0];

                /*
                 * IRP1
                 */
                //unique OUT data for this IRP
                OUTwIndex[1] = 0;
                OUTwLength[1] = 60;     //used later when creating data buffer
                OUTOffset[1] = 30;
                OUTLength[1] = OUTwLength[1];
                OUTExpectedActualLength[1] = OUTwLength[1];

                //unique IN data for this IRP
                INwIndex[1] = OUTwIndex[1];
                INwLength[1] =OUTwLength[1];
                INOffset[1] = OUTOffset[1];
                INLength[1] = OUTwLength[1];
                INExpectedActualLength[1] = OUTwLength[1];

                /*
                 * IRP2
                 */
                //unique OUT data for this IRP
                OUTwIndex[2] = 0;
                OUTwLength[2] = 90;     //used later when creating data buffer
                OUTOffset[2] = 90;
                OUTLength[2] = OUTwLength[2];
                OUTExpectedActualLength[2] = OUTwLength[2];

                //unique IN data for this IRP
                INwIndex[2] = OUTwIndex[2];
                INwLength[2] =OUTwLength[2];
                INOffset[2] = OUTOffset[2];
                INLength[2] = OUTwLength[2];
                INExpectedActualLength[2] = OUTwLength[2];

                RoundTripTestIRPList(transmitList[j], numIrpPairs, bRequest,
                                     acceptShortPacket,
                                     verifyAcceptShortPacket,
                                     bTransformType,
                                     OUTwValue,
                                     bmRequestTypeOUT,
                                     OUTwIndex, OUTwLength, OUTOffset, OUTLength, OUTExpectedActualLength,
                                     expectedException,
                                     INwValue,
                                     bmRequestTypeIN,
                                     INwIndex, INwLength, INOffset, INLength, INExpectedActualLength,
                                     expectedException);
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };



    /**
     * The RoundTripTestIRPList is used when an OUT IRP is sent to the default control pipe followed by an IN IRP
     * retrieving the IRP data from the device.
     * @param SyncOrAsync SYNC_SUBMIT or ASYNC_SUBMIT
     * @param numIRPPairs Number of OUT/IN IRP pairs in IRP List
     * @param bRequest bRequest Used for OUT IRP (always 0xB0=VENDOR_REQUEST_TRANSFER_DATA)
     * @param acceptShortPacket acceptShortPacket setting for IRP (this value only used for checking default value of true.
     * @param verifyAcceptShortPacket Specify whether acceptShortPacket setting should be verified
     * @param bTransformType TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01 used for this test
     * @param OUTwValue wValue used for OUT IRP (always 0x00=VENDOR_REQUEST_DATA_OUT for OUT IRPs)
     * @param bmRequestTypeOUT bmRequestType used for OUT IRP (VENDOR REQUEST TO DEVICE)
     * @param OUTwIndex wIndex for OUT IRP is start index for IRP at device end (usually zero even if offset is non-zero)
     * @param OUTwLength wLength is length of OUT IRP (set by setData() or setLength() method of IRP)
     * @param OUTOffset Offset in byte[] of first byte of data to send 
     * @param OUTLength Length of data in byte[] to send
     * @param OUTExpectedLength Length of data expected to be sent (same as OUTLength)
     * @param OUTexpectedException Exception expected on OUT submit (none in this test)
     * @param INwValue wValue used for IN IRP (always 0x80=VENDOR_REQUEST_DATA_IN for IN IRPs)
     * @param bmRequestTypeIN bmRequestType used for IN IRP (VENDOR REQUEST FROM DEVICE)
     * @param INwIndex wIndex for IN IRP is start index for reading IRP at device end (usually zero even if offset is non-zero)
     * @param INwLength wLength is length of IN IRP to be read from device
     * @param INOffset Offset in byte [] of first byte to receive
     * @param INLength Length of data in byte[] to receive
     * @param INExpectedLength ActualLength of data expected on the IN
     * @param INexpectedException Exception expected on IN submit (none in this test)
     */
    private void RoundTripTestIRPList(boolean SyncOrAsync, int numIRPPairs, byte bRequest,
                                      boolean acceptShortPacket, boolean verifyAcceptShortPacket, byte bTransformType,
                                      short OUTwValue,
                                      byte bmRequestTypeOUT,
                                      short[] OUTwIndex, short[] OUTwLength, int[] OUTOffset, int[] OUTLength, int[] OUTExpectedLength,
                                      Exception OUTexpectedException,
                                      short INwValue,
                                      byte bmRequestTypeIN,
                                      short[] INwIndex, short[] INwLength, int[] INOffset, int[]INLength, int[]INExpectedLength,
                                      Exception INexpectedException)

    {

        if ( SyncOrAsync == SYNC_SUBMIT )
        {
            VerifyIrpMethods.printDebug("RoundTripTestIRPList -- SYNC");
        } else
        {
            VerifyIrpMethods.printDebug("RoundTripTestIRPList -- ASYNC");
        }

        assertNotNull("usbDevice is null, but should not be null.", usbDevice);

        /*
         * For list of IRPs a single byte array will be used for the list of IN IRPs and another
         * single byte array will be used for all of the OUT IRPs.
         */
        byte[] aggregateOUTbuffer = new byte[750]; //750 is large enough for the tests here
        byte[] aggregateINbuffer = new byte[750]; //750 is large enough for the tests here

        /*
         * Create transmit buffer list for the three OUT/IN IRP pairs
         */
        List transmitBuffers = new ArrayList();

        /*
         * Create IRP list alternating OUT and IN IRPS
         */

        List listOfIrps = new ArrayList();

        for ( int i = 0 ; i < numIRPPairs; i++ )
        {
            //create transmit buffer for OUT and IN IRPs
            TransmitBuffer currentTransmitBuffer = new TransmitBuffer(bTransformType, OUTLength[i]);
            transmitBuffers.add(currentTransmitBuffer);

            //create OUT IRP
            UsbControlIrp currentOUTIrp = usbDevice.createUsbControlIrp(bmRequestTypeOUT, bRequest, OUTwValue, OUTwIndex[i]);
            listOfIrps.add(currentOUTIrp);

            //set data in OUT IRP
            currentOUTIrp.setData(aggregateOUTbuffer, OUTOffset[i], OUTLength[i]);

            //OUT IRP is ready to go!


            //create IN IRP
            UsbControlIrp currentINIrp = usbDevice.createUsbControlIrp(bmRequestTypeIN, bRequest, INwValue, INwIndex[i]);
            listOfIrps.add(currentINIrp);

            /*
             * set data in IN IRP -- note that no data is copied to IN byte[] before setting data in IRP.
             * byte[] will be filled by IN operation.
             */
            currentINIrp.setData(aggregateOUTbuffer, OUTOffset[i], OUTLength[i]);

        }

        //copy individual transmitbuffers into single OUT buffer
        for ( int i = 0 ; i < numIRPPairs; i++ )
        {
            TransmitBuffer currentTransmitBuffer = (TransmitBuffer) transmitBuffers.get(i);

            System.arraycopy(currentTransmitBuffer.getOutBuffer(), 0, aggregateOUTbuffer, OUTOffset[i], OUTLength[i]);
        }


        //The IRP List alternates OUTs and INs, so a single submit sends and receives all of the data
        if ( !SendUsbControlIrpList(SyncOrAsync, numIRPPairs, usbDevice, listOfIrps) )
        {
            fail("Unexpected failure submitting IRP List.");
            return;
        } else
        {
            //no exceptions thrown on submit, so verify data
            for ( int i = 0; i<numIRPPairs; i++ )
            {
                //verify OUT IRP after successful transmit
                VerifyIrpMethods.verifyUsbControlIrpAfterEvent((UsbControlIrp) listOfIrps.get(2*i),
                                                               (EventObject) listOfDeviceEvents.get(2*i),
                                                               ((TransmitBuffer)transmitBuffers.get(i)).getOutBuffer(),
                                                               OUTExpectedLength[i],
                                                               OUTexpectedException,
                                                               acceptShortPacket,
                                                               verifyAcceptShortPacket,
                                                               OUTOffset[i],
                                                               OUTLength[i],
                                                               bmRequestTypeOUT,
                                                               bRequest,
                                                               OUTwValue,
                                                               OUTwIndex[i]);

                //verify IN IRP after successful submit
                VerifyIrpMethods.verifyUsbControlIrpAfterEvent((UsbControlIrp) listOfIrps.get((2*i)+1),
                                                               (EventObject) listOfDeviceEvents.get((2*i)+1),
                                                               ((TransmitBuffer)transmitBuffers.get(i)).getInBuffer(),
                                                               INExpectedLength[i],
                                                               INexpectedException,
                                                               acceptShortPacket,
                                                               verifyAcceptShortPacket,
                                                               INOffset[i],
                                                               INLength[i],
                                                               bmRequestTypeIN,
                                                               bRequest,
                                                               INwValue,
                                                               INwIndex[i]);
            }


            //clear out list of events after verification is complete
            while ( listOfDeviceEvents.size() != 0 )
            {
                {
                    listOfDeviceEvents.remove(0);
                }

            }
        }   

    };




    private UsbDeviceListener deviceListener = new   UsbDeviceListener()
    {
        public void dataEventOccurred(UsbDeviceDataEvent uddE)
        {
            numDataEvents++;  //debug use only
            assertNotNull(uddE); //should never happen
            listOfDeviceEvents.add(uddE); // add event to list of events; all events should be returned 										  
                                          // in same order as IRPs in list
            if ( debug )
            {
                System.out.println("Data event occurred.  bmRequestType = " + uddE.getUsbControlIrp().bmRequestType());
            }
        }
        public void errorEventOccurred(UsbDeviceErrorEvent udeE)
        {
            numExceptionEvents++; //debug use only
            assertNotNull(udeE);  // should never happen
            listOfDeviceEvents.add(udeE);  // add event to list of events; all events should be returned in
                                           // same order as IRPs in list 
            fail("No devices error events expected during this test.  Exception is " + udeE.getUsbException().getMessage());
        }
        public void usbDeviceDetached(UsbDeviceEvent udE)
        {
            numDetachEvents++;
            assertNotNull(udE);
            fail("No devices should be detached during this test.");
        }
    };

    /**
     * Send the UsbControlIrp to the UsbDevice on the DCP.
     * @param SyncOrAsync SYNC_SUBMIT or ASYNC_SUBMIT
     * @param numIrpPairs Number of OUT/IN IRP pairs in IRP List
     * @param usbDevice The UsbDevice.
     * @param listOfUsbControlIrps The listOfUsbControlIrps ordered OUT/IN/OUT/IN... 
     * @return
     */
    private boolean SendUsbControlIrpList(boolean SyncOrAsync, int numIrpPairs, 
                                          UsbDevice usbDevice, List listOfUsbControlIrps)
    {
        try
        {
            if ( SyncOrAsync == SYNC_SUBMIT )
            {
                usbDevice.syncSubmit(listOfUsbControlIrps);

                //all IRPs should be complete when sync submit returns
                for ( int i=0; i< (numIrpPairs * 2); i++ )
                {
                    assertTrue("isComplete() not true for IRP after syncSubmit returned",
                               ((UsbControlIrp)listOfUsbControlIrps.get(i)).isComplete());
                    assertFalse("isUsbException() is true for IRP after syncSubmit returned",
                                ((UsbControlIrp)listOfUsbControlIrps.get(i)).isUsbException());
                }
            } else
            {
                usbDevice.asyncSubmit(listOfUsbControlIrps);
                //wait for each IRP in turn to be complete
                for ( int i=0; i< (numIrpPairs * 2); i++ )
                {
                    //wait for each IRP to be complete and then verify it is complete
                    //wait a max of 5000 ms
                    ((UsbControlIrp)listOfUsbControlIrps.get(i)).waitUntilComplete(5000);
                    assertTrue("isComplete() not true for IRP after waitUntilComplete(..)",
                               ((UsbControlIrp)listOfUsbControlIrps.get(i)).isComplete());
                    assertFalse("isUsbException() is true for IRP after waitUntilComplete(..): " + 
                                ((UsbControlIrp)listOfUsbControlIrps.get(i)).getUsbException(),
                                ((UsbControlIrp)listOfUsbControlIrps.get(i)).isUsbException());
                }
            }
        } catch ( UsbException uE )
        {
            /* The exception sould indicate the reason for the failure.
             * For this example, we'll just stop trying.
             */
            fail("No exceptions were expected in this test.  DCP submission failed." + uE.getMessage());
            return false;
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
//		finally
//		{

        try
        {
            /*
             * After all IRPs are complete, wait for all device events.
             * Wait for device event before leaving submit routine.
             * 400 * 5 ms = 2000 ms = 2 seconds max wait for the last event
             * before error.
             * This should be an adequate wait because we don't get here until
             * all of the submits are complete
             */
            for ( int i = 0; i < 400; i++ )
            {
                if ( listOfDeviceEvents.size()== numIrpPairs * 2 )
                {
                    break;
                }
                Thread.sleep( 5 ); //wait 5 ms before checkin for event
            }
        } catch ( InterruptedException e )
        {
            fail("Sleep was interrupted");
            return false;
        } finally
        {
            assertEquals("Did not receive all DeviceDataEvents.", numIrpPairs * 2, listOfDeviceEvents.size());
            numSubmits = numSubmits + (numIrpPairs * 2);
        }
        return true;
//		}
    };

    public DefaultControlPipeTestIRPList()
    {
        super();
    };

    protected DefaultControlPipeTestIRPList(UsbDevice newUsbDevice)
    {
        usbDevice = newUsbDevice;
    }

    private static final byte VENDOR_REQUEST_TRANSFER_DATA = (byte)0xB0;
    private static final short VENDOR_REQUEST_DATA_OUT = 0x00;
    private static final short VENDOR_REQUEST_DATA_IN = 0x80;
    private static final byte TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01;
    private static final byte TRANSFORM_TYPE_INVERT_BITS = (byte)0x02;
    private static final byte TRANSFORM_INVERT_ALTERNATE_BITS = (byte)0x03;
    private List listOfDeviceEvents = new ArrayList();
    private UsbDevice usbDevice;
    //private boolean debug = true;
    private boolean debug = false;
    private int iterations = 10;
    private int numSubmits = 0;
    private int numDataEvents = 0;
    private int numExceptionEvents = 0;
    private int numDetachEvents = 0;
    private static final boolean SYNC_SUBMIT = true;
    private static final boolean ASYNC_SUBMIT = false;
    boolean [] transmitList= {SYNC_SUBMIT, ASYNC_SUBMIT};

    private static final byte[] manufacturerString =  { (byte) 26,   //length of descriptor
        (byte) UsbConst.DESCRIPTOR_TYPE_STRING,
        (byte)'M',(byte) 0x00,
        (byte)'a',(byte) 0x00,
        (byte)'n',(byte) 0x00,
        (byte)'u',(byte) 0x00,
        (byte)'f',(byte) 0x00,
        (byte)'a',(byte) 0x00,
        (byte)'c',(byte) 0x00,
        (byte)'t',(byte) 0x00,
        (byte)'u',(byte) 0x00,
        (byte)'r',(byte) 0x00,
        (byte)'e',(byte) 0x00,
        (byte)'r',(byte) 0x00};
}

