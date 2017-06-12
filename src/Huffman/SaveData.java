/*
 * SaveDate.java
 */

package huffman;
import java.io.*;
/**
 * The class for saving the initial sorted array of data/occurrences.
 * 
 * @author Paul Bladek
 * @version 1.0
 * 
 * Compiler: Java 1.8.0_111
 * OS: Windows 10
 * Hardware: PC
 */
public class SaveData implements Serializable
{
    private char data = '\0';
    private short occurrances = 0;

    /**
     * Creates a new instance of SaveDate
     */
    public SaveData() {}
    
    /** 
     * Creates a new instance of SaveDate.
     * 
     * @param c the data char
     * @param o the number of occurrences
     */
    public SaveData(char c, short o)
    {
        data = c;
        occurrances = o;
    }

    /**
     * Returns the data.
     * 
     * @return the data
     */
    public char getData()
    {
        return data;
    }

    /**
     * Returns the occurrences.
     * 
     * @return the occurrences
     */
    public short getOccurrances()
    {
        return occurrances;
    }
    
    /**
     * Returns the data and its occurrences.
     * 
     * @return a string version of this class
     */
    @Override
    public String toString()
    {
        return data + ":" + occurrances + " ";
    }   
}
