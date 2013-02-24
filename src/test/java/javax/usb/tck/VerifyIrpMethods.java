/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
package javax.usb.tck;

import junit.framework.Assert;

import java.util.*;

import javax.usb.*;
import javax.usb.event.*;

/**
 * VerifyIrpMethods
 * <p>
 * Helper functions to verify IRPs and Events
 *
 * @author Leslie Blair
 */
@SuppressWarnings("all")
public class VerifyIrpMethods
{
    private static void verifyDataEventValuesAndUsbIrpValuesWithExpectedValues(UsbIrp usbIrpSubmitted,
                                                                               EventObject usbEvent,
                                                                               byte[] expectedData)
    {
        printDebug("Entering verifyDataEventValuesAndUsbIrpValuesWithExpectedValues");

        UsbIrp usbEventIrp = null;
        byte[] usbEventData = null;

        //Verify usbDataEvent is not null
        Assert.assertNotNull("Event must not be null.", usbEvent);

        //Get the IRP from the Data event
        if ( UsbDeviceDataEvent.class == usbEvent.getClass() )
        {
            usbEventIrp = (((UsbDeviceDataEvent) usbEvent).getUsbControlIrp());
            usbEventData = (((UsbDeviceDataEvent) usbEvent).getData());
            Assert.assertNotNull("UsbDevice from Device event is unexpectedly null.",((UsbDeviceEvent)usbEvent).getUsbDevice());

        }
        else if ( UsbPipeDataEvent.class == usbEvent.getClass() )
        {
            usbEventIrp = (((UsbPipeDataEvent) usbEvent).getUsbIrp());
            usbEventData = (((UsbPipeDataEvent) usbEvent).getData());
        }

        if ( usbIrpSubmitted != null )
        {

            /*
             * Verify submitted IRP
             */
            Assert.assertTrue("isComplete() not true.",usbIrpSubmitted.isComplete());

            //Verify ActualLength as expected
            Assert.assertEquals("ActualLength not expected value.",
                                expectedData.length, usbIrpSubmitted.getActualLength());

            //Verify data in IRP
            Assert.assertTrue("IRP data not as expected", TransmitBuffer.compareTwoByteArraysForSpecifiedLength(
                                                                                                               usbIrpSubmitted.getData(),usbIrpSubmitted.getOffset(),
                                                                                                               expectedData, 0, expectedData.length));

            //Verify no exception
            Assert.assertFalse("isUsbException() not false.", usbIrpSubmitted.isUsbException());
            Assert.assertNull("getUsbException() not null.", usbIrpSubmitted.getUsbException());

            /*
             * Verify data event IRP and data
             */
            //Verify the IRP returned in data event is the same as IRP submitted
            Assert.assertSame("The submitted Irp and the usbIrp returned " +
                              "in event are not the same object",
                              usbIrpSubmitted, usbEventIrp);
        }
        else
        {
            printDebug("No IRP submitted for verification.  Test must by sync byte [].");
        }

        //Verify data in the IRP from data event (This is really covered in assert that submitted IRP and event IRP are the same.)
        Assert.assertTrue("Data from device data event IRP not expected data.",
                          TransmitBuffer.compareTwoByteArraysForSpecifiedLength(usbEventIrp.getData(),
                                                                                usbEventIrp.getOffset(),
                                                                                expectedData, 0,
                                                                                expectedData.length));

        //Note that the getData() from the DeviceDataEvent returns only the actual data transferred as opposed to the
        //getData() for the DeviceDataEvent IRP which contains the entire byte[] originally set in the IRP.
        //Therefore, the usbDeviceDataEvent.getData() should be an exact match for the expectedData.
        Assert.assertTrue("Data from UsbDeviceDataEvent not expected data.",
                          TransmitBuffer.compareTwoByteArraysForSpecifiedLength(usbEventData, 0,
                                                                                expectedData, 0, expectedData.length));

        printDebug("Leaving verifyDataEventValuesAndUsbIrpValuesWithExpectedValues");
    };


    private static void verifyErrorEventValuesAndUsbIrpValuesWithExpectedValues(UsbIrp usbIrpSubmitted,
                                                                                EventObject usbEvent,
                                                                                Exception expectedException)
    {
        printDebug("Entering verifyErrorEventValuesAndUsbIrpValuesWithExpectedValues");

        //Verify usbEvent is not null
        Assert.assertNotNull("Event must not be null.", usbEvent);

        // Verify submitted IRP
        Assert.assertTrue("isComplete() not true.",usbIrpSubmitted.isComplete());

        //Verify exception
        Assert.assertTrue("isUsbException() not true.", usbIrpSubmitted.isUsbException());
        Assert.assertNotNull("getUsbException() is null.", usbIrpSubmitted.getUsbException());

        //Verify expected exception
        if ( UsbDeviceErrorEvent.class == usbEvent.getClass() )
        {
            if ( (((UsbDeviceErrorEvent)usbEvent).getUsbException()).getClass() != expectedException.getClass() )
            {
                Assert.fail("\nExpected exception class is " + expectedException.getClass().toString() +
                            "\nReceived exception class is " + ((UsbDeviceErrorEvent)usbEvent).getUsbException().getClass().toString() );
            }
            Assert.assertNotNull("UsbDevice from Device event is unexpectedly null.",((UsbDeviceEvent)usbEvent).getUsbDevice());
        }
        else if ( UsbPipeErrorEvent.class == usbEvent.getClass() )
        {
            if ( (((UsbPipeErrorEvent)usbEvent).getUsbException()).getClass() != expectedException.getClass() )
            {
                Assert.fail("\nExpected exception class is " + expectedException.getClass().toString() +
                            "\nReceived exception class is " + ((UsbPipeErrorEvent)usbEvent).getUsbException().getClass().toString() );
            }
        }

/*Possible TODO -- if getUsb(Control)Irp method is added to DeviceErrorEvent and PipeErrorEvent classes
 * then the following code needs to be added to this method.  At that point you may consider combining this method with the
 * "verifyDataEventValuesAndUsbIrpValuesWithExpectedValues" method because there will be more common code.
 *
 
                UsbIrp usbEventIrp = null;
                byte[] usbEventData = null;
                //Get the IRP from the event
                if ( UsbDeviceErrorEvent.class == usbEvent.getClass())
                {
                        usbEventIrp = (((UsbDeviceErrorEvent) usbEvent).getUsbControlIrp());
                        usbEventData = (((UsbDeviceErrorEvent) usbEvent).getData());
                }
                else if (UsbPipeErrorEvent.class == usbEvent.getClass())
                {
                        usbEventIrp = (((UsbPipeErrorEvent) usbEvent).getUsbIrp());
                        usbEventData = (((UsbPipeErrorEvent) usbEvent).getData());
                }
 
 
 
                //Verify the IRP returned in event is the same as IRP submitted
                Assert.assertSame("The submitted usbControlIrp and the usbControlIrp returned " +
                                                "in event are not the same object",
                                                usbIrpSubmitted, usbEventIrp);
                //Assert.assertTrue("The submitted usbControlIrp and the usbControlIrp returned " +
                //                              "in UsbDeviceDataEvent are not equal",
                //                              usbIrpSubmitted.equals(usbEventIrp));
 
*/
        printDebug("Leaving verifyErrorEventValuesAndUsbIrpValuesWithExpectedValues");
    };

    private static void validateExpectations(byte [] expectedData, Exception expectedException) throws IllegalArgumentException
    {
        printDebug("Entering validateExpectations");

        /*
         * The verification to be performed is based on which value, expectedData or expectedException, is set.
         * One, and only one, of these values must be set.
         */
        if ( (expectedData != null) && (expectedException != null) )
        {
            throw new IllegalArgumentException("Both expectedData and expectedException are non-null.  If a data event " +
                                               "is expected, expectedData should be set.  If an error event is expected, expectedError should be set.  " +
                                               "Both values should not be set");
        }
        else if ( (expectedData == null) && (expectedException == null) )
        {
            throw new IllegalArgumentException("Both expectedData and expectedException are null.  If a data event " +
                                               "is expected, expectedData should be set.  If an error event is expected, expectedError should be set.  " +
                                               "One of the values must be set.");
        }

        printDebug("Leaving validateExpectations");
    }

    /**
     * verifyUsbIrpAfterEvent(..) verifies a UsbIrp against expected values
     * @param usbIrpSubmitted IRP to verify
     * @param usbEvent UsbDevice(or Pipe) Data or Error Event
     * @param expectedData expected byte[] in IRP (must be null if expected exception not null)
     * @param expectedActualLength actual length of data transmitted
     * @param expectedException expected exception (must be null if expected data is not null)
     * @param expectedAcceptShortPacket expected value of acceptShortPacket
     * @param verifyAcceptShortPacket Specifies whether to verify acceptShortPacket
     * @param expectedOffset expected offset for byte [] in IRP
     * @param expectedLength expected length for byte [] in IRP
     * @param expectedbmRequestType expected bmRequestType in control IRP
     * @param expectedbRequest expected bRequest in control IRP
     * @param expectedwValue expected wValue in control IRP
     * @param expectedwIndex expected wIndex in control IRP
     */
    protected static void verifyUsbControlIrpAfterEvent(UsbControlIrp usbControlIrpSubmitted,
                                                        EventObject usbEvent,
                                                        byte[] expectedData,
                                                        int expectedActualLength,
                                                        Exception expectedException,
                                                        boolean expectedAcceptShortPacket,
                                                        boolean verifyAcceptShortPacket,
                                                        int expectedOffset,
                                                        int expectedLength,
                                                        byte expectedbmRequestType,
                                                        byte expectedbRequest,
                                                        short expectedwValue,
                                                        short expectedwIndex)
    {
        printDebug("Entering verifyUsbControlIrpAfterEvent");

        //Verify UsbControlIrp values
        Assert.assertEquals("bmRequestType not expected value.",
                            expectedbmRequestType, usbControlIrpSubmitted.bmRequestType());
        Assert.assertEquals("bRequest not expected value.",
                            expectedbRequest, usbControlIrpSubmitted.bRequest());
        Assert.assertEquals("wValue not expected value.",
                            expectedwValue, usbControlIrpSubmitted.wValue());
        Assert.assertEquals("wIndex not expected value.",
                            expectedwIndex, usbControlIrpSubmitted.wIndex());

        //verify UsbIrp values
        VerifyIrpMethods.verifyUsbIrpAfterEvent(usbControlIrpSubmitted,
                                                usbEvent,
                                                expectedData,
                                                expectedActualLength,
                                                expectedException,
                                                expectedAcceptShortPacket,
                                                verifyAcceptShortPacket,
                                                expectedOffset,
                                                expectedLength);

        printDebug("Leaving verifyUsbControlIrpAfterEvent");
    };

    /**
     * verifyUsbIrpAfterEvent(..) verifies a UsbIrp against expected values
     * @param usbIrpSubmitted IRP to verify
     * @param usbEvent UsbDevice(or Pipe) Data or Error Event
     * @param expectedData expected byte[] in IRP (must be null if expected exception not null)
     * @param expectedActualLength actual length of data transmitted
     * @param expectedException expected exception (must be null if expected data is not null)
     * @param expectedAcceptShortPacket expected value of acceptShortPacket
     * @param verifyAcceptShortPacket Specifies whether to verify acceptShortPacket
     * @param expectedOffset expected offset for byte [] in IRP
     * @param expectedLength expected length for byte [] in IRP
     */
    protected static void verifyUsbIrpAfterEvent(UsbIrp usbIrpSubmitted,
                                                 EventObject usbEvent,
                                                 byte[] expectedData,
                                                 int expectedActualLength,
                                                 Exception expectedException,
                                                 boolean expectedAcceptShortPacket,
                                                 boolean verifyAcceptShortPacket,
                                                 int expectedOffset,
                                                 int expectedLength)
    {
        printDebug("Entering verifyUsbIrpAfterEvent");

        try
        {
            validateExpectations(expectedData, expectedException);
        }
        catch ( IllegalArgumentException iaE )
        {
            Assert.fail("Illegal Arguments supplied to verifyUsbIrpAfterEvent(...) method");
        }

        /*
         * Verify values in UsbIrp that should be unchanged after submission.
         * This verification is valid for a data event or error event
 *
         * Verify AcceptShortPacket, Offset, and Length not changed
         */
        if ( usbIrpSubmitted != null )
        {
            Assert.assertEquals("Offset changed after IRP submitted.", expectedOffset,  usbIrpSubmitted.getOffset());
            Assert.assertEquals("Length changed after IRP submitted.", expectedLength, usbIrpSubmitted.getLength());



            if ( verifyAcceptShortPacket )
            {
                Assert.assertEquals("AcceptShortPacket changed after IRP submitted.",
                                    expectedAcceptShortPacket, usbIrpSubmitted.getAcceptShortPacket());
            }
        }
        else
        {
            printDebug("No IRP submitted for verification.  Test must by sync byte [].");
        }


        /*
         * Data event verification
         */
        if ( expectedData != null )
        {
            //Verify expected values in UsbIrp after data event
            VerifyIrpMethods.verifyDataEventValuesAndUsbIrpValuesWithExpectedValues(usbIrpSubmitted,
                                                                                    usbEvent,
                                                                                    expectedData);
        }
        /*
         * Error event verification
         */
        else
        {
            //Verify expected values in UsbIrp after error event
            VerifyIrpMethods.verifyErrorEventValuesAndUsbIrpValuesWithExpectedValues(usbIrpSubmitted,
                                                                                     usbEvent,
                                                                                     expectedException);
        }
        printDebug("Leaving verifyUsbIrpAfterEvent");
    };


    /**
     * verifyRequestTest(..) verifies a UsbIrp against expected values
     * @param usbIrpSubmitted IRP to verify
     * @param expectedbmRequestType expected bmRequestType in control IRP
     * @param expectedbRequest expected bRequest in control IRP
     * @param expectedwValue expected wValue in control IRP
     * @param expectedwIndex expected wIndex in control IRP
     */
    protected static void verifyRequestTest(UsbControlIrp usbControlIrpSubmitted,
                                            byte expectedbmRequestType,
                                            byte expectedbRequest,
                                            short expectedwValue,
                                            short expectedwIndex)
    {
        printDebug("Entering verifyRequestTest");

        //Verify UsbControlIrp values
        Assert.assertEquals("bmRequestType not expected value.",
                            expectedbmRequestType, usbControlIrpSubmitted.bmRequestType());
        Assert.assertEquals("bRequest not expected value.",
                            expectedbRequest, usbControlIrpSubmitted.bRequest());
        Assert.assertEquals("wValue not expected value.",
                            expectedwValue, usbControlIrpSubmitted.wValue());
        Assert.assertEquals("wIndex not expected value.",
                            expectedwIndex, usbControlIrpSubmitted.wIndex());

    }

    /**
     * verifyRequestTestData(..) verifies a UsbIrp against expected values
     * @param usbIrpSubmitted IRP to verify
     * @param expectedbmRequestType expected bmRequestType in control IRP
     * @param expectedbRequest expected bRequest in control IRP
     * @param expectedwValue expected wValue in control IRP
     * @param expectedwIndex expected wIndex in control IRP
     * @param expectedLength expected length for byte [] in IRP
     * @param expectedData expected byte[] in IRP (must be null if expected exception not null)
     */
    protected static void verifyRequestTestData(UsbControlIrp usbControlIrpSubmitted,
                                                byte expectedbmRequestType,
                                                byte expectedbRequest,
                                                short expectedwValue,
                                                short expectedwIndex,
                                                int expectedLength)
    {
        byte[] buffer;
        printDebug("Entering verifyRequestTestData");

        //Verify UsbControlIrp values
        Assert.assertEquals("bmRequestType not expected value.",
                            expectedbmRequestType, usbControlIrpSubmitted.bmRequestType());
        Assert.assertEquals("bRequest not expected value.",
                            expectedbRequest, usbControlIrpSubmitted.bRequest());
        Assert.assertEquals("wValue not expected value.",
                            expectedwValue, usbControlIrpSubmitted.wValue());
        Assert.assertEquals("wIndex not expected value.",
                            expectedwIndex, usbControlIrpSubmitted.wIndex());
        Assert.assertEquals("Length changed after IRP submitted.",
                            expectedLength, usbControlIrpSubmitted.getLength());

    }


    /**
     * printDebug method will print the specified string if "debug" is true.
     * Useful function for debugging
     * @param infoString
     */
    protected static void printDebug(String infoString)
    {
        if ( debug )
        {
            System.out.println(infoString);
        }
    }
    private static boolean debug = false;
    //private static boolean debug = true;
}
