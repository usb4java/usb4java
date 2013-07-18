#!/bin/sh
#
# Builds libusb4java for 64 bit x86 linux

set -e

. $(dirname $0)/common.sh

OS="linux"
ARCH="x86_64"
HOST="$ARCH-$OS-gnu"
CFLAGS="-m64 -O2"
UDEV_SUPPORT="yes"
#USB4JAVA_CFLAGS="-Wl,--wrap=memcpy -DWRAP_MEMCPY"

build
