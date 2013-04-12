/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbIrp;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;
import javax.usb.util.DefaultUsbControlIrp;
import javax.usb.util.DefaultUsbIrp;

import de.ailis.usb4java.support.UsbPipeListenerList;

/**
 * usb4java implementation of UsbPipe.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaPipe implements UsbPipe
{
    /** The endpoint this pipe belongs to. */
    private final Usb4JavaEndpoint endpoint;

    /** The USB pipe listeners. */
    private final UsbPipeListenerList listeners = new UsbPipeListenerList();

    /** If pipe is open or not. */
    private boolean opened;

    /** The request queue. */
    private final IrpQueue queue;

    /**
     * Constructor.
     *
     * @param endpoint
     *            The endpoint this pipe belongs to.
     */
    Usb4JavaPipe(final Usb4JavaEndpoint endpoint)
    {
        this.endpoint = endpoint;
        this.queue = new IrpQueue(this);
    }

    /**
     * Returns the USB device.
     *
     * @return The USB device.
     */
    public Usb4JavaDevice getDevice()
    {
        return this.endpoint.getUsbInterface().getUsbConfiguration().getUsbDevice();
    }

    /**
     * Ensures the pipe is active.
     *
     * @throws UsbNotActiveException
     *             When pipe is not active
     */
    private void checkActive()
    {
        if (!isActive())
            throw new UsbNotActiveException("Pipe is not active.");
    }

    /**
     * Ensures the interface is active.
     *
     * @throws UsbNotClaimedException
     *             When interface is not claimed.
     */
    private void checkClaimed()
    {
        if (!this.endpoint.getUsbInterface().isClaimed())
            throw new UsbNotClaimedException("Interface is not claimed.");
    }

    /**
     * Ensures the device is connected.
     *
     * @throws UsbDisconnectedException
     *             When device has been disconnected.
     */
    private void checkConnected()
    {
        getDevice().checkConnected();
    }

    /**
     * Ensures the pipe is open.
     *
     * @throws UsbNotOpenException
     *             When pipe is not open.
     */
    private void checkOpen()
    {
        if (!isOpen())
            throw new UsbNotOpenException("Pipe is not open.");
    }

    @Override
    public void open() throws UsbException
    {
        checkActive();
        checkClaimed();
        checkConnected();
        if (this.opened) throw new UsbException("Pipe is already open");
        this.opened = true;
    }

    @Override
    public void close() throws UsbException
    {
        checkActive();
        checkClaimed();
        checkConnected();
        if (!this.opened) throw new UsbException("Pipe is already closed");
        if (this.queue.isBusy())
            throw new UsbException("Pipe is still busy");
        this.opened = false;
    }

    @Override
    public boolean isActive()
    {
        final UsbInterface iface = this.endpoint.getUsbInterface();
        final UsbConfiguration config = iface.getUsbConfiguration();
        return iface.isActive() && config.isActive();
    }

    @Override
    public boolean isOpen()
    {
        return this.opened;
    }

    @Override
    public Usb4JavaEndpoint getUsbEndpoint()
    {
        return this.endpoint;
    }

    @Override
    public int syncSubmit(final byte[] data) throws UsbException
    {
        final UsbIrp irp = asyncSubmit(data);
        irp.waitUntilComplete();
        if (irp.isUsbException()) throw irp.getUsbException();
        return irp.getActualLength();
    }

    @Override
    public UsbIrp asyncSubmit(final byte[] data)
    {
        if (data == null)
            throw new IllegalArgumentException("data must not be null");
        final UsbIrp irp = createUsbIrp();
        irp.setAcceptShortPacket(true);
        irp.setData(data);
        asyncSubmit(irp);
        return irp;
    }

    @Override
    public void syncSubmit(final UsbIrp irp) throws UsbException
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        asyncSubmit(irp);
        irp.waitUntilComplete();
        if (irp.isUsbException()) throw irp.getUsbException();
    }

    @Override
    public void asyncSubmit(final UsbIrp irp)
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        checkActive();
        checkConnected();
        checkOpen();
        this.queue.add(irp);
    }

    @Override
    public void syncSubmit(final List list) throws UsbException
    {
        for (final Object item : list)
        {
            final UsbIrp irp = (UsbIrp) item;
            syncSubmit(irp);
        }
    }

    @Override
    public void asyncSubmit(final List list)
    {
        for (final Object item : list)
        {
            final UsbIrp irp = (UsbIrp) item;
            asyncSubmit(irp);
        }
    }

    @Override
    public void abortAllSubmissions()
    {
        checkActive();
        checkConnected();
        checkOpen();
        this.queue.abort();
    }

    @Override
    public UsbIrp createUsbIrp()
    {
        return new DefaultUsbIrp();
    }

    @Override
    public UsbControlIrp createUsbControlIrp(final byte bmRequestType,
        final byte bRequest,
        final short wValue, final short wIndex)
    {
        return new DefaultUsbControlIrp(bmRequestType, bRequest, wValue,
            wIndex);
    }

    @Override
    public void addUsbPipeListener(final UsbPipeListener listener)
    {
        this.listeners.add(listener);
    }

    @Override
    public void removeUsbPipeListener(final UsbPipeListener listener)
    {
        this.listeners.remove(listener);
    }

    /**
     * Sends event to all event listeners.
     *
     * @param irp
     *            Then request package
     */
    public void sendEvent(final UsbIrp irp)
    {
        if (irp.isUsbException())
        {
            this.listeners.errorEventOccurred(new UsbPipeErrorEvent(this, irp));
        }
        else
        {
            this.listeners.dataEventOccurred(new UsbPipeDataEvent(this, irp));
        }
    }
    
    @Override
    public String toString()
    {
        return String.format("USB pipe of endpoint %02x",
            this.endpoint.getUsbEndpointDescriptor().bEndpointAddress());
    }
}
