#!/bin/sh
#
# Builds libusb4java for 64 bit x86 linux

set -e

. $(dirname $0)/common.sh

OS="linux"
ARCH="x86_64"
HOST="$ARCH-$OS-gnu"
CFLAGS="-m64"
LIBUSB_CONFIG="--disable-shared --disable-udev"
USB4JAVA_LIBS="-lrt"

build
