AC_DEFUN([AC_CHECK_JAVA],[
  AC_ARG_WITH(
    java-home,
    [  --with-java-home=DIR    Set Java home directory ],
    [
      JAVA_HOME=`echo $withval`
    ]
  )
  CPPFLAGS="$CPPFLAGS -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I$JAVA_HOME/include/win32 -I$JAVA_HOME/include/darwin"
  AC_CHECK_HEADERS(jni.h,,echo "ERROR: jni.h not found. JAVA_HOME is $JAVA_HOME. Use --with-java-home option to specify an other Java home directory."; exit 1;)
])
