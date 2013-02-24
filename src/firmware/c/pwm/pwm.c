/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt file for licensing information.
 */

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <avr/io.h>
#include "pwm.h"

static int TCCR0CS[] = {
    ((0 << CS02) | (0 << CS01) | (1 << CS00)), // 1
    ((0 << CS02) | (1 << CS01) | (0 << CS00)), // 8
    ((0 << CS02) | (1 << CS01) | (1 << CS00)), // 64
    ((1 << CS02) | (0 << CS01) | (0 << CS00)), // 256
    ((1 << CS02) | (0 << CS01) | (1 << CS00))  // 1024
};

static int TCCR1CS[] = {
    ((0 << CS12) | (0 << CS11) | (1 << CS10)), // 1
    ((0 << CS12) | (1 << CS11) | (0 << CS10)), // 8
    ((0 << CS12) | (1 << CS11) | (1 << CS10)), // 64
    ((1 << CS12) | (0 << CS11) | (0 << CS10)), // 256
    ((1 << CS12) | (0 << CS11) | (1 << CS10))  // 1024
};

static int TCCR2CS[] = {
    ((0 << CS22) | (0 << CS21) | (1 << CS20)), // 1
    ((0 << CS22) | (1 << CS21) | (0 << CS20)), // 8
    ((1 << CS22) | (0 << CS21) | (0 << CS20)), // 64
    ((1 << CS22) | (1 << CS21) | (0 << CS20)), // 256
    ((1 << CS22) | (1 << CS21) | (1 << CS20))  // 1024
};

// Set the compare output mode bit masks for the three timers
#define TCCR0COM ((1 << COM0A1) | (1 << COM0A0) | (1 << COM0B1) | (1 << COM0B0))
#define TCCR1COM ((1 << COM1A1) | (1 << COM1A0) | (1 << COM1B1) | (1 << COM1B0))
#define TCCR2COM ((1 << COM2A1) | (1 << COM2A0) | (1 << COM2B1) | (1 << COM2B0))

/**
 * Initializes the three timers and the six PWM channels.
 *
 * @param prescaling
 *            The prescaling index (0-4)
 *
 * Abbreviations:
 *
 * TCCR = Timer/Counter Control Register
 * PWM  = Pulse Width Modulation
 * WGM  = Waveform Generation Mode
 * COM  = Compare Output Mode
 * OC   = Output Compare pin
 * CS   = Clock Select
 * DDR  = Data Direction Register
 * OCR  = Output Compare Register
 */
void pwmInit(unsigned char prescaling)
{
    // ====================================================================
    // Configure the 16 Bit Timer (TCCR1) for doing PWM on PB1 and PB2
    // ====================================================================

    TCCR1A = 
           // PWM Mode: Fast PWM 8 Bit (Part 1)
             (1 << WGM10) 
           | (0 << WGM11)

           // The configured compare output mode
           | TCCR1COM
           ;


    TCCR1B = 
           // PWM Mode: Fast PWM 8 Bit (Part 2)
             (1 << WGM12)
           | (0 << WGM13)

           // The configured clock selection (Prescaling)
           | TCCR1CS[prescaling]
           ;

    // Enable OC1A PWM Port
    #ifdef USE_PB1
    DDRB |= (1 << PB1);
    #endif

    // Enable OC1B PWM Port
    #ifdef USE_PB2
    DDRB |= (1 << PB2);
    #endif


    // ====================================================================
    // Configure the first 8 Bit Timer (TCCR0) for doing PWM on PD6/PD5
    // ====================================================================

    TCCR0A = 
           // PWM Mode: Fast PWM (Part 1)
             (1 << WGM00)
           | (1 << WGM01)

           // The configured compare output mode
           | TCCR0COM
           ;

    TCCR0B = 
           // PWM Mode: Fast PWM (Part 2)
             (0 << WGM02)

           // The configured clock selection (Prescaling)
           | TCCR0CS[prescaling]
           ;

    // Enable OC0A PWM Port
    #ifdef USE_PD6
    DDRD |= (1 << PD6);
    #endif

    // Enable OC0B PWM Port
    #ifdef USE_PD5
    DDRD |= (1 << PD5);
    #endif



    // ====================================================================
    // Configure the second 8 Bit Timer (TCCR2) for doing PWM on PB3/PD3
    // ====================================================================
    
    TCCR2A = 
           // PWM Mode: Fast PWM (Part 1)
             (1 << WGM20)
           | (1 << WGM21)

           // The configured compare output mode
           | TCCR2COM
           ;

    TCCR2B = 
           // PWM Mode: Fast PWM (Part 2)
             (0 << WGM22)

           // The configured clock selection (Prescaling)
           | TCCR2CS[prescaling]
           ;

    // Enable OC2A PWM Port
    #ifdef USE_PB3
    DDRB |= (1 << PB3);
    #endif

    // Enable OC2B PWM Port
    #ifdef USE_PD3
    DDRD |= (1 << PD3);
    #endif
}
