/*
 * Copyright (C) 2015 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.jna;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * TODO Document me.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public interface LibUsbNative extends Library {

    public String libusb_error_name(int error_code);

    public int libusb_has_capability(int capability);

    public NativeVersion libusb_get_version();

    public int libusb_setlocale(String locale);

    public String libusb_strerror(int errcode);

    public int libusb_init(PointerByReference context);

    public void libusb_set_debug(Pointer context, int level);

    public void libusb_exit(Pointer context);

    public int libusb_get_device_list(Pointer context, PointerByReference list);

    public void libusb_free_device_list(Pointer list, int unref_devices);

    public byte libusb_get_bus_number(Pointer device);

    public byte libusb_get_port_number(Pointer device);

    public int libusb_get_port_numbers(Pointer device, ByteBuffer port_numbers, int port_numbers_len);

    public Pointer libusb_get_parent(Pointer device);

    public byte libusb_get_device_address(Pointer device);

    public int libusb_get_device_speed(Pointer device);

    public int libusb_get_max_packet_size(Pointer device, byte endpoint);

    public int libusb_get_max_iso_packet_size(Pointer device, byte endpoint);

    public Pointer libusb_ref_device(Pointer device);

    public void libusb_unref_device(Pointer device);

    public int libusb_open(Pointer device, PointerByReference handle);

    public Pointer libusb_open_device_with_vid_pid(Pointer context, short vendor_id, short product_id);

    public void libusb_close(Pointer handle);

    public Pointer libusb_get_device(Pointer handle);

    int libusb_get_configuration(Pointer handle, IntByReference config);

    int libusb_set_configuration(Pointer handle, int configuration);

    int libusb_claim_interface(Pointer handle, int interface_number);

    int libusb_release_interface(Pointer handle, int interface_number);

    int libusb_set_interface_alt_setting(Pointer handle, int interface_number, int alternate_setting);

    int libusb_clear_halt(Pointer handle, byte endpoint);

    int libusb_reset_device(Pointer handle);

    int libusb_kernel_driver_active(Pointer handle, int interface_number);

    int libusb_detach_kernel_driver(Pointer handle, int interface_number);

    int libusb_attach_kernel_driver(Pointer handle, int interface_number);

    int libusb_set_auto_detach_kernel_driver(Pointer handle, int enable);

    int libusb_get_device_descriptor(Pointer device, DeviceDescriptor descriptor);

    int libusb_get_active_config_descriptor(Pointer device, PointerByReference config);

    int libusb_get_config_descriptor(Pointer device, byte config_index, PointerByReference config);

    int libusb_get_config_descriptor_by_value(Pointer device, byte bConfigurationValue, PointerByReference config);

    void libusb_free_config_descriptor(ConfigDescriptor config);
}
