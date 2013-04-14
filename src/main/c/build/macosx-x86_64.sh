#!/bin/sh
#
# Builds libusb4java for 64 bit x86 Mac OS X
# Must run natively on a Mac OS X machine

set -e

. $(dirname $0)/common.sh

OS="macosx"
ARCH="x86_64"
CFLAGS="-arch x86_64"
LIBUSBX_CONFIG="--disable-shared"
USB4JAVA_LIBS="-lobjc -Wl,-framework,IOKit -Wl,-framework,CoreFoundation"

build
