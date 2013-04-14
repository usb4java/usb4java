cd "$(dirname $0)"
SRCDIR="$(pwd)/.."
TMPDIR="$SRCDIR/tmp"
DOWNLOADS="$SRCDIR/downloads"
LIBUSBX_VERSION="1.0.14"
LIBUSBX_ARCHIVE="libusbx-$LIBUSBX_VERSION.tar.bz2"
LIBUSBX_FILE="$DOWNLOADS/$LIBUSBX_ARCHIVE"
LIBUSBX_URL="http://downloads.sf.net/project/libusbx/releases/$LIBUSBX_VERSION/source/$LIBUSBX_ARCHIVE"

build()
{
    DISTDIR="$SRCDIR/../resources/de/ailis/usb4java/libusb/$OS-$ARCH"
    
    # Clean up
    rm -rf "$TMPDIR"
    rm -rf "$DISTDIR"

    # Download libusbx if necessary
    mkdir -p "$DOWNLOADS"
    if [ ! -e "$LIBUSBX_FILE" ]
    then
       curl -L -o "$LIBUSBX_FILE" "$LIBUSBX_URL"
    fi

    # Unpack and compile libusbx
    mkdir -p "$TMPDIR"
    cd "$TMPDIR"
    tar xfj "$DOWNLOADS/$LIBUSBX_ARCHIVE"
    cd "libusbx-$LIBUSBX_VERSION"
    CFLAGS="$CFLAGS $LIBUSBX_CFLAGS" \
    ./configure --prefix="$TMPDIR" --host="$HOST" --with-pic $LIBUSBX_CONFIG
    make
    make install-strip

    # Build autoconf stuff of usb4java if needed
    cd "$SRCDIR"
    if [ ! -e configure ]
    then
        make -f Makefile.scm
    fi

    # Build libusb4java
    PKG_CONFIG_PATH="$TMPDIR/lib/pkgconfig" \
    LIBS="$USB4JAVA_LIBS" \
    CFLAGS="$CFLAGS $USB4JAVA_CFLAGS" \
    ./configure --prefix=/ --host="$HOST" $USB4JAVA_CONFIG
    make clean install-strip DESTDIR="$TMPDIR"
    
    # Copy dist files to java resources directory
    mkdir -p "$DISTDIR"
    cp -faL 2>/dev/null \
        "$TMPDIR/lib/libusb4java.so" \
        "$TMPDIR/bin/libusb-1.0.dll" \
        "$DISTDIR" || true
    cp -faL 2>/dev/null \
        "$TMPDIR/bin/libusb4java-1.dll" \
        "$DISTDIR/libusb4java.dll" || true
    cp -faL 2>/dev/null \
        "$TMPDIR/lib/libusb4java.dylib" \
        "$DISTDIR/libusb4java.dylib" || true

    # Remove executable flag from dist files
    chmod -x "$DISTDIR/"*

    # Cleanup
    rm -rf "$TMPDIR"
}
