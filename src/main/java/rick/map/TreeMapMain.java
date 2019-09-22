package rick.map;

import static java.lang.System.*;

public class TreeMapMain {
    public static void main(String[] args) {
        Map map = new TreeMap();
        map.put("E", "E");
        map.put("D", "D");
        map.put("B", "B");
        map.put("A", "A");
        out.println(map);

//        map.put("Andy", "Dog");
//        map.put("Bob", "Monkey");
//        map.put("Babe", "Bird");
//        map.put("Carl", "Horse");
//        map.put("Cam", "Horse");
//        map.put("Aaron", "Dog");

//        out.println(map);
//        map.remove("Bob");
//        System.out.println(map);
//        if (!"Bird".equals(map.get("Babe"))) throw new IllegalStateException("Babe gone");
//        if (!"Horse".equals(map.get("Cam"))) throw new IllegalStateException("Cam gone");
//        if (!"Horse".equals(map.get("Carl"))) throw new IllegalStateException("Carl gone");



//
//       map.remove("Cam");
//        out.println(map);
//
//
//        TreeMap treeMap = (TreeMap) map;
//
//        treeMap.walkMapInOrder(keyValue -> out.println(keyValue.getKey()));
//
//        out.println();
//
//        treeMap.walkMapPreOrder(keyValue -> out.println(keyValue.getKey()));
//
//        out.println();
//
//        treeMap.walkMapPostOrder(keyValue -> out.println(keyValue.getKey()));
//
//        System.out.println(map);
//        System.out.println(map.get("Bob"));
//        System.out.println(map.get("Cam"));
//        System.out.println(map.get("Aaron"));





    }

}
