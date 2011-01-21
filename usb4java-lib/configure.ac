AC_INIT(configure.ac)
AM_INIT_AUTOMAKE(libusb4java, 1.0.0)
AM_CONFIG_HEADER(config.h)
AC_CONFIG_MACRO_DIR([m4])

AC_LANG_C
AC_PROG_CC
AM_PROG_LIBTOOL
AC_CHECK_JAVA
AC_CHECK_HEADERS(usb.h,,echo "ERROR: usb.h not found. Please install libusb"; exit 1;)

AC_CONFIG_FILES(
  Makefile 
  src/Makefile  
  src/main/Makefile
  src/main/c/Makefile
)
AC_OUTPUT
