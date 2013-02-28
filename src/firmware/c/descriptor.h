#if USB_CFG_DESCR_PROPS_CONFIGURATION != 0

char usbDescriptorConfiguration[] = {    /* USB configuration descriptor */
    9,          /* sizeof(usbDescriptorConfiguration): length of descriptor in bytes */
    USBDESCR_CONFIG, /* descriptor type */
    18 + 2 * 7, 0,
                /* total length of data returned (including inlined descriptors) */
    1,          /* number of interfaces in this configuration */
    1,          /* index of this configuration */
    0,          /* configuration name string index */
    (1 << 7) | (1 << 5),                /* attributes */
    USB_CFG_MAX_BUS_POWER/2,            /* max USB current in 2mA units */

    9,          /* sizeof(usbDescrInterface): length of descriptor in bytes */
    USBDESCR_INTERFACE, /* descriptor type */
    0,          /* index of this interface */
    0,          /* alternate setting for this interface */
    2,          /* endpoints excl 0: number of endpoint descriptors to follow */
    USB_CFG_INTERFACE_CLASS,
    USB_CFG_INTERFACE_SUBCLASS,
    USB_CFG_INTERFACE_PROTOCOL,
    0,          /* string index for interface */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x81, /* IN endpoint number 1 */
//    0x03,       /* attrib: Interrupt endpoint */
//    64, 0,       /* maximum packet size */
//    USB_CFG_INTR_POLL_INTERVAL, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x01, /* OUT endpoint number 2 */
//    0x03,       /* attrib: Interrupt endpoint */
//    64, 0,       /* maximum packet size */
//    USB_CFG_INTR_POLL_INTERVAL, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x82, /* IN endpoint number 1 */
//    0x03,       /* attrib: Interrupt endpoint */
//    8, 0,       /* maximum packet size */
//    USB_CFG_INTR_POLL_INTERVAL, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x02, /* OUT endpoint number 2 */
//    0x03,       /* attrib: Interrupt endpoint */
//    8, 0,       /* maximum packet size */
//    USB_CFG_INTR_POLL_INTERVAL, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x83, /* IN endpoint number 1 */
//    0x02,       /* attrib: Interrupt endpoint */
//    64, 0,       /* maximum packet size */
//    0, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x03, /* OUT endpoint number 2 */
//    0x02,       /* attrib: Interrupt endpoint */
//    64, 0,       /* maximum packet size */
//    0, /* in ms */

    7,          /* sizeof(usbDescrEndpoint) */
    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
    (char)0x84, /* IN endpoint number 1 */
    0x02,       /* attrib: Interrupt endpoint */
    8, 0,       /* maximum packet size */
    0, /* in ms */

    7,          /* sizeof(usbDescrEndpoint) */
    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
    (char)0x04, /* OUT endpoint number 2 */
    0x02,       /* attrib: Interrupt endpoint */
    8, 0,       /* maximum packet size */
    0, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x88, /* IN endpoint number 1 */
//    0x01,       /* attrib: Interrupt endpoint */
//    8, 0,       /* maximum packet size */
//    1, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x08, /* OUT endpoint number 2 */
//    0x01,       /* attrib: Interrupt endpoint */
//    8, 0,       /* maximum packet size */
//    1, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x09, /* OUT endpoint number 2 */
//    0x00,       /* attrib: Interrupt endpoint */
//    8, 0,       /* maximum packet size */
//    0, /* in ms */

//    7,          /* sizeof(usbDescrEndpoint) */
//    USBDESCR_ENDPOINT, /* descriptor type = endpoint */
//    (char)0x89, /* OUT endpoint number 2 */
//    0x00,       /* attrib: Interrupt endpoint */
//    8, 0,       /* maximum packet size */
//    0, /* in ms */
};

#endif
