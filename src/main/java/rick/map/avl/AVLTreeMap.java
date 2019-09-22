package rick.map.avl;


import java.io.PrintWriter;
import java.io.StringWriter;

public class AVLTreeMap implements Map {

    private Node root;


    public void walkMapInOrder(final KeyValueVisitor visitor) {
        walkTreeInOrder(root, visitor);
    }

    public void walkMapPostOrder(final KeyValueVisitor visitor) {
        walkTreePostOrder(root, visitor);
    }

    public void walkMapPreOrder(final KeyValueVisitor visitor) {
        walkTreePreOrder(root, visitor);
    }

    private void walkTreeInOrder(final Node node, final KeyValueVisitor visitor) {
        if (node==null) return;
        walkTreeInOrder(node.getLeftNode(), visitor);
        visitor.visit(node.getKeyValue());
        walkTreeInOrder(node.getRightNode(), visitor);
    }

    private void walkTreePostOrder(final Node node, final KeyValueVisitor visitor) {
        if (node==null) return;
        walkTreePostOrder(node.getLeftNode(), visitor);
        walkTreePostOrder(node.getRightNode(), visitor);
        visitor.visit(node.getKeyValue());

    }

    private void walkTreePreOrder(final Node node, final KeyValueVisitor visitor) {
        if (node==null) return;
        visitor.visit(node.getKeyValue());
        walkTreePreOrder(node.getLeftNode(), visitor);
        walkTreePreOrder(node.getRightNode(), visitor);
    }

    private Node rightRotate(final Node node) {
        Node leftNode = node.getLeftNode();
        Node leftNodeRight = leftNode.getRightNode();

        leftNode.setRightNode(node);
        node.setLeftNode(leftNodeRight);

        node.updateHeight();
        leftNode.updateHeight();

        return leftNode;
    }

    private Node leftRotate(final Node node) {
        Node rightNode = node.getRightNode();
        Node rightNodeLeft = rightNode.getLeftNode();


        rightNode.setLeftNode(node);
        node.setRightNode(rightNodeLeft);


        node.updateHeight();
        rightNode.updateHeight();


        return rightNode;
    }

    private int safeGetBalance(Node node) {
        if (node == null)
            return 0;
        return node.getBalance();
    }

    public void put(String key, String value) {
        if (root == null ) {
            root = new Node(new KeyValue(key, value));
        } else {
            root = insert(root, key, value);
        }
    }

    @Override
    public void remove(String key) {
        remove(key, root, null);
    }

    void remove(final String key, final Node node, final Node parentNode) {
        var result  = findNode(key, node, parentNode);
        var currentNode = result[0];
        var parent = result[1];
        if (currentNode == null) return ;


        if (currentNode.isLeaf() && parent!=null) {

            if (isLeft(key, parent)) {
                parent.setLeftNode(null);
            } else {
                parent.setRightNode(null);
            }
            parent.updateHeight();
        }
        //Case 1: if the parent has no right node then current left replaces current.
        if (currentNode.getRightNode() == null) {
            if (parent == null) {
                root = currentNode.getLeftNode();
            } else {
                if (isLeft(key, parent)) {
                    parent.setLeftNode(currentNode.getLeftNode());
                } else  {
                    parent.setRightNode(currentNode.getLeftNode());
                }
                parent.updateHeight();
            }
        } else if (currentNode.getRightNode().getLeftNode() == null) {
            if (parent==null) {
                root = currentNode.getRightNode();
            } else {
                if (isLeft(currentNode.getRightNode().getKeyValue().getKey(), parent)) {
                    parent.setLeftNode(currentNode.getRightNode());
                }else {
                    parent.setRightNode(currentNode.getRightNode());
                }
                parent.updateHeight();
            }
        } else {
            var leftMostParent = currentNode.getRightNode();
            var leftMost = findLeftMost(currentNode.getRightNode().getLeftNode());
            leftMostParent.setLeftNode(leftMost.getRightNode());
            leftMost.setRightNode(currentNode.getRightNode());
            leftMost.setLeftNode(currentNode.getLeftNode());

            leftMost.updateHeight();
            leftMostParent.updateHeight();

            if (parent==null) {
                root = leftMost;
            } else {
                if (isLeft(leftMost.getKeyValue().getKey(), parent)) {
                    parent.setLeftNode(leftMost);
                } else {
                    parent.setRightNode(leftMost);
                }
                parent.updateHeight();
            }
        }

    }

    private Node findLeftMost(Node node) {
        if (node.getLeftNode() != null) {
            return findLeftMost(node.getLeftNode());
        } else {
            return node;
        }
    }

    private Node findRightMost(Node node) {
        if (node.getRightNode() != null) {
            return findRightMost(node.getRightNode());
        } else {
            return node;
        }
    }

    public String max() {
        if (root==null) return "";
        return findRightMost(root).getKeyValue().getKey();
    }

    public String min() {
        if (root==null) return "";
        return findLeftMost(root).getKeyValue().getKey();
    }

    @Override
    public String get(String key) {
        var node = findNode(key, root, null);
        if (node == null) return null;
        else return node[0].getKeyValue().getValue();
    }

    private Node[] findNode(final String key, final Node node, final Node parent) {

        if (node == null) return null;

        if (key.equals(node.getKeyValue().getKey())) {
            return new Node[]{node, parent};
        }


        var foundNode = findNode(key, node.getLeftNode(), node);
        if (foundNode != null) {
            return foundNode;
        } else {
            return findNode(key, node.getRightNode(), node);
        }

    }

    private boolean isLeft(final String key, final Node node) {
        return node.getKeyValue().getKey().compareTo(key)  > 0;
    }

    private Node insert(Node node, String key, String value) {

        if (node == null)
            return (new Node(new KeyValue(key, value)));

        boolean left = isLeft(key, node);

        if (left)
            node.setLeftNode(insert(node.getLeftNode(), key, value));
        else
            node.setRightNode( insert(node.getRightNode(), key, value));

        node.updateHeight();


        int balance = safeGetBalance(node);

        if (balance >1) {
            if (left)
                return rightRotate(node);
            else {
                node.setLeftNode(leftRotate(node.getLeftNode()));
                return rightRotate(node);
            }
        }

        // Right Right Case
        if (balance < -1) {
            if ( !left)
            return leftRotate(node);
            else {
                node.setRightNode( rightRotate(node.getRightNode()));
                return leftRotate(node);
            }

        }


        return node;
    }




    @Override
    public String toString() {
        var writer = new StringWriter();
        var out = new PrintWriter(writer);

        printNode(root, out, 0);

        return writer.toString();
    }

    private void printNode(Node node, PrintWriter out, final int level) {
        int indentUntil = level * 10;

        var nodeKey = "( " + level + " " + node.getKeyValue().getKey() + "  " +  node.getHeight() + ")";

        indent(out, indentUntil);
        out.println("Node " + nodeKey + node.getKeyValue());


        if (node.getLeftNode() != null) {
            indent(out, indentUntil);
            out.printf("     %s Left Node \n", nodeKey);
            printNode(node.getLeftNode(), out, level + 1);
        }
        if (node.getRightNode() != null) {
            indent(out, indentUntil);
            out.printf("     %s Right Node \n", nodeKey);
            printNode(node.getRightNode(), out, level + 1);
        }

    }

    private void indent(PrintWriter out, int indentUntil) {
        while (indentUntil > 0) {
            indentUntil--;
            out.print(' ');
        }
    }
}
