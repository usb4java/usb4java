/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md file for licensing information.
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

/**  Blinks memory size */
#define MEM_SIZE 16

/** The flash checksum constant (1010 0101) */
#define FLASH_CHECKSUM 0xa5

/** Data transfer write index */
static unsigned usbWriteIndex;

/** Data transfer write length */
static unsigned usbWriteLength;

/** The blinks memory */
static unsigned char memory[MEM_SIZE];

/** The flash checksum flag in EEPROM */
static uint8_t flashChecksum = 0;

static unsigned char buffer[512];

static int buffer_size;

static int buffer_read_index = 0;

static int buffer_write_index = 0;

static int dropper_index = 0;

static int to_read = 0;

static int to_write = 0;

static unsigned char buffer_transform;

static uint16_t restart;

/**
 * Reads the flash checksum into RAM.
 */
static void readFlashChecksum() 
{
    eeprom_busy_wait();
    flashChecksum = eeprom_read_byte((uint8_t *) 0);
}

/**
 * Writes the flash checksum to EEPROM.
 */
static void writeFlashChecksum()
{
    eeprom_busy_wait();
    eeprom_write_byte((uint8_t *) 0, flashChecksum);
}


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
        case 0x14:
            return 0xff;

        /* Go into flash mode */
        case 0x2:
            restart--;
            memory[0] &= 0xbf;
            break;

        case 0x1:            
        case 0xb0:
            if (usbData->bmRequestType & USBRQ_DIR_DEVICE_TO_HOST)
            {
                /* Read mode */
                usbMsgPtr = &buffer[usbData->wIndex.word];    
                return usbData->wLength.word > buffer_size ? buffer_size : usbData->wLength.word;
            }
            else
            {
                /* Write mode */
                usbWriteIndex = usbData->wIndex.word;
                usbWriteLength = usbData->wLength.word;
                buffer_size = usbData->wLength.word;
                return USB_NO_MSG;
            }
            break;
            
        case 0xa8:
            if (!(usbData->bmRequestType & USBRQ_DIR_DEVICE_TO_HOST))
            {
                pwmSet(CHANNEL0, 255, 0);
                pwmSet(CHANNEL1, 255, 0);
                pwmSet(CHANNEL2, 255, 0);
                restart--;
                return 0;
            }
            break;
            
        default:
            break;
            
            
    }

    return 0;
}

void usbFunctionWriteOut(uchar *data, uchar len)
{
    int i;

    // Check if this is a new data block
    if (!to_write)
    {
        buffer_transform = data[0];
        to_write = (((int) data[1]) << 8) | data[2];
        dropper_index = 0;
    }
        
    // Copy and transform data
    for (i = 0; i < len; i++)
    {
        switch (buffer_transform)
        {
            case 1:
                buffer[buffer_write_index] = data[i];
                to_write--;
                buffer_write_index++;
                break;
                
            case 2:
                buffer[buffer_write_index] = data[i] ^ 0xff;
                to_write--;
                buffer_write_index++;
                break;
                
            case 3:
                buffer[buffer_write_index] = data[i] ^ 0x55;
                to_write--;
                buffer_write_index++;
                break;
                
            case 4:
                if (dropper_index != 2)
                {
                    buffer[buffer_write_index] = data[i] ^ 0x55;
                    buffer_write_index++;
                    dropper_index++;
                } else dropper_index = 0;
                to_write--;
                break;
                
            default:
                pwmSet(CHANNEL0, 255, 0);
                pwmSet(CHANNEL1, 0, 0);
                pwmSet(CHANNEL2, 0, 0);
        }
    }
    
    // When write is complete then start reading
    if (!to_write)
    {
        to_read = buffer_write_index;
    }
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
 * Enters the boot loader.
 */
static void enterBootloader()
{
    /* Invalidate the checksum in flash */
    flashChecksum = FLASH_CHECKSUM ^ 0xff;
    writeFlashChecksum();

    /* Enable the watchdog and enter an endless loop. This will reboot
     the device */
    cli();
    wdt_enable(WDTO_500MS);
    while (1);
}

/**
 * Main program.
 */
int main()
{   
    uchar i;
    
    /* Disable the watchdog */
    MCUSR = 0;
    wdt_disable();

    /* Read the flash checksum from EEPROM */
    readFlashChecksum();
            
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
    
    /* Initialize the blinks memory from eeprom */
    eeprom_busy_wait();
    eeprom_read_block(memory, (uint8_t*) 1, MEM_SIZE);
    
    /* Make sure bits 7 is cleared and bit 6 is set */
    memory[0] &= 0x7f;
    memory[0] |= 0x40;
    
    /* Infinite program loop */
    i = 0;
    while (memory[0] & 0x40)
    {
        restart = 50;
        while (restart)
        {
            /* Process USB events */
            usbPoll();
            
            int len = buffer_write_index - buffer_read_index;
            if (len > 0 && usbInterruptIsReady())
            {           
                if (len > 8) len = 8;
                usbSetInterrupt(&buffer[buffer_read_index], len);
                buffer_read_index += len;
                to_read -= len;
                if (!to_read)
                {
                    buffer_read_index = 0;
                    buffer_write_index = 0;
                }
            }
            
            if (restart < 50) restart--;

            /* When this loop has been executed for 100 times then
               mark the flash as OK */
            if ((flashChecksum != FLASH_CHECKSUM) && (i < 100))
            {
                i++;
                if (i == 100)
                {
                    flashChecksum = FLASH_CHECKSUM;
                    writeFlashChecksum();
                }
            }
        }
        reenumerate(500);
    }

    enterBootloader();

    return 0;
}
