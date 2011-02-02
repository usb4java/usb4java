/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbPort;
import javax.usb.UsbStringDescriptor;
import javax.usb.event.UsbDeviceListener;
import javax.usb.util.DefaultUsbControlIrp;


/**
 * The virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class VirtualRootHub implements UsbHub, UsbPorts
{
    /** The manufacturer string. */
    private static final String MANUFACTURER = "de.ailis.usb4java";

    /** The manufacturer string. */
    private static final String PRODUCT = "Virtual Root Hub";

    /** The serial number. */
    private static final String SERIAL_NUMBER = "0.1.12-1";

    /** The configurations. */
    private final List<UsbConfiguration> configurations =
            new ArrayList<UsbConfiguration>(1);

    /** The device descriptor. */
    private final UsbDeviceDescriptor descriptor =
        new VirtualUsbDeviceDescriptor();

    /** The device listeners. */
    private final UsbDeviceListenerList listeners = new UsbDeviceListenerList();

    /** The hub ports. */
    private final UsbPortsImpl ports = new UsbPortsImpl(this);


    /**
     * Constructor.
     */

    VirtualRootHub()
    {
        this.configurations.add(new VirtualUsbConfiguration(this));
    }


    /**
     * @see UsbDevice#getParentUsbPort()
     */

    @Override
    public UsbPort getParentUsbPort()
    {
        return null;
    }


    /**
     * @see UsbDevice#isUsbHub()
     */

    @Override
    public boolean isUsbHub()
    {
        return true;
    }


    /**
     * @see UsbDevice#getManufacturerString()
     */

    @Override
    public String getManufacturerString()
    {
        return MANUFACTURER;
    }


    /**
     * @see UsbDevice#getSerialNumberString()
     */

    @Override
    public String getSerialNumberString()
    {
        return SERIAL_NUMBER;
    }


    /**
     * @see UsbDevice#getProductString()
     */

    @Override
    public String getProductString() throws UsbException
    {
        return PRODUCT;
    }


    /**
     * @see UsbDevice#getSpeed()
     */

    @Override
    public Object getSpeed()
    {
        return UsbConst.DEVICE_SPEED_UNKNOWN;
    }


    /**
     * @see UsbDevice#getUsbConfigurations()
     */

    @Override
    public List<UsbConfiguration> getUsbConfigurations()
    {
        return this.configurations;
    }


    /**
     * @see UsbDevice#getUsbConfiguration(byte)
     */

    @Override
    public UsbConfiguration getUsbConfiguration(final byte number)
    {
        if (number != 1) return null;
        return this.configurations.get(0);
    }


    /**
     * @see UsbDevice#containsUsbConfiguration(byte)
     */

    @Override
    public boolean containsUsbConfiguration(final byte number)
    {
        return number == 1;
    }


    /**
     * @see UsbDevice#getActiveUsbConfigurationNumber()
     */

    @Override
    public byte getActiveUsbConfigurationNumber()
    {
        return 1;
    }


    /**
     * @see UsbDevice#getActiveUsbConfiguration()
     */

    @Override
    public UsbConfiguration getActiveUsbConfiguration()
    {
        return this.configurations.get(0);
    }


    /**
     * @see UsbDevice#isConfigured()
     */

    @Override
    public boolean isConfigured()
    {
        return true;
    }


    /**
     * @see UsbDevice#getUsbDeviceDescriptor()
     */

    @Override
    public UsbDeviceDescriptor getUsbDeviceDescriptor()
    {
        return this.descriptor;
    }


    /**
     * @see UsbDevice#getUsbStringDescriptor(byte)
     */

    @Override
    public UsbStringDescriptor getUsbStringDescriptor(final byte index)
        throws UsbException
    {
        throw new UsbException(
            "Can't get USB string descriptor from virtual device");
    }


    /**
     * @see UsbDevice#getString(byte)
     */

    @Override
    public String getString(final byte index) throws UsbException
    {
        throw new UsbException("Can't get string from virtual device");
    }


    /**
     * @see UsbDevice#syncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public void syncSubmit(final UsbControlIrp irp) throws UsbException
    {
        throw new UsbException("Can't syncSubmit on virtual device");
    }


    /**
     * @see UsbDevice#asyncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public void asyncSubmit(final UsbControlIrp irp) throws UsbException
    {
        throw new UsbException("Can't asyncSubmit on virtual device");
    }


    /**
     * @see UsbDevice#syncSubmit(java.util.List)
     */

    @Override
    public void syncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException
    {
        throw new UsbException("Can't syncSubmit on virtual device");
    }


    /**
     * @see UsbDevice#asyncSubmit(java.util.List)
     */

    @Override
    public void asyncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException
    {
        throw new UsbException("Can't asyncSubmit on virtual device");
    }


    /**
     * @see UsbDevice#createUsbControlIrp(byte, byte, short, short)
     */

    @Override
    public UsbControlIrp createUsbControlIrp(final byte bmRequestType,
        final byte bRequest,
        final short wValue, final short wIndex)
    {
        return new DefaultUsbControlIrp(bmRequestType, bRequest, wValue,
            wIndex);
    }


    /**
     * @see UsbDevice#addUsbDeviceListener(javax.usb.event.UsbDeviceListener)
     */

    @Override
    public void addUsbDeviceListener(final UsbDeviceListener listener)
    {
        this.listeners.add(listener);
    }


    /**
     * @see UsbDevice#removeUsbDeviceListener(javax.usb.event.UsbDeviceListener)
     */

    @Override
    public void removeUsbDeviceListener(final UsbDeviceListener listener)
    {
        this.listeners.remove(listener);
    }


    /**
     * @see javax.usb.UsbHub#getNumberOfPorts()
     */

    @Override
    public byte getNumberOfPorts()
    {
        return this.ports.getNumberOfPorts();
    }


    /**
     * @see javax.usb.UsbHub#getUsbPorts()
     */

    @Override
    public List<UsbPort> getUsbPorts()
    {
        return this.ports.getUsbPorts();
    }


    /**
     * @see javax.usb.UsbHub#getUsbPort(byte)
     */

    @Override
    public UsbPort getUsbPort(final byte number)
    {
        return this.ports.getUsbPort(number);
    }


    /**
     * @see javax.usb.UsbHub#getAttachedUsbDevices()
     */

    @Override
    public List<UsbDevice> getAttachedUsbDevices()
    {
        return this.ports.getAttachedUsbDevices();
    }


    /**
     * @see javax.usb.UsbHub#isRootUsbHub()
     */

    @Override
    public boolean isRootUsbHub()
    {
        return true;
    }


    /**
     * @see UsbPorts#connectUsbDevice(UsbDevice)
     */

    @Override
    public void connectUsbDevice(final UsbDevice device)
    {
        this.ports.connectUsbDevice(device);
    }


    /**
     * @see UsbPorts#disconnectUsbDevice(UsbDevice)
     */

    @Override
    public void disconnectUsbDevice(final UsbDevice device)
    {
        this.ports.disconnectUsbDevice(device);
    }
}
