#!/bin/sh
#
# Builds libusb4java for 64 bit windows

set -e

. $(dirname $0)/common.sh

OS="windows"
ARCH="x86_64"
HOST="$ARCH-w64-mingw32"
CFLAGS="-m64 -Wl,--add-stdcall-alias"

build
