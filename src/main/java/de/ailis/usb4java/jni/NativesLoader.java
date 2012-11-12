/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.ailis.usb4java.exceptions.NativesException;
import de.ailis.usb4java.support.IOUtils;

/**
 * Utility class to load native libraries from classpath.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class NativesLoader
{
    /** If Windows operating system. */
    private static final boolean WINDOWS = System.getProperty("os.name")
        .contains("Windows");

    /** If Linux operating system. */
    private static final boolean LINUX = System.getProperty("os.name")
        .contains("Linux");

    /** If Mac operating system. */
    private static final boolean MAC = System.getProperty("os.name").contains(
        "Mac");

    /** If 64 bit operating system. */
    private static final boolean IS_64_BIT = System.getProperty("os.arch")
        .equals("amd64") ||
        System.getProperty("os.arch").equals("x86_64");

    /** The temporary directory for native libraries. */
    private static final File TMP = createTempDirectory();
    
    /** If library is already loaded. */
    private static boolean loaded = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private NativesLoader()
    {
        // Nothing to do here
    }

    /**
     * Creates the temporary directory used for unpacking the native libraries.
     * This directory is marked for deletion on exit.
     * 
     * @return The temporary directory for native libraries.
     */
    private static File createTempDirectory()
    {
        try
        {
            File tmp = File.createTempFile("usb4java", null);
            tmp.delete();
            tmp.mkdirs();
            tmp.deleteOnExit();
            return tmp;
        }
        catch (IOException e)
        {
            throw new NativesException("Unable to create temporary directory " +
                "for usb4java natives: " + e, e);
        }
    }

    /**
     * Returns the platform name. This could be for example "linux-x86" or
     * "windows-x86_64".
     * 
     * @return The architecture name. Never null.
     * @throws NativesException
     *             When operating system or architecture is not supported.
     */
    private static String getPlatform()
    {
        if (WINDOWS)
            return IS_64_BIT ? "windows-x86_64" : "windows-x86";
        else if (MAC)
            return "macosx-universal";
        else if (LINUX)
            return IS_64_BIT ? "linux-x86_64" : "linux-x86";
        else
            throw new NativesException("Unsupported operating system ("
                + System.getProperty("os.name") + ") and/or architecture ("
                + System.getProperty("os.arch") + ")");
    }

    /**
     * Returns the name of the usb4java native library. This could be
     * "libusb4java.dll" for example.
     * 
     * @return The usb4java native library name. Never null.
     * @throws NativesException
     *             When operating system or architecture is not supported.
     */
    private static String getLibName()
    {
        if (WINDOWS)
            return "libusb4java.dll";
        else if (MAC)
            return "libusb4java.dylib";
        else if (LINUX)
            return "libusb4java.so";
        else
            throw new NativesException("Unsupported operating system ("
                + System.getProperty("os.name") + ") and/or architecture ("
                + System.getProperty("os.arch") + ")");
    }

    /**
     * Returns the name of the libusb native library. This could be
     * "libusb0.dll" for example or null if this library is not needed on the
     * current platform (Because it is provided by the operating system).
     * 
     * @return The libusb native library name or null if not needed.
     * @throws NativesException
     *             When operating system or architecture is not supported.
     */
    private static String getExtraLibName()
    {
        if (WINDOWS)
            return "libusb0.dll";
        else if (MAC)
            return "libusb.dylib";
        else if (LINUX)
            return null;
        else
            throw new NativesException("Unsupported operating system ("
                + System.getProperty("os.name") + ") and/or architecture ("
                + System.getProperty("os.arch") + ")");
    }

    /**
     * Extracts a single library.
     * 
     * @param platform
     *            The platform name (For example "linux-x86")
     * @param lib
     *            The library name to extract (For example "libusb0.dll")
     * @return The absolute path to the extracted library.
     */
    private static String extractLibrary(String platform, String lib)
    {
        // Extract the usb4java library
        final String source = platform + "/" + lib;
        final File dest = new File(TMP, lib);
        try
        {
            final InputStream stream =
                NativesLoader.class.getClassLoader()
                    .getResourceAsStream(source);
            if (stream == null)
                throw new NativesException("Unable to find " + source
                    + " in the classpath");
            try
            {
                IOUtils.copy(stream, dest);
            }
            finally
            {
                stream.close();
            }
        }
        catch (IOException e)
        {
            throw new NativesException(
                "Unable to extract native library " + source + " to " + dest
                    + ": " + e, e);
        }

        // Mark usb4java library for deletion
        dest.deleteOnExit();

        return dest.getAbsolutePath();
    }

    /**
     * Extracts the usb4java library (and the libusb library if needed) and
     * returns the absolute filename to be loaded by Java. The extracted
     * libraries are marked for deletion on exit.
     * 
     * @return The absolute path to the extracted usb4java library.
     */
    private static String extract()
    {
        final String platform, lib, extraLib;

        platform = getPlatform();
        lib = getLibName();
        extraLib = getExtraLibName();
        if (extraLib != null) extractLibrary(platform, extraLib);
        return extractLibrary(platform, lib);
    }
    
    /**
     * Loads the usb4java native library. Can be safely called multiple times.
     * Duplicate calls are ignored.
     */
    public static void load()
    {
        if (loaded) return;
        final String path = extract();
        System.load(path);
        loaded = true;
    }
}
