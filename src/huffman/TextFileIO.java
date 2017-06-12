/*
 * TextFileIO.java
 */
package huffman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class: TextFileIO<br>
 * Handle all the file reading and writing. <br>
 *
 * Compiler: Java JDK 1.8, Netbeans IDE 8.2<br>
 * Hardware: Intel Core i7 6500U, 16 GB RAM.<br>
 * OS: Windows 10 Creator Update.<br>
 *
 * @author tienhuynh
 * @version 1.0
 * HISTORY
 * 
 */
public class TextFileIO {

    static private final String newLine = System.getProperty("line.separator");

    static ArrayList<String> lines = new ArrayList<>();
    static Scanner fin = null;
    static FileWriter fout = null;

    /**
     * Check and read a text file from disk.
     *
     * @param fileName File name that will be read.
     */
    public static void readFile(String fileName) {
        if (hasFile(fileName)) {
            while (fin.hasNextLine()) {
                lines.add(fin.nextLine() + "\n");
            }
            fin.close();
        }
        
    }

    /**
     * Return an ArrayList which has original lines.
     *
     * @return an ArrayList which has original lines.
     */
    public static ArrayList<String> getTextFile() {
        return lines;
    }

    /**
     * Return true if the file is found.
     *
     * @param str File name or path.
     * @return true if the file is found.
     */
    public static boolean hasFile(String str) {
        try {
            fin = new Scanner(new File(str));
            return true;
        } catch (NullPointerException e) {
            if (str.isEmpty()) {
                System.out.println("No file name. Please type in one.");
            } else {
                System.out.println(str + " file cannot be read or found.");
            }
            return false;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
