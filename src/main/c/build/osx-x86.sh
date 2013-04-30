#!/bin/sh
#
# Builds libusb4java for 32 bit x86 OS X
# Must run natively on a OS X machine

set -e

. $(dirname $0)/common.sh

OS="osx"
ARCH="x86"
CFLAGS="-arch i686"
LIBUSB_CONFIG="--disable-shared"
USB4JAVA_LIBS="-lobjc -Wl,-framework,IOKit -Wl,-framework,CoreFoundation"
LIBUSB="libusb"

build
