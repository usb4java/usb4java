#!/bin/sh
#
# Builds libusb4java for 32 bit linux.
# If running on 32 bit linux you need libusb-dev.
# If running on 64 bit linux you need ia32-libs-dev and libc6-dev-i386.

set -e
cd $(dirname $0)/..

OS=linux
ARCH=x86
TMPDIR=$(pwd)/tmp
DISTDIR=$(pwd)/../resources/${OS}-${ARCH}

# Clean up
rm -rf $TMPDIR
rm -rf $DISTDIR

# Build autoconf stuff if needed
if [ ! -e configure ]
then
    make -f Makefile.scm
fi

# Build libusb4java
./configure --prefix=/ CFLAGS="-m32"
make clean install-strip DESTDIR=$TMPDIR
mkdir -p $DISTDIR
cp -faL $TMPDIR/lib/libusb4java.so $DISTDIR/
rm -rf $TMPDIR
