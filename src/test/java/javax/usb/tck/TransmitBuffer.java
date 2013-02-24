package javax.usb.tck;
 
/*
 * Copyright (c) IBM Corporation, 2004
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License.
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 *
 */
 
 
/**
 * This is the TransmitBuffer class to create a OUT transmitt buffer based on
 * parameters supply to the constructor.
 * It will create an expected buffer to be received on an IN buffer also
 * based on the parameters supplied to the constructor.
 * @author Thanh Ngo
 */

@SuppressWarnings("all")
public class TransmitBuffer extends Object
{
 
    /**
     * Constructor.
     * @param ucTransformType The Data manipilation:
     *        1 - Pass through
     *        2 - Invert every bit starting with the second bit
     *        3 - Invert every other bit (invert odd bits assuming bit numbering begins at 0)
     *        4 - Drop every third byte (ex. OUT - b1, b2, b3, b4, b5, b6 IN - b1, b2, b4, b5)
     *
     * @param uilRPLength The length of the data byte in
     * @exception IllegalArgumentException If the offset, length or data is invalid.
     */
    protected TransmitBuffer(byte ucTransformType, int uilRPLength)
    {
        //System.out.println("type: " + ucTransformType + " length: " + uilRPLength);
        if ( 0 > ucTransformType )
            throw new IllegalArgumentException("Type cannot be negative");
        if ( 0 == ucTransformType )
            throw new IllegalArgumentException("Type cannot be 0");
        if ( 5 < ucTransformType )
            throw new IllegalArgumentException("Valid value are 1 to 4");
        if ( 0 > uilRPLength )
            throw new IllegalArgumentException("Length cannot be negative");
        transformType = ucTransformType;
        rpLength = uilRPLength;
        outBuffer = new byte[rpLength];
        byte fill = 0;
        for ( int i = 0; i < rpLength; i++ )
            outBuffer[i] = fill++;
        outBuffer[0] = (byte)transformType;
        outBuffer[1] = (byte)(rpLength / 256);
        outBuffer[2] = (byte)(rpLength % 256);
        outBuffer[3] = sequenceNumber;
        int pos = 7;
        while ( pos < rpLength )
        {
            outBuffer[pos] = sequenceNumber;
            pos += 8;
        }
        outBuffer[rpLength-1] = sequenceNumber;
        if ( transformType_passthru == ucTransformType )
        {
            inBuffer = new byte[rpLength];
            System.arraycopy(outBuffer, 0, inBuffer,0, inBuffer.length);
        }
        else if ( transformType_invert_every_bit == ucTransformType )
        {
            inBuffer = new byte[rpLength];
            for ( int i = 0; i < inBuffer.length; i++ )
                inBuffer[i] = (byte)(outBuffer[i] ^ 0xff);
        }
        else if ( transformType_invert_every_other_bit == ucTransformType )
        {
            inBuffer = new byte[rpLength];
            for ( int i = 0; i < inBuffer.length; i++ )
                inBuffer[i] = (byte)(outBuffer[i] ^ 0x55);
        }
        else if ( transformType_drop_every_third_byte == ucTransformType )
        {
            int size = rpLength / 3;
            size = rpLength - size;
            inBuffer = new byte[size];
            int outIndex = 0;
            int inIndex = 0;
            while ( outIndex < rpLength )
            {
                inBuffer[inIndex++] = outBuffer[outIndex++];        // 1st byte
                if ( outIndex < rpLength )
                    inBuffer[inIndex++] = outBuffer[outIndex++];    // 2nd byte
                if ( outIndex < rpLength )
                    outIndex++;                                     // 3rd byte , skip
            }
        }
        else
        {
            throw new IllegalArgumentException("Length exceed limit");
        }
        mySequenceNumber =  sequenceNumber;
        sequenceNumber++;    // next Transmitt buffer sequence
    }
 
    //-------------------------------------------------------------------------
    // Public methods
    //
 
    /** compare the IN buffer expected to an IN buffer actually received
     * @param in The Buffer to be compared with IN buffer
     * @return true if the two buffers are the same, false otherwise.
     */
    boolean compareBuffers(byte[] inData)
    {
        if ( inBuffer.length != inData.length )
        {
            //System.out.println("-->length!!!");
            return false;
        }
        for ( int i = 0; i < inData.length; i++ )
        {
            if ( inBuffer[i] != inData[i] )
            {
                //System.out.println("-->False!!! i= " + i + " in: " + inBuffer[i] + " out: " + inData[i]);
                return false;
            }
        }
        return true;
    }
 
    /** compare the OUT buffer to a buffer supplied as a parameter
     * @param outData The buffer to compare with the OUT buffer
     * @return true if the two buffers are the same, false otherwise
     */
    protected boolean compareToOUTBuffer(byte[] outData)
    {
        if ( outBuffer.length != outData.length )
            return false;
        for ( int i = 0; i < outData.length; i++ )
        {
            if ( outBuffer[i] != outData[i] )
            {
                return false;
            }
        }
        return true;
 
    }
 
    /** compare two byte arrays supplied as a parameter.  Length of byte arrays is expected to be equal.
     * @param buffer1 First byte array.
     * @param buffer2 Second byte array.
     * @return   true if the two byte arrays are the same, false otherwise
     */
    protected static boolean compareTwoByteArrays(byte[] buffer1, byte[] buffer2)
    {
        if ( buffer1.length != buffer2.length )
            return false;
        for ( int i = 0; i < buffer1.length; i++ )
        {
            if ( buffer1[i] != buffer2[i] )
            {
                return false;
            }
        }
        return true;
 
    }
 
    /** Compares first "length" number of bytes of two arrays.
     * The caller must ensure that the length does not exceed the
     * length of either buffer1 or buffer2.
     * @param buffer1 First byte array.
     * @param buffer2 Second byte array.
     * @param length  Number of bytes to compare
     * @return  true if the two byte arrays are the same over the range specified, false otherwise
     */
    protected static boolean compareTwoByteArraysForSpecifiedLength(byte[] buffer1, int offset1, byte[] buffer2, int offset2,int length)
    {
 
        for ( int i = 0; i < length; i++ )
        {
            if ( buffer1[i + offset1] != buffer2[i + offset2] )
            {
                return false;
            }
        }
        return true;
 
    }
 
 
 
    /**
     * Get the IN buffer.
     * @return The IN data buffer.
     */
    protected byte[] getInBuffer()
    {
        return inBuffer;
    }
 
    /**
     * Get the OUT buffer.
     * @return The OUT data buffer.
     */
    protected byte[] getOutBuffer()
    {
        return outBuffer;
    }
 
    /**
     * Get the sequenceNumber.
     * @return The sequenceNumber variable.
     */
    protected int getsequenceNumber()
    {
        return mySequenceNumber;
    }
 
 
 
 
    //-------------------------------------------------------------------------
    // Instance variables
    //
    private byte transformType;
    private int rpLength;
    private byte[] inBuffer = new byte[0];
    private byte[] outBuffer = new byte[0];
    private static byte sequenceNumber = 0;
    private byte mySequenceNumber;
    private static final byte transformType_passthru                = 1;
    private static final byte transformType_invert_every_bit        = 2;
    private static final byte transformType_invert_every_other_bit  = 3;
    private static final byte transformType_drop_every_third_byte   = 4;
 
 
}
