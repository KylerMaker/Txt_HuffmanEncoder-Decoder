import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferedBitReader {
    // Note that we need to look ahead 3 bytes, because when the
    // third byte is -1 (EOF indicator) then the second byte is a count
    // of the number of valid bits in the first byte.

    int current;
    int next;
    int afterNext;
    int bitMask;
    BufferedInputStream input;

    public BufferedBitReader(String pathName) throws IOException {
        input = new BufferedInputStream(new FileInputStream(pathName));

        current = input.read();
        if (current == -1) {
            throw new EOFException("File did not have two bytes");
        
        }
        next = input.read();
        if (next == -1) {
            throw new EOFException("File did not have two bytes");
        
        }

        afterNext = input.read();
        bitMask = 128;
    }

    public int readBit() throws IOException {
        int returnBit;
        if (afterNext == -1) {
            if (next == 0){
                return -1;
            } else {
                if ((bitMask & current) == 0){
                    returnBit = 0;
                } else {
                    returnBit = 1;
                }

                next--;
                bitMask = bitMask >> 1;
                return returnBit;
            }
        } else {
            if ((bitMask & current) == 0){
                returnBit = 0;
            } else{
                returnBit = 1;
            }

            bitMask = bitMask >> 1;

            if (bitMask == 0) {
                bitMask = 128;
                current = next;
                next = afterNext;
                afterNext = input.read();
            }
            return returnBit;
        }
    }

    public void close() throws IOException {
        input.close();
    }

}