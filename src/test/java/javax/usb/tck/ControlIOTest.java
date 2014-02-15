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

import junit.framework.*;

import javax.usb.*;
import javax.usb.util.*;
import javax.usb.event.*;

import org.junit.runner.RunWith;

import org.usb4java.test.TCKRunner;

/**
 * Control I/O Test - Synchronous and asynchronous Control Irp and Control
 * Irp List submissions
 * <p>
 * This test verifies pipes can be opened on a control endpoint other than
 * endpoint 0.  The programmable device used in this test suite supports
 * only one control pipe, the default control pipe.  The target device for
 * this test will be the device with at least one non default control pipe
 * as specified in the hardware configurations.  Because the target device
 * is not programmable, minimal communication verification will be performed
 * with the non default control pipe endpoint.
 * @author Joshua Lowry
 */

@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class ControlIOTest extends TestCase
{

    public ControlIOTest(String name)
    {
        super(name);
    }

    /**
     * The setUp method reinitializes the private variables between test cases,
     * so no run will affect the next run.
     * @throws Exception
     */
    protected void setUp() throws Exception {
        usbCtrlIODev = null;
        usbCtrlIOInterface = null;
        usbCtrlIOInterfaceDesc = null;
        usbCtrlIOEndpoint = null;
        usbCtrlIOEndpointDesc = null;
        usbCtrlIOPipe = null;
        numDataEvents = 0;
        numErrorEvents = 0;
        LastUsbPipeDataEvent = new ArrayList();
        LastUsbPipeErrorEvent = new ArrayList();
    }

    /**
     * The tearDown method closes the the UsbPipe if it is left open and
     * releases the UsbInterface if it is left claimed after a test, so a
     * failing test case doesn't prevent the next test case from running.
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        LastUsbPipeDataEvent.clear();
        LastUsbPipeErrorEvent.clear();
        try
        {
            if ( usbCtrlIOPipe.isOpen() )
            {
                usbCtrlIOPipe.abortAllSubmissions();
                usbCtrlIOPipe.close();
            }
        } catch ( UsbNotOpenException uNOE )
        {
            fail("ControlIO Pipe reports is opened but throws UsbNotOpenException when " +
                 "close or abortAllSubmissions method called: " + uNOE.getMessage());
        } catch ( UsbNotActiveException uNAE ) {
        	fail("ControlIO Pipe reports is opened but throws UsbNotActiveExceptio when " +
        	     "close or abortAllSubmissions method called: " + uNAE.getMessage());
        } catch ( UsbDisconnectedException uDE ) {
        	fail("ControlIO Pipe reports is opened but throws UsbDisconnectedException when " +
        		"close or abortAllSubmissions method called: " + uDE.getMessage());
        } catch ( UsbException uE ) {
        	fail("The close method was unable to close the ControlIO Pipe: " + uE.getMessage());
        }
        try
        {
            if ( usbCtrlIOInterface.isClaimed() )
                usbCtrlIOInterface.release();
        } catch ( UsbClaimException uCE )
        {
            fail("UsbInterface reports interface is claimed, release method throws" +
                 "UsbClaimException (device not claimed): " + uCE.getMessage());
        } catch ( UsbException uE )
        {
            fail("release method unable to release UsbInterface: " + uE.getMessage());
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
    }

    /**
     * Finds the first USB device with a UsbEndpoint with the
     * ENDPOINT_TYPE_CONTROL type other than Endpoint 0.  If it finds such a
     * device, this method ensures that the control UsbEndpoint's interface is
     * the active UsbInterface, claims that UsbInterface and opens its UsbPipe,
     * as well as asserting that alll the methods relating to the open succeed.
     * If it doesn't find such a UsbEndpoint, the method fails.
     * @throws Exception
     */
    protected void openControlIOPipe() throws Exception {
        short wMaxPacketSize;

        usbCtrlIODev = new FindControlIODevice().getControlIODevice();
        assertNotNull("There is no device with a control pipe other than the default connected",
                      usbCtrlIODev);

        usbCtrlIOEndpoint = FindControlIODevice.getControlIOEndpoint(usbCtrlIODev);
        usbCtrlIOEndpointDesc = usbCtrlIOEndpoint.getUsbEndpointDescriptor();
        assertNotNull("There is no endpoint other than Endpoint0 that is the control type",
                      usbCtrlIOEndpoint);
        assertTrue("This endpoint should not be endpoint0",
                   usbCtrlIOEndpointDesc.bEndpointAddress() != (byte) 0x00);
        assertEquals("The endpoint type is not Control",
                     UsbConst.ENDPOINT_TYPE_CONTROL, usbCtrlIOEndpoint.getType());

        usbCtrlIOInterface = usbCtrlIOEndpoint.getUsbInterface();
        usbCtrlIOInterfaceDesc = usbCtrlIOInterface.getUsbInterfaceDescriptor();

                                                                                              // @P2D23
            UsbConfiguration usbCtrlIOConfig;
            UsbConfigurationDescriptor usbCtrlIOConfigDesc;

            usbCtrlIOConfig = usbCtrlIOInterface.getUsbConfiguration();
            usbCtrlIOConfigDesc = usbCtrlIOConfig.getUsbConfigurationDescriptor();

        assertTrue("The configuration, interface & alternate setting for the Control Enpoint isn't active",
                   usbCtrlIOInterface.isActive());
                                                                                              // @P2D2

        try
        {
            usbCtrlIOInterface.claim();
        } catch ( UsbNotActiveException uNAE )
        {
            fail("The interface should be active when claim is called");
        } catch ( UsbClaimException uCE )
        {
            fail("The interface should not be claimed when claim is called");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        } catch ( UsbException uE )                                                           // @P1C
        {
            fail("The interface was unable to be claimed");
        }

        assertTrue("The current interface w/the control endpoint is not claimed after claim is called",
                   usbCtrlIOInterface.isClaimed());
        usbCtrlIOPipe = usbCtrlIOEndpoint.getUsbPipe();
        assertTrue("The Control I/O Pipe is not active!", usbCtrlIOPipe.isActive());
        usbCtrlIOPipe.addUsbPipeListener(usbCtrlIOPipeListener);
        assertFalse("The Control I/O Pipe should not be open before open is called",
                    usbCtrlIOPipe.isOpen());

        try
        {
            usbCtrlIOPipe.open();
        } catch ( UsbNotActiveException uNAE )
        {
            fail("The pipe's configuration and interface should be active when open is called");
        } catch ( UsbNotClaimedException uNCE )
        {
            fail("The pipe's interface should be claimed when open is called");
        } catch ( UsbException uE )
        {
            fail("The interface was unable to be opened");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        assertTrue("The Control I/O Pipe should be open after open is called",
                   usbCtrlIOPipe.isOpen());
        assertNotNull("No Endpoint Descriptor was returned while attempting to open the Control I/O Pipe",
                      usbCtrlIOEndpointDesc);
        assertNotNull("The Control Pipe was not opened",
                      usbCtrlIOPipe);

        wMaxPacketSize = usbCtrlIOEndpointDesc.wMaxPacketSize();
        switch ( wMaxPacketSize )
        {
        case 8:
        case 16:
        case 32:
        case 64:
            break;
        default:
            fail("The max packet size does not conform to a valid max packet size for control pipes");
            break;
        }

    }

    /**
     * Closes the UsbPipe and releases the UsbInterface for the UsbEndpoint
     * found in the openControlIOPipe() method.  It also tests everything
     * related to closing the UsbPipe and releasing the UsbInterface.
     * @throws Exception
     */
    protected void closeControlIOPipe() throws Exception {
        assertTrue("UsbPipe.isOpen() should be true before UsbPipe.close() is called",
                   usbCtrlIOPipe.isOpen());

        try
        {
            usbCtrlIOPipe.close();
        } catch ( UsbException uE )
        {
            fail("The Control I/O Pipe couldn't be closed");
        } catch ( UsbNotOpenException uNOE )
        {
            fail("The Control I/O Pipe threw a UsbNotOpenException on closing an open pipe");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        assertFalse("UsbPipe.isOpen() should be false after UsbPipe.close() is called",
                    usbCtrlIOPipe.isOpen());
        usbCtrlIOPipe.removeUsbPipeListener(usbCtrlIOPipeListener);
        assertTrue("The Control I/O Interface should be claimed before UsbInterface.release() is called",
                   usbCtrlIOInterface.isClaimed());

        try
        {
            usbCtrlIOInterface.release();
        } catch ( UsbClaimException uCE )
        {
            fail("A claimed interface shouldn't throw the UsbClaimException");
        } catch ( UsbException uE )
        {
            fail("The interface could not be released.");
        } catch ( UsbNotActiveException uNAE )
        {
            fail("An active interface setting shouldn't throw the UsbNotActiveException");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        assertFalse("The Control I/O Interface shouldn't be claimed after UsbInterface.release() is called",
                    usbCtrlIOInterface.isClaimed());
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a UsbControlIrp OUT to the device.  It submits the
     * UsbControlIrp ten times, and checks that it got either a UsbPipeDataEvent
     * or a UsbPipeErrorEvent back.  If it gets a data event back, it compares
     * the Irp in the data event to the Irp submitted and checks that the
     * buffered data did not change, and if it gets an error event back, it
     * makes sure that the exception in the event is not null.  It also checks
     * that the UsbControlIrp is complete immediately after the syncSubmit
     * returns.
     * @throws Exception
     */
    public void testControlIOSyncSubmitIrp_OUT() throws Exception{
        UsbControlIrp usbCtrlIOIrp;

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            byte bmRequestType =
                UsbConst.REQUESTTYPE_TYPE_STANDARD |
                UsbConst.ENDPOINT_DIRECTION_OUT |
                UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
            byte bRequest = UsbConst.REQUEST_CLEAR_FEATURE;
            short wValue = (short) 0x0000;
            short wIndex = usbCtrlIOEndpointDesc.bEndpointAddress();
            byte buffer[] = new byte[0];
            int usbCtrlIOIrpOffset;
            int usbCtrlIOIrpLength;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer);
            usbCtrlIOIrpOffset = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength = usbCtrlIOIrp.getLength();

            assertFalse("OUT Irp #" + (i+1) + ": UsbCotrolIrp should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());

            try
            {
                usbCtrlIOPipe.syncSubmit(usbCtrlIOIrp);
                assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                            usbCtrlIOIrp.isUsbException());
            } catch ( IllegalArgumentException uIAE )
            {
                fail("The buffer is set to NULL for OUT syncSubmit #" + (i+1));
            } catch ( UsbNotOpenException uNOE )
            {
                fail("The pipe is not open for OUT syncSubmit #" + (i+1));
            } catch ( UsbException uE )
            {
                assertNotNull("The OUT syncSubmit UsbException should not be null",
                              uE);
                assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                           usbCtrlIOIrp.isUsbException());
                assertEquals("The UsbException for the Irp should match the exception caught",
                             usbCtrlIOIrp.getUsbException(), uE);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A

            if ( !usbCtrlIOIrp.isComplete() )
            {
                fail("OUT Irp #" + (i+1) + ": The UsbControlIrp is not complete after syncSubmit");
            }

            for ( int j = 0; j < 400; j++ )
            {
                Thread.sleep(5);
                if ( numDataEvents + numErrorEvents > i )
                {
                    break;
                }
            }
            if ( numDataEvents + numErrorEvents <= i )
            {
                fail("No UsbPipeDataEvent or UsbPipeErrorEvent received");
            }

            assertEquals("The OUT UsbControlIrp data should not be altered",
                         buffer, usbCtrlIOIrp.getData());
            assertEquals("The OUT bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The OUT bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The OUT getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset, usbCtrlIOIrp.getOffset());
            assertEquals("The OUT getLength() shouldn't be altered",
                         usbCtrlIOIrpLength, usbCtrlIOIrp.getLength());

            if ( !usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeDataEvent lastUsbPipeDE =
                    (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                // TODO No idea why these two arrays should match. Ignoring for now
                //assertEquals("The OUT data event getData doesn't match the OUT Irp getData",
                //             usbCtrlIOIrp.getData(), lastUsbPipeDE.getData());
                assertEquals("The OUT data event UsbIrp ActualLength doesn't match the OUT Irp ActualLenth",
                             usbCtrlIOIrp.getActualLength(), lastUsbPipeDE.getUsbIrp().getActualLength());
                assertEquals("The OUT data event UsbIrp getOffset doesn't match the OUT Irp getOffset",
                             usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                assertEquals("The OUT data event UsbIrp getLength doesn't match the OUT Irp getLength",
                             usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                assertEquals("The OUT data event UsbPipe doesn't match the one the Irp was submitted on",
                             usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
            } else
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertNotNull("No USB Exception was embedded in the OUT Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The OUT error event's UsbPipe doesn't match the one the Irp was submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        if ( numDataEvents + numErrorEvents > 10 )
        {
            fail("A total of 10 UsbPipeDataEvents and UsbPipeErrorEvents are expected, " + numDataEvents
                 + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a UsbControlIrp IN from the device.  It submits the
     * UsbControlIrp ten times, and checks that it got either a UsbPipeDataEvent
     * or a UsbPipeErrorEvent back.  If it gets a data event back, it compares
     * the Irp in the data event to the Irp submitted and checks that the
     * buffered data is not null, and if it gets an error event back, it makes
     * sure that the exception in the event is not null.  It also checks that
     * the UsbControlIrp is complete immediately after the syncSubmit returns.
     * @throws Exception
     */
    public void testControlIOSyncSubmitIrp_IN() throws Exception{
        UsbControlIrp usbCtrlIOIrp;

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            byte bmRequestType =
                UsbConst.REQUESTTYPE_TYPE_STANDARD |
                UsbConst.ENDPOINT_DIRECTION_IN |
                UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
            byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
            short wValue = 
                (short) ((UsbConst.DESCRIPTOR_TYPE_STRING * (short) 0x0100) |
                0x0001);
            short wIndex = (short) 0x0000;
            byte buffer[] = new byte[64];
            int usbCtrlIOIrpOffset;
            int usbCtrlIOIrpLength;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer);
            usbCtrlIOIrp.setAcceptShortPacket(false);
            usbCtrlIOIrpOffset = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength = usbCtrlIOIrp.getLength();

            assertFalse("IN Irp #" + (i+1) + ": UsbCotrolIrp should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());
            assertFalse("IN Irp #" + (i+1) + ": UsbControlIrp.getAcceptShortPacket should be set to false",
                        usbCtrlIOIrp.getAcceptShortPacket());

            try
            {
                usbCtrlIOPipe.syncSubmit(usbCtrlIOIrp);
                assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                            usbCtrlIOIrp.isUsbException());
            } catch ( IllegalArgumentException uIAE )
            {
                fail("The buffer is set to NULL for IN syncSubmit #" + (i+1));
            } catch ( UsbNotOpenException uNOE )
            {
                fail("The pipe is not open for IN syncSubmit #" + (i+1));
            } catch ( UsbException uE )
            {
                assertNotNull("The IN syncSubmit UsbException should not be null",
                              uE);
                assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                           usbCtrlIOIrp.isUsbException());
                assertEquals("The UsbException for the Irp should match the exception caught",
                             usbCtrlIOIrp.getUsbException(), uE);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A

            if ( !usbCtrlIOIrp.isComplete() )
            {
                fail("IN Irp #" + (i+1) + ": The UsbControlIrp is not complete after syncSubmit");
            }

            for ( int j = 0; j < 400; j++ )
            {
                Thread.sleep(5);
                if ( numDataEvents + numErrorEvents > i )
                {
                    break;
                }
            }
            if ( numDataEvents + numErrorEvents <= i )
            {
                fail("No UsbPipeDataEvent or UsbPipeErrorEvent received");
            }

            assertNotNull("The IN UsbControlIrp data should not be NULL",
                          usbCtrlIOIrp.getData());
            assertEquals("The IN bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The IN bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The IN getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset, usbCtrlIOIrp.getOffset());
            assertEquals("The IN getLength() shouldn't be altered",
                         usbCtrlIOIrpLength, usbCtrlIOIrp.getLength());

            if ( !usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeDataEvent lastUsbPipeDE =
                    (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                assertNotNull("The IN data event getData shouldn't be NULL",
                              lastUsbPipeDE.getData());
                assertTrue("The IN data event UsbIrp ActualLength should be >= 0",
                           lastUsbPipeDE.getUsbIrp().getActualLength() >= (byte) 0);
                assertEquals("The IN data event UsbIrp getOffsetdoesn't match the IN Irp getOffset",
                             usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                assertEquals("The IN data event UsbIrp getLength doesn't match the IN Irp getLength",
                             usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                assertEquals("The IN data event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
            } else
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertNotNull("No USB Exception was embedded in the IN Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The IN error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        if ( numDataEvents + numErrorEvents > 10 )
        {
            fail("A total of 10 UsbPipeDataEvents and UsbPipeErrorEvents are expected, " + numDataEvents
                 + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a list of UsbControlIrps OUT to the device.  It submits
     * the list, and checks that it got either got ten UsbPipeDataEvents (one
     * for each Irp in the list) or no more than nine UsbPipeDataEvents and a
     * UsbPipeErrorEvent back. For each data event it receives, it compares the
     * Irp in the data event to the Irp submitted and checks that the buffered
     * data did not change. For each error event received, it makes sure that
     * the exception in the event is not null.  It also checks that the
     * UsbControlIrp is complete for all Irps in the list immediately after the
     * syncSubmit returns.
     * @throws Exception
     */
    public void testControlIOSyncSubmitIrpList_OUT() throws Exception{
        UsbControlIrp usbCtrlIOIrp;
        UsbException exceptionReceived = null;
        List usbCtrlIOIrpList = new ArrayList();
        int usbCtrlIOIrpOffset[] = new int[10];
        int usbCtrlIOIrpLength[] = new int[10];
        byte bmRequestType =
            UsbConst.REQUESTTYPE_TYPE_STANDARD |
            UsbConst.ENDPOINT_DIRECTION_OUT |
            UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
        byte bRequest = UsbConst.REQUEST_CLEAR_FEATURE;
        byte buffer[] = new byte[10];

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            short wValue = (short) 0x0000;
            short wIndex = usbCtrlIOEndpointDesc.bEndpointAddress();

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer,i,0);
            usbCtrlIOIrpOffset[i] = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength[i] = usbCtrlIOIrp.getLength();

            assertFalse("OUT Irp #" + (i+1) + " in List: UsbCotrolIrp in a list should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());
            usbCtrlIOIrpList.add((Object) usbCtrlIOIrp);
        }

        try
        {
            usbCtrlIOPipe.syncSubmit(usbCtrlIOIrpList);
        } catch ( IllegalArgumentException uIAE )
        {
            fail("The buffer is set to NULL for OUT syncSubmit");
        } catch ( UsbNotOpenException uNOE )
        {
            fail("The pipe is not open for OUT syncSubmit");
        } catch ( UsbException uE )
        {
            exceptionReceived = uE;
            assertNotNull("The OUT syncSubmit UsbException should not be null",
                          uE);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        for ( int i = 0; i < 4000; i++ )
        {
            Thread.sleep(5);
            if ( numDataEvents  == 10 || numErrorEvents >= 1 )
                break;
        }
        if ( numDataEvents + numErrorEvents  < 10 && numErrorEvents == 0 )
            fail("10 UsbPipeDataEvents or 0 or more UsbPipeDataEvents & 1 or more UsbPipeErrorEvents expected, " +
                 numDataEvents + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");
        else if ( numDataEvents + numErrorEvents > 10 )
            fail("A maximum total of 10 UsbPipeDataEvents & ErrorEvents were expected, " +
                 numDataEvents + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");

        for ( int i = 0; i < 10; i++ )
        {
            usbCtrlIOIrp = (UsbControlIrp)usbCtrlIOIrpList.remove(0);

            if ( i < numDataEvents + numErrorEvents )
            {
                assertTrue("OUT Irp #" + (i+1) + " in List: The UsbControlIrp is not complete after syncSubmit",
                           usbCtrlIOIrp.isComplete());
                if ( i < numDataEvents )
                {
                    assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                                usbCtrlIOIrp.isUsbException());
                } else
                {
                    assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                               usbCtrlIOIrp.isUsbException());
                }
            } else
            {
                assertFalse("OUT Irp #" + (i+1) + " in List: The UsbControlIrp should not be complete since it wasn't submitted",
                            usbCtrlIOIrp.isComplete());
                assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                            usbCtrlIOIrp.isUsbException());
            }

            assertEquals("The OUT UsbControlIrp data should not be altered",
                         buffer, usbCtrlIOIrp.getData());
            assertEquals("The OUT bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The OUT bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The OUT getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset[i], usbCtrlIOIrp.getOffset());
            assertEquals("The OUT getLength() shouldn't be altered",
                         usbCtrlIOIrpLength[i], usbCtrlIOIrp.getLength());

            if ( usbCtrlIOIrp.isComplete() && !usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeDataEvent lastUsbPipeDE =
                    (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                // TODO No idea why these two arrays should match. Ignoring for now
                //assertEquals("The OUT data event getData doesn't match the OUT Irp getData",
                //             usbCtrlIOIrp.getData(), lastUsbPipeDE.getData());
                assertEquals("The OUT data event UsbIrp ActualLength doesn't match the OUT Irp ActualLenth",
                             usbCtrlIOIrp.getActualLength(), lastUsbPipeDE.getUsbIrp().getActualLength());
                assertEquals("The OUT data event UsbIrp getOffset doesn't match the OUT Irp getOffset",
                             usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                assertEquals("The OUT data event UsbIrp getLength doesn't match the OUT Irp getLength",
                             usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                assertEquals("The OUT data event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
            } else if ( usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertNotNull("No USB Exception was embedded in the OUT Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The OUT error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a list of UsbControlIrps IN from the device.  It submits
     * the list, and checks that it got either got ten UsbPipeDataEvents (one
     * for each Irp in the list) or no more than nine UsbPipeDataEvents and a
     * UsbPipeErrorEvent back. For each data event it receives, it compares the
     * Irp in the data event to the Irp submitted and checks that the buffered
     * data is not null. For each error event received, it makes sure that the
     * exception in the event is not null.  It also checks that the
     * UsbControlIrp is complete for all Irps in the list immediately after the
     * syncSubmit returns.
     * @throws Exception
     */
    public void testControlIOSyncSubmitIrpList_IN() throws Exception{
        UsbControlIrp usbCtrlIOIrp;
        UsbException exceptionReceived = null;
        List usbCtrlIOIrpList = new ArrayList();
        int usbCtrlIOIrpOffset[] = new int[10];
        int usbCtrlIOIrpLength[] = new int[10];
        byte bmRequestType =
            UsbConst.REQUESTTYPE_TYPE_STANDARD |
            UsbConst.ENDPOINT_DIRECTION_IN |
            UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        byte buffer[] = new byte[640];

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            short wValue =
                (short) ((UsbConst.DESCRIPTOR_TYPE_STRING * (short) 0x0100) |
                0x0001);
            short wIndex = (short) 0x0000;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer,(i*64),64);
            usbCtrlIOIrp.setAcceptShortPacket(false);
            usbCtrlIOIrpOffset[i] = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength[i] = usbCtrlIOIrp.getLength();

            assertFalse("IN Irp #" + (i+1) + " in List: UsbCotrolIrp in a list should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());
            assertFalse("IN Irp #" + (i+1) + " in List: UsbControlIrp.getAcceptShortPacket should be set to false",
                        usbCtrlIOIrp.getAcceptShortPacket());
            usbCtrlIOIrpList.add((Object) usbCtrlIOIrp);
        }

        try
        {
            usbCtrlIOPipe.syncSubmit(usbCtrlIOIrpList);
        } catch ( IllegalArgumentException uIAE )
        {
            fail("The buffer is set to NULL for IN syncSubmit");
        } catch ( UsbNotOpenException uNOE )
        {
            fail("The pipe is not open for IN syncSubmit");
        } catch ( UsbException uE )
        {
            exceptionReceived = uE;
            assertNotNull("The IN syncSubmit UsbException should not be null",
                          uE);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        for ( int i = 0; i < 4000; i++ )
        {
            Thread.sleep(5);
            if ( numDataEvents  == 10 || numErrorEvents >= 1 )
                break;
        }
        if ( numDataEvents + numErrorEvents  < 10 && numErrorEvents == 0 )
            fail("10 UsbPipeDataEvents or 0 or more UsbPipeDataEvents & 1 or more UsbPipeErrorEvents expected, " +
                 numDataEvents + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");
        else if ( numDataEvents + numErrorEvents > 10 )
            fail("A maximum total of 10 UsbPipeDataEvents & ErrorEvents were expected, " +
                 numDataEvents + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");

        for ( int i = 0; i < 10; i++ )
        {
            usbCtrlIOIrp = (UsbControlIrp)usbCtrlIOIrpList.get(i);

            if ( i < numDataEvents + numErrorEvents )
            {
                assertTrue("OUT Irp #" + (i+1) + " in List: The UsbControlIrp is not complete after syncSubmit",
                           usbCtrlIOIrp.isComplete());
                if ( i < numDataEvents )
                {
                    assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                                usbCtrlIOIrp.isUsbException());
                } else
                {
                    assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                               usbCtrlIOIrp.isUsbException());
                }
            } else
            {
                assertFalse("OUT Irp #" + (i+1) + " in List: The UsbControlIrp should not be complete since it wasn't submitted",
                            usbCtrlIOIrp.isComplete());
                assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                            usbCtrlIOIrp.isUsbException());
            }

            assertNotNull("The IN UsbControlIrp data should not be NULL",
                          usbCtrlIOIrp.getData());
            assertEquals("The IN bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The IN bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The IN getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset[i], usbCtrlIOIrp.getOffset());
            assertEquals("The IN getLength() shouldn't be altered",
                         usbCtrlIOIrpLength[i], usbCtrlIOIrp.getLength());

            if ( usbCtrlIOIrp.isComplete() && !usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeDataEvent lastUsbPipeDE =
                    (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                assertNotNull("The IN data event getData shouldn't be NULL",
                              lastUsbPipeDE.getData());
                assertTrue("The IN data event ActualLength should be >= 0",
                           lastUsbPipeDE.getUsbIrp().getActualLength() >= (byte) 0);
                assertEquals("The IN data event UsbIrp getOffset doesn't match the IN Irp getOffset",
                             usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                assertEquals("The IN data event UsbIrp getLength doesn't match the IN Irp getLength",
                             usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                assertEquals("The IN data event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
            } else if ( usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertNotNull("No USB Exception was embedded in the OUT Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The IN error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does an
     * asyncSubmit with a UsbControlIrp OUT to the device.  It submits the
     * UsbControlIrp ten times, and checks thatit got either a UsbPipeDataEvent
     * or a UsbPipeErrorEvent back.  If it gets a data event back, it compares
     * the Irp in the data event to the Irp submitted and checks that the
     * buffered data did not change, and if it gets an error event back, it
     * makes sure that the exception in the event is not null.  It also checks
     * that the UsbControlIrp is complete when waitForComplete(5000) is called
     * on the UsbControlIrp after the asyncSubmit returns.
     * @throws Exception
     */
    public void testControlIOAsyncSubmitIrp_OUT() throws Exception{
        UsbControlIrp usbCtrlIOIrp;

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            byte bmRequestType =
                UsbConst.REQUESTTYPE_TYPE_STANDARD |
                UsbConst.ENDPOINT_DIRECTION_OUT |
                UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
            byte bRequest = UsbConst.REQUEST_CLEAR_FEATURE;
            short wValue = (short) 0x0000;
            short wIndex = usbCtrlIOEndpointDesc.bEndpointAddress();
            byte buffer[] = new byte[0];
            int usbCtrlIOIrpOffset;
            int usbCtrlIOIrpLength;
            int lastNumDataEvents = numDataEvents;
            int lastNumErrorEvents = numErrorEvents;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer);
            usbCtrlIOIrpOffset = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength = usbCtrlIOIrp.getLength();

            assertFalse("OUT Irp #" + (i+1) + ": UsbCotrolIrp should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());

            try
            {
                usbCtrlIOPipe.asyncSubmit(usbCtrlIOIrp);
            } catch ( IllegalArgumentException uIAE )
            {
                fail("The buffer is set to NULL for OUT asyncSubmit #" + (i+1));
            } catch ( UsbNotOpenException uNOE )
            {
                fail("The pipe is not open for OUT asyncSubmit #" + (i+1));
            } catch ( UsbException uE )
            {
                assertNotNull("The OUT asyncSubmit UsbException should not be null",
                              uE);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A

            usbCtrlIOIrp.waitUntilComplete(5000);
            assertTrue("OUT Irp #" + (i+1) + ": The UsbCotrolIrp should be complete after waitUntilComplete",
                       usbCtrlIOIrp.isComplete());

            for ( int j = 0; j < 400; j++ )
            {
                Thread.sleep(5);
                if ( numDataEvents + numErrorEvents > i )
                {
                    if ( numDataEvents > lastNumDataEvents )
                    {
                        assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                                    usbCtrlIOIrp.isUsbException());
                    } else
                    {
                        assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                                   usbCtrlIOIrp.isUsbException());
                    }
                    break;
                }
            }
            if ( numDataEvents + numErrorEvents <= i )
            {
                fail("No UsbPipeDataEvent or UsbPipeErrorEvent received");
            }

            assertEquals("The OUT UsbControlIrp data should not be altered",
                         buffer, usbCtrlIOIrp.getData());
            assertEquals("The OUT bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The OUT bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The OUT getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset, usbCtrlIOIrp.getOffset());
            assertEquals("The OUT getLength() shouldn't be altered",
                         usbCtrlIOIrpLength, usbCtrlIOIrp.getLength());

            if ( !usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeDataEvent lastUsbPipeDE =
                    (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                // TODO No idea why these two arrays should match. Ignoring for now
                //assertEquals("The OUT data event getData doesn't match the OUT Irp getData",
                //             usbCtrlIOIrp.getData(), lastUsbPipeDE.getData());
                assertEquals("The OUT data event UsbIrp ActualLength doesn't match the OUT Irp ActualLenth",
                             usbCtrlIOIrp.getActualLength(), lastUsbPipeDE.getUsbIrp().getActualLength());
                assertEquals("The OUT data event UsbIrp getOffset doesn't match the OUT Irp getOffset",
                             usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                assertEquals("The OUT data event UsbIrp getLength doesn't match the OUT Irp getLength",
                             usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                assertEquals("The OUT data event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
            } else
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertNotNull("No USB Exception was embedded in the OUT Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The OUT error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        if ( numDataEvents + numErrorEvents > 10 )
        {
            fail("A total of 10 UsbPipeDataEvents and UsbPipeErrorEvents are expected, " + numDataEvents
                 + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a UsbControlIrp IN from the device.  It submits the
     * UsbControlIrp ten times, and checks that it got either a UsbPipeDataEvent
     * or a UsbPipeErrorEvent back.  If it gets a data event back, it compares
     * the Irp in the data event to the Irp submitted and checks that the
     * buffered data is not null, and if it gets an error event back, it makes
     * sure that the exception in the event is not null.  It also checks that
     * the UsbControlIrp is complete when waitForComplete(5000) is called on the
     * UsbControlIrp after the asyncSubmit returns.
     * @throws Exception
     */
    public void testControlIOAsyncSubmitIrp_IN() throws Exception{
        UsbControlIrp usbCtrlIOIrp;
        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            byte bmRequestType =
                UsbConst.REQUESTTYPE_TYPE_STANDARD |
                UsbConst.ENDPOINT_DIRECTION_IN |
                UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
            byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
            short wValue =
                (short) ((UsbConst.DESCRIPTOR_TYPE_STRING * (short) 0x0100) |
                0x0001);
            short wIndex = (short) 0x0000;
            byte buffer[] = new byte[64];
            int usbCtrlIOIrpOffset;
            int usbCtrlIOIrpLength;
            int lastNumDataEvents = numDataEvents;
            int lastNumErrorEvents = numErrorEvents;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer);
            usbCtrlIOIrp.setAcceptShortPacket(false);
            usbCtrlIOIrpOffset = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength = usbCtrlIOIrp.getLength();

            assertFalse("IN Irp #" + (i+1) + ": UsbCotrolIrp should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());
            assertFalse("IN Irp #" + (i+1) + ": UsbControlIrp.getAcceptShortPacket should be set to false",
                        usbCtrlIOIrp.getAcceptShortPacket());

            try
            {
                usbCtrlIOPipe.asyncSubmit(usbCtrlIOIrp);
            } catch ( IllegalArgumentException uIAE )
            {
                fail("The buffer is set to NULL for IN asyncSubmit #" + (i+1));
            } catch ( UsbNotOpenException uNOE )
            {
                fail("The pipe is not open for IN asyncSubmit #" + (i+1));
            } catch ( UsbException uE )
            {
                assertNotNull("The IN asyncSubmit UsbException should not be null",
                              uE);
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A

            usbCtrlIOIrp.waitUntilComplete(5000);
            assertTrue("IN Irp #" + (i+1) + ": The UsbCotrolIrp should be complete after waitUntilComplete",
                       usbCtrlIOIrp.isComplete());

            for ( int j = 0; j < 400; j++ )
            {
                Thread.sleep(5);
                if ( numDataEvents + numErrorEvents > i )
                {
                    if ( numDataEvents > lastNumDataEvents )
                    {
                        assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                                    usbCtrlIOIrp.isUsbException());
                    } else
                    {
                        assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                                   usbCtrlIOIrp.isUsbException());
                    }
                    break;
                }
            }
            if ( numDataEvents + numErrorEvents <= i )
            {
                fail("No UsbPipeDataEvent or UsbPipeErrorEvent received");
            }

            assertNotNull("The IN UsbControlIrp data should not be NULL",
                          usbCtrlIOIrp.getData());
            assertEquals("The IN bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The IN bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The IN getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset, usbCtrlIOIrp.getOffset());
            assertEquals("The IN getLength() shouldn't be altered",
                         usbCtrlIOIrpLength, usbCtrlIOIrp.getLength());

            if ( !usbCtrlIOIrp.isUsbException() )
            {
                UsbPipeDataEvent lastUsbPipeDE =
                    (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                assertNotNull("The IN data event getData shouldn't be NULL",
                              lastUsbPipeDE.getData());
                assertTrue("The IN data event ActualLength should be >= 0",
                           lastUsbPipeDE.getUsbIrp().getActualLength() >= (byte) 0);
                assertEquals("The IN data event UsbIrp getOffset doesn't match the IN Irp getOffset",
                             usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                assertEquals("The IN data event UsbIrp getLength doesn't match the IN Irp getLength",
                             usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                assertEquals("The IN data event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
            } else
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertNotNull("No USB Exception was embedded in the IN Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The IN error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        if ( numDataEvents + numErrorEvents > 10 )
        {
            fail("A total of 10 UsbPipeDataEvents and UsbPipeErrorEvents are expected, " + numDataEvents
                 + " UsbPipeDataEvents & " + numErrorEvents + " UsbPipeErrorEvents received");
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a list of UsbControlIrps OUT to the device.  It submits
     * the list, and checks that it got either a total of ten UsbPipeDataEvents
     * and UsbPipeErrorEvents (a data event or error event for each Irp in the
     * list) back. For each data event it receives, it compares the Irp in the
     * data event to the Irp submitted and checks that the buffered data did not
     * change. For each error event received it makes sure that the exception in
     * the event is not null.  It also checks that the UsbControlIrp is complete
     * when waitForComplete(5000) is called on all UsbControlIrps in the list
     * after the asyncSubmit returns.
     * @throws Exception
     */
    public void testControlIOAsyncSubmitIrpList_OUT() throws Exception{
        UsbControlIrp usbCtrlIOIrp;
        List usbCtrlIOIrpList = new ArrayList();
        int usbCtrlIOIrpOffset[] = new int[10];
        int usbCtrlIOIrpLength[] = new int[10];
        byte bmRequestType =
            UsbConst.REQUESTTYPE_TYPE_STANDARD |
            UsbConst.ENDPOINT_DIRECTION_OUT |
            UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
        byte bRequest = UsbConst.REQUEST_CLEAR_FEATURE;
        byte buffer[] = new byte[10];

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            e.printStackTrace();
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            short wValue = (short) 0x0000;
            short wIndex = usbCtrlIOEndpointDesc.bEndpointAddress();

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer,i,0);
            usbCtrlIOIrpOffset[i] = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength[i] = usbCtrlIOIrp.getLength();

            assertFalse("OUT Irp #" + (i+1) + " in List: UsbCotrolIrp in a list should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());
            usbCtrlIOIrpList.add((Object) usbCtrlIOIrp);
        }

        try
        {
            usbCtrlIOPipe.asyncSubmit(usbCtrlIOIrpList);
        } catch ( IllegalArgumentException uIAE )
        {
            fail("The buffer is set to NULL for OUT asyncSubmit");
        } catch ( UsbNotOpenException uNOE )
        {
            fail("The pipe is not open for OUT asyncSubmit");
        } catch ( UsbException uE )
        {
            assertNotNull("The OUT asyncSubmit UsbException should not be null",
                          uE);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        for ( int i = 0; i < 10; i++ )
        {
            usbCtrlIOIrp = (UsbControlIrp)usbCtrlIOIrpList.get(i);

            usbCtrlIOIrp.waitUntilComplete(5000);
            assertTrue("OUT Irp #" + (i+1) + " in List: The UsbControlIrp is not complete after waitForComplete",
                       usbCtrlIOIrp.isComplete());

            for ( int j = 0; j < 400; j++ )
            {
                Thread.sleep(5);
                if ( numDataEvents + numErrorEvents > i )
                    break;
            }
        }

        if ( numDataEvents + numErrorEvents  != 10 )
            fail("A total of 10 UsbPipeDataEvents and UsbPipeDataEvents are expected, " +
                 numDataEvents + " UsbPipeDataEvents  & " + numErrorEvents + " UsbPipeErrorEvents received");

        for ( int i = 0; i < 10; i++ )
        {
            usbCtrlIOIrp = (UsbControlIrp)usbCtrlIOIrpList.remove(0);

            assertEquals("The OUT UsbControlIrp data should not be altered",
                         buffer, usbCtrlIOIrp.getData());
            assertEquals("The OUT bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The OUT bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The OUT getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset[i], usbCtrlIOIrp.getOffset());
            assertEquals("The OUT getLength() shouldn't be altered",
                         usbCtrlIOIrpLength[i], usbCtrlIOIrp.getLength());

            if ( LastUsbPipeDataEvent.size() > 0 )
            {
                UsbPipeDataEvent lastUsbPipeDE = (UsbPipeDataEvent) LastUsbPipeDataEvent.get(0);

                if ( usbCtrlIOIrpOffset[i] == lastUsbPipeDE.getUsbIrp().getOffset() &&
                     usbCtrlIOIrpLength[i] == lastUsbPipeDE.getUsbIrp().getLength() )
                {
                    lastUsbPipeDE = (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                    assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                                usbCtrlIOIrp.isUsbException());
                    // TODO No idea why these two arrays should match. Ignoring for now
                    //assertEquals("The OUT data event getData doesn't match the OUT Irp getData",
                    //           usbCtrlIOIrp.getData(), lastUsbPipeDE.getData());
                    assertEquals("The OUT data event UsbIrp ActualLength doesn't match the OUT Irp ActualLenth",
                                 usbCtrlIOIrp.getActualLength(), lastUsbPipeDE.getUsbIrp().getActualLength());
                    assertEquals("The OUT data event UsbIrp getOffset doesn't match the OUT Irp getOffset",
                                 usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                    assertEquals("The OUT data event UsbIrp getLength doesn't match the OUT Irp getLength",
                                 usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                    assertEquals("The OUT data event UsbPipe doesn't match the UsbPipe submitted on",
                                 usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
                } else
                {
                    UsbPipeErrorEvent lastUsbPipeEE = (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                    assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                               usbCtrlIOIrp.isUsbException());
                    assertNotNull("No USB Exception was embedded in the OUT Error Event",
                                  lastUsbPipeEE.getUsbException());
                    assertEquals("The UsbException for the Irp should match the error event exception",
                                 usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                    assertEquals("The OUT error event UsbPipe doesn't match the UsbPipe submitted on",
                                 usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
                }
            } else
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                           usbCtrlIOIrp.isUsbException());
                assertNotNull("No USB Exception was embedded in the OUT Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The OUT error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes a UsbPipe opened by the openControlIOPipe() method and does a
     * syncSubmit with a list of UsbControlIrps IN from the device.  It submits
     * the list, and checks that it got either a total of ten UsbPipeDataEvents
     * and UsbPipeErrorEvents (a data event or error event for each Irp in the
     * list) back. For each data event it receives, it compares the Irp in the
     * data event to the Irp submitted and checks that the buffered data is not
     * null. For each error event received, it makes sure that the exception in
     * the event is not null.  It also checks that the UsbControlIrp is complete
     * when waitForComplete(5000) is called on all UsbControlIrps in the list
     * after the asyncSubmit returns.
     * @throws Exception
     */
    public void testControlIOAsyncSubmitIrpList_IN() throws Exception{
        UsbControlIrp usbCtrlIOIrp;
        List usbCtrlIOIrpList = new ArrayList();
        int usbCtrlIOIrpOffset[] = new int[10];
        int usbCtrlIOIrpLength[] = new int[10];
        byte bmRequestType =
        UsbConst.REQUESTTYPE_TYPE_STANDARD |
        UsbConst.ENDPOINT_DIRECTION_IN |
        UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        byte buffer[] = new byte[640];

        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }

        for ( int i = 0; i < 10; i++ )
        {
            short wValue =
                (short) ((UsbConst.DESCRIPTOR_TYPE_STRING * (short) 0x0100) |
                0x0001);
            short wIndex = (short) 0x0000;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer,(i*64),64);
            usbCtrlIOIrp.setAcceptShortPacket(false);
            usbCtrlIOIrpOffset[i] = usbCtrlIOIrp.getOffset();
            usbCtrlIOIrpLength[i] = usbCtrlIOIrp.getLength();

            assertFalse("IN Irp #" + (i+1) + " in List: UsbCotrolIrp in a list should not be complete before the submit",
                        usbCtrlIOIrp.isComplete());
            assertFalse("IN Irp #" + (i+1) + " in List: UsbControlIrp.getAcceptShortPacket should be set to false",
                        usbCtrlIOIrp.getAcceptShortPacket());
            usbCtrlIOIrpList.add((Object) usbCtrlIOIrp);
        }

        try
        {
            usbCtrlIOPipe.asyncSubmit(usbCtrlIOIrpList);
        } catch ( IllegalArgumentException uIAE )
        {
            fail("The buffer is set to NULL for IN asyncSubmit");
        } catch ( UsbNotOpenException uNOE )
        {
            fail("The pipe is not open for IN asyncSubmit");
        } catch ( UsbException uE )
        {
            assertNotNull("The IN asyncSubmit UsbException should not be null",
                          uE);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        for ( int i = 0; i < 10; i++ )
        {
            usbCtrlIOIrp = (UsbControlIrp)usbCtrlIOIrpList.get(i);

            usbCtrlIOIrp.waitUntilComplete(5000);
            assertTrue("OUT Irp #" + (i+1) + " in List: The UsbControlIrp is not complete after waitForComplete",
                       usbCtrlIOIrp.isComplete());

            for ( int j = 0; j < 400; j++ )
            {
                Thread.sleep(5);
                if ( numDataEvents + numErrorEvents > i )
                    break;
            }
        }

        if ( numDataEvents + numErrorEvents  != 10 )
            fail("A total of 10 UsbPipeDataEvents and UsbPipeDataEvents are expected, " +
                 numDataEvents + " UsbPipeDataEvents  & " + numErrorEvents + " UsbPipeErrorEvents received");

        for ( int i = 0; i < 10; i++ )
        {
            usbCtrlIOIrp = (UsbControlIrp)usbCtrlIOIrpList.get(i);

            assertNotNull("The IN UsbControlIrp data should not be NULL",
                          usbCtrlIOIrp.getData());
            assertEquals("The IN bmRequestType shouldn't be altered",
                         bmRequestType, usbCtrlIOIrp.bmRequestType());
            assertEquals("The IN bRequest shouldn't be altered",
                         bRequest, usbCtrlIOIrp.bRequest());
            assertEquals("The IN getOffset() shouldn't be altered",
                         usbCtrlIOIrpOffset[i], usbCtrlIOIrp.getOffset());
            assertEquals("The IN getLength() shouldn't be altered",
                         usbCtrlIOIrpLength[i], usbCtrlIOIrp.getLength());

            if ( LastUsbPipeDataEvent.size() > 0 )
            {
                UsbPipeDataEvent lastUsbPipeDE = (UsbPipeDataEvent) LastUsbPipeDataEvent.get(0);

                if ( usbCtrlIOIrpOffset[i] == lastUsbPipeDE.getUsbIrp().getOffset() &&
                     usbCtrlIOIrpLength[i] == lastUsbPipeDE.getUsbIrp().getLength() )
                {
                    lastUsbPipeDE = (UsbPipeDataEvent) LastUsbPipeDataEvent.remove(0);

                    assertFalse("The usbCtrlIOIrp.isUsbException should be set to FALSE",
                                usbCtrlIOIrp.isUsbException());
                    assertNotNull("The IN data event getData shouldn't be NULL",
                                  lastUsbPipeDE.getData());
                    assertTrue("The IN data event ActualLength should be >= 0",
                               lastUsbPipeDE.getUsbIrp().getActualLength() >= (byte) 0);
                    assertEquals("The IN data event UsbIrp getOffset doesn't match the IN Irp getOffset",
                                 usbCtrlIOIrp.getOffset(), lastUsbPipeDE.getUsbIrp().getOffset());
                    assertEquals("The IN data event UsbIrp getLength doesn't match the IN Irp getLength",
                                 usbCtrlIOIrp.getLength(), lastUsbPipeDE.getUsbIrp().getLength());
                    assertEquals("The IN data event UsbPipe doesn't match the UsbPipe submitted on",
                                 usbCtrlIOPipe, lastUsbPipeDE.getUsbPipe());
                } else
                {
                    UsbPipeErrorEvent lastUsbPipeEE = (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                    assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                               usbCtrlIOIrp.isUsbException());
                    assertNotNull("No USB Exception was embedded in the OUT Error Event",
                                  lastUsbPipeEE.getUsbException());
                    assertEquals("The UsbException for the Irp should match the error event exception",
                                 usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                    assertEquals("The IN error event UsbPipe doesn't match the UsbPipe submitted on",
                                 usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
                }
            } else
            {
                UsbPipeErrorEvent lastUsbPipeEE =
                    (UsbPipeErrorEvent) LastUsbPipeErrorEvent.remove(0);

                assertTrue("The usbCtrlIOIrp.isUsbException should be set to TRUE",
                           usbCtrlIOIrp.isUsbException());
                assertNotNull("No USB Exception was embedded in the OUT Error Event",
                              lastUsbPipeEE.getUsbException());
                assertEquals("The UsbException for the Irp should match the error event exception",
                             usbCtrlIOIrp.getUsbException(), lastUsbPipeEE.getUsbException());
                assertEquals("The IN error event UsbPipe doesn't match the UsbPipe submitted on",
                             usbCtrlIOPipe, lastUsbPipeEE.getUsbPipe());
            }
        }

        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
    }

    /**
     * Takes an open UsbPipe and calls the UsbPipe.abortAllSubmissions method to
     * test that it doesn't throw any exceptions on an open pipe.
     * @throws Exception
     */
    public void testAbortAllSubmissionsOpenPipe() throws Exception {
        try
        {
            openControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening the Control I/O Pipe");
        }
        assertTrue("The usbPipe.isOpen() method should be true on an open pipe",
                   usbCtrlIOPipe.isOpen());
        try
        {
            usbCtrlIOPipe.abortAllSubmissions();
        } catch ( UsbNotOpenException uNOE )
        {
            fail("AbortAllSubmissions threw a UsbNotOpenException on an open pipe");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
        try
        {
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while closing the Control I/O Pipe");
        }
        assertFalse("The usbPipe.isOpen() method should be false on a closed pipe",
                    usbCtrlIOPipe.isOpen());
    }

    /**
     * Takes a closed UsbPipe and calls the UsbPipe.abortAllSubmissions method
     * to test that it throws a UsbNotOpenException on a closed pipe.
     * @throws Exception
     */
    public void testAbortAllSubmissionsClosedPipe()
    {
        try
        {
            openControlIOPipe();
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening or closing the Control I/O Pipe");
        }
        assertFalse("The usbPipe.isOpen() method should be false on a closed pipe",
                    usbCtrlIOPipe.isOpen());
        try
        {
            usbCtrlIOPipe.abortAllSubmissions();
            fail("abortAllSubmissions didn't throw a UsbNotOpenException on a closed pipe");
        } catch ( UsbNotOpenException uNOE )
        {
            assertNotNull("The UsbNotOpenException shouldn't be null",
                          uNOE);
        }
    }

    /**
     * Takes a closed UsbPipe and calls the UsbPipe.syncSubmit method with a
     * UsbControlIrp to test that it throws a UsbNotOpenException on a closed
     * pipe.
     * @throws Exception
     */
    public void testSyncSubmitClosedPipe()
    {
        try
        {
            openControlIOPipe();
            closeControlIOPipe();
        } catch ( Exception e )
        {
            fail("There was an exception while opening or closing the Control I/O Pipe");
        }
        assertFalse("The usbPipe.isOpen() method should be fale on a closed pipe",
                    usbCtrlIOPipe.isOpen());
        try
        {
            UsbControlIrp usbCtrlIOIrp;
            byte bmRequestType =
                UsbConst.REQUESTTYPE_TYPE_STANDARD |
                UsbConst.ENDPOINT_DIRECTION_OUT |
                UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
            byte bRequest = UsbConst.REQUEST_CLEAR_FEATURE;
            short wValue = (short) 0x0000;
            short wIndex = usbCtrlIOEndpointDesc.bEndpointAddress();
            byte buffer[] = new byte[0];
            int usbCtrlIOIrpOffset;
            int usbCtrlIOIrpLength;

            usbCtrlIOIrp = usbCtrlIOPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
            usbCtrlIOIrp.setData(buffer);
            usbCtrlIOPipe.syncSubmit(usbCtrlIOIrp);
            fail("syncSubmit didn't throw a UsbNotOpenException on a closed pipe");
        } catch ( UsbNotOpenException uNOE )
        {
            assertNotNull("The UsbNotOpenException shouldn't be null",
                          uNOE);
        } catch ( UsbException uE )
        {
            fail("UsbNotOpenException expected.  Submitting on a closed pipe should not throw a generic UsbException" + uE);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        } catch ( java.lang.IllegalArgumentException iAE )                                    // @P1C
        {
            fail("Submit should not throw IllegalArguementException w/valid Irp is used");
        }
    }

    /**
     * This method listens for all the UsbPipeDataEvents and UsbPipeErrorEvents
     * and tests that they are not null.  It then makes sure that the data and
     * error events are available for the test cases above to access to ensure
     * that they correspond to data or error events expected within the test
     * cases.
     */
    protected UsbPipeListener usbCtrlIOPipeListener = new UsbPipeListener()
    {
        public void dataEventOccurred(UsbPipeDataEvent uPDE)
        {
            numDataEvents++;
            assertNotNull("The Data Event object shouldn't be null", uPDE);
            LastUsbPipeDataEvent.add((Object) uPDE);
        }
        public void errorEventOccurred(UsbPipeErrorEvent uPEE)
        {
            numErrorEvents++;
            assertNotNull("The Error Event object shouldn't be null", uPEE);
            LastUsbPipeErrorEvent.add((Object) uPEE);
        }
    };

    //**************************************************************************
    // Private variables accessable between the tests

    private UsbDevice usbCtrlIODev;
    private UsbInterface usbCtrlIOInterface;
    private UsbInterfaceDescriptor usbCtrlIOInterfaceDesc;
    private UsbEndpoint usbCtrlIOEndpoint;
    private UsbEndpointDescriptor usbCtrlIOEndpointDesc;
    private UsbPipe usbCtrlIOPipe;
    private List LastUsbPipeDataEvent;
    private List LastUsbPipeErrorEvent;
    private int numDataEvents;
    private int numErrorEvents;
}
