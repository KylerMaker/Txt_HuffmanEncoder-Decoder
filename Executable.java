import java.io.FileNotFoundException;

public class Executable {

    public static void compress(String pathName) {
        Compressor compressor = new Compressor();
        try {
            compressor.compress(pathName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void decompress(String pathName) {
        Decompressor decompressor = new Decompressor();
        try {
            decompressor.decompress(pathName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Executable.decompress("/Users/kyler/Desktop/mobyDick_compressed.txt");

    }

}

// 100
