/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package de.ailis.usb4java.libusbx;

/**
 * Log message levels.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public enum LogLevel
{
    /** No messages ever printed by the library (default). */
    NONE(0),

    /** Error messages are printed to stderr. */
    ERROR(1),

    /** Warning and error messages are printed to stderr. */
    WARNING(2),

    /**
     * Informational messages are printed to stdout, warning and error messages
     * are printed to stderr.
     */
    INFO(3),

    /**
     * Debug and informational messages are printed to stdout, warnings and
     * errors to stderr.
     */
    DEBUG(4);

    /** The numeric log level. */
    int level;

    /**
     * Constructor.
     * 
     * @param level
     *            The numeric log level.
     */
    private LogLevel(int level)
    {
        this.level = level;
    }
}
