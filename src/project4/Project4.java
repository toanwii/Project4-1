/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project4;

import java.util.ArrayList;
import huffman.*;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author tienhuynh
 */
public class Project4 {

    static final int MAX_NUMBER_CHARS = 128;

    private static int[] count = new int[128];
    private static HuffmanData[] nodes;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int[] c = new int[MAX_NUMBER_CHARS];
        String fileName = "Through_The_Looking_Glass.txt";
        ArrayList<String> story = new ArrayList();

        if (TextFileIO.hasFile(fileName)) {
            TextFileIO.readFile(fileName);
            story = TextFileIO.getTextFile();
        }
        for (String line : story) {
            for (int i = 0; i < line.length(); i++) {
                int k = line.charAt(i);
                if (k > -1 && k < MAX_NUMBER_CHARS) {
                    c[k] += 1;
                }
            }
        }

        int index = 0;
        for (int i = 0; i < MAX_NUMBER_CHARS; i++) {
            if (c[i] > 0) {
                count[i] = c[i];
                index++;
            }
            System.out.print((char) i);
        }

        nodes = new HuffmanData[index];
        index = 0;
        for (int i = 0; i < MAX_NUMBER_CHARS; i++) {
            if (count[i] > 0) {
                nodes[index++] = new HuffmanData<>((char) i, count[i]);
                System.out.println(nodes[index - 1]);
            }
        }
        
        //Sort the array, not completed.
        Arrays.sort(nodes,null);
        System.out.println("====");
        for (int i = 0; i < nodes.length; i++)
            System.out.println(nodes[i]);
    }

}
