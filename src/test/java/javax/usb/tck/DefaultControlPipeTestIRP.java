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

import de.ailis.usb4java.test.TCKRunner;

import junit.framework.TestCase;

/**
 * Default Control Pipe Test -- Synchronous and asynchronous IRP submissions
 * <p>
 * This test verifies that control transfers operations work successfully
 * on the Default Control Pipe and that proper events are generated and proper
 * exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */


@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class DefaultControlPipeTestIRP extends TestCase
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

    public void testBuffersMultiplesOfMaxPacketSize_1packets()
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
                short OUTwLength = 64; //used later when creating data buffer

                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;
                Exception OUTexpectedException = null;


                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =OUTwLength;
                boolean INIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int INIrpLength = OUTwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;
                Exception INexpectedException = null;

                RoundTripTestIRP(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                 OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                 OUTexpectedException,
                                 INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                 INIrpAcceptShortPacket, INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                 INexpectedException,
                                 bTransformType );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };
    public void testBuffersMultiplesOfMaxPacketSize_2packets()
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
                short OUTwLength = 128; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;
                Exception OUTexpectedException = null;

                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =OUTwLength;
                // IRP data when IRP is created and data is set and should be unchanged after submission
                boolean INIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int INIrpLength = OUTwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;
                Exception INexpectedException = null;

                RoundTripTestIRP(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                 OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                 OUTexpectedException,
                                 INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                 INIrpAcceptShortPacket, INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                 INexpectedException,
                                 bTransformType );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    public void testBuffersMultiplesOfMaxPacketSize_moreThan2Packets()
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
                short OUTwLength = 192; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;
                Exception OUTexpectedException = null;

                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =OUTwLength;
                // IRP data when IRP is created and data is set and should be unchanged after submission
                boolean INIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int INIrpLength = OUTwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;
                Exception INexpectedException = null;

                RoundTripTestIRP(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                 OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                 OUTexpectedException,
                                 INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                 INIrpAcceptShortPacket, INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                 INexpectedException,
                                 bTransformType );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    public void testBuffersNotMultiplesOfMaxPacketSize_1packets()
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
                short OUTwLength = 50; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;
                Exception OUTexpectedException = null;

                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =OUTwLength;
                // IRP data when IRP is created and data is set and should be unchanged after submission
                boolean INIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int INIrpLength = OUTwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;
                Exception INexpectedException = null;

                RoundTripTestIRP(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                 OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                 OUTexpectedException,
                                 INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                 INIrpAcceptShortPacket, INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                 INexpectedException,
                                 bTransformType );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    public void testBuffersNotMultiplesOfMaxPacketSize_2packets()
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
                short OUTwLength = 120; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;
                Exception OUTexpectedException = null;

                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =OUTwLength;
                // IRP data when IRP is created and data is set and should be unchanged after submission
                boolean INIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int INIrpLength = OUTwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;
                Exception INexpectedException = null;

                RoundTripTestIRP(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                 OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                 OUTexpectedException,
                                 INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                 INIrpAcceptShortPacket, INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                 INexpectedException,
                                 bTransformType );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    public void testBuffersNotMultiplesOfMaxPacketSize_MoreThan2packets()
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
                short OUTwLength = 160; //used later when creating data buffer

                //set data in control IRP OUT and data should remain unchanged after submission
                byte bTransformType = TRANSFORM_TYPE_PASSTHROUGH;

                //IRP data when IRP is created and data is set and should be unchanged after submission
                boolean OUTIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean OUTverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int OUTIrpLength = OUTwLength;
                int OUTIrpOffset = 0;

                //Expected actual length of IRP after submission
                int OUTIrpExpectedActualLength = OUTwLength;
                Exception OUTexpectedException = null;

                /*******************************
                /Prepare IN IRP
                *********************************/
                //set Control IRP specific data
                byte INbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte INbRequest = VENDOR_REQUEST_TRANSFER_DATA;
                short INwValue = VENDOR_REQUEST_DATA_IN;
                short INwIndex = OUTwIndex;
                short INwLength =OUTwLength;
                // IRP data when IRP is created and data is set and should be unchanged after submission
                boolean INIrpAcceptShortPacket = true; //acceptShortPacket is set on an individual IRP basis; default is true
                boolean INverifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int INIrpLength = OUTwLength;
                int INIrpOffset = 0;

                // Expected actual length of IRP after submission
                int INIrpExpectedActualLength = INwLength;
                Exception INexpectedException = null;

                RoundTripTestIRP(transmitList[j], OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex, OUTwLength,
                                 OUTIrpAcceptShortPacket, OUTverifyAcceptShortPacket, OUTIrpLength,  OUTIrpOffset, OUTIrpExpectedActualLength,
                                 OUTexpectedException,
                                 INbmRequestType, INbRequest, INwValue, INwIndex, INwLength,
                                 INIrpAcceptShortPacket, INverifyAcceptShortPacket, INIrpLength, INIrpOffset, INIrpExpectedActualLength,
                                 INexpectedException,
                                 bTransformType );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    public void testRequestClearFeature()
    {
        usbDevice.addUsbDeviceListener(deviceListener);
        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                UsbInterface iface = usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) 0);

                try
                {
                    iface.claim();
                } catch ( UsbClaimException uCE )
                {
                    fail("Interface already claimed!");
                } catch ( UsbNotActiveException uNAE )
                {
                    fail("Configuration is not active!");
                } catch ( UsbDisconnectedException uDE )                                      // @P1C
                {                                                                             // @P1A
                    fail ("A connected device should't throw the UsbDisconnectedException!"); // @P1A
                } catch ( UsbException ue )                                                   // @P1C
                {
                    fail("Interface was not claimed!");
                }

                byte SentbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
                byte SentbRequest = UsbConst.REQUEST_CLEAR_FEATURE;

                short SentwValue = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
                short SentwIndex = 0x88;  //first isochronous IN endpoint
                short SentwLength = 0;

                byte[] SentData = new byte[SentwLength];
                int SentLength = SentwLength;
                int SentOffset = 0;
                boolean SentacceptShortPacket = true;

                byte expectedbmRequestType = SentbmRequestType;
                byte expectedbRequest =  SentbRequest;
                short expectedwValue = SentwValue;
                short expectedwIndex = SentwIndex;
                short expectedwLength = SentwLength;

                byte[] expectedData = new byte[SentwLength];
                int expectedLength = SentwLength;
                int expectedOffset = 0;
                boolean expectedAcceptShortPacket = SentacceptShortPacket;
                boolean verifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value
                int expectedActualLength = SentwLength;  //length of "Manufacturer" in unicode
                Exception expectedException = null;

                OneWayTestIRP(transmitList[j], SentbmRequestType, SentbRequest, SentwValue, SentwIndex, SentwLength,
                              SentacceptShortPacket, SentLength,  SentOffset, SentData,
                              expectedbmRequestType, expectedbRequest, expectedwValue, expectedwIndex, expectedwLength,
                              expectedAcceptShortPacket, verifyAcceptShortPacket, expectedLength, expectedOffset, expectedActualLength,
                              expectedException,
                              expectedData );

                try
                {
                    iface.release();
                } catch ( UsbClaimException uCE )
                {
                    fail("Interface was not claimed!");
                } catch ( UsbNotActiveException uNAE )
                {
                    fail("Configuration is not active!");
                } catch ( UsbException ue )
                {
                    fail("Interface was not released!");
                } catch ( UsbDisconnectedException uDE )                                      // @P1C
                {                                                                             // @P1A
                    fail ("A connected device should't throw the UsbDisconnectedException!"); // @P1A
                }                                                                             // @P1A
            }

        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    public void testRequestGetDescriptor()
    {
        usbDevice.addUsbDeviceListener(deviceListener);
        for ( int i=0; (i < iterations); i++ )
        {
            for ( int j=0; j<transmitList.length; j++ )
            {
                byte SentbmRequestType =
                    UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
                byte SentbRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
                short SentwValue = UsbConst.DESCRIPTOR_TYPE_STRING << 8;
                SentwValue += 1; //string index 1
                short SentwIndex = 0;
                short SentwLength = 64;

                byte[] SentData = new byte[SentwLength];
                int SentLength = SentwLength;
                int SentOffset = 0;
                boolean SentacceptShortPacket = true;

                byte expectedbmRequestType = SentbmRequestType;
                byte expectedbRequest =  SentbRequest;
                short expectedwValue = SentwValue;
                short expectedwIndex = SentwIndex;
                short expectedwLength = SentwLength;

                byte[] expectedData = manufacturerString;
                int expectedLength = SentwLength;
                int expectedOffset = 0;
                boolean expectedAcceptShortPacket = SentacceptShortPacket;
                boolean verifyAcceptShortPacket = true;  //since we are setting it or allowing it to default, we know the value

                int expectedActualLength = 26;  //length of "Manufacturer" in unicode
                Exception expectedException = null;

                OneWayTestIRP(transmitList[j], SentbmRequestType, SentbRequest, SentwValue, SentwIndex, SentwLength,
                              SentacceptShortPacket, SentLength,  SentOffset, SentData,
                              expectedbmRequestType, expectedbRequest, expectedwValue, expectedwIndex, expectedwLength,
                              expectedAcceptShortPacket, verifyAcceptShortPacket, expectedLength, expectedOffset, expectedActualLength,
                              expectedException,
                              expectedData );
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener);
    };

    /**The RoundTripTest is used when an OUT IRP is sent to the default control pipe followed by an IN IRP
     * retrieving the IRP from the device.
     * @param OUTbmRequestType bmRequestType used for OUT IRP
     * @param OUTbRequest bRequest used for OUT IRP (always 0xB0=VENDOR_REQUEST_TRANSFER_DATA)
     * @param OUTwValue wValue used for OUT IRP (always 0x00=VENDOR_REQUEST_DATA_OUT for OUT IRPs)
     * @param OUTwIndex wIndex for OUT IRP is start index for IRP at device end (usually zero even if offset is non-zero)
     * @param OUTwLength wLength is length of OUT IRP (set by setData() or setLength() method of IRP)
     * @param OUTIrpAcceptShortPacket acceptShortPacket setting for IRP (this value only used for checking default value of true.
     * acceptShortPacket is NOT set in RoundTripTest)
     * @param OUTverifyAcceptShortPacket Specify whether to verify acceptShortPacket
     * @param OUTIrpLength Length of IRP to be read from byte[].  Set indirectly by setData().  Same as wLength for this test,
     * @param OUTIrpOffset Offset in byte[] at which to start reading IRP.
     * @param OUTIrpExpectedActualLength ActualLength is the length of data actually transmitted in IRP. Same as wLength for OUT.
     * @param OUTexpectedException Specify any expected exception
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
     * @param INexpectedException Specify any expected exception
     * @param bTransformType TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01 (only transform used in this test)
     */
    private void RoundTripTestIRP(boolean SyncOrAsync, byte OUTbmRequestType, byte OUTbRequest, short OUTwValue, short OUTwIndex, short OUTwLength,
                                  boolean OUTIrpAcceptShortPacket, boolean OUTverifyAcceptShortPacket, int OUTIrpLength, int OUTIrpOffset, int OUTIrpExpectedActualLength,
                                  Exception OUTexpectedException,
                                  byte INbmRequestType, byte INbRequest, short INwValue, short INwIndex, short INwLength,
                                  boolean INIrpAcceptShortPacket, boolean INverifyAcceptShortPacket, int INIrpLength, int INIrpOffset, int INIrpExpectedActualLength,
                                  Exception INexpectedException,
                                  byte bTransformType )
    {
        if ( SyncOrAsync == SYNC_SUBMIT )
        {
            VerifyIrpMethods.printDebug("RoundTripTestIRP -- SYNC");
        } else
        {
            VerifyIrpMethods.printDebug("RoundTripTestIRP -- ASYNC");
        }

        assertNotNull("usbDevice is null, but should not be null.", usbDevice);

        //create Control IRP
        UsbControlIrp usbControlIrpOUT = usbDevice.createUsbControlIrp(OUTbmRequestType, OUTbRequest, OUTwValue, OUTwIndex);

        //set data in control IRP OUT and data should remain unchanged after submission
        TransmitBuffer transmitBuffer = new TransmitBuffer(bTransformType, OUTwLength);
        byte[] OUTbuffer = transmitBuffer.getOutBuffer();
        usbControlIrpOUT.setData(OUTbuffer);

        //send data
        if ( !SendUsbControlIrp(SyncOrAsync, usbDevice, usbControlIrpOUT) )
        {
            fail("Submitting the OUT IRP failed.");
            return;
        } else
        {
            //verify OUT IRP after successful transmit--dataEventReceived
            VerifyIrpMethods.verifyUsbControlIrpAfterEvent(usbControlIrpOUT,
                                                           (EventObject)LastUsbDeviceDataEvent,
                                                           transmitBuffer.getOutBuffer(),
                                                           OUTIrpExpectedActualLength,
                                                           OUTexpectedException,
                                                           OUTIrpAcceptShortPacket,
                                                           OUTverifyAcceptShortPacket,
                                                           OUTIrpOffset,
                                                           OUTIrpLength,
                                                           OUTbmRequestType,
                                                           OUTbRequest,
                                                           OUTwValue,
                                                           OUTwIndex);

            //Reset device events to null after verifications are complete
            LastUsbDeviceErrorEvent = null;
            LastUsbDeviceDataEvent = null;

            UsbControlIrp usbControlIrpIN = usbDevice.createUsbControlIrp(INbmRequestType, INbRequest, INwValue, INwIndex);

            //create data buffer in control IRP IN
            byte[] INBuffer = new byte[INwLength];
            usbControlIrpIN.setData(INBuffer);

            //send data
            if ( !SendUsbControlIrp(SyncOrAsync, usbDevice, usbControlIrpIN) )
            {
                fail("Submitting the IN IRP failed.");
                return;
            } else
            {
                VerifyIrpMethods.verifyUsbControlIrpAfterEvent(usbControlIrpIN,
                                                               (EventObject) LastUsbDeviceDataEvent,
                                                               transmitBuffer.getInBuffer(),
                                                               INIrpExpectedActualLength,
                                                               INexpectedException,
                                                               INIrpAcceptShortPacket,
                                                               INverifyAcceptShortPacket,
                                                               INIrpOffset,
                                                               INIrpLength,
                                                               INbmRequestType,
                                                               INbRequest,
                                                               INwValue,
                                                               INwIndex);

                //Reset device events to null after verifications are complete
                LastUsbDeviceErrorEvent = null;
                LastUsbDeviceDataEvent = null;
            }
        }
    };


    /**
     * The OneWayTestIRP is a USB Standard Request that is either OUT or IN
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
    private void OneWayTestIRP(boolean SyncOrAsync,byte SentbmRequestType, byte SentbRequest, short SentwValue, short SentwIndex, short SentwLength,
                               boolean SentIrpAcceptShortPacket, int SentIrpLength, int SentIrpOffset, byte []SentData,
                               byte expectedbmRequestType, byte expectedbRequest, short expectedwValue, short expectedwIndex, short expectedwLength,
                               boolean expectedAcceptShortPacket, boolean verifyAcceptShortPacket, int expectedLength, int expectedOffset, int expectedActualLength,
                               Exception expectedException,
                               byte [] expectedData )
    {
        if ( SyncOrAsync == SYNC_SUBMIT )
        {
            VerifyIrpMethods.printDebug("OneWayTestIRP -- SYNC");
        } else
        {
            VerifyIrpMethods.printDebug("OneWayTestIRP -- ASYNC");
        }

        //make sure usbDevice has been set
        assertNotNull("usbDevice is null, but should not be null.", usbDevice);

        //create Control IRP
        UsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(SentbmRequestType, SentbRequest, SentwValue, SentwIndex);

        //set variable parts of Irp
        usbControlIrp.setData(SentData);
        usbControlIrp.setAcceptShortPacket(SentIrpAcceptShortPacket);

        //send data
        if ( !SendUsbControlIrp(SyncOrAsync, usbDevice, usbControlIrp) )
        {
            fail("Submitting the OneWayTest IRP failed.");
            return;
        } else
        {
            //verify OUT IRP after successful transmit
            VerifyIrpMethods.verifyUsbControlIrpAfterEvent(usbControlIrp,
                                                           (EventObject)LastUsbDeviceDataEvent,
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

            //Reset device events to null after verifications are complete
            LastUsbDeviceErrorEvent = null;
            LastUsbDeviceDataEvent = null;
        }
    };

    private UsbDeviceListener deviceListener = new   UsbDeviceListener()
    {
        public void dataEventOccurred(UsbDeviceDataEvent uddE)
        {
            numDataEvents++;
            assertNotNull(uddE);
            LastUsbDeviceDataEvent = uddE;
        }
        public void errorEventOccurred(UsbDeviceErrorEvent udeE)
        {
            numExceptionEvents++;
            assertNotNull(udeE);
            LastUsbDeviceErrorEvent = udeE;
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
     * @param usbDevice
     * @param usbControlIrp
     * @param caughtException
     * @return
     */
    private boolean SendUsbControlIrp(boolean SyncOrAsync, UsbDevice usbDevice, UsbControlIrp usbControlIrp)
    {
        try
        {
            /* This will block until the submission is complete.
             * Note that submissions (except interrupt and bulk in-direction)
             * will not block indefinitely, they will complete or fail within
             * a finite amount of time.  See MouseDriver.HidMouseRunnable for more details.
             */
            if ( SyncOrAsync == SYNC_SUBMIT )
            {
                usbDevice.syncSubmit(usbControlIrp);
            } else
            {
                usbDevice.asyncSubmit(usbControlIrp);
                usbControlIrp.waitUntilComplete(5000);
            }
            assertTrue("isComplete() not true for IRP after waitUntilComplete(..)", usbControlIrp.isComplete());
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
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
                //e.printStackTrace();
            }
            assertNotNull("Did not receive a DeviceDataEvent after sleep.", LastUsbDeviceDataEvent);
            assertNull("Unexpected DeviceErrorEvent received after submit", LastUsbDeviceErrorEvent);
            numSubmits++;
            return true;
        } catch ( UsbException uE )
        {
            /* The exception sould indicate the reason for the failure.
             * For this example, we'll just stop trying.
             */
            fail("DCP submission failed." + uE.getMessage());
            //System.out.println("DCP submission failed : " + uE.getMessage());
            return false;
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
	    return false;                                                                     // @P1A
        }                                                                                     // @P1A
    };


    /**
     * Constructor
     */
    public DefaultControlPipeTestIRP()
    {
    };

    protected DefaultControlPipeTestIRP(UsbDevice newUsbDevice)
    {
        usbDevice = newUsbDevice;
    };


    private static final byte VENDOR_REQUEST_TRANSFER_DATA = (byte)0xB0;
    private static final short VENDOR_REQUEST_DATA_OUT = 0x00;
    private static final short VENDOR_REQUEST_DATA_IN = 0x80;
    private static final byte TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01;
    private static final byte TRANSFORM_TYPE_INVERT_BITS = (byte)0x02;
    private static final byte TRANSFORM_INVERT_ALTERNATE_BITS = (byte)0x03;
    private UsbDeviceDataEvent LastUsbDeviceDataEvent;
    private UsbDeviceErrorEvent LastUsbDeviceErrorEvent;
    private UsbDevice usbDevice;
    // private boolean debug = true;
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
