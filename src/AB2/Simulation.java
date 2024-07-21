package AB2;
import AB1.Vector3;
import codedraw.CodeDraw;
import java.awt.*;
import java.util.Random;

/**
 * Simulates a cluster of bodies.
 */
public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance between earth and sun.
    public static final double AU = 150e9; // meters

    // some further constants needed in the simulation
    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters

    // set some system parameters
    public static final int NUMBER_OF_BODIES = 50;
    public static final double OVERALL_SYSTEM_MASS = 30 * SUN_MASS; // kilograms

    // all quantities are based on units of kilogram respectively second and meter.

    /**
     * The main simulation method using instances of other classes.
     * @param args not used.
     */
    public static void main(String[] args) {

        //TODO: change implementation of this method according to 'Aufgabenblatt2.md'.

        // simulation
        CodeDraw cd = new CodeDraw();

        Random random = new Random(2022);

        BodyQueue bodyQueue = new BodyQueue(NUMBER_OF_BODIES);
        BodyAccelerationMap bodyAccelerationMap = new BodyAccelerationMap(NUMBER_OF_BODIES);

        for(int i=0;i< bodyQueue.getLength();i++) {
            double mass = Math.abs(random.nextGaussian()) * OVERALL_SYSTEM_MASS / bodyQueue.getLength(); // kg
            double massCenterX = 0.2 * random.nextGaussian() * AU;
            double massCenterY = 0.2 * random.nextGaussian() * AU;
            double massCenterZ = 0.2 * random.nextGaussian() * AU;

            double currentMovementX = 0 + random.nextGaussian() * 5e3;
            double currentMovementY = 0 + random.nextGaussian() * 5e3;
            double currentMovementZ = 0 + random.nextGaussian() * 5e3;

            Vector3 massCenter = new Vector3(massCenterX, massCenterY, massCenterZ);
            Vector3 currentMovement = new Vector3(currentMovementX, currentMovementY, currentMovementZ);
            bodyQueue.add(new Body(mass, massCenter, currentMovement));
        }

        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            Body[] tmpBodies = new Body[bodyQueue.getLength()];

            for (int i = 0; i < bodyQueue.getLength(); i++) {
                Body tmp = bodyQueue.poll();
                bodyAccelerationMap.put(tmp, new Vector3(0, 0, 0));
                for(int j = 0; j < tmpBodies.length; j++) {
                    if(i != j){
                        Body tmp2 = bodyQueue.poll();

                        Vector3 accelerationVector = bodyAccelerationMap.get(tmp);

                        accelerationVector = accelerationVector.plus(tmp.acceleration(tmp2));

                        bodyAccelerationMap.put(tmp, accelerationVector);
                        bodyQueue.add(tmp2);
                    }
                }
                bodyQueue.add(tmp);
            }

            for (int i = 0; i < bodyQueue.getLength(); i++) {
                Body bodyI = bodyQueue.poll();
                bodyI.accelerate(bodyAccelerationMap.get(bodyI));
                bodyQueue.add(bodyI);
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                for (Body body : bodyQueue.getBodiesArray()) {
                    body.draw(cd);
                }

                // show new positions
                cd.show();
            }

        }

    }
}

