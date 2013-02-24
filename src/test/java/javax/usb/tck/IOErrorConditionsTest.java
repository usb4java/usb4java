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
 * $P2           tck.rel1 040916 raulortz Redesign TCK to create base and optional
 *                                        tests. Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
 */

import java.util.*;
import javax.usb.*;
import javax.usb.util.*;

import org.junit.Ignore;

import junit.framework.TestCase;

/**
 * IO Error conditions
 * <p>
 * This test verifies that error conditions are generated and proper
 * exceptions are thrown in the operation.
 *
 * @author Thanh Ngo
 */

@SuppressWarnings("all")
@Ignore("These tests are triggered by other tests and must not run directly")
public class IOErrorConditionsTest extends TestCase
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    public IOErrorConditionsTest(String name)
    {
        super(name);
    }
    public IOErrorConditionsTest(int typeOfTest)
    {
        testType = typeOfTest;
    }

    //-------------------------------------------------------------------------
    // Protected overridden methods
    //

    protected void setUp() throws Exception {
        printDebug("setUp");


        usbDevice = programmableDevice.getProgrammableDevice();
        assertTrue("Find Programmable board Failed! Got a null instance",
                   usbDevice != null);


        super.setUp();
    }

    protected void tearDown() throws Exception {
        printDebug("tearDown");
        try                                                                                   // @P1A
        {                                                                                     // @P1A
            if ( (outPipe!=null)&&(outPipe.isOpen()) )                                        // @P1C
            {                                                                                 // @P1C
                outPipe.abortAllSubmissions();                                                // @P1C
                IOMethods.closePipe(outPipe);                                                 // @P1C
            }                                                                                 // @P1C
            if ( (inPipe!=null)&&(inPipe.isOpen()) )                                          // @P1C
            {                                                                                 // @P1C
                inPipe.abortAllSubmissions();                                                 // @P1C
                IOMethods.closePipe(inPipe);                                                  // @P1C
            }                                                                                 // @P1C
            if ( (myIface!=null)&&(myIface.isClaimed()) )                                     // @P1C
            {                                                                                 // @P1C
                IOMethods.releaseInterface(myIface);                                          // @P1C
            }                                                                                 // @P1C
        } catch ( UsbDisconnectedException uDE )                                              // @P1A
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        super.tearDown();
    }

    //-------------------------------------------------------------------------
    // Public testXyz() methods
    //

    /**
     * Null Data Buffer
     */
    public void testNullDataBufferInIrp()
    {
        printDebug("testNullDataBufferInIrp");
        if ( testType == bulkTest )
        {
            doTestNullDataBufferInIrp(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {

            doTestNullDataBufferInIrp(UsbConst.ENDPOINT_TYPE_INTERRUPT);

        } else if ( testType == isochronousTest )
        {
            doTestNullDataBufferInIrp(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);

        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }
    }
    /**
     * Null Data Buffer
     */
    public void testNullDataBufferAsByteArray()
    {
        printDebug("testNullDataBufferAsByteArray");
        if ( testType == bulkTest )
        {
            doTestNullDataBufferAsByteArray(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {
            doTestNullDataBufferAsByteArray(UsbConst.ENDPOINT_TYPE_INTERRUPT);

        } else if ( testType == isochronousTest )
        {
            doTestNullDataBufferAsByteArray(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);

        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }
    }

    /**
     * Action against a closed pipe
     */
    public void testActionAgainstClosePipeAbortAllSubmissions()
    {
        printDebug("testActionAgainstClosePipeAbortAllSubmissions");
        if ( testType == bulkTest )
        {

            doTestActionAgainstClosePipeAbortAllSubmissions(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {
            doTestActionAgainstClosePipeAbortAllSubmissions(UsbConst.ENDPOINT_TYPE_INTERRUPT);
        } else if ( testType == isochronousTest )
        {
            doTestActionAgainstClosePipeAbortAllSubmissions(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }


    }
    /**
     * Action against a closed pipe
     */
    public void testActionAgainstClosePipeSubmit()
    {
        printDebug("testActionAgainstClosePipeSubmit");
        if ( testType == bulkTest )
        {

            doTestActionAgainstClosePipeSubmit(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {
            doTestActionAgainstClosePipeSubmit(UsbConst.ENDPOINT_TYPE_INTERRUPT);
        } else if ( testType == isochronousTest )
        {
            doTestActionAgainstClosePipeSubmit(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }


    }

    /**
     * Close a pipe with pending operation
     */
    public void testClosePipePendingAction()

    {
        printDebug("testClosePipePendingAction");
        if ( testType == bulkTest )
        {
            doTestClosePipePendingAction(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {
            doTestClosePipePendingAction(UsbConst.ENDPOINT_TYPE_INTERRUPT);
        } else if ( testType == isochronousTest )
        {
            doTestClosePipePendingAction(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }


    }

    /**
     * Open a pipe on an inactive interface
     */
    public void testOpenPipeOnInactiveAlternateSetting()
    {
        printDebug("testOpenPipeOnInactiveAlternateSetting");
        if ( testType == bulkTest )
        {

            doTestOpenPipeOnInactiveAlternateSetting(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {
            doTestOpenPipeOnInactiveAlternateSetting(UsbConst.ENDPOINT_TYPE_INTERRUPT);
        } else if ( testType == isochronousTest )
        {
            doTestOpenPipeOnInactiveAlternateSetting(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }
    }

    /**
     * Open a pipe on an unclaimed interface
     */
    public void testOpenPipeOnUnclaimedInterface()
    {
        printDebug("testOpenPipeOnUnclaimedInterface");
        if ( testType == bulkTest )
        {

            doTestOpenPipeOnUnclaimedInterface(UsbConst.ENDPOINT_TYPE_BULK);
        } else if ( testType == intTest )
        {
            doTestOpenPipeOnUnclaimedInterface(UsbConst.ENDPOINT_TYPE_INTERRUPT);
        } else if ( testType == isochronousTest )
        {
            doTestOpenPipeOnUnclaimedInterface(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS);
        } else
        {
            fail("Test is defined for only bulk and isochronous endpoints.");
        }

    }

    /**
     * Claim an already claimed interface
     */
    public void testClaimAnAlreadyClaimedInterface()
    {
        printDebug("testClaimAnAlreadyClaimedInterface");


        myIface = null;
        boolean usbe = false;

        if ( usbDevice.isConfigured() )
        {
            UsbConfiguration usbConfiguration =
                usbDevice.getUsbConfiguration((byte) 1);
            myIface = usbConfiguration.getUsbInterface((byte) 0);

            //claim the interface
            IOMethods.claimInterface(myIface);

            try
            {
                assertTrue("in doTestClaimAnAlreadyClaimedInterface() --isClaimed() should == true): ",
                           myIface.isClaimed());
                myIface.claim();
                fail("should caused UsbClaimException!");
            } catch ( UsbClaimException uce )
            {

                usbe = true;
            } catch ( UsbException ue )
            {
                fail("UsbClaimException expected.  unexpected UsbException!" + ue);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( Exception e )                                                           // @P1C
            {
                fail("UsbClaimException expected.  unexpected Exception" + e);
            }

            if ( !usbe )
                assertTrue("Test Claim an already claimed interface did not throw UsbNotClaimedException!", false);

        } else
        {
            fail("device is not configured!");
        }



    }

//    /**
//     * Claim an inactive interface
//     */
//    public void testClaimAnInactiveInterface()
//    {
//        boolean usbe = false;
//        UsbInterface inActiveInterface = getInactiveInterface();
//        if ( null == inActiveInterface )
//        {
//            fail("could not locate an inactive interface to test!");
//        }
//        try
//        {
//            inActiveInterface.claim();
//            fail("should throw Exception when claim an inactive interface!");
//        }
//        catch ( UsbClaimException uce )
//        {
//        }
//        catch ( UsbException ue )
//        {
//        }
//        catch ( UsbNotActiveException unae )
//        {
//        }
//    }


    /**
     * private functions
     */

    /**
     * Test error conditions -- Null Data Buffer
     */
    private void doTestNullDataBufferAsByteArray(int endpointType)
    {
        outPipe = null;
        UsbEndpoint outEndpoint = null;

        //UsbInterface myIface = null;
        UsbIrp usbIrp = null;
        boolean usbe = false;


        if ( usbDevice.isConfigured() )
        {

            outEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_OUT);

            assertFalse("could not find output endpoint", null == outEndpoint);

                                                                                              // @P2D4
            assertTrue("Interface was not claimed!", myIface.isClaimed());

            //we have an endpoint; now try submitting a null byte array and an IRP
            //with a null data

            outPipe = outEndpoint.getUsbPipe();
            assertTrue("output pipe is not active!!!", outPipe.isActive());
            IOMethods.openPipe(outPipe);

            //submit null byte array 
            byte [] nullBuffer = null;
            try
            {
                outPipe.syncSubmit(nullBuffer);
                fail("Expected a UsbException when submitting a null byte array.");
            } catch ( IllegalArgumentException iAE )
            {
                usbe = true;
            } catch ( UsbException uE )
            {
                fail("java.lang.IllegalArgumentException was expected.  Unexpected UsbException on submitting null buffer:" + uE);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( Exception e )                                                           // @P1C
            {
                fail("java.lang.IllegalArgumentException was expected.  Unexpected generic exception." + e);
            }

            assertTrue("java.lang.IllegalArgumentException not received when submitting a null byte []", usbe);


        } else
        {
            fail ("UsbDevice is not configured!");
        }


    }
    /**
     * Test error conditions -- Null Data Buffer
     */
    private void doTestNullDataBufferInIrp(int endpointType)
    {

        outPipe = null;
        UsbEndpoint outEndpoint = null;

        //UsbInterface myIface = null;
        UsbIrp usbIrp = null;
        boolean usbe = false;


        if ( usbDevice.isConfigured() )
        {

            outEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_OUT);

            assertFalse("could not find output endpoint", null == outEndpoint);

                                                                                              // @P2D4
            assertTrue("Interface was not claimed!", myIface.isClaimed());

            //we have an endpoint; now try submitting a null byte array and an IRP
            //with a null data

            outPipe = outEndpoint.getUsbPipe();
            assertTrue("output pipe is not active!!!", outPipe.isActive());
            IOMethods.openPipe(outPipe);

            //submit null byte array 
            byte [] nullBuffer = null;


            //submit IRP with null data 

            usbIrp = outPipe.createUsbIrp();

            try
            {
                usbIrp.setData(nullBuffer);
                fail("Expected a UsbException when setting null data in IRP.");
                outPipe.syncSubmit(usbIrp);
                fail("Expected a UsbException when submitting an IRP with a null byte array in IRP.");
            } catch ( IllegalArgumentException iAE )
            {
                usbe = true;
            } catch ( UsbException uE )
            {
                fail("java.lang.IllegalArgumentException was expected.  Unexpected UsbException on settin null data in IRP:" + uE);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( Exception e )                                                           // @P1C
            {
                fail("java.lang.IllegalArgumentException was expected.  Unexpected generic exception." + e);
            }

            assertTrue("java.lang.IllegalArgumentException not received when submitting a null byte [] in IRP", usbe);

        } else
        {
            fail ("UsbDevice is not configured!");
        }

    }

    /**
     * Test error conditions -- Action against a closed pipe
     */
    private void doTestActionAgainstClosePipeAbortAllSubmissions(int endpointType)
    {



        outPipe = null;
        UsbEndpoint outEndpoint = null;
        boolean usbe = false;


        if ( usbDevice.isConfigured() )
        {

            outEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_OUT);

            assertFalse("could not find output endpoint", null == outEndpoint);
            outPipe = outEndpoint.getUsbPipe();
            assertTrue("output pipe is not active!!!", outPipe.isActive());
            assertFalse("pipe should not be open.", outPipe.isOpen());

                                                                                              // @P2D4
            assertTrue("Interface was not claimed!", myIface.isClaimed());


            try
            {
                outPipe.abortAllSubmissions();  // should throw UsbNotOpenException
                assertFalse("Pipe should have been closed at this part of the test.", outPipe.isOpen());
                fail("abortAllSumissions should have thrown a UsbNotOpenException when called on a closed pipe.");
            } catch ( UsbNotOpenException ue )
            {
                usbe = true;
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( Exception e )                                                           // @P1C
            {
                fail ("UsbNotOpenException was expected.  Unexpected exception:  " + e);
            }



            if ( !usbe )
                assertTrue("Test Action Agains Close Pipe abortAllSubmission() did not throw UsbNotOpenException!", false);

        }

        else
        {
            fail ("UsbDevice is not configured!");
        }

    }
    /**
     * Test error conditions -- Action against a closed pipe
     */
    private void doTestActionAgainstClosePipeSubmit(int endpointType)
    {

        outPipe = null;
        UsbEndpoint outEndpoint = null;
        boolean usbe = false;


        if ( usbDevice.isConfigured() )
        {

            outEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_OUT);

            assertFalse("could not find output endpoint", null == outEndpoint);
            outPipe = outEndpoint.getUsbPipe();
            assertTrue("output pipe is not active!!!", outPipe.isActive());
            assertFalse("pipe should not be open.", outPipe.isOpen());

                                                                                              // @P2D4
            assertTrue("Interface was not claimed!", myIface.isClaimed());


            try
            {
                byte [] data = {1,2,3,4,5};
                outPipe.syncSubmit(data);
                assertFalse("Pipe should have been closed at this part of the test.", outPipe.isOpen());
                fail("Submit should have thrown a UsbNotOpenException when called on a closed pipe.");
            } catch ( UsbNotOpenException ue )
            {
                usbe = true;
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( Exception e )                                                           // @P1A
            
            {
                fail ("UsbNotOpenException was expected.  Unexpected exception:  " + e);
            }


            if ( !usbe )
                assertTrue("Test Action Agains Close Pipe Submit did not throw UsbNotOpenException!", false);

        }

        else
        {
            fail ("UsbDevice is not configured!");
        }






    }

    /**
     * Test error conditions -- Close a pipe with pending operation
     */
    private void doTestClosePipePendingAction(int endpointType)
    {

        inPipe = null;
        boolean usbe = false;
        UsbEndpoint inEndpoint = null;

        // get an out endpoint
        if ( usbDevice.isConfigured() )
        {

            inEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_IN);

            assertFalse("could not find endpoint", null == inEndpoint);

            inPipe = inEndpoint.getUsbPipe();
            assertTrue("output pipe is not active!!!", inPipe.isActive());
            assertFalse("pipe should not be open.", inPipe.isOpen());

                                                                                              // @P2D4
            assertTrue("Interface was not claimed!", myIface.isClaimed());

            //	open the pipe
            IOMethods.openPipe(inPipe);
            assertTrue("pipe should be opened", inPipe.isOpen());

            try
            {
                //List listOfIrps = new ArrayList();
                List inList = new ArrayList();
                UsbIrp inIrp = null;

                for ( int i = 0; i < 1000; i++ )
                {

                    inIrp = inPipe.createUsbIrp();
                    inIrp.setData(new byte[inPipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize()]); // @P2A
                    inList.add(inIrp);
                }
                inPipe.asyncSubmit(inList);
                inPipe.close();                 // should throw UsbException
                fail ("UsbException should have been thrown when closing pipe with pending submissions.");
            } catch ( UsbException ue )
            {
                usbe = true;

            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( Exception e )                                                           // @P1C
            {
                fail("Unexpected Exception:  " + e);
            }
            if ( !usbe )
                assertTrue("Test Close Pipe With Pending Operations usbPipe.close() did not throw UsbException!", false);


        } else
        {
            fail ("UsbDevice is not configured!");
        }

    }

    /**
     * Test error conditions -- Open a pipe on an inactive interface
     */
    private void doTestOpenPipeOnInactiveAlternateSetting(int endpointType)
    {


        myIface = null;
        boolean usbe = false;
        UsbEndpoint inEndpoint = null;
        inPipe = null;


        if ( usbDevice.isConfigured() )
        {
            inEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_IN);
            if ( (endpointType == UsbConst.ENDPOINT_TYPE_BULK)||
                 (endpointType == UsbConst.ENDPOINT_TYPE_INTERRUPT) )
            {


                assertFalse("could not find endpoint", null == inEndpoint);
                                                                                              // @P2D3
            } else
            {


                assertFalse("could not find endpoint", null == inEndpoint);
                                                                                              // @P2D3
            }

            inPipe = inEndpoint.getUsbPipe();
            assertFalse("Pipe should not be active!!!",
                        inPipe.isActive());


            if ( !myIface.isActive() )
            {
                try
                {
                    inPipe.open();
                    fail("should caused UsbNotActiveException!");
                } catch ( UsbNotActiveException ue )
                {
                    usbe = true;
                } catch ( UsbException ue )
                {
                    fail("UsbNotActiveException was expected.  Unexpected UsbException: " + ue);
                } catch ( UsbDisconnectedException uDE )                                      // @P1C
                {                                                                             // @P1A
                    fail ("A connected device should't throw the UsbDisconnectedException!"); // @P1A
                } catch ( Exception e )                                                       // @P1C
                {
                    fail("UsbNotActiveException was expected.  Unexpected Exception: " + e);
                }
            } else
            {
                fail("Interface should have been inactive at this point.");
            }
            if ( !usbe )
                assertTrue("Test Open Pipe On Inactive Interface did not throw UsbNotActiveException!", false);

        }


        else
        {
            fail ("UsbDevice is not configured!");
        }

        assertFalse("Leaving routine but interface was not released!", myIface.isClaimed());
    }


    private void doTestOpenPipeOnUnclaimedInterface(int endpointType)
    {
        myIface = null;
        boolean usbe = false;
        UsbEndpoint inEndpoint = null;
        inPipe = null;


        if ( usbDevice.isConfigured() )
        {
            inEndpoint = getEndpointOfType(usbDevice, endpointType, UsbConst.REQUESTTYPE_DIRECTION_IN);


            assertFalse("could not find endpoint", null == inEndpoint);

                                                                                              // @P2D4
            try
            {
                inEndpoint.getUsbInterface().release();
            } catch ( UsbClaimException ue )
            {
                fail("The interface should have been durring getEndpointOfType method call!"); 
            } catch ( UsbException ue )
            {
                fail("Unexpected exception: " + ue);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A

            if ( myIface.isActive() )
            {
                inPipe = inEndpoint.getUsbPipe();
                assertTrue("pipe is not active!!!",
                           inPipe.isActive());
                try
                {


                    inPipe.open();
                    fail("should caused UsbNotClaimedException!");

                } catch ( UsbNotClaimedException ue )
                {
                    usbe = true;
                } catch ( UsbException ue )
                {
                    fail("UsbNotClaimedException was expected.  Unexpected UsbException: " + ue);
                } catch ( UsbDisconnectedException uDE )                                      // @P1C
                {                                                                             // @P1A
                    fail ("A connected device should't throw the UsbDisconnectedException!"); // @P1A
                } catch ( Exception e )                                                       // @P1A
                {
                    fail("UsbNotClaimedException was expected.  Unexpected Exception: " + e);

                }
                if ( !usbe )
                    assertTrue("Test Open Pipe On Unclaimed Interface did not throw UsbNotClaimedException!", false);
            }

            else
            {
                fail("Interface should have been active.");
            }


        }



    }


//    /**
//     * looking for an inactive interface
//     * return null if not found
//     */
//    private UsbInterface getInactiveInterface()
//    {
//        UsbConfiguration config = null;
//        UsbHub virtualRootUsbHub = getVirtualRootUsbHub();
//        List usbDevices = null;
//        byte firstInterface = 0;
//        // get all connected devices
//        usbDevices = getAllUsbDevices(virtualRootUsbHub);
//        System.out.println("Found " + usbDevices.size() + " devices total.");
//
//        // find an inactive interface
//        for ( int i = 0; i > usbDevices.size(); i++ )
//        {
//            UsbDevice device = (UsbDevice)usbDevices.get(i);
//            List configurations = device.getUsbConfigurations();
//            for ( int j = 0; i <configurations.size(); j++ )
//            {
//                config = (UsbConfiguration)configurations.get(j);
//                if ( !config.isActive() )
//                    return config.getUsbInterface(firstInterface);
//            }
//
//        }
//        return null;
//    }



    /**
     *  find endpoint that match the requirement
     * @param totalEndpoints The List of End Points
     * @param inDirection The direction ( 81 = in, 00 = out)
     * @param inType The endpoint type ( 3 - interrupt, 2 - bulk)
     * @param inSize The endpoint size (8 or 64 bytes for this programmable board)
     */
    private UsbEndpoint findEndpoint(List totalEndpoints,
                                     int inDirection,
                                     int inType,
                                     int inSize)
    {
        boolean found = false;
        for ( int j = 0; j < totalEndpoints.size(); j++ )
        {
            UsbEndpoint ep = (UsbEndpoint) totalEndpoints.get(j);
            int direction = ep.getDirection();
            int type = ep.getType();
            int maxPackageSize =
                ep.getUsbEndpointDescriptor().wMaxPacketSize();
            int endpointAddress =
                ep.getUsbEndpointDescriptor().bEndpointAddress();
            if ( (byte) (inDirection & 0x80) == (byte)direction && inType == type
                 && inSize == maxPackageSize )
            {
                return ep;
            }

        }

        return null;
    }



/**
 * Returns endpoint of specific type and direction
 */
    private UsbEndpoint getEndpointOfType(UsbDevice usbDevice, int endpointType, int direction)
    {
        UsbEndpoint endpoint = null;

        if ( (endpointType == UsbConst.ENDPOINT_TYPE_BULK)||
             (endpointType == UsbConst.ENDPOINT_TYPE_INTERRUPT) )
        {

            UsbConfiguration usbConfiguration = usbDevice.getUsbConfiguration((byte) 1);
            IOMethods.selectAlternateSetting(usbDevice, (byte) 1, (byte) 0, (byte) 1);

            myIface = usbConfiguration.getUsbInterface((byte) 0);


            List totalEndpoints = myIface.getUsbEndpoints();
            if ( totalEndpoints.isEmpty() )
            {
                fail("no endpoint available");
            }
            endpoint = findEndpoint(totalEndpoints,
                                    direction,
                                    endpointType,
                                    8);
            assertFalse("could not find endpoint", null == endpoint);

        } else
        {

            UsbConfiguration usbConfiguration =
                usbDevice.getUsbConfiguration((byte) 1);



            IOMethods.selectAlternateSetting(usbDevice, (byte) 1, (byte) 0, (byte) 0);

            myIface = usbConfiguration.getUsbInterface((byte) 0);


            List totalEndpoints = myIface.getUsbEndpoints();
            if ( totalEndpoints.isEmpty() )
            {
                fail("no endpoint available");
            }
            endpoint = findEndpoint(totalEndpoints,
                                    direction,
                                    endpointType,
                                    16);
            assertFalse("could not find endpoint", null == endpoint);

        }
        return endpoint;
    }

    private void dumpHex(byte[] buffer)
    {
        for ( int i = 0; i < buffer.length; i++ )
            System.out.print(" 0x" + UsbUtil.toHexString(buffer[i]));
        System.out.println("");
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //
    private FindProgrammableDevice programmableDevice =
        FindProgrammableDevice.getInstance();
    private UsbDevice usbDevice = null;

    /*
     * make myIface global so that the interface can be 
     * released in case of errors in a previous test
     */
    private UsbInterface myIface = null;
    //private UsbConfiguration usbConfiguration = null;


    /*
     * Make inPipe and outPipe global so that pipes can 
     * be closed in case of errors in a previous test
     */
    private UsbPipe inPipe = null;
    private UsbPipe outPipe = null; 

    private int testType = 99;
    private int isochronousTest = UsbConst.ENDPOINT_TYPE_ISOCHRONOUS;
    private int bulkTest = UsbConst.ENDPOINT_TYPE_BULK;
    private int intTest = UsbConst.ENDPOINT_TYPE_INTERRUPT;

    /**
     * printDebug method will print the specified string if "debug" is true.
     * Useful function for debugging
     * @param infoString
     */
    private static void printDebug(String infoString)
    {
        if ( bPrintDebug )
        {
            System.out.println(infoString);
        }
    }

    private static boolean bPrintDebug = false;
    //private static boolean bPrintDebug = true;
}
