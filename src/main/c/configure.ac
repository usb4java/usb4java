AC_PREREQ([2.65])
AC_INIT([libusb4java], [0.1.0], [k@ailis.de])
AM_INIT_AUTOMAKE(foreign -Wall -Werror)
AC_CONFIG_MACRO_DIR([m4])
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_SRCDIR([src/usb4java.h])

CPPFLAGS="\
  -I/System/Library/Frameworks/JavaVM.framework/Headers \
  -I/opt/local/include/libusb-legacy \
"

LDFLAGS="\
  -L/opt/local/lib/libusb-legacy \
"

AC_LANG_C
AC_PROG_CC
AM_PROG_LIBTOOL
AC_CHECK_LIBUSB
AC_CHECK_JAVA

AC_CONFIG_FILES(
  Makefile 
  src/Makefile  
)
AC_OUTPUT
