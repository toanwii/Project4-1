/*
 * TextFileIO.java
 */
package project4;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
 */
public class TextFileIO {

    static private final String newLine = System.getProperty("line.separator");

    static ArrayList<String> lines;
    static boolean fileFound = false;
    static FileReader fin = null;
    static FileWriter fout = null;

    /**
     * Return an ArrayList which has original lines read from a file.
     * @param fin FileReader.
     * @return an ArrayList which has original lines read from a file.
     * @throws FileNotFoundException This method throws FileNotFoundException.
     * @throws IOException This method throws IOException.
     */
    static ArrayList<String> readLines(FileReader fin)
            throws FileNotFoundException, IOException {
        int i;
        String line = "";
        ArrayList<String> tmp_lines = new ArrayList<>();
        do {
            i = fin.read();
            char tmp = (char) i;

            if ((tmp != '\r')) {
                line += (char) i;
            } else {
                tmp_lines.add(line);
                if (i != -1) {
                    line = "";
                }
            }

        } while (i != -1);
        tmp_lines.add(line);
        return tmp_lines;
    }

    /**
     * Check and read a text file from disk.
     * @param fileName File name that will be read.
     */
    static void readFile(String fileName) {
        try {
            if (hasFile(fileName)) {
                lines = readLines(fin);
            }
        } catch (FileNotFoundException | NullPointerException fileNotFound) {
            System.out.println(fileNotFound.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                fin.close();
            } catch (NullPointerException nullEx) {
                System.out.println("No need to close the file.");
            } catch (IOException e) {
                System.out.println("File can't be closed.");
            }
        }
    }

    /**
     * Return an ArrayList which has original lines.
     * @return an ArrayList which has original lines.
     */
    static ArrayList<String> getTextFile() {
        return lines;
    }

    /**
     * Return true if the file is found.   
     * @param str File name or path.
     * @return true if the file is found.
     */
    static boolean hasFile(String str) {
        try {
            fin = new FileReader(str);
            
            return true;
        } catch (FileNotFoundException | NullPointerException e) {
            if (str.isEmpty()) {
                System.out.println("No file name. Please type in one.");
            } else {
                System.out.println(str + " file cannot be read or found.");
            }
            return false;
        }
    }
}
