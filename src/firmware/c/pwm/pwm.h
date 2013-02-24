/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See COPYING.txt file for licensing information.
 */

#ifndef PWM_H
#define PWM_H

#include "pwmconfig.h"

// Define some understandable aliases for the PWM channels
#define PIN_PD6 OCR0A
#define PIN_PD5 OCR0B
#define PIN_PB1 OCR1AL
#define PIN_PB2 OCR1BL
#define PIN_PB3 OCR2A
#define PIN_PD3 OCR2B

// Defines the pwmSet function to set a pin value
#define pwmSet(pin, value, invert) pin = (unsigned char) (invert ? value : ~value)

extern void pwmInit(unsigned char prescaling);

#endif
