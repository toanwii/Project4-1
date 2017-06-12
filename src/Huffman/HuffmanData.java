/*
 * HuffmanData.java
 */

package huffman;

/**
 * Holds a data of type T, and its number of occurrences.
 * 
 * @param <T>
 * 
 * 
 * @author Paul Bladek
 * @version 1.0
 * 
 * Compiler: Java 1.8.0_111
 * OS: Windows 10
 * Hardware: PC
 */
public class HuffmanData<T extends Comparable<? super T>>
        implements Comparable<HuffmanData<T>> {

    private T data;
    private int occurances = 0;

    /**
     * Creates a new instance of HuffmanData.
     */
    public HuffmanData() {
    }

    /**
     * Creates a new instance of HuffmanData using the passed data.
     *
     * @param dataIn the data part
     */
    public HuffmanData(T dataIn) {
        data = dataIn;
    }

    /**
     * Creates a new instance of HuffmanData using the passed data and number
     * of occurrences.
     *
     * @param dataIn the data part
     * @param count the number of occurrences
     */
    public HuffmanData(T dataIn, int count) {
        this(dataIn);
        occurances = count;
    }

    /**
     * Returns the data.
     * 
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Returns the number of occurrences.
     * 
     * @return the number of occurrences
     */
    public int getOccurances() {
        return occurances;
    }

    /**
     * Compare this to another HuffmanData object.
     * 
     * @param o the other HuffmanData
     * @return -1 if <, 0 if ==, 1 if >
     */
    @Override
    public int compareTo(HuffmanData<T> o) {
        return Integer.compare(occurances, o.occurances);
    }

    /**
     * Returns a string of the data and its number of occurrences.
     * 
     * @return string version of object
     */
    @Override
    public String toString() {
        String dataString = "*";
        if (data != null) {
            dataString = data.toString();
        }
        return dataString + ": " + occurances + " ";
    }
}
