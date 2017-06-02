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
    SortedMap<T, String> keyMap;
    SortedMap<String, T> codeMap;
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

        BinaryNode<HuffmanData<T>>[] nodes;
        BinaryNode<HuffmanData<T>> tmp;
        nodes = new BinaryNode[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) {
            nodes[i] = new BinaryNode<>(dataArray[i], null, null);
        }
        while (leafCount < nodes.length - 1) {
            add(nodes[leafCount], nodes[++leafCount]);
            nodes[leafCount] = (BinaryNode<HuffmanData<T>>) super.getRootNode();
            for (int i = leafCount + 1; i < nodes.length - 1 ; i++) {
                if (getRootData().getOccurances() <= nodes[i].getData().getOccurances()) {
                    nodes[i - 1] = (BinaryNode<HuffmanData<T>>) getRootNode();
                    break;
                } else {
                    tmp = nodes[i - 1];
                    nodes[i - 1] = nodes[i];
                    nodes[i] = tmp;
                }
            }
            //Tracking log. Can be deleted
            System.out.println(super.getRootNode().getLeftChild().getData().toString());
            System.out.println(super.getRootNode().getRightChild().getData().toString());
            //End of Tracking log.
            System.out.println("====");
        }
<<<<<<< HEAD
        System.out.println("Path");
        keyMap = new TreeMap<String, T>();
        codeMap = new TreeMap<T, String>();
=======
        System.out.println("Traverse");
        codeMap = new TreeMap<String, T>();
        keyMap = new TreeMap<T, String>();
>>>>>>> commit-encode
        setMaps(getRootNode(), "");
        System.out.println(keyMap.toString());
        System.out.println(codeMap.toString());
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
        HuffmanTree<T> rightTree = new HuffmanTree<T>();

        if (left.getData().getOccurances() <= right.getData().getOccurances()) {
            leftTree.setRootNode(left);
            rightTree.setRootNode(right);
        } else {
            leftTree.setRootNode(right);
            rightTree.setRootNode(left);
        }
        /* This code is kept */
        setTree(new HuffmanData<T>(MARKER, left.getData().getOccurances() + right.getData().getOccurances()), leftTree, rightTree);
        //        setTree(new HuffmanData<T>((T) (left.getData().getData().toString() + right.getData().getData().toString()), left.getData().getOccurances() + right.getData().getOccurances()), leftTree, rightTree);
    }

    /**
     * set up the 2 maps
     *
     * @param node
     * @param codeString
     */
    private void setMaps(BinaryNodeInterface<HuffmanData<T>> node,
            String codeString) {
        BinaryNodeInterface<HuffmanData<T>> currentNode = node;
        String path = codeString;
        if (currentNode.getLeftChild() == null && currentNode.getRightChild() == null) {
            System.out.println(currentNode.getData().toString() + ":" + path);
            codeMap.put(path, currentNode.getData().getData());
            keyMap.put(currentNode.getData().getData(), path);
        } else {
            if (currentNode.getLeftChild() != null) {
                setMaps(currentNode.getLeftChild(), path.concat("0"));
            }
            if (currentNode.getRightChild() != null) {
                setMaps(currentNode.getRightChild(), path.concat("1"));
            }
        }
    }

    /*
     * accessor for codeMap
     * @ return codeMap
     */
    public SortedMap<String, T> getCodeMap() {
        return codeMap;
    }

    /*
     * accessor for keyMap
     * @ return keyMap
     */
    public SortedMap<T, String> getKeyMap() {
        return keyMap;
    }
}
