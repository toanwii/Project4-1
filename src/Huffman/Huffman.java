/**
 * Huffman.java
 */
package huffman;

import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.SortedMap;

/**
 * Class: Huffman.java<br>
 * Main class, will read in the file name the user inputs and if they wish
 * to encode, or decode the file.<br>
 * Compiler: Java JDK 1.8, Netbeans IDE 8.2<br>
 * Hardware: Intel I7 7500U, Windows 10, 16 GB Ram<br>
 * @version 1.0
 * @author tienhuynh, Michael Courter, Branavan Nagendiram, Jason Bowen
 * Junhong Teng<br>
 */
public class Huffman {

    public static final int CHARMAX = 128;
    public static final byte CHARBITS = 7;
    public static final short CHARBITMAX = 128;
    private static final String KEY_FILE_FORMAT = ".cod";
    private static final String ENCODE_FILE_FORMAT = ".huf";

    private static int[] count = new int[128];
    private static HuffmanChar[] nodes;
    private byte[] byteArray = null;
    private HuffmanTree<Character> theTree;
    private String[] encodeLine;
    private SortedMap<Character, String> keyMap;
    private SortedMap<String, Character> codeMap;
    ArrayList<String> story;

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

        String tmp = "";
        ArrayList<Byte> encodeLine = new ArrayList<>();
        final byte oneByte = 0b01000000;
        byte here = 0b00000000;
        for (int i = 0; i < story.size(); i++) {
            for (int j = 0; j < story.get(i).length(); j++) {
                tmp += keyMap.get(story.get(i).charAt(j));
            }
            while (tmp.length() >= 7) {
                here = 0b00000000;
                for (int k = 0; k < 7; k++) {
                    if (tmp.charAt(k) == '1') {
                        here = (byte) ((oneByte >> k) | here);
                    }
                }
                encodeLine.add(here);
                tmp = tmp.substring(7, tmp.length());
            }
        }

        if (!tmp.isEmpty()) {
            here = 0b00000000;
            for (int k = 0; k < tmp.length(); k++) {
                if (tmp.charAt(k) == '1') {
                    here = (byte) ((oneByte >> k) | here);
                }
            }
            encodeLine.add(here);
        }
        byteArray = new byte[encodeLine.size()];

        for (int i = 0; i < encodeLine.size(); i++) {
            byteArray[i] = encodeLine.get(i);
        }

        writeEncodedFile(byteArray, fileName);
        writeKeyFile(fileName);
        
        long finSize = getFileSize(fileName);
        String newFileName = fileName.substring(0,fileName.lastIndexOf("."))+ENCODE_FILE_FORMAT;
        long foutSize = getFileSize(newFileName);
        System.out.printf(newFileName + ": %.2f%% compression%n",finSize*100.0/foutSize);
    }

    /*
     * decode
     * @param inFileName the file to decode
     */
    public void decode(String inFileName) {
        String keyFileName = inFileName.substring(0, inFileName.lastIndexOf("."));
        String encodeFileName = keyFileName + ENCODE_FILE_FORMAT;
        keyFileName += KEY_FILE_FORMAT;

        byte[] charCount = readByteArray(keyFileName);
        nodes = new HuffmanChar[charCount.length / 3];
        for (int i = 0; i < nodes.length; i++) {
            byte[] tmp = new byte[3];
            tmp[0] = charCount[3 * i];
            tmp[1] = charCount[3 * i + 1];
            tmp[2] = charCount[3 * i + 2];
            nodes[i] = new HuffmanChar(tmp);
        }
        theTree = new HuffmanTree<>(nodes);
        byte[] content = readByteArray(encodeFileName);

        String tmp = "";
        String line = "";
        Character a;
        String t = "";

        BinaryNodeInterface<HuffmanData<Character>> currentNode = theTree.getRootNode();
        ArrayList<String> decodeLine = new ArrayList<>();
        for (int i = 0; i < content.length;) {
            t = Integer.toBinaryString(content[i]);

            while (t.length() < 7) {
                t = "0" + t;
            }

            tmp += t;
            while (!tmp.isEmpty()) {
                if (tmp.charAt(0) == '0') {
                    currentNode = currentNode.getLeftChild();
                } else {
                    currentNode = currentNode.getRightChild();
                }

                a = currentNode.getData().getData();
                tmp = tmp.substring(1, tmp.length());
                if (a != null) {
                    if (a == '\n') {
                        decodeLine.add(line + '\n');
                        line = "";
                    } else {
                        line += a;
                    }
                    currentNode = theTree.getRootNode();
                }
            }
            i++;
        }

        if (!line.isEmpty()) {
            decodeLine.add(line);
        }

        String newFileName = inFileName.substring(0, inFileName.
                lastIndexOf(".")) + "_x.txt";
        write2TextFile(decodeLine, newFileName);
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
                System.out.println("Encode File written.");
            } finally {
                output.close();

            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found.");
        } catch (IOException ex) {
            System.out.println("IO issue.");
        } catch (Exception e) {
            System.out.println("Unknown issue");
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

    /**
     * Read byte array from a binary file
     * @param fileName file name.
     * @return A byte array.
     */
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
                try {
                    input.close();
                } catch (NullPointerException e) {
                    System.out.println("No file was opened.");
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            System.out.println("IO issue.");
        }
        return result;
    }

    /**
     * Write an arrayList to a text file
     * @param lines An array list of string
     * @param fileName File name.
     */
    private void write2TextFile(ArrayList<String> lines, String fileName) {
        FileWriter fout = null;
        try {
            fout = new FileWriter(fileName);
            for (String s : lines) {
                fout.write(s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * Get file size of a file.
     * @param filename File name
     * @return Size in long.
     */
    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File doesn\'t exist");
            return -1;
        }
        return file.length();
    }
}
