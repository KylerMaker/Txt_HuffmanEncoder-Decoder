import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class Compressor {
    HashMap<Character, Integer> table;
    PriorityQueue<BinaryTree<CharFreq>> queue;
    BinaryTree<CharFreq> huffmanTree;
    HashMap<Character, String> key;

    public Compressor() {
        this.table = new HashMap<Character, Integer>();
        this.key = new HashMap<Character, String>();
    }

    public BinaryTree<CharFreq> getHuffmanTree() {
        return huffmanTree;
    }

    public void compress(String pathName) throws FileNotFoundException {
        if (pathName.indexOf(".txt") == -1) {
            throw new FileNotFoundException("Invalid file type!");
        }

        BufferedReader inputFile = new BufferedReader(new FileReader(pathName));
        try {
            checkFrequencies(pathName);
            // Checks if the txt file is empty.
            if (inputFile.readLine() == null) {
                inputFile.close();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        queue();
        huffman();
        createKey();
        try {
            writeCompressed(pathName);
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * Check frequencies of chars in the txt file
     */
    public void checkFrequencies(String pathName) throws IOException {
        BufferedReader inputFile = new BufferedReader(new FileReader(pathName));
        try {
            int charInt = inputFile.read();
            while (charInt != -1) {
                char c = (char) charInt;
                Integer value = table.get(c);
                if (value != null) {
                    table.put(c, value + 1);
                } else {
                    table.put(c, 1);
                }
                charInt = inputFile.read();
            }
        } finally {
            inputFile.close();
        }
    }

    public void writeCompressed(String pathName) throws IOException {
        BufferedReader inputFile = new BufferedReader(new FileReader(pathName));
        String compressedPath = "";
        if (pathName.indexOf("_decompressed") == -1) {
            compressedPath += pathName.substring(0, pathName.indexOf(".txt")) + "_compressed" + ".txt";
        } else {
            
            compressedPath += pathName.substring(0, pathName.indexOf("_decompressed")) + "_compressed" + ".txt";
        }

        BufferedBitWriter bitWriter = new BufferedBitWriter(compressedPath);
        try {
            String leafBinary = Integer.toBinaryString(huffmanTree.countLeaves());
            System.out.println("Tree size in binary: " + leafBinary);
            if (leafBinary.length() < 8) {
                for (int i = 0; i < 8 - leafBinary.length(); i++) {
                    bitWriter.writeBit(0);
                }
            }
            for (int i = 0; i < leafBinary.length(); i++) {
                bitWriter.writeBit(Integer.valueOf(leafBinary.substring(i, i + 1)));
            }
            writeTree(huffmanTree, bitWriter);
            int charInt = inputFile.read();
            while (charInt != -1) {
                char c = (char) charInt;
                String binaryKey = key.get(c);
                for (int i = 0; i < key.get(c).length(); i++) {
                    bitWriter.writeBit(Integer.valueOf(binaryKey.substring(i, i + 1)));
                }
                charInt = inputFile.read();
            }
        } finally {
            inputFile.close();
            bitWriter.close();
        }
    }

    public void writeTree(BinaryTree<CharFreq> node, BufferedBitWriter bitWriter) throws IOException {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            // System.out.println("writing 1");
            bitWriter.writeBit(1);
            // System.out.println("writing char: " + node.getData().getChar());
            String charByte = Integer.toBinaryString(node.getData().getChar());
            String zeroes = "";
            if (charByte.length() != 8) {
                for (int i = 0; i < 8 - charByte.length(); i++) {
                    zeroes += "0";
                }
                charByte = zeroes + charByte;
            }
            for (int i = 0; i < charByte.length(); i++) {
                bitWriter.writeBit(Integer.valueOf(charByte.substring(i, i + 1)));
            }
        } else {
            bitWriter.writeBit(0);
            writeTree(node.getLeft(), bitWriter);
            writeTree(node.getRight(), bitWriter);
        }

    }

    /*
     * Takes Hashmap and puts the BinaryTree objects into a min first priority
     * queue.
     */
    public void queue() {
        TreeComparator comparator = new TreeComparator();
        queue = new PriorityQueue<BinaryTree<CharFreq>>(11, comparator);
        Iterator it = table.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            BinaryTree<CharFreq> binaryTree = new BinaryTree<CharFreq>(
                    new CharFreq((char) pair.getKey(), (int) pair.getValue()));
            queue.add(binaryTree);
            it.remove();
        }
        queue.comparator();
    }

    /*
     * Use Huffman Encoding to create a binary tree based on the queue.
     */
    public void huffman() {
        while (queue.size() > 1) {
            // set two trees
            BinaryTree<CharFreq> tree1 = queue.peek();
            queue.remove();
            BinaryTree<CharFreq> tree2 = queue.peek();
            queue.remove();
            // sum their frequencies
            int sum = tree1.getData().getFreq() + tree2.getData().getFreq();

            // create a new tree with children
            BinaryTree<CharFreq> mergedTree = new BinaryTree<CharFreq>(new CharFreq('?', sum));
            mergedTree.setLeft(tree1);
            mergedTree.setRight(tree2);
            // add the tree back into the priority queue
            queue.add(mergedTree);

            // reorder the queue
            queue.comparator();
        }
        // finally, set the huffman tree instance variable.
        huffmanTree = queue.peek();
    }

    /*
     * uses pre-order traversal and creates the key for chars
     */
    public void createKey() {
        inorderTraversalKey(huffmanTree, "");
    }

    public void inorderTraversalKey(BinaryTree<CharFreq> node, String code) {
        if (node == null) {
            return;
        }
        if (huffmanTree.isLeaf() && huffmanTree.count() == 1) {
            key.put(huffmanTree.getData().getChar(), "0");
            return;
        }
        String left = code + "0";
        String right = code + "1";
        inorderTraversalKey(node.getLeft(), left);

        if (node.isLeaf()) {
            key.put(node.getData().getChar(), code);
            System.out.println("Char '" + node.getData().getChar() + "': " + code);
        }
        inorderTraversalKey(node.getRight(), right);
    }

}
