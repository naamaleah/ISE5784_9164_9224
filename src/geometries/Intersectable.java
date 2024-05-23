package geometries;

import primitives.*;

import java.util.List;

/**
 * interface of Intersect-able
 */
public interface Intersectable {
    /**
     * finds the Intersection points with the geometric form
     * @param ray parameter
     * @return List of points
     */
   public List<Point> findIntersections(Ray ray);
}
