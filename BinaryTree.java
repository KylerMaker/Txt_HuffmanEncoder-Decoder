public class BinaryTree<E> {
    private BinaryTree<E> left, right;
    private E data;

    public BinaryTree(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public BinaryTree(E data, BinaryTree<E> left, BinaryTree<E> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean isInternal() {
        return left != null || right != null;
    }

    public boolean hasLeftChild() {
        return left != null;
    }

    public boolean hasRightChild() {
        return right != null;
    }

    public BinaryTree<E> getLeft() {
        return left;
    }

    public BinaryTree<E> getRight() {
        return right;
    }

    public E getData() {
        return data;
    }

    public int count() {
        BinaryTree<E> left = getLeft();
        BinaryTree<E> right = getRight();
        return 1 + (left != null ? left.count() : 0) + (right != null ? right.count() : 0);
    }

    public int countLeaves() {
        if (this.isLeaf() == true) {
            return 1;
        }
        BinaryTree<E> left = getLeft();
        BinaryTree<E> right = getRight();
        return (left != null ? left.countLeaves() : 0) + (right != null ? right.countLeaves() : 0);
    }

    public void setData(E data) {
        this.data = data;
    }

    public void setLeft(BinaryTree<E> left) {
        this.left = left;
    }

    public void setRight(BinaryTree<E> right) {
        this.right = right;
    }

}