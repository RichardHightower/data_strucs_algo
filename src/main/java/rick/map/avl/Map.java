package rick.map.avl;

public interface Map {
    void put(String key, String value);
    void remove(String key);
    String get(String key);
}
