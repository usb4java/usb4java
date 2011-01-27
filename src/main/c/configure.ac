AC_INIT(configure.ac)
AM_INIT_AUTOMAKE(libusb4java, 0.1.12-1)
AM_CONFIG_HEADER(config.h)
AC_CONFIG_MACRO_DIR([m4])

AC_LANG_C
AC_PROG_CC
AM_PROG_LIBTOOL
AC_CHECK_JAVA
AC_CHECK_HEADERS(usb.h,,echo "ERROR: usb.h not found. Please install libusb"; exit 1;)
AC_SEARCH_LIBS(usb_init,usb usb0,,echo "ERROR: libusb not found. Please install libusb"; exit 1;)

AC_CONFIG_FILES(
  Makefile 
  src/Makefile  
)
AC_OUTPUT
