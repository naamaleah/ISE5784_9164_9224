package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Tube is the  class representing a tube of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */

public class Tube extends RadialGeometry {

    protected final Ray axis;

    /**
     * Constructor to initialize Tube with radius and ray
     *
     * @param r    a radius
     * @param axis a ray
     */
    public Tube(double r, Ray axis) {
        super(r);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        double t  =  axis.getDirection().dotProduct(
                point.subtract(
                        axis.getHead()));
        Point O = ( t == 0 ?axis.getHead() :  axis.getHead().add(
                axis.getDirection().scale(t)));
        return point.subtract(O).normalize();
    }


    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        return null;
    }
}
