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

import java.util.List;

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.StandardRequest;

import org.junit.runner.RunWith;

import de.ailis.usb4java.TCKRunner;

import junit.framework.TestCase;

/**
 * Hot Plug Test
 * <p>
 * This test verifies that devices can be attached and detached at runtime 
 * (hot plugging) and checks that proper UsbServices events are generated.
 * @author Charles Jaeger
 */


@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class HotPlugTest extends TestCase
{

    /*
     * Assumed event flow on a renumeration of the Cypress board is...
     * DeviceListener.DataEvent - response from syncSubmit of IRP
     * DeviceListener.DetachedEvent - device is offlined
     * ServiceListener.DetachedEvent - service is offlined
     * ServiceListener.AttachedEvent - service is online
     * 
     * Assumed device under test is a Cypress board with the BulkInt
     * image running.
     */

    public void setUp()
    {
        try
        {
            usbServices = UsbHostManager.getUsbServices();
        } catch ( java.lang.SecurityException se )
        {
            fail("SecurityException was not expected: " + se);
        } catch ( UsbException ue )
        {
            fail("UsbException was not expected: " + ue);
        }

        // traverse the topology tree to locate the test device
        usbDevice =
            FindProgrammableDevice.getInstance().getProgrammableDevice();

        assertTrue("USB Device was not configured", usbDevice.isConfigured());

        // add a listener to receive events when the host has changes
        // (e.g. device is unplugged or is plugged in)
        usbServices.addUsbServicesListener(servicesListener);

        // add a listener to detect when the device is detached.
        usbDevice.addUsbDeviceListener(deviceListener);

        // init for each test ->
        numSubmits = 0;

        numDeviceDataEvents = 0;
        numDeviceDetachEvents = 0;
        numDeviceErrorEvents = 0;
        lastUsbDeviceDataEvent = null;
        lastUsbDeviceErrorEvent = null;
        lastUsbDeviceDetachedEvent = null;

        numServicesAttachEvents = 0;
        numServicesDetachEvents = 0;
        lastServicesAttachEvent = null;
        lastServicesDetachEvent = null;
        // <-

    }

    public void tearDown()
    {

        if ( debug )
        {

            /* Print out some debug info
             */
            System.out.println("numSubmits = " + numSubmits);
            System.out.println("numDataEvents = " + numDeviceDataEvents);
            System.out.println("numExceptionEvents = " + numDeviceErrorEvents);
            System.out.println("numDetachEvents = " + numDeviceDetachEvents);
            System.out.println(
                              "numServicesAttachEvents = " + numServicesAttachEvents);
            System.out.println(
                              "numServicesDetachEvents = " + numServicesDetachEvents);

        }

        usbDevice.removeUsbDeviceListener(deviceListener);
        usbServices.removeUsbServicesListener(servicesListener);

    }

    /**
     * Tests that the setUp() passes all assertions. 
     */
    public void testSetUp()
    {

        // was setup successful in init of members?
        assertNotNull("UsbDevice required for test not found", usbDevice);
        assertNotNull(
                     "UsbHostManager returned null instead of usbServices object",
                     usbServices);

        // test the numXXX members
        assertEquals("Devices detached should be 0", numDeviceDetachEvents, 0);

        assertEquals(
                    "Device commands issued should be 0",
                    numDeviceDataEvents,
                    0);

        assertEquals("Device error count should be 0", numDeviceErrorEvents, 0);

        assertEquals(
                    "Services detached should be 0",
                    numServicesDetachEvents,
                    0);

        assertEquals(
                    "Services attached should be 0",
                    numServicesAttachEvents,
                    0);

        assertEquals("Board commands should be 0", numSubmits, 0);

        // test the lastXXX members
        assertNull(lastUsbDeviceDataEvent);
        assertNull(lastUsbDeviceErrorEvent);
        assertNull(lastUsbDeviceDetachedEvent);

        assertNull(lastServicesAttachEvent);
        assertNull(lastServicesDetachEvent);

        //
        assertNotNull("usbDevice should already exist.", usbDevice);
    }

    public void testUsbServicesEvent()
    {
        UsbServicesEvent event = new UsbServicesEvent(usbServices, usbDevice);
        assertNotNull("UsbServicesEvent constructor failed.", event);

        //test methods of the UsbServicesEvent
        assertNotNull(
                     "UsbServicesEvent.getUsbServices() returned null",
                     event.getUsbServices());
        assertNotNull(
                     "UsbServicesEvent.getUsbDevice() returned null",
                     event.getUsbDevice());

    }

    /**
     * Test the method that will send the reset command to the Cypress board.
     */
    public void testSimulateHotPlug()
    {

        // send reset command to the Cypress board
        simulateHotPlug();

        // verify that all expected events were captured
        assertNotNull(
                     "Did not receive a DeviceListener DataEvent after submitting IRP.",
                     lastUsbDeviceDataEvent);

        assertNotNull(
                     "Did not receive a DeviceListener DetachedEvent (indicating reset not issued).",
                     lastUsbDeviceDetachedEvent);

        assertNotNull(
                     "Did not receive a ServicesListener acknowledging detach.",
                     lastServicesDetachEvent);

        assertNotNull(
                     "Did not receive a ServicesListener acknowledging attach.",
                     lastServicesAttachEvent);

        // Were the number of returned events correct?
        assertEquals(
                    "The device should detach once during the run",
                    numDeviceDetachEvents,
                    1);

        assertEquals("Only 1 command is sent", numDeviceDataEvents, 1);

        assertEquals("No errors should occur", numDeviceErrorEvents, 0);

        assertEquals(
                    "The service should detach once during the run",
                    numServicesDetachEvents,
                    1);

        assertEquals(
                    "The service attached once during the run",
                    numServicesAttachEvents,
                    1);
    }

    /**
     * Verify that proper exceptions are thrown (w/o failure)
     * for all other IO calls to a disconnected device
     */
    public void testExceptionsOnDisconnectedDevice()
    {

        //local variable
        boolean isOK = false;

        // make sure the board is online
        assertNotNull("The Cypress board is not online", usbDevice);

        // flush any events from the listeners.
        // ... automatically done by the setUp() process

        // reset the board
        try
        {
            submitRenumerateIrp();
        } catch ( Exception e )
        {
            e.printStackTrace();
            fail("Resetting the board should not issue any exceptions.");
        }

        // verify that the correct exceptions are returned for this offline device.

        if ( null == lastServicesDetachEvent )
        {

            if ( debug )
            {
                System.out.println(
                                  "-> public void testExceptionsOnDisconnectedDevice()");
                System.out.println("   -> submitRenumerateIrp()");
            }

            // UsbException
            try
            {
                submitRenumerateIrp();
                fail("UsbException expected");
            } catch ( UsbException e )
            {
                isOK = true;
                if ( debug )
                    System.out.println("UsbException thrown" + e);
            } catch ( Exception e )
            {
                e.printStackTrace();
                fail("An Exception other than UsbException was detected.");
            }

            isOK = false;

            // IllegalArgumentException - If the UsbControlIrp is not valid.
            try
            {
                submitBadIrp();
                fail("IllegalArgumentException was expected.");
            } catch ( IllegalArgumentException e )
            {
                // System.out.println("good");
                isOK = true;
                if ( debug )
                    System.out.println("IllegalArgumentException thrown");
            } catch ( Exception e )
            {
                e.printStackTrace();
                fail("Exception other than IllegalArgumentException was detected");
            }

        } else
        {
            fail("Can't proceed because the service got up and running quickly.");
        }

        if ( null != lastServicesDetachEvent )
        {
            fail("The service may have attached during test, so results are inconclusive.");
        }

        // make sure board is up before leaving.
        isOK = false;
        try
        {
            waitForServiceAttachedEvent(20000);
            isOK = true;
        } catch ( InterruptedException e )
        {
            e.printStackTrace();
            fail("Timed out before all events were seen.");
        } finally
        {
            if ( !isOK )
                fail("UsbDevice was not detected as going online");
        }
    }

    /**
     * Verify proper events are generated after disconnecting.
     */
    public void testDisconnectEvents()
    {
        // make sure the board is online
        assertNotNull("The Cypress board is not online", usbDevice);

        // reset the board
        try
        {
            submitRenumerateIrp();
        } catch ( UsbException e )
        {
            e.printStackTrace();
            fail("Resetting the board should not issue any exceptions.");
        }

        // make sure all the disconnect events from the listeners fire
        try
        {
            // the total time to wait for the wanted disconnect events to occur
            waitForServiceDetachedEvent(20000);

            // Disconnect events follow
            // in the expected sequential order...
            assertNotNull(
                         "The device did not detach within time",
                         lastUsbDeviceDetachedEvent);
            assertNotNull(
                         "The service did not detach within time",
                         lastServicesDetachEvent);
            assertEquals(
                        "1 Device detach events expected",
                        numDeviceDetachEvents,
                        1);
            assertEquals(
                        "1 Service detach events expected",
                        numServicesDetachEvents,
                        1);
            assertEquals("0 Device errors expected", numDeviceErrorEvents, 0);

            // make sure board is up before leaving.
            waitForServiceAttachedEvent(5000);

        } catch ( InterruptedException e )
        {
            e.printStackTrace();
            fail("Timed out before all events were seen.");
        }
    }

    /**
     * Verify proper events are generated after hot plugging.
     */
    public void testPlugInEvents()
    {
        // make sure the board is online
        assertNotNull("usbDevice should already be online.", usbDevice);

        try
        {
            // the setUp() flushes any events from the listeners

            // reset the board
            submitRenumerateIrp();

            // wait til the board comes back up
            waitForServiceAttachedEvent(20000);

            // verify that all the events from the listeners fire

            assertNotNull(
                         "Did not receive a DeviceListener DataEvent after submitting IRP.",
                         lastUsbDeviceDataEvent);

            assertNotNull(
                         "Did not receive a DeviceListener DetachedEvent (indicating reset not issued).",
                         lastUsbDeviceDetachedEvent);

            assertNotNull(
                         "Did not receive a ServicesListener acknowledging detach.",
                         lastServicesDetachEvent);

            assertNotNull(
                         "Did not receive a ServicesListener acknowledging attach.",
                         lastServicesAttachEvent);

            // Were the number of returned events correct?
            assertEquals(
                        "The device should detach once during the run",
                        numDeviceDetachEvents,
                        1);

            assertEquals("Only 1 command is sent", numDeviceDataEvents, 1);

            assertEquals("No errors should occur", numDeviceErrorEvents, 0);

            assertEquals(
                        "The service should detach once during the run",
                        numServicesDetachEvents,
                        1);

            assertEquals(
                        "The service attached once during the run",
                        numServicesAttachEvents,
                        1);
        } catch ( InterruptedException e )
        {
            e.printStackTrace();
            fail("Sleep was interrupted");
        } catch ( UsbException uE )
        {
            /* The exception should indicate the reason for the failure.
             * For this example, we'll just stop trying.
             */
            fail("DCP submission failed.  " + uE.toString());
	}
    }

    /**
     * Run a disconnect, reconnect cycle and then verify that 10 transfers will
     * succeed without data error.
     */
    public void testNoErrorsOnDataTransfers()
    {

        // the setUp() flushes any events from the listeners

        // make sure the board is online
        assertNotNull("usbDevice should already be online.", usbDevice);
        assertTrue("usbDevice is not configured.", usbDevice.isConfigured());

        //set up the board's: endpoints, pipes, etc ..
        Board theBoard = new Board(usbDevice);

        // test: while online, data can be sent without error...
        try
        {
            theBoard.sendData();
        } catch ( Exception e )
        {
            e.printStackTrace();
            fail("Exception not expected.");
        }
        // tested: while online, data can be sent without error...

        // While transferring data through input and output endpoints
        // (loop bulk data), unplug the device.
        // (Vendor request to Cypress Board to renumerate will simulate unplug.)
        try
        {
            theBoard.testAsyncInFailure();
        } catch ( InterruptedException ie )
        {
            fail("Timed out while waiting for the device detach event.");
        } catch ( Exception e )
        {
            e.printStackTrace();
            fail("Resetting the board should not issue any exceptions.");
        }
        // tested: when offline, abort all submissions

        try
        {
            waitForServiceAttachedEvent(20000);
        } catch ( InterruptedException ie )
        {
            fail("Timed out while waiting for the device detach event.");
        }

        // test: when online again, send data without error
        // wait for the device to come back up
        try
        {
            waitForServiceAttachedEvent(10000);
            setUp();
            theBoard = new Board(usbDevice);
            theBoard.sendData();
            theBoard.tearDown();
        } catch ( InterruptedException ie )
        {
            fail("Timed out waiting for service attach event.");
        } catch ( Exception e )
        {
            e.printStackTrace();
            fail("Exception not expected.");
        }
        // tested: when online again, send data without error
    }

    /**
     * Tell the Cypress board to initiate its reset and binary upload method.
     * in order to simulate human involved device detach and attach action.
     * Returns successfully once device service is attached; otherwise
     * a fails method is asserted.
     */
    private void simulateHotPlug()
    {
        /* This will block until the submission is complete.
         * Note that submissions (except interrupt and bulk in-direction)
         * will not block indefinitely, they will complete or fail within
         * a finite amount of time.  See MouseDriver.HidMouseRunnable for more details.
         */
        try
        {
            submitRenumerateIrp();

            waitForDeviceDataOrErrorEvent(2000);

            waitForDeviceDetachedEvent(5000);

            waitForServiceDetachedEvent(2000);

            waitForServiceAttachedEvent(10000);

        } catch ( InterruptedException e )
        {
            e.printStackTrace();
            fail("Sleep was interrupted");
        } catch ( UsbException uE )
        {
            /* The exception should indicate the reason for the failure.
             * For this example, we'll just stop trying.
             */
            fail("DCP submission failed.  " + uE.toString());
        }
    }

    /**
     * Generates the Renumerate command for the cypress board as a UsbControlIrp
     * @return the UsbControlIrp ready for use
     */
    private UsbControlIrp getRenumerateIrp()
    {

        byte sentbmRequestType =
            UsbConst.REQUESTTYPE_DIRECTION_OUT
            | UsbConst.REQUESTTYPE_TYPE_VENDOR
            | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;

        short sentwIndex = 0; //first isochronous IN endpoint

        // An IRP
        UsbControlIrp usbControlIrp =
            usbDevice.createUsbControlIrp(
                                     sentbmRequestType,
                                     VENDOR_REQUEST_RENUMERATE,
                                     VENDOR_REQUEST_DATA_OUT,
                                     sentwIndex);

        //set variable parts of Irp
        byte[] sentData = new byte[0];
        usbControlIrp.setData(sentData);
        usbControlIrp.setAcceptShortPacket(true);

        return usbControlIrp;
    }

    /**
     * Generates an IRP that will cause the IllegalArgumentException 
     * on syncSubmit to the Cypress board 
     * @return the UsbControlIrp ready for use
    private UsbControlIrp getBadIrp() {
        UsbControlIrp badIrp = getRenumerateIrp();
        //set Irp data to cause IllegalArgumentException on syncSubmit

        return badIrp;
    }
     */

    /**
     * Sends a reset and binary upload call to the Cypress board.
     * @throws UsbException
     */
    private void submitRenumerateIrp() throws UsbException {
        try
        {

            UsbControlIrp usbControlIrp = getRenumerateIrp();
            assertNotNull("usbControlIrp should not be null", usbControlIrp);

            usbDevice.syncSubmit(usbControlIrp);
            numSubmits++;
        } catch ( IllegalArgumentException e )
        {
            e.printStackTrace();
            fail("IllegalArgumentException was not expected.");
        }
    }

    /**
     * Sends an invalid UsbControlIrp to the Cypress board.
     * @throws IllegalArgumentException
     */
    private void submitBadIrp() throws IllegalArgumentException {

        // An IRP
        UsbControlIrp usbControlIrp = getRenumerateIrp();
        assertNotNull("usbControlIrp should not be null", usbControlIrp);

        //set Irp data to cause IllegalArgumentException on syncSubmit
        usbControlIrp.setData(null);

        try
        {
            usbDevice.syncSubmit(usbControlIrp);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        } catch ( UsbException e )                                                            // @P1A
        {
            e.printStackTrace();
            fail("UsbException not expected.");
        }
        numSubmits++;
    }

    /**
     * Wait for deviceListener deviceData or deviceError event before continuing.
     * @param timeout minimum number of milliseconds before timeout is issued (suggested minimum: 2000)
     * @throws InterruptedException raised if timeout occurs
     */
    private void waitForDeviceDataOrErrorEvent(int timeout)
    throws InterruptedException {
        // up to 2 sec min timeout on submission
        for ( int i = 0; i < (timeout / 20); i++ )
        {

            if ( (lastUsbDeviceDataEvent != null)
                 || (lastUsbDeviceErrorEvent != null) )
            {
                if ( debug )
                    System.out.println(
                                      "waitForDeviceDataOrErrorEvent took less than "
                                      + ((i + 1) * 20)
                                      + " milliseconds");
                break;
            }
            Thread.sleep(20); //wait 20 ms before checkin for event
        }
    }

    /**
     * Wait for deviceListener deviceDetached event before continuing
     * @param timeout minimum number of milliseconds before timeout is issued (suggested minimum: 5000)
     * @throws InterruptedException raised if timeout occurs
     */
    private void waitForDeviceDetachedEvent(int timeout)
    throws InterruptedException {
        for ( int i = 0; i < (timeout / 20); i++ )
        {

            if ( lastUsbDeviceDetachedEvent != null )
            {
                if ( debug )
                    System.out.println(
                                      "waitForDeviceDetachedEvent took less than "
                                      + ((i + 1) * 20)
                                      + " milliseconds");
                break;
            }
            Thread.sleep(20); //wait 20 ms before checkin for event
        }
        if ( lastUsbDeviceDetachedEvent == null )
        {
            throw new InterruptedException(
                                          "DeviceDetachedEvent not found within " + timeout + " ms");
        }

    }

    /**
     * Wait for servicesListener deviceDetached event before continuing
     * @param timeout minimum number of milliseconds before timeout is issued (suggested minimum: 2000)
     * @throws InterruptedException raised if timeout occurs
     */
    private void waitForServiceDetachedEvent(int timeout)
    throws InterruptedException {

        for ( int i = 0; i < (timeout / 20); i++ )
        {

            if ( lastServicesDetachEvent != null )
            {
                if ( debug )
                    System.out.println(
                                      "waitForServiceDetachedEvent took less than "
                                      + ((i + 1) * 20)
                                      + " milliseconds");
                break;
            }
            Thread.sleep(20); //wait 20 ms before checkin for event
        }
    }

    /**
     * Wait for servicesListener deviceAttached event before continuing
     * @param timeout minimum number of milliseconds before timeout is issued (suggested minimum: 5000)
     * @throws InterruptedException raised if timeout occurs
     */
    private void waitForServiceAttachedEvent(int timeout)
    throws InterruptedException {
        for ( int i = 0; i < (timeout / 20); i++ )
        {

            if ( (null != lastServicesAttachEvent) && (usbDevice.isConfigured() ) )
            {
                if ( debug )
                    System.out.println(
                                      "waitForServiceAttachedEvent took less than "
                                      + ((i + 1) * 20)
                                      + " milliseconds");
                break;
            }
            Thread.sleep(20); //wait 20 ms before checkin for event
        }
    }

    private class Board
    {

        // attributes
        private UsbInterface iface;
        private UsbPipe inPipe;
        private UsbPipe outPipe;
        private boolean hasRenumerated = false;
        private boolean hasSubmittedAsyncIn = false;

        // Concrete instance of a UsbPipeListener interface
        private UsbPipeListener inPipeListener = new UsbPipeListener()
        {
            public void dataEventOccurred(UsbPipeDataEvent updE)
            {
            }
            public void errorEventOccurred(UsbPipeErrorEvent upeE)
            {
                if ( hasRenumerated && hasSubmittedAsyncIn ) // ok
                {
                } else //err
                {
                    upeE.getUsbException().printStackTrace();
                    fail ("No in pipe errors are expected at this point.");
                }
            }
        };

        private UsbPipeListener outPipeListener = new UsbPipeListener()
        {
            public void dataEventOccurred(UsbPipeDataEvent updE)
            {
            }
            public void errorEventOccurred(UsbPipeErrorEvent upeE)
            {
                fail("No out pipe errors should occur during this test.");
            }
        };

        // default no arg ctor not allowed
        private Board()
        {
        }

        //ctor
        public Board(UsbDevice usbDevice)
        {
            config(usbDevice);
        }

        /**
         * set up endpoints, pipes, etc ..
         */
        private void config(UsbDevice usbDevice)
        {

            try
            {
                // set the endpoints
                List endpoints = null;

                /**
                 *  select alternate setting for the programmable board (AS0 or AS1)
                 * @param usbDevice The UsbDevice obj
                 */
                if ( usbDevice.isConfigured() )
                {
                    iface = usbDevice.getUsbConfiguration((byte) 1).getUsbInterface((byte) 0);

                    iface.claim();

                                                                                              // @P2D4
                    iface = iface.getActiveSetting();

                    if ( debug )
                        System.out.println("Select Alternate Setting 1");

                    endpoints = iface.getUsbEndpoints();

                    UsbEndpoint inEndpoint8 =
                        findEndpoint(
                                endpoints,
                                UsbConst.REQUESTTYPE_DIRECTION_IN,
                                UsbConst.ENDPOINT_TYPE_BULK,
                                8);

                    UsbEndpoint outEndpoint8 =
                        findEndpoint(
                                endpoints,
                                UsbConst.REQUESTTYPE_DIRECTION_OUT,
                                UsbConst.ENDPOINT_TYPE_BULK,
                                8);

                    inPipe = inEndpoint8.getUsbPipe();
                    outPipe = outEndpoint8.getUsbPipe();
                    inPipe.addUsbPipeListener(inPipeListener);
                    outPipe.addUsbPipeListener(outPipeListener);
                    inPipe.open();
                    outPipe.open();
                } else
                {
                    fail("device is not configured!");
                }

            } catch ( UsbClaimException uce )
            {
                fail("UsbClaimException: " + uce);
            } catch ( UsbDisconnectedException uDE )                                          // @P1A
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( UsbException ue )
            {
                fail("UsbException: " + ue);
            } catch ( UsbNotActiveException unae )
            {
                unae.printStackTrace();
                fail("UsbNotActiveException: " + unae);
	    }
        }

        public void testAsyncInFailure() throws Exception
        {
            int dataSize = 8;
            byte[] inData = new byte[dataSize];

            UsbIrp irp = inPipe.createUsbIrp();
            irp.setData(inData);
            inPipe.asyncSubmit(irp);

            hasSubmittedAsyncIn = true;

            hasRenumerated = true;
            submitRenumerateIrp();
            waitForServiceAttachedEvent(20000);
        }


        public void tearDown()
        {
            try
            {
                inPipe.abortAllSubmissions();
                outPipe.abortAllSubmissions();
                inPipe.close();
                outPipe.close();
                inPipe.removeUsbPipeListener(inPipeListener);
                outPipe.removeUsbPipeListener(outPipeListener);

                if ( null != iface )
                {
                    if ( debug )
                        System.out.println("releasing interface!!!!");
                    iface.release();
                    if ( debug )
                        System.out.println("released interface!!!!");
                }
            } catch ( UsbClaimException uce )
            {
                fail("UsbClaimException: " + uce);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            } catch ( UsbException ue )
            {
                fail("UsbException: " + ue);
            } catch ( UsbNotActiveException unae )
            {
                fail("UsbNotActiveException: " + unae);
	    }
        }

        /**
         * Performs bulk data transfer for 10 datum.  
         * @exception 
         */
        public void sendData() throws Exception {
            for ( int tries = 0; tries < 10; tries++ )
            {
                try
                {
                    sendDatum();
                } catch ( Exception e )
                {
                    fail("The following exception was caught");
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        /**
         * Will throw an exception if a buffer it sends is different than the one it receives. 
         */
        private void sendDatum() throws Exception {
            byte txType = TRANSFORM_TYPE_PASSTHROUGH;
            int dataSize = 8;
            byte[] inData = new byte[dataSize];

            TransmitBuffer txBuffer = new TransmitBuffer(txType, dataSize);
            try                                                                               // @P1A
            {                                                                                 // @P1A
                outPipe.syncSubmit(txBuffer.getOutBuffer());                                  // @P1C
                inPipe.syncSubmit(inData);                                                    // @P1C
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A

            if ( txBuffer.compareBuffers(inData) )
            {
                // it's all good, display the data
                if ( debug )
                {
                    System.out.print("Received data was: ");
                    for ( int i = 0; i < dataSize; i++ )
                        System.out.print(inData[i] + ",");
                    System.out.println();
                }
            } else
            {
                throw new Exception("received inData from pipe was != outData.");
            }
        }

        /** 
         *  find endpoint that match the requirement
         * @param totalEndpoints The List of End Points
         * @param inDirection The direction ( 81 = in, 00 = out)
         * @param inType The endpoint type ( 3 - interrupt, 2 - bulk)
         * @param inSize The endpoint size (8 or 64 bytes for this programmable board)
         */
        private UsbEndpoint findEndpoint(
                                        List totalEndpoints,
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
                if ( (byte) (inDirection & 0x80) == (byte) direction
                     && inType == type
                     && inSize == maxPackageSize )
                {
                    return ep;
                }

            }
            if ( debug )
                System.out.println("End point NOT FOUND!!!");
            return null;
        }
    }

    private final UsbServicesListener servicesListener =
        new UsbServicesListener()
    {

        public void usbDeviceAttached(UsbServicesEvent use)
        {
            if ( debug )
                System.out.println("UsbServicesListener.usbDeviceAttached()");
            numServicesAttachEvents++;
            assertNotNull(use);
            lastServicesAttachEvent = use;
            assertNotNull(
                         "Device was attached: ",
                         lastServicesAttachEvent.toString());
        }

        public void usbDeviceDetached(UsbServicesEvent use)
        {
            if ( debug )
                System.out.println("UsbServicesListener.usbDeviceDetached()");
            numServicesDetachEvents++;
            assertNotNull(use);
            lastServicesDetachEvent = use;
            assertNotNull(
                         "Device was detached: ",
                         lastServicesDetachEvent.toString());

            //testing attributes of the UsbServicesEvent
            assertNotNull(
                         "UsbServicesEvent.getUsbServices() returned null",
                         use.getUsbServices());
            assertNotNull(
                         "UsbServicesEvent.getUsbDevice() returned null",
                         use.getUsbDevice());
        }
    };

    private final UsbDeviceListener deviceListener = new UsbDeviceListener()
    {

        public void dataEventOccurred(UsbDeviceDataEvent uddE)
        {
            numDeviceDataEvents++;
            // fail("Test that we get a data event");
            // assertEquals(0,numDataEvents);
            assertNotNull(uddE);
            lastUsbDeviceDataEvent = uddE;
            if ( debug )
                System.out.println(
                                  "UsbDeviceListener.dataEventOccurred().  uddE = "
                                  + uddE
                                  + "numDataEvents = "
                                  + numDeviceDataEvents);
        }

        public void errorEventOccurred(UsbDeviceErrorEvent udeE)
        {
            numDeviceErrorEvents++;
            assertNotNull(udeE);
            lastUsbDeviceErrorEvent = udeE;
            if ( debug )
                System.out.println(
                                  "UsbDeviceListener.errorEventOccurred().  uddE = "
                                  + udeE
                                  + "numExceptionEvents = "
                                  + numDeviceErrorEvents);
            assertNotNull(
                         "Should not have null UsbException on UsbDeviceErrorEvent.",
                         udeE.getUsbException());
        }

        public void usbDeviceDetached(UsbDeviceEvent udE)
        {
            numDeviceDetachEvents++;
            assertNotNull(udE);
            lastUsbDeviceDetachedEvent = udE;
            if ( debug )
                System.out.println(
                                  "UsbDeviceListener.usbDeviceDetached(). object = "
                                  + udE
                                  + "numDeviceDetachEvents = "
                                  + numDeviceDetachEvents);
            assertNotNull(
                         "Should not have null UsbDevice on usbDeviceEvent.",
                         udE.getUsbDevice());
        }
    };

    // for debugging
    private boolean debug = false;

    // instances of USB classes
    private UsbDevice usbDevice;
    private UsbHostManager usbHostManager;
    private UsbServices usbServices;

    private UsbDeviceDataEvent lastUsbDeviceDataEvent;
    private UsbDeviceErrorEvent lastUsbDeviceErrorEvent;
    private UsbDeviceEvent lastUsbDeviceDetachedEvent;
    private UsbServicesEvent lastServicesDetachEvent, lastServicesAttachEvent;

    private static int numSubmits = 0;
    private static int numDeviceDataEvents = 0;
    private static int numDeviceErrorEvents = 0;
    private static int numDeviceDetachEvents = 0;
    private static int numServicesAttachEvents = 0;
    private static int numServicesDetachEvents = 0;

    private static final short VENDOR_REQUEST_DATA_OUT = 0x00;
    private static final byte VENDOR_REQUEST_RENUMERATE = (byte) 0xA8;
    private static final byte TRANSFORM_TYPE_PASSTHROUGH = (byte) 0x01;

    private static final byte[] manufacturerString =
    {(byte) 26, //length of descriptor
        (byte) UsbConst.DESCRIPTOR_TYPE_STRING,
        (byte) 'M',
        (byte) 0x00,
        (byte) 'a',
        (byte) 0x00,
        (byte) 'n',
        (byte) 0x00,
        (byte) 'u',
        (byte) 0x00,
        (byte) 'f',
        (byte) 0x00,
        (byte) 'a',
        (byte) 0x00,
        (byte) 'c',
        (byte) 0x00,
        (byte) 't',
        (byte) 0x00,
        (byte) 'u',
        (byte) 0x00,
        (byte) 'r',
        (byte) 0x00,
        (byte) 'e',
        (byte) 0x00,
        (byte) 'r',
        (byte) 0x00};
}
