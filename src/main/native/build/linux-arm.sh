#!/bin/sh
#
# Builds libusb4java for 32 bit arm. libusb-dev must be installed.
# This script is meant to be run directly on a linux-arm machine
# (Like the Raspberry)

set -e
cd $(dirname $0)/..

OS=linux
ARCH=arm
TMPDIR=$(pwd)/tmp
DISTDIR=$(pwd)/../resources/de/ailis/usb4java/jni/${OS}-${ARCH}

# Clean up
rm -rf $TMPDIR
rm -rf $DISTDIR

# Build autoconf stuff if needed
if [ ! -e configure ]
then
    make -f Makefile.scm
fi

# Build libusb4java
./configure --prefix=/
make clean install-strip DESTDIR=$TMPDIR
mkdir -p $DISTDIR
cp -faL $TMPDIR/lib/libusb4java.so $DISTDIR/
chmod -x $DISTDIR/libusb4java.so
rm -rf $TMPDIR
