/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.usb4java.jna.NativeConfigDescriptor;
import org.usb4java.jna.NativeInterface;

import com.sun.jna.Pointer;

/**
 * A structure representing the standard USB configuration descriptor.
 *
 * This descriptor is documented in section 9.6.3 of the USB 3.0 specification. All multiple-byte fields are represented
 * in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ConfigDescriptor {
    /** The config descriptor structure. */
    private NativeConfigDescriptor nativeConfigDescriptor;

    /**
     * Initializes the config descriptor.
     *
     * @param nativeConfigDescriptor
     *            The native config descriptor to initialize this one with.
     */
    void init(final NativeConfigDescriptor nativeConfigDescriptor) {
        if (nativeConfigDescriptor == null) {
            throw new IllegalArgumentException("Native config descriptor must not be null");
        }
        if (this.nativeConfigDescriptor != null) {
            throw new IllegalStateException("Config descriptor already initialized");
        }
        this.nativeConfigDescriptor = nativeConfigDescriptor;
    }

    /**
     * Returns the config descriptor structure.
     *
     * @return The config descriptor structure.
     */
    NativeConfigDescriptor getNative() {
        if (this.nativeConfigDescriptor == null) {
            throw new IllegalStateException("Config descriptor not initialized");
        }
        return this.nativeConfigDescriptor;
    }

    /**
     * Returns the size of this descriptor (in bytes).
     *
     * @return The size of this descriptor (in bytes).
     */
    public byte bLength() {
        return this.nativeConfigDescriptor.bLength;
    }

    /**
     * Returns the descriptor type. Will have value {@link LibUsb#DT_CONFIG} in this context.
     *
     * @return The descriptor type.
     */
    public byte bDescriptorType() {
        return this.nativeConfigDescriptor.bDescriptorType;
    }

    /**
     * Returns the total length of data returned for this configuration.
     *
     * @return The total length of data.
     */
    public short wTotalLength() {
        return this.nativeConfigDescriptor.wTotalLength;
    }

    /**
     * Returns the number of interfaces supported by this configuration.
     *
     * @return The number of supported interfaces.
     */
    public byte bNumInterfaces() {
        return this.nativeConfigDescriptor.bNumInterfaces;
    }

    /**
     * Returns the identifier value for this configuration.
     *
     * @return The identifier value.
     */
    public byte bConfigurationValue() {
        return this.nativeConfigDescriptor.bConfigurationValue;
    }

    /**
     * Returns the index of string descriptor describing this configuration.
     *
     * @return The string descriptor index.
     */
    public byte iConfiguration() {
        return this.nativeConfigDescriptor.iConfiguration;
    }

    /**
     * Returns the configuration characteristics.
     *
     * @return The configuration characteristics.
     */
    public byte bmAttributes() {
        return this.nativeConfigDescriptor.bmAttributes;
    }

    /**
     * Returns the maximum power consumption of the USB device from this bus in this configuration when the device is
     * fully operation. Expressed in units of 2 mA.
     *
     * @return The maximum power consumption.
     */
    public byte bMaxPower() {
        return this.nativeConfigDescriptor.bMaxPower;
    }

    /**
     * Returns the array with interfaces supported by this configuration.
     *
     * @return The array with interfaces.
     */
    public Interface[] iface() {
        final int numInterfaces = bNumInterfaces() & 0xff;
        final Interface[] ifaces = new Interface[numInterfaces];
        if (numInterfaces > 0) {
            final NativeInterface[] nativeInterfaces = (NativeInterface[]) this.nativeConfigDescriptor.iface
                    .toArray(numInterfaces);
            for (int i = 0; i != numInterfaces; ++i) {
                ifaces[i] = new Interface(nativeInterfaces[i]);
            }
        }
        return ifaces;
    }

    /**
     * Extra descriptors.
     *
     * If libusb encounters unknown interface descriptors, it will store them here, should you wish to parse them.
     *
     * @return The extra descriptors.
     */
    public ByteBuffer extra() {
        final Pointer pointer = this.nativeConfigDescriptor.extra;
        if (pointer == null) {
            return ByteBuffer.allocate(0);
        } else {
            return pointer.getByteBuffer(0, extraLength());
        }
    }

    /**
     * Length of the extra descriptors, in bytes.
     *
     * @return The extra descriptors length.
     */
    public int extraLength() {
        return this.nativeConfigDescriptor.extra_length;
    }

    /**
     * Returns a dump of this descriptor.
     *
     * @return The descriptor dump.
     */
    public String dump() {
        final StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s" + "  extralen %17d%n" + "  extra:%n" + "%s", DescriptorUtils.dump(this),
                extraLength(), DescriptorUtils.dump(extra()).replaceAll("(?m)^", "    ")));

        for (final Interface iface : iface()) {
            builder.append(String.format("%n") + iface.dump());
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bLength()).append(bDescriptorType()).append(wTotalLength())
                .append(bNumInterfaces()).append(bConfigurationValue()).append(iConfiguration())
                .append(bmAttributes()).append(bMaxPower()).append(iface()).append(extra())
                .append(extraLength()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final ConfigDescriptor other = (ConfigDescriptor) obj;

        return new EqualsBuilder().append(bLength(), other.bLength())
                .append(bDescriptorType(), other.bDescriptorType())
                .append(wTotalLength(), other.wTotalLength())
                .append(bNumInterfaces(), other.bNumInterfaces())
                .append(bConfigurationValue(), other.bConfigurationValue())
                .append(iConfiguration(), other.iConfiguration())
                .append(bmAttributes(), other.bmAttributes()).append(bMaxPower(), other.bMaxPower())
                .append(iface(), other.iface()).append(extra(), other.extra())
                .append(extraLength(), other.extraLength()).isEquals();
    }

    @Override
    public String toString() {
        return dump();
    }
}
