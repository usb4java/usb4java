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
 
package javax.usb.tck;
 
import java.util.*;
 
import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;
 
 
import junit.framework.Assert;
 
/**
 * IOShortPacketTest -- Helper methods for Bulk, Interrupt, and Isochronous IO Tests
 * UsbShortPacketException test.
 * <p>
 * The goal of the Bulk, Interrupt, and Isochronous IO test is to
 * verify that IN and OUT pipes can be opened and closed, and verify
 * that bulk, interrupt, and isochronous transfer operations work successfully, proper
 * events are generated, and proper exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */
 

@SuppressWarnings("all")
public class IOShortPacketTest
{
 
     protected void RoundTripIOTestShortPacket(byte testType,  int syncOrAsync,int  numIrps, int endpointmaxPacketSize,
                                   boolean []acceptShortPacket, boolean []verifyAcceptShortPacket,
                                   int[] OUTLength, int[] OUTOffset, int[] OUTExpectedLength,
                                   Exception []OUTexpectedException,
 
                                   int[] INLength, int[]INOffset, int[]INExpectedLength,
                                   Exception []INexpectedException,
                                   byte [] transformType
                                  )
 
    {
    	
    	//Note that this code is based on IOTests.RounTripTest which handles IRP, IRPList, and Byte array.  Since only IRPs
    	//are being sent in this test, some of the complexity has been removed; however,  although the numIrps is always 
    	//one for this test, the code has not been changed to get rid of the arrays.
 
        //ensure all values set up
        Assert.assertEquals(numIrps,transformType.length);
        Assert.assertEquals(numIrps,OUTLength.length);
        Assert.assertEquals(numIrps,OUTOffset.length);
        Assert.assertEquals(numIrps,acceptShortPacket.length);
        Assert.assertEquals(numIrps,verifyAcceptShortPacket.length);
        Assert.assertEquals(numIrps,OUTExpectedLength.length);
        Assert.assertEquals(numIrps,OUTexpectedException.length);
        Assert.assertEquals(numIrps,INLength.length);
        Assert.assertEquals(numIrps,INOffset.length);
        Assert.assertEquals(numIrps,INExpectedLength.length);
        Assert.assertEquals(numIrps,INexpectedException.length);
 
        Assert.assertNotNull("usbDevice is null, but should not be null.", usbDevice);
		Assert.assertEquals("For short packet test, number of IRPs should be 1.", 1, numIrps);
 
 
        /*
         * set up Pipes and add listeners
         */
        UsbPipe inPipe = null;
        UsbPipe outPipe = null;
 
        //we need two int values back from method call so we'll put them in the array
        int [] pipeListIndexes = new int[2];
        int inPipeArrayIndex = 0;
        int outPipeArrayIndex = 1;
 
        IOMethods.findINandOUTPipesForTest(usbPipeListGlobal, endpointmaxPacketSize, pipeListIndexes, inPipeArrayIndex, outPipeArrayIndex);
        inPipe = (UsbPipe) usbPipeListGlobal.get(pipeListIndexes[inPipeArrayIndex]);
        outPipe = (UsbPipe) usbPipeListGlobal.get(pipeListIndexes[outPipeArrayIndex]);
        IOMethods.verifyThePipes(inPipe, outPipe, endpointmaxPacketSize);
 
        inPipe.addUsbPipeListener(inPipeListener);
        outPipe.addUsbPipeListener(outPipeListener);
 
        IOMethods.openPipe(inPipe);
        IOMethods.openPipe(outPipe);
 
  
 
                //define buffers to be used in IRPs or sent as byte[]
                byte[] aggregateOUTbuffer = null;
                byte[] aggregateINbuffer = null;
 
 
                    //for byte array and single IRP, the OUT and IN buffers are the length specified by the tests
                    aggregateOUTbuffer = new byte[OUTLength[0]];
                    aggregateINbuffer = new byte[INLength[0]];
     
                  
                        printDebug("RoundTripTestShortPacket -- IRP " + transmitListStrings[syncOrAsync] +" " +  endpointTypeStrings[endpointType]);
                   
               
 
            
 
 
                /*
                 * Create the OUT and IN IRPs or byte arrrays
                 */
                List transmitBuffers = new ArrayList();
                List listOfOUTIrps = new ArrayList();
                List listOfINIrps = new ArrayList();
 
                for ( int k = 0 ; k < numIrps; k++ )
                {
                    //create transmit buffer for OUT and IN IRPs
                    TransmitBuffer currentTransmitBuffer = new TransmitBuffer(transformType[k], OUTLength[k]);
                    transmitBuffers.add(currentTransmitBuffer);
 
 
                    //create OUT IRP
                    UsbIrp currentOUTIrp = outPipe.createUsbIrp();
                    listOfOUTIrps.add(currentOUTIrp);
 
                    //set data in OUT IRP
                    currentOUTIrp.setData(aggregateOUTbuffer, OUTOffset[k], OUTLength[k]);
					currentOUTIrp.setAcceptShortPacket(acceptShortPacket[k]);
 
                    //OUT IRP is ready to go!
 
 
                    if ( endpointType == UsbConst.ENDPOINT_TYPE_ISOCHRONOUS )
                    {
                        /*
                        * For isochronous transfers, all IN Irps will have an offset of zero and
                        * the same buffer length.
                        */
                        //get the longest required buffer length for all of the IRPs. All of the IRPs in the list
                        //have the same length buffer.
                        int standardISOINBufferLength = INLength[0];
                        for ( int l = 1; l<numIrps; l++ )
                        {
                            if ( INLength[l]> standardISOINBufferLength )
                            {
                                standardISOINBufferLength = INLength[l];
                            }
                        }
                        //now that the largest has been found, set the length for each in IRP to the new length
                        for ( int l = 0; l<numIrps; l++ )
                        {
                            INLength[l] = standardISOINBufferLength;
                        }
                        int standardISOINBufferOffset = 0;
 
                        //int numCopies = 1;//there will be numCopies * numIrps in the list of ISO In Irps
                        int totalNumCopies = 40;//there will be numCopies * numIrps in the list of ISO In Irps
                        for ( int indexOfCurrentCopy=0; indexOfCurrentCopy < totalNumCopies; indexOfCurrentCopy++ )
                        {
                            //create IN IRP
                            UsbIrp currentINIrp = inPipe.createUsbIrp();
                            listOfINIrps.add(currentINIrp);
                            byte[] currentINbuffer = new byte[standardISOINBufferLength];
 
                            currentINIrp.setData(currentINbuffer, standardISOINBufferOffset, standardISOINBufferLength);
							currentINIrp.setAcceptShortPacket(acceptShortPacket[k]);
                        }
 
 
                    }
                    else
                    {
                        //create IN IRP
                        UsbIrp currentINIrp = inPipe.createUsbIrp();
                        listOfINIrps.add(currentINIrp);
                        /*
                         * set data in IN IRP -- note that no data is copied to IN byte[] before setting data in IRP.
                         * byte[] will be filled by IN operation.
                         */
                        currentINIrp.setData(aggregateINbuffer, INOffset[k], INLength[k]);
						currentINIrp.setAcceptShortPacket(acceptShortPacket[k]);
 
                    }
					
 
 
 
                }
 
 
                //copy individual transmitbuffers into single OUT  buffer
                for ( int k = 0 ; k < numIrps; k++ )
                {
                    TransmitBuffer currentTransmitBuffer = (TransmitBuffer) transmitBuffers.get(k);
 
                    System.arraycopy(currentTransmitBuffer.getOutBuffer(), 0, aggregateOUTbuffer, OUTOffset[k], OUTLength[k]);
                }
 
                //ensure all events are clear before sending IRPs
                inPipeEvents.clear();
                outPipeEvents.clear();
 
				//send both the OUT and IN IRPs
				sendOUTandIN( testType, transmitList[syncOrAsync],  inPipe, outPipe,
									 listOfINIrps, listOfOUTIrps); 

					printDebug("return from sendoutandin");
                    if ( endpointType == UsbConst.ENDPOINT_TYPE_ISOCHRONOUS )
                    {
                        clearOutTheINEventAndIrpListForISO(inPipeEvents, listOfINIrps);
                    }
 
                    //verify IRP data against expected values
                    for ( int k = 0; k<numIrps; k++ )
                    {
                        UsbIrp submittedOUTIRP = (UsbIrp) listOfOUTIrps.get(k);
  
 
                        //verify OUT IRP after successful transmit
                        VerifyIrpMethods.verifyUsbIrpAfterEvent(submittedOUTIRP,
                                                                (EventObject) outPipeEvents.get(k),
                                                                ((TransmitBuffer)transmitBuffers.get(k)).getOutBuffer(),
                                                                OUTExpectedLength[k],
                                                                OUTexpectedException[k],
                                                                acceptShortPacket[k],
                                                                verifyAcceptShortPacket[k],
                                                                OUTOffset[k],
                                                                OUTLength[k]
                                                               );
 
                    
                        UsbIrp submittedINIRP = (UsbIrp) listOfINIrps.get(k);
                        byte [] expectedINBuffer = ((TransmitBuffer)transmitBuffers.get(k)).getInBuffer();
                        
                        //if exception is expected, do not verify the In buffer
                        if (INexpectedException[k] != null)
                        {
							expectedINBuffer = null;
							
							//if we got a USB exception as expected, then clear the endpoint
							if (submittedINIRP.isUsbException())
							{							
							
							try{
		
							StandardRequest.clearFeature(usbDevice, UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT,
							UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT,inPipe.getUsbEndpoint().getUsbEndpointDescriptor().bEndpointAddress());

							printDebug("clear feature");
							}
							catch (Exception e)
							{
								Assert.fail("Exception clearing halt on enpoint after short packet exception.");
							}
							}
							else
											{
												printDebug("not clear feature");
											}
						}
				
                        //verify IN IRP after successful submit
                        VerifyIrpMethods.verifyUsbIrpAfterEvent(submittedINIRP,
                                                                (EventObject) inPipeEvents.get(k),
                                                                expectedINBuffer,
                                                                INExpectedLength[k],
                                                                INexpectedException[k],
                                                                acceptShortPacket[k],
                                                                verifyAcceptShortPacket[k],
                                                                INOffset[k],
                                                                INLength[k]
                                                               );
                    }
      
  
 
 
        IOMethods.closePipe(inPipe);
        IOMethods.closePipe(outPipe);
 
        inPipe.removeUsbPipeListener(inPipeListener);
        outPipe.removeUsbPipeListener(outPipeListener);
    };
 
 
    private void sendOUTandIN(byte testType, boolean SyncOrAsync,  UsbPipe inPipe, UsbPipe outPipe,
                                 List listOfINIrps, List listOfOUTIrps)
    {
        try
        {
            if ( endpointType == UsbConst.ENDPOINT_TYPE_ISOCHRONOUS )
            {
                //For isochronous, a list of IN IRPs must be async submitted as the IN for
                //the byte[], IRP, and IRP List tests.  Only the OUT, will be submitted
                // as a byte[], IRP, IRPList and in both synchronous and asynchronous modes.
                try
                {
                    printDebug("About to async Submit IN IRP List for ISOCHRONOUS");
                    inPipe.asyncSubmit(listOfINIrps);
                    printDebug("Return from async Submit IN IRP List for ISOCHRONOUS");
                }
                catch ( Exception uE )
                {
                    System.out.println("Exception on async submit.  Submission failed." + uE.getMessage());
                    Assert.fail("Exception on async submit.  Submission failed." + uE.getMessage());
                }
            }
 
  

                if ( SyncOrAsync == SYNC_SUBMIT )
                {
                    printDebug("About to sync Submit OUT IRP");
                    //for single IRP, send the first IRP in the IRP list
                    outPipe.syncSubmit((UsbIrp)listOfOUTIrps.get(0));
                    printDebug("Return from sync Submit OUT IRP");
                }
                else
                {
                    printDebug("About to async Submit OUT IRP");
                    //for single IRP, send the first IRP in the IRP list
                    outPipe.asyncSubmit((UsbIrp)listOfOUTIrps.get(0));
                    printDebug("Return from async Submit OUT IRP");
                    //wait for IRP to be complete; wait a max of 5000 ms
                    ((UsbIrp)listOfOUTIrps.get(0)).waitUntilComplete(5000);
                }
  
 
			
            //all IRPs should be complete when sync submit returns
            for ( int i=0; i< (listOfOUTIrps.size()); i++ )
            {
                Assert.assertTrue("isComplete() not true for IRP after syncSubmit returned",
                                  ((UsbIrp)listOfOUTIrps.get(i)).isComplete());
                Assert.assertFalse("isUsbException() is true for IRP after syncSubmit returned",
                                   ((UsbIrp)listOfOUTIrps.get(i)).isUsbException());
            }
			
 
            //isochronous IN IRP List was already submitted (see above)
            if ( endpointType != UsbConst.ENDPOINT_TYPE_ISOCHRONOUS )
            {
   
                    if ( SyncOrAsync == SYNC_SUBMIT )
                    {
                        printDebug("About to sync Submit IN IRP");
                        //for single IRP, send the first IRP in the IRP list
                        inPipe.syncSubmit((UsbIrp)listOfINIrps.get(0));
                        printDebug("Return from sync Submit IN IRP");
                    }
                    else
                    {
                        printDebug("About to async Submit IN IRP");
                        //for single IRP, send the first IRP in the IRP list
                        inPipe.asyncSubmit((UsbIrp)listOfINIrps.get(0));
                        printDebug("Return from async Submit IN IRP");
                        //wait for IRP to be complete; wait a max of 5000 ms
                        ((UsbIrp)listOfINIrps.get(0)).waitUntilComplete(5000);
						printDebug("Finished waiting on async in irp--timeout");
						((UsbIrp)listOfINIrps.get(0)).waitUntilComplete();
                        printDebug("Finished waiting on async in irp");
                    }
  
                }
 
		
                //all IRPs should be complete when sync submit returns
                for ( int i=0; i< (listOfINIrps.size()); i++ )
                {
                    Assert.assertTrue("isComplete() not true for IRP after syncSubmit returned",
                                      ((UsbIrp)listOfINIrps.get(i)).isComplete());
                    //Assert.assertFalse("isUsbException() is true for IRP after syncSubmit returned",
                    //                   ((UsbIrp)listOfINIrps.get(i)).isUsbException());
                }
		
        }
 
        catch ( UsbException uE )
        {
            
           //exceptions are expected in this test
        }
        catch ( UsbDisconnectedException uDE )                                                // @P1A
        {                                                                                     // @P1A
            Assert.fail ("A connected device should't throw the UsbDisconnectedException!");  // @P1A
        }                                                                                     // @P1A
 
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
            //first wait for all OUT events
            for ( int i = 0; i < 400; i++ )
            {
 
                if ( outPipeEvents.size() == listOfOUTIrps.size() )
                {
                    //System.out.println("Data event took less than " + ((i+1) * 20 ) +" milliseconds");
                    break;
                }
 
 
                Thread.sleep( 5 ); //wait 5 ms before checkin for event
            }
		
            //now wait for all IN events
            for ( int i = 0; i < 400; i++ )
            {
                if ( inPipeEvents.size() == listOfINIrps.size() )
                {
                    //System.out.println("Data event took less than " + ((i+1) * 20 ) +" milliseconds");
                    break;
                }
 
                Thread.sleep( 5 ); //wait 5 ms before checkin for event
 
            }
			
        }
        catch ( InterruptedException e )
        {
            Assert.fail("Sleep was interrupted");
           
        }
        finally
        {
 
            Assert.assertEquals("Did not receive all expected IN pipe events after sleep.", listOfINIrps.size(), inPipeEvents.size());
 
            Assert.assertEquals("Did not receive all expected OUT pipe event after sleep.", listOfOUTIrps.size(), outPipeEvents.size());
 
        }
		
    };
    private void clearOutTheINEventAndIrpListForISO(List listOfPipeEvents, List listOfIrps)
    {
        //all of the IRPs and events in which we are interested SHOULD have a non zero length
        //amount of data returned.
        //Once the zero length data IRPs and events are removed, only the IRPs and events that received the
        //expected OUT data should be left in the lists
        printDebug("Total pipe events for IN ISO is " + listOfPipeEvents.size());
 
        for ( int i=(listOfPipeEvents.size()-1); i>=0; i-- )
        {
            //all of the IRPs in which we are interested SHOULD have a non zero length
            //amount of data returned.
            //Once the zero length data IRP events are removed, only the IRP events that received the
            //expected OUT data should be left in the list
 
            Assert.assertTrue("There should be no error events in the list.",((UsbPipeEvent) listOfPipeEvents.get(i)).getClass() == UsbPipeDataEvent.class);
            if ( ((UsbPipeDataEvent) listOfPipeEvents.get(i)).getUsbIrp().getActualLength() == 0 )
            {
                listOfPipeEvents.remove(i);
                Assert.assertEquals("Original Irps and pipe event Irps should be zero actual length in the same position in the list.",0,((UsbIrp) listOfIrps.get(i)).getActualLength());
                listOfIrps.remove(i);
            }
            else
            {
                Assert.assertEquals("Original Irps and pipe event Irps actual length should be the same at the same position in the list.",
                                    ((UsbPipeDataEvent) listOfPipeEvents.get(i)).getUsbIrp().getActualLength(),
                                    ((UsbIrp) listOfIrps.get(i)).getActualLength());
                printDebug("Non-zero actual length pipe event found at index " + i);
            }
        }
        Assert.assertFalse("None of the Isochronous IN IRPs received any data",0 == listOfPipeEvents.size());
    };
 
 
 
 
    //-------------------------------------------------------------------------
    // Instance variables
    //
 
    private UsbPipeListener inPipeListener = new UsbPipeListener()
    {
        public void dataEventOccurred(UsbPipeDataEvent updE)
        {
            Assert.assertNotNull(updE);
            inPipeEvents.add(updE);
			printDebug("IN Length:  " + updE.getUsbIrp().getLength());
            printDebug("IN actualLength:  " + updE.getUsbIrp().getActualLength());
			printDebug("IN acceptShortPackets:  " + updE.getUsbIrp().getAcceptShortPacket());
            printDebug("IN data event.");
        }
 
        public void errorEventOccurred(UsbPipeErrorEvent upeE)
        {
            Assert.assertNotNull(upeE);
            inPipeEvents.add(upeE);
			printDebug("IN error event.");
           
        }
    };
 
    private UsbPipeListener outPipeListener = new UsbPipeListener()
    {
        public void dataEventOccurred(UsbPipeDataEvent updE)
        {
            Assert.assertNotNull(updE);
            outPipeEvents.add(updE);
			printDebug("OUT actualLength:  " + updE.getUsbIrp().getActualLength());
			printDebug("OUT data event.");
        }
 
        public void errorEventOccurred(UsbPipeErrorEvent upeE)
        {
            Assert.assertNotNull(upeE);
            //outPipeEvents.add(upeE);
			printDebug("OUT error event.");
			upeE.getUsbException().printStackTrace();
            Assert.fail("No OUT pipe error events expected during this test.  Exception is " + upeE.getUsbException().getMessage());
        }
    };
 
    /**
     * Constructor
     */
    public IOShortPacketTest()
    {
        super();
    };
 
    protected IOShortPacketTest(UsbDevice newUsbDevice, List newUsbPipeList, byte newEndpointType, byte newTestType)
    {
        usbPipeListGlobal = newUsbPipeList;
        usbDevice = newUsbDevice;
        endpointType = newEndpointType;
        testType = newTestType;
    };
 
    private List inPipeEvents = new ArrayList();
    private List outPipeEvents = new ArrayList();
 
    private List usbPipeListGlobal = new ArrayList();
 
    private byte endpointType;
    private static final int MAX_SIZE_IRP_BUFFER = 750;
 
 
    protected static final byte TRANSFORM_TYPE_PASSTHROUGH = (byte)0x01;
    protected static final byte TRANSFORM_TYPE_INVERT_BITS = (byte)0x02;
    protected static final byte TRANSFORM_TYPE_INVERT_ALTERNATE_BITS = (byte)0x03;
 
    private UsbDevice usbDevice;
    protected static int totalIterations = 10;
    //private int totalIterations = 1;
    private static final boolean SYNC_SUBMIT = true;
    private static final boolean ASYNC_SUBMIT = false;
    protected static final boolean [] transmitList= {SYNC_SUBMIT, ASYNC_SUBMIT};
    private static final String [] transmitListStrings = {"SYNC","ASYNC"};
    protected static final byte BYTE_ARRAY = 0;
    protected static final byte IRP = 1;
    protected static final byte IRPLIST = 2;
    private static final String [] endpointTypeStrings = {"CONTROL","ISOCHRONOUS", "BULK", "INTERRUPT"};
    byte testType;
 
    protected static void printDebug(String infoString)
    {
        if ( printDebug )
        {
            System.out.println(infoString);
        }
    }
    private static boolean printDebug = false;
   // private static boolean printDebug = true;
}
