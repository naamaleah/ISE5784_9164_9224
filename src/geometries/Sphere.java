package geometries;

import primitives.*;

import java.util.List;

import static java.lang.Math.sqrt;
import static primitives.Util.alignZero;

/**
 * Class Sphere is the  class representing a sphere of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Sphere extends RadialGeometry {
    private final Point center;

    /**
     * Constructor to initialize Sphere with radius and point
     *
     * @param p a point
     * @param r a radius
     */
    public Sphere(Point p, double r) {
        super(r);
        center = p;
    }

    @Override
    public Vector getNormal(Point p) {
        return ((p).subtract(this.center)).normalize();
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {
        Point P0 = ray.getHead();
        Vector v = ray.getDirection();

        if (P0.equals(center))
            return List.of(new GeoPoint(this, ray.getPoint(radius)));

        Vector u = center.subtract(P0);
        double tm = alignZero(v.dotProduct(u));
        double d = alignZero(sqrt(u.lengthSquared() - tm * tm));

        if (d >= radius)
            return null;

        double th = alignZero(sqrt(this.radius * this.radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);


        if (alignZero(t1) <= 0 || alignZero(maxDistance - t1)<0) {
            //There are no intersection points
            if (alignZero(t2) <= 0 || alignZero(maxDistance - t2)<0)
                return null;

            else
                return List.of(new GeoPoint(this,ray.getPoint(t2)));

        } else if (alignZero(t2) <= 0||alignZero(maxDistance - t2)<0) {
            return List.of(new GeoPoint(this,ray.getPoint(t1)));
        } else
            return List.of(new GeoPoint(this,ray.getPoint(t1)), new GeoPoint(this,ray.getPoint(t2)));

    }
}
