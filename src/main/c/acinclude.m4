AC_DEFUN([AC_CHECK_LIBUSB],[
  AC_ARG_WITH(
    libusb,
    [  --with-libusb=DIR       Set path to libusb prefix],
    [
      extra_includes=$(echo $withval | sed s/','/'\/include -I'/g)
      extra_libs=$(echo $withval | sed s/','/'\/lib -L'/g)
      CPPFLAGS="-I$withval/include $CPPFLAGS"
      LDFLAGS="-L$withval/lib $LDFLAGS"
    ]
  )
  AC_ARG_WITH(
    libusb-libs,
    [  --with-libusb-libs=DIR  Set path to libusb library files],
    [
      extra_libs=$(echo $withval | sed s/','/' -L'/g)
      LDFLAGS="-L$extra_libs $LDFLAGS"
    ]
  )
  AC_ARG_WITH(
    libusb-includes,
    [  --with-libusb-includes=DIR
                          Set path to libusb include files],
    [
      extra_includes=$(echo $withval | sed s/','/' -I'/g)
      CPPFLAGS="-I$extra_includes $CPPFLAGS"
    ]
  )
  AC_CHECK_HEADERS(usb.h,,echo "ERROR: usb.h not found."; exit 1;)
  AC_SEARCH_LIBS(usb_init,usb usb0 usb-legacy,,echo "ERROR: libusb not found."; exit 1;)
])


AC_DEFUN([AC_CHECK_JAVA],[
  AC_ARG_WITH(
    java-home,
    [  --with-java-home=DIR    Set Java home directory ],
    [
      JAVA_HOME=`echo $withval`
    ]
  )
  CPPFLAGS="$CPPFLAGS -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I$JAVA_HOME/include/win32"
  AC_CHECK_HEADERS(jni.h,,echo "ERROR: jni.h not found. JAVA_HOME is $JAVA_HOME. Use --with-java-home option to specify an other Java home directory."; exit 1;)
])
