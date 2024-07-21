package AB8;

import java.util.*;

/**
 * Represents a hierarchical system consisting of more than one subsystem (i.e., implying more
 * than one body).
 */

public class MultiBody  implements HierarchicalSystem
{

    private List <HierarchicalSystem> systemContainer = new ArrayList<>();

    /**
     * Initializes this system with more than one subsystem.
     *
     * @param subsystems an array of components of this system (bodies or subsystems),
     *           subsystems != null && subsystems.length > 1.
     *           Refer to Java Varargs documentation for more details:
     *           https://docs.oracle.com/javase/8/docs/technotes/guides/language/varargs.html
     */
    public MultiBody(HierarchicalSystem... subsystems) {

        for (HierarchicalSystem system : subsystems) {
            systemContainer.add(system);
        }

    }

    @Override
    public String getName() {
        double minDistance = Double.MAX_VALUE;
        HierarchicalSystem nearest = null;
        Vector3 barycenter = getCenter();

        for (HierarchicalSystem system : systemContainer) {
            Vector3 position = system.getCenter();
            double distance = position.minus(barycenter).length();

            if (distance < minDistance) {
                minDistance = distance;
                nearest = system;
            }
        }

        return nearest.getName();
    }


    @Override
    public double getMass() {
        double totalMass = 0;
        for (HierarchicalSystem system : systemContainer) {
            totalMass += system.getMass();
        }
        return totalMass;
    }

    @Override
    public Vector3 getCenter() {
        Vector3 weightedSum = new Vector3(0, 0, 0);
        double totalMass = 0;

        for (HierarchicalSystem system : systemContainer) {
            double mass = system.getMass();
            Vector3 position = system.getCenter();

            weightedSum = weightedSum.plus(position.times(mass));
            totalMass += mass;
        }

        return weightedSum.times(1/totalMass);
    }

    @Override
    public double getRadius() {
        double maxDistance = 0;
        Vector3 barycenter = getCenter();

        for (HierarchicalSystem system : systemContainer) {
            Vector3 position = system.getCenter();
            double distance = position.minus(barycenter).length();
            double totalDistance = distance + system.getRadius();

            if (totalDistance > maxDistance) {
                maxDistance = totalDistance;
            }
        }

        return maxDistance;
    }

    @Override
    public int getNumberOfBodies() {
        int totalBodies = 0;
        for (HierarchicalSystem system : systemContainer) {
            totalBodies += system.getNumberOfBodies();
        }
        return totalBodies;
    }

    @Override
    public Deque<Body> asOrderedList(SystemComparator comp) {

        List<Body> bodyList = new ArrayList<>();

        for (HierarchicalSystem system : systemContainer) {
            if (system instanceof Body) {
                bodyList.add((Body) system);
            } else if (system instanceof MultiBody) {
                bodyList.addAll(system.asOrderedList(comp));
            }
        }

        bodyList.sort(comp);
        return new ArrayDeque<>(bodyList);
    }

    @Override
    public BodyIterator iterator() {
        return new multiBodyIter(this);
    }

    //Done: Edit this
    @Override
    public String toString() {
        List<HierarchicalSystem> sortedListByDistance = new LinkedList<>(systemContainer);
        sortedListByDistance.sort(new DistanceComparator(getCenter()));
        String res = "";
        boolean firstElement = true;

        for (HierarchicalSystem bodyOrSubsystem : sortedListByDistance) {
            if (bodyOrSubsystem.getClass() == Body.class) {
                res += (firstElement ? "" : "   ") + bodyOrSubsystem + "\n";
                firstElement = false;
            } else {
                String resFromSubsystem = bodyOrSubsystem.toString();
                String[] allLines = resFromSubsystem.lines().toArray(String[]::new);
                for (String line : allLines) {
                    res += "   " + line + "\n";
                }
            }
        }
        return res;
    }

    public void removeBody(Body currentlyReturned) {
        for (HierarchicalSystem system : systemContainer) {
            if (system instanceof Body && system.equals(currentlyReturned)) {
                systemContainer.remove(system);
                return;
            } else if (system instanceof MultiBody) {
                ((MultiBody) system).removeBody(currentlyReturned);
            }
        }
    }
}

class multiBodyIter implements BodyIterator {
    private Deque <Body> subsystems;
    private Body currentlyReturned;
    private MultiBody multiBody;


    public multiBodyIter(MultiBody system) {
        multiBody = system;
        subsystems = system.asOrderedList(new MassComparator());
        currentlyReturned = null;

    }
    @Override
    public boolean hasNext() {
        return !subsystems.isEmpty();
    }

    @Override
    public Body next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentlyReturned = subsystems.pollFirst();
        return currentlyReturned;
    }

    @Override
    public void remove() {
        if (currentlyReturned == null) {
            throw new IllegalStateException("Remove can only be called once per call.");
        }
        multiBody.removeBody(currentlyReturned);
        currentlyReturned = null;



    }
}

