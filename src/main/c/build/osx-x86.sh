#!/bin/sh
#
# Builds libusb4java for 32 bit x86 OS X
# Must run natively on a OS X machine

set -e

. $(dirname $0)/common.sh

OS="osx"
ARCH="x86"
HOST="i686-apple-darwin"
CFLAGS="-arch i686 -O2"
USB4JAVA_LIBS="-lobjc -Wl,-framework,IOKit -Wl,-framework,CoreFoundation"

build
