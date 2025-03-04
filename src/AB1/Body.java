package AB1;

import codedraw.CodeDraw;

/**
 * This class represents celestial bodies like stars, planets, asteroids, teapots, etc..
 */
public class Body {

    //TODO: change modifiers.
    private double mass;
    private Vector3 massCenter; // position of the center of mass.
    private Vector3 currentMovement;

    //TODO: define constructor.
    public Body (double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.mass = mass;
        this.massCenter = massCenter;
        this.currentMovement = currentMovement;
    }

    /**
     * Returns the distance between the centers of mass of this body and the specified body 'b'.
     * @param b the specified body, b != null.
     * @return the distance between the centers of mass of this body and the specified body 'b'.
     */

    public double distanceTo(Body b) {

        //TODO: implement method.
        return this.massCenter.distanceTo(b.massCenter);
    }

    /**
     * Returns the acceleration vector of 'this' that results from the gravitational interaction
     * with the specified body 'b'. The returned vector is computed according to Newton's laws of
     * gravitation.
     * Hint: see method 'acceleration' in Simulation.java to find out how this is done.
     * @param b the specified body, b != null.
     * @return the acceleration vector.
     */
    public Vector3 acceleration(Body b) {

        //TODO: implement method.
        Vector3 direction = b.massCenter.minus(this.massCenter);
        double distance = distanceTo(b);
        direction.normalize();
        double length = 6.6743e-11 * b.mass / (distance * distance);
        return direction.times(length);
    }

    /**
     * Accelerates this body for one second according to the specified 'acceleration' vector
     * and updates the current movement accordingly.
     * Hint: see method 'accelerate' in Simulation.java to find out how this is done.
     * @param acceleration the acceleration vector, acceleration != null.
     */
    public void accelerate(Vector3 acceleration) {

        //TODO: implement method.
        this.currentMovement = this.currentMovement.plus(acceleration);
        this.massCenter = this.massCenter.plus(this.currentMovement);
    }

    /**
     * Returns the approximate radius of this body.
     * (It is assumed that the radius r is related to the mass m of the body by
     * r = Math.pow(m, 0.5), where m and r measured in solar mass units.)
     * @return the radius of this body.
     */
    public double getRadius() {

        //TODO: implement method.
        return SpaceDraw.massToRadius(mass);
    }

    /**
     * Returns a new body that is formed by the collision of this body and 'b'. The mass of the
     * returned body is the sum of the masses of this body and 'b'. The current movement of the
     * returned body is given by the law of conservation of momentum. (The momentum of the
     * returned body is the sum of the momentums of 'this' and 'b').
     * Hint: see method 'merge' in Simulation.java to find out how this is done.
     * @param b the body with which 'this' is merged, b != null.
     * @return the body being formed by the collision.
     */
    public Body merge(Body b) {

        //TODO: implement method.

        double combinedMass = mass + b.mass;
        Vector3 combinedMassCenter = (massCenter.times(mass).plus(b.massCenter.times(b.mass))).times(1.0 / combinedMass);
        Vector3 combinedCurrentMovement = (currentMovement.times(mass).plus(b.currentMovement.times(b.mass))).times(1.0 / combinedMass);
        Body result = new Body(combinedMass, combinedMassCenter, combinedCurrentMovement);

        return result;
        }


    /**
     * Draws this body to the specified canvas as a filled circle.
     * The radius of the circle corresponds to the radius of the body
     * (use a conversion of the real scale to the scale of the canvas as
     * in 'Simulation.java').
     * Hint: call the method 'drawAsFilledCircle' implemented in 'Vector3'.
     * @param cd the CodeDraw object used for drawing, cd != null.
     */
    public void draw(CodeDraw cd) {

        //TODO: implement method.

        cd.setColor(SpaceDraw.massToColor(mass));
        double radius= Math.max(cd.getWidth()*getRadius()/Simulation.SECTION_SIZE,0.5);
    }

    /**
     * Returns a string with the information about this body including
     * mass, position (mass center) and current movement. Example:
     * "5.972E24 kg, position: [1.48E11, 0.0, 0.0] m, movement: [0.0, 29290.0, 0.0] m/s."
     * @return 'this' represented as a string.
     */
    public String toString() {

        //TODO: implement method.
        return this.mass + "kg, position: [" + this.massCenter.toString() + ", " + this.currentMovement.toString() + "] m/s.";
    }



}

