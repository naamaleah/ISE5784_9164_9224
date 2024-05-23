package geometries;

import primitives.*;

import java.util.List;

/**
 * Class of a Sphere
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
