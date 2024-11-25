package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Class Cylinder is the  class representing a Cylinder of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Cylinder extends Tube {

    private final double height;

    /**
     * Constructor to initialize Cylinder based on radius,ray and height
     *
     * @param r      for radius
     * @param axis   for ray
     * @param height for  height
     */
    public Cylinder(double r, Ray axis, double height) {
        super(r, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p) {
        Vector direction = this.axis.getDirection();
        Point P0 = this.axis.getHead();

        //given point is on base of cylinder
        if (p.equals(P0) || isZero(p.subtract(P0).dotProduct(direction)))
            return direction.normalize().scale(-1);


        // given point is on top base of the cylinder
        if (p.equals(P0.add(direction.scale(height))) ||
                isZero(p.subtract(P0.add(direction.scale(height))).dotProduct(direction)))
            return direction.normalize();

        // given point is on the circumference of cylinder
        return super.getNormal(p);
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

    /**
     * find intersection points between ray and 3D cylinder
     * @param ray ray towards the sphere
     * @return null
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray ,double maxDistance) {
        return null;
    }
}
