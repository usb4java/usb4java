package javax.usb.tck;

/**
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
 * ---- -------- -------- ------ -------- ------------------------------------
 * 0000 nnnnnnn           yymmdd          Initial Development
 * $P1           tck.rel1 041222 raulortz Redesign TCK to create base and optional
 *                                        tests. Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
 */

import java.util.*;
import javax.usb.*;
import javax.usb.util.*;

import org.junit.runner.RunWith;

import de.ailis.usb4java.test.TCKRunner;

/**
 * FindControlIODevice
 * <p>
 * This Singleton class finds the first device with a UsbEndpoint with the type
 * ENDPOINT_TYPE_CONTROL and returns the device.  Used by the
 * ControlIODevice class.
 * usage: (FindControlIODevice.getInstance()).getControlIODevice()
 * @author Joshua Lowry
 */

@SuppressWarnings("all")
public class FindControlIODevice extends Object
{
    //--------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * The constructor is protected as this is a singleton class.
     */
    protected FindControlIODevice()
    {
    }

    //--------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Creates and returns the sole instance of the FindControlIODevice class.
     * @return FindControlIODevice
     */
    public static FindControlIODevice getInstance()
    {
        if ( instance == null )
        {
            instance = new FindControlIODevice();
            instance.init();
        }
        return instance;
    }

    //--------------------------------------------------------------------------
    // Protected methods
    //

    /** Initializes the FindControlIODevice instance */
    protected void init()
    {
        // empty
    }


    //--------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Finds the first device with a Control Pipe other than the default Control
     * Pipe and returns it.  Returns null if no such device is found.
     * @return UsbDevice
     */
    public UsbDevice getControlIODevice()
    {
        virtualRootUsbHub = getVirtualRootUsbHub();
        usbDevices = null;
        // get all connected devices
        if ( debug )
        {
            usbDevices = getAllUsbDevices(virtualRootUsbHub);
            System.out.println("Found " + usbDevices.size() + " devices total.");
        }
        usbDevices = getUsbDevicesWithCtrlEndpoint(virtualRootUsbHub);
        if ( usbDevices.isEmpty() )
        {
            if ( debug )
            {
                System.out.println("List Empty");
            }
            return null;
        } else
        {
            // return the first item
            return(UsbDevice)usbDevices.get(0);
        }
    }

    /**
     * Finds a list of devices with a Control Pipe other than the default Cotrol
     * Pipe and returns it.  Returns null if no devices are found.
     * @return UsbDevice
     */
    public List getControlIODevicesList()
    {
        virtualRootUsbHub = getVirtualRootUsbHub();
        usbDevices = null;
        // get all connected devices
        if ( debug )
        {
            usbDevices = getAllUsbDevices(virtualRootUsbHub);
            System.out.println("Found " + usbDevices.size() + " devices total.");
        }
        usbDevices = getUsbDevicesWithCtrlEndpoint(virtualRootUsbHub);
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

    //--------------------------------------------------------------------------
    // Private methods
    //

    /*
     * Get the virtual root UsbHub.
     * @return
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
        } catch ( UsbException uE )
        {
            throw new RuntimeException("Error : " + uE.getMessage());
        } catch ( SecurityException sE )
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
        } catch ( UsbException uE )
        {
            throw new RuntimeException("Error : " + uE.getMessage());
        } catch ( SecurityException sE )
        {
            throw new RuntimeException("Error : " + sE.getMessage());
        }

        return virtualRootUsbHub;
    }


    /*
     * This forms an inclusive list of all UsbDevices connected to this
     * UsbDevice.
     * <p>
     * The list includes the provided device.  If the device is also a hub,
     * the list will include all devices connected to it, recursively.
     * @param usbDevice The UsbDevice to use.
     * @return
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

    /*
     * Get a List of all devices that have a control pipe other than the default
     * control pipe
     * @param usbDevice The UsbDevice to check.
     * @return
     */
    private static List getUsbDevicesWithCtrlEndpoint(UsbDevice usbDevice)
    {
        List list = new ArrayList();
        boolean containsNonDfltCtrlPipe = false;

        if ( getControlIOEndpoint(usbDevice) != null )
            list.add(usbDevice);

        /* this is just normal recursion.  Nothing special. */
        if ( usbDevice.isUsbHub() )
        {
            List devices = ((UsbHub)usbDevice).getAttachedUsbDevices();
            for ( int i=0; i<devices.size(); i++ )
                list.addAll(getUsbDevicesWithCtrlEndpoint((UsbDevice)devices.get(i)));
        }

        return list;
    }

    /**
     * Gets the first UsbEndpoint of type ENDPOINT_TYPE_CONTROL that is not
     * UsbEndpoint0 on the first UsbDevice to have such an endpoint.
     * @param usbDevice The UsbDevice to check.
     * @return UsbEndpoint
     */
    public static UsbEndpoint getControlIOEndpoint(UsbDevice usbDevice)
    {
        UsbEndpoint usbCtrlEndpoint = null;

        /*
         * To find a control pipe other than the default control pipe
         * on a device, the search has to get all the device endpoints,
         * which can mean changing the active configurations.
         *
         * To get to the endpoints, on each configuration, all the interfaces
         * need to be examined.  For each interface, all alternate settings
         * need to be examined.  Then for each alternate setting, all the
         * endpoints need to be examined until an endpoint (other than
         * Endpoint0) of type ENDPOINT_TYPE_CONTROL is found,
         * or all endpoints have been examined and none are that type.
         */
        for ( int i=0; i<usbDevice.getUsbDeviceDescriptor().bNumConfigurations(); i++ )
        {
            UsbConfiguration usbCurrentConfig;
            UsbConfigurationDescriptor usbCurrentConfigDescriptor;

            usbCurrentConfig = usbDevice.getUsbConfiguration((byte) (i+1));
            if ( usbCurrentConfig == null) continue;                                          // @P1A
            usbCurrentConfigDescriptor = usbCurrentConfig.getUsbConfigurationDescriptor();
                                                                                              // @P1D11
            for ( int j=0; j<usbCurrentConfigDescriptor.bNumInterfaces(); j++ )
            {
                UsbInterface usbCurrentInterface;

                usbCurrentInterface = usbCurrentConfig.getUsbInterface((byte) j);
                for ( int k=0; k<usbCurrentInterface.getNumSettings(); k++ )
                {
                    usbCurrentInterface = usbCurrentInterface.getSetting((byte) k);
                    List usbEndpoints;

                    usbEndpoints = usbCurrentInterface.getUsbEndpoints();
                    for ( int l = 0; l<usbEndpoints.size(); l++ )
                    {
                        UsbEndpoint usbCurrentEndpoint;

                        usbCurrentEndpoint = (UsbEndpoint) usbEndpoints.get(l);
                        if ( usbCurrentEndpoint.getType() == UsbConst.ENDPOINT_TYPE_CONTROL &&
                             usbCurrentEndpoint.getUsbEndpointDescriptor().bEndpointAddress() != (byte) 0x00 )
                        {
                            usbCtrlEndpoint = usbCurrentEndpoint;
                            break;
                        }
                    }
                    if ( usbCtrlEndpoint != null )
                        break;
                }
                if ( usbCtrlEndpoint != null )
                    break;
            }
            if ( usbCtrlEndpoint != null )
                break;
        }
        return usbCtrlEndpoint;
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //
    private static final boolean debug = false;             // true = debug

    protected static FindControlIODevice instance = null;
    private UsbHub virtualRootUsbHub = getVirtualRootUsbHub();
    private List usbDevices = null;
}
