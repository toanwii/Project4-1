/*
 * HuffmanTree.java
 *
 * Created on May 21, 2007, 2:16 PM
 */
package huffman;

import java.util.*;

/**
 * binary tree for Huffman coding
 *
 * @author pbladek
 */
public class HuffmanTree<T extends Comparable<? super T>>
        extends BinaryTree<HuffmanData<T>> {

    private final T MARKER = null;
    SortedMap<T, String> codeMap;
    SortedMap<String, T> keyMap;

    private int leafCount = 0;

    /**
     * Creates a new instance of HuffmanTree
     */
    public HuffmanTree() {
        super();
    }

    /**
     * Creates a new instance of HuffmanTree from an array of Huffman Data
     *
     * @param dataArray n array of Huffman Data
     */
    public HuffmanTree(HuffmanData<T>[] dataArray) {
        // your code here
        while (leafCount < dataArray.length - 1) {
            firstAdd(dataArray[leafCount], dataArray[++leafCount]);
            dataArray[leafCount] = super.getRootData();
            //Tracking log. Can be deleted
            System.out.println(super.getRootNode().getLeftChild().getData().getData() + ":" + super.getRootNode().getLeftChild().getData().getOccurances());
            System.out.println(super.getRootNode().getRightChild().getData().getData() + ":" + super.getRootNode().getRightChild().getData().getOccurances());
            //End of Tracking log.
            System.out.println("====");
            Arrays.sort(dataArray, leafCount, dataArray.length);
        }

        keyMap = new TreeMap<String, T>();
        codeMap = new TreeMap<T, String>();
        setMaps(getRootNode(), "");
    }

    /**
     * creates two new HuffmanTrees and adds them to the root of this tree
     *
     * @param left
     * @param rightt
     */
    private void add(BinaryNode<HuffmanData<T>> left,
            BinaryNode<HuffmanData<T>> right) {
        HuffmanTree<T> leftTree = new HuffmanTree<T>();
        leftTree.setRootNode(left);
        HuffmanTree<T> rightTree = new HuffmanTree<T>();
        rightTree.setRootNode(right);
        /* This code is kept
        setTree(new HuffmanData<T>(MARKER, left.getData().getOccurances() + right.getData().getOccurances()), leftTree, rightTree);
         */
        setTree(new HuffmanData<T>((T) (left.getData().getData().toString() + right.getData().getData().toString()), left.getData().getOccurances() + right.getData().getOccurances()), leftTree, rightTree);
    }

    /**
     * adds 2 new elements to this tree<br>
     * smaller on the left
     *
     * @param element1
     * @param element2
     */
    private void firstAdd(HuffmanData<T> element1, HuffmanData<T> element2) {
        BinaryNode<HuffmanData<T>> node1 = new BinaryNode<>(element1);
        BinaryNode<HuffmanData<T>> node2 = new BinaryNode<>(element2);

        if (node1.getData().getOccurances() < node2.getData().getOccurances()) {
            add(node1, node2);
        } else {
            add(node2, node1);
        }
    }

    /**
     * add a single element to the tree smaller on the left
     * NEED REDOING.
     * @param element1
     */
    private void add(HuffmanData<T> element1) {
        BinaryNode<HuffmanData<T>> node = new BinaryNode<>(element1);
        HuffmanTree<T> leftTree = new HuffmanTree<T>();
        leftTree.setRootNode(node);
        if (getRootNode() == null) {
            setTree(new HuffmanData<T>(MARKER, node.getData().getOccurances()), leftTree , null);
            return;
        }

        if (getRootNode().getLeftChild() == null) {
            add(node, (BinaryNode<HuffmanData<T>>) getRootNode().getRightChild());
        } else {
            add((BinaryNode<HuffmanData<T>>) getRootNode().getLeftChild(), node);
        }
    }

    /**
     * set up the 2 maps
     *
     * @param node
     * @param codeString
     */
    private void setMaps(BinaryNodeInterface<HuffmanData<T>> node,
            String codeString) {

    }

    /*
     * accessor for codeMap
     * @ return codeMap
     */
    public SortedMap<T, String> getCodeMap() {
        return codeMap;
    }

    /*
     * accessor for keyMap
     * @ return keyMap
     */
    public SortedMap<String, T> getKeyMap() {
        return keyMap;
    }

}
