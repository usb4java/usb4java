1 javax.usb.UsbAbortException extends javax.usb.UsbException
{
  1 javax.usb.UsbAbortException (java.lang.String)
  1 javax.usb.UsbAbortException ()
}
1 javax.usb.UsbBabbleException extends javax.usb.UsbException
{
  1 javax.usb.UsbBabbleException (java.lang.String)
  1 javax.usb.UsbBabbleException ()
}
1 javax.usb.UsbBitStuffException extends javax.usb.UsbException
{
  1 javax.usb.UsbBitStuffException (java.lang.String)
  1 javax.usb.UsbBitStuffException ()
}
1 javax.usb.UsbClaimException extends javax.usb.UsbException
{
  1 javax.usb.UsbClaimException (java.lang.String)
  1 javax.usb.UsbClaimException ()
}
1537 javax.usb.UsbConfiguration
{
  1025 boolean isActive ()
  1025 java.util.List getUsbInterfaces ()
  1025 javax.usb.UsbInterface getUsbInterface (byte)
  1025 boolean containsUsbInterface (byte)
  1025 javax.usb.UsbDevice getUsbDevice ()
  1025 javax.usb.UsbConfigurationDescriptor getUsbConfigurationDescriptor ()
  1025 java.lang.String getConfigurationString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException,javax.usb.UsbDisconnectedException
}
1537 javax.usb.UsbConfigurationDescriptor implements javax.usb.UsbDescriptor
{
  1025 short wTotalLength ()
  1025 byte bNumInterfaces ()
  1025 byte bConfigurationValue ()
  1025 byte iConfiguration ()
  1025 byte bmAttributes ()
  1025 byte bMaxPower ()
}
1537 javax.usb.UsbConst
{
}
1537 javax.usb.UsbControlIrp implements javax.usb.UsbIrp
{
  1025 byte bmRequestType ()
  1025 byte bRequest ()
  1025 short wValue ()
  1025 short wIndex ()
}
1 javax.usb.UsbCRCException extends javax.usb.UsbException
{
  1 javax.usb.UsbCRCException (java.lang.String)
  1 javax.usb.UsbCRCException ()
}
1537 javax.usb.UsbDescriptor
{
  1025 byte bLength ()
  1025 byte bDescriptorType ()
}
1537 javax.usb.UsbDevice
{
  1025 javax.usb.UsbPort getParentUsbPort () throws javax.usb.UsbDisconnectedException
  1025 boolean isUsbHub ()
  1025 java.lang.String getManufacturerString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException,javax.usb.UsbDisconnectedException
  1025 java.lang.String getSerialNumberString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException,javax.usb.UsbDisconnectedException
  1025 java.lang.String getProductString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException,javax.usb.UsbDisconnectedException
  1025 java.lang.Object getSpeed ()
  1025 java.util.List getUsbConfigurations ()
  1025 javax.usb.UsbConfiguration getUsbConfiguration (byte)
  1025 boolean containsUsbConfiguration (byte)
  1025 byte getActiveUsbConfigurationNumber ()
  1025 javax.usb.UsbConfiguration getActiveUsbConfiguration ()
  1025 boolean isConfigured ()
  1025 javax.usb.UsbDeviceDescriptor getUsbDeviceDescriptor ()
  1025 javax.usb.UsbStringDescriptor getUsbStringDescriptor (byte) throws javax.usb.UsbException,javax.usb.UsbDisconnectedException
  1025 java.lang.String getString (byte) throws javax.usb.UsbException,java.io.UnsupportedEncodingException,javax.usb.UsbDisconnectedException
  1025 void syncSubmit (javax.usb.UsbControlIrp) throws javax.usb.UsbException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void asyncSubmit (javax.usb.UsbControlIrp) throws javax.usb.UsbException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void syncSubmit (java.util.List) throws javax.usb.UsbException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void asyncSubmit (java.util.List) throws javax.usb.UsbException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 javax.usb.UsbControlIrp createUsbControlIrp (byte,byte,short,short)
  1025 void addUsbDeviceListener (javax.usb.event.UsbDeviceListener)
  1025 void removeUsbDeviceListener (javax.usb.event.UsbDeviceListener)
}
1537 javax.usb.UsbDeviceDescriptor implements javax.usb.UsbDescriptor
{
  1025 short bcdUSB ()
  1025 byte bDeviceClass ()
  1025 byte bDeviceSubClass ()
  1025 byte bDeviceProtocol ()
  1025 byte bMaxPacketSize0 ()
  1025 short idVendor ()
  1025 short idProduct ()
  1025 short bcdDevice ()
  1025 byte iManufacturer ()
  1025 byte iProduct ()
  1025 byte iSerialNumber ()
  1025 byte bNumConfigurations ()
}
1 javax.usb.UsbDisconnectedException extends java.lang.RuntimeException
{
  1 javax.usb.UsbDisconnectedException (java.lang.String)
  1 javax.usb.UsbDisconnectedException ()
}
1537 javax.usb.UsbEndpoint
{
  1025 javax.usb.UsbInterface getUsbInterface ()
  1025 javax.usb.UsbEndpointDescriptor getUsbEndpointDescriptor ()
  1025 byte getDirection ()
  1025 byte getType ()
  1025 javax.usb.UsbPipe getUsbPipe ()
}
1537 javax.usb.UsbEndpointDescriptor implements javax.usb.UsbDescriptor
{
  1025 byte bEndpointAddress ()
  1025 byte bmAttributes ()
  1025 short wMaxPacketSize ()
  1025 byte bInterval ()
}
1 javax.usb.UsbException extends java.lang.Exception
{
  1 javax.usb.UsbException (java.lang.String)
  1 javax.usb.UsbException ()
}
17 javax.usb.UsbHostManager extends java.lang.Object
{
  2 javax.usb.UsbHostManager ()
  9 javax.usb.UsbServices getUsbServices () throws javax.usb.UsbException,java.lang.SecurityException
  9 java.util.Properties getProperties () throws javax.usb.UsbException,java.lang.SecurityException
  10 javax.usb.UsbServices createUsbServices () throws javax.usb.UsbException,java.lang.SecurityException
  10 void setupProperties () throws javax.usb.UsbException,java.lang.SecurityException
  26 java.lang.String USBSERVICES_PROPERTY_NOT_DEFINED ()
  26 java.lang.String USBSERVICES_CLASSNOTFOUNDEXCEPTION (java.lang.String)
  26 java.lang.String USBSERVICES_EXCEPTIONININITIALIZERERROR (java.lang.String)
  26 java.lang.String USBSERVICES_INSTANTIATIONEXCEPTION (java.lang.String)
  26 java.lang.String USBSERVICES_ILLEGALACCESSEXCEPTION (java.lang.String)
  26 java.lang.String USBSERVICES_CLASSCASTEXCEPTION (java.lang.String)
}
1537 javax.usb.UsbHub implements javax.usb.UsbDevice
{
  1025 byte getNumberOfPorts ()
  1025 java.util.List getUsbPorts ()
  1025 javax.usb.UsbPort getUsbPort (byte)
  1025 java.util.List getAttachedUsbDevices ()
  1025 boolean isRootUsbHub ()
}
1537 javax.usb.UsbInterface
{
  1025 void claim () throws javax.usb.UsbClaimException,javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbDisconnectedException
  1025 void claim (javax.usb.UsbInterfacePolicy) throws javax.usb.UsbClaimException,javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbDisconnectedException
  1025 void release () throws javax.usb.UsbClaimException,javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbDisconnectedException
  1025 boolean isClaimed ()
  1025 boolean isActive ()
  1025 int getNumSettings ()
  1025 byte getActiveSettingNumber () throws javax.usb.UsbNotActiveException
  1025 javax.usb.UsbInterface getActiveSetting () throws javax.usb.UsbNotActiveException
  1025 javax.usb.UsbInterface getSetting (byte)
  1025 boolean containsSetting (byte)
  1025 java.util.List getSettings ()
  1025 java.util.List getUsbEndpoints ()
  1025 javax.usb.UsbEndpoint getUsbEndpoint (byte)
  1025 boolean containsUsbEndpoint (byte)
  1025 javax.usb.UsbConfiguration getUsbConfiguration ()
  1025 javax.usb.UsbInterfaceDescriptor getUsbInterfaceDescriptor ()
  1025 java.lang.String getInterfaceString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException,javax.usb.UsbDisconnectedException
}
1537 javax.usb.UsbInterfaceDescriptor implements javax.usb.UsbDescriptor
{
  1025 byte bInterfaceNumber ()
  1025 byte bAlternateSetting ()
  1025 byte bNumEndpoints ()
  1025 byte bInterfaceClass ()
  1025 byte bInterfaceSubClass ()
  1025 byte bInterfaceProtocol ()
  1025 byte iInterface ()
}
1537 javax.usb.UsbInterfacePolicy
{
  1025 boolean forceClaim (javax.usb.UsbInterface)
}
1537 javax.usb.UsbIrp
{
  1025 byte[] getData ()
  1025 int getOffset ()
  1025 int getLength ()
  1025 int getActualLength ()
  1025 void setData (byte[])
  1025 void setData (byte[],int,int)
  1025 void setOffset (int)
  1025 void setLength (int)
  1025 void setActualLength (int)
  1025 boolean isUsbException ()
  1025 javax.usb.UsbException getUsbException ()
  1025 void setUsbException (javax.usb.UsbException)
  1025 boolean getAcceptShortPacket ()
  1025 void setAcceptShortPacket (boolean)
  1025 boolean isComplete ()
  1025 void setComplete (boolean)
  1025 void complete ()
  1025 void waitUntilComplete ()
  1025 void waitUntilComplete (long)
}
1 javax.usb.UsbNativeClaimException extends javax.usb.UsbClaimException
{
  1 javax.usb.UsbNativeClaimException (java.lang.String)
  1 javax.usb.UsbNativeClaimException ()
}
1 javax.usb.UsbNotActiveException extends java.lang.RuntimeException
{
  1 javax.usb.UsbNotActiveException (java.lang.String)
  1 javax.usb.UsbNotActiveException ()
}
1 javax.usb.UsbNotClaimedException extends java.lang.RuntimeException
{
  1 javax.usb.UsbNotClaimedException (java.lang.String)
  1 javax.usb.UsbNotClaimedException ()
}
1 javax.usb.UsbNotOpenException extends java.lang.RuntimeException
{
  1 javax.usb.UsbNotOpenException (java.lang.String)
  1 javax.usb.UsbNotOpenException ()
}
1 javax.usb.UsbPIDException extends javax.usb.UsbException
{
  1 javax.usb.UsbPIDException (java.lang.String)
  1 javax.usb.UsbPIDException ()
}
1537 javax.usb.UsbPipe
{
  1025 void open () throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotClaimedException,javax.usb.UsbDisconnectedException
  1025 void close () throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,javax.usb.UsbDisconnectedException
  1025 boolean isActive ()
  1025 boolean isOpen ()
  1025 javax.usb.UsbEndpoint getUsbEndpoint ()
  1025 int syncSubmit (byte[]) throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 javax.usb.UsbIrp asyncSubmit (byte[]) throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void syncSubmit (javax.usb.UsbIrp) throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void asyncSubmit (javax.usb.UsbIrp) throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void syncSubmit (java.util.List) throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void asyncSubmit (java.util.List) throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,java.lang.IllegalArgumentException,javax.usb.UsbDisconnectedException
  1025 void abortAllSubmissions () throws javax.usb.UsbNotActiveException,javax.usb.UsbNotOpenException,javax.usb.UsbDisconnectedException
  1025 javax.usb.UsbIrp createUsbIrp ()
  1025 javax.usb.UsbControlIrp createUsbControlIrp (byte,byte,short,short)
  1025 void addUsbPipeListener (javax.usb.event.UsbPipeListener)
  1025 void removeUsbPipeListener (javax.usb.event.UsbPipeListener)
}
1 javax.usb.UsbPlatformException extends javax.usb.UsbException
{
  1 javax.usb.UsbPlatformException (java.lang.String,int,java.lang.Exception)
  1 javax.usb.UsbPlatformException (int,java.lang.Exception)
  1 javax.usb.UsbPlatformException (java.lang.String,java.lang.Exception)
  1 javax.usb.UsbPlatformException (java.lang.String,int)
  1 javax.usb.UsbPlatformException (java.lang.Exception)
  1 javax.usb.UsbPlatformException (int)
  1 javax.usb.UsbPlatformException (java.lang.String)
  1 javax.usb.UsbPlatformException ()
  1 java.lang.Exception getPlatformException ()
  1 int getErrorCode ()
}
1537 javax.usb.UsbPort
{
  1025 byte getPortNumber ()
  1025 javax.usb.UsbHub getUsbHub ()
  1025 javax.usb.UsbDevice getUsbDevice ()
  1025 boolean isUsbDeviceAttached ()
}
1537 javax.usb.UsbServices
{
  1025 javax.usb.UsbHub getRootUsbHub () throws javax.usb.UsbException,java.lang.SecurityException
  1025 void addUsbServicesListener (javax.usb.event.UsbServicesListener)
  1025 void removeUsbServicesListener (javax.usb.event.UsbServicesListener)
  1025 java.lang.String getApiVersion ()
  1025 java.lang.String getImpVersion ()
  1025 java.lang.String getImpDescription ()
}
1 javax.usb.UsbShortPacketException extends javax.usb.UsbException
{
  1 javax.usb.UsbShortPacketException (java.lang.String)
  1 javax.usb.UsbShortPacketException ()
}
1 javax.usb.UsbStallException extends javax.usb.UsbException
{
  1 javax.usb.UsbStallException (java.lang.String)
  1 javax.usb.UsbStallException ()
}
1537 javax.usb.UsbStringDescriptor implements javax.usb.UsbDescriptor
{
  1025 byte[] bString ()
  1025 java.lang.String getString () throws java.io.UnsupportedEncodingException
}
1 javax.usb.Version extends java.lang.Object
{
  1 javax.usb.Version ()
  9 void main (java.lang.String[])
  9 java.lang.String getApiVersion ()
  9 java.lang.String getUsbVersion ()
}
1 javax.usb.event.UsbDeviceDataEvent extends javax.usb.event.UsbDeviceEvent
{
  1 javax.usb.event.UsbDeviceDataEvent (javax.usb.UsbDevice,javax.usb.UsbControlIrp)
  1 byte[] getData ()
  1 javax.usb.UsbControlIrp getUsbControlIrp ()
}
1 javax.usb.event.UsbDeviceErrorEvent extends javax.usb.event.UsbDeviceEvent
{
  1 javax.usb.event.UsbDeviceErrorEvent (javax.usb.UsbDevice,javax.usb.UsbControlIrp)
  1 javax.usb.UsbException getUsbException ()
  1 javax.usb.UsbControlIrp getUsbControlIrp ()
}
1 javax.usb.event.UsbDeviceEvent extends java.util.EventObject
{
  1 javax.usb.event.UsbDeviceEvent (javax.usb.UsbDevice)
  1 javax.usb.UsbDevice getUsbDevice ()
}
1537 javax.usb.event.UsbDeviceListener implements java.util.EventListener
{
  1025 void usbDeviceDetached (javax.usb.event.UsbDeviceEvent)
  1025 void errorEventOccurred (javax.usb.event.UsbDeviceErrorEvent)
  1025 void dataEventOccurred (javax.usb.event.UsbDeviceDataEvent)
}
1 javax.usb.event.UsbPipeDataEvent extends javax.usb.event.UsbPipeEvent
{
  1 javax.usb.event.UsbPipeDataEvent (javax.usb.UsbPipe,javax.usb.UsbIrp)
  1 javax.usb.event.UsbPipeDataEvent (javax.usb.UsbPipe,byte[],int)
  1 byte[] getData ()
  1 int getActualLength ()
}
1 javax.usb.event.UsbPipeErrorEvent extends javax.usb.event.UsbPipeEvent
{
  1 javax.usb.event.UsbPipeErrorEvent (javax.usb.UsbPipe,javax.usb.UsbIrp)
  1 javax.usb.event.UsbPipeErrorEvent (javax.usb.UsbPipe,javax.usb.UsbException)
  1 javax.usb.UsbException getUsbException ()
}
1 javax.usb.event.UsbPipeEvent extends java.util.EventObject
{
  1 javax.usb.event.UsbPipeEvent (javax.usb.UsbPipe,javax.usb.UsbIrp)
  1 javax.usb.event.UsbPipeEvent (javax.usb.UsbPipe)
  1 javax.usb.UsbPipe getUsbPipe ()
  1 boolean hasUsbIrp ()
  1 javax.usb.UsbIrp getUsbIrp ()
}
1537 javax.usb.event.UsbPipeListener implements java.util.EventListener
{
  1025 void errorEventOccurred (javax.usb.event.UsbPipeErrorEvent)
  1025 void dataEventOccurred (javax.usb.event.UsbPipeDataEvent)
}
1 javax.usb.event.UsbServicesEvent extends java.util.EventObject
{
  1 javax.usb.event.UsbServicesEvent (javax.usb.UsbServices,javax.usb.UsbDevice)
  1 javax.usb.UsbServices getUsbServices ()
  1 javax.usb.UsbDevice getUsbDevice ()
}
1537 javax.usb.event.UsbServicesListener implements java.util.EventListener
{
  1025 void usbDeviceAttached (javax.usb.event.UsbServicesEvent)
  1025 void usbDeviceDetached (javax.usb.event.UsbServicesEvent)
}
1 javax.usb.util.DefaultUsbControlIrp extends javax.usb.util.DefaultUsbIrp implements javax.usb.UsbControlIrp
{
  1 javax.usb.util.DefaultUsbControlIrp (byte[],int,int,boolean,byte,byte,short,short)
  1 javax.usb.util.DefaultUsbControlIrp (byte,byte,short,short)
  1 byte bmRequestType ()
  1 byte bRequest ()
  1 short wValue ()
  1 short wIndex ()
  1 short wLength ()
}
1 javax.usb.util.DefaultUsbIrp extends java.lang.Object implements javax.usb.UsbIrp
{
  1 javax.usb.util.DefaultUsbIrp (byte[],int,int,boolean)
  1 javax.usb.util.DefaultUsbIrp (byte[])
  1 javax.usb.util.DefaultUsbIrp ()
  1 byte[] getData ()
  1 int getOffset ()
  1 int getLength ()
  1 int getActualLength ()
  1 void setData (byte[],int,int) throws java.lang.IllegalArgumentException
  1 void setData (byte[]) throws java.lang.IllegalArgumentException
  1 void setOffset (int) throws java.lang.IllegalArgumentException
  1 void setLength (int) throws java.lang.IllegalArgumentException
  1 void setActualLength (int) throws java.lang.IllegalArgumentException
  1 boolean isUsbException ()
  1 javax.usb.UsbException getUsbException ()
  1 void setUsbException (javax.usb.UsbException)
  1 boolean getAcceptShortPacket ()
  1 void setAcceptShortPacket (boolean)
  1 boolean isComplete ()
  1 void setComplete (boolean)
  1 void complete ()
  1 void waitUntilComplete ()
  1 void waitUntilComplete (long)
}
1 javax.usb.util.StandardRequest extends java.lang.Object
{
  1 javax.usb.util.StandardRequest (javax.usb.UsbDevice)
  1 void clearFeature (byte,short,short) throws javax.usb.UsbException,java.lang.IllegalArgumentException
  1 byte getConfiguration () throws javax.usb.UsbException
  1 int getDescriptor (byte,byte,short,byte[]) throws javax.usb.UsbException
  1 byte getInterface (short) throws javax.usb.UsbException
  1 short getStatus (byte,short) throws javax.usb.UsbException,java.lang.IllegalArgumentException
  1 void setAddress (short) throws javax.usb.UsbException
  1 void setConfiguration (short) throws javax.usb.UsbException
  1 int setDescriptor (byte,byte,short,byte[]) throws javax.usb.UsbException
  1 void setFeature (byte,short,short) throws javax.usb.UsbException,java.lang.IllegalArgumentException
  1 void setInterface (short,short) throws javax.usb.UsbException
  1 short synchFrame (short) throws javax.usb.UsbException
  9 void clearFeature (javax.usb.UsbDevice,byte,short,short) throws javax.usb.UsbException,java.lang.IllegalArgumentException
  9 byte getConfiguration (javax.usb.UsbDevice) throws javax.usb.UsbException
  9 int getDescriptor (javax.usb.UsbDevice,byte,byte,short,byte[]) throws javax.usb.UsbException
  9 byte getInterface (javax.usb.UsbDevice,short) throws javax.usb.UsbException
  9 short getStatus (javax.usb.UsbDevice,byte,short) throws javax.usb.UsbException,java.lang.IllegalArgumentException
  9 void setAddress (javax.usb.UsbDevice,short) throws javax.usb.UsbException
  9 void setConfiguration (javax.usb.UsbDevice,short) throws javax.usb.UsbException
  9 int setDescriptor (javax.usb.UsbDevice,byte,byte,short,byte[]) throws javax.usb.UsbException
  9 void setFeature (javax.usb.UsbDevice,byte,short,short) throws javax.usb.UsbException,java.lang.IllegalArgumentException
  9 void setInterface (javax.usb.UsbDevice,short,short) throws javax.usb.UsbException
  9 short synchFrame (javax.usb.UsbDevice,short) throws javax.usb.UsbException
  12 void checkRecipient (byte) throws java.lang.IllegalArgumentException
}
1 javax.usb.util.UsbUtil extends java.lang.Object
{
  1 javax.usb.util.UsbUtil ()
  9 short unsignedShort (byte)
  9 int unsignedInt (byte)
  9 int unsignedInt (short)
  9 long unsignedLong (byte)
  9 long unsignedLong (short)
  9 long unsignedLong (int)
  9 short toShort (byte,byte)
  9 int toInt (byte,byte,byte,byte)
  9 long toLong (byte,byte,byte,byte,byte,byte,byte,byte)
  9 int toInt (short,short)
  9 long toLong (short,short,short,short)
  9 long toLong (int,int)
  9 java.lang.String toHexString (byte)
  9 java.lang.String toHexString (short)
  9 java.lang.String toHexString (int)
  9 java.lang.String toHexString (long)
  9 java.lang.String toHexString (long,char,int,int)
  9 java.lang.String toHexString (java.lang.String,byte[],int)
  9 java.lang.String toHexString (java.lang.String,short[],int)
  9 java.lang.String toHexString (java.lang.String,int[],int)
  9 java.lang.String toHexString (java.lang.String,long[],int)
  9 java.lang.String toHexString (java.lang.String,byte[])
  9 java.lang.String toHexString (java.lang.String,short[])
  9 java.lang.String toHexString (java.lang.String,int[])
  9 java.lang.String toHexString (java.lang.String,long[])
  9 java.lang.String getSpeedString (java.lang.Object)
  9 javax.usb.UsbDevice synchronizedUsbDevice (javax.usb.UsbDevice)
  9 javax.usb.UsbPipe synchronizedUsbPipe (javax.usb.UsbPipe)
}
9 javax.usb.util.UsbUtil$SynchronizedUsbDevice extends java.lang.Object implements javax.usb.UsbDevice
{
  1 javax.usb.util.UsbUtil$SynchronizedUsbDevice (javax.usb.UsbDevice)
  1 javax.usb.UsbPort getParentUsbPort ()
  1 boolean isUsbHub ()
  1 java.lang.String getManufacturerString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException
  1 java.lang.String getSerialNumberString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException
  1 java.lang.String getProductString () throws javax.usb.UsbException,java.io.UnsupportedEncodingException
  1 java.lang.Object getSpeed ()
  1 java.util.List getUsbConfigurations ()
  1 javax.usb.UsbConfiguration getUsbConfiguration (byte)
  1 boolean containsUsbConfiguration (byte)
  1 byte getActiveUsbConfigurationNumber ()
  1 javax.usb.UsbConfiguration getActiveUsbConfiguration ()
  1 boolean isConfigured ()
  1 javax.usb.UsbDeviceDescriptor getUsbDeviceDescriptor ()
  1 javax.usb.UsbStringDescriptor getUsbStringDescriptor (byte) throws javax.usb.UsbException
  1 java.lang.String getString (byte) throws javax.usb.UsbException,java.io.UnsupportedEncodingException
  1 void syncSubmit (javax.usb.UsbControlIrp) throws javax.usb.UsbException
  1 void asyncSubmit (javax.usb.UsbControlIrp) throws javax.usb.UsbException
  1 void syncSubmit (java.util.List) throws javax.usb.UsbException
  1 void asyncSubmit (java.util.List) throws javax.usb.UsbException
  1 javax.usb.UsbControlIrp createUsbControlIrp (byte,byte,short,short)
  1 void addUsbDeviceListener (javax.usb.event.UsbDeviceListener)
  1 void removeUsbDeviceListener (javax.usb.event.UsbDeviceListener)
}
9 javax.usb.util.UsbUtil$SynchronizedUsbPipe extends java.lang.Object implements javax.usb.UsbPipe
{
  1 javax.usb.util.UsbUtil$SynchronizedUsbPipe (javax.usb.UsbPipe)
  1 void open () throws javax.usb.UsbException,javax.usb.UsbNotActiveException,javax.usb.UsbNotClaimedException
  1 void close () throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 boolean isActive ()
  1 boolean isOpen ()
  1 javax.usb.UsbEndpoint getUsbEndpoint ()
  1 int syncSubmit (byte[]) throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 javax.usb.UsbIrp asyncSubmit (byte[]) throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 void syncSubmit (javax.usb.UsbIrp) throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 void asyncSubmit (javax.usb.UsbIrp) throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 void syncSubmit (java.util.List) throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 void asyncSubmit (java.util.List) throws javax.usb.UsbException,javax.usb.UsbNotOpenException
  1 void abortAllSubmissions () throws javax.usb.UsbNotOpenException
  1 javax.usb.UsbIrp createUsbIrp ()
  1 javax.usb.UsbControlIrp createUsbControlIrp (byte,byte,short,short)
  1 void addUsbPipeListener (javax.usb.event.UsbPipeListener)
  1 void removeUsbPipeListener (javax.usb.event.UsbPipeListener)
}
