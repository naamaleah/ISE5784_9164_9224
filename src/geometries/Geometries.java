package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Geometries is the  class representing a complex geometric shape of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Naama and Yeela
 */
public class Geometries extends Intersectable {

    final private List<Intersectable> geometries = new LinkedList<Intersectable>();

    /**
     * default constructor
     */
    public Geometries() {
    }

    /**
     * constructor
     *
     * @param geometries list of geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * adds geometries to list of geometries
     *
     * @param geometries list of geometries
     */
    public void add(Intersectable... geometries) {

        this.geometries.addAll(Arrays.asList(geometries));
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> result = null;

        for (Intersectable geometry : geometries) {
            // get intersection point for each specific item, (item can be either geometry/nested composite of geometries)
            List<GeoPoint> geoPoints = geometry.findGeoIntersections(ray);

            // points were found , add to composite's total intersection points list
            if (geoPoints != null) {
                if (result == null) {
                    result = new LinkedList<GeoPoint>();
                }
                result.addAll(geoPoints);
            }
        }
        return result;
    }
}
