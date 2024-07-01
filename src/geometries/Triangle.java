package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class Triangle is the  class representing a triangle of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Triangle extends Polygon {
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {

        var intersections = plane.findGeoIntersections(ray, maxDistance);
        //there is no intersection at all
        if (intersections == null)
            return null;

        Vector v1 = this.vertices.get(0).subtract(ray.getHead());
        Vector v2 = this.vertices.get(1).subtract(ray.getHead());
        Vector v3 = this.vertices.get(2).subtract(ray.getHead());

        Vector n1 = (v1.crossProduct(v2)).normalize();
        Vector n2 = (v2.crossProduct(v3)).normalize();
        Vector n3 = (v3.crossProduct(v1)).normalize();

        double t1 = alignZero(n1.dotProduct(ray.getDirection()));
        double t2 = alignZero(n2.dotProduct(ray.getDirection()));
        double t3 = alignZero(n3.dotProduct(ray.getDirection()));

        //point in triangle
        if ((t1 < 0 && t2 < 0 && t3 < 0) || (t1 > 0 && t2 > 0 && t3 > 0))

            return List.of(new GeoPoint(this,intersections.getFirst().point));

        //point is not in triangle
        return null;
    }
}
