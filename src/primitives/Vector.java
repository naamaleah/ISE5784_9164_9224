package primitives;
/**
 * This class represents a vector
 */
public class Vector extends Point {

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
     * @param d Double3 parameter
     */
    public Vector(Double3 d) {
        super(d);
        if(d.equals(Double3.ZERO))
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
     * @return
     */
    public Vector add(Vector v){
        return new Vector(this.xyz.add(v.xyz));
    }

    /**Scaling vectors returns a vector
     *
     * @param x double prameter
     * @return a vectore
     */
    public Vector scale(double x){
        return new Vector(this.xyz.scale(x));
    }

    /**Calculation of a scalar product
     *
     * @param v vectore parameter
     * @return dotProduct as double
     */
    public double dotProduct(Vector v){
        return xyz.d1*v.xyz.d1+xyz.d2*v.xyz.d2+xyz.d3*v.xyz.d3;
    }

    /**Calculation of vector product
     *
     * @param v vectore parameter
     * @return the crossProduct
     */
    public Vector crossProduct (Vector v){
        return new Vector(this.xyz.d2 * v.xyz.d3 - this.xyz.d3  * v.xyz.d2,this.xyz.d3  * v.xyz.d1 - this.xyz.d1 * v.xyz.d3 ,this.xyz.d1 * v.xyz.d2 - this.xyz.d2 * v.xyz.d1);
    }

    /**The length of the vector squared
     *
     * @return The length of the vector squared
     */
    public double lengthSquared(){
        return dotProduct(this);
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
}
