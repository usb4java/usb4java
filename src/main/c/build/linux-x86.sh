#!/bin/sh
#
# Builds libusb4java for 32 bit x86 linux

set -e

. $(dirname $0)/common.sh

OS="linux"
ARCH="x86"
HOST="$ARCH-$OS-gnu"
CFLAGS="-m32"
LIBUSB_CONFIG="--disable-shared"
USB4JAVA_LIBS="-lrt"

build
