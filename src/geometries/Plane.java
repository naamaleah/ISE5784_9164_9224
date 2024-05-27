package geometries;

import primitives.*;
import java.util.List;

import static primitives.Util.*;

/**
 * Class Plain is the  class representing a plain of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Naama and Yeela
 */
public class Plane implements Geometry {

    private final Point q;
    private final Vector normal;

    /**
     * Constructor to initialize Plane based on three  points
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1,Point p2,Point p3){
        q=p1;
        if(p1.equals(p2)||p2.equals(p3)||p1.equals(p3))
            throw new IllegalArgumentException("All point should be defendant's ");
        Vector v1=p1.subtract(p2);
        Vector v2=p2.subtract(p3);

        try{
            normal=v1.crossProduct(v2).normalize();
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("This three points in the same line!");
        }
    }

    /**
     * Constructor to initialize Plane based a Point and a Vector
     * @param p a point
     * @param v a vector
     */
    public Plane(Point p,Vector v){
        q=p;
        normal=v.normalize();
    }


    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    /**
     * Getter
     * @return normal-field
     */
    public Vector getNormal() {
        return normal;
    }


    @Override
    public List<Point> findIntersections(Ray ray) {

        Point q=this.q;
        Vector n=this.normal;
        Vector v=ray.getDirection();
        Point p0=ray.getHead();
        //ray is on the plane
        if(q.equals(p0))
            return null;

        double nv = n.dotProduct(v);
        // ray direction cannot be parallel to plane orientation
        if (isZero(nv))
            return null;

        double nqp0= alignZero(n.dotProduct(q.subtract(p0)));
        //t is not 0
        if(isZero(nqp0))
            return null;

        double t=(nqp0)/(nv);
        Point p=p0.add(v.scale(t));

        // t must be positive
        if(t<0)
            return null;
        return  List.of(p);
    }
}
