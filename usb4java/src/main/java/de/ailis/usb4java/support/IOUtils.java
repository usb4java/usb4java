/*
 * Copyright (C) 2012 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO utility methods.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class IOUtils
{
    /**
     * Private constructor to prevent instantiation.
     */
    private IOUtils()
    {
        // Empty
    }

    /**
     * Copies the specified input stream to the specified output stream.
     * 
     * @param input
     *            The input stream.
     * @param output
     *            The output stream.
     * @throws IOException
     *             If copying failed.
     */
    public static void copy(final InputStream input, final OutputStream output)
        throws IOException
    {
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, read);
        }
    }

    /**
     * Copies the specified input stream to the specified output file.
     * 
     * @param input
     *            The input stream.
     * @param output
     *            The output file.
     * @throws IOException
     *             If copying failed.
     */
    public static void copy(final InputStream input, final File output)
        throws IOException
    {
        FileOutputStream stream = new FileOutputStream(output);
        try
        {
            copy(input, stream);
        }
        finally
        {
            stream.close();
        }
    }
}
