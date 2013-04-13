#!/bin/sh
#
# Builds libusb4java for 32 bit linux.
#
# If running on 32 bit linux you just need libusb-1.0-0-dev.
#
# If running on 64 bit linux you need ia32-libs-dev and libc6-dev-i386
# or gcc-multilib and libusb-1.0-0:i386 or something like that on newer 
# systems. Depending on your multilib installation it may be required to 
# create a manual symlink libusb.so in /lib/i386-linux-gnu or otherwise
# compilation will fail because the compiler can't find the 32 bit
# library.

set -e
cd "$(dirname $0)/.."

OS="linux"
ARCH="x86"
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
CFLAGS="-m32" ./configure "--prefix=$TMPDIR" --disable-shared --with-pic
make
make install

# Build autoconf stuff of usb4java if needed
cd "$TMPDIR/.."
if [ ! -e configure ]
then
    make -f Makefile.scm
fi

# Build libusb4java
PKG_CONFIG_PATH="$TMPDIR/lib/pkgconfig" CFLAGS="-m32" ./configure --prefix=/
make clean install-strip DESTDIR="$TMPDIR"
mkdir -p "$DISTDIR"
cp -faL "$TMPDIR/lib/libusb4java.so" "$DISTDIR/"
chmod -x "$DISTDIR/libusb4java.so"

# Cleanup
rm -rf $TMPDIR
