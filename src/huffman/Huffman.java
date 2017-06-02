/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.ArrayList;
import huffman.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.SortedMap;

/**
 *
 * @author tienhuynh
 */
public class Huffman {

    static final int CHARMAX = 128;

    private static int[] count = new int[128];
    private static HuffmanData[] nodes;
    private byte[] byteArray = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//----------------------------------------------------
// used for debugging encoding
//----------------------------------------------------
//        args = new String[1];
//        args[0] = "alice.txt";
//----------------------------------------------------
// used for debugging encoding
//----------------------------------------------------
//        args = new String[2];
//        args[0] = "-d";
//        args[1] = "alice.txt";  
//----------------------------------------------------        
        boolean decode = false;
        String textFileName = "";
        if (args.length > 0) {
            if (args[0].substring(0, 2).toLowerCase().equals("-d")) {
                decode = true;
                if (args.length > 1) {
                    textFileName = args[1];
                }
            } else {
                textFileName = args[0];
            }
        }
        Huffman coder = new Huffman();
        if (!decode) {
            coder.encode(textFileName);
        } else {
            coder.decode(textFileName);
        }
    }

    /*
     * encode
     * @param fileName the file to encode
     */
    public void encode(String fileName) {
        // YOUR CODE HERE
        int[] c = new int[CHARMAX];

        ArrayList<String> story;

        if (TextFileIO.hasFile(fileName)) {
            TextFileIO.readFile(fileName);
            story = TextFileIO.getTextFile();
        } else {
            return;
        }

        for (String line : story) {
            for (int i = 0; i < line.length(); i++) {
                int k = line.charAt(i);
                if (k > -1 && k < CHARMAX) {
                    c[k] += 1;
                }
            }
        }

        int index = 0;
        for (int i = 0; i < CHARMAX; i++) {
            if (c[i] > 0) {
                count[i] = c[i];
                index++;
            }
            System.out.print((char) i);
        }

        nodes = new HuffmanData[index];
        index = 0;
        for (int i = 0; i < CHARMAX; i++) {
            if (count[i] > 0) {
                nodes[index++] = new HuffmanChar((char) i, count[i]);
                System.out.println(nodes[index - 1]);
            }
        }
        //Sort the array, not completed.
        Arrays.sort(nodes);
        System.out.println("====");
        for (int i = 0; i < nodes.length; i++) {
            System.out.println(nodes[i]);
        }
        System.out.println("===");
        HuffmanTree a = new HuffmanTree(nodes);

        String tmp;
        ArrayList<String> encodeLine = new ArrayList<>();
        SortedMap<Character, String> map = a.getCodeMap();
        for (String line : story) {
            tmp = "";
            for (int i = 0; i < line.length(); i++) {
                tmp += map.get(line.charAt(i));
            }
            System.out.println(tmp);
            System.out.println(line);

        }

        System.out.println(a);
        writeEncodedFile(byteArray, fileName);
        writeKeyFile(fileName);
    }

    /*
     * decode
     * @param inFileName the file to decode
     */
    public void decode(String inFileName) {

    }

    /**
     * writeEncodedFile
     *
     * @param bytes bytes for file
     * @param fileName file input
     */
    public void writeEncodedFile(byte[] bytes, String fileName) {

    }

    /**
     * writeKeyFile
     *
     * @param fileName the name of the file to write to
     */
    public void writeKeyFile(String fileName) {

    }
}
