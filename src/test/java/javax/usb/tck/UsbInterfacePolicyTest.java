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
 * ---- -------- -------- ------ -------- ------------------------------------
 * 0000 nnnnnnn           yymmdd          Initial Development
 * $P1           tck.rel1 040804 raulortz Support for UsbDisconnectedException
 * $P2           tck.rel1 041222 raulortz Redesign TCK to create base and optional
 *                                        tests. Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
 */

import junit.framework.TestCase;
import java.util.*;

import javax.usb.*;

import org.junit.runner.RunWith;

import de.ailis.usb4java.test.TCKRunner;

/**
 * Usb Interface Policy Test
 * <p>
 * Goal: This test will use a new implementation of the UsbInterfacePolicy
 *               Interface to test the that the UsbInterfacePolicy is being used correctly.
 *
 * @author Dale Heeks
 */
@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class UsbInterfacePolicyTest extends TestCase
{

    public UsbInterfacePolicyTest(String name)
    {

        super(name);
    }

    protected void setUp() throws Exception {
        usbDevice = programmableDevice.getProgrammableDevice();
        assertTrue( "Find Programmable board Failed! Got a null instance", usbDevice != null );
        assertTrue("The usbDevice is not confuigured", usbDevice.isConfigured());
        usbConfiguration = usbDevice.getUsbConfiguration((byte)1);
        policy1 = new InterfacePolicyImp();
    }

    protected void tearDown() throws Exception {

    }


    /*ForceClaim policy test cases */
    /**
     * Test forceClaim policy with a false return
     * <p>
     * This test case will set the ForceClaim policy
     * to false and then call claim to ensure that the
     * ForceClaim policy method is invoked inside
     * the UsbInterfacePolicyImp class
     *<p>
     * <strong>NOTE:</strong> This will not check if the claim isn't forced
     * because some implemetations
     * will not be able to do a force claim and some will,
     *
     */

    public void testIfaceForceClaimFalse()
    {


        policy1.setForceClaimPolicy(false);

        assertFalse("Force Claim tag isn't reset, this is probably a TCK defect",
                    policy1.getForceClaimTag() );

        assertTrue("Can't claim Interface, this is probably a defect with your implementation",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
                                                                                              // @P2D4
        //reset the claim tag
        policy1.setForceClaimTag(false);

        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }

    /**
     * Test forceClaim policy with a true return
     * <p>
     * This test case will set the ForceClaim policy
     * to true and then call claim to ensure that the
     * ForceClaim policy method is invoked inside
     * the UsbInterfacePolicyImp class
     *<p>
     * <strong>NOTE:</strong> This will not check if the claim is forced
     * because some implemetations
     * will not be able to do a force claim and some will.
     *
     */
    public void testIfaceForceClaimTrue()
    {

        policy1.setForceClaimPolicy(true);

        assertFalse("Force Claim tag isn't reset, this is probably a TCK defect",
                    policy1.getForceClaimTag() );

        assertTrue("Can't claim Interface, this is probably a defect with your implementation",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
                                                                                              // @P2D4
        //Reset the force claim tag
        policy1.setForceClaimTag(false);

        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }


    //This method will take an interface and policy and call the appropriate
    //claim method, this method should only be used within this test
    private boolean claimIface(UsbInterface iface,
                               UsbInterfacePolicy ifacePolicy)
    {
        try
        {
            if ( ifacePolicy == null )
                iface.claim();
            else
                iface.claim(ifacePolicy);
        } catch ( UsbClaimException uce )
        {
            return false;
        } catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        } catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
        return true;
    }


    //This method takes a interface and key and make the appropriate
    //interface method call
    private int releaseIface(UsbInterface iface,
                             Object key)
    {
        try
        {
            iface.release();
        } catch ( UsbClaimException uce )
        {
            return notClaimed;
        } catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        } catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
        return released;
    }

    //Method that takes an interface and returns it's first pipe
    private UsbPipe getUsbPipe(UsbInterface iface)
    {
        List endpoints = iface.getUsbEndpoints();
        UsbEndpoint e1 = (UsbEndpoint)endpoints.get(0);
        return e1.getUsbPipe();
    }

    //Method that will call the appropriate open method given
    //a pipe and a key
    private int openPipe(UsbPipe pipe, Object key)
    {
        try
        {
            pipe.open();
        } catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        } catch ( UsbNotClaimedException unae )
        {
            fail("UsbNotActiveException: " + unae);
        } catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        return opened;
    }

    //method to close a pipe, this is for test case clean up
    private boolean closePipe(UsbPipe pipe)
    {
        try
        {
            pipe.close();
        } catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        } catch ( UsbNotActiveException unae )
        {
            fail("UsbNotOpenedException: " + unae);
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A
        return true;
    }


    private FindProgrammableDevice programmableDevice = FindProgrammableDevice.getInstance();
    private InterfacePolicyImp policy1 = null;
    private UsbConfiguration usbConfiguration = null;
    private UsbDevice usbDevice = null;
    private UsbInterface iface = null;
    private UsbEndpoint Endpoint;
    private static final boolean debug = true;
    private static final int released = 1111;
    private static final int opened = 2222;
    private static final int notClaimed = 4444;


    /*
     *************************************************************************
     ********************Test Interface Policy Implemetation class************
     *************************************************************************/
    /**
     * Copyright (c) 2004, International Business Machines Corporation.
     * All Rights Reserved.
     *
     * This software is provided and licensed under the terms and conditions
     * of the Common Public License:
     * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
     */

    /**
     * Name of class -- Usb Interface Policy test implementation
     * <p>
     * This class is an implementation of the UsbInterfacePolicy, this
     * should only be used for the Javax.usb tck.
     *
     * @author Dale Heeks
     */

    private class InterfacePolicyImp implements UsbInterfacePolicy
    {

        /**Constuctor
         *
         * <p>
         * Default constructor, sets everything in this policy to true,
         * which is also the same as the default policy
         *
         *
         */
        public InterfacePolicyImp ()
        {
            forceClaimPolicy = true;
            forceClaimTag = false;
        }

        /**Method that should be called from a claim using
         * the non default interface policy
         *
         *@param arg0 The current UsbInterface that is being claimed
         *
         *@return boolean Whether to allow an interface to attempt
         *a force claim
         */
        public boolean forceClaim(UsbInterface arg0)
        {
            forceClaimTag = true;
            return forceClaimPolicy;
        }

        /**Allows a user to dynamically set the force claim policy
         *
         * @param forceClaimIn The new force claim policy
         */
        public void setForceClaimPolicy(boolean forceClaimIn)
        {
            forceClaimPolicy = forceClaimIn;
        }

        /**Allows a user to dynamically set the force claim tag
         *
         * <p>
         * The force claim tag checks to see if the force claim policy has been called
         *
         * @param forceClaimTagIn The new force claim tag
         */
        public void setForceClaimTag(boolean forceClaimTagIn)
        {
            forceClaimTag = forceClaimTagIn;
        }

        /**Allows a user to check the force claim tag
         *
         * <p>
         * The force claim tag checks to see if the force claim policy has been called
         *
         * @return boolean The force claim tag
         */
        public boolean getForceClaimTag()
        {
            return forceClaimTag;
        }

        private boolean forceClaimPolicy;
        private boolean forceClaimTag;



    }
    /*************************************************************************/
}
