/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.jsr80;

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


/**
 * usb4java implementation of UsbPipe.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

final class UsbPipeImpl implements UsbPipe
{
    /** The endpoint this pipe belongs to. */
    private final UsbEndpointImpl endpoint;

    /** The device. */
    private final AbstractDevice device;

    /** The USB pipe listeners. */
    private final UsbPipeListenerList listeners = new UsbPipeListenerList();

    /** If pipe is open or not. */
    private boolean opened;

    /** The request queue. */
    private final IrpQueue queue ;


    /**
     * Constructor.
     *
     * @param endpoint
     *            The endpoint this pipe belongs to.
     * @param device
     *            The USB device.
     */


    UsbPipeImpl(final UsbEndpointImpl endpoint, final AbstractDevice device)
    {
        this.endpoint = endpoint;
        this.device = device;
        this.queue = new IrpQueue(device, this);
    }


    /**
     * Returns the USB device.
     *
     * @return The USB device.
     */

    AbstractDevice getDevice()
    {
        return this.device;
    }


    /**
     * Ensures the pipe is active.
     *
     * @throws UsbNotActiveException
     *             When pipe is not active
     */

    private void checkActive() throws UsbNotActiveException
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

    private void checkClaimed() throws UsbNotClaimedException
    {
        if (!this.endpoint.getUsbInterface().isClaimed())
            throw new UsbNotClaimedException("Interface is not claimed.");
    }


    /**
     * Ensures the device is not disconnected.
     *
     * @throws UsbDisconnectedException
     *             When device has been disconnected.
     */

    private void checkDisconnected() throws UsbDisconnectedException
    {
        // TODO implement me
    }


    /**
     * Ensures the pipe is open.
     *
     * @throws UsbNotOpenException
     *             When pipe is not open.
     */

    private void checkOpen() throws UsbNotOpenException
    {
        if (!isOpen())
            throw new UsbNotOpenException("Pipe is not open.");
    }


    /**
     * @see UsbPipe#open()
     */

    @Override
    public void open() throws UsbException, UsbNotActiveException,
        UsbNotClaimedException, UsbDisconnectedException
    {
        checkActive();
        checkClaimed();
        checkDisconnected();
        if (this.opened) throw new UsbException("Pipe is already open");
        this.opened = true;
    }


    /**
     * @see UsbPipe#close()
     */

    @Override
    public void close() throws UsbException, UsbNotActiveException,
        UsbNotOpenException, UsbDisconnectedException
    {
        checkActive();
        checkClaimed();
        checkDisconnected();
        if (!this.opened) throw new UsbException("Pipe is already closed");
        if (this.queue.isBusy())
            throw new UsbException("Pipe is still busy");
        this.opened = false;
    }


    /**
     * @see UsbPipe#isActive()
     */

    @Override
    public boolean isActive()
    {
        final UsbInterface iface = this.endpoint.getUsbInterface();
        final UsbConfiguration config = iface.getUsbConfiguration();
        return iface.isActive() && config.isActive();
    }


    /**
     * @see UsbPipe#isOpen()
     */

    @Override
    public boolean isOpen()
    {
        return this.opened;
    }


    /**
     * @see UsbPipe#getUsbEndpoint()
     */

    @Override
    public UsbEndpointImpl getUsbEndpoint()
    {
        return this.endpoint;
    }


    /**
     * @see UsbPipe#syncSubmit(byte[])
     */

    @Override
    public int syncSubmit(final byte[] data) throws UsbException,
        UsbNotActiveException, UsbNotOpenException, IllegalArgumentException,
        UsbDisconnectedException
    {
        final UsbIrp irp = asyncSubmit(data);
        irp.waitUntilComplete();
        if (irp.isUsbException()) throw irp.getUsbException();
        return irp.getActualLength();
    }


    /**
     * @see UsbPipe#asyncSubmit(byte[])
     */

    @Override
    public UsbIrp asyncSubmit(final byte[] data) throws UsbException,
        UsbNotActiveException, UsbNotOpenException, IllegalArgumentException,
        UsbDisconnectedException
    {
        if (data == null)
            throw new IllegalArgumentException("data must not be null");
        final UsbIrp irp = createUsbIrp();
        irp.setAcceptShortPacket(true);
        irp.setData(data);
        asyncSubmit(irp);
        return irp;
    }


    /**
     * @see UsbPipe#syncSubmit(javax.usb.UsbIrp)
     */

    @Override
    public void syncSubmit(final UsbIrp irp) throws UsbException,
        UsbNotActiveException, UsbNotOpenException, IllegalArgumentException,
        UsbDisconnectedException
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        asyncSubmit(irp);
        irp.waitUntilComplete();
        if (irp.isUsbException()) throw irp.getUsbException();
    }


    /**
     * @see UsbPipe#asyncSubmit(javax.usb.UsbIrp)
     */

    @Override
    public void asyncSubmit(final UsbIrp irp) throws UsbException,
        UsbNotActiveException, UsbNotOpenException, IllegalArgumentException,
        UsbDisconnectedException
    {
        if (irp == null)
            throw new IllegalArgumentException("irp must not be null");
        checkActive();
        checkDisconnected();
        checkOpen();
        this.queue.add(irp);
    }


    /**
     * @see UsbPipe#syncSubmit(java.util.List)
     */

    @Override
    public void syncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException, UsbNotActiveException, UsbNotOpenException,
        IllegalArgumentException, UsbDisconnectedException
    {
        for (final Object item : list)
        {
            final UsbIrp irp = (UsbIrp) item;
            syncSubmit(irp);
        }
    }


    /**
     * @see UsbPipe#asyncSubmit(java.util.List)
     */

    @Override
    public void asyncSubmit(@SuppressWarnings("rawtypes") final List list)
        throws UsbException, UsbNotActiveException, UsbNotOpenException,
        IllegalArgumentException, UsbDisconnectedException
    {
        for (final Object item : list)
        {
            final UsbIrp irp = (UsbIrp) item;
            asyncSubmit(irp);
        }
    }


    /**
     * @see UsbPipe#abortAllSubmissions()
     */

    @Override
    public void abortAllSubmissions() throws UsbNotActiveException,
        UsbNotOpenException, UsbDisconnectedException
    {
        checkActive();
        checkDisconnected();
        checkOpen();
        this.queue.abort();
    }


    /**
     * @see UsbPipe#createUsbIrp()
     */

    @Override
    public UsbIrp createUsbIrp()
    {
        return new DefaultUsbIrp();
    }


    /**
     * @see UsbPipe#createUsbControlIrp(byte, byte, short, short)
     */

    @Override
    public UsbControlIrp createUsbControlIrp(final byte bmRequestType,
        final byte bRequest,
        final short wValue, final short wIndex)
    {
        return new DefaultUsbControlIrp(bmRequestType, bRequest, wValue,
            wIndex);
    }


    /**
     * @see UsbPipe#addUsbPipeListener(UsbPipeListener)
     */

    @Override
    public void addUsbPipeListener(final UsbPipeListener listener)
    {
        this.listeners.add(listener);
    }


    /**
     * @see UsbPipe#removeUsbPipeListener(UsbPipeListener)
     */

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

    void sendEvent(final UsbIrp irp)
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
}
