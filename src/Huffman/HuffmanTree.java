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
<<<<<<< HEAD
        // your code here

//        add(new BinaryNode<HuffmanData<T>>(dataArray[leafCount]),
//                new BinaryNode<HuffmanData<T>>(dataArray[++leafCount]) );
=======
>>>>>>> commit-encode
        BinaryNode<HuffmanData<T>>[] nodes;
        BinaryNode<HuffmanData<T>> tmp;
        nodes = new BinaryNode[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) {
            nodes[i] = new BinaryNode<>(dataArray[i], null, null);
        }
        while (leafCount < nodes.length - 1) {
            add(nodes[leafCount], nodes[++leafCount]);
            nodes[leafCount] = (BinaryNode<HuffmanData<T>>) super.getRootNode();
            for (int i = leafCount + 1; i < nodes.length; i++) {
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
        System.out.println("Traverse");
        inorderTraverse();
        keyMap = new TreeMap<String, T>();
        codeMap = new TreeMap<T, String>();
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
<<<<<<< HEAD
     * adds 2 new elements to this tree<br>
     * smaller on the left
     *
     * @param element1
     * @param element2
     */
    private void firstAdd(BinaryNode<HuffmanData<T>> node1,
            BinaryNode<HuffmanData<T>> node2) {
//        if (getRootNode() == null) {
//            add(node1, node2);
//            return;
//        }
//
        BinaryNode<HuffmanData<T>> internalNode;
        if (node1.getData().getOccurances() < node2.getData().getOccurances()) {
            internalNode = new BinaryNode<>(
                    new HuffmanData<>(MARKER, node1.getData().getOccurances() + node2.getData().getOccurances()),
                    node1,
                    node2);
        } else {
            internalNode = new BinaryNode<>(
                    new HuffmanData<>(MARKER, node1.getData().getOccurances() + node2.getData().getOccurances()),
                    node2,
                    node1);
        }
        if (internalNode.getData().getOccurances() < getRootNode().getData().getOccurances()) {
            add(internalNode, (BinaryNode<HuffmanData<T>>) super.getRootNode());
        } else {
            add((BinaryNode<HuffmanData<T>>) super.getRootNode(), internalNode);
        }
//        if (node1.getData().getOccurances() <= node2.getData().getOccurances()) {
//            add(node1, node2);
//        } else {
//            add(node2, node1);
//        }
    }

    /**
     * add a single element to the tree smaller on the left
     * NEED REDOING.
     * add a single element to the tree smaller on the left NEED REDOING.
     *
     * @param element1
     */
    private void add(HuffmanData<T> element1) {
        BinaryNode<HuffmanData<T>> node = new BinaryNode<>(element1);
        HuffmanTree<T> leftTree = new HuffmanTree<T>();
        leftTree.setRootNode(node);
        if (getRootNode() == null) {

            setTree(new HuffmanData<T>(MARKER, node.getData().getOccurances()), leftTree, null);
            return;
        }

        if (getRootNode().getLeftChild() == null) {
            add(node, (BinaryNode<HuffmanData<T>>) getRootNode().getRightChild());
        } else {
            add((BinaryNode<HuffmanData<T>>) getRootNode().getLeftChild(), node);
        }
    }

    /**
=======
>>>>>>> commit-encode
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
            keyMap.put(path, currentNode.getData().getData());
            codeMap.put(currentNode.getData().getData(), path);
        } else {
            if (currentNode.getLeftChild() != null) {
                setMaps(currentNode.getLeftChild(), path.concat("0"));
            }
            if(currentNode.getRightChild() != null) {
                setMaps(currentNode.getRightChild(), path.concat("1"));
            }
        }
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
