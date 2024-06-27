package geometries;

import primitives.*;

/**
 * An interface that represents some geometric body
 *
 * @author Naama and Yeela
 */
public interface Geometry extends Intersectable {

    /**
     * Calculates and returns the normal
     *
     * @param p A point to calculate the normal
     * @return normal
     */
    Vector getNormal(Point p);
}
