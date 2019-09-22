package rick.map;

import java.util.Arrays;

public class HashMap implements Map {

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
                newMap.put(kv.getKey(), kv.getValue());
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
