package de.ailis.usb4java.libusb;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class IsoPacketDescriptor
{
    /** The native pointer to the descriptor structure. */
    private long isoPacketDescriptorPointer;

    /**
     * Package-private constructor to prevent manual instantiation.
     * IsoPacketDescriptors are always created by JNI.
     */
    IsoPacketDescriptor()
    {
        // Empty
    }

    /**
     * Returns the native pointer.
     * 
     * @return The native pointer.
     */
    public long getPointer()
    {
        return isoPacketDescriptorPointer;
    }

    public native int length();

    public native void setLength(final int length);

    public native int actualLength();

    public native int status();

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(length())
        .append(actualLength())
        .append(status())
        .toHashCode();
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

        final IsoPacketDescriptor other = (IsoPacketDescriptor) obj;

        return new EqualsBuilder()
        .append(length(), other.length())
        .append(actualLength(), other.actualLength())
        .append(status(), other.status())
        .isEquals();
    }
}
