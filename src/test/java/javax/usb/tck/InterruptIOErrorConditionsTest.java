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

//import java.util.*;
import javax.usb.*;

import org.junit.runner.RunWith;

import de.ailis.usb4java.TCKRunner;
//import javax.usb.util.*;

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
@RunWith(TCKRunner.class)
public class InterruptIOErrorConditionsTest extends TestCase
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //


    public InterruptIOErrorConditionsTest()
    {
        thisTest = new IOErrorConditionsTest(UsbConst.ENDPOINT_TYPE_INTERRUPT);
    }

    //-------------------------------------------------------------------------
    // Protected overridden methods
    //

    protected void setUp() throws Exception {
        thisTest.setUp();

    }

    protected void tearDown() throws Exception {
        thisTest.tearDown();
    }



    /**
     * Null Data Buffer
     */
    public void testNullDataBufferInIrp()
    {
        thisTest.testNullDataBufferInIrp();
    }
    /**
     * Null Data Buffer
     */
    public void testNullDataBufferAsByteArray()
    {
        thisTest.testNullDataBufferAsByteArray();
    }

    /**
     * Action against a closed pipe
     */
    public void testActionAgainstClosePipeAbortAllSubmissions()
    {
        thisTest.testActionAgainstClosePipeAbortAllSubmissions();
    }
    /**
     * Action against a closed pipe
     */
    public void testActionAgainstClosePipeSubmit()
    {
        thisTest.testActionAgainstClosePipeSubmit();
    }

    /**
     * Close a pipe with pending operation
     */
    public void testClosePipePendingAction()
    {
        thisTest.testClosePipePendingAction();
    }

    // Moved method testOpenPipeOnInactiveAlternateSetting into the optional setInterface tests  @P1D7

    /**
     * Open a pipe on an unclaimed interface
     */
    public void testOpenPipeOnUnclaimedInterface()
    {
        thisTest.testOpenPipeOnUnclaimedInterface();
    }

    /**
     * Claim an already claimed interface
     */
    public void testClaimAnAlreadyClaimedInterface()
    {
        thisTest.testClaimAnAlreadyClaimedInterface();

    }


    private IOErrorConditionsTest thisTest = null;
}
