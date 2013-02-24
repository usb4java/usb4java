package javax.usb.tck;
 
import javax.usb.*;
import junit.framework.TestCase;
import junit.framework.Assert;
import javax.usb.util.*;
import java.util.*;
 
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
 * $P2           tck.rel1 041222 raulortz Redesign TCK to create base and optional
 *                                        tests. Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
 */
 
/**
* IRP Test
*
* This test  verifies the creation of IRP's and Control IRP's from the
* UsbDevice, UsbPipe, UsbDefaultIrp and UsbDefaultControlIrp classes.
*@author Vinitha Modepalle
*/
 
 
@SuppressWarnings("all")
public class IrpTest extends TestCase
{
 
    public void testUsbIrpwithPipeCreate()
    {
 
 
        try
        {
            //get Pipe
            UsbPipe inPipe = usbPipecreate();
            Assert.assertTrue("input pipe is not active!!!", inPipe.isActive());
            Assert.assertFalse("input pipe isOpen() should == false): ", inPipe.isOpen());
            inPipe.open();
            Assert.assertTrue("input pipe isOpen() should == true): ", inPipe.isOpen());
            UsbIrp usbIrp = inPipe.createUsbIrp();
            Assert.assertTrue( "Create UsbInPipe Irp Failed! Got a null instance", usbIrp != null );
 
            byte[] data = new byte[0];
            int offset = 0;
            int length = 0;
            boolean shortPacket = true;
            int actualLength = 0;
            boolean bIsUsbException = false;
            UsbException usbException = null;
            boolean bIsComplete = false;
 
 
            performIrpTests(usbIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                            usbException, bIsComplete);
 
            testWaitUntilComplete(usbIrp);
             
            inPipe.close();
			FindProgrammableDevice.getInstance().getProgrammableDevice().getUsbConfiguration((byte)1).getUsbInterface((byte)0).release();
 
        }
        catch ( UsbClaimException uce )
        {
            fail("UsbClaimException: " + uce);
        }
        catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        }
        catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        }
        catch ( UsbDisconnectedException uDE )                                                // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
        catch ( Exception e )
        {
            fail("Exception: " + e);
        }
 
 
    }
    public void testUsbControlIrpwithPipeCreate()
    {
        byte[] data = new byte[0];
        int offset = 0;
        int length = 0;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
        byte bmRequestType =UsbConst.REQUESTTYPE_DIRECTION_OUT |UsbConst.REQUESTTYPE_TYPE_STANDARD |UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        short wValue = UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
        short wIndex = 5;
 
        try
        {
 
            UsbPipe inPipe = usbPipecreate();
            Assert.assertTrue("input pipe is not active!!!", inPipe.isActive());
            Assert.assertFalse("input pipe isOpen() should == false: ", inPipe.isOpen());
            inPipe.open();
            Assert.assertTrue("input pipe isOpen() should == true: ", inPipe.isOpen());
            UsbControlIrp usbControlIrp = inPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            Assert.assertTrue( "Create UsbInPipe Control Irp Failed! Got a null instance", usbControlIrp != null );
 
            verifyControlIrpValues(usbControlIrp, bmRequestType, bRequest, wValue, wIndex);
            performIrpTests(usbControlIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                            usbException, bIsComplete);
 
 
            testWaitUntilComplete(usbControlIrp);
 
            inPipe.close();
			FindProgrammableDevice.getInstance().getProgrammableDevice().getUsbConfiguration((byte)1).getUsbInterface((byte)0).release();
            
        }
        catch ( UsbClaimException uce )
        {
            fail("UsbClaimException: " + uce);
        }
        catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        }
        catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        }
        catch ( UsbDisconnectedException uDE )                                                // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
        catch ( Exception e )
        {
            fail("Exception: " + e);
        }
 
 
    }
    public void testUsbControlIrpwithDeviceCreate()
    {
        byte[] data = new byte[0];
        int offset = 0;
        int length = 0;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
        byte bmRequestType =UsbConst.REQUESTTYPE_DIRECTION_OUT |UsbConst.REQUESTTYPE_TYPE_STANDARD |UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        short wValue = UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
        short wIndex = 5;
 
 
        UsbDevice usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        Assert.assertTrue( "Find Programmable board Failed! Got a null instance", usbDevice != null );
 
        UsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType,bRequest, wValue, wIndex);
        Assert.assertTrue( "Create UsbDevice Control Irp Failed! Got a null instance", usbControlIrp != null );
 
 
        verifyControlIrpValues(usbControlIrp, bmRequestType, bRequest, wValue, wIndex);
        performIrpTests(usbControlIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                        usbException, bIsComplete);
 
 
        testWaitUntilComplete(usbControlIrp);
 
 
 
 
    }
    public void testUsbIrpwithNoParmsConstructor()
    {
        /** This method is to create a UsbIrp using the
          * constructor DefaultUsbIrp.
          */
        byte[] data = new byte[0];
        int offset = 0;
        int length = 0;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
 
 
        UsbIrp usbIrp = new DefaultUsbIrp();
        Assert.assertTrue( "Create Usb Irp Failed! Got a null instance", usbIrp != null );
 
 
 
        performIrpTests(usbIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                        usbException, bIsComplete);
        testWaitUntilComplete(usbIrp);
 
 
    }
    public void testUsbIrpwithDataParmConstructor()
    {
        /** This method is to create a UsbIrp with Data using the
         * constructor DefaultUsbIrp(data)
         */
        byte[] data = {9,8,7,6,5,4,3,2,1};
        int offset = 0;
        int length = 9;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
 
 
        UsbIrp usbIrp = new DefaultUsbIrp(data);
        Assert.assertTrue( "Create Usb Irp with Data Failed! Got a null instance", usbIrp != null );
 
 
        performIrpTests(usbIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                        usbException, bIsComplete);
 
        testWaitUntilComplete(usbIrp);
 
 
    }
    public void testUsbIrpwithAllParmsConstructor()
    {
        /** This method is to create a UsbIrp with variables using the
          * constructor DefaultUsbIrp(data, offset, length, shortPacket)
          */
        byte[] data = {9,8,7,6,5,4,3,2,1};
        int offset = 2;
        int length = 4;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
 
 
        UsbIrp usbIrp = new DefaultUsbIrp(data, offset, length, shortPacket);
        Assert.assertTrue( "Create Usb Irp with all variables Failed! Got a null instance", usbIrp != null );
 
        performIrpTests(usbIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                        usbException, bIsComplete);
 
        testWaitUntilComplete(usbIrp);
 
 
 
 
    }
    public void testUsbControlIrpwithBasicParmsContructor()
    {
        /**This method is to create a usbControlIrp using the
         * constructor DefaultUsbControlIrp(bmRequestType, bRequest,wValue,wIndex)
         */
        byte[] data = new byte[0];
        int offset = 0;
        int length = 0;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
        byte bmRequestType =UsbConst.REQUESTTYPE_DIRECTION_OUT |UsbConst.REQUESTTYPE_TYPE_STANDARD |UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        short wValue = UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
        short wIndex = 5;
 
 
 
        UsbControlIrp usbControlIrp = new DefaultUsbControlIrp(bmRequestType, bRequest,wValue,wIndex);
        Assert.assertTrue( "Create Usb Control Irp Failed! Got a null instance", usbControlIrp != null );
        verifyControlIrpValues(usbControlIrp, bmRequestType, bRequest, wValue, wIndex);
        performIrpTests(usbControlIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                        usbException, bIsComplete);
 
        testWaitUntilComplete(usbControlIrp);
 
 
    }
    public void testUsbControlIrpwithAllParmsConstructor()
    {
        /**This method is to create a usbControlIrp with Data using the
            * constructor DefaultUsbControlIrp(data, offset, length,
            *shortPacket, bmRequestType,  bRequest, wValue, wIndex)
            */
 
        byte[] data = {9,8,7,6,5,4,3,2,1};
        int offset = 3;
        int length = 2;
        boolean shortPacket = true;
        int actualLength = 0;
        boolean bIsUsbException = false;
        UsbException usbException = null;
        boolean bIsComplete = false;
        byte bmRequestType =UsbConst.REQUESTTYPE_DIRECTION_OUT |UsbConst.REQUESTTYPE_TYPE_STANDARD |UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        short wValue = UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
        short wIndex = 5;
 
        UsbControlIrp usbControlIrp = new DefaultUsbControlIrp(data, offset, length, shortPacket, bmRequestType,  bRequest, wValue, wIndex);
        Assert.assertTrue( "Create Usb Control  Irp with Data Failed! Got a null instance", usbControlIrp != null );
 
        verifyControlIrpValues(usbControlIrp, bmRequestType, bRequest, wValue, wIndex);
        performIrpTests(usbControlIrp,data, offset, length, shortPacket,actualLength, bIsUsbException,
                        usbException, bIsComplete);
        testWaitUntilComplete(usbControlIrp);
 
 
    }
 
    public void performIrpTests(UsbIrp usbIrp, byte []data, int offset, int length, boolean shortPacket, int actualLength,
                                boolean bIsUsbException, UsbException usbException, boolean bIsComplete)
    {
 
 
        /*
         * Verify IRP values against values supplied in parameters
         */
 
 
        //verify that IRP was created with expected values
        verifyUsbIrpValues(usbIrp, data, offset, length, shortPacket, actualLength, bIsUsbException, usbException, bIsComplete);
 
 
 
        /*
         * Verify expected exceptions thrown when setting bad values
         */
        //sample IRP values to be set and verified
        byte[] samplebyteArrayData = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        int sampleOffset = 2;
        int sampleLength = 6;
        verifyExceptionsThrownForInvalidSetValues(usbIrp, samplebyteArrayData, sampleOffset, sampleLength);
 
 
 
 
        /*
         * Set various IRP values and verify that they were set properly
         */
        //IRP values to be set and verified
        byte[] byteArrayData = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        offset = 2;
        length = 6;
 
 
        shortPacket = false;
        actualLength = 6;
        bIsUsbException = true;
        usbException = new UsbShortPacketException();
        bIsComplete = true;
 
        //set and verify correct setting of IRP values
        setAndVerifyIrpParameters(usbIrp, byteArrayData, offset, length, shortPacket, actualLength, bIsUsbException, usbException, bIsComplete);
 
    }
 
 
    /**
     * This method creates a UsbDevice,
     * UsbInterface, UsbEndpoints
     *  and UsbPipe .
     */
    private UsbPipe usbPipecreate()
    {
        List endpoints = null;
        UsbDevice usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        Assert.assertTrue( "Find Programmable board Failed! Got a null instance", usbDevice != null );
        if ( usbDevice.isConfigured() )
        {
            UsbConfiguration usbConfiguration = usbDevice.getUsbConfiguration((byte)1);
            UsbInterface iface = usbConfiguration.getUsbInterface((byte)0);
            try
            {
                                                                                              // @P2D4
                iface.claim();
                assertTrue("isClaimed() should be true): ", iface.isClaimed());
                endpoints = iface.getUsbEndpoints();
                assertFalse("no endpoints found!", 0 == endpoints.size());
                List totalep = endpoints;
                UsbEndpoint  inEndpoint, outEndpoint;
                inEndpoint = findEndpoint(totalep, UsbConst.REQUESTTYPE_DIRECTION_IN,
                                          UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
                outEndpoint = findEndpoint(totalep,UsbConst.REQUESTTYPE_DIRECTION_OUT,
                                           UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
 
                assertFalse("could not find input endpoint", null == inEndpoint);
                assertFalse("could not find output endpoint", null == outEndpoint);
                UsbPipe iPipe = inEndpoint.getUsbPipe();
                return iPipe;
            }
            catch ( UsbClaimException uce )
            {
                fail("UsbClaimException: " + uce);
            }
            catch ( UsbNotActiveException unae )
            {
                fail("UsbNotActiveException: " + unae);
            }
            catch ( UsbException ue )
            {
                fail("UsbException: " + ue);
            }
            catch ( UsbDisconnectedException uDE )                                            // @P1A
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A
            catch ( Exception e )
            {
                fail("Exception: " + e);
            }
        }
        else
            fail("device is not configured!");
        return null;
 
 
 
    }
 
    /** This method is called by UsbPipeCreate method
     * to find an In Endpoint.
     */
    private UsbEndpoint  findEndpoint( List totalEndpoints, int inDirection, int inType)
    {
        boolean found =false;
        for ( int j = 0; j < totalEndpoints.size(); j++ )
        {
            UsbEndpoint ep = (UsbEndpoint)totalEndpoints.get(j);
            int direction = ep.getDirection();
            int type = ep.getType();
            if ( ((byte)(inDirection) == (byte)direction) &&  (inType == type) )
            {
                return  ep;
            }
 
        }
 
        fail("End point NOT FOUND!!!");
        return null;
 
    }
 
 
 
    private void testWaitUntilComplete(UsbIrp usbIrp)
    {
 
        /*
         * verify waitUntilComplete timeout
         */
        ThreadTest tt = new ThreadTest(usbIrp);
        do
                tt.run();
        while ( tt.donef ==false );
    }
 
 
 
 
 
 
    /**
    *This method is to conduct verifications on a UsbControlIrp values
    * @param usbControlIrp
    * @param bmreqtype
    * @param breq
    * @param wval
    * @param wind
    */
    private void verifyControlIrpValues(UsbControlIrp usbControlIrp,byte bmreqtype, byte breq, short wval, short wind)
    {
        Assert.assertEquals("bmRequestType does not match the value specified when UsbControlIrp was created.",bmreqtype,usbControlIrp.bmRequestType());
        Assert.assertEquals("bRequest does not match the value specified when UsbControlIrp was created.",breq,usbControlIrp.bRequest());
        Assert.assertEquals("wValue does not match the value specified when UsbControlIrp was created.",wval,usbControlIrp.wValue());
        Assert.assertEquals("wIndex does not match the value specified when UsbControlIrp was created.",wind,usbControlIrp.wIndex());
    }
 
    /**
     * The below procedure is to use the various set methods
     * on a ControlIrp/Irp and verify if the variables retain their
     * values after they are set.
   */
    private void setAndVerifyIrpParameters(UsbIrp usbIrp, byte[] allData, int oset, int lgth, boolean spacket, int actualLength, boolean bException, UsbException usbException,
                                           boolean bComplete)
    {
        //default IRP values
        int defaultOffset = 0;
        int defaultLength = allData.length;
        boolean defaultShortPacket = true;
        int defaultActualLength = 0;
        boolean defaultIsUsbException = false;
        UsbException defaultUsbException = null;
        boolean defaultIsComplete = false;
 
        //set data byte array only; verify values
        usbIrp.setData(allData);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        //set data byte array plus offset and length; verify values
        usbIrp.setData(allData, oset, lgth);
        verifyUsbIrpValues(usbIrp, allData, oset, lgth, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        //set data back to whole byte array only; verify values
        usbIrp.setData(allData);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        //set data back to whole byte array only and set offset and length separately; verify values
        usbIrp.setData(allData);
        usbIrp.setOffset(oset);
        usbIrp.setLength(lgth);
        verifyUsbIrpValues(usbIrp, allData, oset, lgth, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        //set accept short packet; verify value
        spacket = true;
        usbIrp.setData(allData);
        usbIrp.setAcceptShortPacket(spacket);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, spacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        spacket = false;
        usbIrp.setAcceptShortPacket(spacket);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, spacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        spacket = defaultShortPacket;  //set ShortPacket back to default for the rest of the tests
        usbIrp.setAcceptShortPacket(spacket);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        //call setComplete; verify value
        bComplete = true;
        usbIrp.setData(allData);
        usbIrp.setComplete(bComplete);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, spacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, bComplete);
 
        bComplete = false;
        usbIrp.setData(allData);
        usbIrp.setComplete(bComplete);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, bComplete);
 
        //call complete; verify value
        usbIrp.setData(allData);
        bComplete = true;
        usbIrp.complete();
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, bComplete);
 
        usbIrp.setData(allData);
        usbIrp.setComplete(defaultIsComplete); //set IsComplete back to default
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
 
 
        //call setActualLength
        usbIrp.setData(allData);
        int expectedActualLength = defaultActualLength;
        usbIrp.setActualLength(expectedActualLength);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, expectedActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        expectedActualLength = actualLength;
        usbIrp.setActualLength(expectedActualLength);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, expectedActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        usbIrp.setActualLength(defaultActualLength); //set actual Length back to default
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
        //call setUsbException; verify getUsbException and isUsbException
        usbIrp.setData(allData);
        UsbException expectedUsbException =  defaultUsbException;
        boolean expectedIsUsbException = defaultIsUsbException;
        usbIrp.setUsbException(expectedUsbException);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, expectedIsUsbException,
                           expectedUsbException, defaultIsComplete);
 
        expectedUsbException = usbException;
        expectedIsUsbException = bException;
        usbIrp.setUsbException(expectedUsbException);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, expectedIsUsbException,
                           expectedUsbException, defaultIsComplete);
 
        usbIrp.setUsbException(defaultUsbException);
        verifyUsbIrpValues(usbIrp, allData, defaultOffset, defaultLength, defaultShortPacket, defaultActualLength, defaultIsUsbException,
                           defaultUsbException, defaultIsComplete);
 
    }
    /**
     * Method verifies proper exceptions are thrown when setting invalid data in IRP
     */
    private void verifyExceptionsThrownForInvalidSetValues(UsbIrp usbIrp, byte[] allData, int oset, int lgth)
    {
        int negLength = -1;
        int negOffset = -1;
        byte[] nullByteArray = null;
        boolean expectedExceptionReceived = false;
 
        //null byte array in setData, offset, length
        try
        {
            usbIrp.setData(nullByteArray, oset, lgth);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (1) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
        //neg offset in setData, offset, length
        try
        {
            usbIrp.setData(allData, negOffset, lgth);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (1) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
        //neg length in setData, offset, length
        try
        {
            usbIrp.setData(allData, oset, negLength);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (2) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
 
        //null byte array in setData
        try
        {
            usbIrp.setData(nullByteArray);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (3) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
 
        //neg Length in setLength
        try
        {
            usbIrp.setData(allData);
            usbIrp.setLength(negLength);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (4) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
 
        //neg Offset in setOffset
        try
        {
            usbIrp.setData(allData);
            usbIrp.setOffset(negOffset);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (5) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
        //neg ActualLength in setActualLength
        try
        {
            usbIrp.setData(allData);
            usbIrp.setActualLength(negLength);
            expectedExceptionReceived = false;
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            expectedExceptionReceived = true;
        }
        catch ( Exception e )
        {
            expectedExceptionReceived = false;
        }
        Assert.assertTrue("Expected illegal argument exception (5) not received.", expectedExceptionReceived);
        expectedExceptionReceived = false;
 
    }
 
 
    /*
     * This method is to compare the data
     * @param usbControlIrp or usbIrp
     * @ param dat2.
   */
    private boolean comparebytearray(UsbIrp usbIrp, byte[] dat2)
    {
        byte[] dat1 ;
        dat1 = usbIrp.getData() ;
 
        if ( dat1.length != dat2.length )
        {
            return false;
        }
        else
        {
            for ( int i = 0;i<dat1.length;i++ )
            {
                if ( dat1[i] != dat2[i] )
                {
                    return false;
                }
            }
        }
        return true;
    }
 
    //-----------------------------------------------------------------------------------------------------------------------
    /**
     *This method is to conduct verifications on a UsbIrp
     * on variables used by the Irp
     * @param dat
     * @param oset
     * @param lgth
     * @param spacket.
     * This checks if the values of the above parameters retain their set values
     * after they are specified during the Irp creation or if they retain their
     * default values when they are not specified during the Irp creation.
     */
    private void verifyUsbIrpValues(UsbIrp usbIrp, byte[] dat, int oset, int lgth, boolean spacket, int actualLength, boolean bException,
                                    UsbException usbException, boolean bComplete)
    {
        Assert.assertNotNull("IRP byte array has null pointer.", usbIrp.getData());
        Assert.assertTrue("IRP byte array contents not as expected.", comparebytearray(usbIrp, dat));
        Assert.assertEquals("IRP offset is not as expected.", oset, usbIrp.getOffset());
        Assert.assertEquals("IRP length is not as expected.", lgth, usbIrp.getLength());
        Assert.assertEquals("IRP short packet setting is not as expected.", spacket, usbIrp.getAcceptShortPacket());
        Assert.assertEquals("IRP actual length is not as expected.", actualLength, usbIrp.getActualLength());
        Assert.assertEquals("IRP isUsbException() not as expected",bException, usbIrp.isUsbException());
        if ( usbException == null )
        {
            Assert.assertNull("IRP exception is not null when it is expected to be.", usbIrp.getUsbException());
        }
        else
        {
            //verify exception in IRP is the expected exception
            Assert.assertEquals("IRP exception is not as expected.", usbException.getClass(), usbIrp.getUsbException().getClass());
        }
        Assert.assertEquals("IRP isComplete() is not as expected.",bComplete, usbIrp.isComplete());
 
    }
 
 
 
    /*
     * Below code is to create a new thread and pass a reference to IRPwhich calls
     * the waitUntilComplete() method.
     */
 
    private class ThreadTest  extends TestCase
    {
        boolean donef = false;
        long actualtime;
        /*
         * Constructor of the class ThreadTest.
         */
        ThreadTest(UsbIrp usbIrp)
        {
 
            /*
             * Below we are creating a thread and pass a reference to IRP which calls
             * waitUntilComplete() and terminates after waitUntilComplete() returns.
          */
            MyThread1 mythread1 = new MyThread1(usbIrp);
            mythread1.start();
            try
            {
                for ( int i = 0; i<=2000; i = i + 200 )
                {
                    Thread.sleep(200);
                    if ( mythread1.isAlive() )
                    {
                        Thread.sleep(200); //sleep a little more
                        break;
                    }
                }
 
            }
            catch ( InterruptedException ie )
            {
                fail("Interrupted exception (1) not expected" + ie);
            }
            Assert.assertTrue("waitUntilComplete() in mythread1 terminated before complete() is called", mythread1.isAlive());
            /*
             * Now we call complete() and wait a second and verify thread has terminated.
             */
 
            if ( mythread1.isAlive() == true )
            {
                usbIrp.complete();
            }
 
            try
            {
                //thread should terminate in less than 1 sec
                for ( int i = 0; i<=1000; i = i + 200 )
                {
                    Thread.sleep(200);
                    if ( !mythread1.isAlive() )
                    {
                        break;
                    }
                }
 
 
            }
            catch ( InterruptedException ie )
            {
                fail("Interrupted exception (2) not expected" + ie);
            }
 
            Assert.assertFalse("waitUntilComplete() in mythread1 not terminated even after complete() is called", mythread1.isAlive());
 
 
            /*
             * Below we are creating a thread and pass a reference to IRP which calls
             * waitUntilComplete(100000) and terminates after waitUntilComplete(100000) returns.
          */
            MyThread2 mythread2 = new MyThread2(usbIrp);
            boolean complt = false;
            usbIrp.setComplete(complt);
            mythread2.start();
            /*
             * Below we put the thread to sleep for less than the timeout value
             * and make sure the thread does not terminate before the
             * timeout value.
             */
            try
            {
                MyThread2.sleep(2000); //waiting 2 seconds to make sure IRP does not complete on its own
            }
            catch ( InterruptedException ie )
            {
                fail("Interrupted exception (3) not expected" + ie);
            }
 
            Assert.assertTrue("waitUntilComplete() in mythread2 terminated before timeout value expired", mythread2.isAlive());
            /*
             * Now we call complete() and wait a second and verify thread has terminated.
             */
            usbIrp.complete();
            //wait a second
            try
            {
                //thread should terminate in less than 1 sec
                for ( int i = 0; i<=1000; i = i + 200 )
                {
                    Thread.sleep(200);
                    if ( !mythread2.isAlive() )
                    {
                        break;
                    }
                }
            }
            catch ( InterruptedException ie )
            {
                fail("Interrupted exception (4) not expected" + ie);
            }
            Assert.assertFalse("waitUntilComplete() in mythread2 not terminated even after Complete() is called", mythread2.isAlive());
            Assert.assertTrue("isComplete should be True after complete() is called in mythread2", usbIrp.isComplete());
 
            /*
           * Below we are creating a thread and pass a reference to IRP which calls
           * waitUntilComplete(5000) and terminates after waitUntilComplete(5000)
           * returns.
          */
            MyThread3 mythread3 = new MyThread3(usbIrp);
            usbIrp.setComplete(complt);
            mythread3.start();
            /*
           * Below we wait for the timeout value and check to see if the thread
           * terminated before the timeout value expired.
           */
            Assert.assertTrue("waitUntilComplete() in mythread3 terminated even before timeout value expired", mythread3.isAlive());
            try
            {
                //sleep for up to 10 seconds, but thread should terminate in 5.  checking for 5 seconds is done in MyThread3
                for ( int i = 0; i<=10000; i = i + 200 )
                {
                    Thread.sleep(200);
                    if ( !mythread3.isAlive() )
                    {
                        break;
                    }
                }
            }
            catch ( InterruptedException ie )
            {
                fail("Interrupted exception (5) not expected" + ie);
            }
            /*
            * Do not call complete().
            * Verify if the thread terminated after timeout value expired.
            * Verify if isComplete() returns False since complete() was not called.
            */
            Assert.assertFalse("waitUntilComplete() in mythread3 has not terminated even after timeout value expired", mythread3.isAlive());
            Assert.assertFalse("isComplete() should be False since Complete() was not called in mythread3", usbIrp.isComplete());
            donef = true;
        }
 
        /*
         * Start of a new thread with a reference to usbControlIrp
         * which calls waitUntilComplete() method.
         */
        class MyThread1 extends Thread
        {
 
            MyThread1(UsbIrp Usbirp)
            {
                usbIrp = Usbirp;
            }
            public void run()
            {
 
                usbIrp.waitUntilComplete();
            }
 
            private UsbIrp usbIrp;
        }
        /*
         * Start of a new thread with a reference to usbControlIrp
         * which calls waitUntilComplete(100000) method.
         */
 
        class MyThread2 extends Thread
        {
 
            MyThread2(UsbIrp usbIRP)
            {
                usbIrp = usbIRP;
            }
            public void run()
            {
 
                usbIrp.waitUntilComplete(100000);
            }
            private UsbIrp usbIrp;
 
        }
        /*
             * Start of a new thread with a reference to usbControlIrp
             * which calls waitUntilComplete(5000) method.
             */
 
        class MyThread3 extends Thread
        {
 
            MyThread3(UsbIrp usbIRP)
            {
                usbIrp = usbIRP;
            }
            public void run()
            {
 
                st = System.currentTimeMillis();
                usbIrp.waitUntilComplete(5000);
                et = System.currentTimeMillis();
                acttime = et -st;
                if ( acttime > 6000 )
                {
                    fail("Timeout value exceeded");
                }
                else
                {
                    done = true;
                }
            }
            private boolean done = false;
            private long st, et, acttime;
            private UsbIrp usbIrp;
        }
    }
 
}
