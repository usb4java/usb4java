package javax.usb.tck;

/*
 * Copyright (c) IBM Corporation, 2004
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License.
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 *
 */

/*
 * Change Activity: See below.
 *
 * FLAG REASON   RELEASE  DATE   WHO      DESCRIPTION
 * ---- -------- -------- ------ -------  ------------------------------------
 * 0000 nnnnnnn           yymmdd          Initial Development
 * $P1           tck.rel1 040804 raulortz Support for UsbDisconnectedException
 */

import java.io.*;
import java.util.*;
import javax.usb.*;
import javax.usb.util.*;

/**
 * This is the Singleton class to find the Programmable Development board
 * usage: (FindProgrammableDevice.getInstance()).getProgrammableDevice()
 * @author Thanh Ngo
 */

@SuppressWarnings("all")
public class FindProgrammableDevice extends Object
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /** Ctor is protected because we are a Singleton */
    protected FindProgrammableDevice()
    {
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return the sole instance of this class (lazily) */
    public static FindProgrammableDevice getInstance()
    {
        if ( instance == null )
        {
            instance = new FindProgrammableDevice();
            instance.init();
        }
        return instance;
    }

    //-------------------------------------------------------------------------
    // Protected methods
    //

    /** Initializing the FindProgrammableDevice instance */
    protected void init()
    {
        // empty
    }


    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * find the first Programmable board with default vendorID = 0x0547 and 
     * default productID = 0x1002
     * return null if not found
     */
    public UsbDevice getProgrammableDevice()
    {
        programmableVendorId  = (short)0x0547;
        programmableProductId = (short)0x1002;
        return getProgrammableDevice(programmableVendorId, programmableProductId);
    }

    /**
         * find the first Programmable board with default vendorID = 0x0547 and 
         * default productID = 0xFF01
         * This device is used for topology test.
         * return null if not found
         */
    public UsbDevice getTopologyTestDevice()
    {
        programmableVendorId  = (short)0x0547;
        programmableProductId = (short)0xFF01;
        return getProgrammableDevice(programmableVendorId, programmableProductId);
    }

    /**
     * find the first Programmable board with vendor ID and product ID
     * return null if not found
     */
    public UsbDevice getProgrammableDevice(short vendorId, short productId)
    {
        return getUsbDevice(vendorId, productId);
    }

    /**
     * find the Programmable boards
     * return List of Programmable boards, null if not found
     */
    public List getProgrammableDevicesList()
    {
        short vendorId = getProgrammableVendorId();
        short productId = getProgrammableProductId();
        return getUsbDevicesList(vendorId, productId);
    }

    /**
     * find the usb boards with its manufacture string
     * return List of device if found
     * return null if not found.
     */
    public List getUsbDeviceWithProductString(String manufactureString)
    {
        return getUsbDevicesWithManufacturerString(getVirtualRootUsbHub(), manufactureString);
    }

    /**
     * find the first usb board with its vendorID and productID
     * return null if not found
     */
    public UsbDevice getUsbDevice(short vendorId, short productId)
    {

        virtualRootUsbHub = getVirtualRootUsbHub();
        usbDevices = null;
        // get all connected devices
        if ( debug )
        {
            usbDevices = getAllUsbDevices(virtualRootUsbHub);
            System.out.println("Found " + usbDevices.size() + " devices total.");
        }
        usbDevices = getUsbDevicesWithId(virtualRootUsbHub, vendorId, productId);
        if ( debug )
        {
            System.out.print("Found " + usbDevices.size() + " devices with");
            System.out.print(" vendor ID 0x" + UsbUtil.toHexString(vendorId));
            System.out.print(" product ID 0x" + UsbUtil.toHexString(productId));
            System.out.println("");
        }
        if ( usbDevices.isEmpty() )
        {
            if ( debug )
            {
                System.out.println("List Empty");
            }
            return null;
        }
        else
        {
            // return the first item 
            return(UsbDevice)usbDevices.get(0);
        }
    }


    /**
     * find the usb board with its vendorID and productID
     * return List of device if found
     * return null if not found.
     */
    public List getUsbDevicesList(short vendorId, short productId)
    {
        virtualRootUsbHub = getVirtualRootUsbHub();
        usbDevices = null;
        // get all connected devices
        if ( debug )
        {
            usbDevices = getAllUsbDevices(virtualRootUsbHub);
            System.out.println("Found " + usbDevices.size() + " devices total.");
        }
        usbDevices = getUsbDevicesWithId(virtualRootUsbHub, vendorId, productId);
        if ( debug )
        {
            System.out.print("Found " + usbDevices.size() + " devices with");
            System.out.print(" vendor ID 0x" + UsbUtil.toHexString(vendorId));
            System.out.print(" product ID 0x" + UsbUtil.toHexString(productId));
            System.out.println("");
        }
        if ( usbDevices.isEmpty() )
        {
            if ( debug )
            {
                System.out.println("List Empty");
            }
            return null;
        }

        // return the list 
        return usbDevices;
    }

    //-------------------------------------------------------------------------
    // Private methods
    //

    /**
     * Get the virtual root UsbHub.
     * @return virtual root UsbHub.
     */
    private static UsbHub getVirtualRootUsbHub()
    {
        UsbServices services = null;
        UsbHub virtualRootUsbHub = null;

        /* First we need to get the UsbServices.
         * This might throw either an UsbException or SecurityException.
         * A SecurityException means we're not allowed to access the USB bus,
         * while a UsbException indicates there is a problem either in
         * the javax.usb implementation or the OS USB support.
         */
        try
        {
            services = UsbHostManager.getUsbServices();
        }
        catch ( UsbException uE )
        {
            throw new RuntimeException("Error : " + uE.getMessage());
        }
        catch ( SecurityException sE )
        {
            throw new RuntimeException("Error : " + sE.getMessage());
        }

        /* Now we need to get the virtual root UsbHub,
         * everything is connected to it.  The Virtual Root UsbHub
         * doesn't actually correspond to any physical device, it's
         * strictly virtual.  Each of the devices connected to one of its
         * ports corresponds to a physical host controller located in
         * the system.  Those host controllers are (usually) located inside
         * the computer, e.g. as a PCI board, or a chip on the mainboard,
         * or a PCMCIA card.  The virtual root UsbHub aggregates all these
         * host controllers.
         *
         * This also may throw an UsbException or SecurityException.
         */
        try
        {
            virtualRootUsbHub = services.getRootUsbHub();
        }
        catch ( UsbException uE )
        {
            throw new RuntimeException("Error : " + uE.getMessage());
        }
        catch ( SecurityException sE )
        {
            throw new RuntimeException("Error : " + sE.getMessage());
        }

        return virtualRootUsbHub;
    }


    /**
     * This forms an inclusive list of all UsbDevices connected to this UsbDevice.
     * <p>
     * The list includes the provided device.  If the device is also a hub,
     * the list will include all devices connected to it, recursively.
     * @param usbDevice The UsbDevice to use.
     * @return An inclusive List of all connected UsbDevices.
     */
    private static List getAllUsbDevices(UsbDevice usbDevice)
    {
        List list = new ArrayList();

        list.add(usbDevice);

        /* this is just normal recursion.  Nothing special. */
        if ( usbDevice.isUsbHub() )
        {
            List devices = ((UsbHub)usbDevice).getAttachedUsbDevices();
            for ( int i=0; i<devices.size(); i++ )
                list.addAll(getAllUsbDevices((UsbDevice)devices.get(i)));
        }

        return list;
    }

    /**
     * Get a List of all devices that match the specified vendor and product id.
     * @param usbDevice The UsbDevice to check.
     * @param vendorId The vendor id to match.
     * @param productId The product id to match.
     * @param A List of any matching UsbDevice(s).
     */
    private static List getUsbDevicesWithId(UsbDevice usbDevice, short vendorId, short productId)
    {
        List list = new ArrayList();

        /* A device's descriptor is always available.  All descriptor
         * field names and types match exactly what is in the USB specification.
         * Note that Java does not have unsigned numbers, so if you are 
         * comparing 'magic' numbers to the fields, you need to handle it correctly.
         * For example if you were checking for Intel (vendor id 0x8086) devices,
         *   if (0x8086 == descriptor.idVendor())
         * will NOT work.  The 'magic' number 0x8086 is a positive integer, while
         * the _short_ vendor id 0x8086 is a negative number!  So you need to do either
         *   if ((short)0x8086 == descriptor.idVendor())
         * or
         *   if (0x8086 == UsbUtil.unsignedInt(descriptor.idVendor()))
         * or
         *   short intelVendorId = (short)0x8086;
         *   if (intelVendorId == descriptor.idVendor())
         * Note the last one, if you don't cast 0x8086 into a short,
         * the compiler will fail because there is a loss of precision;
         * you can't represent positive 0x8086 as a short; the max value
         * of a signed short is 0x7fff (see Short.MAX_VALUE).
         *
         * See javax.usb.util.UsbUtil.unsignedInt() for some more information.
         */
        if ( vendorId == usbDevice.getUsbDeviceDescriptor().idVendor() &&
             productId == usbDevice.getUsbDeviceDescriptor().idProduct() )
            list.add(usbDevice);

        /* this is just normal recursion.  Nothing special. */
        if ( usbDevice.isUsbHub() )
        {
            List devices = ((UsbHub)usbDevice).getAttachedUsbDevices();
            for ( int i=0; i<devices.size(); i++ )
                list.addAll(getUsbDevicesWithId((UsbDevice)devices.get(i), vendorId, productId));
        }

        return list;
    }


    /**
     * Get a List of all devices that match the specified manufacturer string.
     * @param usbDevice The UsbDevice to check.
     * @param manufacturerString The manufacturer string to match.
     * @return A List of any matching UsbDevice(s).
     */
    public static List getUsbDevicesWithManufacturerString(UsbDevice usbDevice, String manufacturerString)
    {
        List list = new ArrayList();

        /* Getting the product string may generate an UsbException,
         * as it may be necessary to actually communicate with the device
         * which could fail.
         */
        try
        {
            if ( manufacturerString.equals(usbDevice.getManufacturerString()) )
                list.add(usbDevice);
        }
        catch ( UsbException uE )
        {
            /* If there is an UsbException, we couldn't communicate
             * with the device for some reason.  The exact reason should be
             * indicated by the UsbException (we won't try to determine it here).
             * We could try to get the string again (possibly after trying to
             * figure out and/or fix the cause of the UsbException),
             * or we could ignore this device, or we could throw the UsbException,
             * or some other Exception, on up.  Since this is an example we'll
             * throw a RuntimeException on up (if we threw the UsbException,
             * we would have to declare that in this method definition).
             * This isn't a good thing to do in normal code.
             */
            throw new RuntimeException("Couldn't get manufacturer string : " + uE.toString());
        }
        catch ( UnsupportedEncodingException usE )
        {
            /* If there is an UnsupportedEncodingException, the
             * available Java libraries did not have an encoding that
             * could convert the 16-bit UNICODE byte[] to a String.
             * This is uncommon, and probably means that the string
             * is not in english _and_ the Java libraries are significantly
             * reduced, possibly for an embedded Java (J2ME?) implementation.
             * For this case, we'll ignore the device - the provided string
             * most likely does not match whatever the device's string is.
             * But, who knows, it might...remember this is just an example!
             */
        }
        catch ( UsbDisconnectedException uDE )                                                // @P1A
        {                                                                                     // @P1A
            System.out.println ("A connected device should't throw the UsbDisconnectedException!");// @P1A
        }                                                                                     // @P1A


        /* this is just normal recursion.  Nothing special. */
        if ( usbDevice.isUsbHub() )
        {
            List devices = ((UsbHub)usbDevice).getAttachedUsbDevices();
            for ( int i=0; i<devices.size(); i++ )
                list.addAll(getUsbDevicesWithManufacturerString((UsbDevice)devices.get(i), manufacturerString));
        }

        return list;
    }

    /**
     * Get a Programmable vendor ID.
     * @return A vendor ID.
     */
    public short getProgrammableVendorId()
    {
        return programmableVendorId;
    }

    /**
     * Get a Programmable product ID.
     * @return A product ID.
     */
    public short getProgrammableProductId()
    {
        return programmableProductId;
    }


    //-------------------------------------------------------------------------
    // Instance variables
    //
    private static final boolean debug = false;     // true = debug

    protected static FindProgrammableDevice instance = null;
    private UsbHub virtualRootUsbHub = getVirtualRootUsbHub();
    private List usbDevices = null;

    // Programmable Development Kit
    private short programmableVendorId;
    private short programmableProductId;
}