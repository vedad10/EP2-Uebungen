package AB6;

/**
 * This data structure maps variables ('IntVar' objects) to constants ('IntConst' objects).
 * It is implemented as a binary search tree where variables are sorted lexicographically according
 * to their name using the 'compareTo' method of String. There is no limit on the number of
 * key-value mappings stored in the map.
 */
public class IntVarConstTreeMap implements IntVarConstMap {

    private TreeNode root;
    private int size;

    /**
     * Initializes this map as an empty map.
     */
    public IntVarConstTreeMap() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Constructs this map as a copy of the specified 'map'. This map has the same key-value mappings
     * as 'map'. However, if 'map' is changed later, it will not affect 'this' and vice versa.
     *
     * @param map the map from which key-value mappings are copied to this new map, map != null.
     */
    public IntVarConstTreeMap(IntVarConstTreeMap map) {
        this.root = copyTree(map.root);
        this.size = map.size();
    }

    /**
     * Recursively copies a tree.
     *
     * @param node the root node of the tree to be copied
     * @return the root node of the new tree
     */
    private TreeNode copyTree(TreeNode node) {
        if (node == null) return null;
        TreeNode newNode = new TreeNode(node.getKey(), node.getValue());
        newNode.setLeft(copyTree(node.getLeft()));
        newNode.setRight(copyTree(node.getRight()));
        return newNode;
    }

    @Override
    public IntConst get(IntVar key) {
        if (root == null) return null;
        return root.get(key);
    }

    @Override
    public IntConst put(IntVar key, IntConst value) {
        if (root == null) {
            root = new TreeNode(key, value);
            size++;
            return null;
        } else {
            IntConst oldValue = root.put(key, value);
            if (oldValue == null) {
                size++;
            }
            return oldValue;
        }
    }

    @Override
    public IntConst remove(IntVar key) {
        if (key == null) return null;

        TreeNode parent = null;
        TreeNode current = root;

        // Locate the node to be removed
        while (current != null && !current.getKey().equals(key)) {
            parent = current;
            int cmp = key.getName().compareTo(current.getKey().getName());
            if (cmp < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        if (current == null) {
            return null; // Node with the given key not found
        }

        IntConst oldValue = current.getValue();

        // Node to be deleted has two children
        if (current.getLeft() != null && current.getRight() != null) {
            TreeNode successorParent = current;
            TreeNode successor = current.getRight();
            while (successor.getLeft() != null) {
                successorParent = successor;
                successor = successor.getLeft();
            }
            current.setKey(successor.getKey());
            current.setValue(successor.getValue());
            current = successor;
            parent = successorParent;
        }

        // Node to be deleted has at most one child
        TreeNode child = (current.getLeft() != null) ? current.getLeft() : current.getRight();
        if (current == root) {
            root = child;
        } else if (current == parent.getLeft()) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }

        size--;
        return oldValue;
    }

    @Override
    public boolean containsKey(IntVar key) {
        if (root != null) {
            return root.containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public IntVarSet keySet() {
        return new IntVarSet() {

            IntVar[] keys = new IntVar[root.size()];

            @Override
            public void add(IntVar var) {
                if (!contains(var)) {
                    for (int i = 0; i < keys.length; i++) {
                        if (keys[i] == null) {
                            keys[i] = var;
                            return;
                        }
                    }
                }
            }

            @Override
            public boolean contains(IntVar var) {
                for (IntVar key : keys) {
                    if (key != null && key.equals(var)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public int size() {
                int count = 0;
                for (IntVar key : keys) {
                    if (key != null) {
                        count++;
                    }
                }
                return count;
            }

            @Override
            public IntVarIterator iterator() {
                keys = getAllKeys();
                return new IntVarIterator() {
                    int currentIndex = 0;

                    @Override
                    public boolean hasNext() {
                        return currentIndex < keys.length;
                    }

                    @Override
                    public IntVar next() {
                        if (!hasNext()) {
                            return null;
                        }
                        return keys[currentIndex++];
                    }
                };
            }
        };
    }

    /**
     * Collects all keys in the map.
     *
     * @return an array of all keys in the map
     */
    public IntVar[] getAllKeys() {
        IntVar[] keys = new IntVar[size];
        if (root != null) {
            root.collectKeys(keys, 0);
        }
        return keys;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "empty";
        } else {
            return root.toString();
        }
    }



    // UE 6

    public IntVarSetSimple keySet(IntVar smallerThan){
        IntVar[] allKeys = new IntVar[getAllKeys().length];
        IntVar[] simpleKeys = new IntVar[size];
        for(int i = 0; i < size; i++){
            if(allKeys[i].getName().compareTo(smallerThan.getName()) < 0){
                simpleKeys[i] = allKeys[i];
            }
        }
        //return simpleKeys;
        return null;
    }
}

/**
 * Represents a node in the binary search tree.
 */
class TreeNode {

    private IntVar key;
    private IntConst value;
    private TreeNode left, right;

    public TreeNode(IntVar key, IntConst value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public int collectKeys(IntVar[] keys, int index) {
        if (left != null) {
            index = left.collectKeys(keys, index);
        }

        keys[index++] = this.key;

        if (right != null) {
            index = right.collectKeys(keys, index);
        }

        return index;
    }

    public String toString() {
        String s = "";
        if (left != null) {
            s += left.toString();
        }

        s += this.key.toString() + " " + this.value.toString();

        if (right != null) {
            s += right.toString();
        }
        return s;
    }

    public IntConst put(IntVar key, IntConst value) {
        int comparison = key.getName().compareTo(this.key.getName());

        if (comparison == 0) {
            IntConst oldValue = this.value;
            this.value = value;
            return oldValue;
        } else if (comparison < 0) {
            if (this.left == null) {
                this.left = new TreeNode(key, value);
                return null;
            } else {
                return left.put(key, value);
            }
        } else {
            if (this.right == null) {
                this.right = new TreeNode(key, value);
                return null;
            } else {
                return right.put(key, value);
            }
        }
    }

    public boolean containsKey(IntVar key) {
        int comparison = key.getName().compareTo(this.key.getName());

        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return left != null && left.containsKey(key);
        } else {
            return right != null && right.containsKey(key);
        }
    }

    public IntConst get(IntVar key) {
        int comparison = key.getName().compareTo(this.key.getName());

        if (comparison == 0) {
            return this.value;
        } else if (comparison < 0) {
            return left != null ? left.get(key) : null;
        } else {
            return right != null ? right.get(key) : null;
        }
    }

    public int size() {
        int count = 1;
        if (left != null) {
            count += left.size();
        }
        if (right != null) {
            count += right.size();
        }
        return count;
    }

    public IntVar getKey() {
        return key;
    }

    public void setKey(IntVar key) {
        this.key = key;
    }

    public IntConst getValue() {
        return value;
    }

    public void setValue(IntConst value) {
        this.value = value;
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
}
