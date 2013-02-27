AC_PREREQ([2.61])
AC_INIT([libusb4java], [1.0.0], [k@ailis.de])
AM_INIT_AUTOMAKE(foreign -Wall -Werror)
AC_CONFIG_MACRO_DIR([m4])
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_SRCDIR([src/usb4java.h])

AC_LANG_C
AC_PROG_CC
AM_PROG_LIBTOOL
AC_CHECK_JAVA
PKG_CHECK_MODULES(LIBUSB, libusb-1.0)

AC_CONFIG_FILES(
  Makefile 
  src/Makefile  
)
AC_OUTPUT
