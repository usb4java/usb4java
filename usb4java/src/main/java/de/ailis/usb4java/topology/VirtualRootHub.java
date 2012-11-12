/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbPort;
import javax.usb.UsbStringDescriptor;
import javax.usb.event.UsbDeviceListener;
import javax.usb.util.DefaultUsbControlIrp;

import de.ailis.usb4java.descriptors.SimpleUsbDeviceDescriptor;
import de.ailis.usb4java.support.UsbDeviceListenerList;


/**
 * The virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class VirtualRootHub implements UsbHub,
    UsbPorts<LibUsbPort, LibUsbDevice>
{
    /** The manufacturer string. */
    private static final String MANUFACTURER = "de.ailis.usb4java";

    /** The manufacturer string. */
    private static final String PRODUCT = "Virtual Root Hub";

    /** The serial number. */
    private static final String SERIAL_NUMBER = "0.1.0";

    /** The configurations. */
    private final List<UsbConfiguration> configurations =
            new ArrayList<UsbConfiguration>(1);

    /** The device descriptor. */
    private final UsbDeviceDescriptor descriptor =
        new SimpleUsbDeviceDescriptor(
            UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE,
            UsbConst.DESCRIPTOR_TYPE_DEVICE,
            (short) 0x101,
            UsbConst.HUB_CLASSCODE,
            (byte) 0,
            (byte) 0,
            (byte) 8,
            (short) 0xffff,
            (short) 0xffff,
            (byte) 0,
            (byte) 1,
            (byte) 2,
            (byte) 3,
            (byte) 1);

    /** The device listeners. */
    private final UsbDeviceListenerList listeners = new UsbDeviceListenerList();

    /** The hub ports. */
    private final LibUsbPorts ports = new LibUsbPorts(this);


    /**
     * Constructor.
     */

    public VirtualRootHub()
    {
        this.configurations.add(new VirtualRootHubConfiguration(this));
    }


    /**
     * @see UsbHub#getParentUsbPort()
     */

    @Override
    public UsbPort getParentUsbPort()
    {
        return null;
    }


    /**
     * @see UsbHub#isUsbHub()
     */

    @Override
    public boolean isUsbHub()
    {
        return true;
    }


    /**
     * @see UsbHub#getManufacturerString()
     */

    @Override
    public String getManufacturerString()
    {
        return MANUFACTURER;
    }


    /**
     * @see UsbHub#getSerialNumberString()
     */

    @Override
    public String getSerialNumberString()
    {
        return SERIAL_NUMBER;
    }


    /**
     * @see UsbHub#getProductString()
     */

    @Override
    public String getProductString()
    {
        return PRODUCT;
    }


    /**
     * @see UsbHub#getSpeed()
     */

    @Override
    public Object getSpeed()
    {
        return UsbConst.DEVICE_SPEED_UNKNOWN;
    }


    /**
     * @see UsbHub#getUsbConfigurations()
     */

    @Override
    public List<UsbConfiguration> getUsbConfigurations()
    {
        return this.configurations;
    }


    /**
     * @see UsbHub#getUsbConfiguration(byte)
     */

    @Override
    public UsbConfiguration getUsbConfiguration(final byte number)
    {
        if (number != 1) return null;
        return this.configurations.get(0);
    }


    /**
     * @see UsbHub#containsUsbConfiguration(byte)
     */

    @Override
    public boolean containsUsbConfiguration(final byte number)
    {
        return number == 1;
    }


    /**
     * @see UsbHub#getActiveUsbConfigurationNumber()
     */

    @Override
    public byte getActiveUsbConfigurationNumber()
    {
        return 1;
    }


    /**
     * @see UsbHub#getActiveUsbConfiguration()
     */

    @Override
    public UsbConfiguration getActiveUsbConfiguration()
    {
        return this.configurations.get(0);
    }


    /**
     * @see UsbHub#isConfigured()
     */

    @Override
    public boolean isConfigured()
    {
        return true;
    }


    /**
     * @see UsbHub#getUsbDeviceDescriptor()
     */

    @Override
    public UsbDeviceDescriptor getUsbDeviceDescriptor()
    {
        return this.descriptor;
    }


    /**
     * @see UsbHub#getUsbStringDescriptor(byte)
     */

    @Override
    public UsbStringDescriptor getUsbStringDescriptor(final byte index)
        throws UsbException
    {
        throw new UsbException(
            "Can't get USB string descriptor from virtual device");
    }


    /**
     * @see UsbHub#getString(byte)
     */

    @Override
    public String getString(final byte index) throws UsbException
    {
        throw new UsbException("Can't get string from virtual device");
    }


    /**
     * @see UsbHub#syncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public void syncSubmit(final UsbControlIrp irp) throws UsbException
    {
        throw new UsbException("Can't syncSubmit on virtual device");
    }


    /**
     * @see UsbHub#asyncSubmit(javax.usb.UsbControlIrp)
     */

    @Override
    public void asyncSubmit(final UsbControlIrp irp) throws UsbException
    {
        throw new UsbException("Can't asyncSubmit on virtual device");
    }


    /**
     * @see UsbHub#syncSubmit(java.util.List)
     */

    @Override
    public void syncSubmit(final List list) throws UsbException
    {
        throw new UsbException("Can't syncSubmit on virtual device");
    }


    /**
     * @see UsbHub#asyncSubmit(java.util.List)
     */

    @Override
    public void asyncSubmit(final List list) throws UsbException
    {
        throw new UsbException("Can't asyncSubmit on virtual device");
    }


    /**
     * @see UsbHub#createUsbControlIrp(byte, byte, short, short)
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
     * @see UsbHub#addUsbDeviceListener(javax.usb.event.UsbDeviceListener)
     */

    @Override
    public void addUsbDeviceListener(final UsbDeviceListener listener)
    {
        this.listeners.add(listener);
    }


    /**
     * @see UsbHub#removeUsbDeviceListener(javax.usb.event.UsbDeviceListener)
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
    public List<LibUsbPort> getUsbPorts()
    {
        return this.ports.getUsbPorts();
    }


    /**
     * @see javax.usb.UsbHub#getUsbPort(byte)
     */

    @Override
    public LibUsbPort getUsbPort(final byte number)
    {
        return this.ports.getUsbPort(number);
    }


    /**
     * @see javax.usb.UsbHub#getAttachedUsbDevices()
     */

    @Override
    public List<LibUsbDevice> getAttachedUsbDevices()
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
     * @see UsbPorts#connectUsbDevice(javax.usb.UsbDevice)
     */

    @Override
    public void connectUsbDevice(final LibUsbDevice device)
    {
        this.ports.connectUsbDevice(device);
    }


    /**
     * @see UsbPorts#disconnectUsbDevice(javax.usb.UsbDevice)
     */

    @Override
    public void disconnectUsbDevice(final LibUsbDevice device)
    {
        this.ports.disconnectUsbDevice(device);
    }
}
