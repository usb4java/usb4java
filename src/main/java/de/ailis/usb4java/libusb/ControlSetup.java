/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://www.libusb.org/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2011 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2012 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2012 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2012 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */

package de.ailis.usb4java.libusb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.ailis.usb4java.utils.BufferUtils;

public final class ControlSetup
{
    private final ByteBuffer controlSetup;

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
     * USB Control Setup is always 8 bytes long.
     * Structured as follows:
     * byte 0: bmRequestType
     * byte 1: bRequest
     * bytes 2-3: wValue (Little Endian)
     * bytes 4-5: wIndex (Little Endian)
     * bytes 6-7: wLength (Little Endian)
     */

    public byte bmRequestType()
    {
        return this.controlSetup.get(0);
    }

    public void setBmRequestType(final byte bmRequestType)
    {
        this.controlSetup.put(0, bmRequestType);
    }

    public byte bRequest()
    {
        return this.controlSetup.get(1);
    }

    public void setBRequest(final byte bRequest)
    {
        this.controlSetup.put(1, bRequest);
    }

    public short wValue()
    {
        return this.controlSetup.getShort(2);
    }

    public void setWValue(final short wValue)
    {
        this.controlSetup.putShort(2, wValue);
    }

    public short wIndex()
    {
        return this.controlSetup.getShort(4);
    }

    public void setWIndex(final short wIndex)
    {
        this.controlSetup.putShort(4, wIndex);
    }

    public short wLength()
    {
        return this.controlSetup.getShort(6);
    }

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
