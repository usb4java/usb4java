/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class to load native libraries from classpath.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Loader
{
    /** The temporary directory for native libraries. */
    private static File tmp;

    /** If library is already loaded. */
    private static boolean loaded = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private Loader()
    {
        // Nothing to do here
    }

    /**
     * Returns the operating system name. This could be "linux", "windows" or
     * "macosx" or (for any other non-supported platform) the value of the
     * "os.name" property converted to lower case and with removed space
     * characters.
     * 
     * @return The operating system name.
     */
    private static String getOS()
    {
        final String os = System.getProperty("os.name");
        if (os.contains("Windows")) return "windows";
        return os.toLowerCase().replace(" ", "");
    }

    /**
     * Returns the CPU architecture. This will be "x86" or "x86_64" (Platform
     * names i386 und amd64 are converted accordingly) or (when platform is
     * unsupported) the value of os.arch converted to lower-case and with
     * removed space characters.
     * 
     * @return The CPU architecture
     */
    private static String getArch()
    {
        final String os = getOS();
        if (os.equals("macosx")) return "universal";
        final String arch = System.getProperty("os.arch");
        if (arch.equals("i386")) return "x86";
        if (arch.equals("amd64")) return "x86_64";
        return arch.toLowerCase().replace(" ", "");
    }

    /**
     * Returns the shared library extension name.
     * 
     * @return The shared library extension name.
     */
    private static String getExt()
    {
        final String os = getOS();
        final String key = "usb4java.libext." + getOS();
        final String ext = System.getProperty(key);
        if (ext != null) return ext;
        if (os.equals("linux") || os.equals("freebsd") || os.equals("sunos"))
            return "so";
        if (os.equals("windows"))
            return "dll";
        if (os.equals("macosx"))
            return "dylib";
        throw new LoaderException("Unable to determine the shared library " +
            "file extension for operating system '" + os +
            "'. Please specify Java parameter -D" + key + "=<FILE-EXTENSION>");
    }

    /**
     * Creates the temporary directory used for unpacking the native libraries.
     * This directory is marked for deletion on exit.
     * 
     * @return The temporary directory for native libraries.
     */
    private static File createTempDirectory()
    {
        // Return cached tmp directory when already created
        if (tmp != null) return tmp;

        try
        {
            tmp = File.createTempFile("usb4java", null);
            tmp.delete();
            tmp.mkdirs();
            tmp.deleteOnExit();
            return tmp;
        }
        catch (final IOException e)
        {
            throw new LoaderException("Unable to create temporary directory " +
                "for usb4java natives: " + e, e);
        }
    }

    /**
     * Returns the platform name. This could be for example "linux-x86" or
     * "windows-x86_64".
     * 
     * @return The architecture name. Never null.
     */
    private static String getPlatform()
    {
        return getOS() + "-" + getArch();
    }

    /**
     * Returns the name of the usb4java native library. This could be
     * "libusb4java.dll" for example.
     * 
     * @return The usb4java native library name. Never null.
     */
    private static String getLibName()
    {
        return "libusb4java." + getExt();
    }

    /**
     * Returns the name of the libusb native library. This could be
     * "libusb0.dll" for example or null if this library is not needed on the
     * current platform (Because it is provided by the operating system).
     * 
     * @return The libusb native library name or null if not needed.
     */
    private static String getExtraLibName()
    {
        String os = getOS();
        if (os.equals("windows")) return "libusb0.dll";
        if (os.equals("mnacosx")) return "libusb.dylib";
        return null;
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
    private static void copy(final InputStream input, final File output)
        throws IOException
    {
        final byte[] buffer = new byte[8192];
        final FileOutputStream stream = new FileOutputStream(output);
        try
        {
            int read;
            while ((read = input.read(buffer)) != -1)
            {
                stream.write(buffer, 0, read);
            }
        }
        finally
        {
            stream.close();
        }
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
    private static String extractLibrary(final String platform, final String lib)
    {
        // Extract the usb4java library
        final String source = '/' +
            Loader.class.getPackage().getName().replace('.', '/') +
            '/' + platform + "/" + lib;

        // Check if native library is present
        final URL url = Loader.class.getResource(source);
        if (url == null) throw new LoaderException(
            "Native library not found in classpath: " + source);

        // If native library was found in an already extracted form then
        // return this one without extracting it
        if (url.getProtocol().equals("file"))
        {
            try
            {
                return new File(url.toURI()).getAbsolutePath();
            }
            catch (final URISyntaxException e)
            {
                // Can't happen because we are not constructing the URI
                // manually. But even when it happens then we fall back to
                // extracting the library.
            }
        }

        // Extract the library and return the path to the extracted file.
        final File dest = new File(createTempDirectory(), lib);
        try
        {
            final InputStream stream =
                Loader.class.getResourceAsStream(source);
            if (stream == null)
                throw new LoaderException("Unable to find " + source
                    + " in the classpath");
            try
            {
                copy(stream, dest);
            }
            finally
            {
                stream.close();
            }
        }
        catch (final IOException e)
        {
            throw new LoaderException(
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
     * Loads the libusb0.1.x native wrapper library. Can be safely called
     * multiple times. Duplicate calls are ignored. This method is automatically
     * called when the {@link USB} class is loaded. When you need to do it
     * earlier (To catch exceptions for example) then simply call this method
     * manually.
     * 
     * @throws LoaderException
     *             When loading the native wrapper libraries failed.
     */
    public static void load() throws LoaderException
    {
        if (loaded) return;
        final String path = extract();
        System.load(path);
        loaded = true;
    }
}
