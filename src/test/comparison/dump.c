#include <usb.h>
#include <stdio.h>

int level = 0;

void indent()
{
    int i;

    for (i = 0; i < level; i++) printf("  ");
}

void dump_device_descriptor(struct usb_device_descriptor descriptor)
{
    indent(); printf("bLength: 0x%02x\n", descriptor.bLength);
    indent(); printf("bDescriptorType: 0x%02x\n", descriptor.bDescriptorType);
    indent(); printf("bcdUSB: 0x%04x\n", descriptor.bcdUSB);
    indent(); printf("bDeviceClass: 0x%02x\n", descriptor.bDeviceClass);
    indent(); printf("bDeviceSubClass: 0x%02x\n", descriptor.bDeviceSubClass);
    indent(); printf("bDeviceProtocol: 0x%02x\n", descriptor.bDeviceProtocol);
    indent(); printf("bMaxPacketSize0: 0x%02x\n", descriptor.bMaxPacketSize0);
    indent(); printf("idVendor: 0x%04x\n", descriptor.idVendor);
    indent(); printf("idProduct: 0x%04x\n", descriptor.idProduct);
    indent(); printf("bcdDevice: 0x%04x\n", descriptor.bcdDevice);
    indent(); printf("iManufacturer: 0x%02x\n", descriptor.iManufacturer);
    indent(); printf("iProduct: 0x%02x\n", descriptor.iProduct);
    indent(); printf("iSerialNumber: 0x%02x\n", descriptor.iSerialNumber);
    indent(); printf("bNumConfigurations: 0x%02x\n", descriptor.bNumConfigurations);
}

void dump_endpoint_descriptor(struct usb_endpoint_descriptor descriptor)
{
    int i;

    indent(); printf("Endpoint\n");
    level++;
    indent(); printf("bLength: 0x%02x\n", descriptor.bLength);
    indent(); printf("bDescriptorType: 0x%02x\n", descriptor.bDescriptorType);
    indent(); printf("bEndpointAddress: 0x%02x\n", descriptor.bEndpointAddress);
    indent(); printf("bmAttributes: 0x%02x\n", descriptor.bmAttributes);
    indent(); printf("wMaxPacketSize: 0x%04x\n", descriptor.wMaxPacketSize);
    indent(); printf("bInterval: 0x%02x\n", descriptor.bInterval);
    indent(); printf("bRefresh: 0x%02x\n", descriptor.bRefresh);
    indent(); printf("bSynchAddress: 0x%02x\n", descriptor.bSynchAddress);
    indent(); printf("extralen: 0x%04x\n", descriptor.extralen);
    indent(); printf("extra:");
    for (i = 0; i < descriptor.extralen; i++)
        printf(" %02x", descriptor.extra[i]);
    printf("\n");
    level--;
}

void dump_interface_descriptor(struct usb_interface_descriptor descriptor)
{
    int i;

    indent(); printf("Interface descriptor:\n");
    level++;
    indent(); printf("bLength: 0x%02x\n", descriptor.bLength);
    indent(); printf("bDescriptorType: 0x%02x\n", descriptor.bDescriptorType);
    indent(); printf("bInterfaceNumber: 0x%02x\n", descriptor.bInterfaceNumber);
    indent(); printf("bAlternateSetting: 0x%02x\n", descriptor.bAlternateSetting);
    indent(); printf("bNumEndpoints: 0x%02x\n", descriptor.bNumEndpoints);
    indent(); printf("bInterfaceClass: 0x%02x\n", descriptor.bInterfaceClass);
    indent(); printf("bInterfaceSubClass: 0x%02x\n", descriptor.bInterfaceSubClass);
    indent(); printf("bInterfaceProtocol: 0x%02x\n", descriptor.bInterfaceProtocol);
    indent(); printf("iInterface: 0x%02x\n", descriptor.iInterface);
    indent(); printf("extralen: 0x%04x\n", descriptor.extralen);
    indent(); printf("extra:");
    for (i = 0; i < descriptor.extralen; i++)
        printf(" %02x", descriptor.extra[i]);
    printf("\n");
    indent(); printf("Endpoints:\n");
    level++;
    for (i = 0; i < descriptor.bNumEndpoints; i++)
        dump_endpoint_descriptor(descriptor.endpoint[i]);
    level--;
    level--;
}

void dump_interface(struct usb_interface iface)
{
    int i;

    indent(); printf("Interface:\n");
    level++;
    indent(); printf("num_altsetting: 0x%04x\n", iface.num_altsetting);
    indent(); printf("altsetting:\n");
    level++;
    for (i = 0; i < iface.num_altsetting; i++)
        dump_interface_descriptor(iface.altsetting[i]);
    level--;
    level--;
}

void dump_config_descriptor(struct usb_config_descriptor config)
{
    int i, max;

    indent(); printf("Config Descriptor:\n");
    level++;
    indent(); printf("bLength: 0x%02x\n", config.bLength);
    indent(); printf("bDescriptorType: 0x%02x\n", config.bDescriptorType);
    indent(); printf("wTotalLength: 0x%04x\n", config.wTotalLength);
    indent(); printf("bNumInterfaces: 0x%02x\n", config.bNumInterfaces);
    indent(); printf("bConfigurationValue: 0x%02x\n", config.bConfigurationValue);
    indent(); printf("iConfiguration: 0x%02x\n", config.iConfiguration);
    indent(); printf("bmAttributes: 0x%02x\n", config.bmAttributes);
    indent(); printf("MaxPower: 0x%02x\n", config.MaxPower);
    indent(); printf("extralen: 0x%04x\n", config.extralen);
    indent(); printf("extra:");
    for (i = 0; i < config.extralen; i++)
        printf(" %02x", config.extra[i]);
    printf("\n");
    indent(); printf("Interfaces:\n");
    level++;
    for (i = 0; i < config.bNumInterfaces; i++)
        dump_interface(config.interface[i]);
    level--;
    level--;
}

void dump_device(struct usb_device *device)
{
    int i, buffer[256];

    indent(); printf("Device:\n");
    level++;
    indent(); printf("filename: %s\n", device->filename);
    indent(); printf("bus: %s\n", device->bus->dirname);
    indent(); printf("devnum: %i\n", device->devnum);
    indent(); printf("num_children: %i\n", device->num_children);
    indent(); printf("descriptor:\n");
    level++;
    dump_device_descriptor(device->descriptor);
    level--;
    struct usb_handle *handle = usb_open(device);
    i = usb_get_string_simple(handle, device->descriptor.iManufacturer, buffer, 255);
    indent(); printf("Manufacturer: %s\n", i > 0 ? buffer : "Unknown");
    i = usb_get_string_simple(handle, device->descriptor.iProduct, buffer, 255);
    indent(); printf("Product: %s\n", i > 0 ? buffer : "Unknown");
    i = usb_get_string_simple(handle, device->descriptor.iSerialNumber, buffer, 255);
    indent(); printf("Serial: %s\n", i > 0 ? buffer : "Unknown");
    usb_close(handle);
    indent(); printf("config descriptors:\n");
    level++;
    int max = device->descriptor.bNumConfigurations;
    if (max == 0)
    {
        indent(); printf("None\n");
    }
    else
    {
        for (i = 0; i < max; i++)
        {
            dump_config_descriptor(device->config[i]);
        }
    }
    level--;
    indent(); printf("children:\n");
    level++;
    if (device->num_children == 0)
    {
        indent(); printf("None\n");
    }
    else
    {
        for (i = 0; i< device->num_children; i++)
            dump_device(device->children[i]);
    }
    level--;
    level--;
}

int main(int argc, char *argv[])
{
    usb_init();
    int bus_count = usb_find_busses();
    printf("Found %i busses\n", bus_count);
    int dev_count = usb_find_devices();
    printf("Found %i devices\n", dev_count);

    struct usb_bus *bus = usb_get_busses();
    while (bus)
    {
        printf("Bus:\n");
        level++;
        indent(); printf("dirname: %s\n", bus->dirname);
        indent(); printf("location: %i\n", bus->location);
        indent(); printf("Root device: ");
        level++;
        if (bus->root_dev)
            printf("%s/%s\n", bus->root_dev->bus->dirname,
                bus->root_dev->filename);
        else
            printf("None\n");
        level--;
        indent(); printf("devices: \n");
        level++;
        struct usb_device *device = bus->devices;
        while (device)
        {
            dump_device(device);
            device = device->next;
        }
        level--;
        level--;

        printf("\n");
        bus = bus->next;
    }
}
