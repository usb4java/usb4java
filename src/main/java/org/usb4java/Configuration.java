/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

import org.usb4java.descriptors.SimpleUsbConfigurationDescriptor;
import org.libusb4java.ConfigDescriptor;
import org.libusb4java.InterfaceDescriptor;
import org.libusb4java.LibUsb;

/**
 * usb4java implementation of JSR-80 UsbConfiguration.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
final class Configuration implements UsbConfiguration
{
    /** The configurationDescriptor. */
    private final UsbConfigurationDescriptor descriptor;
 
    /** The USB device this configuration belongs to. */
    private final AbstractDevice device;

    /**
     * The interfaces. This is a map from interface number to a map of alternate
     * settings which maps setting numbers to actual interfaces.
     */
    private final Map<Integer, Map<Integer, Interface>> interfaces =
        new HashMap<Integer, Map<Integer, Interface>>();

    /** This map contains the active USB interfaces. */
    private final Map<Integer, Interface> activeSettings =
        new HashMap<Integer, Interface>();

    /**
     * Constructor.
     * 
     * @param device
     *            The device this configuration belongs to.
     * @param descriptor
     *            The libusb configuration descriptor.
     */
    Configuration(final AbstractDevice device,
        final ConfigDescriptor descriptor)
    {
        this.device = device;
        this.descriptor = new SimpleUsbConfigurationDescriptor(descriptor);
        for (org.libusb4java.Interface iface: descriptor.iface())
        {
            for (InterfaceDescriptor ifaceDescriptor: iface.altsetting())
            {
                final int ifaceNumber =
                    ifaceDescriptor.bInterfaceNumber() & 0xff;
                final int settingNumber =
                    ifaceDescriptor.bAlternateSetting() & 0xff;

                Map<Integer, Interface> settings = this.interfaces
                    .get(ifaceNumber);
                if (settings == null)
                {
                    settings = new HashMap<Integer, Interface>();
                    this.interfaces.put(ifaceNumber, settings);
                }
                final Interface usbInterface =
                    new Interface(this, ifaceDescriptor);

                // If we have no active setting for current interface number
                // yet or the alternate setting number is 0 (which marks the
                // default alternate setting) then set current interface as
                // the active setting.
                if (!this.activeSettings.containsKey(ifaceNumber)
                    || ifaceDescriptor.bAlternateSetting() == 0)
                {
                    this.activeSettings.put(ifaceNumber, usbInterface);
                }

                // Add the interface to the settings list
                settings.put(settingNumber, usbInterface);
            }
        }
    }

    /**
     * Ensures that the device is connected.
     * 
     * @throws UsbDisconnectedException
     *             When device has been disconnected.
     */
    private void checkConnected()
    {
        this.device.checkConnected();
    }

    @Override
    public boolean isActive()
    {
        return this.device.getActiveUsbConfigurationNumber() == this.descriptor
            .bConfigurationValue();
    }

    @Override
    public List<Interface> getUsbInterfaces()
    {
        return Collections.unmodifiableList(new ArrayList<Interface>(
            this.activeSettings.values()));
    }

    /**
     * Returns the alternate settings for the specified interface.
     * 
     * @param number
     *            The interface number.
     * @return The alternate settings for the specified interface.
     */
    Map<Integer, Interface> getSettings(final byte number)
    {
        return this.interfaces.get(number & 0xff);
    }

    /**
     * Returns the number of alternate settings of the specified interface.
     * 
     * @param number
     *            The interface number.
     * @return The number of alternate settings.
     */
    int getNumSettings(final byte number)
    {
        return this.interfaces.get(number & 0xff).size();
    }

    @Override
    public Interface getUsbInterface(final byte number)
    {
        return this.activeSettings.get(number & 0xff);
    }

    /**
     * Sets the active USB interface setting.
     *
     * @param number
     *            THe interface number.
     * @param iface
     *            The interface setting to activate.
     * @throws UsbException
     *             When interface setting could not be set.
     */
    void setUsbInterface(final byte number, final Interface iface)
        throws UsbException
    {
        if (this.activeSettings.get(number & 0xff) != iface)
        {
            final int result = LibUsb.setInterfaceAltSetting(
                this.device.open(), number,
                iface.getUsbInterfaceDescriptor().bAlternateSetting());
            if (result < 0)
            {
                throw new LibUsbException(
                    "Unable to set alternate interface", result);
            }
            this.activeSettings.put(number & 0xff, iface);
        }
    }
    
    @Override
    public boolean containsUsbInterface(final byte number)
    {
        return this.activeSettings.containsKey(number & 0xff);
    }

    @Override
    public AbstractDevice getUsbDevice()
    {
        return this.device;
    }

    @Override
    public UsbConfigurationDescriptor getUsbConfigurationDescriptor()
    {
        return this.descriptor;
    }

    @Override
    public String getConfigurationString() throws UsbException,
        UnsupportedEncodingException
    {
        checkConnected();
        final byte iConfiguration = this.descriptor.iConfiguration();
        if (iConfiguration == 0) return null;
        return this.device.getString(iConfiguration);
    }

    @Override
    public String toString()
    {
        return String.format("USB configuration %02x",
            this.descriptor.bConfigurationValue());
    }
}
