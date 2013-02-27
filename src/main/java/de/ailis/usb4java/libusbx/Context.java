/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 * 
 * Based on libusbx <http://libusbx.org/>:  
 * 
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2008 Daniel Drake <dsd@gentoo.org>
 * Copyright 2012 Pete Batard <pete@akeo.ie>
 */
package de.ailis.usb4java.libusbx;

import java.nio.ByteBuffer;

/**
 * Structure representing a libusbx session. The concept of individual libusbx 
 * sessions allows for your program to use two libraries (or dynamically load 
 * two modules) which both independently use libusb. This will prevent 
 * interference between the individual libusbx users - for example 
 * libusb_set_debug() will not affect the other user of the library, 
 * and libusb_exit() will not destroy resources that the other user is still 
 * using.
 *
 * Sessions are created by libusb_init() and destroyed through libusb_exit(). 
 * If your application is guaranteed to only ever include a single libusbx 
 * user (i.e. you), you do not have to worry about contexts: pass NULL in 
 * every function call where a context is required. The default context will be 
 * used.
 * 
 * For more information, see 
 * <a href="http://libusbx.sourceforge.net/api-1.0/contexts.html">Contexts</a>.
 */
public class Context
{
    /** The low-level context structure. */
    ByteBuffer data;
}
