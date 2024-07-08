package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Ray {

    private static final double DELTA = 0.1;
    /**
     * Source point
     */
    private final Point head;
    /**
     * Direction vector
     */
    private final Vector direction;

    @Override
    public String toString() {
        return "head:" + head + "direction:" + direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Ray other && head.equals(other.head) && direction.equals(other.direction);
    }

    /**
     * A parameter constructor accepts a point and a vector
     *
     * @param p first parameter Point
     * @param v second parameter Vector
     */
    public Ray(Point p, Vector v) {
        this.head = p;
        this.direction = v.normalize();
    }

    public Ray(Point p0, Vector direction, Vector normal) {
        double res = direction.dotProduct(normal);
        this.head = isZero(res) ? p0 : res > 0 ? p0.add(normal.scale(DELTA)) : p0.add(normal.scale(-DELTA));
        this.direction = direction.normalize();
    }

    /**
     * getter for head
     *
     * @return head
     */
    public Point getHead() {
        return head;
    }

    /**
     * getter for direction
     *
     * @return direction
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns a point on the ray
     *
     * @param t A number to scale with
     * @return Returns a point along the ray
     */
    public Point getPoint(double t) {
        if (isZero(t))
            return head;
        return head.add(direction.scale(t));
    }

    /**
     * find the closest Point to ray origin from a list of GeoPoints
     *
     * @param points list of intersection points
     * @return the closest {@link Point}
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }

    /**
     * find the closest GeoPoint to ray origin from a list of GeoPoints
     *
     * @param pointList list of intersection points
     * @return the closest {@link GeoPoint}
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> pointList) {
        if (pointList == null)
            return null;

        GeoPoint result = null;
        double minDistance = Double.MAX_VALUE;
        double ptDistance;
        for (var pt : pointList) {
            ptDistance = head.distance(pt.point);
            if (ptDistance < minDistance) {
                minDistance = ptDistance;
                result = pt;
            }
        }
        return result;
    }

}
