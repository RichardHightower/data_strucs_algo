package rick.map.avl;

public class Node {
    private final KeyValue keyValue;

    private int height;
    private Node leftNode;
    private Node rightNode;

    public Node(KeyValue keyValue) {
        this.keyValue = keyValue;
        height = 1;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public KeyValue getKeyValue() {
        return keyValue;
    }



    public int getHeight() {
        return height;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    private int safeHeight(Node node) {
        if (node == null)
            return 0;

        return node.height;
    }

    public int getBalance() {
        return safeHeight(leftNode) - safeHeight(rightNode);
    }

    public void updateHeight() {
        height = Math.max(safeHeight(leftNode), safeHeight(rightNode)) + 1;
    }


    public boolean isLeaf() {
        return leftNode==null && rightNode==null;
    }
}
