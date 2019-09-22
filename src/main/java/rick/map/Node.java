package rick.map;

public class Node {
    private final KeyValue keyValue;
    private Node leftNode;
    private Node rightNode;



    public KeyValue getKeyValue() {
        return keyValue;
    }

    public Node(KeyValue keyValue) {
        this.keyValue = keyValue;
    }

    public boolean isLeaf() {
        return rightNode==null && leftNode == null;
    }
    public Node getLeftNode() {
        return leftNode;
    }


    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }



    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "\nNode{" +
                "keyValue=" + keyValue +
                "\n, leftNode=" + leftNode +
                "\n, rightNode=" + rightNode +
                "}\n";
    }
}
