package AB4;

/**
 * This data structure maps variables ('IntVar' objects) to constants ('IntConst' objects).
 * It is implemented as a binary search tree where variables are sorted lexicographically according
 * their name using the 'compareTo' method of String. The map allows multiple variables with the
 * same name as long as they are not identical. There is no limit on the number of key-value
 * mappings stored in the map.
 */
//
// TODO: define further classes and methods for the implementation of the binary search tree, if
//  needed.
//

class TreeNode {
    private IntVar key;
    private IntConst value;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(IntVar key, IntConst value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public IntVar getKey() {
        return key;
    }

    public IntConst getValue() {
        return value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public void KeyList(IntVarDoublyLinkedList keys) {
        if(left == null && right == null){
            keys.addLast(key);
            return;
        }
        if(left != null) {
            this.left.KeyList(keys);
        }
        keys.addLast(key);
        if(right != null) {
            this.right.KeyList(keys);
        }
    }

    public IntConst put(IntVar key, IntConst value) {

        if (key == this.key) {
            IntConst oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        if (key.getName().compareTo(this.key.getName()) == -1) {
            if (left == null) {
                left = new TreeNode(key, value);
                return null;
            } else return left.put(key, value);
        } else {
            if (right == null) {
                right = new TreeNode(key, value);
                return null;
            } else return right.put(key, value);
        }
    }

    public boolean containsKey(IntVar key) {
        if (key.getName().compareTo(this.key.getName()) == 0) {
            return true;
        }
        if (key.getName().compareTo(this.key.getName()) == -1) {
            if (left == null) {
                return false;
            }
            return left.containsKey(key);
        } else {
            if (right == null) {
                return false;
            }
            return right.containsKey(key);
        }
    }

    public IntConst get(IntVar key) {
        if (key.getName().compareTo(this.key.getName()) == 0) {
            return value;
        }
        if (key.getName().compareTo(this.key.getName()) == -1) {
            if (left == null) return null;
            return left.get(key);
        } else {
            if (right == null) return null;
            return right.get(key);
        }
    }

    public int size() {
        int index = 1;
        if (right != null) {
            index += right.size();
        }

        if (left != null) {
            index += left.size();
        }
        return index;
    }
}

public class IntVarConstTreeMap {

    //TODO: declare variables.

    private TreeNode root;

    public IntVarConstTreeMap() {

        //TODO: implement constructor.
        this.root = null;
    }

    /**
     * Constructs this map as a copy of the specified 'map'. This map has the same key-value mappings
     * as 'map'. However, if 'map' is changed later, it will not affect 'this' and vice versa.
     *
     * @param map the map from which key-value mappings are copied to this new map, map != null.
     */
    public IntVarConstTreeMap(IntVarConstTreeMap map) {

        //TODO: implement constructor.
        if (map == null) {
            throw new IllegalArgumentException("Provided map cannot be null.");
        }
        this.root = null;
        if (map.root != null) {
            this.root = new TreeNode(map.root.getKey(), map.root.getValue());
            copySubTree(map.root, this.root);
        }
    }

    /**
     * Constructs this map by copying only those key-value mappings from 'map' for which the key is
     * contained in the specified list. Elements of 'toCopy' which are not keys in 'map' are
     * ignored.
     *
     * @param map    the map from which key-value mappings are copied to this new map, map != null.
     * @param toCopy the list of keys specifying which key-value mappings to copy, toCopy != null.
     */
    public IntVarConstTreeMap(IntVarConstTreeMap map, IntVarDoublyLinkedList toCopy) {

        //TODO: implement constructor.
        this.root = null;

        for (int i = 0; i < toCopy.size(); i++) {
            IntVar var = toCopy.pollFirst();
            if (var != null) {
                put(var, map.get(var));
            }
        }
    }

    /**
     * Adds a new key-value association to this map. If the key already exists in this map,
     * the value is replaced and the old value is returned. Otherwise, 'null' is returned.
     *
     * @param key   a variable != null.
     * @param value the constant to be associated with the key (can also be 'null').
     * @return the old value if the key already existed in this map, or 'null' otherwise.
     */
    public IntConst put(IntVar key, IntConst value) {

        //TODO: implement method.
        if (root == null) {
            root = new TreeNode(key, value);
            return null;
        } else {
            return root.put(key, value);
        }
    }

    /**
     * Returns the value associated with the specified key, i.e. the vector
     * associated with the specified body. Returns 'null' if the key is not contained in this map.
     *
     * @param key a variable != null.
     * @return the value associated with the specified key (or 'null' if the key is not contained in
     * this map).
     */
    public IntConst get(IntVar key) {

        //TODO: implement method.
        return root.get(key);
    }

    /**
     * Returns 'true' if this map contains a mapping for the specified key.
     *
     * @param key a variable != null.
     * @return 'true' if this map contains a mapping for the specified key, or 'false' otherwise.
     */
    public boolean containsKey(IntVar key) {

        //TODO: implement method.
        return root.containsKey(key);
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map.
     */
    public int size() {

        //TODO: implement method.
        if (root == null) {
            return 0;
        } else {
            return root.size();
        }
    }

    /**
     * Returns a new list with all the keys of this map. The list is ordered ascending according to
     * the key order relation. (This means that for any two variables x and y, x has a lower
     * index than y in the returned list, if x.getName().compareTo(y.getName()) < 0.
     * If x.getName().compareTo(y.getName()) > 0, y has a lower index than x in the returned list.
     * If there are variables with equal names, they are consecutive entries in the returned list.)
     *
     * @return an ordered list of keys.
     */
    public IntVarDoublyLinkedList keyList() {

        //TODO: implement method.
        IntVarDoublyLinkedList list = new IntVarDoublyLinkedList();
        if (root != null) {
            root.KeyList(list);
        }
        return list;
    }


    // TODO: define further classes, if needed (either here or in a separate file).
    private void copySubTree(TreeNode sourceRoot, TreeNode targetRoot) {
        if (sourceRoot.getLeft() != null) {
            targetRoot.setLeft(new TreeNode(sourceRoot.getLeft().getKey(), sourceRoot.getLeft().getValue()));
            copySubTree(sourceRoot.getLeft(), targetRoot.getLeft());
        }
        if (sourceRoot.getRight() != null) {
            targetRoot.setRight(new TreeNode(sourceRoot.getRight().getKey(), sourceRoot.getRight().getValue()));
            copySubTree(sourceRoot.getRight(), targetRoot.getRight());
        }
    }
}