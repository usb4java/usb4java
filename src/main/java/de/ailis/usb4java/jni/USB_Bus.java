/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import de.ailis.usb4java.libusb.LibUSB;

/**
 * The USB Bus.
 * 
 * @deprecated Use the
 *             {@link LibUSB#getDeviceList(de.ailis.usb4java.libusb.Context, de.ailis.usb4java.libusb.DeviceList)}
 *             method of the new libusb 1.0 API to enumerate USB devices.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
@Deprecated
public final class USB_Bus
{
    /** The location. */
    private final long location;

    /** The next USB bus. */
    private USB_Bus next;

    /** The previous USB bus. */
    private USB_Bus prev;

    /** The USB devices. Null if none. */
    USB_Device devices;
    
    /** The root device. Null if none. */
    USB_Device root_dev;

    /**
     * Constructor.
     * 
     * @param location
     *            The bus location.
     */
    USB_Bus(final long location)
    {
        this.location = location;
    }

    /**
     * Checks if there is already a bus with the specified location. Only call
     * this on the first bus in the list.
     * 
     * @param location
     *            The bus location.
     * @return True if bus already exists, false if not.
     */
    boolean exists(final long location)
    {
        if (this.location == location) return true;
        if (this.next != null) return this.next.exists(location);
        return false;
    }

    /**
     * Adds the specified bus to the list.
     * 
     * @param bus
     *            The bus to add.
     */
    void add(final USB_Bus bus)
    {
        if (this.next == null)
        {
            this.next = bus;
            bus.prev = this;
        }
        else
            this.next.add(bus);
    }

    /**
     * Counts the differences between this bus and the specified one.
     * 
     * @param other
     *            The other bus to compare to.
     * @return The number of differences.
     */
    int diff(final USB_Bus other)
    {
        if (other == null) return count();

        int changes = 0;
        
        USB_Bus bus = other;
        while (bus != null)
        {
            if (!exists(bus.location)) changes++;
            bus = bus.next;
        }

        bus = this;
        while (bus != null)
        {
            if (!other.exists(bus.location)) changes++;
            bus = bus.next;
        }

        return changes;
    }

    /**
     * Counts the number of busses. Only call this on the first bus in the list.
     * 
     * @return The number of busses in the list.
     */
    int count()
    {
        int count = 1;
        USB_Bus bus = this.next;
        while (bus != null)
        {
            count++;
            bus = bus.next;
        }
        return count;
    }

    /**
     * Returns the directory name of the USB bus.
     * 
     * @return The directory name. Never null.
     */
    public String dirname()
    {
        return String.format("%03d", this.location);
    }

    /**
     * Returns the next USB bus or null if none.
     * 
     * @return The next USB bus or null if none.
     */
    public USB_Bus next()
    {
        return this.next;
    }

    /**
     * Returns the previous USB bus or null if none.
     * 
     * @return The previous USB bus or null if none.
     */
    public USB_Bus prev()
    {
        return this.prev;
    }

    /**
     * Returns the location.
     * 
     * @return The location (32 bit unsigned integer).
     */
    public long location()
    {
        return this.location;
    }

    /**
     * Returns the USB devices. Actually this returns the first USB device and
     * you can use the {@link USB_Device#next()} and {@link USB_Device#prev()}
     * methods to navigate to the other devices. When no USB device was found
     * then null is returned.
     * 
     * @return The first USB device or null if none.
     */
    public USB_Device devices()
    {
        return this.devices;
    }

    /**
     * Returns the USB root device.
     * 
     * @return The USB root device or null if none or if it could not be
     *         determined because of insufficient permissions.
     */
    public USB_Device root_dev()
    {
        return this.root_dev;
    }

    /**
     * Returns a dump of this descriptor.
     * 
     * @return The descriptor dump.
     */
    public String dump()
    {
        final USB_Device rootDev = root_dev();
        final String rootDevName =
            rootDev == null ? "None or unknown" : rootDev
                .filename();
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("Bus:%n"
            + "  dirname %23s%n"
            + "  location        %15d%n"
            + "  root_dev %22s%n",
            dirname(), location(), rootDevName));
        USB_Device device = devices();
        while (device != null)
        {
            builder.append(device.dump().replaceAll("(?m)^", "  "));
            device = device.next();
        }
        return builder.toString();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return dirname();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final USB_Bus other = (USB_Bus) obj;
        return dirname().equals(other.dirname())
            && location() == other.location();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + dirname().hashCode();
        final long location = location();
        result = 37 * result + (int) (location ^ (location >>> Integer.SIZE));
        return result;
    }
}
