package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        return null;
    }
}
