/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusbx;

/**
 * 
 */
public class LibUSB
{
    static
    {
        Loader.load();
    }
    
    /**
     * Initialize libusb.
     * 
     * This function must be called before calling any other libusbx function.
     * 
     * If you do not provide an output location for a {@link Context}, a default
     * context will be created. If there was already a default context, it will
     * be reused (and nothing will be initialized/reinitialized).
     * 
     * @param context
     *            Optional output location for context pointer. Null to use
     *            default context. Only valid on return code 0.
     * @return 0 on success, or a {@link Error} code on failure
     * 
     * @see <a
     *      href="http://libusbx.sourceforge.net/api-1.0/contexts.html">Contexts</a>
     */
    public static native Error init(final Context context);

    /**
     * Deinitialize libusb.
     * 
     * Should be called after closing all open devices and before your
     * application terminates.
     * 
     * @param context
     *            The {@link Context} to deinitialize, or NULL for the default
     *            context
     */
    public static native void exit(final Context context);

    /**
     * Set log message verbosity.
     * 
     * The default level is {@link LogLevel#NONE}, which means no messages are
     * ever printed. If you choose to increase the message verbosity level,
     * ensure that your application does not close the stdout/stderr file
     * descriptors.
     * 
     * You are advised to use level {@link LogLevel#WARNING}. libusbx is
     * conservative with its message logging and most of the time, will only log
     * messages that explain error conditions and other oddities. This will help
     * you debug your software.
     * 
     * If the {@link LogLevel#DEBUG} environment variable was set when libusbx
     * was initialized, this function does nothing: the message verbosity is
     * fixed to the value in the environment variable.
     * 
     * If libusbx was compiled without any message logging, this function does
     * nothing: you'll never get any messages.
     * 
     * If libusbx was compiled with verbose debug message logging, this function
     * does nothing: you'll always get messages from all levels.
     * 
     * @param context
     *            The {@link Context} to operate on, or NULL for the default
     *            context.
     * @param level
     *            The log level to set.
     */
    public static native void setDebug(final Context context, final LogLevel level);
    
    /**
     * Returns the version of the libusbx runtime.
     * 
     * @return The version of the libusbx runtime.
     */
    public static native Version getVersion();

}
