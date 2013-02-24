/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt file for licensing information.
 */

#include <avr/interrupt.h>
#include <avr/wdt.h>
#include <util/delay.h>
#include <avr/eeprom.h>
#include <avr/pgmspace.h>
#include "pwm/pwm.h"
#include "usbdrv/usbdrv.h"
#include "descriptor.h"

/** Map output channels to port pins */
#define CHANNEL0 PIN_PB1
#define CHANNEL1 PIN_PB2
#define CHANNEL2 PIN_PD6
#define CHANNEL3 PIN_PD5
#define CHANNEL4 PIN_PB3
#define CHANNEL5 PIN_PD3

/** Data transfer write index */
static unsigned usbWriteIndex;

/** Data transfer write length */
static unsigned usbWriteLength;

/** The blinks memory */
static unsigned char buffer[512];

static unsigned char buffer_size;

static uint16_t restart;

static void reenumerate(uint16_t delay)
{
    usbDeviceDisconnect(); 
    do
    {
        _delay_ms(1);
    }
    while(--delay);
    usbDeviceConnect();

    pwmSet(CHANNEL0, 0, 0);
    pwmSet(CHANNEL1, 0, 0);
    pwmSet(CHANNEL2, 255, 0);
}


/**
 * Called when a larger data block is received by the device. It writes
 * the data to the blinks memory.
 *
 * @param data *            The data block
 * @param len
 *            The length of the data block.
 * @return 1 when no more data is expected, 0 if more data is expected, 0xff
 *         on error.
 */
uchar usbFunctionWrite(uchar *data, uchar len)
{
    while (len && usbWriteLength)
    {
        buffer[usbWriteIndex] = data[0];
        usbWriteIndex++;
        data++;
        len--;
        usbWriteLength--;
    }
    return usbWriteLength ? 0 : 1;
}

uchar usbFunctionRead(uchar *data, uchar len)
{
    pwmSet(CHANNEL2, 0, 0);
    pwmSet(CHANNEL1, 0, 0);
    pwmSet(CHANNEL0, 0, 0);
    return 1;
}

/**
 * Called to setup a USB request. In most cases this also handles the whole
 * USB request. Only when a larger data block is to written to the memory
 * then it passes the control to the usbFunctionWrite function.
 */
usbMsgLen_t usbFunctionSetup(uchar setupData[8])
{
    #define usbData ((usbRequest_t *) setupData)
    
    switch (usbData->bRequest)
    {
        case 0xb0:
            if (usbData->bmRequestType & USBRQ_DIR_DEVICE_TO_HOST)
            {
                /* Read mode */
                usbMsgPtr = &buffer[usbData->wIndex.word];
                return usbData->wLength.word;
            }
            else
            {
                /* Write mode */
                usbWriteIndex = usbData->wIndex.word;
                usbWriteLength = usbData->wLength.word;
                return USB_NO_MSG;
            }
            break;
            
        case 0xa8:
            if (!(usbData->bmRequestType & USBRQ_DIR_DEVICE_TO_HOST))
            {
                pwmSet(CHANNEL0, 255, 0);
                pwmSet(CHANNEL1, 0, 0);
                pwmSet(CHANNEL2, 0, 0);
                restart--;
                return 0;
            }
            break;
            
    }

    return 0;
}

void usbFunctionWriteOut(uchar *data, uchar len)
{
    buffer_size = len;
    int i;
    for (i = 0; i < len; i++) buffer[i] = data[i];
}

/**
 * Initializes USB.
 */
static void initUSB(void)
{
    usbInit();
    reenumerate(250);
    sei();
}

/**
 * Main program.
 */
int main()
{   
    /* Disable the watchdog */
    MCUSR = 0;
    wdt_disable();
    
    /* Initialize USB and enable global interrupts */
    initUSB();

    /* First initialize the PWM so all LEDs are off. */
    pwmInit(0);
    pwmSet(CHANNEL0, 0, 0);
    pwmSet(CHANNEL1, 0, 0);
    pwmSet(CHANNEL2, 255, 0);
    pwmSet(CHANNEL3, 0, 0);
    pwmSet(CHANNEL4, 0, 0);
    pwmSet(CHANNEL5, 0, 0);
    
    
    /* Infinite program loop */
    while (1)
    {
        restart = 50;
        while (restart)
        {
            /* Process USB events */
            usbPoll();
            
            
            if (usbInterruptIsReady() && buffer_size)
            {               // only if previous data was sent
                usbSetInterrupt(buffer, buffer_size);
                buffer_size = 0;
            }
            
            if (restart < 50) restart--;
        }
        reenumerate(500);
    }
    return 0;
}
