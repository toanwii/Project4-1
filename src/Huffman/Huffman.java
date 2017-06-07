/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.ArrayList;
import huffman.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.SortedMap;

/**
 *
 * @author tienhuynh
 */
public class Huffman {
    
    public static final int CHARMAX = 128;
    public static final byte CHARBITS = 7;
    public static final short CHARBITMAX = 128;
    private static final String KEY_FILE_FORMAT = ".cod";
    private static final String ENCODE_FILE_FORMAT = ".huff";
    
    private static int[] count = new int[128];
    private static HuffmanChar[] nodes;
    private byte[] byteArray = null;
    private HuffmanTree<Character> theTree;
    private String[] encodeLine;
    private SortedMap<Character, String> keyMap;
    private SortedMap<String, Character> codeMap;

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
        }
        
        nodes = new HuffmanChar[index];
        index = 0;
        for (int i = 0; i < CHARMAX; i++) {
            if (count[i] > 0) {
                nodes[index++] = new HuffmanChar((char) i, count[i]);
            }
        }
        //Sort the array, not completed.
        Arrays.sort(nodes);
        
        theTree = new HuffmanTree(nodes);
        codeMap = theTree.getCodeMap();
        keyMap = theTree.getKeyMap();
        
        String tmp;
        encodeLine = new String[story.size()];
        for (int i = 0; i < story.size(); i++) {
            tmp = "";
            for (int j = 0; j < story.get(i).length(); j++) {
                tmp += keyMap.get(story.get(i).charAt(j));
            }
            encodeLine[i] = tmp;
        }
        
        //writeEncodedFile(byteArray, fileName);
        writeKeyFile(fileName);
        byte[] r = readByteArray(fileName.substring(0, fileName.lastIndexOf(".")) + ".cod");
        for (int i = 0; i < r.length; i++) {
            System.out.println(r[i]);
        }
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
        String newFileName = fileName.substring(0, fileName.lastIndexOf("."));
        newFileName += ENCODE_FILE_FORMAT;
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(newFileName));
                output.write(bytes);
            } finally {
                output.close();
                System.out.println("Key file written.");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found.");
        } catch (IOException ex) {
            System.out.println("IO issue.");
        }
    }

    /**
     * writeKeyFile
     *
     * @param fileName the name of the file to write to
     */
    public void writeKeyFile(String fileName) {
        String newFileName = fileName.substring(0, fileName.lastIndexOf("."));
        newFileName += KEY_FILE_FORMAT;
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(newFileName));
                for (int i = 0; i < nodes.length; i++) {
                    output.write(nodes[i].toThreeBytes());
                }
            } finally {
                output.close();
                System.out.println("Key file written.");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found.");
        } catch (IOException ex) {
            System.out.println("IO issue.");
        }
    }
    
    public byte[] readByteArray(String fileName) {
        File file = new File(fileName);
        byte[] result = new byte[(int) file.length()];
        try {
            InputStream input = null;
            try {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while (totalBytesRead < result.length) {
                    int bytesRemaining = result.length - totalBytesRead;
                    //input.read() returns -1, 0, or more :
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                    if (bytesRead > 0) {
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
                /*
         the above style is a bit tricky: it places bytes into the 'result' array; 
         'result' is an output parameter;
         the while loop usually has a single iteration only.
                 */
            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            System.out.println("IO issue.");
        }
        return result;
        
    }
}
