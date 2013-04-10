/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import java.nio.ByteBuffer;

import de.ailis.usb4java.libusb.ConfigDescriptor;

/**
 * The USB configuration descriptor describes information about a specific USB
 * device configuration.
 *
 * @author Klaus Reimer (k@ailis.de)
 * 
 * @deprecated Use the new libusb 1.0 API or the JSR 80 API.
 */
@Deprecated
public final class USB_Config_Descriptor extends USB_Descriptor_Header
{
    /** The number of hex dump columns for dumping extra descriptor. */
    private static final int HEX_DUMP_COLS = 16;

    /** The total descriptor length. */
    private final int wTotalLength;

    /** The number of interfaces. */
    private final int bNumInterfaces;

    /** The configuration value. */
    private final int bConfigurationValue;

    /** The configuration number. */
    private final int iConfiguration;

    /** The attributes. */
    private final int bmAttributes;

    /** The maximum power. */
    private final int bMaxPower;
    
    /** The length of the extra data. */
    private final int extralen;

    /** The extra data. */
    private final byte[] extra;

    /** The interfaces. */
    private final USB_Interface[] iface;

    /**
     * Constructor.
     * 
     * @param desc
     *            The new config descriptor.
     */
    public USB_Config_Descriptor(final ConfigDescriptor desc)
    {
        super(desc);
        this.bConfigurationValue = desc.bConfigurationValue() & 0xff;
        this.bmAttributes = desc.bmAttributes() & 0xff;
        this.bMaxPower = desc.bMaxPower() & 0xff;
        this.bNumInterfaces = desc.bNumInterfaces() & 0xff;
        this.iConfiguration = desc.iConfiguration() & 0xff;
        this.wTotalLength = desc.wTotalLength() & 0xffff;
        this.extralen = desc.extraLength();
        this.extra = new byte[this.extralen];
        desc.extra().get(this.extra);
        this.iface = new USB_Interface[this.bNumInterfaces];
        for (int i = 0; i < this.bNumInterfaces; i++)
            this.iface[i] = new USB_Interface(desc.iface()[i]);    

    }

    /**
     * Returns the total size of the data returned for this configuration
     * including the length of all descriptors returned for this configuration.
     * 
     * @return The total length of all configuration data (unsigned short).
     */
    public int wTotalLength()
    {
        return this.wTotalLength;
    }

    /**
     * Returns the number of supported interfaces.
     * 
     * @return The number of supported interfaces (unsigned byte).
     */
    public int bNumInterfaces()
    {
        return this.bNumInterfaces;
    }

    /**
     * The value to be used for usb_set_configuration() to select this
     * configuration.
     * 
     * @return The configuration value (unsigned byte).
     */
    public int bConfigurationValue()
    {
        return this.bConfigurationValue;
    }

    /**
     * Returns the index of the string descriptor describing this configuration.
     * 
     * @return The index of the string descriptor describing this configuration
     *         (unsigned byte).
     */
    public int iConfiguration()
    {
        return this.iConfiguration;
    }

    /**
     * Returns a bitmap with configuration attributes.
     * <ul>
     * <li>Bit 7: Reserved. (USB 1: Bus-powered)</li>
     * <li>Bit 6: Self-powered.</li>
     * <li>Bit 5: Remote wakeup.</li>
     * <li>Bit 4: Reserved.</li>
     * <li>Bit 3: Reserved.</li>
     * <li>Bit 2: Reserved.</li>
     * <li>Bit 1: Reserved.</li>
     * <li>Bit 0: Reserved.</li>
     * </ul>
     * 
     * @return A bitmap with configuration attributes (unsigned byte).
     */
    public int bmAttributes()
    {
        return this.bmAttributes;
    }

    /**
     * Returns the maximum power consumption of the device when this
     * configuration is active and the device is fully operational. The power is
     * expressed in 2 maA units (50 means 100mA for example).
     * 
     * @return The maximum power consumption in 2mA units (unsigned byte).
     */
    public int bMaxPower()
    {
        return this.bMaxPower;
    }

    /**
     * Returns the length of the extra data block in bytes.
     * 
     * @return The length of the extra data block in bytes.
     */
    public int extralen()
    {
        return this.extralen;
    }

    /**
     * Returns the extra data block.
     * 
     * @return The extra data block.
     */
    public ByteBuffer extra()
    {
        return ByteBuffer.wrap(this.extra);
    }

    /**
     * Returns the interfaces of this USB configuration. The original method is
     * named "interface" but this can't be used in Java because it is a reserved
     * word.
     * 
     * @return The interfaces of this USB configuration.
     */
    public USB_Interface[] iface()
    {
        return this.iface;
    }

    /**
     * Returns a dump of this descriptor.
     * 
     * @return The descriptor dump.
     */
    public String dump()
    {
        return dump(null);
    }

    /**
     * Returns a dump of this descriptor.
     * 
     * @param handle
     *            The USB device handle for resolving string descriptors. If
     *            null then no strings are resolved.
     * @return The descriptor dump.
     */
    public String dump(final USB_Dev_Handle handle)
    {
        final StringBuilder builder = new StringBuilder();
        builder
            .append(String.format("Configuration Descriptor:%n"
                + "  bLength               %5d%n"
                + "  bDescriptorType       %5d%n"
                + "  wTotalLength          %5d%n"
                + "  bNumInterfaces        %5d%n"
                + "  bConfigurationValue   %5d%n"
                + "  iConfiguration        %5d%n"
                + "  bmAttributes           %#04x%n"
                + "  MaxPower              %5d mA%n"
                + "  extralen         %10d%n"
                + "  extra:%n"
                + "%s",
                bLength(), bDescriptorType(), wTotalLength(), bNumInterfaces(),
                bConfigurationValue(), iConfiguration(), bmAttributes(),
                bMaxPower() * 2, extralen(), toHexDump(extra(), HEX_DUMP_COLS)
                    .replaceAll("(?m)^", "    ")));
        for (final USB_Interface descriptor: iface())
        {
            builder.append(descriptor.dump(handle)
                .replaceAll("(?m)^", "  "));
        }
        return builder.toString();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        final USB_Config_Descriptor other = (USB_Config_Descriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && bConfigurationValue() == other.bConfigurationValue()
            && bmAttributes() == other.bmAttributes()
            && bNumInterfaces() == other.bNumInterfaces()
            && iConfiguration() == other.iConfiguration()
            && bMaxPower() == other.bMaxPower()
            && wTotalLength() == other.wTotalLength();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bLength();
        result = 37 * result + bDescriptorType();
        result = 37 * result + wTotalLength();
        result = 37 * result + bNumInterfaces();
        result = 37 * result + bConfigurationValue();
        result = 37 * result + iConfiguration();
        result = 37 * result + bmAttributes();
        result = 37 * result + bMaxPower();
        result = 37 * result + extra().hashCode();
        result = 37 * result + extralen();
        return result;
    }
}
