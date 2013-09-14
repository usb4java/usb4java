lib_LTLIBRARIES = libusb4java.la
libusb4java_la_CFLAGS = -pedantic -Wall -Wextra -Wformat=2 -Winit-self -Wunused -Wno-unused-parameter -Wuninitialized -Wundef -Wshadow -Wbad-function-cast -Wcast-qual -Wcast-align -Wwrite-strings -Wconversion -Wstrict-prototypes -Wredundant-decls -Wnested-externs $(LIBUSB_CFLAGS)
libusb4java_la_LIBADD = $(LIBUSB_LIBS)
libusb4java_la_LDFLAGS = -version-info 1:0:0 -no-undefined
EXTRA_DIST = *.h
libusb4java_la_SOURCES = \
    wrappers.c \
    usb4java.c \
    LibUsb.c \
    Version.c \
    Context.c \
    Device.c \
    DeviceHandle.c \
    DeviceList.c \
    DeviceDescriptor.c \
    ConfigDescriptor.c \
    Interface.c \
    InterfaceDescriptor.c \
    EndpointDescriptor.c \
    IsoPacketDescriptor.c \
    Transfer.c \
    SsEndpointCompanionDescriptor.c \
    BosDescriptor.c \
    BosDevCapabilityDescriptor.c \
    Usb20ExtensionDescriptor.c \
    SsUsbDeviceCapabilityDescriptor.c \
    ContainerIdDescriptor.c \
    HotplugCallbackHandle.c
