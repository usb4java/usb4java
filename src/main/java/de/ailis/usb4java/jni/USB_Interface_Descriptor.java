/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import static de.ailis.usb4java.jni.USB.usb_get_string_simple;

import java.nio.ByteBuffer;

/**
 * The interface descriptor describes a specific interface of a USB
 * configuration.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class USB_Interface_Descriptor extends USB_Descriptor_Header
{
    /** The number of hex dump columns for dumping extra descriptor. */
    private static final int HEX_DUMP_COLS = 16;

    /**
     * Constructor.
     *
     * @param data
     *            The descriptor data
     */
    public USB_Interface_Descriptor(final ByteBuffer data)
    {
        super(data);
    }

    /**
     * Returns the zero-based interface number.
     *
     * @return The interface number (unsigned byte).
     */
    public native int bInterfaceNumber();

    /**
     * Returns the value used to select this alternate setting for the
     * interface.
     *
     * @return The value used to select this alternate setting (unsigned byte).
     */
    public native int bAlternateSetting();

    /**
     * Returns the number of endpoints.
     *
     * @return The number of endpoints (unsigned byte).
     */
    public native int bNumEndpoints();

    /**
     * Returns the interface class code as assigned by the USB-IF. Class 0 is
     * reserved for future standardization. Class 0xff means that the interface
     * class is vendor-specific. All other values are reserved for assignment by
     * the USB_IF.
     *
     * @return The interface class code (unsigned byte).
     */
    public native int bInterfaceClass();

    /**
     * Returns the interface sub class code as assigned by the USB-IF. These
     * codes are qualified by the interface class. If bInterfaceClass is 0 then
     * the sub class is also 0. If class is not 0xff then all sub classes are
     * reserved by the USB-IF.
     *
     * @return The interface sub class code (unsigned byte).
     */
    public native int bInterfaceSubClass();

    /**
     * Returns the protocol code as assigned by the USB-IF. These codes are
     * qualified by the interface class and sub class. If an interface supports
     * class-specific requests, this code identifies the protocols that the
     * device uses as defined by the specification of the device class. If this
     * field is set to zero then the device does not use a class-specific
     * protocol on this interface. If this field is set to 0xff then the device
     * uses a vendor-specific protocol for this interface.
     *
     * @return The protocol code (unsigned byte).
     */
    public native int bInterfaceProtocol();

    /**
     * Returns the index of the string descriptor describing this interface.
     *
     * @return The string descriptor index (unsigned byte).
     */
    public native int iInterface();

    /**
     * Returns the array with endpoints.
     *
     * @return The array with endpoints.
     */
    public native USB_Endpoint_Descriptor[] endpoint();

    /**
     * Returns the extra descriptor data.
     *
     * @return The extra descriptor data.
     */
    public native ByteBuffer extra();

    /**
     * Returns the size of the extra data in bytes.
     *
     * @return The size of the extra data in bytes.
     */
    public native int extralen();

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
        final int iInterface = iInterface();
        String sInterface = iInterface == 0 || handle == null ? ""
            : usb_get_string_simple(handle, iInterface);
        if (sInterface == null) sInterface = "";
        builder.append(String.format("Interface Descriptor:%n"
            + "  bLength             %5d%n"
            + "  bDescriptorType     %5d%n"
            + "  bInterfaceNumber    %5d%n"
            + "  bAlternateSetting   %5d%n"
            + "  bNumEndpoints       %5d%n"
            + "  bInterfaceClass     %5d %s%n"
            + "  bInterfaceSubClass  %5d%n"
            + "  bInterfaceProtocol  %5d%n"
            + "  iInterface          %5d %s%n"
            + "  extralen       %10d%n"
            + "  extra:%n"
            + "%s",
            bLength(), bDescriptorType(), bInterfaceNumber(),
            bAlternateSetting(), bNumEndpoints(), bInterfaceClass(),
            getUSBClassName(bInterfaceClass()), bInterfaceSubClass(),
            bInterfaceProtocol(), iInterface(), sInterface, extralen(),
            toHexDump(extra(), HEX_DUMP_COLS)
                .replaceAll("(?m)^", "    ")));
        if (extralen() != 0) return builder.toString();
        for (final USB_Endpoint_Descriptor edesc : endpoint())
        {
            builder.append(edesc.dump().replaceAll("(?m)^", "  "));
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
        final USB_Interface_Descriptor other = (USB_Interface_Descriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && bAlternateSetting() == other.bAlternateSetting()
            && bInterfaceClass() == other.bInterfaceClass()
            && bInterfaceNumber() == other.bInterfaceNumber()
            && bInterfaceProtocol() == other.bInterfaceProtocol()
            && bInterfaceSubClass() == other.bInterfaceSubClass()
            && bNumEndpoints() == other.bNumEndpoints()
            && iInterface() == other.iInterface()
            && extralen() == other.extralen()
            && extra().equals(other.extra());
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
        result = 37 * result + bInterfaceNumber();
        result = 37 * result + bAlternateSetting();
        result = 37 * result + bNumEndpoints();
        result = 37 * result + bInterfaceClass();
        result = 37 * result + bInterfaceSubClass();
        result = 37 * result + bInterfaceProtocol();
        result = 37 * result + iInterface();
        result = 37 * result + extra().hashCode();
        result = 37 * result + extralen();
        return result;
    }
}
