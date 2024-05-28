package geometries;

import primitives.*;

import java.util.List;

/**
 * Class Tube is the  class representing a tube of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */

public class Tube extends RadialGeometry{

    protected final Ray axis;

    /**
     * Constructor to initialize Tube with radius and ray
     * @param r a radius
     * @param axis a ray
     */
    public Tube(double r, Ray axis) {
        super(r);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point p){
        Vector v=this.axis.getDirection();
        Point p0=this.axis.getHead();
        double t=v.dotProduct(p.subtract(p0));
        Point o=p0.add(v.scale(t));
        return ((p).subtract(o)).normalize();

    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
