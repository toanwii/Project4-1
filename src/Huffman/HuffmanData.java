/*
 * HuffmanData.java
 *
 * Created on May 21, 2007, 2:17 PM
 */
package huffman;

import java.lang.*;
import java.util.Comparator;

/**
 * HuffmanData.
 * @author pbladek
 * @param <T> Type that implemented Compareable interface.
 */
public class HuffmanData<T extends Comparable<? super T>>
        implements Comparable<HuffmanData<T>> {

    private T data;
    private int occurances = 0;

    /**
     * Creates a new instance of HuffmanData
     */
    public HuffmanData() {
    }

    /**
     * Creates a new instance of HuffmanData
     *
     * @param dataIn the data part
     */
    public HuffmanData(T dataIn) {
        data = dataIn;
    }

    /**
     * Creates a new instance of HuffmanData
     *
     * @param dataIn the data part
     * @param count the number of occurances
     */
    public HuffmanData(T dataIn, int count) {
        this(dataIn);
        occurances = count;
    }

    /*
     * accessor
     * @return data
     */
    public T getData() {
        return data;
    }

    /*
     * accessor
     * @return occurances
     */
    public int getOccurances() {
        return occurances;
    }

    /**
     * Compare occurance between two HuffmanData classes.
     * @param o the other HuffmanData
     * @return -1 if less than, 0 if equals, 1 if greater than.
     */
    @Override
    public int compareTo(HuffmanData<T> o) {
        return Integer.compare(occurances, o.occurances);
    }

    /*
     * @return strng version of class
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
