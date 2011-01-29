#!/bin/sh
#
# Cross-compiles libusb4java for 64 bit windows using mingw.
# libusb-dev is needed.

set -e
cd $(dirname $0)/..

OS=windows
ARCH=x86
TMPDIR=$(pwd)/tmp
DISTDIR=$(pwd)/../assembly/${OS}-${ARCH}

# Clean up
rm -rf $TMPDIR
rm -rf $DISTDIR

# Download and unpack libusb-win32
LIBUSBWIN32_VERSION=1.2.2.0
mkdir -p $TMPDIR
wget -O $TMPDIR/libusb-win32.zip "http://downloads.sourceforge.net/project/libusb-win32/libusb-win32-releases/$LIBUSBWIN32_VERSION/libusb-win32-bin-$LIBUSBWIN32_VERSION.zip"
cd $TMPDIR
unzip libusb-win32.zip
INCLUDES=$TMPDIR/libusb-win32-bin-$LIBUSBWIN32_VERSION/include
LIBS=$TMPDIR/libusb-win32-bin-$LIBUSBWIN32_VERSION/lib/gcc
BINS=$TMPDIR/libusb-win32-bin-$LIBUSBWIN32_VERSION/bin/x86
cd ..

# Create autoconf stuff if needed
if [ ! -e configure ]
then
    make -f Makefile.scm
fi

# Build libusb4java
./configure \
    --prefix=/ \
    --host=i586-mingw32msvc \
    --with-libusb-includes=$INCLUDES \
    --with-libusb-libs=$LIBS,$BINS
make clean install-strip DESTDIR=$TMPDIR
mkdir -p $DISTDIR
cp -faL $TMPDIR/bin/libusb4java-0.dll $DISTDIR/libusb4java.dll
cp -faL $BINS/libusb0_x86.dll $DISTDIR/libusb0.dll
rm -rf $TMPDIR
