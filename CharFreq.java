public class CharFreq implements Comparable<CharFreq> {
    private char character;
    private int frequency;

    public CharFreq(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public char getChar() {
        return character;
    }

    public int getFreq() {
        return frequency;
    }

    public void setChar(char character) {
        this.character = character;
    }

    public void setFreq(int frequency) {
        this.frequency = frequency;
    }

    public boolean equals(CharFreq other) {
        if (character == other.getChar() && frequency == other.getFreq()) {
            return true;
        }
        return false;
    }

    public int compareTo(CharFreq other) {
        if (other.equals(this)) {
            return 0;
        }
        if (other.getFreq() < frequency) {
            return 1;
        }
        return -1;
    }
}
