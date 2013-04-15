cd "$(dirname $0)"
SRCDIR="$(pwd)/.."
TMPDIR="$SRCDIR/tmp"
DOWNLOADS="$SRCDIR/downloads"
LIBUSB="libusbx"
LIBUSB_VERSION="1.0.16"
LIBUSB_RC="-rc6"
LIBUSBX_VERSION="1.0.15"
LIBUSBX_RC="-rc3"

build()
{
    if [ "$LIBUSB" = "libusbx" ]
    then
        LIBUSB_NAME="libusbx-$LIBUSBX_VERSION$LIBUSBX_RC"
        LIBUSB_ARCHIVE="$LIBUSB_NAME.tar.bz2"
        LIBUSB_URL="http://downloads.sf.net/project/libusbx/releases/$LIBUSBX_VERSION/source/$LIBUSB_ARCHIVE"
    else
        LIBUSB_NAME="libusb-$LIBUSB_VERSION$LIBUSB_RC"
        LIBUSB_ARCHIVE="$LIBUSB_NAME.tar.bz2"
        LIBUSB_URL="http://downloads.sf.net/project/libusb/libusb-1.0/libusb-$LIBUSB_VERSION$LIBUSB_RC/$LIBUSB_ARCHIVE"
    fi

    DISTDIR="$SRCDIR/../resources/de/ailis/usb4java/libusb/$OS-$ARCH"
    
    # Clean up
    rm -rf "$TMPDIR"
    rm -rf "$DISTDIR"

    # Download libusb if necessary
    mkdir -p "$DOWNLOADS"
    if [ ! -e "$DOWNLOADS/$LIBUSB_ARCHIVE" ]
    then
       curl -L -o "$DOWNLOADS/$LIBUSB_ARCHIVE" "$LIBUSB_URL"
    fi

    # Unpack and compile libusb
    mkdir -p "$TMPDIR"
    cd "$TMPDIR"
    tar xfj "$DOWNLOADS/$LIBUSB_ARCHIVE"
    cd "$LIBUSB_NAME"
    CFLAGS="$CFLAGS $LIBUSB_CFLAGS" \
    ./configure --prefix="$TMPDIR" --host="$HOST" --with-pic $LIBUSB_CONFIG
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
