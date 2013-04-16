/*
 * Copyright (C) 2013 Klaus Reimer (k@ailis.de)
 * See COPYING file for copying conditions
 */

#include <string.h>

// Enforce usage of older memcpy to be compatible with older libc versions
#if WRAP_MEMCPY
asm (".symver memcpy, memcpy@GLIBC_2.2.5");
void *__wrap_memcpy(void *dest, const void *src, size_t n)
{
    return memcpy(dest, src, n);
}
#endif
