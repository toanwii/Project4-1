/*
 * HuffmanTree.java
 */
package huffman;

import java.io.Serializable;
import java.util.*;

/**
 * A binary tree for Huffman coding.
 *
 * @author Tien Huynh
 * @author Michael Courter
 * @author Paul Bladek
 * @version 1.0
 * @param <T> data Type T
 *
 * Compiler: Java 1.8.0_111 OS: Windows 10 Hardware: PC HISTORY LOG<br>
 * Delete first, add(HuffmanData element).<br>
 * Implement code for add(BinaryNode left, BinaryNode right)<br>
 * Write up the constructor.<br>
 * Test and run to ensure the tree is correctly constructed.
 */
public class HuffmanTree<T extends Comparable<? super T>>
        extends BinaryTree<HuffmanData<T>>
        implements Serializable {

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
     * Creates a new instance of HuffmanTree from an array of Huffman Data.
     *
     * @param dataArray n array of Huffman Data
     */
    public HuffmanTree(HuffmanData<T>[] dataArray) {
        if (dataArray.length == 0 || dataArray == null) {
            System.out.println("Empty array. Cannot construct a tree.");
            return;
        }

        if (dataArray.length == 1) {
            BinaryNode<HuffmanData<T>> left = new BinaryNode<>(dataArray[0], null, null);
            BinaryNode<HuffmanData<T>> right = new BinaryNode<>(new HuffmanData<>(MARKER));
            add(left, right);
        } else {
            BinaryNode<HuffmanData<T>>[] nodes;
            BinaryNode<HuffmanData<T>> tmp;
            nodes = new BinaryNode[dataArray.length];
            for (int i = 0; i < dataArray.length; i++) {
                nodes[i] = new BinaryNode<>(dataArray[i], null, null);
            }
            while (leafCount < nodes.length - 1) {
                add(nodes[leafCount], nodes[++leafCount]);
                nodes[leafCount] = (BinaryNode<HuffmanData<T>>) super.getRootNode();
                for (int i = leafCount + 1; i < nodes.length - 1; i++) {
                    if (getRootData().getOccurances() <= nodes[i].getData().getOccurances()) {
                        nodes[i - 1] = (BinaryNode<HuffmanData<T>>) getRootNode();
                        break;
                    } else {
                        tmp = nodes[i - 1];
                        nodes[i - 1] = nodes[i];
                        nodes[i] = tmp;
                    }
                }
            }
            codeMap = new TreeMap<String, T>();
            keyMap = new TreeMap<T, String>();
            setMaps(getRootNode(), "");
        }
    }

    /**
     * Creates two new HuffmanTrees and adds them to the root of this tree.
     *
     * @param left branch of this tree
     * @param right branch of this tree
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
        setTree(new HuffmanData<T>(MARKER, left.getData().getOccurances()
                + right.getData().getOccurances()), leftTree, rightTree);
    }

    /**
     * Sets up the 2 maps needed for decompression.
     *
     * @param node to start at while traversing the tree
     * @param codeString
     */
    private void setMaps(BinaryNodeInterface<HuffmanData<T>> node,
            String codeString) {
        BinaryNodeInterface<HuffmanData<T>> currentNode = node;
        String path = codeString;
        if (currentNode.getLeftChild() == null
                && currentNode.getRightChild() == null) {
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

    /**
     * Returns the codeMap.
     *
     * @return the codeMap
     */
    public SortedMap<String, T> getCodeMap() {
        return codeMap;
    }

    /**
     * Returns the keyMap.
     *
     * @return the keyMap
     */
    public SortedMap<T, String> getKeyMap() {
        return keyMap;
    }
}
