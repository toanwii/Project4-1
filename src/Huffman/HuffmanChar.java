/*
 * HuffmanChar.java
 */

package huffman;
import java.io.*;

/**
 * 
 * 
 * @author Tien Huynh
 * @author Michael Courter
 * @author Paul Bladek
 * @version 1.1
 * 
 * Compiler: Java 1.8.0_111
 * OS: Windows 10
 * Hardware: PC
 */
public class HuffmanChar extends HuffmanData<Character>
        implements Serializable
{  
    public static final int BITS_IN_BYTE = 8;
    public static final int BYTE_SIZE_NUMBER = 256;
  
    /**
     * Creates a new instance of HuffmanChar.
     */
    public HuffmanChar()
    {
        super();
    }
    
    /**
     * Creates a new instance of HuffmanChar with the passed Character.
     * 
     * @param c the character
     */
    public HuffmanChar(Character c)
    {
        super(c);
    }

    /**
     * Creates a new instance of HuffmanChar with the passed Character, and
     * the number of times it occurred.
     * 
     * @param c the character
     * @param oc the number of occurrences
     */

    public HuffmanChar(Character c, int oc)
    {
        super(c, oc);
    }
    
    /**
     *  Creates a new instance of HuffmanChar using the passed HuffmanChar.
     * 
     * @param hc a HuffmanChar
     */
    public HuffmanChar(HuffmanChar hc)
    {
        super(hc.getData(), hc.getOccurances());
    }
   
    /**
     * Creates a new instance of HuffmanChar, using the bytes in the passed
     * array.
     * 
     * @param threeBytes an array of three bytes
     */
    public HuffmanChar(byte[] threeBytes)
    {
        super(new Character((char)threeBytes[0]),
            ((int)threeBytes[2]) >= 0 ? (int)threeBytes[2] |
                    ((int)threeBytes[1] << BITS_IN_BYTE)
            : ((BYTE_SIZE_NUMBER + (int)threeBytes[2]) +
                    ((int)threeBytes[1] << BITS_IN_BYTE)));
    }
    
    /**
     * returns the class converted to a 3-byte array.
     * 
     * @return the class converted to a 3-byte array
     */
    public byte[] toThreeBytes()
    {
        byte[] ba = new byte[3];
        ba[0] = (byte)(getData().charValue());
        short oc = (short)getOccurances();
        ba[1] = (byte)(oc >> 8);
        ba[2] = (byte)(oc & (byte)(-1));
        return ba; 
    }
}