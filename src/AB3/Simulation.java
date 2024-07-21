package AB3;

import AB1.SpaceDraw;
import AB1.Vector3;

import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

public class Simulation {
    // Constants
    public static final double G = 6.6743e-11;
    public static final double AU = 150e9; // meters
    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters
    public static final int NUMBER_OF_BODIES = 50;
    public static final double OVERALL_SYSTEM_MASS = 30 * SUN_MASS; // kilograms

    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();

        BodyQueue bodyQueue = new BodyQueue();
        BodyAccelerationTreeMap accelerationOfBodyMap = new BodyAccelerationTreeMap();

        Random random = new Random(2024);

        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            bodyQueue.add(new AB2.Body(Math.abs(random.nextGaussian()) * OVERALL_SYSTEM_MASS / NUMBER_OF_BODIES,
                    new Vector3(0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU, 0.2 * random.nextGaussian() * AU),
                    new Vector3(random.nextGaussian() * 5e3, random.nextGaussian() * 5e3, random.nextGaussian() * 5e3)));

        }

        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            BodyQueue bodyQueueCopy = new BodyQueue(bodyQueue);

            BodyQueue mergedQueue = new BodyQueue();
            BodyQueue processQueue = new BodyQueue(bodyQueue);

            while (processQueue.size() > 0) {
                AB2.Body firstBody = processQueue.poll();
                BodyQueue tempQueue = new BodyQueue();

                while (processQueue.size() > 0) {
                    AB2.Body second_body = processQueue.poll();
                    if (firstBody.getMassCenter().distanceTo(second_body.getMassCenter()) <
                            SpaceDraw.massToRadius(second_body.getMass()) + SpaceDraw.massToRadius(firstBody.getMass())) {
                        firstBody = firstBody.merge(second_body);
                    } else {
                        tempQueue.add(second_body);
                    }
                }

                mergedQueue.add(firstBody);

                while (tempQueue.size() > 0) {
                    processQueue.add(tempQueue.poll());
                }
            }

            bodyQueue = new BodyQueue();
            while (mergedQueue.size() > 0) {
                bodyQueue.add(mergedQueue.poll());
            }

            while (bodyQueueCopy.size() > 0) {
                Vector3 accleration = new Vector3(0, 0, 0);
                AB2.Body body1 = bodyQueueCopy.poll();
                BodyQueue bodyQCopy2 = new BodyQueue(bodyQueue);

                while (bodyQCopy2.size() > 0) {
                    AB2.Body body2 = bodyQCopy2.poll();
                    if (body1 != body2) {
                        Vector3 toAdd = body1.acceleration(body2);
                        accleration = accleration.plus(toAdd);
                    }
                }
                accelerationOfBodyMap.put(body1, accleration);
            }

            bodyQueueCopy = new BodyQueue(bodyQueue);

            while (bodyQueueCopy.size() > 0) {
                AB2.Body body1 = bodyQueueCopy.poll();
                Vector3 acceleration = accelerationOfBodyMap.get(body1);
                if (acceleration != null) {
                    body1.accelerate(accelerationOfBodyMap.get(body1));
                }
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);
                // draw new positions
                bodyQueueCopy = new BodyQueue(bodyQueue);

                while (bodyQueueCopy.size() > 0) {
                    AB2.Body body1 = bodyQueueCopy.poll();
                    body1.draw(cd);
                }
                // show new positions
                cd.show();
            }
        }
    }
}