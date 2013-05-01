#!/bin/sh
#
# Builds libusb4java for 32 bit ARM linux (Raspberry Pi)

set -e

. $(dirname $0)/common.sh

OS="linux"
ARCH="arm"
HOST="$ARCH-$OS-gnueabi"
LIBUSB_CONFIG="--disable-shared"
USB4JAVA_LIBS="-lrt"

build
