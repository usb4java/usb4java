# ========================================================================
# $Id$
# Copyright (C) 2010 microblinks UG (Haftungsbeschraenkt)
# See LICENSE.txt for licensing information
# ========================================================================

# Project information
NAME = usb4java-test-firmware
MAJOR_VERSION = 1
MINOR_VERSION = 0
VERSION = $(MAJOR_VERSION).$(MINOR_VERSION)

# Programmer information
PROGRAMMER = avrispmkII
PORT = usb:30

# Device information
DEVICE = atmega88
F_CPU = 12000000

# Paths and filenames
SRC = .
TARGET = ../../../target/firmware
ELFFILE = $(TARGET)/$(NAME)-$(VERSION).elf
BINFILE = $(TARGET)/$(NAME)-$(VERSION).bin
HEXFILE = $(TARGET)/$(NAME)-$(VERSION).hex

# Objects to compile
OBJECTS = \
  $(TARGET)/usbdrv/usbdrv.o \
  $(TARGET)/usbdrv/usbdrvasm.o \
  $(TARGET)/pwm/pwm.o \
  $(TARGET)/main.o

# Include files
INCLUDES = \
  $(SRC)/descriptor.h \
  $(SRC)/usbconfig.h \
  $(SRC)/pwm/pwm.h \
  $(SRC)/pwm/pwmconfig.h

# Compiler configuration
CC = avr-gcc
OBJCOPY = avr-objcopy
SIZE = avr-size
INCLUDEDIRS = -I$(SRC) -I$(SRC)/usbdrv
CFLAGS = -Wall -Werror -Os $(INCLUDEDIRS) -mmcu=$(DEVICE) \
  -DF_CPU=$(F_CPU) -DDEBUG_LEVEL=0 -DVERSION=$(VERSION) \
  -DMAJOR_VERSION=$(MAJOR_VERSION) -DMINOR_VERSION=$(MINOR_VERSION)
LDFLAGS = 

# AVR Dude configuration
AVRDUDE = avrdude
AVRDUDE_FLAGS = -c $(PROGRAMMER) -P $(PORT) -p $(DEVICE)

# Default target
all: $(HEXFILE) $(BINFILE)

# Builds the HEX file
$(HEXFILE): $(ELFFILE)
	@echo "Building $(HEXFILE)"
	@$(OBJCOPY) -j .text -j .data -O ihex $(ELFFILE) $(HEXFILE)

# Builds the RAW file
$(BINFILE): $(ELFFILE)
	@echo "Building $(BINFILE)"
	@$(OBJCOPY) -j .text -j .data -O binary $(ELFFILE) $(BINFILE)

# Builds the BIN file
$(ELFFILE): $(OBJECTS)	
	@echo "Building $(ELFFILE)"
	@$(CC) $(CFLAGS) $(LDFLAGS) -o $(ELFFILE) $(OBJECTS)
	@$(SIZE) $(ELFFILE)

# Removes all build files from the source
clean:
	@echo "Cleaning source"
	@$(RM) -rf $(TARGET)

# Create source distribution archive
dist:
	mkdir .$(NAME)-$(VERSION)
	cp -raf * .$(NAME)-$(VERSION)
	find .$(NAME)-$(VERSION) -name "*.svn" | xargs rm -rf
	rm -rf .$(NAME)-$(VERSION)/$(TARGET)
	mv .$(NAME)-$(VERSION) $(NAME)-$(VERSION)
	mkdir -p $(TARGET)
	tar cvfj $(TARGET)/$(NAME)-$(VERSION).tar.bz2 $(NAME)-$(VERSION)
	rm -rf $(NAME)-$(VERSION)

# Flashes the firmware
flash: $(HEXFILE)
	@echo "Flashing firmware"
	@$(AVRDUDE) $(AVRDUDE_FLAGS) -U flash:w:$(HEXFILE):i

# Generic target for compiling C files
$(TARGET)/%.o: $(SRC)/%.c $(INCLUDES)
	@echo "Compiling $< to $@"
	@mkdir -p "$(@D)"
	@$(CC) $(CFLAGS) -c "$<" -o "$@"

# Generic target for compiling assembler files
$(TARGET)/%.o: $(SRC)/%.S $(INCLUDES)
	@echo "Assembling $< to $@"
	@mkdir -p "$(@D)"
	@$(CC) $(CFLAGS) -x assembler-with-cpp -c "$<" -o "$@"
