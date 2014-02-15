AC_PREREQ([2.61])
AC_INIT([libusb4java], [1.2.0], [usb4java@googlegroups.com])
AM_INIT_AUTOMAKE([foreign -Wall -Werror])
m4_ifdef([AM_SILENT_RULES], [AM_SILENT_RULES(yes)])
AC_CONFIG_MACRO_DIR([m4])
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_SRCDIR([src/usb4java.h])

AC_LANG_C
AC_PROG_CC
AC_PROG_CC_STDC
AM_PROG_LIBTOOL
m4_ifdef([AM_PROG_AR], [AM_PROG_AR])
AC_CHECK_JAVA
AC_CHECK_SIZEOF([void *])
PKG_CHECK_MODULES([LIBUSB], [libusb-1.0])

AC_CONFIG_FILES([
  Makefile 
  src/Makefile  
])
AC_OUTPUT
