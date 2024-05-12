package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Class of plain
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
        normal=null;
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

    public Vector getNormal() {
        return normal;
    }
}
