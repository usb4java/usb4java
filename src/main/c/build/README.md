Build scripts
=============

This directory contains various build scripts to automate the process of
generating the native library as they are shipped with usb4java.  The
`common.sh` file contains the generic build instructions while all the other
scripts just configures the build and runs it.

The created libraries are placed inside the `/src/main/resources` folder of
the Java project. 

The following sections describe the requirements to build the library for
the various platforms.


all
---

For all platforms you must install a current Oracle JDK or OpenJDK and
configure the `JAVA_HOME` environment variable properly. Ensure that the file
`$JAVA_HOME/include/jni.h` exists.

The common build script needs `curl` or `wget` to download the libusb
sources. So make sure one of them is installed.


Build for linux-x86
-------------------

On a 32 bit x86 Linux host you only need the `gcc` package.

On a 64 bit x86 Linux host you need the `gcc-multilib` package to be able to
cross-compile for 32 bit linux.
 
 
Build for linux-x86_64
----------------------

On a 64 bit x868 Linux host you only need the `gcc` package.


Build for linux-arm
-------------------

On a Linux host you need the `gcc-arm-linux-gnueabi` package to be able to
cross-compile for the ARM platform.

 
Build for windows-x86 and windows-x86_64
----------------------------------------

The build script is configured to cross-compile the windows binaries on
Linux.  You need to install the `mingw-w64` package for it.  But
unfortunately only the 64 bit library is actually working.  The 32 bit
library just crashes when used.  I have no idea why, so for now you have to
compile the 32 bit windows library on a 32 bit windows machine natively. 
Here are some step by step instructions to do this:

1. Download and execute the latest MingW Installer (mingw-get-inst) from the
   [MinGW Project](http://www.mingw.org/).
2. During installation additionally select the `MSYS Basic System` component.
3. Start the MinGW shell.
4. Run the command `mingw-get install autoconf automake libtool msys-wget`
5. Download the latest [pkg-config](http://www.freedesktop.org/wiki/Software/pkg-config). 
   You can do it in the MinGW shell like this:

        wget http://pkgconfig.freedesktop.org/releases/pkg-config-0.28.tar.gz

6. Unpack, compile and install pkg-config:

        tar xvfz pkg-config-0.28.tar.gz
        cd pkg-config-0.28
        ./configure --with-internal-glib CFLAGS=-march=i486
        make
        make install

7. Go to the directory of the usb4java JNI wrapper source code. If usb4java
   is in `c:\projects\usb4java` then you do it like this:

        cd /c/projects/usb4java/src/main/c

8. Run the build script:

        build/windows-x86.sh


Build for osx-x86 and osx-x86_64
--------------------------------

No idea how to cross-compile it. You need a real OS X machine. Install the
latest XCode and the XCode command line tools and the build should work.
 