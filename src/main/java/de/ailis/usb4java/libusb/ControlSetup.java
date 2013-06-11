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

    public void setBmRequestType(final int bmRequestType)
    {
        controlSetup.put(0, (byte) bmRequestType);
    }

    public byte bRequest()
    {
        return controlSetup.get(1);
    }

    public void setBRequest(final int bRequest)
    {
        controlSetup.put(1, (byte) bRequest);
    }

    public short wValue()
    {
        return controlSetup.getShort(2);
    }

    public void setWValue(final int wValue)
    {
        controlSetup.putShort(2, (short) wValue);
    }

    public short wIndex()
    {
        return controlSetup.getShort(4);
    }

    public void setWIndex(final int wIndex)
    {
        controlSetup.putShort(4, (short) wIndex);
    }

    public short wLength()
    {
        return controlSetup.getShort(6);
    }

    public void setWLength(final int wLength)
    {
        controlSetup.putShort(6, (short) wLength);
    }
}
