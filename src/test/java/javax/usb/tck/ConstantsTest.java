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


import javax.usb.*;

import org.junit.runner.RunWith;

import org.usb4java.test.TCKRunner;

import junit.framework.Assert;
import junit.framework.TestCase;




/**
 * ConstantsTest
 * This test verifies constants defined in the implementation
 * against that defined in the USB specification.
 * @author Vinitha Modepalle
 */
@SuppressWarnings("all")
@RunWith(TCKRunner.class)
public class ConstantsTest extends TestCase
{

    /*
    * Below is the declaration of all the constants;
    */

    byte TCK_H_CLASSCODE        = (byte)0x09;
    byte TCK_CONF_PWRD_MASK     = (byte)0x60;
    byte TCK_CONF_SELF_PWRD     = (byte)0x40;
    byte TCK_CONF_REMOTE_WAKEUP = (byte)0x20;
    byte TCK_ENDPT_NUM_MASK     = (byte)0x0f;
    byte TCK_ENDPT_DIR_MASK     = (byte)0x80;
    byte TCK_ENDPT_DIR_OUT      = (byte)0x00;
    byte TCK_ENDPT_DIR_IN       = (byte)0x80;
    byte TCK_ENDPT_TYPE_MASK    = (byte)0x03;
    byte TCK_ENDPT_TYPE_CTRL    = (byte)0x00;
    byte TCK_ENDPT_TYPE_ISOCHR  = (byte)0x01;
    byte TCK_ENDPT_TYPE_BULK    = (byte)0x02;
    byte TCK_ENDPT_TYPE_INTERPT = (byte)0x03;
    byte TCK_ENDPT_SYNC_TYPE_MASK=(byte)0x0c;
    byte TCK_ENDPT_SYNC_TYPE_NONE=(byte)0x00;
    byte TCK_ENDPT_SYNC_TYPE_ASYN=(byte)0x04;
    byte TCK_ENDPT_SYNC_TYPE_ADAP=(byte)0x08;
    byte TCK_ENDPT_SYNC_TYPE_SYNC=(byte)0x0c;
    byte TCK_ENDPT_USAGE_TYPE_MASK=(byte)0x30;
    byte TCK_ENDPT_USAGE_TYPE_DATA=(byte)0x00;
    byte TCK_ENDPT_USAGE_TYPE_FBACK=(byte)0x10;
    byte TCK_ENDPT_USAGE_TYPE_IMPL_FBACK_DATA=(byte)0x20;
    byte TCK_ENDPT_USAGE_TYPE_RESRVD=(byte)0x30;
    byte TCK_REQTYPE_DIR_MASK =(byte)0x80;
    byte TCK_REQTYPE_DIR_IN   =(byte)0x80;
    byte TCK_REQTYPE_DIR_OUT  =(byte)0x00;
    byte TCK_REQTYPE_TYPE_MASK=(byte)0x60;
    byte TCK_REQTYPE_TYPE_STD =(byte)0x00;
    byte TCK_REQTYPE_TYPE_CLASS=(byte)0x20;
    byte TCK_REQTYPE_TYPE_VENDOR=(byte)0x40;
    byte TCK_REQTYPE_TYPE_RESRVD=(byte)0x60;
    byte TCK_REQTYPE_RECEIP_MASK=(byte)0x1f;
    byte TCK_REQTYPE_RECEIP_DEV =(byte)0x00;
    byte TCK_REQTYPE_RECEIP_INTF=(byte)0x01;
    byte TCK_REQTYPE_RECEIP_ENDPT=(byte)0x02;
    byte TCK_REQTYPE_RECEIP_OTHER=(byte)0x03;
    byte TCK_REQ_GET_STATUS      =(byte)0x00;
    byte TCK_REQ_CLR_FEATURE     =(byte)0x01;
    byte TCK_REQ_SET_FEATURE     =(byte)0x03;
    byte TCK_REQ_SET_ADDRESS     =(byte)0x05;
    byte TCK_REQ_GET_DESCRIPTOR  =(byte)0x06;
    byte TCK_REQ_SET_DESCRIPTOR  =(byte)0x07;
    byte TCK_REQ_GET_CONFIG      =(byte)0x08;
    byte TCK_REQ_SET_CONFIG      =(byte)0x09;
    byte TCK_REQ_GET_INTERFACE   =(byte)0x0a;
    byte TCK_REQ_SET_INTERFACE   =(byte)0x0b;
    byte TCK_REQ_SYNC_FRAME      =(byte)0x0c;
    byte TCK_DESCR_TYPE_DEV      =(byte)0x01;
    byte TCK_DESC_TYPE_CONFIG    =(byte)0x02;
    byte TCK_DESC_TYPE_STRING    =(byte)0x03;
    byte TCK_DESC_TYPE_INTERFACE =(byte)0x04;
    byte TCK_DESC_TYPE_ENDPT     =(byte)0x05;
    byte TCK_DESC_MIN_LENGTH     =(byte)0x02;
    byte TCK_DESC_MIN_LGTH_DEV   =(byte)0x12;
    byte TCK_DESC_MIN_LGTH_CONFIG=(byte)0x09;
    byte TCK_DESC_MIN_LGTH_INTF  =(byte)0x09;
    byte TCK_DESC_MIN_LGTH_ENDPT =(byte)0x07;
    byte TCK_DESC_MIN_LGTH_STRING=(byte)0x02;
    byte TCK_FEATSELECT_ENDPT_HALT=(byte)0x00;
    byte TCK_FEATSELECT_DEV_REM_WAKEUP=(byte)0x01;
    String  TCK_JAVAXUSB_PROP_FILE = "javax.usb.properties";
    String TCK_JAVAXUSB_USBSERV_PROP = "javax.usb.services";
    /*
    * This test only tests and asserts for FAIL conditions.
    */

    public void testUsbConstants()
    {

        constCompare(UsbConst.HUB_CLASSCODE, TCK_H_CLASSCODE,
                     "UsbConst.HUB_CLASSCODE is not equal to TCK_H_CLASSCODE");
        constCompare(UsbConst.CONFIGURATION_POWERED_MASK, TCK_CONF_PWRD_MASK,
                     "UsbConst.CONFIGURATION_POWERED_MASK is not equal to TCK_CONF_PWRD_MASK");
        constCompare(UsbConst.CONFIGURATION_SELF_POWERED, TCK_CONF_SELF_PWRD,
                     "UsbConst.CONFIGURATION_SELF_POWERED is not equal to TCK_CONF_SELF_PWRD");
        constCompare(UsbConst.CONFIGURATION_REMOTE_WAKEUP, TCK_CONF_REMOTE_WAKEUP,
                     "UsbConst.CONFIGURATION_REMOTE_WAKEUP is not equal to TCK_CONF_REMOTE_WAKEUP");
        constCompare(UsbConst.ENDPOINT_NUMBER_MASK, TCK_ENDPT_NUM_MASK,
                     "UsbConst.ENDPOINT_NUMBER_MASK is not equal to TCK_ENDPT_NUM_MASK");
        constCompare(UsbConst.ENDPOINT_DIRECTION_MASK, TCK_ENDPT_DIR_MASK,
                     "UsbConst.ENDPOINT_DIRECTION_MASK is not equal to TCK_ENDPT_DIR_MASK");
        constCompare(UsbConst.ENDPOINT_DIRECTION_OUT, TCK_ENDPT_DIR_OUT,
                     "UsbConst.ENDPOINT_DIRECTION_OUT is not equal to TCK_ENDPT_DIR_OUT");
        constCompare(UsbConst.ENDPOINT_DIRECTION_IN, TCK_ENDPT_DIR_IN,
                     "UsbConst.ENDPOINT_DIRECTION_IN is not equal to TCK_ENDPT_DIR_IN");
        constCompare(UsbConst.ENDPOINT_TYPE_MASK, TCK_ENDPT_TYPE_MASK,
                     "UsbConst.ENDPOINT_TYPE_MASK is not equal  to TCK_ENDPT_TYPE_MASK");
        constCompare(UsbConst.ENDPOINT_TYPE_CONTROL, TCK_ENDPT_TYPE_CTRL,
                     "UsbConst.ENDPOINT_TYPE_CONTROL is not equal to TCK_ENDPT_TYPE_CTRL");
        constCompare(UsbConst.ENDPOINT_TYPE_ISOCHRONOUS, TCK_ENDPT_TYPE_ISOCHR,
                     "UsbConst.ENDPOINT_TYPE_ISOCHRONOUS is not equal to TCK_ENDPT_TYPE_ISOCHR");
        constCompare(UsbConst.ENDPOINT_TYPE_BULK, TCK_ENDPT_TYPE_BULK,
                     "UsbConst.ENDPOINT_TYPE_BULK is not equal to TCK_ENDPT_TYPE_BULK");
        constCompare(UsbConst.ENDPOINT_TYPE_INTERRUPT, TCK_ENDPT_TYPE_INTERPT,
                     "usbConst.ENDPOINT_TYPE_INTERRUPT is not equal to TCK_ENDPT_TYPE_INTERPT");
        constCompare(UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_MASK, TCK_ENDPT_SYNC_TYPE_MASK, "UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_MASK is not equal to TCK_ENDPT_SYNC_TYPE_MASK");
        constCompare(UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_NONE, TCK_ENDPT_SYNC_TYPE_NONE, "UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_NONE is not equal to TCK_ENDPT_SYNC_TYPE_NONE");
        constCompare(UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_ASYNCHRONOUS, TCK_ENDPT_SYNC_TYPE_ASYN, "UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_ASYNCHRONOUS is not equal to TCK_ENDPT_SYNC_TYPE_ASYN");
        constCompare(UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_ADAPTIVE, TCK_ENDPT_SYNC_TYPE_ADAP, "UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_ADAPTIVE is not equal to TCK_ENDPOINT_SYNC_TYPE_ADAP");
        constCompare(UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_SYNCHRONOUS, TCK_ENDPT_SYNC_TYPE_SYNC, "UsbConst.ENDPOINT_SYNCHRONIZATION_TYPE_SYNCHRONOUS is not equal to TCK_ENDPT_SYNC_TYPE_SYNC");
        constCompare(UsbConst.ENDPOINT_USAGE_TYPE_MASK, TCK_ENDPT_USAGE_TYPE_MASK, "UsbConst.ENDPOINT_USAGE_TYPE_MASK is not equal to TCK_ENDPT_USAGE_TYPE_MASK");
        constCompare(UsbConst.ENDPOINT_USAGE_TYPE_DATA, TCK_ENDPT_USAGE_TYPE_DATA, "UsbConst.ENDPOINT_USAGE_TYPE_DATA is not equal to TCK_ENDPT_USAGE_TYPE_DATA");
        constCompare(UsbConst.ENDPOINT_USAGE_TYPE_FEEDBACK, TCK_ENDPT_USAGE_TYPE_FBACK, "UsbConst.ENDPOINT_USAGE_TYPE_FEEDBACK is not equal to TCK_ENDPT_USAGE_TYPE_FBACK");
        constCompare(UsbConst.ENDPOINT_USAGE_TYPE_IMPLICIT_FEEDBACK_DATA, TCK_ENDPT_USAGE_TYPE_IMPL_FBACK_DATA, "UsbConst.ENDPOINT_USAGE_TYPE_IMPLICIT_FEEDBACK_DATA is not equal to TCK_ENDPT_USAGE_TYPE_IMPL_FBACK_DATA");
        constCompare(UsbConst.ENDPOINT_USAGE_TYPE_RESERVED, TCK_ENDPT_USAGE_TYPE_RESRVD, "UsbConst.ENDPOINT_USAGE_TYPE_RESERVED is not equal to TCK_ENDPT_USAGE_TYPE_RESRVD");
        constCompare(UsbConst.REQUESTTYPE_DIRECTION_MASK, TCK_REQTYPE_DIR_MASK, "UsbConst.REQUESTTYPE_DIRECTION_MASK is not equal to TCK_REQTYPE_DIR_MASK");
        constCompare(UsbConst.REQUESTTYPE_DIRECTION_IN, TCK_REQTYPE_DIR_IN, "UsbConst.REQUESTTYPE_DIRECTION_IN is not equal to TCK_REQTYPE_DIR_IN");
        constCompare(UsbConst.REQUESTTYPE_DIRECTION_OUT, TCK_REQTYPE_DIR_OUT, "UsbConst.REQUESTTYPE_DIRECTION_OUT is not equal to TCK_REQTYPE_DIR_OUT");
        constCompare(UsbConst.REQUESTTYPE_TYPE_MASK, TCK_REQTYPE_TYPE_MASK, "UsbConst.REQUESTTYPE_TYPE_MASK is not equal to TCK_REQTYPE_TYPE_MASK");
        constCompare(UsbConst.REQUESTTYPE_TYPE_STANDARD, TCK_REQTYPE_TYPE_STD, "UsbConst.REQUESTYPE_TYPE_STANDARD is not equal to TCK_REQTYPE_TYPE_STD");
        constCompare(UsbConst.REQUESTTYPE_TYPE_CLASS, TCK_REQTYPE_TYPE_CLASS, "UsbConst.REQUESTTYPE_TYPE_CLASS is not equal to TCK_REQTYPE_TYPE_CLASS");          constCompare(UsbConst.REQUESTTYPE_TYPE_VENDOR, TCK_REQTYPE_TYPE_VENDOR, "UsbConst.REQUESTTYPE_TYPE_VENDOR is not equal to TCK_REQTYPE_TYPE_VENDOR");
        constCompare(UsbConst.REQUESTTYPE_TYPE_RESERVED, TCK_REQTYPE_TYPE_RESRVD, "UsbConst.REQUESTTYPE_TYPE_RESERVED is not equal to TCK_REQTYPE_TYPE_RESRVD");
        constCompare(UsbConst.REQUESTTYPE_RECIPIENT_MASK, TCK_REQTYPE_RECEIP_MASK, "UsbConst.REQUESTTYPE_RECIPIENT_MASK is not equal to TCK_REQTYPE_RECEIP_MASK");
        constCompare(UsbConst.REQUESTTYPE_RECIPIENT_DEVICE, TCK_REQTYPE_RECEIP_DEV, "UsbConst.REQUESTTYPE_RECIPIENT_DEVICE is not equal to TCK_REQTYPE_RECEIP_DEV");
        constCompare(UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE, TCK_REQTYPE_RECEIP_INTF, "UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE is not equal to TCK_REQTYPE_RECEIP_INTF");
        constCompare(UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT, TCK_REQTYPE_RECEIP_ENDPT, "UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT is not equal to TCK_REQTYPE_RECEIP_ENDPT");
        constCompare(UsbConst.REQUESTTYPE_RECIPIENT_OTHER, TCK_REQTYPE_RECEIP_OTHER, "UsbConst.REQUESTTYPE_RECIPIENT_OTHER is not equal to TCK_REQTYPE_RECEIP_OTHER");
        constCompare(UsbConst.REQUEST_GET_STATUS, TCK_REQ_GET_STATUS, "UsbConst.REQUEST_GET_STATUS is not equal to TCK_REQ_GET_STATUS");
        constCompare(UsbConst.REQUEST_CLEAR_FEATURE, TCK_REQ_CLR_FEATURE, "UsbConst.REQUEST_CLEAR_FEATURE is not equal to TCK_REQ_CLR_FEATURE");
        constCompare(UsbConst.REQUEST_SET_FEATURE, TCK_REQ_SET_FEATURE, "UsbConst.REQUEST_SET_FEATURE is not equal to TCK_REQ_SET_FEATURE");
        constCompare(UsbConst.REQUEST_SET_ADDRESS, TCK_REQ_SET_ADDRESS, "UsbConst.REQUEST_SET_ADDRESS is not equal to TCK_REQ_SET_ADDRESS");
        constCompare(UsbConst.REQUEST_GET_DESCRIPTOR, TCK_REQ_GET_DESCRIPTOR, "UsbConst.REQUEST_GET_DESCRIPTOR is not equal to TCK_REQ_GET_DESCRIPTOR");          constCompare(UsbConst.REQUEST_SET_DESCRIPTOR, TCK_REQ_SET_DESCRIPTOR, "UsbConst.REQUEST_SET_DESCRIPTOR is not equal to TCK_REQ_SET_DESCRIPTOR");          constCompare(UsbConst.REQUEST_GET_CONFIGURATION, TCK_REQ_GET_CONFIG, "UsbConst.REQUEST_GET_CONFIGURATION is not equal to TCK_REQ_GET_CONFIG");
        constCompare(UsbConst.REQUEST_SET_CONFIGURATION, TCK_REQ_SET_CONFIG, "UsbConst.REQUEST_SET_CONFIGURATION is not equal to TCK_REQ_SET_CONFIG");
        constCompare(UsbConst.REQUEST_GET_INTERFACE, TCK_REQ_GET_INTERFACE, "UsbConst.REQUEST_GET_INTERFACE is not equal to TCK_REQ_GET_INTERFACE");
        constCompare(UsbConst.REQUEST_SET_INTERFACE, TCK_REQ_SET_INTERFACE, "UsbConst.REQUEST_SET_INTERFACE is not equal to TCK_REQ_SET_INTERFACE");
        constCompare(UsbConst.REQUEST_SYNCH_FRAME, TCK_REQ_SYNC_FRAME, "UsbConst.REQUEST_SYNCH_FRAME is not equal to TCK_REQ_SYNC_FRAME");
        constCompare(UsbConst.DESCRIPTOR_TYPE_DEVICE, TCK_DESCR_TYPE_DEV, "UsbConst.DESCRIPTOR_TYPE_DEVICE is not equal to TCK_DESCR_TYPE_DEV");
        constCompare(UsbConst.DESCRIPTOR_TYPE_CONFIGURATION, TCK_DESC_TYPE_CONFIG, "UsbConst.DESCRIPTOR_TYPE_CONFIGURATION is not equal to TCK_DESC_TYPE_CONFIG");
        constCompare(UsbConst.DESCRIPTOR_TYPE_STRING, TCK_DESC_TYPE_STRING, "UsbConst.DESCRIPTOR_TYPE_STRING is not equal to TCK_DESC_TYPE_STRING");
        constCompare(UsbConst.DESCRIPTOR_TYPE_INTERFACE, TCK_DESC_TYPE_INTERFACE, "UsbConst.DESCRIPTOR_TYPE_INTERFACE is not equal to TCK_DESC_TYPE_INTERFACE");
        constCompare(UsbConst.DESCRIPTOR_TYPE_ENDPOINT, TCK_DESC_TYPE_ENDPT, "UsbConst.DESCRIPTOR_TYPE_ENDPOINT is not equal to TCK_DESC_TYPE_ENDPT");
        constCompare(UsbConst.DESCRIPTOR_MIN_LENGTH, TCK_DESC_MIN_LENGTH, "UsbConst.DESCRIPTION_MIN_LENGTH is not equal to TCK_DESC_MIN_LENGTH");
        constCompare(UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE, TCK_DESC_MIN_LGTH_DEV, "UsbConst.DESCRIPTION_MIN_LENGTH_DEVICE is not equal to TCK_DESC_MIN_LGTH_DEV");
        constCompare(UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION, TCK_DESC_MIN_LGTH_CONFIG, "UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION is not equal to TCK_DESC_MIN_LGTH_CONFIG");
        constCompare(UsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE, TCK_DESC_MIN_LGTH_INTF, "UsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE is not equal to TCK_DESC_MIN_LGTH_INTF");
        constCompare(UsbConst.DESCRIPTOR_MIN_LENGTH_ENDPOINT, TCK_DESC_MIN_LGTH_ENDPT, "UsbConst.DESCRIPTOR_MIN_LENGTH_ENDPOINT is not equal to TCK_DESC_MIN_LGTH_ENDPT");
        constCompare(UsbConst.DESCRIPTOR_MIN_LENGTH_STRING, TCK_DESC_MIN_LGTH_STRING, "UsbConst.DESCRIPTOR_MIN_LENGTH_STRING is not equal to TCK_DESC_MIN_LGTH_STRING");
        constCompare(UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT, TCK_FEATSELECT_ENDPT_HALT, "UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT is not equal to TCK_FEATSELECT_ENDPT_HALT");
        constCompare(UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP, TCK_FEATSELECT_DEV_REM_WAKEUP, "UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP is not equal to TCK_FEATSELECT_DEV_REM_WAKEUP");
        strCompare(UsbHostManager.JAVAX_USB_PROPERTIES_FILE, TCK_JAVAXUSB_PROP_FILE,
                   "UsbHostManager.JAVAX_USB_PROPERTIES_FILE is not equal to TCK_JAVAXUSB_PROP_FILE");
        strCompare(UsbHostManager.JAVAX_USB_USBSERVICES_PROPERTY, TCK_JAVAXUSB_USBSERV_PROP,
                   "UsbHostManager.JAVAX_USB_USBSERVICES_PROPERTY is not equal to TCK_JAVAXUSB_USBSERV_PROP");

    }

    /*
    * Below is common code which compares the values of the implemented
    * constants with those defined in the specification.
    */

    private void constCompare(byte first, byte second, String text)
    {
        Assert.assertEquals(text, first, second);
    }

    private void strCompare(String first, String second, String text)
    {
        Assert.assertEquals(text, first, second);
    }

}

