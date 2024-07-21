package AB8;

/**
 * This class represents a comparator based on the distance to a specified
 * reference point.
 */
public class DistanceComparator implements SystemComparator {

    private Vector3 reference;

    /**
     * Initializes this comparator with the reference point.
     *
     * @param reference the reference point, reference != null.
     */
    public DistanceComparator(Vector3 reference) {

        this.reference = reference;
    }

    /**
     * Returns the reference point of this comparator.
     *
     * @return the reference point of this comparator.
     */
    public Vector3 getReference() {

        return reference;
    }

    @Override
    /**
     * Compares its two systems for order based on the distance of their barycenter to the
     * reference point of this comparator. See further details in 'SystemComparator'.
     *
     * @param s1 the first hierarchical system to be compared, s1 != null.
     * @param s2 the second hierarchical system to be compared, s1 != null.
     * @return a negative integer, zero, or a positive integer as the first argument
     * is less than, equal to, or greater than the second.
     */
    public int compare(HierarchicalSystem s1, HierarchicalSystem s2) {

        double distance1 = Math.abs(s1.getCenter().minus(reference).length());
        double distance2 = Math.abs(s2.getCenter().minus(reference).length());
        return Double.compare(distance1, distance2);
    }
}
