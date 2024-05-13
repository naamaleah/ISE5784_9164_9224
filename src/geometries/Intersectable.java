package geometries;

import primitives.*;

import java.util.List;

/**
 * interface of Intersectable
 */
public interface Intersectable {
    /**
     * finds the Intersection points with the geometric form
     * @param ray parameter
     * @return List of points
     */
   public List<Point> findIntsersections(Ray ray);
}
