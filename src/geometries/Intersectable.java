package geometries;

import primitives.*;

import java.util.List;

/**
 * abstract class of Intersect-able
 *
 * @author Naama and Yeela
 */
public abstract class Intersectable {
    /**
     * finds the Intersection points with the geometric form
     *
     * @param ray parameter
     * @return List of points
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }


    /**
     * static internal helper class representing a point on/in a geometric shape
     */
    public static class GeoPoint {
        /**
         * geometric shape
         */
        public Geometry geometry;
        /**
         * point in/on the geometric shape
         */
        public Point point;

        /**
         * constructor
         *
         * @param geometry geometric shape
         * @param point    point on/in geometric shape
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint: " +
                    "geometry: " + geometry +
                    ", point: " + point;
        }
    }

    //NVI pattern

    /**
     * find all intersection {@link  GeoPoint}s between ray and a geometric object
     * calls abstract helper method, each implementing class , implements helper method
     * to return list of intersection {@link GeoPoint}s for that specific geometry
     *
     * @param ray ray towards the object
     * @return immutable list of intersection {@link  GeoPoint}s
     */
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return this.findGeoIntersectionsHelper(ray);
    }

    /**
     * abstract helper method , gets list of intersection {@link GeoPoint}s between a ray and geometry
     * that are closer to rya origin than the upper distance boundary
     * implemented by interface implementing classes,calculating intersection
     * for the specific type of the class's geometry
     *
     * @param ray ray towards the object
     * @return immutable list of intersection {@link  GeoPoint}s
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);
}
