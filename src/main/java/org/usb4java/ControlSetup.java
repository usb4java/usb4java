/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package org.usb4java;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Setup packet for control transfers.
 * 
 * @author Luca Longinotti (l@longi.li)
 */
public final class ControlSetup
{
    /** 
     * The native data structure of the setup packet.
     * 
     * USB Control Setup is always 8 bytes long. Structured as follows:
     * 
     * byte 0: bmRequestType
     * byte 1: bRequest
     * bytes 2-3: wValue (Little Endian)
     * bytes 4-5: wIndex (Little Endian)
     * bytes 6-7: wLength (Little Endian)
     */
    private final ByteBuffer controlSetup;

    /**
     * Constructs a new control setup packet using the first 8 bytes from the
     * specified transfer buffer.
     * 
     * @param buffer
     *            The transfer buffer.
     */
    ControlSetup(final ByteBuffer buffer)
    {
        if (buffer == null)
        {
            throw new IllegalArgumentException("buffer cannot be null");
        }

        this.controlSetup = BufferUtils.slice(buffer, 0,
            LibUsb.CONTROL_SETUP_SIZE);

        // Control Setup (as all of USB) is Little Endian.
        this.controlSetup.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Returns the request type.
     * 
     * Bits 0:4 determine recipient.
     * Bits 5:6 determine type. 
     * Bit 7 determines data transfer direction.
     * 
     * @return The request type.
     */
    public byte bmRequestType()
    {
        return this.controlSetup.get(0);
    }

    /**
     * Sets the request type.
     * 
     * @param bmRequestType
     *            The request type to set.
     */
    public void setBmRequestType(final byte bmRequestType)
    {
        this.controlSetup.put(0, bmRequestType);
    }

    /**
     * Returns the request.
     * 
     * If the type bits of {@link #bmRequestType()} are equal to 
     * {@link LibUsb#REQUEST_TYPE_STANDARD} then this field refers to 
     * one of the REQUEST_* constants or {@link LibUsb#SET_ISOCH_DELAY}. For 
     * other cases, use of this field is application-specific.
     * 
     * @return The request.
     */
    public byte bRequest()
    {
        return this.controlSetup.get(1);
    }

    /**
     * Sets the request.
     * 
     * @param bRequest
     *            The request to set.
     */
    public void setBRequest(final byte bRequest)
    {
        this.controlSetup.put(1, bRequest);
    }

    /**
     * Returns the value.
     * 
     * Varies according to request
     * 
     * @return The value.
     */
    public short wValue()
    {
        return this.controlSetup.getShort(2);
    }

    /**
     * Sets the value.
     * 
     * @param wValue
     *            The value to set.
     */
    public void setWValue(final short wValue)
    {
        this.controlSetup.putShort(2, wValue);
    }

    /**
     * Returns the index.
     * 
     * Varies according to request, typically used to pass an index or offset
     * 
     * @return The index.
     */
    public short wIndex()
    {
        return this.controlSetup.getShort(4);
    }

    /**
     * Sets the index.
     * 
     * @param wIndex
     *            The index to set.
     */
    public void setWIndex(final short wIndex)
    {
        this.controlSetup.putShort(4, wIndex);
    }

    /**
     * Returns the number of bytes to transfer.
     * 
     * @return The number of bytes to transfer.
     */    
    public short wLength()
    {
        return this.controlSetup.getShort(6);
    }

    /**
     * Sets the number of bytes to transfer.
     * 
     * @param wLength
     *            The number of bytes to transfer.
     */
    public void setWLength(final short wLength)
    {
        this.controlSetup.putShort(6, wLength);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + ((this.controlSetup == null) ? 0 : this.controlSetup.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final ControlSetup other = (ControlSetup) obj;
        if (this.controlSetup == null)
        {
            if (other.controlSetup != null)
            {
                return false;
            }
        }
        else if (!this.controlSetup.equals(other.controlSetup))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb control setup with buffer %s",
            this.controlSetup.toString());
    }
}
