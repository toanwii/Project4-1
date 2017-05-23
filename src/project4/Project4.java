/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project4;

import java.util.ArrayList;
import Huffman.*;
/**
 *
 * @author tienhuynh
 */
public class Project4 {
    static final int MAX_NUMBER_CHARS = 70000;
    
    private static int[] count = new int[MAX_NUMBER_CHARS];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String fileName = "Through_The_Looking_Glass.txt";
        ArrayList<String> story = new ArrayList();
        if (TextFileIO.hasFile(fileName)) {
            TextFileIO.readFile(fileName);
            story = TextFileIO.getTextFile();
        }
        for (String line : story) {
            for (int i = 0; i < line.length(); i++) {
                count[line.charAt(i)] += 1;
            }

        }
        for (int i = 0; i < 128; i++) {
            System.out.println(count[i]);
        }
    }

}
