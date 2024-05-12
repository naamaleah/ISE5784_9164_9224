package geometries;
import primitives.*;

/**
 * An interface that represents some geometric body
 */
public interface Geometry {

    /**
     *Calculates and returns the normal
     * @param p A point to calculate the normal
     * @return normal
     */
    public Vector getNormal(Point p);
}