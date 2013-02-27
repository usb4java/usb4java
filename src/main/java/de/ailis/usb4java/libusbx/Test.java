/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package de.ailis.usb4java.libusbx;

/**
 */
public class Test
{
    /**
     * @param args command line args
     */
    public static void main(String[] args)
    {
        System.out.println(LibUSB.getVersion());
        Context context = new Context();
        System.out.println(context);
        System.out.println(LibUSB.init(context));
        LibUSB.setDebug(context, LogLevel.DEBUG);
        System.out.println(context);
        LibUSB.exit(context);
        System.out.println(context);
    }
}
