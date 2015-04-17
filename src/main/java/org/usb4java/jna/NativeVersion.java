package org.usb4java.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public final class NativeVersion extends Structure {
    public short major;

    public short minor;

    public short micro;

    public short nano;

    public String rc;

    public String describe;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "major", "minor", "micro", "nano", "rc", "describe" });
    }
}