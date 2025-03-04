package AB1;

import codedraw.CodeDraw;

/**
 * This class represents vectors in a 3D vector space.
 */
public class Vector3 {

    //TODO: change modifiers.
    private double x;
    private double y;
    private double z;

    //TODO: define constructor.

    public Vector3 (double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Returns the sum of this vector and vector 'v'.
     * @param v another vector, v != null.
     * @return the sum of this vector and vector 'v'.
     */
    public Vector3 plus(Vector3 v) {

        //TODO: implement method.
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    /**
     * Returns the product of this vector and 'd'.
     * @param d the coefficient for the product.
     * @return the product of this vector and 'd'.
     */
    public Vector3 times(double d) {

        //TODO: implement method.
        return new Vector3(this.x * d, this.y * d, this.z * d);
    }

    /**
     * Returns the sum of this vector and -1*v.
     * @param v another vector, v != null.
     * @return the sum of this vector and -1*v.
     */
    public Vector3 minus(Vector3 v) {

        //TODO: implement method.
        return new Vector3 (this.x - v.x, this.y - v.y, this.z - v.z);
    }

    /**
     * Returns the Euclidean distance of this vector to the specified vector 'v'.
     * @param v another vector, v != null.
     * @return the Euclidean distance of this vector to the specified vector 'v'.
     */

    // d = √[ (x2 – x1)2 + (y2 – y1)2]
    public double distanceTo(Vector3 v) {

        //TODO: implement method.
        double xdistance = this.x - v.x;
        double ydistance = this.y - v.y;
        double zdistance = this.z - v.z;
        return Math.sqrt(xdistance * xdistance + ydistance * ydistance + zdistance * zdistance);
    }

    /**
     * Returns the norm of this vector.
     * @return the length (norm) of this vector.
     */

    // distance from the origin (0, 0, 0 in three-dimensional space, or 0, 0 in two-dimensional space) to the point represented by the vector.
    public double length() {

        //TODO: implement method.
        Vector3 start = new Vector3(0, 0, 0);
        double length = this.distanceTo(start);
        return length;
    }

    /**
     * Normalizes this vector: changes the length of this vector such that it becomes 1.
     * The direction of the vector is not affected.
     */
    public void normalize() {

        //TODO: implement method.
        double length = this.length();

        if (length != 0.0) {
            this.x = this.x / length;
            this.y = this.y / length;
            this.z = this.z / length;
        }
    }

    /**
     * Draws a filled circle with a specified radius centered at the (x,y) coordinates of this vector
     * in the canvas associated with 'cd'. The z-coordinate is not used.
     * @param cd the CodeDraw object used for drawing.
     * @param radius the radius > 0.
     */
    public void drawAsFilledCircle(CodeDraw cd, double radius) {

        //TODO: implement method.
        double convertedx = cd.getWidth() * (x + Simulation.SECTION_SIZE / 2) / Simulation.SECTION_SIZE;
        double convertedy = cd.getWidth() * (y + Simulation.SECTION_SIZE / 2) / Simulation.SECTION_SIZE;
        cd.fillCircle(convertedx, convertedy, Math.max(radius, 1.5));

    }

    /**
     * Returns the coordinates of this vector in brackets as a string
     * in the form "[x, y, z]", e.g., "[1.48E11, 0.0, 0.0]".
     * @return 'this' represented as a string.
     */
    public String toString() {

        //TODO: implement method.
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }

    public Vector3 divide(double d) {
        if (d == 0) {
            return new Vector3(0,0,0);
        }
        return new Vector3(this.x / d, this.y / d, this.z / d);
    }
}

