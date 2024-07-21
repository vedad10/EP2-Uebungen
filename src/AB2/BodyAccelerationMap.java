package AB2;
import AB1.Vector3;

/**
 * A map that associates a body with an acceleration vector. The number of
 * key-value pairs is not limited.
 */
public class BodyAccelerationMap {

    //TODO: declare variables.
    private Body[] bodiesArray;
    private Vector3[] accelerationArray;

    /**
     * Initializes this map with an initial capacity.
     * @param initialCapacity specifies the size of the internal array. initialCapacity > 0.
     */
    public BodyAccelerationMap(int initialCapacity) {

        //TODO: define constructor.
        bodiesArray = new Body[initialCapacity];
        accelerationArray = new Vector3[initialCapacity];
    }

    /**
     * Returns the value associated with the specified key, i.e. the acceleration vector
     * associated with the specified body. Returns 'null' if the key is not contained in this map.
     * @param key a body != null.
     * @return the value associated with the specified key (or 'null' if the key is not contained in
     * this map).
     */
    public Vector3 get(Body key) {

        //TODO: implement method.
        for (int i = 0; i < accelerationArray.length; i++) {
            if(bodiesArray[i] == null) return null;

            if(bodiesArray[i].getMass() == key.getMass()) {
                return accelerationArray[i];
            }
        }
        return null;
    }

    /**
     * Adds a new key-value association to this map. If the key already exists in this map,
     * the value is replaced and the old value is returned. Otherwise, 'null' is returned.
     * @param key a body != null.
     * @param acceleration the acceleration vector to be associated with the key.
     * @return the old value if the key already existed in this map, or 'null' otherwise.
     */
    public Vector3 put(Body key, Vector3 acceleration) {

        //TODO: implement method.

        //first case: Key already exists, return old acceleration value

        if(get(key) != null) {
            for (int i = 0; i < bodiesArray.length; i++) {
                if(bodiesArray[i].getMass() == key.getMass()) {
                    Vector3 oldValue = accelerationArray[i];
                    accelerationArray[i] = acceleration;
                    return oldValue;
                }
            }
        }

        //second case: Add new key, return null

        else{
            for (int i = 0; i < bodiesArray.length; i++) {
                if(bodiesArray[i] == null) {
                    bodiesArray[i] = key;
                    accelerationArray[i] = acceleration;
                    sort();
                    return null;
                }
            }
        }
        return null;
    }

    //My methods:

    /**
     * Sorts the {@code bodies} array in ascending order of mass and rearranges the {@code forces} array
     * accordingly to maintain association. Utilizes the bubble sort algorithm for sorting.
     *
     * <p>Assumes non-null elements in {@code bodies} and equal lengths of {@code bodies} and {@code forces}.
     * Inefficient for large datasets due to O(n^2) time complexity.</p>
     */
    public void sort (){

        Vector3 temporaryAcceleration;
        Body temporaryBody;

        for (int i = 0; i < bodiesArray.length; i++) {
            for (int j = i + 1; j < bodiesArray.length; j++) {
                if(bodiesArray[i] != null && bodiesArray[j] != null && bodiesArray[i].getMass() > bodiesArray[j].getMass()) {
                    temporaryBody = bodiesArray[i];
                    temporaryAcceleration = accelerationArray[i];

                    bodiesArray[i] = bodiesArray[j];
                    bodiesArray[j] = temporaryBody;

                    accelerationArray[i] = accelerationArray[j];
                    accelerationArray[j] = temporaryAcceleration;
                }
            }
        }
    }
}
