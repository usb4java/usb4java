/**
 *
 */

package org.usb4java.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * @author k
 *
 */
public class Test {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final LibUsbNative lib = (LibUsbNative) Native.loadLibrary("usb-1.0", LibUsbNative.class);
        final PointerByReference contextRef = new PointerByReference();
        System.out.println(lib.libusb_init(contextRef));
        final Pointer context = contextRef.getValue();
        lib.libusb_set_debug(context, 4);


        final PointerByReference listRef = new PointerByReference();
        final int size = lib.libusb_get_device_list(context, listRef);
        final Pointer list = listRef.getValue();
        final Pointer[] devices = list.getPointerArray(0, size);
        for (final Pointer device: devices) {
            System.out.print("bus " + lib.libusb_get_bus_number(device));
            System.out.println(" port " + lib.libusb_get_port_number(device));
            final byte[] ports = new byte[16];
            final int len = lib.libusb_get_port_numbers(device, ports, 16);
            System.out.print("Port numbers: ");
            for (int i = 0; i < len; ++i) {
                System.out.print(ports[i] + " ");
            }
            final Pointer parent = lib.libusb_get_parent(device);
            System.out.println("Parent: " + parent);
            System.out.println("Address: " + lib.libusb_get_device_address(device));
            System.out.println("Speed: " + lib.libusb_get_device_speed(device));
            System.out.println("max packet size: " + lib.libusb_get_max_packet_size(device, (byte) 1));
            System.out.println("max iso packet size: " + lib.libusb_get_max_iso_packet_size(device, (byte) 1));

            final Pointer ref2 = lib.libusb_ref_device(device);
            lib.libusb_unref_device(ref2);

            final PointerByReference handleRef = new PointerByReference();
            lib.libusb_open(device,  handleRef);
            final Pointer handle = handleRef.getValue();
            System.out.println("Handle: " + handle);
            if (handle != null) {
                System.out.println("Got one!");

                lib.libusb_close(handle);
                System.out.println(lib.libusb_get_device(handle));
                System.out.println(device);

                final IntByReference configRef = new IntByReference();
    //            System.out.println(lib.libusb_get_configuration(handle,  configRef));
      //          System.out.println(configRef.getValue());

//                System.out.println(lib.libusb_set_configuration(handle, 1));
  //              System.out.println(lib.libusb_claim_interface(handle, 1));

                final DeviceDescriptor descriptor = new DeviceDescriptor();
                System.out.println(lib.libusb_get_device_descriptor(device, descriptor));
                System.out.println(Integer.toHexString(descriptor.idVendor));
                System.out.println(Integer.toHexString(descriptor.idProduct));

                final PointerByReference configDescriptorRef = new PointerByReference();
                System.out.println(configDescriptorRef.getValue());
                System.out.println(lib.libusb_get_active_config_descriptor(device,  configDescriptorRef));
                final ConfigDescriptor configDescriptor = new ConfigDescriptor(configDescriptorRef.getValue());
                System.out.println(configDescriptorRef.getValue());
                System.out.println(configDescriptor.bNumInterfaces);
                System.out.println(configDescriptor.bDescriptorType);
                System.out.println(configDescriptor.wTotalLength);
                lib.libusb_free_config_descriptor(configDescriptor);

                final PointerByReference configDescriptorRef2 = new PointerByReference();
                System.out.println(configDescriptorRef2.getValue());
                System.out.println(lib.libusb_get_config_descriptor(device, (byte) 0, configDescriptorRef2));
                final ConfigDescriptor configDescriptor2 = new ConfigDescriptor(configDescriptorRef2.getValue());
                System.out.println(configDescriptorRef2.getValue());
                System.out.println(configDescriptor2.bNumInterfaces);
                System.out.println(configDescriptor2.bDescriptorType);
                System.out.println(configDescriptor2.wTotalLength);
                System.out.println(configDescriptor2.iface);
                lib.libusb_free_config_descriptor(configDescriptor2);

                break;
            }


            System.out.println();
        }
        lib.libusb_free_device_list(list, 1);


//        System.out.println(lib.libusb_open_device_with_vid_pid(context, (short) 0x16c0, (short) 0x05dc));

        lib.libusb_exit(context);
        System.out.println("Exit");
    }

}
