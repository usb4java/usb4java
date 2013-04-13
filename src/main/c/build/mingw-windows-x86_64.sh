#!/bin/sh
#
# Compile libusb4java for 32 bit windows with mingw on Windows
# or cross-compile it with mingw on Linux. 
#
# For cross-compiling on Linux mingw-w64-dev must be installed.

set -e
cd $(dirname $0)/..

OS="windows"
ARCH="x86_64"
LIBUSBX_VERSION="1.0.14"
LIBUSBX_ARCHIVE="libusbx-$LIBUSBX_VERSION.tar.bz2"
TMPDIR="$(pwd)/tmp"
DOWNLOADS="$(pwd)/downloads"
DISTDIR="$(pwd)/../resources/de/ailis/usb4java/libusb/${OS}-${ARCH}"

# Clean up
rm -rf "$TMPDIR"
rm -rf "$DISTDIR"

# Download libusbx if necessary
mkdir -p "$DOWNLOADS"
if [ ! -e "$DOWNLOADS/$LIBUSBX_ARCHIVE" ]
then
    wget -O "$DOWNLOADS/$LIBUSBX_ARCHIVE" "http://downloads.sourceforge.net/project/libusbx/releases/$LIBUSBX_VERSION/source/$LIBUSBX_ARCHIVE"
fi

# Unpack and compile libusbx
mkdir -p "$TMPDIR"
cd "$TMPDIR"
tar xfj "$DOWNLOADS/$LIBUSBX_ARCHIVE"
cd "libusbx-$LIBUSBX_VERSION"
./configure \
    --prefix="$TMPDIR" \
    --with-pic \
    --host=x86_64-w64-mingw32
make
make install-strip

# Build autoconf stuff of usb4java if needed
cd "$TMPDIR/.."
if [ ! -e configure ]
then
    make -f Makefile.scm
fi

# Build libusb4java
PKG_CONFIG_PATH="$TMPDIR/lib/pkgconfig" ./configure \
    --prefix="$TMPDIR" \
    --host=x86_64-w64-mingw32
make clean install-strip
mkdir -p "$DISTDIR"
cp -faL "$TMPDIR/bin/libusb4java-1.dll" "$DISTDIR/libusb4java.dll"
cp -faL "$TMPDIR/bin/libusb-1.0.dll" "$DISTDIR"
chmod -x "$DISTDIR/"*.dll

# Cleanup
rm -rf "$TMPDIR"
