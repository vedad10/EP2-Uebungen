package AB5;

public class IntVarConstHashMap implements IntVarConstMap {

    private IntVar[] keys;
    private IntConst[] values;
    private int count;

    public IntVarConstHashMap() {
        this.keys = new IntVar[16];
        this.values = new IntConst[16];
        this.count = 0;
    }

    public IntVarConstHashMap(IntVarConstHashMap map) {
        keys = new IntVar[map.keys.length];
        values = new IntConst[map.values.length];
        count = 0;
        for (int i = 0; i < map.keys.length; i++) {
            if (map.keys[i] != null) {
                put(map.keys[i], map.values[i]);
            }
        }
    }

    @Override
    public IntConst get(IntVar key) {
        int index = find(key);
        return keys[index] != null ? values[index] : null;
    }

    @Override
    public IntConst put(IntVar key, IntConst value) {
        int index = find(key);
        if (keys[index] != null) {
            IntConst oldValue = values[index];
            values[index] = value;
            return oldValue;
        }
        keys[index] = key;
        values[index] = value;
        count++;
        if ((count + 1.0) / keys.length > 0.5) {
            reHash();
        }
        return null;
    }

    @Override
    public IntConst remove(IntVar key) {
        int index = find(key);
        if (keys[index] == null) return null;

        IntConst oldValue = values[index];
        keys[index] = null;
        values[index] = null;
        count--;

        for (int i = (index + 1) % keys.length; keys[i] != null; i = (i + 1) % keys.length) {
            IntVar tmpKey = keys[i];
            IntConst tmpValue = values[i];
            keys[i] = null;
            values[i] = null;
            count--;
            put(tmpKey, tmpValue);
        }

        return oldValue;
    }

    @Override
    public boolean containsKey(IntVar key) {
        int index = find(key);
        return keys[index] != null;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public IntVarQueue keyQueue() {
        IntVarQueue queue = new IntVarQueue();
        for (IntVar key : keys) {
            if (key != null) {
                queue.add(key);
            }
        }
        return queue;
    }

    private int find(IntVar key) {
        int hash = Math.abs(key.hashCode()) % keys.length;
        while (keys[hash] != null && !keys[hash].equals(key)) {
            hash = (hash + 1) % keys.length;
        }
        return hash;
    }

    private void reHash() {
        IntVar[] oldKeys = keys;
        IntConst[] oldValues = values;
        keys = new IntVar[oldKeys.length * 2];
        values = new IntConst[oldValues.length * 2];
        count = 0;
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }
}
