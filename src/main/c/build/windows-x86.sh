#!/bin/sh
#
# Builds libusb4java for 32 bit windows

set -e

. $(dirname $0)/common.sh

OS="windows"
ARCH="x86"
HOST="i686-w64-mingw32"
CFLAGS="-m32 -O2 -Wl,--add-stdcall-alias"

build
