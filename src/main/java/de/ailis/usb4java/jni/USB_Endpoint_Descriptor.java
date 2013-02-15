/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import java.nio.ByteBuffer;

/**
 * This descriptor contains information about an endpoint.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Endpoint_Descriptor extends USB_Descriptor_Header
{
    /** The number of hex dump columns for dumping extra descriptor. */
    private static final int HEX_DUMP_COLS = 16;

    /**
     * Constructor.
     *
     * @param data
     *            The descriptor data.
     */
    public USB_Endpoint_Descriptor(final ByteBuffer data)
    {
        super(data);
    }

    /**
     * Returns the endpoint address. The address is encoded as follows:
     * <ul>
     * <li>Bit 7: The direction (ignored by control endpoints), 0=OUT, 1=IN</li>
     * <li>Bit 6-4: Reserved</li>
     * <li>Bit 3-0: The endpoint number.</li>
     * </ul>
     *
     * @return The endpoint address (unsigned byte).
     */
    public native int bEndpointAddress();

    /**
     * Returns the endpoint attributes. This is a bitmask with the following
     * meaning:
     * <ul>
     * <li>Bit 7-6: Reserved</li>
     * <li>Bit 5-4: Usage type (00=Data endpoint, 01=Feedback endpoint,
     * 10=Implicit feedback data endpoint, 11=Reserved)</li>
     * <li>Bit 3-2: Synchronization type (00=No synchronization,
     * 01=Asynchronous, 10=Adaptive, 11=Synchronous)</li>
     * <li>Bit 1-0: Transfer type
     * (00=Control,01=Isochronous,10=Bulk,11=Interrupt). See USB_ENDPOINT_TYPE_*
     * constants.</li>
     * </ul>
     *
     * @return The endpoint attributes bitmask (unsigned byte).
     */
    public native int bmAttributes();

    /**
     * Returns the maximum packet size the endpoint is capable of sending or
     * receiving when this configuration is selected.
     *
     * For all endpoints this maximum packet size is located in bits 10-0.
     *
     * Bits 12-11 specify the number of additional opportunities per microframe:
     * 00=None (1 Transaction per microframe), 01=1 additional (2 per
     * microframe), 10=2 additional (3 per microframe), 11=Reserved.
     *
     * Bits 15-13 are reserved.
     *
     * @return The maximum packet size of the endpoint (unsigned short).
     */
    public native int wMaxPacketSize();

    /**
     * Returns the interval for polling endpoint for data transfers in frames or
     * microframes depending on the device operating speed.
     *
     * @return The interval for polling endpoint (unsigned byte).
     */
    public native int bInterval();

    /**
     * Returns the rate at which synchronization feedback is provided. (For
     * audio devices only)
     *
     * @return The synchronization rate (unsigned byte).
     */
    public native int bRefresh();

    /**
     * Returns the address if the synch endpoint. (For audio devices only)
     *
     * @return The address of the synch endpoint (unsigned byte).
     */
    public native int bSynchAddress();

    /**
     * Returns the extra descriptor data.
     *
     * @return The extra descriptor data.
     */
    public native ByteBuffer extra();

    /**
     * Returns the size of the extra descriptor data in bytes.
     *
     * @return The extra descriptor size.
     */
    public native int extralen();

    /**
     * Returns a dump of this descriptor.
     *
     * @return The descriptor dump.
     */
    public String dump()
    {
        return String.format("Endpoint Descriptor:%n"
            + "  bLength           %5d%n"
            + "  bDecsriptorType   %5d%n"
            + "  bEndpointAddress   0x%02x%n"
            + "  bmAttributes       0x%02x%n"
            + "  wMaxPacketSize    %5d%n"
            + "  bInterval         %5d%n"
            + "  extralen     %10d%n"
            + "  extra:%n"
            + "%s",
            bLength(), bDescriptorType(), bEndpointAddress(), bmAttributes(),
            wMaxPacketSize(), bInterval(), extralen(),
            toHexDump(extra(), HEX_DUMP_COLS)
                .replaceAll("(?m)^", "    "));
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
        final USB_Endpoint_Descriptor other = (USB_Endpoint_Descriptor) o;
        return bDescriptorType() == other.bDescriptorType()
            && bLength() == other.bLength()
            && bEndpointAddress() == other.bEndpointAddress()
            && bmAttributes() == other.bmAttributes()
            && bInterval() == other.bInterval()
            && bSynchAddress() == other.bSynchAddress()
            && wMaxPacketSize() == other.wMaxPacketSize()
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
        result = 37 * result + bEndpointAddress();
        result = 37 * result + bmAttributes();
        result = 37 * result + wMaxPacketSize();
        result = 37 * result + bInterval();
        result = 37 * result + bRefresh();
        result = 37 * result + bSynchAddress();
        result = 37 * result + extra().hashCode();
        result = 37 * result + extralen();
        return result;
    }
}
