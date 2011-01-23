/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * This descriptor contains information about an endpoint.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Endpoint_Descriptor extends USB_Descriptor_Header
{
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
     * Returns the endpoint address. THe address is encoded as follos:
     * <ul>
     * <li>Bit 7: The direction (ignored by control endpoints), 0=OUT, 1=IN</li>
     * <li>Bit 6-4: Reserved</li>
     * <li>Bit 3-0: The endpoint number.</li>
     * </ul>
     *
     * @return The endpoint address (unsigned byte).
     */

    public final int bEndpointAddress()
    {
        return this.data.get(2) & 0xff;
    }


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
     * (00=Control,01=Isochronous,10=Bulk,11=Interrupt)</li>
     * </ul>
     *
     * @return The endpoint attributes bitmask (unsigned byte).
     */

    public final int bmAttributes()
    {
        return this.data.get(3) & 0xff;
    }


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

    public final int wMaxPacketSize()
    {
        this.data.order(ByteOrder.LITTLE_ENDIAN).position(4);
        return this.data.asShortBuffer().get() & 0xffff;
    }


    /**
     * Returns the interval for polling endpoint for data transfers in frames
     * or microframes depending on the device operating speed.
     *
     * @return The interval for polling endpoint (unsigned byte).
     */

    public final int bInterval()
    {
        return this.data.get(6) & 0xff;
    }


    /**
     * Returns the rate at which synchronization feedback is provided.
     * (For audio devices only)
     *
     * @return The synchronization rate (unsigned byte).
     */

    public final int bRefresh()
    {
        return this.data.get(7) & 0xff;
    }


    /**
     * Returns the address if the synch endpoint.
     * (For audio devices only)
     *
     * @return The address of the synch endpoint (unsigned byte).
     */

    public final int bSynchAddress()
    {
        return this.data.get(8) & 0xff;
    }


    /**
     * Returns the extra descriptor data.
     *
     * @return The extra descriptor data.
     */

    public final native ByteBuffer extra();


    /**
     * Returns the size of the extra descriptor data in bytes.
     *
     * @return The extra descriptor size.
     */

    public final native int extralen();
}
