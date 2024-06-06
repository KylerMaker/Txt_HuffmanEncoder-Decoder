import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Decompressor {

    BinaryTree<Character> huffmanTree;

    public Decompressor() {

    }

    public void decompress(String pathName) throws FileNotFoundException {
        if (pathName.indexOf(".txt") == -1) {
            throw new FileNotFoundException("Invalid file type!");
        }
        try {
            if (pathName.indexOf("_compressed") == -1) {
                return;
            }
            String decompressedPathName = pathName.substring(0, pathName.indexOf("_compressed")) + ".txt";
            BufferedWriter outputFile = new BufferedWriter(
                    new FileWriter(decompressedPathName));
            BufferedBitReader bitReader = new BufferedBitReader(pathName);
            System.out.println("reading...");
            String leafCount = "";
            for (int i = 0; i < 8; i++) {
                leafCount += String.valueOf(bitReader.readBit());
            }
            System.out.println(leafCount);
            huffmanTree = buildTree(bitReader, binaryToInt(leafCount));
            System.out.println("Built huffman tree with leaf count " +
                    huffmanTree.countLeaves());
            decodeChars(outputFile, bitReader);
            outputFile.close();
            bitReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BinaryTree<Character> buildTree(BufferedBitReader bitReader, int leavesLeft) throws IOException {
        if (bitReader.readBit() == 1 && leavesLeft > 0) {
            String byteStr = "";
            for (int i = 0; i < 8; i++) {
                byteStr += Integer.toString(bitReader.readBit());

            }
            return new BinaryTree<Character>((char) binaryToInt(byteStr));
        } else {
            BinaryTree<Character> leftChild = buildTree(bitReader, leavesLeft--);
            BinaryTree<Character> rightChild = buildTree(bitReader, leavesLeft--);
            return new BinaryTree<Character>('?', leftChild, rightChild);
        }
    }

    public void decodeChars(BufferedWriter writer, BufferedBitReader bitReader) throws IOException {
        int bit = bitReader.readBit();
        BinaryTree<Character> currentNode = huffmanTree;
        while (bit != -1) {
            if (huffmanTree.count() == 1 && bit == 0) {
                writer.write(huffmanTree.getData());
                bit = bitReader.readBit();
                continue;
            }
            if (bit == 0) {
                currentNode = currentNode.getLeft();
            }
            if (bit == 1) {
                currentNode = currentNode.getRight();
            }
            if (currentNode.isLeaf() == true) {
                System.out.print(currentNode.getData());
                writer.write(currentNode.getData());
                currentNode = huffmanTree;
            }
            bit = bitReader.readBit();
        }
    }

    public static int binaryToInt(String binary) {
        if (binary.contains("-")) {
            throw new IllegalArgumentException("convertBinaryToInt(): cannot accept a negative value.");
        }
        if (binary.length() == 0) {
            return 0;
        }
        int capacity = (int) Math.pow(2, binary.length() - 1);
        int bit = Integer.parseInt(binary.substring(0, 1));
        return bit * capacity + binaryToInt(binary.substring(1));
    }

}
