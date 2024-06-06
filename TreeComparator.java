import java.util.Comparator;

public class TreeComparator implements Comparator<BinaryTree<CharFreq>> {

    public TreeComparator() {
    }

    public int compare(BinaryTree<CharFreq> tree1, BinaryTree<CharFreq> tree2) {
        return tree1.getData().compareTo(tree2.getData());
    }
}
