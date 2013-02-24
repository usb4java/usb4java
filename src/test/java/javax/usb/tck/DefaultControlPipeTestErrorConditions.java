package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;
import javax.usb.event.*;

import org.junit.runner.RunWith;

import de.ailis.usb4java.TCKRunner;

import junit.framework.TestCase;

/**
 * Default Control Pipe Test -- Error Conditions
 * <p>
 * This test verifies that control transfers operations work successfully
 * on the Default Control Pipe and that proper events are generated and proper
 * exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */


@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class DefaultControlPipeTestErrorConditions extends TestCase
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
            /* Print out some debug info
             */
            System.out.println("iterations = " + iterations);
            System.out.println("numSubmits = " + numSubmits);
            System.out.println("numDataEvents = " + numDataEvents);
            System.out.println("numExceptionEvents = " + numExceptionEvents);
            System.out.println("numDetachEvents = " + numDetachEvents);
        }
        super.tearDown();
    }

    public void testUsbShortPacketException()
    {
        usbDevice.addUsbDeviceListener(deviceListener);

        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                /*******************************
                /Prepare OUT IRP
                *********************************/
                //set Control IRP specific data
                byte OUTbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte OUTbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short OUTwValue = VENDOR_REQUEST_DATA_OUT;
                short OUTwIndex = 0;
                short OUTwLength = 20; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true; //since we are setting it or expect it to default true then we'll check it
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;

                //Expected exception after submission
                UsbException OUTIrpExpectedException = null;

                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =UsbConst.REQUESTTYPE_DIRECTION_IN |
                                      UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =32;
                // IRP data when IRP is created and data is set and should be unchanged after submission
                int INIrpLength = INwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;

                boolean INIrpAcceptShortPacket = true;  //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true; //since we are setting it
                UsbException INIrpExpectedException = null;


                //For even iterations,do INIrpAcceptShortPacket false--exception expected
                //For odd iterations, do INIrpAcceptShortPacket true--data expected
                //if ((iterations % 2) == 0)
                if ( (iterations % 2) == 0 )
                {
                    INIrpAcceptShortPacket = false; //acceptShortPacket is set on an individual IRP basis; default is true

                    //Expected exception after submission
                    INIrpExpectedException = new UsbShortPacketException();
                }


                INverifyAcceptShortPacket = true; //since we are setting it

                RoundTripTestPossibleErrors(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                            OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket,OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                            OUTIrpExpectedException,
                                            INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                            INIrpAcceptShortPacket,INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                            INIrpExpectedException,
                                            bTransformType );
            }
        } 
        usbDevice.removeUsbDeviceListener(deviceListener);                   
    };

    // TODO No idea how to detect a STALL condition in libusb 0.1
    public void disabled_testUsbStallException()
    {
        usbDevice.addUsbDeviceListener(deviceListener);

        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                //set Control IRP specific data
                byte OUTbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte OUTbRequest = 20;
                short OUTwValue = 0;
                short OUTwIndex = 0;
                short OUTwLength = 0; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = false; //acceptShortPacket is set on an individual IRP basis; default is true
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;
                byte[] OUTIrpData = new byte[OUTwLength];

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;

                //Expected exception after submission
                UsbException expectedException = new UsbStallException();

                //Other expected values
                byte expectedbmRequestType = OUTbmRequestType;
                byte expectedbRequest = OUTbRequest;
                short expectedwValue = OUTwValue;
                short expectedwIndex = 0;
                short expectedwLength = 0; //used later when creating data buffer
                boolean expectedAcceptShortPacket = false;
                boolean verifyAcceptShortPacket = true;
                int expectedIrpLength = OUTwLength;
                int expectedIrpOffset = 0;
                byte[] expectedData = null;
                int expectedActualLength = 0;

                OneWayTestPossibleErrors(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                         OUTIrpAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpData,
                                         expectedbmRequestType, expectedbRequest, expectedwValue, expectedwIndex, expectedwLength,
                                         expectedAcceptShortPacket,verifyAcceptShortPacket, expectedIrpLength, expectedIrpOffset, expectedActualLength,
                                         expectedException,
                                         expectedData );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener); 
    };

    public void testNotCallingsetDataForIRP()
    {
        usbDevice.addUsbDeviceListener(deviceListener);

        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                //set Control IRP specific data
                byte OUTbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte OUTbRequest = UsbConst.REQUEST_GET_CONFIGURATION;
                short OUTwValue = 0;
                short OUTwIndex = 0;
                short OUTwLength = 0; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = false; //acceptShortPacket is set on an individual IRP basis; default is true
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                /*
                 * This is the signal to not set the data in the IRP.
                 * Normally, for get configuration, the byte [] must be available
                 * to receive the configuration value
                 */
                byte[] OUTIrpData = null;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;

                //Expected exception after submission
                UsbException expectedException = new UsbException();

                //Other expected values
                byte expectedbmRequestType = OUTbmRequestType;
                byte expectedbRequest = OUTbRequest;
                short expectedwValue = OUTwValue;
                short expectedwIndex = 0;
                short expectedwLength = 0; //used later when creating data buffer
                boolean expectedAcceptShortPacket = false;
                boolean verifyAcceptShortPacket = true;
                int expectedIrpLength = OUTwLength;
                int expectedIrpOffset = 0;
                byte[] expectedData = null;
                int expectedActualLength = 0;

                OneWayTestPossibleErrors(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                         OUTIrpAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpData,
                                         expectedbmRequestType, expectedbRequest, expectedwValue, expectedwIndex, expectedwLength,
                                         expectedAcceptShortPacket,verifyAcceptShortPacket, expectedIrpLength, expectedIrpOffset, expectedActualLength,
                                         expectedException,
                                         expectedData );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);  
    };

    /**
     * The RoundTripTestPossibleErrors is used when an OUT IRP is sent to the default control pipe followed by an IN IRP
     * retrieving the IRP from the device.
     * <p>
     * @param SyncOrAsync
     * @param OUTbmRequestType bmRequestType used for OUT IRP
     * @param OUTbRequest bRequest used for OUT IRP
     * @param OUTwValue wValue used for OUT IRP (always 0x00=VENDOR_REQUEST_DATA_OUT for OUT IRPs)
     * @param OUTwIndex wIndex for OUT IRP is start index for IRP at device end (usually zero even if offset is non-zero)
     * @param OUTwLength wLength is length of OUT IRP (set by setData() or setLength() method of IRP)
     * @param OUTIrpAcceptShortPacket acceptShortPacket setting for IRP (this value only used for checking default value of true.
     * acceptShortPacket is NOT set in RoundTripTest)
     * @param OUTverifyAcceptShortPacket Specify whether to verify acceptShortPacket
     * @param OUTIrpLength Length of IRP to be read from byte[].  Set indirectly by setData().  Same as wLength for this test,
     * @param OUTIrpOffset Offset in byte[] at which to start reading IRP.
     * @param OUTIrpExpectedActualLength ActualLength is the length of data actually transmitted in IRP. Same as wLength for OUT.
     * @param INbmRequestType bmRequestType used for IN IRP
     * @param INbRequest bRequest used for IN IRP (always 0xB0=VENDOR_REQUEST_TRANSFER_DATA)
     * @param INwValue wValue used for IN IRP (always 0x80=VENDOR_REQUEST_DATA_IN for IN IRPs)
     * @param INwIndex wIndex for IN IRP is start index for reading IRP at device end (usually zero even if offset is non-zero)
     * @param INwLength wLength is length of IN IRP to be read from device
     * @param INIrpAcceptShortPacket acceptShortPacket setting for IRP (this value only used for checking default value of true.
     * acceptShortPacket is NOT set in RoundTripTest)
     * @param INverifyAcceptShortPacket Specify whether to verify acceptShortPacket
     * @param INIrpLength Length of IN data buffer
     * @param INIrpOffset Offset at which to start writing IN data buffer
     * @param INIrpExpectedActualLength Actual length of data in IN buffer after submit complete.
     * @param INIrpExpectedException Expected exception
     * @param bTransformType TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01 (only transform used for this test)
     */

    private void RoundTripTestPossibleErrors(boolean SyncOrAsync, byte OUTbmRequestType, byte OUTbRequest, short OUTwValue, short OUTwIndex, short OUTwLength,
                                             boolean OUTIrpAcceptShortPacket, boolean OUTverifyAcceptShortPacket, int OUTIrpLength, int OUTIrpOffset, int OUTIrpExpectedActualLength,
                                             Exception OUTIrpExpectedException,
                                             byte INbmRequestType, byte INbRequest, short INwValue, short INwIndex, short INwLength,
                                             boolean INIrpAcceptShortPacket, boolean INverifyAcceptShortPacket, int INIrpLength, int INIrpOffset, int INIrpExpectedActualLength,
                                             UsbException INIrpExpectedException,
                                             byte bTransformType )
    {

        if ( SyncOrAsync == SYNC_SUBMIT )
        {
            VerifyIrpMethods.printDebug("RoundTripTestPossibleErrors -- SYNC");
        }
        else
        {
            VerifyIrpMethods.printDebug("RoundTripTestPossibleErrors -- ASYNC");
        }

        byte[] expectedData = null;     
        Exception caughtException = null;
        UsbDeviceEvent LastUsbDeviceEvent = null;

        assertNotNull("usbDevice is null, but should not be null.", usbDevice);

        //create Control IRP
        UsbControlIrp usbControlIrpOUT = usbDevice.createUsbControlIrp(OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex);

        //set data in control IRP OUT and data should remain unchanged after submission
        TransmitBuffer transmitBuffer = new TransmitBuffer(bTransformType, OUTwLength);
        byte[] OUTbuffer = transmitBuffer.getOutBuffer();
        usbControlIrpOUT.setData(OUTbuffer);

        //Don't check return on send because exceptions must be verified in this test
        caughtException = SendUsbControlIrp(SyncOrAsync, usbDevice, usbControlIrpOUT);

        //set generic LastUsbDeviceEvent
        LastUsbDeviceEvent = null;
        if ( LastUsbDeviceErrorEvent == null )
        {
            LastUsbDeviceEvent = LastUsbDeviceDataEvent;
        }
        else
        {
            LastUsbDeviceEvent = LastUsbDeviceErrorEvent;
        }

        if ( debug )
        {
            System.out.println("LastUsbDeviceDataEvent is " + LastUsbDeviceDataEvent);
            System.out.println("LastUsbDeviceErrorEvent is " + LastUsbDeviceErrorEvent);
            System.out.println("LastUsbDeviceEvent is " + LastUsbDeviceEvent);
        }

        //You can either have expected data or an expected exception; not both
        if ( OUTIrpExpectedException == null )
        {
            expectedData = transmitBuffer.getOutBuffer();
        }
        else
        {
            expectedData = null;
        }

        //verify OUT IRP after successful transmit
        VerifyIrpMethods.verifyUsbControlIrpAfterEvent(usbControlIrpOUT,
                                                       (EventObject)LastUsbDeviceEvent,
                                                       expectedData,
                                                       OUTIrpExpectedActualLength,
                                                       OUTIrpExpectedException,
                                                       OUTIrpAcceptShortPacket,
                                                       OUTverifyAcceptShortPacket,
                                                       OUTIrpOffset,
                                                       OUTIrpLength,
                                                       OUTbmRequestType,
                                                       OUTbRequest,
                                                       OUTwValue,
                                                       OUTwIndex);              
        /*
         * For syncSubmit, all exceptions should be thrown on submit and should equal expected exception.
         * For asyncSubmit, exceptions might not be thrown on submit so they will only be checked against 
         * expected exception if one was actually thrown.
         */
        if ( (SyncOrAsync == SYNC_SUBMIT) && (OUTIrpExpectedException != null) )
        {
            assertEquals("For sync submit, expected exceptions should be thrown on submit",
                         OUTIrpExpectedException, caughtException);
        }
        else if ( (SyncOrAsync == ASYNC_SUBMIT) && (caughtException != null) )
        {
            assertEquals("For async submit, any caught exceptions should be match expected exception",
                         OUTIrpExpectedException, caughtException);
        }

        //Only do IN if there was no error event on the out
        if ( LastUsbDeviceErrorEvent == null )
        {
            //Reset device events to null after verifications are complete
            LastUsbDeviceErrorEvent = null;
            LastUsbDeviceDataEvent = null;

            UsbControlIrp usbControlIrpIN = usbDevice.createUsbControlIrp(INbmRequestType, INbRequest, INwValue, INwIndex);

            //create data buffer in control IRP IN
            byte[] INBuffer = new byte[INwLength];
            usbControlIrpIN.setData(INBuffer);
            usbControlIrpIN.setAcceptShortPacket(INIrpAcceptShortPacket);

            //Don't check return on send because exceptions must be verified in this test
			caughtException = SendUsbControlIrp(SyncOrAsync, usbDevice, usbControlIrpIN);

            //set generic LastUsbDeviceEvent
            LastUsbDeviceEvent = null;
            if ( LastUsbDeviceErrorEvent == null )
            {
                LastUsbDeviceEvent = LastUsbDeviceDataEvent;
            }
            else
            {
                LastUsbDeviceEvent = LastUsbDeviceErrorEvent;
            }

            if ( debug )
            {
                System.out.println("LastUsbDeviceDataEvent is " + LastUsbDeviceDataEvent);
                System.out.println("LastUsbDeviceErrorEvent is " + LastUsbDeviceErrorEvent);
                System.out.println("LastUsbDeviceEvent is " + LastUsbDeviceEvent);
            }

            //You can either have expected data or an expected exception; not both 
            if ( INIrpExpectedException == null )
            {
                expectedData = transmitBuffer.getInBuffer();
            }
            else
            {
                expectedData = null;
            }

            //verify OUT IRP after successful transmit
            VerifyIrpMethods.verifyUsbControlIrpAfterEvent(usbControlIrpIN,
                                                           (EventObject) LastUsbDeviceEvent,
                                                           expectedData,
                                                           INIrpExpectedActualLength,
                                                           INIrpExpectedException,
                                                           INIrpAcceptShortPacket,
                                                           INverifyAcceptShortPacket,
                                                           INIrpOffset,
                                                           INIrpLength,
                                                           INbmRequestType,
                                                           INbRequest,
                                                           INwValue,
                                                           INwIndex);
            /*
             * For syncSubmit, all exceptions should be thrown on submit and should equal expected exception.
             * For asyncSubmit, exceptions might not be thrown on submit so they will only be checked against 
             * expected exception if one was actually thrown.
             */
            if ( (SyncOrAsync == SYNC_SUBMIT) && (OUTIrpExpectedException != null) )
            {
                assertEquals("For sync submit, expected exceptions should be thrown on submit",
                             OUTIrpExpectedException, caughtException);
            }
            else if ( (SyncOrAsync == ASYNC_SUBMIT) && (caughtException != null) )
            {
                assertEquals("For async submit, any caught exceptions should be match expected exception",
                             OUTIrpExpectedException, caughtException);
            }


        }
        //Reset device events to null after verifications are complete
        LastUsbDeviceErrorEvent = null;
        LastUsbDeviceDataEvent = null;
    };

    /**
     * The OneWayTestPossibleErrors is a USB Standard Request that is either OUT or IN
     * @param SyncOrAsync SYNC_SUBMIT or ASYNC_SUBMIT
     * @param SentbmRequestType bmRequestType for IRP to be sent
     * @param SentbRequest bRequest for IRP to be sent
     * @param SentwValue wValue for IRP to be sent
     * @param SentwIndex wIndex for IRP to be sent
     * @param SentwLength wLength for IRP to be sent
     * @param SentIrpAcceptShortPacket acceptShortPacket setting of IRP to be sent
     * @param SentIrpLength Length of byte[] data to be sent
     * @param SentIrpOffset Starting offset of byte[] data to be sent
     * @param SentData byte[] to be set in IRP
     * @param expectedbmRequestType expected bmRequestType
     * @param expectedbRequest expected bRequest
     * @param expectedwValue expected wValue
     * @param expectedwIndex expected wIndex
     * @param expectedwLength expected wLength
     * @param expectedAcceptShortPacket expected acceptShortPacket setting
     * @param verifyAcceptShortPacket Specify whether or not to verify acceptShortPacket
     * @param expectedLength expected unchanged length for byte[]
     * @param expectedOffset expected unchaged offset for byte[]
     * @param expectedActualLength expected actual length of data sent or received
     * @param expectedException expectedException
     * @param expectedData expected byte[]
     */
    private void OneWayTestPossibleErrors(boolean SyncOrAsync,byte SentbmRequestType, byte SentbRequest, short SentwValue, short SentwIndex, short SentwLength,
                                          boolean SentIrpAcceptShortPacket, int SentIrpLength, int SentIrpOffset, byte []SentData,
                                          byte expectedbmRequestType, byte expectedbRequest, short expectedwValue, short expectedwIndex, short expectedwLength,
                                          boolean expectedAcceptShortPacket, boolean verifyAcceptShortPacket, int expectedLength, int expectedOffset, int expectedActualLength,
                                          Exception expectedException,
                                          byte [] expectedData)
    {
        if ( SyncOrAsync == SYNC_SUBMIT )
        {
            VerifyIrpMethods.printDebug("OneWayTestPossibleErrors -- SYNC");
        }
        else
        {
            VerifyIrpMethods.printDebug("OneWayTestPossibleErrors -- ASYNC");
        }

        Exception caughtException = null;

        //make sure usbDevice has been set
        assertNotNull("usbDevice is null, but should not be null.", usbDevice);

        //create Control IRP
        UsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(SentbmRequestType, SentbRequest, SentwValue, SentwIndex);

        //set variable parts of Irp
        usbControlIrp.setAcceptShortPacket(SentIrpAcceptShortPacket);

        if ( SentData != null )
        {
            //if SentData is not null, then go ahead and set the data
            usbControlIrp.setData(SentData);

        }
        else
        {
            //if SentData is null, that is a signal to force the error condition where
            //a null buffer is set in the IRP
            try
            {
                usbControlIrp.setData(SentData);
                fail("java.lang.IllegalArgumentException should have been thrown, but no expception was thrown." );
            }
            catch ( java.lang.IllegalArgumentException e )
            {
                //should have come through here	
                VerifyIrpMethods.printDebug("Got illegal argument exception for attempting to set a null buffer.");
                return; //there will be nothing to verify
            }
            catch ( Exception e )
            {
                fail("java.lang.IllegalArgumentException should have been thrown, but expception " 
                     + e.getMessage() + " was thrown instead.");
            }

            //Don't verify return since we need to check exceptions for this test

        }

		caughtException = SendUsbControlIrp(SyncOrAsync, usbDevice, usbControlIrp);

        //set generic LAstUsbDeviceEvent
        UsbDeviceEvent LastUsbDeviceEvent = null;
        if ( LastUsbDeviceErrorEvent == null )
        {
            LastUsbDeviceEvent = LastUsbDeviceDataEvent;
        }
        else
        {
            LastUsbDeviceEvent = LastUsbDeviceErrorEvent;
        }

        if ( debug )
        {
            System.out.println("LastUsbDeviceDataEvent is " + LastUsbDeviceDataEvent);
            System.out.println("LastUsbDeviceDataEvent is " + LastUsbDeviceErrorEvent);
            System.out.println("LastUsbDeviceDataEvent is " + LastUsbDeviceEvent);
        }

        //verify OUT IRP after transmit
        VerifyIrpMethods.verifyUsbControlIrpAfterEvent(usbControlIrp,
                                                       (EventObject)LastUsbDeviceEvent,
                                                       expectedData,
                                                       expectedActualLength,
                                                       expectedException,
                                                       expectedAcceptShortPacket,
                                                       verifyAcceptShortPacket,
                                                       expectedOffset,
                                                       expectedLength,
                                                       expectedbmRequestType,
                                                       expectedbRequest,
                                                       expectedwValue,
                                                       expectedwIndex);
        /*
         * For syncSubmit, all exceptions should be thrown on submit and should equal expected exception.
         * For asyncSubmit, exceptions might not be thrown on submit so they will only be checked against 
         * expected exception if one was actually thrown.
         */
        if ( (SyncOrAsync == SYNC_SUBMIT) && (expectedException != null) )
        {
            assertEquals("For sync submit, expected exceptions should be thrown on submit",
                         expectedException.getClass(), caughtException.getClass());
        }
        else if ( (SyncOrAsync == ASYNC_SUBMIT) && (caughtException != null) )
        {
            assertEquals("For async submit, any caught exceptions should be match expected exception",
                         expectedException, caughtException);
        }

        //Reset device events to null after verifications are complete
        LastUsbDeviceErrorEvent = null;
        LastUsbDeviceDataEvent = null;
    };

    private UsbDeviceListener deviceListener = new   UsbDeviceListener()
    {
        public void dataEventOccurred(UsbDeviceDataEvent uddE)
        {
            numDataEvents++;
            assertNotNull(uddE);
            LastUsbDeviceDataEvent = uddE;
            if ( debug )
            {
                System.out.println("Data event occurred.  uddE = " + uddE + "numDataEvents = " + numDataEvents);
            }
        }
        public void errorEventOccurred(UsbDeviceErrorEvent udeE)
        {
            numExceptionEvents++;
            assertNotNull(udeE);
            LastUsbDeviceErrorEvent = udeE;
            if ( debug )
            {
                System.out.println("Error event occurred.  Exception is " + udeE.getUsbException().getMessage());
                System.out.println("Error event occurred.  udeE = " + udeE + "numErrorEvents = " + numExceptionEvents);
            }
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
     * @param usbDevice
     * @param usbControlIrp
     * @param caughtException
     * @return
     */
    private Exception SendUsbControlIrp(boolean SyncOrAsync, UsbDevice usbDevice, UsbControlIrp usbControlIrp)
    {
    	Exception caughtException = null;
        try
        {
            if ( SyncOrAsync == SYNC_SUBMIT )
            {
                usbDevice.syncSubmit(usbControlIrp);
            }
            else
            {
                usbDevice.asyncSubmit(usbControlIrp);
                usbControlIrp.waitUntilComplete(5000);
            }
            assertTrue("isComplete() not true for IRP after waitUntilComplete(..)", usbControlIrp.isComplete());

            numSubmits++;
        }
        catch ( Exception uE )
        {
            /* The exception sould indicate the reason for the failure.
             * For this example, we'll just stop trying.
             */
            if ( debug )
            {
                System.out.println("DCP submission failed : " + uE.getMessage());
            }
            caughtException = uE;
            //fail("DCP submission failed." + uE.getMessage());
            //System.out.println("DCP submission failed : " + uE.getMessage());

        }
        finally
        {
            try
            {
                /*
                 * Wait for device event before leaving submit routine
                 */
                for ( int i = 0; i < 100; i++ )
                {
                    if ( (LastUsbDeviceDataEvent != null)|
                         (LastUsbDeviceErrorEvent != null) )
                    {
                        //System.out.println("Data event took less than " + ((i+1) * 20 ) +" milliseconds");
                        break;
                    }
                    Thread.sleep( 20 ); //wait 20 ms before checkin for event
                }

                if ( (LastUsbDeviceDataEvent == null) && (LastUsbDeviceErrorEvent == null) )
                {
                    fail("Did not receive a data event or an error event after submit.");
                }
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
                //e.printStackTrace();
            }
        }
        return caughtException;
    };

    /*
     * Constructors
     */
    public DefaultControlPipeTestErrorConditions()
    {
        super();
    }
    protected DefaultControlPipeTestErrorConditions(UsbDevice newUsbDevice)
    {
        usbDevice = newUsbDevice;
    }

    private static final byte VENDOR_REQUEST_TRANSFER_DATA = (byte)0xB0;
    private static final short VENDOR_REQUEST_DATA_OUT = 0x00;
    private static final short VENDOR_REQUEST_DATA_IN = 0x80;
    private static final byte TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01;
    private static final byte TRANSFORM_TYPE_INVERT_BITS = (byte)0x02;
    private static final byte TRANSFORM_INVERT_ALTERNATE_BITS = (byte)0x03;


    private UsbDeviceDataEvent LastUsbDeviceDataEvent;
    private UsbDeviceErrorEvent LastUsbDeviceErrorEvent;
    private UsbDevice usbDevice;
//      private boolean debug = true;
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
