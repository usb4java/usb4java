The usb4java test firmware for the microblinks.de L controller.

Makefile targets:

all:   Compiles the firmware. HEX file is then located in target directory.
clean: Removes all build artifacts from the source directory.
dist:  Creates source distribution archive
flash: Builds the firmware and flashes it to the device

To compile you need the packages gcc-avr and avr-libc. To flash you need
the package avrdude

The programmer and the port for avrdude can be specified with the
variables PROGRAMMER and PORT. See avrdude manual for details.

Example:

make flash PROGRAMMER=avrispmkII PORT=usb:30


This software includes V-USB (http://www.obdev.at/products/vusb/) from
Objective Development (http://www.obdev.at/). The source code together with
its license is located in the usbdrv folder.
