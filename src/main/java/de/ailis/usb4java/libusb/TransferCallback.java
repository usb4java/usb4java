package de.ailis.usb4java.libusb;

public interface TransferCallback
{
    void processTransfer(Transfer transfer);
}
