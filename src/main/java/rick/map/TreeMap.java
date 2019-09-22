package rick.map;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TreeMap implements Map {

    private Node root;
    public void walkMapInOrder(KeyValueVisitor visitor) {
        walkTreeInOrder(root, visitor);
    }
    public void walkMapPostOrder(KeyValueVisitor visitor) {
        walkTreePostOrder(root, visitor);
    }
    public void walkMapPreOrder(KeyValueVisitor visitor) {
        walkTreePreOrder(root, visitor);
    }

    private void walkTreeInOrder(Node node, KeyValueVisitor visitor) {
        if (node == null) return;
        walkTreeInOrder(node.getLeftNode(), visitor);
        visitor.visit(node.getKeyValue());
        walkTreeInOrder(node.getRightNode(), visitor);
    }

    private void walkTreePostOrder(Node node, KeyValueVisitor visitor) {
        if (node == null) return;
        walkTreePostOrder(node.getLeftNode(), visitor);
        walkTreePostOrder(node.getRightNode(), visitor);
        visitor.visit(node.getKeyValue());

    }

    private void walkTreePreOrder(Node node, KeyValueVisitor visitor) {
        if (node == null) return;
        visitor.visit(node.getKeyValue());
        walkTreePreOrder(node.getLeftNode(), visitor);
        walkTreePreOrder(node.getRightNode(), visitor);
    }


    @Override
    public void put(final String key, final String value) {

        if (root == null) {
            root = new Node(new KeyValue(key, value));
        } else {
            insert(root, null, new KeyValue(key, value));
        }

    }

    private void insert(final Node node, final Node parent, final KeyValue keyValue) {
        if (keyValue.getKey().compareTo(node.getKeyValue().getKey()) < 0) {
            if (node.getLeftNode() == null) {
                node.setLeftNode(new Node(keyValue));
            } else {
                insert(node.getLeftNode(), node, keyValue);
            }
        } else {
            if (node.getRightNode() == null) {
                node.setRightNode(new Node(keyValue));
            } else {
                insert(node.getRightNode(), node, keyValue);
            }
        }
    }

    private boolean isLeft(String key, Node node) {
        return node.getKeyValue().getKey().compareTo(key) > 0;
    }
    @Override
    public void remove(String key) {
        remove(key, root, null);
    }
    void remove(String key, Node node, Node parentNode) {
        var result = find(key, node, parentNode);
        var currentNode = result[0];
        var parent = result[1];
        if (currentNode == null) return;
        if (currentNode.isLeaf() && parent != null) {
            if (isLeft(key, parent)) {
                parent.setLeftNode(null);
            } else {
                parent.setRightNode(null);
            }
        }
        if (currentNode.getRightNode() == null) {
            if (parent == null) {
                root = currentNode.getLeftNode();
            } else {
                if (isLeft(key, parent)) {
                    parent.setLeftNode(currentNode.getLeftNode());
                } else {
                    parent.setRightNode(currentNode.getLeftNode());
                }
            }
        } else if (currentNode.getRightNode().getLeftNode() == null) {
            if (parent == null && currentNode == root) {
                var newRoot = currentNode.getRightNode();
                newRoot.setLeftNode(root.getLeftNode());
                root = newRoot;
            } else {
                if (isLeft(currentNode.getRightNode().getKeyValue().getKey(), parent)) {
                    parent.setLeftNode(currentNode.getRightNode());
                } else {
                    parent.setRightNode(currentNode.getRightNode());
                }
            }
        } else {
            var leftMostParent = currentNode.getRightNode();
            var leftMost = findLeftMost(currentNode.getRightNode().getLeftNode());
            leftMostParent.setLeftNode(leftMost.getRightNode());
            leftMost.setRightNode(currentNode.getRightNode());
            leftMost.setLeftNode(currentNode.getLeftNode());
            if (parent == null) {
                root = leftMost;
            } else {
                if (isLeft(leftMost.getKeyValue().getKey(), parent)) {
                    parent.setLeftNode(leftMost);
                } else {
                    parent.setRightNode(leftMost);
                }
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

    @Override
    public String get(String key) {
        var node = find(key, root, null);
        if (node == null) return null;
        else return node[0].getKeyValue().getValue();
    }

    Node[] find(String key, Node node, Node parent) {
        if (node == null) return null;
        if (key.equals(node.getKeyValue().getKey())) {
            return new Node[]{node, parent};
        }
        var foundNode = find(key, node.getLeftNode(), node);
        if (foundNode != null) {
            return foundNode;
        } else {
            return find(key, node.getRightNode(), node);
        }
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


        var nodeKey = "( " + level + " " + node.getKeyValue().getKey() + ")";

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
