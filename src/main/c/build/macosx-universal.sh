#!/bin/sh
#
# Builds universal libusb4java for Mac OS X (x86_64, x86, ppc)

set -e
cd $(dirname $0)/..

OS=macosx
ARCH=universal
TMPDIR=$(pwd)/tmp
DISTDIR=$(pwd)/../assembly/${OS}-${ARCH}

# Clean up
rm -rf $TMPDIR
rm -rf $DISTDIR

# Download and unpack libusb
LIBUSB_VERSION=0.1.12
URL="http://downloads.sourceforge.net/project/libusb/libusb-0.1%20%28LEGACY%29/$LIBUSB_VERSION/libusb-$LIBUSB_VERSION.tar.gz"
mkdir -p $TMPDIR
wget -O $TMPDIR/libusb.tar.gz "$URL"
cd $TMPDIR
tar xvfz libusb.tar.gz

# Compile libusb
cd libusb-$LIBUSB_VERSION
cat << EOF | patch || exit 1
--- ltmain.sh.orig	2009-10-10 17:26:28.000000000 +0200
+++ ltmain.sh	2009-10-10 17:25:40.000000000 +0200
@@ -917,7 +917,7 @@
     old_convenience=
     deplibs=
     old_deplibs=
-    compiler_flags=
+    compiler_flags=\$CFLAGS
     linker_flags=
     dllsearchpath=
     lib_search_path=`pwd`
EOF
export CFLAGS="-w -arch i386 -arch x86_64 -arch ppc"
./configure --prefix=/usr --disable-dependency-tracking --disable-build-docs
make install-strip DESTDIR=$TMPDIR SUBDIRS=
INCLUDES=$TMPDIR/usr/include
LIBS=$TMPDIR/usr/lib
cd ../..

# Create autoconf stuff if needed
if [ ! -e configure ]
then
    make -f Makefile.scm
fi

# Build libusb4java
./configure \
    --prefix=/usr \
    --with-libusb-includes=$INCLUDES \
    --with-libusb-libs=$LIBS \
    --disable-dependency-tracking \
    CFLAGS="-arch i386 -arch x86_64 -arch ppc"
make clean install-strip DESTDIR=$TMPDIR
mkdir -p $DISTDIR
cp -faL $TMPDIR/usr/lib/libusb4java.dylib $DISTDIR/
cp -faL $LIBS/libusb.dylib $DISTDIR/
rm -rf $TMPDIR

# Remove paths from libraries
install_name_tool -id @executable_path/libusb.dylib $DISTDIR/libusb.dylib
install_name_tool -id @executable_path/libusb4java.dylib $DISTDIR/libusb4java.dylib
install_name_tool -change /usr/lib/libusb-0.1.4.dylib @executable_path/libusb.dylib $DISTDIR/libusb4java.dylib
