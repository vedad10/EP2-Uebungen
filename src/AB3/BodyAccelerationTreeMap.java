package AB3;

import AB1.Vector3;
import AB2.Body;

/**
 * This data structure maps bodies to vectors ('Vector3' objects). It is implemented
 * as a binary search tree where bodies are sorted based on their masses.
 * The map allows multiple bodies with the same mass as long as they are not identical.
 * There is no limit on the number of key-value pairs stored in the map.
 */
//
// TODO: define further classes and methods for the implementation of the binary search tree, if
//  needed.

    class MyTreeNode {

        Body key;
        Vector3 value;
        MyTreeNode left, right;

        public MyTreeNode (Body key, Vector3 value) {
            this.key = key;
            this.value = value;
        }

        public Vector3 get(Body key) {
            if (this.key.getMass() == key.getMass() && this.key.equals(key)) return this.value;
            else if (this.key.getMass() > key.getMass()) {
                if (this.left != null) return this.left.get(key);
                else return null;
            } else {
                if (this.right != null) return this.right.get(key);
                else return null;
            }
        }

        public Vector3 put (Body key, Vector3 value) {
            if (this.key.getMass() == key.getMass()) {
                Vector3 oldValue = this.value;

                this.value = value;
                return oldValue;
            } else if (this.key.getMass() > key.getMass()) {
                if (this.left != null) {
                    return this.left.put(key, value);
                } else {
                    this.left = new MyTreeNode(key, value);
                    return null;
                }

            } else {

                if (this.right != null) {
                    return this.right.put(key, value);
                } else {
                    this.right = new MyTreeNode(key, value);
                    return null;
                }
            }
        }

}
public class BodyAccelerationTreeMap {

    //TODO: declare variables.
    private MyTreeNode root;
    private String reps;

    /**
     * Adds a new key-value association to this map. If the key already exists in this map,
     * the value is replaced and the old value is returned. Otherwise, 'null' is returned.
     * @param key a body != null.
     * @param value the vector to be associated with the key (can also be 'null').
     * @return the old value if the key already existed in this map, or 'null' otherwise.
     */
    public Vector3 put(Body key, Vector3 value) {

        // TODO: implement method.
        if (root == null){
            root = new MyTreeNode(key, value);
            return null;
        }
        Vector3 res = root.put(key, value);
        return res;
    }

    /**
     * Returns the value associated with the specified key, i.e. the vector
     * associated with the specified body. Returns 'null' if the key is not contained in this map.
     * @param key a body != null.
     * @return the value associated with the specified key (or 'null' if the key is not contained in
     * this map).
     */
    public Vector3 get(Body key) {

        // TODO: implement method.
        return root.get(key);
    }

    /**
     * Returns 'true' if this map contains a mapping for the specified key.
     * @param key a body != null.
     * @return 'true' if this map contains a mapping for the specified key, or 'false' otherwise.
     */
    public boolean containsKey(Body key) {

        // TODO: implement method.
        return get(key) != null;
    }

    /**
     *  Returns a readable representation of this map, in which key-value pairs are listed in
     *  descending order according to the mass of the bodies.
     */
    public String toString() {

        // TODO: implement method.
        reps = "";
        read(root);
        return reps;
    }


// TODO: define further classes, if needed (either here or in a separate file).
public void read(MyTreeNode root) {
    if (root != null) {
        read(root.right);
        this.reps += "" + root.key.toString() + "   " + root.value.toString() + "\n";
        read(root.left);
    }
}
}