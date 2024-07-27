package primitives;

/**
 * Class Vector is the basic class representing a vector of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */

public class Vector extends Point {

    /*
    Static vectors representing the X, Y, and Z axes for convenience
     */
    public static final Vector X = new Vector(1, 0, 0);
    public static final Vector Y = new Vector(0, 1, 0);
    public static final Vector Z = new Vector(0, 0, 1);

    /**A constructor that accepts three values
     *
     * @param x first number value
     * @param y second number value
     * @param z third number value
     */
    public Vector(double x, double y,double z) {
        super(x,y,z);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }

    /**A constructor that accepts Double3 value
     *
     * @param xyz Double3 parameter
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }

    @Override
    public boolean equals (Object obj){
        if(this== obj) return true;
        return obj instanceof Vector other &&  super.equals(other);
    }

    @Override
    public String toString(){ return "->" + super.toString(); }

    /**Adding vectors returns a vector
     *
     * @param v vector parameter
     * @return vector
     */
    public Vector add(Vector v){
        return new Vector(this.xyz.add(v.xyz));
    }

    /**Scaling vectors returns a vector
     *
     * @param scale double
     * @return a vector
     */
    public Vector scale(double scale){
        return new Vector(this.xyz.scale(scale));
    }

    /**Calculation of a scalar product
     *
     * @param v vector parameter
     * @return dotProduct as double
     */
    public double dotProduct(Vector v){
        return xyz.d1*v.xyz.d1+xyz.d2*v.xyz.d2+xyz.d3*v.xyz.d3;
    }

    /**Calculation of vector product
     *
     * @param v vector parameter
     * @return the crossProduct
     */
    public Vector crossProduct (Vector v){
        return new Vector(this.xyz.d2 * v.xyz.d3 - this.xyz.d3  * v.xyz.d2,
                this.xyz.d3  * v.xyz.d1 - this.xyz.d1 * v.xyz.d3 ,
                this.xyz.d1 * v.xyz.d2 - this.xyz.d2 * v.xyz.d1);
    }

    /**The length of the vector squared
     *
     * @return The length of the vector squared
     */
    public double lengthSquared(){
        return  xyz.d1*xyz.d1+xyz.d2*xyz.d2+xyz.d3*xyz.d3;
    }

    /**The length of the vector
     *
     * @return The length of the vector
     */
    public double length(){
        return Math.sqrt(lengthSquared());
    }

    /**Vector normalization
     *
     * @return normalized vector
     */
    public Vector normalize(){
        return new Vector(this.xyz.reduce(length()));
    }

    /**
     * Rotates the vector around the x-axis
     *
     * @param alpha the amount to rotate in degrees
     * @return the current vector
     */
    public Vector rotateX(double alpha) {
        double radianAlpha = alpha * Math.PI / 180;

        double x = getX();
        double y = getY() * Math.cos(radianAlpha) - getZ() * Math.sin(radianAlpha);
        double z = getY() * Math.sin(radianAlpha) + getZ() * Math.cos(radianAlpha);

        return new Vector(x, y, z);
    }

    /**
     * creates a vector perpendicular to the vector.
     *
     * @return the resulting vector
     */
    public Vector makePerpendicularVector() {
        double a = getX(), b = getY(), c = getZ();
        return (a == b && b == c) ? new Vector(0, -a, a).normalize() : new Vector(b - c, c - a, a - b).normalize();
    }

    /**
     * Rotates the vector around the y axis
     *
     * @param alpha the amount to rotate in degrees
     * @return the current vector
     */
    public Vector rotateY(double alpha) {
        double radianAlpha = alpha * Math.PI / 180;

        double x = getX() * Math.cos(radianAlpha) + getZ() * Math.sin(radianAlpha);
        double y = getY();
        double z = -getX() * Math.sin(radianAlpha) + getZ() * Math.cos(radianAlpha);

        return new Vector(x, y, z);
    }


    /**
     * Rotates the vector around the z axis
     *
     * @param alpha the amount to rotate in degrees
     * @return the current vector
     */
    public Vector rotateZ(double alpha) {
        double radianAlpha = alpha * Math.PI / 180;

        double x = getX() * Math.cos(radianAlpha) - getY() * Math.sin(radianAlpha);
        double y = getX() * Math.sin(radianAlpha) + getY() * Math.cos(radianAlpha);
        double z = getZ();

        return new Vector(x, y, z);
    }

}
