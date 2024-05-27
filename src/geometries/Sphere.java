package geometries;

import primitives.*;

import java.util.List;

import static java.lang.Math.sqrt;
import static primitives.Util.alignZero;

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
        Point P0 = ray.getHead();
        Vector v = ray.getDirection();
        if (P0.equals(center))
            return List.of( center.add(v.scale(radius)));
        Vector u=center.subtract(P0);
        double tm=alignZero(v.dotProduct(u));
        double d=alignZero(sqrt(u.lengthSquared()-tm*tm));
        if(d>=radius) return null;
        double th=alignZero(sqrt(this.radius*this.radius-d*d));
        double t1=alignZero(tm-th);
        double t2=alignZero(tm+th);
        if(alignZero(t1)<=0)
           return List.of(P0.add(v.scale(t2)));
        if(alignZero(t2)<=0)
            return List.of(P0.add(v.scale(t1)));
        return List.of(P0.add(v.scale(t1)),P0.add(v.scale(t2)));

    }
}
