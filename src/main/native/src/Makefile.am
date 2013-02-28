lib_LTLIBRARIES = libusb4java.la
libusb4java_la_CFLAGS = -Wall -Werror $(LIBUSB_CFLAGS)
libusb4java_la_LIBADD = $(LIBUSB_LIBS)
libusb4java_la_LDFLAGS = -version-info 1:0:0 -no-undefined
EXTRA_DIST = *.h
libusb4java_la_SOURCES = \
    LibUSB.c \
    Version.c \
    Context.c \
    Device.c \
    DeviceHandle.c \
    DeviceList.c \
    DeviceDescriptor.c
