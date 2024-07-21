package AB7;

import java.util.*;

/**
 * Represents a hierarchical system consisting of more than one subsystem (i.e., implying more
 * than one body).
 */
//
// TODO: Complete this class definition. You can use the Java-Collection-Framework.
//       You can define further classes and methods for the implementation.
//
public class MultiBody implements HierarchicalSystem
{

    //TODO: private variables and methods if needed.
    //TODO: missing parts of this class.
    private HierarchicalSystem[] subsystems;

    /**
     * Initializes this system with more than one subsystem.
     *
     * @param subsystems an array of components of this system (bodies or subsystems),
     *           subsystems != null && subsystems.length > 1.
     *           Refer to Java Varargs documentation for more details:
     *           https://docs.oracle.com/javase/8/docs/technotes/guides/language/varargs.html
     */
    public MultiBody(HierarchicalSystem... subsystems) {

        //TODO: implement constructor.
        this.subsystems = subsystems;
    }

    @Override
    public String getName() {
        String name = "";

        for(HierarchicalSystem h : subsystems) {
            name += h.getName() + ", ";
        }

        return name.substring(0, name.length()-2); // -2 = comma and space.
    }

    @Override
    public double getMass() {
        double totalMass = 0;
        for (int i = 0; i < subsystems.length; i++) {
            totalMass += subsystems[i].getMass();
        }
        return totalMass;
    }


    @Override
    public Vector3 getCenter() {
        Vector3 weightedSum = new Vector3(0, 0, 0);
        double totalMass = getMass();
        for (HierarchicalSystem s : subsystems) {
            weightedSum = weightedSum.plus(s.getCenter().times(s.getMass()));
        }
        return weightedSum.times(1 / totalMass);
    }

    @Override
    public double getRadius() {
        Vector3 barycenter = this.getCenter();
        double maxcenter = 0;

        for (int i = 0; i < subsystems.length; i++) {
            double center = subsystems[i].getCenter().distanceTo(barycenter) + subsystems[i].getRadius();
            if (center > maxcenter) maxcenter = center;
        }
        return maxcenter;
    }

    @Override
    public int getNumberOfBodies() {
        int count = 0;
        for (HierarchicalSystem system : subsystems) {
            count += system.getNumberOfBodies();
        }
        return count;
    }

    @Override
    public Deque<Body> asOrderedList(SystemComparator comp) {
        List<Body> list = new ArrayList<>();
        for (HierarchicalSystem s : subsystems) {
            if (s instanceof Body) {
                list.add((Body) s);
            } else {
                list.addAll(s.asOrderedList(comp));
            }
        }
        list.sort(comp);
        return new ArrayDeque<>(list);
    }

    @Override
    public BodyIterator iterator() {
        return new HierarchicalIterator(this);
    }

    @Override
    public String toString() {
        Arrays.sort(subsystems, Comparator.comparingDouble(s -> s.getCenter().distanceTo(this.getCenter())));
        StringBuilder s = new StringBuilder();
        int currentLevel = 1;
        for (HierarchicalSystem system : subsystems) {
            s.append(recursiveToString(system, currentLevel));
        }
        return s.toString();
    }

    private String recursiveToString(HierarchicalSystem system, int level) {
        StringBuilder result = new StringBuilder();
        String indent = "   ".repeat(level);
        if (system instanceof Body) {
            result.append(indent).append(system).append("\n");
        }

        if (system instanceof MultiBody multiBody) {
            for (int i = 0; i < multiBody.subsystems.length; i++) {
                if (i == 0) {
                    result.append(indent).append(multiBody.subsystems[i].toString()).append("\n");
                } else {
                    result.append(recursiveToString(multiBody.subsystems[i], level + 1));
                }
            }
        }
        return result.toString();
    }
}

// TODO: define further classes, if needed (either here or in a separate file).

class HierarchicalIterator implements BodyIterator {

    private Deque<Body> subsystems;

    public HierarchicalIterator(HierarchicalSystem system) {
        this.subsystems = system.asOrderedList(new MassComparator());
    }

    @Override
    public boolean hasNext() {
        return !subsystems.isEmpty();
    }

    @Override
    public Body next() {
        if (hasNext()) {
            return subsystems.pollFirst();
        }
        throw new NoSuchElementException();
    }
}