# Txt_HuffmanEncoder-Decoder
A program that compresses/decompresses .txt files using Huffman coding.

**Class Breakdown**
Binary Tree -> Well, ya know, a binary tree.
BufferedBitReader -> reads bits with FileInputStream
BufferedBitWriter -> writes bits with FileOutputStream
CharFreq -> Stores the char and its frequency in the given txt file--to be used as the element of the Binary Tree.
Compressor -> compresses the file using Huffman Coding.
Decompressor -> builds the Huffman tree from the bytes, and decompresses the file using the tree as a key.
**Executable** -> stores the main methods to execute this program.

**Instructions to Run**
In the _Executable.java_ file, choose between the two static methods compress(String pathName) and decompress(String pathName) and run it in the main method. Your file will be written to to the same place as the given pathName.
