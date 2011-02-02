/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

import static de.ailis.usb4java.USB.usb_set_altinterface;
import static de.ailis.usb4java.USB.usb_strerror;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConfigurationDescriptor;
import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbInterface;

import de.ailis.usb4java.USBLock;
import de.ailis.usb4java.USB_Config_Descriptor;
import de.ailis.usb4java.USB_Interface;
import de.ailis.usb4java.USB_Interface_Descriptor;


/**
 * usb4java implementation of JSR80 UsbConfiguration.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class UsbConfigurationImpl implements UsbConfiguration
{
    /** The JSR 80 USB configuration descriptor. */
    private final UsbConfigurationDescriptor descriptor;

    /** The USB device. */
    private final AbstractDevice device;

    /**
     * The interfaces. This is a map from interface number to a map of
     * altsettings which maps setting numbers to actual interfaces.
     */
    private final Map<Integer, Map<Integer, UsbInterface>> interfaces =
            new HashMap<Integer, Map<Integer, UsbInterface>>();

    /** This map contains the active USB interfaces. */
    private final Map<Integer, UsbInterface> activeSettings = new HashMap<Integer, UsbInterface>();


    /**
     * Constructor.
     *
     * @param device
     *            The USB device.
     * @param lowLevelDescriptor
     *            The low-level USB configuration descriptor.
     */

    public UsbConfigurationImpl(final AbstractDevice device,
        final USB_Config_Descriptor lowLevelDescriptor)
    {
        this.device = device;
        this.descriptor = new UsbConfigurationDescriptorImpl(
            lowLevelDescriptor);

        // Build interfaces
        for (final USB_Interface iface : lowLevelDescriptor.iface())
        {
            for (final USB_Interface_Descriptor desc : iface.altsetting())
            {
                final int ifaceNumber = desc.bInterfaceNumber();
                final int settingNumber = desc.bAlternateSetting();

                Map<Integer, UsbInterface> settings = this.interfaces
                        .get(ifaceNumber);
                if (settings == null)
                {
                    settings = new HashMap<Integer, UsbInterface>();
                    this.interfaces.put(ifaceNumber, settings);
                }
                final UsbInterface usbInterface = new UsbInterfaceImpl(this,
                    desc);

                // If we have no active setting for current interface number
                // yet or the alternate setting number is 0 (which marks the
                // default alternate setting) then set current interface as
                // the active setting.
                if (!this.activeSettings.containsKey(ifaceNumber) ||
                        desc.bAlternateSetting() == 0)
                    this.activeSettings.put(ifaceNumber, usbInterface);

                // Add the interface to the settings list
                settings.put(settingNumber, usbInterface);
            }
        }
    }


    /**
     * @see javax.usb.UsbConfiguration#isActive()
     */

    @Override
    public boolean isActive()
    {
        return this.device.getActiveUsbConfigurationNumber() == this.descriptor
                .bConfigurationValue();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbInterfaces()
     */

    @Override
    public List<UsbInterface> getUsbInterfaces()
    {
        return Collections.unmodifiableList(new ArrayList<UsbInterface>(
            this.activeSettings.values()));
    }


    /**
     * Returns the alternate settings for the specified interface.
     *
     * @param number
     *            The interface number.
     * @return The alternate settings for the specified interface.
     */

    Map<Integer, UsbInterface> getSettings(final byte number)
    {
        return this.interfaces.get(number);
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
        return this.interfaces.get(number).size();
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbInterface(byte)
     */

    @Override
    public UsbInterface getUsbInterface(final byte number)
    {
        return this.activeSettings.get(number);
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

    void setUsbInterface(final byte number, final UsbInterface iface)
        throws UsbException
    {
        if (this.activeSettings.get(number & 0xff) != iface)
        {
            USBLock.acquire();
            try
            {
                final int result = usb_set_altinterface(this.device.open(),
                    iface.getUsbInterfaceDescriptor().bAlternateSetting());
                if (result < 0) throw new UsbException(usb_strerror());
                this.activeSettings.put(number & 0xff, iface);
            }
            finally
            {
                USBLock.release();
            }
        }
    }


    /**
     * @see javax.usb.UsbConfiguration#containsUsbInterface(byte)
     */

    @Override
    public boolean containsUsbInterface(final byte number)
    {
        return this.activeSettings.containsKey(number);
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbDevice()
     */

    @Override
    public UsbDevice getUsbDevice()
    {
        return this.device;
    }


    /**
     * @see javax.usb.UsbConfiguration#getUsbConfigurationDescriptor()
     */

    @Override
    public UsbConfigurationDescriptor getUsbConfigurationDescriptor()
    {
        return this.descriptor;
    }


    /**
     * @see javax.usb.UsbConfiguration#getConfigurationString()
     */

    @Override
    public String getConfigurationString() throws UsbException,
        UnsupportedEncodingException
    {
        final byte iConfiguration = this.descriptor.iConfiguration();
        if (iConfiguration == 0) return null;
        return this.device.getString(iConfiguration);
    }
}
