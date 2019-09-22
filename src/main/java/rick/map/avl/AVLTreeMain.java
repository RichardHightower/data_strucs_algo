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
