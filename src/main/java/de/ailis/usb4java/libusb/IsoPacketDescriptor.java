package de.ailis.usb4java.libusb;

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

    // Theoretically the right representation for a C unsigned int would be a
    // Java long, but the maximum length for ISO Packets is 1024 bytes, so an
    // int more than suffices to hold any possible valid values here.
    public native void setLength(final int length);

    public native int actualLength();

    public native int status();

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
            + (int) (isoPacketDescriptorPointer ^ (isoPacketDescriptorPointer >>> 32));
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
        final IsoPacketDescriptor other = (IsoPacketDescriptor) obj;
        if (isoPacketDescriptorPointer != other.isoPacketDescriptorPointer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("libusb iso packet descriptor 0x%x",
            isoPacketDescriptorPointer);
    }
}
