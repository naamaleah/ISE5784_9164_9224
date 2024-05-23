package primitives;

/**
 * Class Point is the basic class representing a point of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */
public class Point {
    /**coordinates */
    protected final Double3 xyz;
    /** Zero point (0,0,0) */
    public static Point ZERO=new Point(0,0,0);

    /**Parameter constructor
     *
     * @param d3 coordinates
     */
    public Point(Double3 d3) {
       xyz=d3;
    }

    /**A constructor that accepts three parameters
     *
     * @param x first number value
     * @param y second number value
     * @param z third number value
     */
   public Point(double x,double y,double z) {
       xyz=new Double3(x,y,z);
    }

    /**Point subtraction returns a vector
     *
     * @param other a point
     * @return vector
     */
    public Vector subtract(Point other){
        return new Vector(this.xyz.subtract(other.xyz));
    }

    /**Adding points returns a point
     *
     * @param vec a vector
     * @return point
     */
    public Point add(Vector vec){
        return new Point(this.xyz.add(vec.xyz));
    }

    /**Returns distance between points squared
     *
     * @param p a point
     * @return distance Squared
     */
    public double distanceSquared(Point p){
        return (xyz.d1-p.xyz.d1)*(xyz.d1-p.xyz.d1)+(xyz.d2-p.xyz.d2)*(xyz.d2-p.xyz.d2)+(xyz.d3-p.xyz.d3)*(xyz.d3-p.xyz.d3);
    }

    /**returns distance between points
     *
     * @param p a point
     * @return distance
     */
    public double distance(Point p){
        return Math.sqrt(this.distanceSquared(p));
    }




    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        return obj instanceof Point other && xyz.equals(other.xyz);
    }

    @Override
    public String toString() {return ""+xyz;}
}
