package geometries;

import primitives.*;

import java.util.List;

/**
 * Class Sphere is the  class representing a sphere of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */
public class Sphere extends RadialGeometry {
    private final Point center;

    /**
     * Constructor to initialize Sphere with radius and point
     * @param r a radius
     * @param p a point
     */
    public Sphere(double r,Point p) {
        super(r);
        center=p;
    }


    public Vector getNormal(Point p){
       return ((p).subtract(this.center)).normalize();
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
