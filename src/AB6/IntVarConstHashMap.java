package AB6;

import java.util.Arrays;

/**
 * This data structure maps variables ('IntVar' objects) to constants ('IntConst' objects).
 * It is implemented as a hash map. There is no limit on the number of key-value
 * mappings stored in the map. This class implements 'IntVarConstMap'.
 */
public class IntVarConstHashMap implements IntVarConstMap {

    private Node[] hashTable;

    /**
     * Initializes this map as an empty map.
     */
    public IntVarConstHashMap() {
        this.hashTable = new Node[2]; // Initial capacity of the hash table
    }

    /**
     * Initializes this map as an independent copy of the specified map. Later changes of 'this'
     * will not affect 'map' and vice versa.
     */
    public IntVarConstHashMap(IntVarConstHashMap map) {
        this.hashTable = new Node[map.hashTable.length];
        for (int i = 0; i < map.hashTable.length; i++) {
            if (map.hashTable[i] != null) {
                this.hashTable[i] = new Node(map.hashTable[i].getKey(), map.hashTable[i].getValue()); // Copy each entry
            }
        }
    }

    @Override
    public IntConst get(IntVar key) {
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null && hashTable[i].getKey().equals(key)) {
                return hashTable[i].getValue(); // Return the value if key is found
            }
        }
        return null; // Return null if key is not found
    }

    @Override
    public IntConst put(IntVar key, IntConst value) {
        if (hashTable[hashTable.length - 1] != null) {
            doubleSize(); // Double the size of the table if it is full
        }

        int firstEmptyIndex = hashTable.length - 1;
        boolean emptyIndexFound = false;
        for (int i = 0; i < hashTable.length; i++) { // Iterate over the table to find the key or an empty spot
            if (hashTable[i] == null && !emptyIndexFound) {
                emptyIndexFound = true;
                firstEmptyIndex = i; // Remember the first empty spot
            }
            if (hashTable[i] != null && hashTable[i].getKey() != null) {
                if (hashTable[i].getKey().equals(key)) {
                    IntConst oldValue = hashTable[i].getValue();
                    hashTable[i].setValue(value); // Update the value if key is found
                    return oldValue; // Return the old value
                }
            }
        }
        if (hashTable[firstEmptyIndex] == null) {
            hashTable[firstEmptyIndex] = new Node(key, value); // Insert the new key-value pair
        }

        return null; // Return null if a new key-value pair is inserted
    }

    /**
     * Doubles the size of the hash table.
     */
    private void doubleSize() {
        Node[] oldHashTable = this.hashTable;
        this.hashTable = new Node[oldHashTable.length * 2];

        for (int i = 0; i < oldHashTable.length; i++) {
            if (oldHashTable[i] != null) {
                this.hashTable[i] = new Node(oldHashTable[i].getKey(), oldHashTable[i].getValue()); // Rehash all entries
            }
        }
    }

    @Override
    public IntConst remove(IntVar key) {
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null && hashTable[i].getKey().equals(key)) {
                IntConst removedValue = hashTable[i].getValue();
                hashTable[i] = null; // Remove the entry by setting it to null
                return removedValue; // Return the removed value
            }
        }
        return null; // Return null if key is not found
    }

    @Override
    public boolean containsKey(IntVar key) {
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null && hashTable[i].getKey().equals(key)) {
                return true; // Return true if key is found
            }
        }
        return false; // Return false if key is not found
    }

    @Override
    public int size() {
        int count = 0;
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                count++; // Count the number of non-null entries
            }
        }
        return count; // Return the size of the map
    }

    @Override
    public IntVarSet keySet() {
        IntVarSet keySet = new IntVarSet() {
            IntVar[] keys = new IntVar[hashTable.length];

            @Override
            public void add(IntVar var) {
                if (!contains(var)) {
                    for (int i = 0; i < keys.length; i++) {
                        if (keys[i] == null) {
                            keys[i] = var; // Add the key if it's not already present
                            return;
                        }
                    }
                }
            }

            @Override
            public boolean contains(IntVar var) {
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i] != null && keys[i].equals(var)) {
                        return true; // Return true if key is found
                    }
                }
                return false; // Return false if key is not found
            }

            @Override
            public int size() {
                int count = 0;
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i] != null) {
                        count++; // Count the number of non-null keys
                    }
                }
                return count; // Return the size of the set
            }

            @Override
            public IntVarIterator iterator() {
                for (int i = 0; i < hashTable.length; i++) {
                    if (hashTable[i] != null) {
                        add(hashTable[i].getKey()); // Add all keys from the map to the set
                    }
                }
                return new IntVarIterator() {
                    int currentIndex = 0;

                    @Override
                    public boolean hasNext() {
                        return currentIndex < keys.length; // Check if there are more elements to iterate over
                    }

                    @Override
                    public IntVar next() {
                        if (!hasNext()) {
                            return null; // Return null if no more elements
                        }
                        return keys[currentIndex++]; // Return the next element
                    }
                };
            }
        };

        return keySet; // Return the set of keys
    }

    @Override
    public String toString() {
        return "IntVarConstHashMap{" +
                "hashTable=" + Arrays.toString(hashTable) +
                ", initialSize=" + 2 +
                '}';
    }
}

class Node {
    private IntVar key;
    private IntConst value;

    public Node(IntVar key, IntConst value) {
        this.key = key;
        this.value = value;
    }

    public IntVar getKey() {
        return key;
    }

    public IntConst getValue() {
        return value;
    }

    public void setKey(IntVar key) {
        this.key = key;
    }

    public void setValue(IntConst value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
