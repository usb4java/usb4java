#!/bin/sh
#
# Builds libusb4java for 64 bit linux.
# Must be executed on 64 bit linux machine and must have libusb-dev installed.

set -e
cd $(dirname $0)/..

OS=linux
ARCH=x86_64
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
./configure --prefix=/ CFLAGS="-m64"
make clean install-strip DESTDIR=$TMPDIR
mkdir -p $DISTDIR
cp -faL $TMPDIR/lib/libusb4java.so $DISTDIR/
chmod -x $DISTDIR/libusb4java.so
rm -rf $TMPDIR
