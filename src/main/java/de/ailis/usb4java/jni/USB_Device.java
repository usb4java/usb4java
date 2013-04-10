/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import static de.ailis.usb4java.jni.USB.usb_close;
import static de.ailis.usb4java.jni.USB.usb_open;
import de.ailis.usb4java.libusb.ConfigDescriptor;
import de.ailis.usb4java.libusb.Device;
import de.ailis.usb4java.libusb.DeviceDescriptor;
import de.ailis.usb4java.libusb.LibUSB;

/**
 * USB Device.
 * 
 * @deprecated Use the
 *             {@link LibUSB#getDeviceList(de.ailis.usb4java.libusb.Context, de.ailis.usb4java.libusb.DeviceList)}
 *             method of the new libusb 1.0 API to enumerate USB devices.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
@Deprecated
public final class USB_Device
{
    /** The USB bus this device belongs to. */
    private USB_Bus bus;

    /** The device number. */
    private final short devnum;

    /** The next device in the list. Null if none. */
    private USB_Device next;

    /** The previous device in the list. Null if none. */
    private USB_Device prev;

    /** The USB device descriptor. Null if device is not yet initialized. */
    private USB_Device_Descriptor descriptor;

    /** The libusb 1.0 device reference. */
    Device device;
    
    /** THe config descriptors. Null if device is not yet initialized. */
    private USB_Config_Descriptor[] config;

    /**
     * Constructor.
     * 
     * @param device
     *            The libusb 1.0 device reference.
     * @param bus
     *            The USB bus this device belongs to.
     * @param num
     *            The device number.
     */
    USB_Device(final Device device, final USB_Bus bus, final short num)
    {
        this.device = device;
        this.bus = bus;
        this.devnum = num;
    }

    /**
     * Returns the filename.
     * 
     * @return The filename.
     */
    public String filename()
    {
        return String.format("%03d", this.devnum);
    }

    /**
     * Returns the next USB device or null if none.
     * 
     * @return The next USB device or null if none.
     */
    public USB_Device next()
    {
        return this.next;
    }

    /**
     * Returns the child devices.
     * 
     * @return The child devices.
     */
    public USB_Device[] children()
    {
        // Not supported in libusb 1.0
        return null;
    }

    /**
     * Returns the previous USB device or null if none.
     * 
     * @return The previous USB device or null if none.
     */
    public USB_Device prev()
    {
        return this.prev;
    }

    /**
     * Returns the device number. The original data type for this information is
     * an unsigned byte. This wrapper returns a short int instead to avoid
     * problems with values larger then 127.
     * 
     * @return The device number (unsigned byte).
     */
    public short devnum()
    {
        return this.devnum;
    }

    /**
     * Returns the number of child devices. The original data type for this
     * information is an unsigned byte. This wrapper returns a short int instead
     * to avoid problems with values larger then 127.
     * 
     * @return The number of child devices (unsigned byte).
     */
    public short num_children()
    {
        // Not supported in libusb 1.0
        return 0;
    }

    /**
     * Returns the USB bus.
     * 
     * @return The USB bus.
     */
    public USB_Bus bus()
    {
        return this.bus;
    }

    /**
     * Returns the USB device descriptor.
     * 
     * @return The USB device descriptor.
     */
    public USB_Device_Descriptor descriptor()
    {
        return this.descriptor;
    }

    /**
     * Returns the USB config descriptor.
     * 
     * @return The USB config descriptor.
     */
    public USB_Config_Descriptor[] config()
    {
        return this.config;
    }

    /**
     * Dumps all device information to a string and returns it.
     * 
     * @return The dumped device information.
     */
    public String dump()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("Device:%n"
            + "  filename %20s%n"
            + "  bus %25s%n"
            + "  num_children            %5d%n"
            + "  devnum                  %5d%n",
            filename(), bus().dirname(), num_children(), devnum()));
        final USB_Dev_Handle handle = usb_open(this);
        try
        {
            builder.append(descriptor().dump(handle).replaceAll("(?m)^",
                "  "));
            for (final USB_Config_Descriptor descriptor: config())
            {
                if (descriptor == null) continue;
                builder.append(descriptor.dump(handle).replaceAll("(?m)^",
                    "  "));
            }
            return builder.toString();
        }
        finally
        {
            if (handle != null) usb_close(handle);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return String.format("USB device %s/%s (%04x:%04x)",
            bus().dirname(), filename(), descriptor().idVendor(),
            descriptor().idProduct());
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
        final USB_Device other = (USB_Device) obj;
        return bus().equals(other.bus()) && filename().equals(other.filename())
            && descriptor().equals(other.descriptor());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bus().hashCode();
        result = 37 * result + filename().hashCode();
        result = 37 * result + descriptor().hashCode();
        return result;
    }
    
    /**
     * Initializes the device.
     * 
     * @return 0 on success, error code otherwise.
     */
    int init()
    {
        final DeviceDescriptor descriptor = new DeviceDescriptor();
        int result = LibUSB.getDeviceDescriptor(this.device, descriptor);
        if (result < 0) return USB.err(result);
        
        // Initialize device descriptor
        this.descriptor = new USB_Device_Descriptor(descriptor);
        
        // Initialize config descriptors
        int numConfigs = descriptor.bNumConfigurations() & 0xff;
        this.config = new USB_Config_Descriptor[numConfigs];
        for (int i = 0; i < numConfigs; i++)
        {
            ConfigDescriptor config = new ConfigDescriptor();            
            result = LibUSB.getConfigDescriptor(this.device, i, config);
            if (result < 0) return USB.err(result);
            try
            {
                this.config[i] = new USB_Config_Descriptor(config);
            }
            finally
            {
                LibUSB.freeConfigDescriptor(config);
            }
        }
        
        // Reference the device
        LibUSB.refDevice(this.device);
        
        // Success
        return 0;
    }
    
    /**
     * Free the device.
     */
    void free()
    {
        LibUSB.unrefDevice(this.device);
    }

    /**
     * Adds the specified device to the list.
     * 
     * @param device
     *            The device to add.
     */
    void add(final USB_Device device)
    {
        if (this.next == null)
        {
            this.next = device;
            device.prev = this;
        }
        else
            this.next.add(device);
    }
    
    /**
     * Returns the first device in the list.
     * 
     * @return The first device in the list.
     */
    USB_Device first()
    {
        if (this.prev != null) return this.prev.first();
        return this;              
    }
    
    /**
     * Remove this device from the list.
     * 
     * @return The first device in the list. Null if no device left.
     */
    USB_Device remove()
    {
        if (this.next != null) this.next.prev = this.prev;                   
        if (this.prev == null) this.prev.next = this.next;
        return this.prev == null ? this.next : this.first();
    }
    
    /**
     * Checks if there is a device with the specified number. Only call
     * this on the first device in the list.
     * 
     * @param devnum
     *            The device number.
     * @return True if device exists, false if not.
     */
    boolean exists(final short devnum)
    {
        if (this.devnum == devnum) return true;
        if (this.next != null) return this.next.exists(devnum);
        return false;
    }
}
