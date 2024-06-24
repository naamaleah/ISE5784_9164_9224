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
public class Geometries implements Intersectable {

    final private List<Intersectable> geometries = new LinkedList<Intersectable>();

    /**
     * default constructor
     */
    public Geometries() {
    }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {

        this.geometries.addAll(Arrays.asList(geometries));
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> lst = null;

        for (Intersectable shape : geometries) {
            // get intersection point for each specific item, (item can be either geometry/nested composite of geometries)
            List<Point> PointList = shape.findIntersections(ray);

            // points were found , add to composite's total intersection points list
            if (PointList != null) {
                if (lst == null) {
                    lst = new LinkedList<>();
                }
                lst.addAll(PointList);
            }
        }
        return lst;
    }
}
