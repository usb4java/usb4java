/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbPort;

import de.ailis.usb4java.jni.USB_Device;


/**
 * UsbHub implementation of usb4java.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class UsbHubImpl extends UsbDeviceImpl implements UsbHub
{
    /**
     * Constructor.
     *
     * @param device
     *            The low-level USB device.
     */

    public UsbHubImpl(final USB_Device device)
    {
        super(device);

        // TODO Implement me!
    }


    /**
     * @see javax.usb.UsbHub#getNumberOfPorts()
     */

    @Override
    public byte getNumberOfPorts()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbHub#getUsbPorts()
     */

    @Override
    public List<UsbPort> getUsbPorts()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbHub#getUsbPort(byte)
     */

    @Override
    public UsbPort getUsbPort(final byte number)
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbHub#getAttachedUsbDevices()
     */

    @Override
    public List<UsbDevice> getAttachedUsbDevices()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.usb.UsbHub#isRootUsbHub()
     */

    @Override
    public boolean isRootUsbHub()
    {
        // TODO Implement me!
        throw new UnsupportedOperationException();
    }
}
