package rick.map;


public class HashMapMain {


    public static void main(String[] args) {
        Map map = new HashMap(4, 2, 2.0f);
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
