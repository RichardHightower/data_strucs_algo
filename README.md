I spent some time practicing. Messing around with Java 11, sbt, data structs, and such. 
Enjoying my new MacBook pro and setting up my local dev env. 



I implemented a [merge sort, quicksort, and then a combo of the two](#a-few-sort-algorithms).
I implemented a hash map that enforces a loadFactor and uses a growthFactor. It does this automatically based on the detection of a loadFactor violation.
I implemented several binary treemaps. Did all three types of traversal. 
I implemented a full AVL treemap that is self-balancing. It does a rotateLeft, a rotateRight, min, max, search, remove, and of course insert. 
I did not get as far as I wanted with graph theory and graph algorithms(yet), but I did read about breadth-first search, shortest path, and Dijkstra, etc. as well some NP-complete problems and dynamic programming. 

----


## Tasks
* Create qsort, merge sort examples and then combine qsort and merge DONE [scala qsort](#scala-version-of-quick-sort) [java sorts, merge, quick combo](https://gist.github.com/RichardHightower/5b8a2f5f631b1f664f90cd3b18f81da1#a-few-sort-algorithims), [python qsort](https://gist.github.com/RichardHightower/5b8a2f5f631b1f664f90cd3b18f81da1#python-version-of-quick-sort)
* Create HashMap with auto-balance, load factor, size. DONE (simple [java hashmap](#a-simple-hashmap))
* Create simple binary tree Map DONE ([simple binary tree](#simple-tree-map-binary-tree))
* Create AVL binary tree Map DONE ([avl tree](#avl-tree-map) )
* Traverse binary tree in inorder, postorder and preorder. DONE (see AVL tree map)
* TODO Be familiar with one type of balance tree (AVL and Example [red/black](https://www.java-tips.org/java-se-tips-100019/24-java-lang/1904-red-black-tree-implementation-in-java.html) [red/black tree 2](https://en.wikipedia.org/wiki/Red%E2%80%93black_tree)) DONE 
* Balance a tree (AVL example below) DONE
* Implement max, min etc. on a binary tree (AVL example below)  DONE
* TODO N-ary tree ([BTree](https://en.wikipedia.org/wiki/B-tree), used to optimize disk scans, instead of a single value have a set or array at each node and you can have more than one node pointer) DONE
* TODO implement a [Trie-trees](https://en.wikipedia.org/wiki/Trie) (parent node have prefix, final value key, used for auto-complete) DONE
* Review OS mutexes, semaphores, scheduling, deadlocks and livelocks. (DONE)
* Review graph theory (some but not enough, but want to create some examples of breath first search and breadth first search). 
* TODO implement some graph theory stuff



----

## AVL Tree Map

This is a balanced tree Map using AVL.
Shows insert, search, balancing height using AVL algo, and inorder, postorder and preorder, etc.

See actual source for more... 

#### AVLTreeMain
```java
package rick.map.avl;

public class AVLTreeMain {
    public static void main(String[] args) {
        AVLTreeMap treeMap = new AVLTreeMap();

        treeMap.put("10", "10");
        treeMap.put("20", "20");
        treeMap.put("30", "30");
        treeMap.put("40", "40");
        treeMap.put("50", "50");

        System.out.println(treeMap);


        treeMap.walkMapInOrder(keyValue -> {
            System.out.println(treeMap.get(keyValue.getKey()));
        });

        System.out.println("Min key " + treeMap.min());
        System.out.println("Max key " + treeMap.max());


        treeMap.remove("20");

        System.out.println(treeMap.get("20"));

        System.out.println(treeMap);



    }
}

```


#### AVLTreeMap
```java
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

```


#### KeyValue 
```java
package rick.map.avl;

public class KeyValue {

    private final String key;
    private final String value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
```


#### KeyValueVisitor
```java
package rick.map.avl;

public interface KeyValueVisitor {
    void visit(KeyValue keyValue);
}

```


#### Map
```java
package rick.map.avl;

public interface Map {
    void put(String key, String value);
    void remove(String key);
    String get(String key);
}

```


#### Node (AVL)
```java
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

```


### Simple Binary Tree
```java

```

### Tree Traversal 

#### Main

```java
        Map map = new TreeMap();

        map.put("Andy", "Dog");
        map.put("Bob", "Monkey");
        map.put("Babe", "Bird");
        map.put("Carl", "Horse");
        map.put("Cam", "Horse");
        map.put("Aaron", "Dog");

        TreeMap treeMap = (TreeMap) map;

        treeMap.walkMapInOrder(keyValue -> out.println(keyValue.getKey()));

        out.println();

        treeMap.walkMapPreOrder(keyValue -> out.println(keyValue.getKey()));

        out.println();

        treeMap.walkMapPostOrder(keyValue -> out.println(keyValue.getKey()));
```

#### Visitor 

```java
package rick.map;

public interface KeyValueVisitor {
    void visit(KeyValue keyValue);
}

```

#### Map traversal 
```java
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
        if (node==null) return;
        walkTreeInOrder(node.getLeftNode(), visitor);
        visitor.visit(node.getKeyValue());
        walkTreeInOrder(node.getRightNode(), visitor);
    }

    private void walkTreePostOrder(Node node, KeyValueVisitor visitor) {
        if (node==null) return;
        walkTreePostOrder(node.getLeftNode(), visitor);
        walkTreePostOrder(node.getRightNode(), visitor);
        visitor.visit(node.getKeyValue());

    }

    private void walkTreePreOrder(Node node, KeyValueVisitor visitor) {
        if (node==null) return;
        visitor.visit(node.getKeyValue());
        walkTreePreOrder(node.getLeftNode(), visitor);
        walkTreePreOrder(node.getRightNode(), visitor);
    }
    ...
```
________________



### simple tree map (binary tree)


#### Map
```java
package rick.map;

public interface Map {
    void put(String key, String value);
    void remove(String key);
    String get(String key);
}

```

#### Node 
```java
package rick.map;

public class Node {
    private final KeyValue keyValue;
    private Node leftNode;
    private Node rightNode;

    public KeyValue getKeyValue() {
        return keyValue;
    }

    public Node(KeyValue keyValue, Node leftNode, Node rightNode) {
        this.keyValue = keyValue;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
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

```

#### Binary Tree Map

```java

```




### A simple hashmap
```java

package rick;

import java.util.Arrays;


public class HashMapMain {

    public static interface Map {
        void put(String key, String value);
        void remove(String key);
        String get(String key);
    }

    public static class KeyValue {

        private final String key;
        private final String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "KeyValue{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }


    public static class HashMap implements Map {

        private KeyValue [][] table;
        private final int loadFactor;
        private final float growthFactor;




        public HashMap(int size, int loadFactor, float growthFactor) {
            table = new KeyValue[size][];
            this.loadFactor = loadFactor;
            this.growthFactor = growthFactor;
        }

        @Override
        public void put(String key, String value) {

            final int tableIndex = key.hashCode() % table.length;

            //System.out.printf("%s %s %s\n", key, value, tableIndex);

            if (table[tableIndex] == null) {
                table[tableIndex] = new KeyValue[loadFactor];
                table[tableIndex][0] = new KeyValue(key, value);
            } else {

                var array = table[tableIndex];
                var placed = false;
                for (int index = 0; index < loadFactor; index++) {

                    var kv = array[index];
                    if (kv==null) {
                        placed = true;
                        array[index] = new KeyValue(key, value);
                        break;
                    } else {
                        if (kv.getKey().equals(key) && kv.getValue().equals(value)) {
                            placed = true;
                            break;
                        } else if (kv.getKey().equals(key)){
                            array[index] = new KeyValue(key, value);
                            placed = true;
                            break;
                        }
                    }
                }

                if (!placed) {

                    balance();
                    put(key, value);
                }
            }

        }

        private void balance() {
            var newMap = new HashMap((int) (table.length *growthFactor), loadFactor, growthFactor);
            for (int tableIndex=0; tableIndex < table.length; tableIndex++) {
                if (table[tableIndex] == null) continue;
                var array = table[tableIndex];
                for (int index = 0; index < array.length; index++) {
                    var kv = array[index];
                    if (kv == null) break;
                    newMap.put(kv.key, kv.value);
                }
            }
            table = newMap.table;
        }

        //We could make this more efficient and avoid the buffer copy but probably easier just to use list or create a list.
        //Boon for example has its own list implementations.
        private KeyValue[] compact(KeyValue[] array){
            KeyValue[] newArray = new KeyValue[array.length];
            int count = 0;
            for(int index = 0; index < array.length; index++){
                if(array[index] != null){
                    newArray[count] = array[index];
                    count++;
                }
            }
            return newArray;
        }

        @Override
        public void remove(String key) {
            final int tableIndex = key.hashCode() % table.length;
            var array = table[tableIndex];

            if (table[tableIndex] == null) return;

            var found = false;
            for (int index =0; index < array.length; index++) {
                var kv = array[index];
                if (kv == null) break;
                if (kv.getKey().equals(key)) {
                    found = true;
                    array[index] = null;
                }
            }

            if (found) {
                table[tableIndex] = compact(array);
            }

        }

        @Override
        public String get(String key) {
            final int tableIndex = key.hashCode() % table.length;
            var array = table[tableIndex];
            if (array == null) return null;

            for (int index =0; index < array.length; index++) {
                var kv = array[index];
                if (kv == null) break;
                if (kv.getKey().equals(key)) {
                    return array[index].getValue();
                }
            }

            return null;
        }

        @Override
        public String toString() {

            StringBuilder output = new StringBuilder();
            output.append("HashMap{ table=\n");

            for (int index =0; index < table.length; index++) {
                output.append(Arrays.toString(table[index]));
                output.append("\n");
            }
            output.append(",\n loadFactor=");
            output.append(loadFactor);
            output.append("\n}");

            return output.toString();
        }
    }

    public static void main(String[] args) {
        Map map = new HashMap(2, 2, 2.0f);
        map.put("Rick", "Dog");
        map.put("Diana", "Monkey");
        System.out.println(map);
        map.put("Nick", "Cat");
        System.out.println(map);

        map.remove("Bob");
        System.out.println(map);

        map.remove("Rick");
        System.out.println(map);

        System.out.println(map.get("Bob"));

        System.out.println(map.get("Nick"));


    }
}

```

#### Python version of quick sort 

```python

if __name__ == "__main__":

      names = ["Bob", "Rick", "Aaron", "Nick", "Zoo", "Zze", "Ann", "Joe", "Sam", "Dora", "Maya"]
      def quick_sort(array) :
        if len(array) < 2 :
            return array

        pivot = array[0]
        less = [ item for item in array if item < pivot]
        more = [ item for item in array if item > pivot]
        return less + [pivot] + more


      sorted = quick_sort(names)

      print(sorted)
```

#### Scala version of quick sort

```scala 

package rick.scala


object Main extends App {

  val names = Array("Bob", "Rick", "Aaron", "Nick", "Zoo", "Zze", "Ann", "Joe", "Sam", "Dora", "Maya")
  def quickSort(array: Array[String]): Seq[String] = if (array.size < 2) array else {
    val pivot = array.head
    val less = (for (item <- array if item < pivot) yield item)
    val more = for (item <- array if item > pivot) yield item
    less ++ Array(pivot) ++ more
  }

  val sorted = quickSort(names)

  println(sorted)

}
```

#### A few sort algorithms 


```java

package rick;

import java.util.*;


import static java.lang.System.out;


public class SortMain {

    public static void main(String[] args) {
        Sort qSorter = new QuickSort();
        Sort mergeSorter = new MergeSort();
        Sort comboSorter = new QuickMergeSort(2);
        List<Sort> sorters = Arrays.asList(qSorter, mergeSorter, comboSorter);
        sorters.forEach(sorter -> {
            Collections.shuffle(Arrays.asList(names));
            //out.printf("Before sorter=%s, %s\n", sorter.getClass().getSimpleName(), Arrays.toString(names));
            sorter.sort(names);
            out.printf("After sorter=%s, %s\n", sorter.getClass().getSimpleName(), Arrays.toString(names));
        });
    }


    private static String[] names = {"Bob", "Rick", "Aaron", "Nick", "Zoo", "Zze", "Ann", "Joe", "Sam", "Dora", "Maya"};

    public static interface Sort {
        void sort(String[] array);
    }


    static void mergeSortToOutput(String[] leftArray, String[] rightArray, String[] outputArray, int offset) {


        int idxLeft = 0;
        int idxRight = 0;
        int index = offset;

        while (idxLeft < leftArray.length && idxRight < rightArray.length) {
            String leftValue = leftArray[idxLeft];
            String rightValue = rightArray[idxRight];
            if (leftValue.compareTo(rightValue) < 0) {
                outputArray[index] = leftArray[idxLeft];
                idxLeft++;
            } else {
                outputArray[index] = rightArray[idxRight];
                idxRight++;
            }
            index++;
        }

        while (idxLeft < leftArray.length) {
            outputArray[index] = leftArray[idxLeft];
            idxLeft++;
            index++;
        }

        while (idxRight < rightArray.length) {
            outputArray[index] = rightArray[idxRight];
            idxRight++;
            index++;
        }
    }



    public static class MergeSort implements Sort {

        @Override
        public void sort(String[] array) {
            mergeSort(array, 0, array.length - 1);
        }

        void mergeSort(String[] array, int leftIndex, int rightIndex) {
            if (leftIndex < rightIndex) {
                int middleIndex = (leftIndex + rightIndex) / 2;

                mergeSort(array, middleIndex + 1, rightIndex);
                mergeSort(array, leftIndex, middleIndex);

                merge(array, leftIndex, rightIndex);
            }
        }

        void merge(String[] array, int leftIndex, int rightIndex) {

            int middleIndex = (leftIndex + rightIndex) / 2;


            String[] leftArray = new String[middleIndex - leftIndex + 1];
            String[] rightArray = new String[rightIndex - middleIndex];

            System.arraycopy(array, leftIndex, leftArray, 0, leftArray.length);
            System.arraycopy(array, middleIndex + 1, rightArray, 0, rightArray.length);

            mergeSortToOutput(leftArray, rightArray, array, leftIndex);

        }




    }

    public static class QuickSort implements Sort {



        @Override
        public void sort(String[] array) {
            quickSort(array, 0, array.length - 1);
        }

        static int quickPartition(String[] array, final int startIndex, final int untilIndex) {


            String pivotValue = array[untilIndex];
            int leftIndex = (startIndex - 1);
            for (int rightIndex = startIndex; rightIndex < untilIndex; rightIndex++) {
                String currentValue = array[rightIndex];
                if (currentValue.compareTo(pivotValue) < 0) {
                    leftIndex++;

                    if (leftIndex != rightIndex)
                        swap(array, leftIndex, rightIndex, currentValue);
                }
            }
            swap(array, leftIndex + 1, untilIndex, array[untilIndex]);


            return leftIndex + 1;
        }

        private static void swap(String[] array, int leftIndex, int rightIndex, String currentValue) {


            String leftValue = array[leftIndex];
            //out.printf("SWAP %s, %s, %s, %s \n", leftIndex, rightIndex, currentValue, leftValue);
            array[leftIndex] = currentValue;
            array[rightIndex] = leftValue;
        }


        void quickSort(String[] array, final int startIndex, final int untilIndex) {
            if (startIndex < untilIndex) {

//                out.println("Before quickSort Call " +  " for " +
//                        " start " + startIndex + " until " + untilIndex + " " + " pivot value "
//                        + Arrays.toString(names));

                int partitionIndex = quickPartition(array, startIndex, untilIndex);

                quickSort(array, startIndex, partitionIndex - 1);
                quickSort(array, partitionIndex + 1, untilIndex);
//                out.println("After quickSort Call " +  " for " +
//                        " start " + startIndex + " until " + untilIndex + " " + " pivot value "
//                        + Arrays.toString(names));
            }


        }
    }


    public static class QuickMergeSort implements Sort {


        private final int partitions;

        private final Sort sorter = new QuickSort();

        public QuickMergeSort(int partitions) {
            this.partitions = partitions;
        }

        @Override
        public void sort(String[] array) {

            int sizeOfEachArray = array.length / partitions;
            int leftOver = array.length % partitions;

            String[][] arrays = new String[partitions][];

            int used = 0;

            for (int count =0; count < partitions; count++) {

                final int startIndex = count * sizeOfEachArray;
                final int endIndex;
                final int size;

                if (count +1 == partitions) {
                    endIndex =  startIndex+sizeOfEachArray + leftOver;
                    size = sizeOfEachArray + leftOver;

                } else {
                    endIndex = startIndex+sizeOfEachArray;
                    size = sizeOfEachArray;
                }

                arrays[count] = new String[size];
                System.arraycopy(array, startIndex, arrays[count], 0, size);

                sorter.sort(arrays[count]);

                //out.printf("%s %s %s %s\n", startIndex, endIndex, size, Arrays.toString(arrays[count]));

            }


            for (int count =1; count < partitions; count++) {
                var leftArray = arrays[count-1];
                var rightArray = arrays[count];

                final int offset = (count-1) * sizeOfEachArray;

                mergeSortToOutput(leftArray, rightArray, array, offset);
            }
        }
    }




}

```