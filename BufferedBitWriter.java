import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferedBitWriter {
  private byte currentByte;
  private byte numBitsWritten;
  private int totalBytesWritten;
  private BufferedOutputStream output;

  public BufferedBitWriter(String pathName) throws FileNotFoundException {
    currentByte = 0;
    numBitsWritten = 0;
    output = new BufferedOutputStream(new FileOutputStream(pathName));
  }

  public void writeBit(int bit) throws IOException {
    if (bit != 0 && bit != 1) {
      throw new IllegalArgumentException("Argument to writeBit: bit = " + bit);
    }
    numBitsWritten++;
    currentByte |= bit << (8 - numBitsWritten);
    if (numBitsWritten == 8) {
      output.write(currentByte);
      numBitsWritten = 0;
      currentByte = 0;
      totalBytesWritten++;
    }
  }

  public void close() throws IOException {
    output.write(currentByte);
    output.write(numBitsWritten);
    totalBytesWritten += 2;
    System.out.println("Wrote " + totalBytesWritten + " bytes.");

    output.close();
  }
}