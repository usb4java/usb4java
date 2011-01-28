AC_INIT(configure.ac)
AM_INIT_AUTOMAKE(libusb4java, 0.1.12-1)
AM_CONFIG_HEADER(config.h)
AC_CONFIG_MACRO_DIR([m4])

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
