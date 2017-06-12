/* 
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
 * Compresses the passed text file into a binary file if the only argument is
 * a text file otherwise if the first argument is "-d", then the compressed 
 * version of the file is uncompressed and "x" is added to the end of the name.
 * 
 * @author tienhuynh
 * @author Michael Courter
 * @author Paul Bladeck
 * @version 1.1
 * 
 * Compiler: Java 1.8.0_111
 * OS: Windows 10
 * Hardware: PC
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
     * Main method of program. Starts the code and decode processes depending
     * on the passed argument.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //----------------------------------------------------
        // used for debugging encoding
        //----------------------------------------------------
        //args = new String[1];
        //args[0] = "AlmostAllCharacterTest.txt";
        //----------------------------------------------------
        // used for debugging encoding
        //----------------------------------------------------
        args = new String[2];
        args[0] = "-d";
        args[1] = "AlmostAllCharacterTest.txt";  
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

    /**
     * Encodes the file with the passed name as a .huf binary file.
     * 
     * @param fileName the file to encode
     */
    public void encode(String fileName) {
        int[] c = new int[CHARMAX];

        // Turn the text file into an array of Strings
        if (TextFileIO.hasFile(fileName)) {
            TextFileIO.readFile(fileName);
            story = TextFileIO.getTextFile();
        } else {
            return;
        }

        // Find the frequency of the characters in the text file
        for (String line : story) {
            for (int i = 0; i < line.length(); i++) {
                int k = line.charAt(i);
                if (k > -1 && k < CHARMAX) {
                    c[k] += 1;
                }
            }
        }

        // Creates a copy of c as a static array called count
        int index = 0;
        for (int i = 0; i < CHARMAX; i++) {
            if (c[i] > 0) {
                count[i] = c[i];
                index++;
            }
        }

        // Creates an array holding nodes that hold the chars and frequencies
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
    }

    /**
     * Decodes the .huf file back to a text file with "x" added to the end of 
     * the file name.
     * 
     * @param inFileName the file to decode
     */
    public void decode(String inFileName) {
        String keyFileName = 
                inFileName.substring(0, inFileName.lastIndexOf("."));
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

        BinaryNodeInterface<HuffmanData<Character>> currentNode = 
                theTree.getRootNode();
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
        
        if (!line.isEmpty())
            decodeLine.add(line);
        
        String newFileName = inFileName.substring(0, inFileName.
                lastIndexOf(".")) + "_x.txt";
        writeToTextFile(decodeLine, newFileName);
    }

    /**
     * Creates the .huf file.
     *
     * @param bytes for the file
     * @param fileName the name and extension of the file
     */
    public void writeEncodedFile(byte[] bytes, String fileName) {
        String newFileName = fileName.substring(0, fileName.lastIndexOf("."));
        newFileName += ENCODE_FILE_FORMAT;
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(
                        new FileOutputStream(newFileName));
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
     * Creates the .cod.
     *
     * @param fileName of the file to write to
     */
    public void writeKeyFile(String fileName) {
        String newFileName = fileName.substring(0, fileName.lastIndexOf("."));
        newFileName += KEY_FILE_FORMAT;
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(
                        new FileOutputStream(newFileName));
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
     *  Reads the byte array from the passed file, and returns it.
     * 
     * @param fileName of the file that contains the byte array
     * 
     * @return the byte array from the file
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
                    int bytesRead = 
                            input.read(result, totalBytesRead, bytesRemaining);
                    if (bytesRead > 0) {
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
                /*
                 The above style is a bit tricky: it places bytes into the 
                 'result' array; 'result' is an output parameter; the while 
                 loop usually has a single iteration only.
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
     * Writes the passed list of Strings to the file with the passed file 
     * name.
     * 
     * @param lines
     * @param fileName 
     */
    private void writeToTextFile(ArrayList<String> lines, String fileName) {
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
}