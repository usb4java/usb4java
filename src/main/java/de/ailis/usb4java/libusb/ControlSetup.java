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

        controlSetup = BufferUtils.slice(buffer, 0, LibUsb.CONTROL_SETUP_SIZE);

        // Control Setup (as all of USB) is Little Endian.
        controlSetup.order(ByteOrder.LITTLE_ENDIAN);
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
        return controlSetup.get(0);
    }

    public void setBmRequestType(final byte bmRequestType)
    {
        controlSetup.put(0, bmRequestType);
    }

    public byte bRequest()
    {
        return controlSetup.get(1);
    }

    public void setBRequest(final byte bRequest)
    {
        controlSetup.put(1, bRequest);
    }

    public short wValue()
    {
        return controlSetup.getShort(2);
    }

    public void setWValue(final short wValue)
    {
        controlSetup.putShort(2, wValue);
    }

    public short wIndex()
    {
        return controlSetup.getShort(4);
    }

    public void setWIndex(final short wIndex)
    {
        controlSetup.putShort(4, wIndex);
    }

    public short wLength()
    {
        return controlSetup.getShort(6);
    }

    public void setWLength(final short wLength)
    {
        controlSetup.putShort(6, wLength);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + ((controlSetup == null) ? 0 : controlSetup.hashCode());
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
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ControlSetup other = (ControlSetup) obj;
        if (controlSetup == null)
        {
            if (other.controlSetup != null)
            {
                return false;
            }
        }
        else if (!controlSetup.equals(other.controlSetup))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb control setup with buffer %s",
            controlSetup.toString());
    }
}
