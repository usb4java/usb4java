Build scripts
=============

This directory contains various build scripts to automate the process of 
generating the native library for usb4java. The `common.sh` file contains
the generic build instructions while all the other scripts just configures
the build and runs it.

The created libraries are placed inside the `/src/main/resources` folder of
the Java project. 

The following sections describe the requirements to build the library for
the various platforms.


all
---

For all platforms you must install a current Oracle JDK or OpenJDK and
configure the `JAVA_HOME` environment variable properly. Ensure that the file
`$JAVA_HOME/include/jni.h` exists.

The common build script uses `curl` to download the `libusb` sources. So make
sure `curl` is installed.


Build for linux-x86
-------------------

On a 32 bit x86 Linux host you only need the `gcc` package.

On a 64 bit x86 Linux host you need the `gcc-multilib` package.
 
 
Build for linux-x86_64
----------------------

On a 64 bit x868 Linux host you only need the `gcc` package.


Build for linux-arm
-------------------

On a Linux host you need the `gcc-arm-linux-gnueabi` package.

 
Build for windows-x86 and windows-x86_64
----------------------------------------

On a Linux host you need the `mingw-w64` package.


Build for osx-x86 and osx-x86_64
--------------------------------

No idea how to cross-compile it. You need a real OS X machine. Install the
latest XCode and the XCode command line tools and the build should work.
 