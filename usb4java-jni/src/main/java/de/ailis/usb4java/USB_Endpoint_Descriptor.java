/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;


/**
 * This descriptor contains information about an endpoint.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public class USB_Endpoint_Descriptor extends USB_Descriptor_Header
{
    /**
     * Constructor.
     *
     * @param pointer
     *            The low-level pointer to the C structure.
     */

    USB_Endpoint_Descriptor(final long pointer)
    {
        super(pointer);
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

    public native short bEndpointAddress();

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

    public native short bmAttributes();


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
     * Returns the interval for polling endpoint for data transfers in frames
     * or microframes depending on the device operating speed.
     *
     * @return The interval for polling endpoint (unsigned byte).
     */

    public native byte bInterval();


    /**
     * Returns the refresh information.
     *
     * @return The refresh information (unsigned byte).
     */

    public native short bRefresh();


    /**
     * Returns the synch address.
     *
     * @return The synch address (unsigned byte).
     */

    public native short bSynchAddress();


    /**
     * Returns the extra descriptor data.
     *
     * @return The extra descriptor data.
     */

    public native byte[] extra();


    /**
     * Returns the size of the extra descriptor data in bytes.
     *
     * @return The extra descriptor size.
     */

    public native int extralen();
}
